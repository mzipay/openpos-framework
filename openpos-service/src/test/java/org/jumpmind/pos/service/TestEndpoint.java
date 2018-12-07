package org.jumpmind.pos.service;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Endpoint("/this/is/a/test")
@Order(Ordered.LOWEST_PRECEDENCE)
public class TestEndpoint {

    public int invokeCount = 0;

    public void test() {
        invokeCount++;
    }
}
