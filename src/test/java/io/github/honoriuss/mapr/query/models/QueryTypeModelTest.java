package io.github.honoriuss.mapr.query.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class QueryTypeModelTest {
    @Test
    public void constructorTest() {
        var queryType = new QueryTypeModel(QueryType.EQueryType.LIKE);
        queryType.addQueryAttribute("attr1");
        Assert.assertEquals(queryType.getQueryType(), QueryType.EQueryType.LIKE);
        Assert.assertTrue(queryType.getQueryAttributes().isPresent());
        Assert.assertEquals("attr1", queryType.getQueryAttributes().get().get(0));
    }

    @Test
    public void getQueryTypeModelListTest() {
        var queryType = new QueryTypeModel(QueryType.EQueryType.LIKE);
        queryType.addQueryAttribute("attr1");
        Assert.assertTrue(queryType.getQueryAttributes().isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addToManyAttributesTest() {
        var queryType = new QueryTypeModel(QueryType.EQueryType.LIKE);
        queryType.addQueryAttribute("attr1");
        queryType.addQueryAttribute("attr2");
    }
}
