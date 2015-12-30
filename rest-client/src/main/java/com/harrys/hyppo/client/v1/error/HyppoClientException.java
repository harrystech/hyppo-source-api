package com.harrys.hyppo.client.v1.error;

import java.io.IOException;

/**
 * Created by jpetty on 12/21/15.
 */
public class HyppoClientException extends IOException {

    public HyppoClientException(){
        this(null, null);
    }

    public HyppoClientException(final String message){
        this(message, null);
    }

    public HyppoClientException(final Throwable cause){
        this(null, cause);
    }

    public HyppoClientException(final String message, final Throwable cause){
        super(message, cause);
    }

    @Override
    public String toString(){
        return this.getClass().getName() + ": " + this.getMessage();
    }
}
