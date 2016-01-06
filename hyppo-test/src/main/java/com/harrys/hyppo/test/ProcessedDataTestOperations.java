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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pettyjamesm on 12/28/15.
 */
public final class ProcessedDataTestOperations<R extends SpecificRecord> {

    private final ProcessedDataIntegration<R> integration;

    private final DataIngestionJob job;

    private final TestOutputFiles files;

    public ProcessedDataTestOperations(final ProcessedDataIntegration<R> integration, final DataIngestionJob job, final File outputRoot) {
        this.integration    = integration;
        this.job            = job;
        this.files          = new TestOutputFiles(outputRoot);
    }

    public final CreateIngestionTasks createIngestionTasksOperation() {
        return new CreateIngestionTasks(job);
    }

    public final ProcessedDataFetching<R> fetchProcessedDataOperation(final DataIngestionTask task) throws IOException {
        final DataIngestionTask jobFix = task.cloneWithJob(job);
        final AvroRecordAppender<R> appender  = files.createAvroRecordAppender(integration.avroType(), jobFix);
        final FetchProcessedData<R> operation = new FetchProcessedData<>(jobFix, appender);
        return new ProcessedDataFetching<>(operation, appender);
    }

    public final PersistProcessedData<R> persistProcessedDataOperation(final DataIngestionTask task, final File avroFile) {
        return new PersistProcessedData<>(task.cloneWithJob(job), integration.avroType(), avroFile);
    }

    public final HandleJobCompleted handleJobCompletedOperation(final LocalDateTime completedAt, final List<DataIngestionTask> tasks) {
        final List<DataIngestionTask> jobFixed = tasks.stream().map(t -> t.cloneWithJob(job)).collect(Collectors.toList());
        return new HandleJobCompleted(completedAt, job, jobFixed);
    }

    public final void cleanupOutputFiles() {
        files.cleanAllFiles();
    }


    public final AvroRecordAppender<R> createAvroRecordAppender(final DataIngestionTask task) throws IOException {
        return files.createAvroRecordAppender(integration.avroType(), task);
    }


    public final DataIngestionJob getJob(){
        return job;
    }

    public static final class ProcessedDataFetching<R extends SpecificRecord> {

        private final FetchProcessedData<R> operation;

        private final AvroRecordAppender<R> appender;

        private ProcessedDataFetching(final FetchProcessedData<R> operation, final AvroRecordAppender<R> appender) {
            this.operation = operation;
            this.appender  = appender;
        }

        public FetchProcessedData<R> getOperation() {
            return operation;
        }

        public AvroRecordAppender<R> getAppender() {
            return appender;
        }
    }
}
