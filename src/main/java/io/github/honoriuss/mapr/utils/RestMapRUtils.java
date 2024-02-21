package io.github.honoriuss.mapr.utils;

import io.github.honoriuss.mapr.connections.models.TableBaseModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * @author H0n0riuss
 */
public abstract class RestMapRUtils {
    private static final Logger logger = Logger.getLogger(RestMapRUtils.class.getSimpleName());

    public static boolean createTable(String maprDbRestUrl, TableBaseModel tableFullModel) {
        return createTable(maprDbRestUrl, tableFullModel.toJson());
    }

    /**
     * Example for jsonTableBodyString: {"name":"example_table","columns":[{"name":"column1","type":"string"},{"name":"column2","type":"int"}]}
     * Min requirement: {"name":"example_table"}
     *
     * @param jsonTableBodyString: this is sent to the maprDbRestUrl
     * @return true if no exception occurs
     */
    public static boolean createTable(String maprDbRestUrl, String jsonTableBodyString) { //TODO authorization?
        try {
            var url = new URL(maprDbRestUrl + "/rest/table/create");
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
            logger.warning(e.getMessage());
        }
        return false;
    }

    public static boolean tableExists(String maprDbRestUrl, String fullPath) {
        try {
            //var urlStr = maprDbRestUrl + "/rest/table/info?name=" + tableName; //TODO tablename or fullpath?
            var urlStr = maprDbRestUrl + "/rest/table/info?name=" + fullPath;
            var url = new URL(urlStr);

            var conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            var authString = "username" + ":" + "password"; //TODO other auths implement?
            var encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuthString);

            var responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return false;
    }
}
