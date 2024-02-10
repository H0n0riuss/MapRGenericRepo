package io.github.honoriuss.mapr.connections.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author H0n0riuss
 */
public class TableFullModel extends TableBaseModel {
    @JsonProperty("columns")
    private TableColumnsModel[] tableColumnsModels;
    @JsonProperty("permissions")
    private PermissionModel permissionModel;

    public TableColumnsModel[] getTableColumnsModels() {
        return tableColumnsModels;
    }

    public void setTableColumnsModels(TableColumnsModel[] tableColumnsModels) {
        this.tableColumnsModels = tableColumnsModels;
    }

    public PermissionModel getPermissionModel() {
        return permissionModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }
}
