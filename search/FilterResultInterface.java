package search;

import common.DetailFilterResult;
import dbhelper.filter.DetailsFilterDBHelper;

import java.util.Vector;


public class FilterResultInterface {
    public static class SearchItem {
        String mStockId;
        String mDateBegin;
        String mDateEnd;
        String mDateNow;

        public SearchItem(String stockId, String dateBegin, String dateEnd, String dateNow) {
            mStockId   = stockId;
            mDateBegin = dateBegin;
            mDateEnd   = dateEnd;
            mDateNow   = dateNow;
        }
    };

    private static boolean shouldBeCountedOn(SearchItem singleItem) {
        System.out.println(singleItem.mStockId);
        DetailsFilterDBHelper detailsFilterDBHelper = new DetailsFilterDBHelper(singleItem.mStockId);
        Vector<DetailFilterResult> targetDetailFilterResults = detailsFilterDBHelper.getFilterResultsForDate(singleItem.mDateBegin, singleItem.mDateEnd);
        Vector<DetailFilterResult> tipDetailFilterResults = detailsFilterDBHelper.getFilterResultsForDate(singleItem.mDateEnd, singleItem.mDateNow);

        DetailFilterResult sumTargetDetailFilterResult = DetailFilterResult.sumOfChildDetailFilterResults(targetDetailFilterResults);
        DetailFilterResult sumTipDetailFilterResult = DetailFilterResult.sumOfChildDetailFilterResults(tipDetailFilterResults);
        sumTargetDetailFilterResult.dump();
        sumTipDetailFilterResult.dump();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
        
        if (DetailFilterResult.sendLowerThanFirst(sumTargetDetailFilterResult, sumTipDetailFilterResult)) {
            sumTargetDetailFilterResult.dump();
            sumTipDetailFilterResult.dump();
            return true;
        }

        return false;
    }

    public static void getTargetFrom(Vector<SearchItem> searchItems) {
        int sizeOfSearchItems = searchItems.size();
        SearchItem singleSearchItem = null;
        for ( int i = 0; i < sizeOfSearchItems; ++i) {
            singleSearchItem = searchItems.get(i);
            shouldBeCountedOn(singleSearchItem);
        }
    } 
};
