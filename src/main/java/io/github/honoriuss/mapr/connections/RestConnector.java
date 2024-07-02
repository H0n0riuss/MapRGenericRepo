package io.github.honoriuss.mapr.connections;

import io.github.honoriuss.mapr.connections.interfaces.ITableCreator;
import io.github.honoriuss.mapr.connections.models.TableBaseModel;
import io.github.honoriuss.mapr.utils.RestMapRUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * @author H0n0riuss
 */
@Service
public class RestConnector implements ITableCreator {
    private final Logger logger = Logger.getLogger(RestConnector.class.getSimpleName());

    @Value("${mapr.rest.baseUrl:https://localhost:}")
    private String maprDbRestUrl;
    @Value("${mapr.rest.port:8243}")
    private String port;
    @Value("${mapr.rest.api:/api/v2/table/}")
    private String api;

    public void setMaprDbRestUrl(String maprDbRestUrl) {
        this.maprDbRestUrl = maprDbRestUrl;
    }

    @Override
    public boolean createTable(TableBaseModel tableFullModel) {
        //return RestMapRUtils.createTable(maprDbRestUrl, port, api, tableFullModel.toJson());
        return false;
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
