package com.harrys.hyppo.source.api.task;

/**
 * Created by jpetty on 7/17/15.
 */
public interface RawDataFetcher {
    void fetchRawData(FetchRawData operation) throws Exception;
}
