package io.github.honoriuss.mapr.connections.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class TablePermissionsModelTest {
    @Test
    public void toJsonTest() {
        var tablePermissionModel = new TablePermissionModel();
        tablePermissionModel.setTableName("Hello World");
        var permissions = new PermissionModel();
        permissions.setOwner("user1");
        permissions.setGroup("group1");
        permissions.setOther("none");
        tablePermissionModel.setPermissionModel(permissions);
        var res = tablePermissionModel.toJson();
        var expected = "{\"name\":\"Hello World\",\"permissions\":{\"owner\":\"user1\",\"group\":\"group1\",\"other\":\"none\"}}";
        Assert.assertEquals(expected, res);
    }
}
