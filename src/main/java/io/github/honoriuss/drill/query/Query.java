package io.github.honoriuss.drill.query;

import io.github.honoriuss.drill.connection.DrillConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Query {
    public ResultSet ExecuteQuery(String query) throws SQLException {
        Statement st = DrillConnection.getConnection().createStatement();
        return st.executeQuery(query);
    }
}
