import common.DetailDealElement;
import dbhelper.origin.DetailDealDBHelper;
import updator.FilterResultUpdator;
import filewrapper.TextFileParser;
import updator.DetailUpdator;
import search.FilterResultInterface;
import search.FilterResultInterface.SearchItem;

import java.io.File;
import java.util.Vector;

public class Main {
    //For test only
    public static void main(String[] args)  {
        try {
            String detailsPath = "../../stock/details";
            File detailsDir = new File(detailsPath);
            if (!detailsDir.isDirectory()) {
                return;
            }

            String[] stockDirs = detailsDir.list();
            if (stockDirs == null) {
                return;
            }
            int stockNum = stockDirs.length;

            for (int i = 0; i < stockNum; i++) {
                String strSingleYearDir = detailsPath + "/" + stockDirs[i];
                DetailUpdator updator = new DetailUpdator();
                updator.doUpdate(strSingleYearDir);
            }

            //FilterResultUpdator filterResultUpdator = new FilterResultUpdator();
            //filterResultUpdator.doSingleUpdate("600165");

            //Vector<SearchItem> searchItems = new Vector<SearchItem>();
            //SearchItem s165 = new FilterResultInterface.SearchItem("600165", "20160330", "20160930", "20161114");
            //searchItems.add(s165);
            //FilterResultInterface.getTargetFrom(searchItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

