package com.harrys.hyppo.source.api;

import com.harrys.hyppo.source.api.data.AvroRecordType;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.model.IngestionSource;
import com.harrys.hyppo.source.api.task.HandleJobCompleted;
import com.harrys.hyppo.source.api.task.IngestionTaskCreator;
import com.harrys.hyppo.source.api.task.ProcessedDataPersister;
import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/6/15.
 */
public interface DataIntegration<T extends SpecificRecord> {

    AvroRecordType<T> avroType();

    ValidationResult validateSourceConfiguration(final IngestionSource source);

    ValidationResult validateJobParameters(final DataIngestionJob job);

    ValidationResult validateTaskArguments(final DataIngestionTask task);

    IngestionTaskCreator newIngestionTaskCreator();

    ProcessedDataPersister<T> newProcessedDataPersister();

    /**
     * Allows the integration to handle application specific logic after completion of a job
     * @param details The {@link HandleJobCompleted} instance containing relevant information about the job itself;
     */
    default void onJobCompleted(HandleJobCompleted details){}

}
