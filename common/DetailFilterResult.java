package common;

import java.sql.*;

import java.util.Vector;

public class DetailFilterResult extends TableRecord {
    private String mDate;
    private float mW10PriceBuy;
    private int   mW10VolumeBuy;
    private int   mW10TurnOverBuy;
    private float mW10PriceSale;
    private int   mW10VolumeSale;
    private int   mW10TurnOverSale;

    private float mW20PriceBuy;
    private int   mW20VolumeBuy;
    private int   mW20TurnOverBuy;
    private float mW20PriceSale;
    private int   mW20VolumeSale;
    private int   mW20TurnOverSale;

    private float mW40PriceBuy;
    private int   mW40VolumeBuy;
    private int   mW40TurnOverBuy;
    private float mW40PriceSale;
    private int   mW40VolumeSale;
    private int   mW40TurnOverSale;

    private float mW80PriceBuy;
    private int   mW80VolumeBuy;
    private int   mW80TurnOverBuy;
    private float mW80PriceSale;
    private int   mW80VolumeSale;
    private int   mW80TurnOverSale;

    private float mMTW80PriceBuy;
    private int   mMTW80VolumeBuy;
    private int   mMTW80TurnOverBuy;
    private float mMTW80PriceSale;
    private int   mMTW80VolumeSale;
    private int   mMTW80TurnOverSale;

    public static DetailFilterResult constructFromResultSet(ResultSet resultSet) {
        DetailFilterResult detailFilterResult = null;
        try {
            detailFilterResult = new DetailFilterResult(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return detailFilterResult;
    }

    public static DetailFilterResult sumOfChildDetailFilterResults(Vector<DetailFilterResult> childDetailFilterResults) {
        int sizeOfFilterResults = childDetailFilterResults.size();
        int   tW10VolumeBuy = 0, tW10VolumeSal = 0, tW10TurnOverBuy = 0, tW10TurnOverSale = 0;
        float tW10PriceBuy  = 0.0f, tW10PriceSale = 0.0f;

        int   tW20VolumeBuy = 0, tW20VolumeSal = 0, tW20TurnOverBuy = 0, tW20TurnOverSale = 0;
        float tW20PriceBuy  = 0.0f, tW20PriceSale = 0.0f;

        int   tW40VolumeBuy = 0, tW40VolumeSal = 0, tW40TurnOverBuy = 0, tW40TurnOverSale = 0;
        float tW40PriceBuy  = 0.0f, tW40PriceSale = 0.0f;

        int   tW80VolumeBuy = 0, tW80VolumeSal = 0, tW80TurnOverBuy = 0, tW80TurnOverSale = 0;
        float tW80PriceBuy  = 0.0f, tW80PriceSale = 0.0f;

        int   tMTW80VolumeBuy = 0, tMTW80VolumeSal = 0, tMTW80TurnOverBuy = 0, tMTW80TurnOverSale = 0;
        float tMTW80PriceBuy  = 0.0f, tMTW80PriceSale = 0.0f;

        DetailFilterResult singleDetailFilterResult = null;
        String date = new String("");;
        if (sizeOfFilterResults > 0) {
            System.out.println( childDetailFilterResults.get(0).mDate);
            date += childDetailFilterResults.get(0).mDate.substring(4, 8);
            date += "-";
            System.out.println( childDetailFilterResults.get(sizeOfFilterResults - 1).mDate);
            date += childDetailFilterResults.get(sizeOfFilterResults - 1).mDate.substring(4, 8);
        }
        for (int i = 0; i < sizeOfFilterResults; ++i) {
            singleDetailFilterResult = childDetailFilterResults.get(i);

            tW10VolumeBuy      += singleDetailFilterResult.mW10VolumeBuy;
            tW10TurnOverBuy    += singleDetailFilterResult.mW10TurnOverBuy;    
            tW10VolumeSal      += singleDetailFilterResult.mW10VolumeSale;
            tW10TurnOverSale   += singleDetailFilterResult.mW10TurnOverSale;

            tW20VolumeBuy      += singleDetailFilterResult.mW20VolumeBuy;
            tW20TurnOverBuy    += singleDetailFilterResult.mW20TurnOverBuy;    
            tW20VolumeSal      += singleDetailFilterResult.mW20VolumeSale;
            tW20TurnOverSale   += singleDetailFilterResult.mW20TurnOverSale;

            tW40VolumeBuy      += singleDetailFilterResult.mW40VolumeBuy;
            tW40TurnOverBuy    += singleDetailFilterResult.mW40TurnOverBuy;    
            tW40VolumeSal      += singleDetailFilterResult.mW40VolumeSale;
            tW40TurnOverSale   += singleDetailFilterResult.mW40TurnOverSale;

            tW80VolumeBuy      += singleDetailFilterResult.mW80VolumeBuy;
            tW80TurnOverBuy    += singleDetailFilterResult.mW80TurnOverBuy;    
            tW80VolumeSal      += singleDetailFilterResult.mW80VolumeSale;
            tW80TurnOverSale   += singleDetailFilterResult.mW80TurnOverSale;

            tMTW80VolumeBuy    += singleDetailFilterResult.mMTW80VolumeBuy;
            tMTW80TurnOverBuy  += singleDetailFilterResult.mMTW80TurnOverBuy;    
            tMTW80VolumeSal    += singleDetailFilterResult.mMTW80VolumeSale;
            tMTW80TurnOverSale += singleDetailFilterResult.mMTW80TurnOverSale;
        }

        tW10PriceBuy  = tW10TurnOverBuy /(tW10VolumeBuy * 100 + 1.0f);
        tW10PriceSale = tW10TurnOverSale/(tW10VolumeSal * 100 + 1.0f);

        tW20PriceBuy  = tW20TurnOverBuy /(tW20VolumeBuy * 100 + 1.0f);
        tW20PriceSale = tW20TurnOverSale/(tW20VolumeSal * 100 + 1.0f);

        tW40PriceBuy  = tW40TurnOverBuy /(tW40VolumeBuy * 100 + 1.0f);
        tW40PriceSale = tW40TurnOverSale/(tW40VolumeSal * 100 + 1.0f);

        tW80PriceBuy  = tW80TurnOverBuy /(tW80VolumeBuy * 100 + 1.0f);
        tW80PriceSale = tW80TurnOverSale/(tW80VolumeSal * 100 + 1.0f);

        tMTW80PriceBuy  = tMTW80TurnOverBuy /(tMTW80VolumeBuy * 100 + 1.0f);
        tMTW80PriceSale = tMTW80TurnOverSale/(tMTW80VolumeSal * 100 + 1.0f);
        return new DetailFilterResult(date,
                                      tW10PriceBuy,   tW10VolumeBuy,   tW10TurnOverBuy,   tW10PriceSale,   tW10VolumeSal,   tW10TurnOverSale,
                                      tW20PriceBuy,   tW20VolumeBuy,   tW20TurnOverBuy,   tW20PriceSale,   tW20VolumeSal,   tW20TurnOverSale,
                                      tW40PriceBuy,   tW40VolumeBuy,   tW40TurnOverBuy,   tW40PriceSale,   tW40VolumeSal,   tW40TurnOverSale,
                                      tW80PriceBuy,   tW80VolumeBuy,   tW80TurnOverBuy,   tW80PriceSale,   tW80VolumeSal,   tW80TurnOverSale,
                                      tMTW80PriceBuy, tMTW80VolumeBuy, tMTW80TurnOverBuy, tMTW80PriceSale, tMTW80VolumeSal, tMTW80TurnOverSale);
    }

    public DetailFilterResult(ResultSet resultSet) throws SQLException {
         mDate              = resultSet.getString(1); 
         mW10PriceBuy       = resultSet.getFloat(2);  
         mW10VolumeBuy      = resultSet.getInt(3);    
         mW10TurnOverBuy    = resultSet.getInt(4);    
         mW10PriceSale      = resultSet.getFloat(5);  
         mW10VolumeSale     = resultSet.getInt(6);    
         mW10TurnOverSale   = resultSet.getInt(7);    

         mW20PriceBuy       = resultSet.getFloat(8);  
         mW20VolumeBuy      = resultSet.getInt(9);    
         mW20TurnOverBuy    = resultSet.getInt(10);    
         mW20PriceSale      = resultSet.getFloat(11);  
         mW20VolumeSale     = resultSet.getInt(12);    
         mW20TurnOverSale   = resultSet.getInt(13);    

         mW40PriceBuy       = resultSet.getFloat(14); 
         mW40VolumeBuy      = resultSet.getInt(15);    
         mW40TurnOverBuy    = resultSet.getInt(16);   
         mW40PriceSale      = resultSet.getFloat(17); 
         mW40VolumeSale     = resultSet.getInt(18);    
         mW40TurnOverSale   = resultSet.getInt(19);   

         mW80PriceBuy       = resultSet.getFloat(20); 
         mW80VolumeBuy      = resultSet.getInt(21);    
         mW80TurnOverBuy    = resultSet.getInt(22);   
         mW80PriceSale      = resultSet.getFloat(23); 
         mW80VolumeSale     = resultSet.getInt(24);    
         mW80TurnOverSale   = resultSet.getInt(25);   

         mMTW80PriceBuy     = resultSet.getFloat(26); 
         mMTW80VolumeBuy    = resultSet.getInt(27);    
         mMTW80TurnOverBuy  = resultSet.getInt(28);   
         mMTW80PriceSale    = resultSet.getFloat(29); 
         mMTW80VolumeSale   = resultSet.getInt(30);    
         mMTW80TurnOverSale = resultSet.getInt(31);   
    }

    public DetailFilterResult(String date,
                              float w10PriceBuy,   int w10VolumeBuy,     int w10TurnOverBuy,   float w10PriceSale,   int w10VolumeSale,   int w10TurnOverSale,
                              float w20PriceBuy,   int w20VolumeBuy,     int w20TurnOverBuy,   float w20PriceSale,   int w20VolumeSale,   int w20TurnOverSale,
                              float w40PriceBuy,   int w40VolumeBuy,     int w40TurnOverBuy,   float w40PriceSale,   int w40VolumeSale,   int w40TurnOverSale,
                              float w80PriceBuy,   int w80VolumeBuy,     int w80TurnOverBuy,   float w80PriceSale,   int w80VolumeSale,   int w80TurnOverSale,
                              float mtw80PriceBuy, int mtw80VolumeBuy,   int mtw80TurnOverBuy, float mtw80PriceSale, int mtw80VolumeSale, int mtw80TurnOverSale) {
         mDate            = date;
         mW10PriceBuy     = w10PriceBuy;
         mW10VolumeBuy    = w10VolumeBuy;
         mW10TurnOverBuy  = w10TurnOverBuy;
         mW10PriceSale    = w10PriceSale;
         mW10VolumeSale   = w10VolumeSale;
         mW10TurnOverSale = w10TurnOverSale;

         mW20PriceBuy     = w20PriceBuy;
         mW20VolumeBuy    = w20VolumeBuy;
         mW20TurnOverBuy  = w20TurnOverBuy;
         mW20PriceSale    = w20PriceSale;
         mW20VolumeSale   = w20VolumeSale;
         mW20TurnOverSale = w20TurnOverSale;

         mW40PriceBuy     = w40PriceBuy;
         mW40VolumeBuy    = w40VolumeBuy;
         mW40TurnOverBuy  = w40TurnOverBuy;
         mW40PriceSale    = w40PriceSale;
         mW40VolumeSale   = w40VolumeSale;
         mW40TurnOverSale = w40TurnOverSale;

         mW80PriceBuy     = w80PriceBuy;
         mW80VolumeBuy    = w80VolumeBuy;
         mW80TurnOverBuy  = w80TurnOverBuy;
         mW80PriceSale    = w80PriceSale;
         mW80VolumeSale   = w80VolumeSale;
         mW80TurnOverSale = w80TurnOverSale;

         mMTW80PriceBuy     = mtw80PriceBuy;
         mMTW80VolumeBuy    = mtw80VolumeBuy;
         mMTW80TurnOverBuy  = mtw80TurnOverBuy;
         mMTW80PriceSale    = mtw80PriceSale;
         mMTW80VolumeSale   = mtw80VolumeSale;
         mMTW80TurnOverSale = mtw80TurnOverSale;
    }

    public void bindToSQLStatement(PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, mDate);

        insertStatement.setFloat(2, mW10PriceBuy);
        insertStatement.setInt(3,   mW10VolumeBuy);
        insertStatement.setInt(4,   mW10TurnOverBuy);
        insertStatement.setFloat(5, mW10PriceSale);
        insertStatement.setInt(6,   mW10VolumeSale);
        insertStatement.setInt(7,   mW10TurnOverSale);

        insertStatement.setFloat(8, mW20PriceBuy);
        insertStatement.setInt(9,   mW20VolumeBuy);
        insertStatement.setInt(10,  mW20TurnOverBuy);
        insertStatement.setFloat(11,mW20PriceSale);
        insertStatement.setInt(12,  mW20VolumeSale);
        insertStatement.setInt(13,  mW20TurnOverSale);

        insertStatement.setFloat(14, mW40PriceBuy);
        insertStatement.setInt(15,   mW40VolumeBuy);
        insertStatement.setInt(16,   mW40TurnOverBuy);
        insertStatement.setFloat(17, mW40PriceSale);
        insertStatement.setInt(18,   mW40VolumeSale);
        insertStatement.setInt(19,   mW40TurnOverSale);

        insertStatement.setFloat(20, mW80PriceBuy);
        insertStatement.setInt(21,   mW80VolumeBuy);
        insertStatement.setInt(22,   mW80TurnOverBuy);
        insertStatement.setFloat(23, mW80PriceSale);
        insertStatement.setInt(24,   mW80VolumeSale);
        insertStatement.setInt(25,   mW80TurnOverSale);

        insertStatement.setFloat(26, mMTW80PriceBuy);
        insertStatement.setInt(27,   mMTW80VolumeBuy);
        insertStatement.setInt(28,   mMTW80TurnOverBuy);
        insertStatement.setFloat(29, mMTW80PriceSale);
        insertStatement.setInt(30,   mMTW80VolumeSale);
        insertStatement.setInt(31,   mMTW80TurnOverSale);
    }

    public void dump() {
        String element = new String("");
        element += " \nmDate            =: " + mDate           ;
        element += " \nmW10PriceBuy     =: " + mW10PriceBuy    ;
        element += " \nmW10VolumeBuy    =: " + mW10VolumeBuy   ;
        element += " \nmW10TurnOverBuy  =: " + mW10TurnOverBuy ;
        element += " \nmW10PriceSale    =: " + mW10PriceSale   ;
        element += " \nmW10VolumeSale   =: " + mW10VolumeSale  ;
        element += " \nmW10TurnOverSale =: " + mW10TurnOverSale;
        element += "\n";

        element += " \nmW20PriceBuy     =: " + mW20PriceBuy    ;
        element += " \nmW20VolumeBuy    =: " + mW20VolumeBuy   ;
        element += " \nmW20TurnOverBuy  =: " + mW20TurnOverBuy ;
        element += " \nmW20PriceSale    =: " + mW20PriceSale   ;
        element += " \nmW20VolumeSale   =: " + mW20VolumeSale  ;
        element += " \nmW20TurnOverSale =: " + mW20TurnOverSale;
        element += "\n";

        element += " \nmW40PriceBuy     =: " + mW40PriceBuy    ;
        element += " \nmW40VolumeBuy    =: " + mW40VolumeBuy   ;
        element += " \nmW40TurnOverBuy  =: " + mW40TurnOverBuy ;
        element += " \nmW40PriceSale    =: " + mW40PriceSale   ;
        element += " \nmW40VolumeSale   =: " + mW40VolumeSale  ;
        element += " \nmW40TurnOverSale =: " + mW40TurnOverSale;
        element += "\n";

        element += " \nmW80PriceBuy     =: " + mW80PriceBuy    ;
        element += " \nmW80VolumeBuy    =: " + mW80VolumeBuy   ;
        element += " \nmW80TurnOverBuy  =: " + mW80TurnOverBuy ;
        element += " \nmW80PriceSale    =: " + mW80PriceSale   ;
        element += " \nmW80VolumeSale   =: " + mW80VolumeSale  ;
        element += " \nmW80TurnOverSale =: " + mW80TurnOverSale;
        element += "\n";

        element += " \nmMTW80PriceBuy     =: " + mMTW80PriceBuy    ;
        element += " \nmMTW80VolumeBuy    =: " + mMTW80VolumeBuy   ;
        element += " \nmMTW80TurnOverBuy  =: " + mMTW80TurnOverBuy ;
        element += " \nmMTW80PriceSale    =: " + mMTW80PriceSale   ;
        element += " \nmMTW80VolumeSale   =: " + mMTW80VolumeSale  ;
        element += " \nmMTW80TurnOverSale =: " + mMTW80TurnOverSale;
        element += "\n";
        System.out.println(element);
    }
};
