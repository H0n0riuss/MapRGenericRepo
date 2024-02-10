package io.github.honoriuss.mapr.connections.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class TableBaseModelTest {
    @Test
    public void toJsonTest() {
        var tableBaseModel = new TableBaseModel();
        tableBaseModel.setTableName("Hello World");
        var res = tableBaseModel.toJson();
        var expected = "{\"name\":\"Hello World\"}";
        Assert.assertEquals(expected, res);
    }
}
