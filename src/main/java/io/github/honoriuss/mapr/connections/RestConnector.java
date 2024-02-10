package io.github.honoriuss.mapr.connections;

import io.github.honoriuss.mapr.connections.models.TableFullModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author H0n0riuss
 */
@Service
public class RestConnector {
    private final Logger logger = Logger.getLogger(RestConnector.class.getSimpleName());

    @Value("${mapr.rest.url:http://localhost:8444/rest/table/create}")
    private String maprDbRestUrl;

    public void setMaprDbRestUrl(String maprDbRestUrl) {
        this.maprDbRestUrl = maprDbRestUrl;
    }

    public boolean createTable(TableFullModel tableFullModel) {
        return createTable(tableFullModel.toJson());
    }

    /**
     * Example for jsonTableBodyString: {"name":"example_table","columns":[{"name":"column1","type":"string"},{"name":"column2","type":"int"}]}
     * Min requirement: {"name":"example_table"}
     *
     * @param jsonTableBodyString: this is sent to the maprDbRestUrl
     * @return true if no exception occurs
     */
    public boolean createTable(String jsonTableBodyString) { //TODO name + column Model?
        try {
            var url = new URL(maprDbRestUrl);
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            var outputStream = connection.getOutputStream();
            outputStream.write(jsonTableBodyString.getBytes());
            outputStream.flush();

            var responseCode = connection.getResponseCode();
            logger.info("Response Code: " + responseCode);

            var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            var response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            logger.info("Response Body: " + response);

            connection.disconnect();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return false;
    }
}
