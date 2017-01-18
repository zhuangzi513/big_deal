package dbhelper.filter;

import common.DetailFilterResult;
import dbhelper.DBHelper;

import java.sql.*;
import java.util.*;

public class DetailsFilterDBHelper extends DBHelper<DetailFilterResult> {
    private static final String INSERT_SQL = "INSERT INTO %s (Date, "
                                              + "W10PriceBuy,   W10VolumeBuy,   W10TurnOverBuy,   W10PriceSale,   W10VolumeSale,   W10TurnOverSale, "
                                              + "W20PriceBuy,   W20VolumeBuy,   W20TurnOverBuy,   W20PriceSale,   W20VolumeSale,   W20TurnOverSale, "
                                              + "W40PriceBuy,   W40VolumeBuy,   W40TurnOverBuy,   W40PriceSale,   W40VolumeSale,   W40TurnOverSale, "
                                              + "W80PriceBuy,   W80VolumeBuy,   W80TurnOverBuy,   W80PriceSale,   W80VolumeSale,   W80TurnOverSale, "
                                              + "MTW80PriceBuy, MTW80VolumeBuy, MTW80TurnOverBuy, MTW80PriceSale, MTW80VolumeSale, MTW80TurnOverSale) "
                                              + "VALUES(?, "
                                              + "?, ?, ?, ?, ?, ?, "
                                              + "?, ?, ?, ?, ?, ?, "
                                              + "?, ?, ?, ?, ?, ?, "
                                              + "?, ?, ?, ?, ?, ?, "
                                              + "?, ?, ?, ?, ?, ?)";

    private static final String CREATE_TABLE_SQL_FORMAT = "CREATE TABLE IF NOT EXISTS %s (Date varchar(10), "
                                                          + "W10PriceBuy float,   W10VolumeBuy int,   W10TurnOverBuy int,   W10PriceSale float,   W10VolumeSale int,   W10TurnOverSale int, "
                                                          + "W20PriceBuy float,   W20VolumeBuy int,   W20TurnOverBuy int,   W20PriceSale float,   W20VolumeSale int,   W20TurnOverSale int, "
                                                          + "W40PriceBuy float,   W40VolumeBuy int,   W40TurnOverBuy int,   W40PriceSale float,   W40VolumeSale int,   W40TurnOverSale int, "
                                                          + "W80PriceBuy float,   W80VolumeBuy int,   W80TurnOverBuy int,   W80PriceSale float,   W80VolumeSale int,   W80TurnOverSale int, "
                                                          + "MTW80PriceBuy float, MTW80VolumeBuy int, MTW80TurnOverBuy int, MTW80PriceSale float, MTW80VolumeSale int, MTW80TurnOverSale int) "
                                                          + "DEFAULT CHARSET=utf8";

   private static final String SELECT_DATES_ALREADY_COMPUTED = "SELECT Date from %s";

   private static final String SELECT_FILTER_RESULTS_BETWEEN_DATES = "SELECT * from %s where Date >= %s and Date <= %s";

    private static final String FILTER_RESULT_TABLE_NAME = "FILTER_RESULT_TABLE";
    private static final String DETAIL_DEAL_TABLE_NAME_PREFIX = "D";

    public DetailsFilterDBHelper(String dbName) {
        super(DETAIL_DEAL_TABLE_NAME_PREFIX + dbName);
    }

    public void insertDetailsFilterResultsToTable(Vector<DetailFilterResult> detailFilterResults) {
        String insertSQL = String.format(INSERT_SQL, FILTER_RESULT_TABLE_NAME);
        insertRecordsToTable(FILTER_RESULT_TABLE_NAME, insertSQL, detailFilterResults);
    }

    private void createFilterResultTable(String tableName) {
        String createTableSQL = String.format(CREATE_TABLE_SQL_FORMAT, FILTER_RESULT_TABLE_NAME);
        //System.out.println(createTableSQL);
        createTableForType(FILTER_RESULT_TABLE_NAME, createTableSQL, DetailFilterResult.class);
    }

    public Vector<DetailFilterResult> getFilterResultsForDate(String dateBegin, String dateEnd) {
        String selectFilterResultsSQL = String.format(SELECT_FILTER_RESULTS_BETWEEN_DATES, FILTER_RESULT_TABLE_NAME, dateBegin, dateEnd);
        ResultSet resultSet = selectColsFromTable(FILTER_RESULT_TABLE_NAME, selectFilterResultsSQL);
        Vector<DetailFilterResult> retFilterResults = new Vector<DetailFilterResult>();
        try {
            int count = 1;
            DetailFilterResult singleFilterResult = null;
            while (resultSet.next()) {
                singleFilterResult = DetailFilterResult.constructFromResultSet(resultSet);
                retFilterResults.add(singleFilterResult);
            }
        } catch(Exception e) {
        }

        return retFilterResults;
    }

    public Vector<String> getExistingFilterResultDate() {
        createFilterResultTable(FILTER_RESULT_TABLE_NAME);
        Vector<String> existingFilterResultDates = new Vector<String>();
        String selectExitingDatesSQL = String.format(SELECT_DATES_ALREADY_COMPUTED, FILTER_RESULT_TABLE_NAME);
        ResultSet resultSet = selectColsFromTable(FILTER_RESULT_TABLE_NAME, selectExitingDatesSQL);
        try {
            while (resultSet.next()) {
                existingFilterResultDates.add(resultSet.getString(1));
            }
            return existingFilterResultDates;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
