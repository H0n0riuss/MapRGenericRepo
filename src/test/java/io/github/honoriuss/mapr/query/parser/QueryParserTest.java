package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.models.Query;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {
    private class TestClass {
        public String attr1;
        public String attr2;
        public int limit;
    }

    @Test
    public void partsAsStringTest() {
        var method = "public void findByAttr1LikeAttr2Limit(String attr1, String attr2, int limit);";
        var expected = ".like(attr1, attr2).limit(limit);"; //TODO richtiges pattern nehmen
        var query = new Query(method, TestClass.class);
        var result = QueryParser.createMethodCalls(query);
        Assert.assertEquals(expected, result);
    }
}
