package org.jumpmind.pos.service;

@EndpointOverride(path = "/this/is/a/test")
public class TestEndpointOverride {

    public int invokeCount = 0;

    public void test() {
        invokeCount++;
    }
}
