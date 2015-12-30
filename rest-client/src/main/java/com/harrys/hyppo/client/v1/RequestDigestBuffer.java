package com.harrys.hyppo.client.v1;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by jpetty on 12/22/15.
 */
public final class RequestDigestBuffer {

    private final Mac mac;

    private final String keyName;

    private final long timestamp;

    private final String method;

    private final String path;

    private final List<NameValuePair> queryParams;

    private RequestDigestBuffer(Builder builder) {
        this.mac         = instantiateMac(builder.key);
        this.keyName     = builder.keyName;
        this.timestamp   = builder.timestamp;
        this.method      = builder.method;
        this.path        = builder.path;
        this.queryParams = prepareQueryParams(builder.queryParams);

        prepareForContent();
    }

    private void prepareForContent(){
        mac.update(keyName.getBytes(StandardCharsets.UTF_8));
        mac.update(Long.toString(timestamp).getBytes(StandardCharsets.UTF_8));

        mac.update(method.toUpperCase().getBytes(StandardCharsets.UTF_8));
        mac.update(path.getBytes(StandardCharsets.UTF_8));

        for (final NameValuePair param : queryParams){
            mac.update(param.getName().getBytes(StandardCharsets.UTF_8));
            mac.update(param.getValue().getBytes(StandardCharsets.UTF_8));
        }
    }

    public final Header signingKeyNameHeader(){
        return HyppoSigning.signingKeyNameHeader(keyName);
    }

    public final Header timestampHeader(){
        return HyppoSigning.timestampHeader(timestamp);
    }

    public final Header signatureHeader(final byte[] signature){
        return HyppoSigning.signatureHeader(signature);
    }

    public final List<Header> requestHeaders(final byte[] signature){
        return Arrays.asList(
                signingKeyNameHeader(),
                timestampHeader(),
                signatureHeader(signature)
        );
    }

    public final void update(final byte[] bytes){
        if (bytes.length > 0){
            mac.update(bytes);
        }
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
        if (bytes.length > 0){
            return mac.doFinal(bytes);
        } else {
            return mac.doFinal();
        }
    }

    public final Mac getMacInstance(){
        return this.mac;
    }

    public static Builder newBuilder(final String keyName, final SecretKeySpec key){
        return new Builder(keyName, key);
    }

    public static Builder newBuilder(final HyppoClientConfig config) {
        return new Builder(config.getKeyName(), config.getKeySecret());
    }


    private static final Comparator<NameValuePair> paramOrder = (p1, p2) -> {
        final int c1 = p1.getName().compareTo(p2.getName());
        if (c1 == 0){
            return p1.getValue().compareTo(p2.getValue());
        } else {
            return c1;
        }
    };

    private static Mac instantiateMac(final SecretKeySpec key){
        try {
            final Mac mac = Mac.getInstance(HyppoSigning.SigningAlgorithm);
            mac.init(key);
            return mac;
        } catch (NoSuchAlgorithmException nse) {
            throw new RuntimeException("Failed initialize with algorithm: " + HyppoSigning.SigningAlgorithm, nse);
        } catch (InvalidKeyException ike){
            throw new RuntimeException("Failed to initialize the MAC instance with key algorithm: " + key.getAlgorithm(), ike);
        }
    }

    private static List<NameValuePair> prepareQueryParams(final List<NameValuePair> params) {
        final List<NameValuePair> output = (params == null) ? new ArrayList<>() : new ArrayList<>(params);
        output.sort(paramOrder);
        return Collections.unmodifiableList(output);
    }


    public static final class Builder {
        private String keyName;
        private SecretKeySpec key;
        private long timestamp;
        private String method;
        private String path;
        private List<NameValuePair> queryParams = new ArrayList<>();

        private Builder(final String keyName, final SecretKeySpec key){
            this.keyName = keyName;
            this.key     = key;
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
            queryParams = new ArrayList<>(val);
            return this;
        }

        public RequestDigestBuffer build() {
            return new RequestDigestBuffer(this);
        }
    }
}
