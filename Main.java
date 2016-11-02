import common.DetailDealElement;
import dbhelper.origin.DetailDealDBHelper;
import filewrapper.TextFileParser;

public class Main {
    //For test only
    public static void main(String[] args)  {
        try {
            TextFileParser parser = new TextFileParser("19.xls");
            parser.parseFileIntoDealElements();
            DetailDealDBHelper dbHelper = new dbhelper.origin.DetailDealDBHelper("ramotest");
            dbHelper.createOriginTable(new String("ramotest1"));
            dbHelper.insertDetailDealElementsToTable(new String("ramotest1"), parser.getDetailDealElements());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

