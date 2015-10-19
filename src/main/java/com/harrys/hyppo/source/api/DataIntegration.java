package com.harrys.hyppo.source.api;

import com.harrys.hyppo.source.api.data.AvroRecordType;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.task.ProcessedDataPersister;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.IngestionSource;
import com.harrys.hyppo.source.api.task.TaskCreator;
import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/6/15.
 */
public interface DataIntegration<T extends SpecificRecord> {

    AvroRecordType<T> avroType();

    ValidationResult validateSourceConfiguration(final IngestionSource source);

    ValidationResult validateJobParameters(final DataIngestionJob job);

    ValidationResult validateTaskArguments(final DataIngestionTask task);

    TaskCreator newIngestionTaskCreator();

    ProcessedDataPersister<T> newDataPersister();
}
