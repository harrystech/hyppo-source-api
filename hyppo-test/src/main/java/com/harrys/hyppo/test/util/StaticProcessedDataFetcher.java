package com.harrys.hyppo.test.util;

import com.harrys.hyppo.source.api.data.AvroRecordType;
import com.harrys.hyppo.source.api.task.FetchProcessedData;
import com.harrys.hyppo.source.api.task.ProcessedDataFetcher;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jpetty on 1/5/16.
 */
public final class StaticProcessedDataFetcher<T extends SpecificRecord> implements ProcessedDataFetcher<T> {

    private final Iterable<T> records;

    public StaticProcessedDataFetcher(final Iterable<T> records){
        this.records = records;
    }

    @Override
    public final void fetchProcessedData(final FetchProcessedData<T> operation) throws Exception {
        operation.append(records);
    }


    public static <T extends SpecificRecord> StaticProcessedDataFetcher<T> createWithFile(final AvroRecordType<T> avroType, final File inputFile) throws IOException {
        final DataFileReader<T> reader = avroType.createFileReader(inputFile);
        return new StaticProcessedDataFetcher<>(reader);
    }

    public static <T extends SpecificRecord> StaticProcessedDataFetcher<T> createWithResource(final AvroRecordType<T> avroType, final String resource) throws IOException {
        final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        final DataFileStream<T> reader = new DataFileStream<>(stream, avroType.createDatumReader());
        return new StaticProcessedDataFetcher<>(reader);
    }
}
