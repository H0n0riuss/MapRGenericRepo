package io.github.honoriuss.mapr.connections.connectors;

import io.github.honoriuss.mapr.connections.interfaces.ITableCreator;
import io.github.honoriuss.mapr.connections.models.TableBaseModel;
import io.github.honoriuss.mapr.utils.RestMapRUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @author H0n0riuss
 */
@Service
public class RestConnector implements ITableCreator {
    private final Logger logger = Logger.getLogger(RestConnector.class.getSimpleName());

    @Value("${mapr.rest.url:http://localhost:8444}")
    private String maprDbRestUrl;

    public void setMaprDbRestUrl(String maprDbRestUrl) {
        this.maprDbRestUrl = maprDbRestUrl;
    }

    @Override
    public boolean createTable(TableBaseModel tableFullModel) {
        return RestMapRUtils.createTable(maprDbRestUrl, tableFullModel.toJson());
    }

    /**
     * Example for jsonTableBodyString: {"name":"example_table","columns":[{"name":"column1","type":"string"},{"name":"column2","type":"int"}]}
     * Min requirement: {"name":"example_table"}
     *
     * @param jsonTableBodyString: this is sent to the maprDbRestUrl
     * @return true if no exception occurs
     */
    @Override
    public boolean createTable(String jsonTableBodyString) { //TODO authorization?
        return RestMapRUtils.createTable(maprDbRestUrl, jsonTableBodyString);
    }

    @Override
    public boolean tableExists(String fullPath) {
        return RestMapRUtils.tableExists(maprDbRestUrl, fullPath);
    }
}
