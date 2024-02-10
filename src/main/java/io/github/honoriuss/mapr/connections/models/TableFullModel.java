package io.github.honoriuss.mapr.connections.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author H0n0riuss
 */
public class TableFullModel extends TableBaseModel {
    @JsonProperty("columns")
    private TableColumnsModel[] tableColumnsModels;
    @JsonProperty("permissions")
    private TablePermissionModel tablePermissionModel;

    public TableColumnsModel[] getTableColumnsModels() {
        return tableColumnsModels;
    }

    public void setTableColumnsModels(TableColumnsModel[] tableColumnsModels) {
        this.tableColumnsModels = tableColumnsModels;
    }

    public TablePermissionModel getTablePermissionModel() {
        return tablePermissionModel;
    }

    public void setTablePermissionModel(TablePermissionModel tablePermissionModel) {
        this.tablePermissionModel = tablePermissionModel;
    }
}
