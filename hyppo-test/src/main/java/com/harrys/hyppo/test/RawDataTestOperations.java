package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.RawDataIntegration;
import com.harrys.hyppo.source.api.data.AvroRecordAppender;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.task.*;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pettyjamesm on 12/28/15.
 */
public final class RawDataTestOperations<T extends SpecificRecord> {

    private final RawDataIntegration<T> integration;

    private final DataIngestionJob job;

    private final TestOutputFiles files;

    public RawDataTestOperations(final RawDataIntegration<T> integration, final DataIngestionJob job, final File outputRoot){
        this.integration    = integration;
        this.job            = job;
        this.files          = new TestOutputFiles(outputRoot);
    }

    public final CreateIngestionTasks createIngestionTasksOperation(){
        return new CreateIngestionTasks(job);
    }

    public final FetchRawData fetchRawDataOperation(final DataIngestionTask task) {
        return new FetchRawData(task.cloneWithJob(job), files.taskRawDataCollector(task));
    }

    public final RawDataProcessing<T> processRawDataOperations(final DataIngestionTask task, final List<File> files) throws IOException {
        final DataIngestionTask taskJobFix       = task.cloneWithJob(job);
        final AvroRecordAppender<T> appender     = createAvroRecordAppender(taskJobFix);
        final List<ProcessRawData<T>> operations = files.stream()
                .map(f -> new ProcessRawData<>(taskJobFix, f, appender))
                .collect(Collectors.toList());
        return new RawDataProcessing<T>(operations, appender);
    }

    public final PersistProcessedData<T> persistProcessedDataOperation(final DataIngestionTask task, final File avroFile) {
        return new PersistProcessedData<T>(task.cloneWithJob(job), integration.avroType(), avroFile);
    }

    public final HandleJobCompleted handleJobCompletedOperation(final LocalDateTime completedAt, final List<DataIngestionTask> tasks) {
        final List<DataIngestionTask> jobFixed = tasks.stream().map(t -> t.cloneWithJob(job)).collect(Collectors.toList());
        return new HandleJobCompleted(completedAt, job, jobFixed);
    }

    public final AvroRecordAppender<T> createAvroRecordAppender(final DataIngestionTask task) throws IOException {
        return files.createAvroRecordAppender(integration.avroType(), task);
    }


    public final DataIngestionJob getJob(){
        return job;
    }

    public final void cleanupOutputFiles() {
        files.cleanAllFiles();
    }


    public static final class RawDataProcessing<T extends SpecificRecord> {

        private final List<ProcessRawData<T>> operations;

        private final AvroRecordAppender<T> appender;

        private RawDataProcessing(final List<ProcessRawData<T>> operations, final AvroRecordAppender<T> appender) {
            this.operations = operations;
            this.appender   = appender;
        }

        public List<ProcessRawData<T>> getOperations() {
            return operations;
        }

        public AvroRecordAppender<T> getAppender() {
            return appender;
        }
    }
}
