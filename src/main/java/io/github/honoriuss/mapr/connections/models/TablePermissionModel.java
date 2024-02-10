package io.github.honoriuss.mapr.connections.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author H0n0riuss
 */
public class TablePermissionModel extends TableBaseModel {
    @JsonProperty("permissions")
    private PermissionModel permissionModel;

    public PermissionModel getPermissionModel() {
        return permissionModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }
}
