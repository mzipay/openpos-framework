package org.jumpmind.pos.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class MoneyCalculatorTest {

    @Test
    public void testAddition() {
        MoneyCalculator calculator = new MoneyCalculator("USD");
        assertEquals(new BigDecimal("15.50"), calculator.add(new BigDecimal("10.33"), new BigDecimal("5.17")));
    }
}
