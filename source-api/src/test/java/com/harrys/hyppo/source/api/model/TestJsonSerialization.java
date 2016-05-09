package com.harrys.hyppo.source.api.model;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

/**
 * Created by jpetty on 5/9/16.
 */
public class TestJsonSerialization {

    private final ObjectMapper mapper    = new ObjectMapper();
    private final IngestionSource source = new IngestionSource("test", ConfigFactory.empty().withValue("test", ConfigValueFactory.fromAnyRef(1L)));
    private final DataIngestionJob job   = new DataIngestionJob(source, UUID.randomUUID(), ConfigFactory.empty(), Instant.now());
    private final DataIngestionTask task = new DataIngestionTask(job, 1, ConfigFactory.empty());

    @Test()
    public void test_IngestionSourceSerialization() throws Exception {
        final String serialized = mapper.writeValueAsString(source);
        IngestionSource parsed  = mapper.readValue(serialized, IngestionSource.class);
        Assert.assertEquals(source, parsed);
    }


    @Test()
    public void test_DataIngestionJobSerialization() throws Exception {
        final String serialized = mapper.writeValueAsString(job);
        DataIngestionJob parsed = mapper.readValue(serialized, DataIngestionJob.class);
        Assert.assertEquals(job, parsed);
    }


    @Test()
    public void test_DataIngestionTaskSerialization() throws Exception {
        final String serialized  = mapper.writeValueAsString(task);
        DataIngestionTask parsed = mapper.readValue(serialized, DataIngestionTask.class);
        Assert.assertEquals(task, parsed);
    }
}
