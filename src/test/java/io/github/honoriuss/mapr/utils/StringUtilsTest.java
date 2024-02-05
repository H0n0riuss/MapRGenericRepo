package io.github.honoriuss.mapr.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void getFirstColumnArgumentInMethodNameTest() {
        var methodName = "testName";
        var should = "";
        var argumentList = new ArrayList<>(List.of("t", "test","testName"));
        var res = StringUtils.getFirstColumnArgumentInMethodName(methodName, argumentList);

        Assert.assertNotNull(res);
        Assert.assertTrue(res.isPresent());

        if (methodName.startsWith(res.get())) {
            methodName = methodName.substring(res.get().length());
        }
        Assert.assertEquals(should, methodName);
    }

    @Test
    public void cutFirstColumnArgumentInMethodName() {
        var methodName = "testName";
        var should = "";
        var argumentList = new ArrayList<>(List.of("t", "test","testName"));
        var res = StringUtils.cutFirstColumnArgumentInMethodName(methodName, argumentList);

        Assert.assertNotNull(res);

        Assert.assertEquals(should, res);
    }
}
