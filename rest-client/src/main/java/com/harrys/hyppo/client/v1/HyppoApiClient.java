package com.harrys.hyppo.client.v1;

import com.harrys.hyppo.client.v1.error.InvalidHyppoRequest;
import com.harrys.hyppo.client.v1.model.CreateIngestionJob;
import com.harrys.hyppo.client.v1.model.IngestionJobCreated;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jpetty on 12/18/15.
 */
public final class HyppoApiClient {
    private static final Logger log = LoggerFactory.getLogger(HyppoApiClient.class);


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

    private final class WrapSuccessHandler<T> implements ResponseHandler<T> {

        private final ResponseHandler<T> successHandler;

        public WrapSuccessHandler(final ResponseHandler<T> successHandler){
            this.successHandler = successHandler;
        }

        public final T handleResponse(final CloseableHttpResponse response) throws IOException {
            try {
                return successHandler.handleResponse(response);
            } catch (JsonMappingException jme) {
                if (response.getEntity().isRepeatable()){
                    InvalidHyppoRequest invalid = tryInvalidRequest(response);
                    if (invalid != null){
                        throw invalid;
                    }
                }
                throw jme;
            } catch (JsonProcessingException jpe){
                if (response.getEntity().isRepeatable()){
                    InvalidHyppoRequest invalid = tryTextResponse(response);
                    if (invalid != null){
                        throw invalid;
                    }
                }
                throw jpe;
            }
        }

        private InvalidHyppoRequest tryTextResponse(final CloseableHttpResponse response) {
            try {
                return new InvalidHyppoRequest(Arrays.asList(EntityUtils.toString(response.getEntity())));
            } catch (IOException ioe){
                log.warn("Unexpected IOException while attempting to parse as text", ioe);
                return null;
            }
        }

        private InvalidHyppoRequest tryInvalidRequest(final CloseableHttpResponse response) {
            try {
                return mapper.readValue(response.getEntity().getContent(), InvalidHyppoRequest.class);
            } catch (Exception e){
                log.debug("Failed to parse fallback JSON error response " + InvalidHyppoRequest.class.getName(), e);
                return null;
            }
        }
    }
}
