package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.models.QueryType;
import io.github.honoriuss.mapr.query.models.Subject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class QueryCreatorTest {

    public class TestClass {
        public String attr1;
        public String attr2;
    }


    @Test
    public void createConditionTest(){
        var interfaceMethodName = "String findByTitle(String title);";
        var should = new String[]{"Title(", "String title);"}; //TODO PartTree erstellen

        var res = QueryCreator.createCondition(interfaceMethodName);
        Assert.assertEquals(should, res);
    }

    @Test
    public void testCreateCondition() {
        // Arrange
        String methodName = "findBySomeCondition";

        // Act
        String[] result = QueryCreator.createCondition(methodName);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("Some", result[0]);
        Assert.assertEquals("Condition", result[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateConditionWithNullMethodName() {
        // Arrange
        String methodName = null;

        // Act/Assert
        QueryCreator.createCondition(methodName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateConditionWithEmptyMethodName() {
        // Arrange
        String methodName = "";

        // Act/Assert
        QueryCreator.createCondition(methodName);
    }

    @Test
    public void testGetQuery() {
        var method = "public void findByAttr1LikeAttr2Limit(String attr1, String attr2, int limit);";
        var obj = QueryCreator.getQuery(method, TestClass.class);
        Assert.assertNotNull(obj);
        Assert.assertEquals("void", obj.getReturnType().getReturnType().toString());
        Assert.assertTrue(obj.getSubject().isPresent());
        Assert.assertTrue(obj.getSubject().get().getSubjectType().isPresent());
        Assert.assertEquals(Subject.ESubjectType.READ, obj.getSubject().get().getSubjectType().get());
        Assert.assertTrue(obj.getQueryTypes().isPresent());
        Assert.assertTrue(obj.getQueryTypes().get().getQueryTypeStringList().isPresent());
        Assert.assertEquals(4, obj.getQueryTypes().get().getQueryTypeStringList().get().size());
        Assert.assertEquals("Attr1", obj.getQueryTypes().get().getQueryTypeStringList().get().get(0)); //TODO ist das wirklich das was man erwartet...
        Assert.assertEquals("Like", obj.getQueryTypes().get().getQueryTypeStringList().get().get(1));
        Assert.assertEquals("Attr2", obj.getQueryTypes().get().getQueryTypeStringList().get().get(2));
        Assert.assertEquals("Limit", obj.getQueryTypes().get().getQueryTypeStringList().get().get(3));
    }
}
