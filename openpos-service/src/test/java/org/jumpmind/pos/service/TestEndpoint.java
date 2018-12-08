package org.jumpmind.pos.service;

@Endpoint("/this/is/a/test")
public class TestEndpoint {

    public int invokeCount = 0;

    public void test() {
        invokeCount++;
    }
}
