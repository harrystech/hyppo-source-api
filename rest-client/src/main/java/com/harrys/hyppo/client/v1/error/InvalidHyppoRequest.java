package com.harrys.hyppo.client.v1.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jpetty on 12/21/15.
 */
public final class InvalidHyppoRequest extends HyppoClientException {
    private static final long serialVersionUID = 1L;

    @JsonProperty("messages")
    private final List<String> messages;

    @JsonCreator
    public InvalidHyppoRequest(
            @JsonProperty("messages") final List<String> messages
    ){
        this.messages = new ArrayList<>(messages);
    }

    public InvalidHyppoRequest(final String message){
        this.messages = new ArrayList<>(1);
        this.messages.add(message);
    }

    private InvalidHyppoRequest(Builder builder) {
        this(new ArrayList<String>(builder.messages));
    }

    public final List<String> getMessages(){
        return this.messages;
    }

    @Override
    public final String toString(){
        return this.getClass().getName() + " - " + messages.stream().collect(Collectors.joining(", "));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private List<String> messages = new ArrayList<>();

        private Builder() {
        }

        public Builder withMessages(List<String> val) {
            messages = val;
            return this;
        }

        public Builder addMessage(final String message){
            messages.add(message);
            return this;
        }

        public InvalidHyppoRequest build() {
            return new InvalidHyppoRequest(this);
        }
    }
}
