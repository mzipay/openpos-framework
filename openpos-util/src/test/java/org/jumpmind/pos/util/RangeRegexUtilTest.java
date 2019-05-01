package org.jumpmind.pos.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

public class RangeRegexUtilTest {

    @Test
    public void testRangeRegex66_77() {
        assertForRange(66, 77);
    }

    @Test
    public void testRangeRegex101_555() {
        assertForRange(101, 555);
    }

    @Test
    public void testRangeRegex0_101() {
        assertForRange(0, 101);
    }
    
    @Test
    public void testRangeRegex1_10() {
        assertForRange(1, 10);
    }

    private void assertForRange(int start, int end) {
        String regex = RangeRegexUtil.getRegex(Integer.toString(start), Integer.toString(end));
        for (int i = start - 100; i <= end + 100; i++) {
            if (i >= start && i <= end) {
                assertTrue(Pattern.matches(regex, Integer.toString(i)));
            } else {
                assertFalse(Pattern.matches(regex, Integer.toString(i)));
            }
        }

    }
}
