import common.DetailDealElement;
import dbhelper.origin.DetailDealDBHelper;
import updator.FilterResultUpdator;
import filewrapper.TextFileParser;
import updator.DetailUpdator;
import search.FilterResultInterface;
import search.FilterResultInterface.SearchItem;

import java.io.File;
import java.util.Collections;
import java.util.Vector;
import java.util.Arrays;
import java.util.ArrayList;

public class Main {
    //For test only
    public static void main(String[] args)  {
        try {
            String detailsPath = "details";
            File detailsDir = new File(detailsPath);
            if (!detailsDir.isDirectory()) {
                return;
            }

            String[] stockDirs = detailsDir.list();
            if (stockDirs == null) {
                return;
            }
            int stockNum = stockDirs.length;
            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(stockDirs));  
            Collections.sort(arrayList);

            for (int i = 0; i < stockNum; i++) {
                String strSingleYearDir = detailsPath + "/" + arrayList.get(i);
                DetailUpdator updator = new DetailUpdator();
                updator.doUpdate(strSingleYearDir);
            }

            FilterResultUpdator filterResultUpdator = new FilterResultUpdator();
            filterResultUpdator.doSingleUpdate("002365");
            filterResultUpdator.doSingleUpdate("600165");

            Vector<SearchItem> searchItems = new Vector<SearchItem>();
            SearchItem s365 = new FilterResultInterface.SearchItem("002365", "20170104", "20170113", "20170114");
            SearchItem s165 = new FilterResultInterface.SearchItem("600165", "20170104", "20170113", "20170114");
            searchItems.add(s365);
            searchItems.add(s165);
            FilterResultInterface.getTargetFrom(searchItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

