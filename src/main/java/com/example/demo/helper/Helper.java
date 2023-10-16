package com.example.demo.helper;

import com.example.demo.entity.Locations;
import com.example.demo.entity.Routes;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {
    //file excel or not
    public static boolean checkExcelFormat(MultipartFile file){
        String contentType =file.getContentType();
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return true;
        } else {
            return false;
        }
    }

    // excel to list
    public static List <Routes> convertExceltoList(InputStream is){
        List<Routes>list=new ArrayList<>();

        try{
            XSSFWorkbook workbook=new XSSFWorkbook(is);
            XSSFSheet sheet=workbook.getSheet("Lanes");
            int rowNumber=0;
            Iterator<Row> iterator=sheet.iterator();
            while(iterator.hasNext())
            {
                Row row = iterator.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell>cells=row.iterator();
                int cid=0;
                Routes r =new Routes();
                while(cells.hasNext()){
                    Cell cell=cells.next();
                    switch(cid)
                    {
                        case 0:
                            r.setLaneID((long)cell.getNumericCellValue());
                            break;
                        case 1:
                            r.setFrom(cell.getStringCellValue());
                            break;
                        case 2:
                            r.setTo(cell.getStringCellValue());
                            break;
                        case 3:
                            r.setVolume((int)cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cid++;
                }
                list.add(r);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  list;
    }
    public static List<Locations> convertExceltoList1(InputStream is) {
        List<Locations>list1=new ArrayList<>();
        try{
            XSSFWorkbook workbook=new XSSFWorkbook(is);
            XSSFSheet sheet1=workbook.getSheet("Locations");
            int rowNumber1=0;
            Iterator<Row> iterator1=sheet1.iterator();
            while(iterator1.hasNext())
            {
                Row row1 = iterator1.next();

                if (rowNumber1 == 0) {
                    rowNumber1++;
                    continue;
                }
                Iterator<Cell>cells1=row1.iterator();
                int cid1=0;
                Locations l =new Locations();
                while(cells1.hasNext()){
                    Cell cell1=cells1.next();
                    switch (cid1) {
                        case 0 :
                            l.setLocation(cell1.getStringCellValue());
                            break;
                        case 1 :
                            l.setLatitude((double)cell1.getNumericCellValue());
                            break;
                        case 2 :
                            l.setLongitude((double)cell1.getNumericCellValue());
                            break;
                        default :
                            break;

                    }
                    cid1++;
                }
                list1.add(l);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  list1;
    }

}
