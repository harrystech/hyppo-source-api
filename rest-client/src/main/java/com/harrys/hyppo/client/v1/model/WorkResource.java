package com.harrys.hyppo.client.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jpetty on 12/21/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WorkResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("metric")
    private final Integer metric;

    @JsonCreator
    public WorkResource(
            @JsonProperty("id")     final Integer id,
            @JsonProperty("name")   final String  name,
            @JsonProperty("type")   final String  type,
            @JsonProperty("metric") final Integer metric
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

    public final Integer getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final Integer getMetric() {
        return metric;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer id;
        private String name;
        private String type;
        private Integer metric;

        private Builder() {
        }

        public final Builder withId(int val) {
            id = val;
            return this;
        }

        public final Builder withName(String val) {
            name = val;
            return this;
        }

        public final Builder withType(String val) {
            type = val;
            return this;
        }

        public final Builder withMetric(int val) {
            metric = val;
            return this;
        }

        public final WorkResource build() {
            return new WorkResource(this);
        }
    }
}
