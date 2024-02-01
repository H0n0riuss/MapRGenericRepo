package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author H0n0riuss
 */
public class AQueryConditionCreatorTest {
    public class TestClass {
        public String title;
        String entityName;
        int limit;
        int limit2;
    }

    @Test
    public void codeBlockQueryConditionTest() {
        var methodName = "LikeTitleNotLikeTitleLimit";
        var arguments = new ArrayList<>(Arrays.asList("title", "notTitle", "limit"));
        var classAttributes = new ArrayList<String>();
        classAttributes.add("title");
        var queryCondition = AQueryConditionExtractor.extractQueryCondition(methodName, arguments, classAttributes);
        var queryCodeBlock = AQueryConditionCreator.createQueryCodeBlock(queryCondition.eQueryPartList);
        var conditionCodeBlock = AQueryConditionCreator.createConditionCodeBlock(queryCondition.eConditionPartList);
        Assert.assertNotNull(queryCodeBlock);
        Assert.assertNotNull(conditionCodeBlock);
    }

    @Test
    public void deleteMe(){
        var method = "findByLikeEntityNameLimitLimit";
        var argumentStringList = new ArrayList<>(Arrays.asList("name", "limit", "limit2"));
        var entityClassName = ClassName.get(TestClass.class);
        var hasReturnType = true;
        var hasListReturnType = true;
        var attributeList = new ArrayList<>(Arrays.asList("name", "limit", "limit2", "entityName"));
        var query = MethodGenerator.getStoreQuery(method, argumentStringList, entityClassName, hasReturnType, hasListReturnType, attributeList);
        Assert.assertNotNull(query);
    }
}
