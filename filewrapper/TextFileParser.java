package filewrapper;

import common.DetailDealElement;

import java.io.*;
import java.util.Vector;


public class TextFileParser extends FileParser {
    private static final String BUY_IN_CHINESE = "买盘";
    private static final String SAL_IN_CHINESE = "卖盘";
    private static final String TAB = "	";
    private static final int DEAL_PROPERTY_LENGTH = 6;
    private String mFullFilePath;
    private Vector<DetailDealElement> mInnerDetailDealElements;

    public TextFileParser(String filePath) {
        mFullFilePath = filePath;
        mInnerDetailDealElements = new Vector<DetailDealElement>();
    }

    public Vector<DetailDealElement> getDetailDealElements() {
        return mInnerDetailDealElements;
    }

    public boolean parseFileIntoDealElements() throws IOException {
        DetailDealElement singleElement = null;
        String textLine = new String("");
        BufferedReader bufferReader = null;
        try {
            bufferReader = new BufferedReader(new FileReader(mFullFilePath));

            //Skip first line
            bufferReader.readLine();

            while ((textLine = bufferReader.readLine()) != null) {
                singleElement = createDetailDealElementFromString(textLine);
                //System.out.println("singleElement");
                //singleElement.dump();
                mInnerDetailDealElements.add(singleElement);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            bufferReader.close();
        }

        return true;
    }

    private DetailDealElement createDetailDealElementFromString(String textLine) {
        //Format of the string:
        //  Time       price    float   volume    turnover 
        //  09:31:00   22.00    0.01    20        20 * 22 * 100
        String [] tabSplited = textLine.split(TAB);
        //System.out.println(tabSplited.length);
        DetailDealElement retDetailDealElement = null;
        if (tabSplited.length == DEAL_PROPERTY_LENGTH) {
            String date     = tabSplited[0];
/*
            String price    = tabSplited[1];
            String priceUp  = tabSplited[2];
            String volume   = tabSplited[3];
            String turnOver = tabSplited[4];
            String isBuy    = tabSplited[5];
*/

            float price    = Float.parseFloat(tabSplited[1]);
            float priceUp  = 0.0f;
            if (!tabSplited[2].equals("--")) {
                priceUp = Float.parseFloat(tabSplited[2]);
            }
            int volume     = Integer.valueOf(tabSplited[3]);
            int turnOver   = Integer.valueOf(tabSplited[4]);

            boolean isBuy = false;
            if (tabSplited[5].equals(BUY_IN_CHINESE)) {
                isBuy = true;
            }

            retDetailDealElement = new DetailDealElement(date, price, priceUp, volume, turnOver, isBuy);
        }

        return retDetailDealElement;
    }
}

