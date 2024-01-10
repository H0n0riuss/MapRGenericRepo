package io.github.honoriuss.mapr.generator;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author H0n0riuss
 */
public class AQueryConditionCreatorTest {
    private class TestClass {
        public String title;
    }

    @Test
    public void codeBlockQueryConditionTest() {
        var methodName = "LikeTitleNotLikeTitleLimit";
        var arguments = List.of("title", "notTitle", "limit");
        var queryCondition = AQueryConditionExtractor.extractQueryCondition(methodName, arguments, TestClass.class);
        var queryCodeBlock = AQueryConditionCreator.createQueryCodeBlock(queryCondition.eQueryPartList);
        var conditionCodeBlock = AQueryConditionCreator.createConditionCodeBlock(queryCondition.eConditionPartList);
        Assert.assertNotNull(queryCodeBlock);
        Assert.assertNotNull(conditionCodeBlock);
    }
}
