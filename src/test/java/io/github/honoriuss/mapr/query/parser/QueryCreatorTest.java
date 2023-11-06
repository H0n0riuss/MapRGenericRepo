package io.github.honoriuss.mapr.query.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class QueryCreatorTest {


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
}
