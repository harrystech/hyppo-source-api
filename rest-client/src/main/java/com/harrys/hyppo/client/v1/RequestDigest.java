package com.harrys.hyppo.client.v1;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;

import javax.crypto.Mac;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jpetty on 12/22/15.
 */
public final class RequestDigest {

    private final Mac mac;

    private RequestDigest(Builder builder) {
        mac = builder.mac;
        prepareForContent(builder.keyName, builder.timestamp, builder.method, builder.path, builder.queryParams);
    }

    public static Builder newBuilder(final String keyName, final Mac initializedMac) {
        return new Builder(keyName, initializedMac);
    }

    public static Builder newBuilder(final HyppoClientConfig config) {
        return HyppoSigning.newRequestDigestBuilder(config);
    }


    public final void update(final byte[] bytes){
        mac.update(bytes);
    }

    public final void update(final byte b){
        mac.update(b);
    }

    public final byte[] doFinal(final InputStream stream) throws IOException {
        final byte[] buffer = new byte[1024];
        int n;
        try {
            while (-1 != (n = stream.read(buffer))){
                mac.update(buffer, 0, n);
            }
        } finally {
            IOUtils.closeQuietly(stream);
        }
        return mac.doFinal();
    }

    public final byte[] doFinal(final File file) throws IOException {
        return doFinal(new FileInputStream(file));
    }

    public final byte[] doFinal(){
        return mac.doFinal();
    }

    public final byte[] doFinal(final byte[] bytes){
        return mac.doFinal(bytes);
    }

    private void prepareForContent(final String keyName, final long timestamp, final String method, final String path, final List<NameValuePair> queryParams){
        mac.update(keyName.getBytes(StandardCharsets.UTF_8));
        mac.update(Long.toString(timestamp).getBytes(StandardCharsets.UTF_8));

        mac.update(method.toUpperCase().getBytes(StandardCharsets.UTF_8));
        mac.update(path.getBytes(StandardCharsets.UTF_8));

        final List<NameValuePair> params = new ArrayList<>(queryParams);
        params.sort(paramOrder);

        for (final NameValuePair param : params){
            mac.update(param.getName().getBytes(StandardCharsets.UTF_8));
            mac.update(param.getValue().getBytes(StandardCharsets.UTF_8));
        }
    }

    private static final Comparator<NameValuePair> paramOrder = new Comparator<NameValuePair>() {
        @Override
        public final int compare(NameValuePair o1, NameValuePair o2) {
            final int c1 = o1.getName().compareTo(o2.getName());
            if (c1 == 0){
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return c1;
            }
        }
    };


    public static final class Builder {
        private Mac mac;
        private String keyName;
        private long timestamp;
        private String method;
        private String path;
        private List<NameValuePair> queryParams = new ArrayList<>();

        private Builder(final String keyName, final Mac mac){
            this.keyName = keyName;
            this.mac     = mac;
        }

        public Builder withTimestamp(long val) {
            timestamp = val;
            return this;
        }

        public Builder withMethod(String val) {
            method = val;
            return this;
        }

        public Builder withPath(String val) {
            path = val;
            return this;
        }

        public Builder withQueryParams(List<NameValuePair> val) {
            queryParams = val;
            return this;
        }

        public RequestDigest build() {
            return new RequestDigest(this);
        }
    }
}
