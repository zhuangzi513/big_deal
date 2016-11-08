package common;

import java.sql.*;

public class DetailDealElement extends TableRecord {
    public static final int IS_BUY_DEFAULT = -1;
    public static final int IS_BUY_TRUE = 1;
    public static final int IS_BUY_FALSE = 2;
    public final String mDate;
    public final float mPrice;
    public final float mPriceUp;
    public final int mVolume;
    public final int mTurnOver;
    public final int mIsBuy;

    public DetailDealElement(String date, float price, float priceUp, int volume, int turnOver, int isBuy) {
        mDate = date;
        mPrice = price;
        mVolume = volume;
        mPriceUp = priceUp;
        mTurnOver = turnOver;
        mIsBuy = isBuy;
    }

    public void bindToSQLStatement(PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, mDate);
        insertStatement.setFloat(2, mPrice);
        insertStatement.setFloat(3, mPriceUp);
        insertStatement.setInt(4, mVolume);
        insertStatement.setInt(5, mTurnOver);
        insertStatement.setInt(6, mIsBuy);
    }

    public void dump() {
        String element = new String("");
        element += " date: " + mDate;
        element += " price: " + mPrice;
        element += " volume: " + mVolume;
        element += " priceup: " + mPriceUp;
        element += " turnover: " + mTurnOver;
        element += " isbuy: " + mIsBuy;
        System.out.println(element);
    }
}
