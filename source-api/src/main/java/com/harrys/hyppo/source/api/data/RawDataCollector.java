package com.harrys.hyppo.source.api.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * Created by jpetty on 7/16/15.
 */
public final class RawDataCollector {

    private final File directory;

    private final List<File> rawFiles;

    public RawDataCollector(final File directory){
        if (!directory.isDirectory()){
            throw new IllegalArgumentException("File output location: " + directory.getAbsolutePath() + " is not a valid directory!");
        }
        this.directory = directory;
        this.rawFiles  = new ArrayList<>();
    }

    public final boolean hasRawDataFiles(){
        synchronized (this.rawFiles){
            return !this.rawFiles.isEmpty();
        }
    }

    public final List<File> getRawFiles(){
        List<File> output;
        synchronized (this.rawFiles){
            output = new ArrayList<>(this.rawFiles);
        }
        return output;
    }

    public final void add(final InputStream stream) throws IOException {
        final File outputFile = this.createNewFile();
        try {
            this.copyToFile(stream, outputFile);
            this.appendFile(outputFile);
        } catch (IOException ioe){
            outputFile.delete();
            throw ioe;
        }
    }

    private final void appendFile(final File rawFile) {
        synchronized (this.rawFiles){
            this.rawFiles.add(rawFile);
        }
    }


    private final File createNewFile() throws IOException {
        return Files.createTempFile(this.directory.toPath(), "stream", "raw.gz").toFile();
    }

    private final void copyToFile(final InputStream source, final File outputFile) throws IOException {
        try (final FileOutputStream fos  = new FileOutputStream(outputFile);
             final GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            int n;
            final byte[] buffer = new byte[8024];
            while (-1 != (n = source.read(buffer))) {
                gzos.write(buffer, 0, n);
            }
        }
    }
}
