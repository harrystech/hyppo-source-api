package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.RawDataIntegration;
import com.harrys.hyppo.source.api.data.AvroRecordAppender;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.task.*;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.util.ArrayList;
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
        final List<DataIngestionTask> tasks = createIngestionTasks(operations.createIngestionTasksOperation()).stream()
                .map(t -> t.cloneWithJob(operations.getJob()))
                .collect(Collectors.toList());

        final List<DataIngestionTaskWithRawFiles> taskRawFiles = new ArrayList<>();
        for (DataIngestionTask task : tasks){
            final List<File> files = fetchRawData(operations.fetchRawDataOperation(task));
            taskRawFiles.add(new DataIngestionTaskWithRawFiles(task, files));
        }

        final List<DataIngestionTaskWithAvroFile<T>> taskAvro = new ArrayList<>();
        for (DataIngestionTaskWithRawFiles rawTask : taskRawFiles){
            final DataIngestionTask task          = rawTask.getTask();
            final AvroRecordAppender<T> appender  = operations.createAvroRecordAppender(task);
            for (ProcessRawData<T> operation : operations.processRawDataOperations(task, rawTask.getRawDataFiles(), appender)){
                processRawData(operation);
                appender.close();
            }
            taskAvro.add(new DataIngestionTaskWithAvroFile<>(task, appender));
        }

        for (DataIngestionTaskWithAvroFile<T> avroTask : taskAvro){
            persistProcessedData(operations.persistProcessedDataOperation(avroTask.getTask(), avroTask.getAvroAppender().getOutputFile()));
        }

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
}
