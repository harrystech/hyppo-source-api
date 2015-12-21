package com.harrys.hyppo.client.v1;

import com.harrys.hyppo.client.v1.error.HyppoClientException;
import com.harrys.hyppo.client.v1.model.CreateIngestionJob;
import com.harrys.hyppo.client.v1.model.IngestionJobCreated;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by jpetty on 12/18/15.
 */
public final class HyppoApiClient {

    private final HyppoHttpClient http;

    private final ObjectMapper mapper;

    public HyppoApiClient(final HyppoClientConfig config){
        this.http   = new HyppoHttpClient(config);
        this.mapper = new ObjectMapper();
    }


    public IngestionJobCreated createIngestionJob(final CreateIngestionJob request) throws IOException {
        try {
            final String body = mapper.writeValueAsString(request);
            return http.executePost("/api/v1/ingestion_jobs", body, responseHandler(IngestionJobCreated.class));
        } catch (JsonProcessingException jpe) {
            throw new IllegalArgumentException("Invalid request body object", jpe);
        }
    }



    private <T> ResponseHandler<T> responseHandler(final Class<T> klass) {
        return new WrapSuccessHandler<>(response -> mapper.readValue(response.getEntity().getContent(), klass));
    }

    private static final class WrapSuccessHandler<T> implements ResponseHandler<T> {

        private final ResponseHandler<T> successHandler;

        public WrapSuccessHandler(final ResponseHandler<T> successHandler){
            this.successHandler = successHandler;
        }

        public final T handleResponse(final CloseableHttpResponse response) throws IOException {
            // TODO: Wrap  with error detection
            return successHandler.handleResponse(response);
        }
    }
}
