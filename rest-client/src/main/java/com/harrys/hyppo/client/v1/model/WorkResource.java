package com.harrys.hyppo.client.v1.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by jpetty on 12/21/15.
 */
public final class WorkResource {

    @JsonProperty("id")
    private final int id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("metric")
    private final int metric;

    @JsonCreator
    public WorkResource(
            @JsonProperty("id")     final int id,
            @JsonProperty("name")   final String name,
            @JsonProperty("type")   final String type,
            @JsonProperty("metric") final int metric
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.metric = metric;
    }

    private WorkResource(Builder builder) {
        id = builder.id;
        name = builder.name;
        type = builder.type;
        metric = builder.metric;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMetric() {
        return metric;
    }


    public static final class Builder {
        private int id;
        private String name;
        private String type;
        private int metric;

        private Builder() {
        }

        public Builder withId(int val) {
            id = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withType(String val) {
            type = val;
            return this;
        }

        public Builder withMetric(int val) {
            metric = val;
            return this;
        }

        public WorkResource build() {
            return new WorkResource(this);
        }
    }
}
