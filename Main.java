import common.DetailDealElement;
import dbhelper.origin.DetailDealDBHelper;
import filewrapper.TextFileParser;
import updator.DetailUpdator;

import java.io.File;

public class Main {
    //For test only
    public static void main(String[] args)  {
        try {
            String detailsPath = "./details";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

