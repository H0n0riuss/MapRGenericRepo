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

    public static boolean createTable(String maprDbRestUrl, String port, TableBaseModel tableFullModel, String username, String password) {
        //return createTable(maprDbRestUrl, port, tableFullModel.toJson(), username, password);
        return false;
    }

    /**
     * Example for jsonTableBodyString: {"name":"example_table","columns":[{"name":"column1","type":"string"},{"name":"column2","type":"int"}]}
     * Min requirement: {"name":"example_table"}
     *
     * @param jsonTableBodyString: this is sent to the maprDbRestUrl
     * @return true if no exception occurs
     */
    public static boolean createTable(String maprDbRestUrl, String port, String api, String jsonTableBodyString, String username, String password) { //TODO authorization?
        try {
            var url = maprDbRestUrl + ":" + port + api;

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Setzen von Request-Methode und Basic-Authentifizierung
                con.setRequestMethod("PUT");
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
                String authHeaderValue = "Basic " + new String(encodedAuth);
                con.setRequestProperty("Authorization", authHeaderValue);

                int responseCode = con.getResponseCode();
                logger.info("Response code: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Drucken der Antwort
                logger.info("Response body: " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }


            logger.info("Response Body: " );

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
