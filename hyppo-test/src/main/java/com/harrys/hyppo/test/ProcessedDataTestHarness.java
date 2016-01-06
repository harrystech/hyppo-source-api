package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.ProcessedDataIntegration;
import com.harrys.hyppo.source.api.data.AvroRecordAppender;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.task.CreateIngestionTasks;
import com.harrys.hyppo.source.api.task.FetchProcessedData;
import com.harrys.hyppo.source.api.task.HandleJobCompleted;
import com.harrys.hyppo.source.api.task.PersistProcessedData;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by jpetty on 1/5/16.
 */
public abstract class ProcessedDataTestHarness<T extends SpecificRecord> implements Callable<Void> {

    protected final ProcessedDataIntegration<T> integration;

    protected final ProcessedDataTestOperations<T> operations;

    public ProcessedDataTestHarness(final ProcessedDataIntegration<T> integration, final DataIngestionJob job, final File root){
        this.integration = integration;
        this.operations  = new ProcessedDataTestOperations<>(integration, job, root);
    }

    @Override
    public Void call() throws Exception {
        //  Since it's fairly common for these tasks creations to include stripped DataIngestionJob instances,
        //  we force-reassign it here.
        final List<DataIngestionTask> tasks = createIngestionTasks(operations.createIngestionTasksOperation()).stream()
                .map(t -> t.cloneWithJob(operations.getJob()))
                .collect(Collectors.toList());

        final List<MutableTaskDetail> details = tasks.stream()
                .map(MutableTaskDetail::new)
                .collect(Collectors.toList());

        for (MutableTaskDetail taskDetail : details) {
            final ProcessedDataTestOperations.ProcessedDataFetching<T> pack = operations.fetchProcessedDataOperation(taskDetail.getTask());
            final FetchProcessedData<T> operation = pack.getOperation();
            final AvroRecordAppender<T> appender  = pack.getAppender();
            fetchProcessedData(operation);
            appender.close();
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

    protected void fetchProcessedData(final FetchProcessedData<T> operation) throws Exception {
        integration.newProcessedDataFetcher().fetchProcessedData(operation);
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
