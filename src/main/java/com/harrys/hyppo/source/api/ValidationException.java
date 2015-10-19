package com.harrys.hyppo.source.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jpetty on 7/7/15.
 */
public final class ValidationException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public ValidationException(){
        super();
    }

    public ValidationException(String message){
        super(message);
    }

    public ValidationException(String message, Throwable cause){
        super(message, cause);
    }

    public static final ValidationException withMessages(final List<String> messages){

        if (messages.isEmpty()){
            return new ValidationException();
        } else {
            final StringBuilder sb = new StringBuilder();

            for (int i = 0; i < messages.size(); i++){
                sb.append(messages.get(i));
                if (i + 1 < messages.size()){
                    sb.append('\n');
                }
            }

            return new ValidationException(sb.toString());
        }
    }
}
