package com.harrys.hyppo.source.api.data;

import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by jpetty on 7/16/15.
 */
public final class AvroRecordAppender<T extends SpecificRecord> {

    private final AvroRecordType<T> avroType;

    private long counter;

    private final File outputFile;

    private final DataFileWriter<T> writer;

    public AvroRecordAppender(final AvroRecordType<T> avroType, final File outputFile, final CodecFactory codec) throws IOException {
        this.avroType   = avroType;
        this.outputFile = outputFile;
        this.counter    = 0;
        this.writer     = avroType.createFileWriter(outputFile, codec);
    }

    public final AvroRecordType<T> getAvroType(){
        return avroType;
    }

    public final long getRecordCount(){
        return this.counter;
    }

    public final File getOutputFile(){
        return this.outputFile;
    }

    public final synchronized void append(T record) throws IOException {
        this.writer.append(record);
        this.counter++;
    }

    public final synchronized  void appendAll(Iterable<T> records) throws IOException {
        this.appendAll(records.iterator());
    }

    public final synchronized void appendAll(Iterator<T> records) throws IOException {
        while (records.hasNext()){
            this.writer.append(records.next());
            this.counter++;
        }
    }

    public final synchronized void flush() throws IOException {
        this.writer.flush();
    }

    public final synchronized void close() throws IOException {
        this.writer.close();
    }
}
