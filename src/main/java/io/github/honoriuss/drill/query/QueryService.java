package io.github.honoriuss.drill.query;

import io.github.honoriuss.drill.connection.DrillConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryService {
    public ResultSet ExecuteQuery(String query) throws SQLException {
        Statement st = DrillConnection.getConnection().createStatement();
        return st.executeQuery(query);
    }

    public DrillResult getQueryResult(String query) throws SQLException {
        DrillResult result = null;
        var distinctQueries = query.split(";");
        try (Connection drillConnection = DrillConnection.getConnection()){
            for(var distinctQuery : distinctQueries){
                try(var statement = drillConnection.createStatement()){
                    try(var resultSet = statement.executeQuery(distinctQuery)){
                        result = new DrillResult(resultSet);
                    }
                }
            }
        }
        return result;
    }
}
