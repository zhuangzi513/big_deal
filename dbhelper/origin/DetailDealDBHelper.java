package dbhelper.origin;

import common.DetailDealElement;
import dbhelper.DBHelper;

import java.util.*;

public class DetailDealDBHelper extends DBHelper<DetailDealElement> {
    private static final String INSERT_SQL = "INSERT INTO %s (Date, Price, PriceUp, Volume, TurnOver, IsBuy) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String CREATE_TABLE_SQL_FORMAT = "CREATE TABLE IF NOT EXISTS %s (Date varchar(10),  Price float, PriceUp float, Volume int, TurnOver int, IsBuy boolean) DEFAULT CHARSET=utf8";

   public DetailDealDBHelper(String dbName) {
       super(dbName);
   }

    public void insertSingleDetailDealElementToTable(String table, DetailDealElement detailDealElement) {
        String insertSQL = String.format(INSERT_SQL, table);
        //System.out.println(insertSQL);
        insertSingleRecordsToTable(table, insertSQL, detailDealElement);
    }

    public void insertDetailDealElementsToTable(String table, Vector<DetailDealElement> detailDealElements) {
        String insertSQL = String.format(INSERT_SQL, table);
        //System.out.println(insertSQL);
        insertRecordsToTable(table, insertSQL, detailDealElements);
    }

    public void createOriginTable(String tableName) {
        String createTableSQL = String.format(CREATE_TABLE_SQL_FORMAT, tableName);
        //System.out.println(createTableSQL);
        createTableForType(tableName, createTableSQL, DetailDealElement.class);
    }
};
