package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.query.enums.EQueryType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class QueryPartModelTest {
    @Test
    public void constructorTest() {
        var queryType = new QueryTypeModel(EQueryType.LIKE, "attr1");
        queryType.addQueryAttribute("attr1");
        Assert.assertEquals(queryType.getQueryType(), EQueryType.LIKE);
        Assert.assertTrue(queryType.getQueryAttributes().isPresent());
        Assert.assertEquals("attr1", queryType.getQueryAttributes().get().get(0));
    }

    @Test
    public void getQueryTypeModelListTest() {
        var queryType = new QueryTypeModel(EQueryType.LIKE, "attr1");
        queryType.addQueryAttribute("attr1");
        Assert.assertTrue(queryType.getQueryAttributes().isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addToManyAttributesTest() {
        var queryType = new QueryTypeModel(EQueryType.LIKE, "attr1");
        queryType.addQueryAttribute("attr1");
        queryType.addQueryAttribute("attr2");
    }
}
