package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.data.AvroRecordAppender;
import com.harrys.hyppo.source.api.data.AvroRecordType;
import com.harrys.hyppo.source.api.data.RawDataCollector;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pettyjamesm on 12/28/15.
 */
public final class TestOutputFiles {

    private final File root;

    private final File rawData;

    private final File avroData;

    public TestOutputFiles(final File root) {
        if (!root.isDirectory()){
            throw new IllegalArgumentException("Output root is not a directory: " + root.getAbsolutePath());
        }
        this.root       = root;
        this.rawData    = resolveAndCreateDirectory(root, "raw-data");
        this.avroData   = resolveAndCreateDirectory(root, "avro-data");
    }

    public final File taskRawDataDirectory(final DataIngestionTask task) {
        final String relative = String.format("task-%d", task.getTaskNumber());
        return resolveAndCreateDirectory(rawData, relative);
    }

    public final RawDataCollector taskRawDataCollector(final DataIngestionTask task) {
        return new RawDataCollector(taskRawDataDirectory(task));
    }

    public final File taskAvroDataFile(final DataIngestionTask task) {
        final String relative = String.format("avro-%d", task.getTaskNumber());
        return avroData.toPath().resolve(relative).toFile();
    }

    public final <T extends SpecificRecord> AvroRecordAppender<T> createAvroRecordAppender(final AvroRecordType<T> avroType, final DataIngestionTask task) throws IOException {
        return avroType.createAvroRecordAppender(taskAvroDataFile(task), CodecFactory.deflateCodec(9));
    }

    public final List<File> taskRawDataFiles(final DataIngestionTask task) {
        final File directory = taskRawDataDirectory(task);
        final File[] files   = directory.listFiles();
        if (files == null){
            throw new IllegalArgumentException("Ingestion Task " + task.getTaskNumber() + " does not have raw data directory: " + directory.getAbsolutePath());
        } else {
            return Arrays.asList(files);
        }
    }

    public final boolean cleanAllFiles() {
        return recursiveDelete(root);
    }


    private static boolean recursiveDelete(final File file) {
        if (file.isFile()) {
            return file.delete();
        } else if (!file.isDirectory()){
            return !file.exists();
        }
        final File[] children = file.listFiles();
        if (children == null){
            return false;
        } else {
            boolean result = true;
            for (final File f : children){
                result &= recursiveDelete(f);
            }
            return result;
        }
    }

    private static File resolveAndCreateDirectory(final File root, final String resolve) {
        final File target = root.toPath().resolve(resolve).toFile();
        if (!target.mkdirs()){
            throw new IllegalStateException("Failed to create sub-directory: " + target.getAbsolutePath());
        } else {
            return target;
        }
    }
}
