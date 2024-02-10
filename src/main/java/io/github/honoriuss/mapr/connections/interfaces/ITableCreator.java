package io.github.honoriuss.mapr.connections.interfaces;

import io.github.honoriuss.mapr.connections.models.TableBaseModel;

public interface ITableCreator {
    boolean createTable(TableBaseModel tableFullModel);
    boolean createTable(String jsonTableBodyString);
    boolean tableExists(String fullPath);
}
