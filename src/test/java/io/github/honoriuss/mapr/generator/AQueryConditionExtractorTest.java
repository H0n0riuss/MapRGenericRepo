package io.github.honoriuss.mapr.generator;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author H0n0riuss
 */
public class AQueryConditionExtractorTest {
    private class TestClass {
        public String title;
    }
    @Test
    public void extractionTest() {
        var methodName = "LikeTitleNotLikeTitleLimit";
        var arguments = List.of("title", "notTitle", "limit");
        var res = AQueryConditionExtractor.extractQueryCondition(methodName, arguments, TestClass.class);
        Assert.assertNotNull(res);
        Assert.assertEquals(2, res.eConditionPartList.size());
        Assert.assertEquals(1, res.eQueryPartList.size());
    }
}
