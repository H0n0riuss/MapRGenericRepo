package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.models.Query;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {
    private class TestClass {
        public String attr1;
        public String attr2;
    }

    @Test
    public void partsAsStringTest() {
        var method = "public void findByAttr1LikeLimit(String attr1, int limit);";
        var expected = ".like(Attr1, attr1).limit(limit);";
        var query = new Query(method, TestClass.class);
        var result = QueryParser.createMethodCalls(query);
        Assert.assertEquals(expected, result);
    }
}
