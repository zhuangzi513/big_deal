package updator;

import common.DetailFilterResult;
import common.DetailDealElement;
import dbhelper.origin.DetailDealDBHelper;
import dbhelper.filter.DetailsFilterDBHelper;

import java.util.Vector;

public class FilterResultUpdator {
    private static final String DETAIL_DATABSE_NAMEPREFIX = "D";
    private static final String DETAIL_TABLE_NAMEPREFIX = "D";

    private class UpdateRunnable {
        private String mStockId;
        private Vector<String> mTableNamesToCompute;
        private Vector<DetailFilterResult> mFilterResultsToUpdate;

        public UpdateRunnable(String stockId, Vector<String> tableNamesToCompute) {
            mStockId = stockId;
            mTableNamesToCompute = tableNamesToCompute;
            mFilterResultsToUpdate = new Vector<DetailFilterResult>();
        }

        public void run() {
            DetailDealDBHelper detailDealHelper = new DetailDealDBHelper(mStockId);
            int sizeOfTablesNeedToCompute = mTableNamesToCompute.size();
            for (int i = 0; i < sizeOfTablesNeedToCompute; i++) {
                String targetTableName = mTableNamesToCompute.get(i);
                Vector<DetailDealElement> detailDealElements = detailDealHelper.getAllInnerElementsFromTable(targetTableName);
                //System.out.println("detailDealElements.size: " + detailDealElements.size());
                if (detailDealElements != null) {
                    DetailFilterResult detailFilterResult = computeFilterResultFromDetailDeals(targetTableName, detailDealElements);
                    //detailFilterResult.dump();
                    mFilterResultsToUpdate.add(detailFilterResult);
                }
            }

            DetailsFilterDBHelper detailFilterDBHelper = new DetailsFilterDBHelper(mStockId);
            if (mFilterResultsToUpdate != null) {
                detailFilterDBHelper.insertDetailsFilterResultsToTable(mFilterResultsToUpdate);
            }
        }

        private DetailFilterResult computeFilterResultFromDetailDeals(String date, Vector<DetailDealElement> detailDealElements) {
            int   tW10VolumeBuy = 0, tW10VolumeSale = 0, tW10TurnOverBuy = 0, tW10TurnOverSale = 0;
            float tW10PriceBuy  = 0.0f, tW10PriceSale = 0.0f;

            int   tW20VolumeBuy = 0, tW20VolumeSale = 0, tW20TurnOverBuy = 0, tW20TurnOverSale = 0;
            float tW20PriceBuy  = 0.0f, tW20PriceSale = 0.0f;

            int   tW40VolumeBuy = 0, tW40VolumeSale = 0, tW40TurnOverBuy = 0, tW40TurnOverSale = 0;
            float tW40PriceBuy  = 0.0f, tW40PriceSale = 0.0f;

            int   tW80VolumeBuy = 0, tW80VolumeSale = 0, tW80TurnOverBuy = 0, tW80TurnOverSale = 0;
            float tW80PriceBuy  = 0.0f, tW80PriceSale = 0.0f;

            int   tMTW80VolumeBuy = 0, tMTW80VolumeSale = 0, tMTW80TurnOverBuy = 0, tMTW80TurnOverSale = 0;
            float tMTW80PriceBuy  = 0.0f, tMTW80PriceSale = 0.0f;

            int detailDealsSize = detailDealElements.size();
            for (int j = 0; j < detailDealsSize; j++) {
                DetailDealElement detailDealElement = detailDealElements.get(j);
                //detailDealElement.dump();
                ////System.out.println("detailDealElement.mTurnOver: " + detailDealElement.mTurnOver);

                //10w ~= 128 * 1024 = 2^17
                int devideBy10W = detailDealElement.mTurnOver >> 17;
                if (devideBy10W <= 0) {
                    continue;
                }
                ////System.out.println("devideBy10W: " + devideBy10W);
                switch (devideBy10W) {
                   //10W
                   case 1:
                   //System.out.println("10W");
                   if (detailDealElement.mIsBuy == DetailDealElement.IS_BUY_TRUE) {
                       tW10VolumeBuy += detailDealElement.mVolume;
                       tW10TurnOverBuy += detailDealElement.mTurnOver;
                   } else if (detailDealElement.mIsBuy != DetailDealElement.IS_BUY_FALSE) {
                       tW10VolumeSale += detailDealElement.mVolume;
                       tW10TurnOverSale += detailDealElement.mTurnOver;
                   }
                   break; 
         

                   //20W
                   case 2:
                   //System.out.println("20W");
                   if (detailDealElement.mIsBuy == DetailDealElement.IS_BUY_TRUE) {
                       tW20VolumeBuy += detailDealElement.mVolume;
                       tW20TurnOverBuy += detailDealElement.mTurnOver;
                   } else if (detailDealElement.mIsBuy != DetailDealElement.IS_BUY_FALSE) {
                       tW20VolumeSale += detailDealElement.mVolume;
                       tW20TurnOverSale += detailDealElement.mTurnOver;
                   }
                   break; 

                   //40W
                   case 3:
                   case 4:
                   //System.out.println("40W");
                   if (detailDealElement.mIsBuy == DetailDealElement.IS_BUY_TRUE) {
                       tW40VolumeBuy += detailDealElement.mVolume;
                       tW40TurnOverBuy += detailDealElement.mTurnOver;
                   } else if (detailDealElement.mIsBuy != DetailDealElement.IS_BUY_FALSE) {
                       tW40VolumeSale += detailDealElement.mVolume;
                       tW40TurnOverSale += detailDealElement.mTurnOver;
                   }
                   break;

                   //80W
                   case 5:
                   case 6:
                   case 7:
                   case 8:
                   //System.out.println("80W");
                   if (detailDealElement.mIsBuy == DetailDealElement.IS_BUY_TRUE) {
                       tW80VolumeBuy += detailDealElement.mVolume;
                       tW80TurnOverBuy += detailDealElement.mTurnOver;
                   } else if (detailDealElement.mIsBuy != DetailDealElement.IS_BUY_FALSE) {
                       tW80VolumeSale += detailDealElement.mVolume;
                       tW80TurnOverSale += detailDealElement.mTurnOver;
                   }
                   break;

                   //>80W
                   default:
                       ////System.out.println("more than 80W");
                       if (detailDealElement.mIsBuy == DetailDealElement.IS_BUY_TRUE) {
                           tMTW80VolumeBuy += detailDealElement.mVolume;
                           tMTW80TurnOverBuy += detailDealElement.mTurnOver;
                       } else if (detailDealElement.mIsBuy != DetailDealElement.IS_BUY_FALSE) {
                           tMTW80VolumeSale += detailDealElement.mVolume;
                           tMTW80TurnOverSale += detailDealElement.mTurnOver;
                       }
                }
            }

            tW10PriceBuy  = (tW10TurnOverBuy )/(tW10VolumeBuy * 100.0f + 1.0f);
            tW10PriceSale = (tW10TurnOverSale)/(tW10VolumeSale * 100.0f + 1.0f);

            tW20PriceBuy  = (tW20TurnOverBuy )/(tW20VolumeBuy * 100.0f + 1.0f);
            tW20PriceSale = (tW20TurnOverSale)/(tW20VolumeSale * 100.0f + 1.0f);

            tW40PriceBuy  = (tW40TurnOverBuy )/(tW40VolumeBuy * 100.0f + 1.0f);
            tW40PriceSale = (tW40TurnOverSale)/(tW40VolumeSale * 100.0f + 1.0f);

            tW80PriceBuy  = (tW80TurnOverBuy )/(tW80VolumeBuy * 100.0f + 1.0f);
            tW80PriceSale = (tW80TurnOverSale)/(tW80VolumeSale * 100.0f + 1.0f);

            tMTW80PriceBuy  = (tW80TurnOverBuy )/(tW80VolumeBuy * 100.0f + 1.0f);
            tMTW80PriceSale = (tW80TurnOverSale)/(tW80VolumeSale * 100.0f + 1.0f);

            return new DetailFilterResult(date,
                                          tW10PriceBuy,   tW10VolumeBuy,   tW10TurnOverBuy,   tW10PriceSale,   tW10VolumeSale,   tW10TurnOverSale,
                                          tW20PriceBuy,   tW20VolumeBuy,   tW20TurnOverBuy,   tW20PriceSale,   tW20VolumeSale,   tW20TurnOverSale,
                                          tW40PriceBuy,   tW40VolumeBuy,   tW40TurnOverBuy,   tW40PriceSale,   tW40VolumeSale,   tW40TurnOverSale,
                                          tW80PriceBuy,   tW80VolumeBuy,   tW80TurnOverBuy,   tW80PriceSale,   tW80VolumeSale,   tW80TurnOverSale,
                                          tMTW80PriceBuy, tMTW80VolumeBuy, tMTW80TurnOverBuy, tMTW80PriceSale, tMTW80VolumeSale, tMTW80TurnOverSale);
        }
    }//end runnable//end runnable

    private Vector<String> getDetailDealTablesToUpdateIntoFilterResult(String stockId) {
        DetailDealDBHelper detailDealHelper = new DetailDealDBHelper(stockId);
        Vector<String> existingDetailDealTables = detailDealHelper.getAllExistingDetailDealTableNames();
        if (existingDetailDealTables.isEmpty()) {
            return null;
        }

        DetailsFilterDBHelper detailFilterDBHelper = new DetailsFilterDBHelper(stockId);
        Vector<String> existingDetailsFilterResults = detailFilterDBHelper.getExistingFilterResultDate();

        if (existingDetailsFilterResults.isEmpty()) {
            return existingDetailDealTables;
        }

        Vector<String> targetDetailDealTables = new Vector<String>();

        int sizeOfExistingDetailDealTables = existingDetailDealTables.size();
        int sizeOfExistingDetailFilterResults = existingDetailsFilterResults.size();
        int i = 0, j = 0;
        while(i < sizeOfExistingDetailDealTables && j < sizeOfExistingDetailFilterResults) {
           String detailDealTableName = existingDetailDealTables.get(i);
           String filterResultDate = existingDetailsFilterResults.get(j);
           int detailCompareTofilter = detailDealTableName.compareTo(filterResultDate);
           if (detailCompareTofilter < 0) {
               //The detail-deal table is older than the filter-result-date
               targetDetailDealTables.add(detailDealTableName);
               ++i;
           } else if (detailCompareTofilter == 0) {
               ++i;
               ++j;
           }
        }

        while (i < sizeOfExistingDetailDealTables) {
           targetDetailDealTables.add(existingDetailDealTables.get(i));
        }

        return targetDetailDealTables;
    }

    public void doUpdate() {
        for (int i = 1; i < 1000; ++i) {
            String stockId = String.format("%6d", i);
            Vector<String> detailDealTablesToUpdate = getDetailDealTablesToUpdateIntoFilterResult(stockId);
            UpdateRunnable updateRunnable = new UpdateRunnable(stockId, detailDealTablesToUpdate);
            updateRunnable.run();
        }

        for (int i = 2001; i < 3000; ++i) {
            String stockId = String.format("%6d", i);
            Vector<String> detailDealTablesToUpdate = getDetailDealTablesToUpdateIntoFilterResult(stockId);
            UpdateRunnable updateRunnable = new UpdateRunnable(stockId, detailDealTablesToUpdate);
            updateRunnable.run();
        }

        for (int i = 600001; i < 602000; ++i) {
            String stockId = String.format("%6d", i);
            Vector<String> detailDealTablesToUpdate = getDetailDealTablesToUpdateIntoFilterResult(stockId);
            UpdateRunnable updateRunnable = new UpdateRunnable(stockId, detailDealTablesToUpdate);
            updateRunnable.run();
        }

        for (int i = 603001; i < 604000; ++i) {
            String stockId = String.format("%6d", i);
            Vector<String> detailDealTablesToUpdate = getDetailDealTablesToUpdateIntoFilterResult(stockId);
            UpdateRunnable updateRunnable = new UpdateRunnable(stockId, detailDealTablesToUpdate);
            updateRunnable.run();
        }
    }

    public void doSingleUpdate(String stockId) {
        Vector<String> detailDealTablesToUpdate = getDetailDealTablesToUpdateIntoFilterResult(stockId);
        UpdateRunnable updateRunnable = new UpdateRunnable(stockId, detailDealTablesToUpdate);
        updateRunnable.run();
    }
}
