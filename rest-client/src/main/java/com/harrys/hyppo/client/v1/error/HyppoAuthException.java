package com.harrys.hyppo.client.v1.error;

import com.harrys.hyppo.client.v1.HyppoClientConfig;

/**
 * Created by jpetty on 12/31/15.
 */
public class HyppoAuthException extends HyppoClientException {
    private static final long serialVersionUID = 1L;

    public HyppoAuthException(final HyppoClientConfig config){
        super("Hyppo Server @ " + config.getBaseURI().getHost() + " does not recognize API Key: " + config.getKeyName());
    }
}
