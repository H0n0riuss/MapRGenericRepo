package io.github.honoriuss.mapr.generator;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
public class AQueryConditionCreatorTest {
    public class TestClass {
        public String title;
    }

    @Test
    public void codeBlockQueryConditionTest() {
        var methodName = "LikeTitleNotLikeTitleLimit";
        var arguments = List.of("title", "notTitle", "limit");
        var classAttributes = new ArrayList<String>();
        classAttributes.add("title");
        var queryCondition = AQueryConditionExtractor.extractQueryCondition(methodName, arguments, classAttributes);
        var queryCodeBlock = AQueryConditionCreator.createQueryCodeBlock(queryCondition.eQueryPartList);
        var conditionCodeBlock = AQueryConditionCreator.createConditionCodeBlock(queryCondition.eConditionPartList);
        Assert.assertNotNull(queryCodeBlock);
        Assert.assertNotNull(conditionCodeBlock);
    }
}
