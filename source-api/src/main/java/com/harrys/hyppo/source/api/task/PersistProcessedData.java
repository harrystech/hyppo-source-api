package com.harrys.hyppo.source.api.task;

import com.harrys.hyppo.source.api.data.AvroRecordType;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.model.IngestionSource;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by jpetty on 7/17/15.
 */
public final class PersistProcessedData<T extends SpecificRecord> {

    private final DataIngestionTask task;

    private final AvroRecordType<T> recordType;

    private final File avroFile;

    public PersistProcessedData(final DataIngestionTask task, final AvroRecordType<T> recordType, final File avroFile){
        this.task       = task;
        this.recordType = recordType;
        this.avroFile   = avroFile;
    }

    public final IngestionSource getSource(){
        return this.getJob().getIngestionSource();
    }

    public final DataIngestionJob getJob(){
        return this.task.getIngestionJob();
    }

    public final DataIngestionTask getTask(){
        return this.task;
    }

    public final Iterator<T> openReader() throws IOException {
        final DataFileReader<T> reader = this.recordType.createFileReader(this.avroFile);
        return new FileReaderWrapper(reader);
    }

    private final class FileReaderWrapper implements Iterator<T> {

        private final DataFileReader<T> reader;

        private T instance;

        private FileReaderWrapper(final DataFileReader<T> reader){
            this.reader     = reader;
            this.instance   = null;
        }

        @Override
        public final boolean hasNext() {
            return reader.hasNext();
        }

        @Override
        public final T next() {
            try {
                return reader.next(instance);
            } catch (IOException ioe){
                instance = null;
                throw new AvroRuntimeException(ioe);
            }
        }
    }
}
