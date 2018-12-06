package org.jumpmind.pos.persist.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReflectUtilsTest {

    @Test
    public void testSettingAnEnum() {
        TestEnum test = new TestEnum();
        ReflectUtils.setProperty(test, "field", "ONE");
        assertEquals(TestEnum.Test.ONE, test.field);
    }
    
    static class TestEnum {
        enum Test {ONE,TWO};
        
        Test field;
    }
}
