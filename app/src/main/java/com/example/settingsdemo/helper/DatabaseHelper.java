package com.example.settingsdemo.helper;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    private static final int LOGIN_TIMEOUT = 5;

    private final String _server;
    private final String _database;
    private final String _user;
    private final String _password;
    private static final String DEBUG_TAG = "DatabaseHelper";

    public DatabaseHelper(String server, String database, String user, String password) {
        _server = server;
        _database = database;
        _user = user;
        _password = password;
    }

    public Connection getSqlConnection() {
        Connection conn = null;
        String ConnURL =
                "jdbc:jtds:sqlserver://" + _server + ":1433" +
                        ";databaseName=" + _database +
                        ";user=" + _user +
                        ";password=" + _password + ";";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.setLoginTimeout(LOGIN_TIMEOUT);
            conn = DriverManager.getConnection(ConnURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }

    public void executeNonQuerySQLString(String sql) throws SQLException {
        String component = "executeNonQuerySQLString";
        DebugLogger.log(DEBUG_TAG,component,sql);
        try (Connection connection = getSqlConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, String> executeQuerySQLString(String sql) throws SQLException {
        String component = "executeQuerySQLString";
        DebugLogger.log(DEBUG_TAG,component,sql);
        Map<String, String> map = null;
        try (Connection connection = getSqlConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            map = GetMapFromResultSet(resultSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
    public Map<Integer, Map<String, String>> executeQuerySQLStringMultiRow(String sql) throws SQLException {
        String component = "executeQuerySQLString";
        DebugLogger.log(DEBUG_TAG,component,sql);
        Map<Integer, Map<String, String>> map = null;
        try (Connection connection = getSqlConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            map = GetMapOfMapsFromResultSet(resultSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    private Map<Integer, Map<String, String>> GetMapOfMapsFromResultSet(ResultSet resultSet) {
        Map<Integer, Map<String, String>> rows = new HashMap<>();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            int rowCount = 0;
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(resultSetMetaData.getColumnLabel(i), resultSet.getString(i));
                }
                rows.put(rowCount, row);
                rowCount++;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return rows;
    }

    private Map<String, String> GetMapFromResultSet(ResultSet resultSet) {
        Map<String, String> map = new HashMap<>();
        try{
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int column_count = resultSetMetaData.getColumnCount();
            while (resultSet.next()){
                for (int i = 1; i<=column_count; i++){
                    map.put(resultSetMetaData.getColumnLabel(i), resultSet.getString(i));
                }
            }
            map.put("row_count", String.valueOf(resultSet.getRow() + 1));
            map.put("exception", "");
        }
        catch (Exception e){
            map.put("exception", e.getMessage());
        }
        return map;
    }
}
