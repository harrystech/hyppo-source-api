package io.ingestion.source.api;

import io.ingestion.source.api.data.AvroRecordType;
import io.ingestion.source.api.model.DataIngestionJob;
import io.ingestion.source.api.model.DataIngestionTask;
import io.ingestion.source.api.model.IngestionSource;
import io.ingestion.source.api.task.ProcessedDataPersister;
import io.ingestion.source.api.task.TaskCreator;
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
