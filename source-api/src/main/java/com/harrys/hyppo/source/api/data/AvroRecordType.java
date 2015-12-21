package com.harrys.hyppo.source.api.data;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by jpetty on 7/14/15.
 */
public abstract class AvroRecordType<T extends SpecificRecord> {

    public abstract Class<T> recordClass();

    public abstract Schema recordSchema();

    @SuppressWarnings("unchecked")
    public final T[] createArrayType(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Array size must be > 0. Received: " + size);
        }
        return (T[]) Array.newInstance(this.recordClass(), size);
    }

    public final SpecificDatumWriter<T> createDatumWriter(){
        final SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(this.recordClass());
        writer.setSchema(this.recordSchema());
        return writer;
    }

    public final SpecificDatumReader<T> createDatumReader(){
        final SpecificDatumReader<T> reader = new SpecificDatumReader<>(this.recordClass());
        reader.setSchema(this.recordSchema());
        return reader;
    }

    @SuppressWarnings("unchecked")
    public final SpecificDatumWriter<SpecificRecord> createGeneralDatumWriter(){
        final SpecificDatumWriter<T> writer = this.createDatumWriter();
        return (SpecificDatumWriter<SpecificRecord>)writer;
    }

    @SuppressWarnings("unchecked")
    public final SpecificDatumReader<SpecificRecord> createGeneralDatumReader(){
        final SpecificDatumReader<T> reader = this.createDatumReader();
        return (SpecificDatumReader<SpecificRecord>)reader;
    }

    public final DataFileWriter<T> createFileWriter(final File outputFile, final CodecFactory codec) throws IOException {
        return new DataFileWriter<>(this.createDatumWriter()).setCodec(codec).create(this.recordSchema(), outputFile);
    }

    public final DataFileReader<T> createFileReader(final File inputFile) throws IOException {
        return new DataFileReader<T>(inputFile, this.createDatumReader());
    }

    public final DataFileWriter<SpecificRecord> createGeneralFileWriter(final File outputFile, final CodecFactory codec) throws IOException {
        return new DataFileWriter<SpecificRecord>(this.createGeneralDatumWriter()).setCodec(codec).create(this.recordSchema(), outputFile);
    }

    public final DataFileReader<SpecificRecord> createGeneralFileReader(final File inputFile) throws IOException {
        return new DataFileReader<SpecificRecord>(inputFile, this.createGeneralDatumReader());
    }

    public final AvroRecordAppender<T> createAvroRecordAppender(final File outputFile, final CodecFactory codec) throws IOException {
        return new AvroRecordAppender<>(this, outputFile, codec);
    }


    public static final <T extends SpecificRecordBase> AvroRecordType<T> forClass(final Class<T> recordClass){
        try {
            final Schema schema = recordClass.newInstance().getSchema();
            return new SimpleAvroRecordType<>(schema, recordClass);
        } catch (Exception e){
            throw new IllegalArgumentException("Class " + recordClass.getName() + " can't be instantiated!", e);
        }
    }

    public static final class SimpleAvroRecordType<T extends SpecificRecord> extends AvroRecordType<T> {

        private final Schema recordSchema;

        private final Class<T> recordClass;


        private SimpleAvroRecordType(final Schema recordSchema, final Class<T> recordClass){
            this.recordSchema   = recordSchema;
            this.recordClass    = recordClass;
        }

        @Override
        public final Schema recordSchema(){
            return this.recordSchema;
        }

        @Override
        public final Class<T> recordClass(){
            return this.recordClass;
        }

        @Override
        public final boolean equals(Object check){
            if (check instanceof SimpleAvroRecordType<?>){
                final SimpleAvroRecordType<?> other = (SimpleAvroRecordType<?>)check;
                return (
                        recordSchema.equals(other.recordSchema)
                        && recordClass.equals(other.recordClass)
                );
            } else {
                return super.equals(check);
            }
        }
    }
}
