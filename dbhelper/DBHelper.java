package dbhelper;

import java.sql.*;
import java.util.*;

import common.*;

public class DBHelper<T extends TableRecord> {
    private static final int MAX_CONNECT_TRIES = 5;
    private static final String DB_DIR = "jdbc:mysql://127.0.0.1:3306/";
    private static final String DB_URL_POST = "?useUnicode=true&characterEncoding=utf-8";
    private static final String TABLE_NAME = "shares_info";
    private static final String USER_NAME = "root";
    private static final String PASS_WD = "123456";
    private static Connection mServerConnection;

    private static final String CHECK_TABLE_SQL_FORMAT = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '%s' AND table_name = '%s'";
    private static final String CREATE_DATABASE_SQL_FORMAT = "CREATE DATABASE %s CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'";
    private static final String DELETE_DATABASE_SQL_FORMAT = "DROP DATABASE IF EXISTS %s";


    private Connection mDBConnection;
    private String mDBName;
    private String mDBUrl;

    private static void connectServer() {
        if (mServerConnection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                mServerConnection = DriverManager.getConnection(DB_DIR, USER_NAME, PASS_WD);
            } catch (SQLException se) {
                se.printStackTrace();
                //System.out.println("Fail to connect mysql server");
                //throw my Exception;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectToDB() {
        int tryConnectTimes = 0;
        while (mDBConnection == null &&
               tryConnectTimes++ < MAX_CONNECT_TRIES) {
            if (mServerConnection == null) {
                connectServer();
            } else {
                try {
                    Statement createDBStmt = mServerConnection.createStatement();
                    String createDBSql = String.format(CREATE_DATABASE_SQL_FORMAT, mDBName);
                    createDBStmt.executeUpdate(createDBSql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            try {
                Class.forName("com.mysql.jdbc.Driver");
                mDBConnection = DriverManager.getConnection(mDBUrl, USER_NAME, PASS_WD);
            } catch (SQLException se){
                //sLog.log(Level.INFO, "Fail to connect to :" + mDBUrl);
                se.printStackTrace();
                continue;
            } catch (ClassNotFoundException e) {
                //sLog.log(Level.INFO, e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    private void dispose() {
        if (mDBConnection != null) {
            try {
                mDBConnection.close();
            } catch (Exception e) {
            }
        }
    }


    protected DBHelper(String dbName) {
        mDBName = dbName;
        mDBUrl = DB_DIR + mDBName + DB_URL_POST;
        //TODO: if connection availiable?
        if (mServerConnection == null) {
            connectServer();
        }
        connectToDB();
    }

    protected boolean availableRecordForTable(String table, T exampleRecrod) {
        return true;
    } 

    protected void insertSingleRecordsToTable(String table, String sqlFormat, T singleRecord) {
        try {
            mDBConnection.setAutoCommit(false);
            PreparedStatement insertStatement = mDBConnection.prepareStatement(sqlFormat);
            ((TableRecord)singleRecord).bindToSQLStatement(insertStatement);
            insertStatement.execute();
            mDBConnection.commit();
            System.out.println("1 record inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void insertRecordsToTable(String table, String sqlFormat, Vector<T> rawRecords) {
        try {
            mDBConnection.setAutoCommit(false);
            PreparedStatement insertStatement = mDBConnection.prepareStatement(sqlFormat);
            int recordsCount = rawRecords.size();
            T singleRecord = null;
            for (int k = 0; k < recordsCount; ++k) {
                 singleRecord = rawRecords.get(k);
                 ((TableRecord)singleRecord).bindToSQLStatement(insertStatement);
                 insertStatement.addBatch();
            }

            insertStatement.executeBatch();
            mDBConnection.commit();
            System.out.println(recordsCount + " records inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void createTableForType(String tableName, String sqlFormat, Class type) {
        if (mDBConnection != null) {
            try {
                Statement createTableStmt = mDBConnection.createStatement();
                createTableStmt.executeUpdate(sqlFormat);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void checkTableExist(String tableName) {
    }
};