package dbhelper.origin;

import common.DetailDealElement;
import dbhelper.DBHelper;

import java.sql.*;

import java.util.*;

public class DetailDealDBHelper extends DBHelper<DetailDealElement> {
    private static final String INSERT_SQL = "INSERT INTO %s (Date, Price, PriceUp, Volume, TurnOver, IsBuy) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String CREATE_TABLE_SQL_FORMAT = "CREATE TABLE IF NOT EXISTS %s (Date varchar(10),  Price float, PriceUp float, Volume int, TurnOver int, IsBuy int) DEFAULT CHARSET=utf8";
    private static final String SELECT_ALL_COLS_OF_TABLE = "SELECT * FROM %s ;";

    private static final String DETAIL_DEAL_TABLE_NAME_PREFIX = "D";
    private static final String DETAIL_DEAL_DATABASE_NAME_PREFIX = "D";

   public DetailDealDBHelper(String dbName) {
       super(DETAIL_DEAL_DATABASE_NAME_PREFIX + dbName);
   }

    public void insertSingleDetailDealElementToTable(String table, DetailDealElement detailDealElement) {
        String insertSQL = String.format(INSERT_SQL, DETAIL_DEAL_TABLE_NAME_PREFIX + table);
        //System.out.println(insertSQL);
        insertSingleRecordsToTable(table, insertSQL, detailDealElement);
    }

    public void insertDetailDealElementsToTable(String table, Vector<DetailDealElement> detailDealElements) {
        String insertSQL = String.format(INSERT_SQL, DETAIL_DEAL_TABLE_NAME_PREFIX + table);
        //System.out.println(insertSQL);
        insertRecordsToTable(table, insertSQL, detailDealElements);
    }

    public void createOriginTable(String tableName) {
        String createTableSQL = String.format(CREATE_TABLE_SQL_FORMAT, DETAIL_DEAL_TABLE_NAME_PREFIX + tableName);
        //System.out.println(createTableSQL);
        createTableForType(DETAIL_DEAL_TABLE_NAME_PREFIX + tableName, createTableSQL, DetailDealElement.class);
    }

    public String getLatestTableName() {
        String latestTableName = getLatestTableName(DETAIL_DEAL_TABLE_NAME_PREFIX);
        if (latestTableName != null) {
            return latestTableName.substring(DETAIL_DEAL_TABLE_NAME_PREFIX.length(), latestTableName.length());
        }
        return null;
    }

   public Vector<DetailDealElement> getAllInnerElementsFromTable(String tableName) {
       Vector<DetailDealElement> detailsDealElements = new Vector<DetailDealElement>();
       String selectSQL = String.format(SELECT_ALL_COLS_OF_TABLE, DETAIL_DEAL_TABLE_NAME_PREFIX + tableName);
       ResultSet resultSet = selectColsFromTable(DETAIL_DEAL_TABLE_NAME_PREFIX + tableName, selectSQL);
       try {
           while (resultSet.next()) {
              String date = resultSet.getString(1);
              float price = resultSet.getFloat(2);
              float priceUp = resultSet.getFloat(3);
              int volume = resultSet.getInt(4);
              int turnOver = resultSet.getInt(5);
              int isBuy = resultSet.getInt(6);
              DetailDealElement tmp = new DetailDealElement(date, price, priceUp, volume, turnOver, isBuy);
              detailsDealElements.add(tmp);
              //tmp.dump();
           }
           return detailsDealElements;
       } catch(SQLException e) {
           e.printStackTrace();
           System.exit(1);
       }

       return null;
   }

   public Vector<String> getAllExistingDetailDealTableNames() {
       Vector<String> existingDetailDealTableNames = new Vector<String>();
       ResultSet resultSet = getAllExistingTables();
       String singleTableName = null;

       try {
           while (resultSet.next()) {
               singleTableName = resultSet.getString(1);
               if (singleTableName.startsWith(DETAIL_DEAL_TABLE_NAME_PREFIX)) {
                   existingDetailDealTableNames.add(singleTableName.substring(DETAIL_DEAL_TABLE_NAME_PREFIX.length(), singleTableName.length()));
               }
           }

           return existingDetailDealTableNames;
       } catch(SQLException e) {
           e.printStackTrace();
           System.exit(2);
       }

       return null;
   }

};
