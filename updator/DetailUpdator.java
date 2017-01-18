package updator; import common.DetailDealElement;
import dbhelper.origin.DetailDealDBHelper;
import filewrapper.TextFileParser;


import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

import java.lang.Runnable;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  

public class DetailUpdator {
    private static final int DATE_STRING_LENGTH = 8;
    private static ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(10);

    private class UpdateRunnable implements Runnable {
        private String mStockId;
        private Vector<String> mAllExistingDetailFileList;

        public UpdateRunnable(String id, Vector<String> existingDetailsFileList ) {
            super();
            mStockId = id;
            mAllExistingDetailFileList = existingDetailsFileList;
        }

        public void run() {
            DetailDealDBHelper dbHelper = new DetailDealDBHelper(mStockId);
            //dbHelper.dropDatabase();
            String latestDetailTableName = dbHelper.getLatestTableName();
            int existingDetailFileLength = mAllExistingDetailFileList.size();
            for (int i = 0; i < existingDetailFileLength; ++i) {
                //Convert the fullpath to corresponding table name
                String dateOfDetail = mAllExistingDetailFileList.get(i).replace("/", "");
                int startPosOfPostFix = dateOfDetail.length() - 4;
                dateOfDetail = dateOfDetail.substring(startPosOfPostFix - DATE_STRING_LENGTH, startPosOfPostFix);
                System.out.println(dateOfDetail + ":" + latestDetailTableName);

                if (latestDetailTableName != null &&
                    latestDetailTableName.compareTo(dateOfDetail) >= 0) {
                    System.out.println(dateOfDetail);
                    //ALREADY A TABLE IN DATABSE CORRESPONDING TO THE DETAILFILE, SKIP
                    continue;
                }
                try {
                    System.out.println(dateOfDetail + ":" + new Date());
                    TextFileParser parser = new TextFileParser(mAllExistingDetailFileList.get(i));
                    parser.parseFileIntoDealElements();
                    dbHelper.createOriginTable(dateOfDetail);
                    dbHelper.insertDetailDealElementsToTable(dateOfDetail, parser.getDetailDealElements());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            dbHelper.dispose();
        }
    }

    private Vector<String> scanAndGetExistingFileList(String dir) {

        Vector<String> allExistingDetailFiles = null;
        File stockDir = new File(dir);
        if (!stockDir.isDirectory()) {
            return null;
        }

        String[] yearDirs = stockDir.list();
        if (yearDirs == null) {
            return null;
        }
        allExistingDetailFiles = new Vector<String>();

        for (int i = 0; i < yearDirs.length; i++) {
            String strSingleYearDir = dir + "/" + yearDirs[i];
            //System.out.println(strSingleYearDir);
            File yearDir = new File(strSingleYearDir);
            if (!yearDir.isDirectory()) {
                System.out.println(strSingleYearDir + " is not directory");
                continue;
            }
            String[] monthesDirs = yearDir.list();
            int monthesLength = monthesDirs.length;
            for (int j = 0; j < monthesLength; j++) {
                 String strSingleMonthDir = strSingleYearDir + "/" + monthesDirs[j];
                 File mountDir = new File(strSingleMonthDir);
                 //System.out.println(strSingleMonthDir);
                 if (!mountDir.isDirectory()) {
                     continue;
                 }

                 String[] strDetailFiles = mountDir.list();
                 int detailsFilesLength = strDetailFiles.length;
                 for (int k = 0; k < detailsFilesLength; k++) {
                      String strSingleDetailFile = strSingleMonthDir + "/" + strDetailFiles[k];
                      allExistingDetailFiles.add(strSingleDetailFile);
                 }
            }
        }

        if (allExistingDetailFiles.size() > 0) {
            Collections.sort(allExistingDetailFiles);
        }

        return allExistingDetailFiles;
    }

    public void doUpdate(String dirForOneStock) {
        Vector<String> existingDetailFiles = scanAndGetExistingFileList(dirForOneStock);
        int lengthOfStockDetails = dirForOneStock.length();
        String stockId = dirForOneStock.substring(lengthOfStockDetails - 6, lengthOfStockDetails);
        System.out.println("stock id: " + stockId);
        if (existingDetailFiles != null) {
            Runnable detailUpdateRunnable = new UpdateRunnable(stockId, existingDetailFiles);
            //FIXED_THREAD_POOL.execute(detailUpdateRunnable);
            detailUpdateRunnable.run();
        }
    }
};
