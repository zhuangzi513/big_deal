package filewrapper;

import common.DetailDealElement;

import java.io.*;
import java.util.Vector;

import java.nio.ByteBuffer;  
import java.nio.CharBuffer;  
import java.nio.charset.Charset;  
import java.nio.charset.CharsetDecoder;  


public class TextFileParser extends FileParser {
    private static final String FILE_FORMAT = new String("gb2312");
    private static String GB2312_BUY_IN_CHINESE = null;
    private static String GB2312_SAL_IN_CHINESE = null;
    private static final String SAL_BUY_FILE_PATCH = "./common/buy_sale";

    private static final String TAB = "	";
    private static final int DEAL_PROPERTY_LENGTH = 6;
    private String mFullFilePath;
    private Vector<DetailDealElement> mInnerDetailDealElements;

    public void initStatic() {
        try {
            FileInputStream fileInputStream = new FileInputStream(SAL_BUY_FILE_PATCH);  
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);  
            BufferedReader bufferReader = new BufferedReader (new InputStreamReader(bufferedInputStream, FILE_FORMAT)); 

            GB2312_BUY_IN_CHINESE = bufferReader.readLine();
            GB2312_SAL_IN_CHINESE = bufferReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextFileParser(String filePath) {
        if (GB2312_BUY_IN_CHINESE != null
            && GB2312_SAL_IN_CHINESE != null) {
        } else {
            initStatic();
        }
        mFullFilePath = filePath;
        mInnerDetailDealElements = new Vector<DetailDealElement>();
    }

    public Vector<DetailDealElement> getDetailDealElements() {
        return mInnerDetailDealElements;
    }

    public boolean parseFileIntoDealElements() throws IOException {
        DetailDealElement singleElement = null;
        Vector<String> textLines = new Vector<String>();
        String textLine = new String("");
        BufferedReader bufferReader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(mFullFilePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferReader = new BufferedReader (new InputStreamReader(bufferedInputStream, FILE_FORMAT));

            //Skip first line
            bufferReader.readLine();

            while ((textLine = bufferReader.readLine()) != null) {
                textLines.add(textLine);
            }

            for (int i = 0; i < textLines.size(); ++i) {
                singleElement = createDetailDealElementFromString(textLines.get(i));
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
            String buyOrSale = tabSplited[5];

            int isBuy = DetailDealElement.IS_BUY_DEFAULT;
            try {
                if (buyOrSale.equals(GB2312_BUY_IN_CHINESE)) {
                    isBuy = DetailDealElement.IS_BUY_TRUE;
                    //System.out.println("isBuy: true");
                } else if (buyOrSale.equals(GB2312_SAL_IN_CHINESE)) {
                    isBuy = DetailDealElement.IS_BUY_FALSE;
                    //System.out.println("isBuy: false");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            retDetailDealElement = new DetailDealElement(date, price, priceUp, volume, turnOver, isBuy);
        }

        return retDetailDealElement;
    }
}

