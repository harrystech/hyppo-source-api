package com.harrys.hyppo.client.v1;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.time.Duration;

/**
 * Created by jpetty on 12/18/15.
 */
public final class HyppoClientConfig {

    private final Config underlying;

    private final String keyName;

    private final SecretKeySpec keySecret;

    private final URI baseURI;

    private final Duration connectTimeout;

    private final Duration socketTimeout;

    public HyppoClientConfig(final Config config){
        this.underlying = config.withFallback(referenceConfig()).resolve();
        this.keyName    = underlying.getString("hyppo.client.key-name");
        this.keySecret  = HyppoSigning.decodeSecretKey(underlying.getString("hyppo.client.key-secret"));
        this.baseURI    = URI.create(underlying.getString("hyppo.client.service-uri"));
        this.connectTimeout = underlying.getDuration("hyppo.client.connect-timeout");
        this.socketTimeout  = underlying.getDuration("hyppo.client.socket-timeout");
    }


    public final URI getBaseURI() {
        return baseURI;
    }

    public final SecretKeySpec getKeySecret() {
        return keySecret;
    }

    public final String getKeyName() {
        return keyName;
    }

    public final Duration getConnectTimeout() {
        return connectTimeout;
    }

    public final Duration getSocketTimeout() {
        return socketTimeout;
    }

    public final Config underlying() {
        return underlying;
    }


    private static Config referenceConfig(){
        return ConfigFactory.parseResources("com/harrys/hyppo/client/reference.conf");
    }
}
