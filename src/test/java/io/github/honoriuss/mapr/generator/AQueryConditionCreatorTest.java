package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import io.github.honoriuss.mapr.utils.StringUtils;
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
        var queryCodeBlock = AQueryConditionCreator.createQueryCodeBlock(queryCondition.getQueryPartList());
        var conditionCodeBlock = AQueryConditionCreator.createConditionCodeBlock(queryCondition.getConditionPartList());
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
        var attributeList = StringUtils.getAttributesFromClass(TestClass.class);
        var query = AMethodGenerator.getStoreQuery(method, argumentStringList, entityClassName, hasReturnType, hasListReturnType, attributeList);
        Assert.assertNotNull(query);
    }

    @Test
    public void getStoreQueryTest(){
        var method = "findByEntityName";
        var argumentStringList = new ArrayList<>(List.of("entityName"));
        var entityClassName = ClassName.get(TestClass.class);
        var hasReturnType = true;
        var hasListReturnType = true;
        var attributeList = StringUtils.getAttributesFromClass(TestClass.class);
        var query = AMethodGenerator.getStoreQuery(method, argumentStringList, entityClassName, hasReturnType, hasListReturnType, attributeList);
        Assert.assertNotNull(query);
    }

    @Test
    public void getStoreQuerySpecialIdTest(){
        var method = "find";
        var argumentStringList = new ArrayList<>(List.of("id"));
        var entityClassName = ClassName.get(TestClass.class);
        var hasReturnType = true;
        var hasListReturnType = false;
        var attributeList = StringUtils.getAttributesFromClass(TestClass.class);
        var query = AMethodGenerator.getStoreQuery(method, argumentStringList, entityClassName, hasReturnType, hasListReturnType, attributeList);
        Assert.assertNotNull(query);
        var shouldContain = """
                org.ojai.store.Query query = connection.newQuery().build();
                var queryResult = store.find(query);
                return queryResult.iterator().next().toJavaBean(TestClass.class)""";
        Assert.assertEquals(shouldContain, query);
    }

    @Test
    public void getStoreQuerySpecialIdTest2(){
        var method = "findById";
        var argumentStringList = new ArrayList<>(List.of("id"));
        var entityClassName = ClassName.get(TestClass.class);
        var hasReturnType = true;
        var hasListReturnType = false;
        var attributeList = StringUtils.getAttributesFromClass(TestClass.class);
        var query = AMethodGenerator.getStoreQuery(method, argumentStringList, entityClassName, hasReturnType, hasListReturnType, attributeList);
        Assert.assertNotNull(query);
        var shouldContain = String.format("return store.findById(id).toJavaBean(%s.class)", entityClassName.simpleName());
        Assert.assertEquals(shouldContain, query);
    }

    @Test
    public void getStoreQuerySpecialIdTest3(){
        var method = "findByTitle";
        var argumentStringList = new ArrayList<>(List.of("rui"));
        var entityClassName = ClassName.get(TestClass.class);
        var hasReturnType = true;
        var hasListReturnType = false;
        var attributeList = StringUtils.getAttributesFromClass(TestClass.class);
        var query = AMethodGenerator.getStoreQuery(method, argumentStringList, entityClassName, hasReturnType, hasListReturnType, attributeList);
        Assert.assertNotNull(query);
        var shouldContain = """
                org.ojai.store.QueryCondition condition = connection.newCondition().like("title",rui).build();
                org.ojai.store.Query query = connection.newQuery().where(condition).build();
                var queryResult = store.find(query);
                return queryResult.iterator().next().toJavaBean(TestClass.class)""";
        Assert.assertEquals(shouldContain, query);
    }
}
