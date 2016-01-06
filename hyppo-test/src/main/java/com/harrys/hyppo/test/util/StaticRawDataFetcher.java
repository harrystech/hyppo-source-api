package com.harrys.hyppo.test.util;

import com.harrys.hyppo.source.api.task.FetchRawData;
import com.harrys.hyppo.source.api.task.RawDataFetcher;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jpetty on 1/5/16.
 */
public final class StaticRawDataFetcher implements RawDataFetcher {

    private final List<URL> resources;

    public StaticRawDataFetcher(final List<URL> resources) {
        this.resources = resources;
    }

    @Override
    public void fetchRawData(final FetchRawData operation) throws Exception {
        for (final URL url : resources) {
            try (final InputStream stream = url.openStream()) {
                operation.addData(stream);
            }
        }
    }


    public static StaticRawDataFetcher createWithFile(final File file) {
        return createWithFiles(Collections.singletonList(file));
    }

    public static StaticRawDataFetcher createWithFiles(final List<File> files) {
        final List<URL> fileUrls = files.stream()
                .map(File::toURI)
                .map(uri -> {
                    try {
                        return uri.toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e); //   Should never happen
                    }
                })
                .collect(Collectors.toList());
        return new StaticRawDataFetcher(fileUrls);
    }

    public static StaticRawDataFetcher createWithResource(final String resource) {
        return createWithResources(Collections.singletonList(resource));
    }

    public static StaticRawDataFetcher createWithResources(final List<String> resources) {
        final ClassLoader loader = currentClassLoader();
        final List<URL> urlList  = resources.stream()
                .map(loader::getResource)
                .collect(Collectors.toList());
        return new StaticRawDataFetcher(urlList);
    }

    private static ClassLoader currentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
