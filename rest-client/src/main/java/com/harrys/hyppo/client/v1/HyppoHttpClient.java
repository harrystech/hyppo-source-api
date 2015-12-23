package com.harrys.hyppo.client.v1;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jpetty on 12/18/15.
 */
public final class HyppoHttpClient {
    private static final Logger log = LoggerFactory.getLogger(HyppoHttpClient.class);

    private final HyppoClientConfig config;

    public HyppoHttpClient(final HyppoClientConfig config) {
        this.config = config;
    }

    public <T> T executeDelete(final String path, final ResponseHandler<T> handler) throws IOException {
        return executeDelete(path, Collections.emptyList(), handler);
    }

    public <T> T executeDelete(final String path, final List<NameValuePair> queryParams, final ResponseHandler<T> handler) throws IOException {
        final HttpDelete request = new HttpDelete(requestURI(path, queryParams));
        attachSignature(request, queryParams, new byte[0]);

        return executeRequest(request, handler);
    }

    public <T> T executeGet(final String path, final ResponseHandler<T> handler) throws IOException {
        return executeGet(path, Collections.emptyList(), handler);
    }

    public <T> T executeGet(final String path, final List<NameValuePair> queryParams, final ResponseHandler<T> handler) throws IOException {
        final HttpGet request = new HttpGet(requestURI(path, queryParams));
        attachSignature(request, queryParams, new byte[0]);

        return executeRequest(request, handler);
    }

    public <T> T executePost(final String path, final String body, final ResponseHandler<T> handler) throws IOException {
        return executePost(path, Collections.emptyList(), body, handler);
    }

    public <T> T executePost(final String path, final List<NameValuePair> params, final String body, final ResponseHandler<T> handler) throws IOException {
        final HttpPost request = new HttpPost(requestURI(path, params));
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        attachSignature(request, params, body.getBytes(StandardCharsets.UTF_8));

        return executeRequest(request, handler);
    }

    private <T> T executeRequest(final HttpUriRequest request, final ResponseHandler<T> handler) throws IOException {
        final CloseableHttpClient client = createClientInstance();
        try {
            final CloseableHttpResponse response = client.execute(request);
            try {
                log.debug("{} - {} : {}", request.getMethod(), request.getURI().getPath(), response.getStatusLine().getStatusCode());
                return handler.handleResponse(response);
            } finally {
                IOUtils.closeQuietly(response);
            }
        } catch (Exception e) {
            log.error("{} - {} : FAILED", request.getMethod(), request.getURI().getPath(), e);
            throw e;
        } finally {
            IOUtils.closeQuietly(client);
        }
    }


    private void attachSignature(final HttpUriRequest request, final List<NameValuePair> params, final byte[] content) {
        final RequestDigestBuffer digest =
                RequestDigestBuffer.newBuilder(config)
                    .withMethod(request.getMethod())
                    .withPath(request.getURI().getPath())
                    .withQueryParams(params)
                    .withTimestamp(Instant.now(Clock.systemUTC()).toEpochMilli())
                    .build();
        final byte[] signature = digest.doFinal(content);
        for (final Header h : digest.requestHeaders(signature)){
            request.addHeader(h);
        }
    }


    private CloseableHttpClient createClientInstance(){
        final RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setConnectionRequestTimeout((int)config.getConnectTimeout().toMillis())
                .setSocketTimeout((int)config.getConnectTimeout().toMillis())
                .build();

        final List<Header> headers = new ArrayList<>(1);
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultHeaders(headers)
                .build();
    }


    private URI requestURI(final String path, final List<NameValuePair> queryParams) {

        try {
            return new URIBuilder(config.getBaseURI())
                    .setPath(path)
                    .setParameters(queryParams)
                    .build();
        } catch (URISyntaxException use) {
            String paramString = "";
            for (final NameValuePair nvp : queryParams) {
                paramString += "'" + nvp.getName() + "' -> '" + nvp.getValue() + "'";
            }
            throw new IllegalArgumentException("Invalid URI for path='" + path + "' params=" + paramString, use);
        }
    }

}
