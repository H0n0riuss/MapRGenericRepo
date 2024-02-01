package io.github.honoriuss.mapr.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class StringUtilsTest {
    private class TestClass {
        public String testName;
    }

    @Test
    public void classAttributesTest() {
        var res = StringUtils.getAttributesFromClass(TestClass.class);
        Assert.assertEquals(2, res.size());
        Assert.assertTrue(res.contains("testName"));
    }
}
