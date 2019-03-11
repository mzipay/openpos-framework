package org.jumpmind.pos.service;

@Endpoint(path="/this/is/a/test")
public class TestEndpoint {

    public int invokeCount = 0;

    public void test() {
        invokeCount++;
    }
}
