package io.github.honoriuss.mapr.query.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class QueryTest {
    private class TestClass {
        public String attr1;
        public String attr2;
    }

    @Test
    public void queryTest() {
        var source = "public void findByAttr1LikeLimit(String likeIt, int limitIt);";
        var query = new Query(source, TestClass.class);
        Assert.assertNotNull(query);
    }
}
