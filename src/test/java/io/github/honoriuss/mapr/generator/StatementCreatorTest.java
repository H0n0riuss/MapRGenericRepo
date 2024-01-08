package io.github.honoriuss.mapr.generator;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author H0n0riuss
 */
public class StatementCreatorTest {

    @Test
    public void statementTest() {
        var methodName = "insertByLimit(int limit)";
        var attributeList = List.of("limit");
        var codeBock = StatementCreator.createStatement(methodName, attributeList);
        Assert.assertNotNull(codeBock);

    }
}
