package io.github.honoriuss.mapr.generator;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author H0n0riuss
 */
public class AQueryCreatorTest {

    private class TestClass {
        public String title;
    }

    @Test
    public void statementTest() {
        var methodName = "insertByLimitLimit(int limit, int limit2)";
        var attributeList = List.of("limit", "limit2");
        var codeBock = AQueryCreator.createQueryStatement(methodName, attributeList);
        Assert.assertNotNull(codeBock);
        var should = ".limit(limit).limit(limit2)";
        Assert.assertEquals(should, codeBock.toString());
    }

    @Test
    public void statementOrderByIgnoreTest() {
        var methodName = "insertByLimitOrderByCol1(int limit, int limit2)";
        var attributeList = List.of("limit", "limit2");
        var codeBock = AQueryCreator.createQueryStatement(methodName, attributeList);
        Assert.assertNotNull(codeBock);
        var should = ".limit(limit)";
        Assert.assertEquals(should, codeBock.toString());
    }

    @Test
    public void statementFindByTitleTest() {
        var methodName = "findByTitleLimitOrderByCol1(String title, int limit)";
        var attributeList = List.of("limit", "");
        var codeBock = AQueryCreator.createQueryStatement(methodName, TestClass.class, attributeList);
        Assert.assertNotNull(codeBock);
        var should = ".limit(limit)";
        Assert.assertEquals(should, codeBock.toString());
    }

    @Test
    public void statementFindByLikeTitleTest() {
        //TODO remove?
        /*var methodName = "findByTitleLikeLimitOrderByCol1(String title, int limit)";
        var attributeList = List.of("title", "limit");
        var codeBock = AQueryCreator.createQueryStatement(methodName, TestClass.class, attributeList);
        Assert.assertNotNull(codeBock);
        var should = ".like(Title, title).limit(limit)";
        Assert.assertEquals(should, codeBock.toString());*/
    }
}
