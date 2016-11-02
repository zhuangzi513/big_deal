package filewrapper;

import common.DetailDealElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSFileParser extends FileParser {
    private XSSFSheet mXLSXSheet;
    private XSSFWorkbook mXLSXWorkbook;

    private HSSFSheet mXLSSheet;
    private HSSFWorkbook mXLSWorkbook;

    private String mFullFilePath;

    public XLSFileParser(String filePath) {
        mFullFilePath = filePath;
    }

    public boolean parseFileIntoDealElements() throws IOException {
        return false;
    }

    public Vector<DetailDealElement> getDetailDealElements() {
        return null;
    }

    
    private Vector<DetailDealElement> parseXLSXFileIntoDealElements() throws IOException {
        try {
            mXLSXWorkbook = new XSSFWorkbook(new FileInputStream(mFullFilePath));
            mXLSXSheet = mXLSXWorkbook.getSheetAt(0);
            int maxRowNum = mXLSXSheet.getLastRowNum();
            for (int i = 0; i < mXLSXSheet.getLastRowNum(); i++){ 
                XSSFRow hssfRow = mXLSXSheet.getRow(i);
                XSSFCell date = hssfRow.getCell(0); 
                System.out.println(date.getStringCellValue()); 
                XSSFCell price = hssfRow.getCell(1); 
                System.out.println(price.getStringCellValue()); 
                XSSFCell priceUp = hssfRow.getCell(2); 
                System.out.println(priceUp.getStringCellValue()); 
                XSSFCell volume = hssfRow.getCell(3); 
                System.out.println(volume.getStringCellValue()); 
                XSSFCell turnOver = hssfRow.getCell(4); 
                System.out.println(turnOver.getStringCellValue()); 
                XSSFCell isBuy = hssfRow.getCell(5); 
                System.out.println(isBuy.getStringCellValue()); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Vector<DetailDealElement> parseXLSFileIntoDealElements() throws IOException {
        try {
            mXLSWorkbook = new HSSFWorkbook(new FileInputStream(mFullFilePath));
            mXLSSheet = mXLSWorkbook.getSheetAt(0);
            int maxRowNum = mXLSSheet.getLastRowNum();
            for (int i = 0; i < mXLSSheet.getLastRowNum(); i++){ 
                HSSFRow hssfRow = mXLSSheet.getRow(i);
                HSSFCell date = hssfRow.getCell(0); 
                HSSFCell price = hssfRow.getCell(1); 
                HSSFCell priceUp = hssfRow.getCell(2); 
                HSSFCell volume = hssfRow.getCell(3); 
                HSSFCell turnOver = hssfRow.getCell(4); 
                HSSFCell isBuy = hssfRow.getCell(5); 
                System.out.println(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
};
