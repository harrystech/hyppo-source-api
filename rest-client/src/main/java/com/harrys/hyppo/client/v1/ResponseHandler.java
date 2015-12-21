package com.harrys.hyppo.client.v1;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

/**
 * Created by jpetty on 12/18/15.
 */
@FunctionalInterface
public interface ResponseHandler<T> {
    T handleResponse(CloseableHttpResponse response) throws IOException;
}
