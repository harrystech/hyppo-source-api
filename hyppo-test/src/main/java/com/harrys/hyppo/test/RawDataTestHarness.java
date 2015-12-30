package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.RawDataIntegration;
import com.harrys.hyppo.source.api.data.AvroRecordAppender;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.task.*;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by pettyjamesm on 12/28/15.
 */
public abstract class RawDataTestHarness<T extends SpecificRecord> implements Callable<Void> {

    protected final RawDataIntegration<T> integration;

    protected final RawDataTestOperations<T> operations;

    public RawDataTestHarness(final RawDataIntegration<T> integration, final DataIngestionJob job, final File root){
        this.integration = integration;
        this.operations  = new RawDataTestOperations<>(integration, job, root);
    }

    @Override
    public Void call() throws Exception {
        //  Since it's fairly common for these tasks creations to include stripped DataIngestionJob instances,
        //  we force-reassign it here.
        final List<DataIngestionTask> tasks   = createIngestionTasks(operations.createIngestionTasksOperation()).stream()
                .map(t -> t.cloneWithJob(operations.getJob())).collect(Collectors.toList());
        final List<MutableTaskDetail> details = tasks.stream()
                .map(MutableTaskDetail::new)
                .collect(Collectors.toList());

        for (MutableTaskDetail taskDetail : details){
            final List<File> files = fetchRawData(operations.fetchRawDataOperation(taskDetail.getTask()));
            taskDetail.setRawDataFiles(files);
        }

        for (MutableTaskDetail taskDetail : details){
            final AvroRecordAppender<T> appender = operations.createAvroRecordAppender(taskDetail.getTask());
            for (ProcessRawData<T> operation : operations.processRawDataOperations(taskDetail.getTask(), taskDetail.getRawDataFiles(), appender)){
                processRawData(operation);
                appender.close();
            }
            taskDetail.setAvroDataFile(appender.getOutputFile());
        }

        for (MutableTaskDetail taskDetail : details){
            persistProcessedData(operations.persistProcessedDataOperation(taskDetail.getTask(), taskDetail.getAvroDataFile()));
        }

        onJobCompleted(operations.handleJobCompletedOperation(LocalDateTime.now(Clock.systemUTC()), tasks));

        return null;
    }


    protected List<DataIngestionTask> createIngestionTasks(final CreateIngestionTasks operation) throws Exception {
        integration.newIngestionTaskCreator().createIngestionTasks(operation);
        return operation.getTaskBuilder().build();
    }

    protected List<File> fetchRawData(final FetchRawData operation) throws Exception {
        integration.newRawDataFetcher().fetchRawData(operation);
        return operation.getDataFiles();
    }

    protected void processRawData(final ProcessRawData<T> operation) throws Exception {
        integration.newRawDataProcessor().processRawData(operation);
    }

    protected void persistProcessedData(final PersistProcessedData<T> operation) throws Exception {
        integration.newProcessedDataPersister().persistProcessedData(operation);
    }

    protected void onJobCompleted(final HandleJobCompleted operation) throws Exception {
        integration.onJobCompleted(operation);
    }


    public void cleanupOutputFiles() {
        operations.cleanupOutputFiles();
    }
}
