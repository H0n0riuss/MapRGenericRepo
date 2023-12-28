package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.enums.EQueryType;
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

        Assert.assertTrue(obj.getQueryParts().isPresent());
        Assert.assertTrue(obj.getQueryParts().get().getQueryTypeStringList().isPresent());
        var queryTypeStringList = obj.getQueryParts().get().getQueryTypeStringList().get();
        Assert.assertEquals(4, queryTypeStringList.size());
        Assert.assertEquals("Attr1", queryTypeStringList.get(0)); //TODO ist das wirklich das was man erwartet...
        Assert.assertEquals("Like", queryTypeStringList.get(1));
        Assert.assertEquals("Attr2", queryTypeStringList.get(2));
        Assert.assertEquals("Limit", queryTypeStringList.get(3));

        Assert.assertTrue(obj.getQueryParts().get().getQueryTypeModelList().isPresent());
        var queryTypeList = obj.getQueryParts().get().getQueryTypeModelList().get();
        Assert.assertEquals(EQueryType.LIKE, queryTypeList.get(0).getQueryType());
    }
}
