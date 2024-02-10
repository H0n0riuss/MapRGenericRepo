package io.github.honoriuss.mapr.connections.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class TableFullModelTest {
    @Test
    public void toJsonTest(){
        var tableFullModel = new TableFullModel();
        tableFullModel.setTableName("Hello World");
        var permissions = new TablePermissionModel();
        permissions.setOwner("user1");
        permissions.setGroup("group1");
        permissions.setOther("none");
        tableFullModel.setTablePermissionModel(permissions);
        var res = tableFullModel.toJson();
        var expected = "{\"name\":\"Hello World\",\"columns\":null,\"permissions\":{\"owner\":\"user1\",\"group\":\"group1\",\"other\":\"none\"}}";
        Assert.assertEquals(expected, res);
    }

    @Test
    public void toJsonFullTest(){
        var tableFullModel = new TableFullModel();
        tableFullModel.setTableName("Hello World");
        tableFullModel.setTableColumnsModels(new TableColumnsModel[3]);

        var permissions = new TablePermissionModel();
        permissions.setOwner("user1");
        permissions.setGroup("group1");
        permissions.setOther("none");
        tableFullModel.setTablePermissionModel(permissions);

        var column1 = new TableColumnsModel();
        column1.setColumnName("column1");
        column1.setColumnType("string");
        tableFullModel.getTableColumnsModels()[0] = column1;
        var column2 = new TableColumnsModel();
        column2.setColumnName("column2");
        column2.setColumnType("int");
        tableFullModel.getTableColumnsModels()[1] = column2;
        var column3 = new TableColumnsModel();
        column3.setColumnName("column3");
        column3.setColumnType("boolean");
        tableFullModel.getTableColumnsModels()[2] = column3;

        var res = tableFullModel.toJson();
        var expected = "{\"name\":\"Hello World\",\"columns\":[{\"name\":\"column1\",\"type\":\"string\"},{\"name\":\"column2\",\"type\":\"int\"},{\"name\":\"column3\",\"type\":\"boolean\"}],\"permissions\":{\"owner\":\"user1\",\"group\":\"group1\",\"other\":\"none\"}}";
        Assert.assertEquals(expected, res);
    }
}
