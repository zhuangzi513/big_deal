package search;

import common.DetailFilterResult;
import dbhelper.filter.DetailsFilterDBHelper;

import java.util.Vector;


public class FilterResultInterface {
    public static void getFilterResultForStockBetweenDate(String stockId, String dateBegin, String dateEnd) {
        DetailsFilterDBHelper detailsFilterDBHelper = new DetailsFilterDBHelper(stockId);
        Vector<DetailFilterResult> detailFilterResults = detailsFilterDBHelper.getFilterResultsForDate(dateBegin, dateEnd);
        DetailFilterResult sumDetailFilterResult = DetailFilterResult.sumOfChildDetailFilterResults(detailFilterResults);
        sumDetailFilterResult.dump();
    }
};
