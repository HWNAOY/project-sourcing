package com.blumeglobal.springor.service;

import com.blumeglobal.springor.repository.CapacityRepo;
import com.blumeglobal.springor.Utility.CellHandle;
import com.blumeglobal.springor.models.Capacity;
import com.blumeglobal.springor.models.ProcessId;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class CapacityServiceImpl implements CapacityService{

    @Autowired
    private CapacityRepo capacityRepo;
    @Autowired
    private CellHandle cellHandle;

    public List<Capacity> save(MultipartFile file, ProcessId processId){
        if(file!=null && !file.isEmpty()){
            try{
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet5  = workbook.getSheetAt(3);
                Sheet sheet4  = workbook.getSheetAt(2);
                int rn = 1;
                for(int ri = rn; ri <= sheet5.getLastRowNum(); ri++){
                    Row row = sheet4.getRow(ri);
                    Row row2 = sheet5.getRow(ri);
                    if(row==null && row2==null)
                        break;
                    Capacity capacity = new Capacity();
                    String carr = (String) cellHandle.getmcell(row.getCell(5), 1);
                    if(carr.isEmpty())
                        break;
                    double capacityNum = (double) cellHandle.getmcell(row.getCell(6), 0);
                    double blumeScore = (double) cellHandle.getmcell(row2.getCell(1),0);
                    capacity.setCarrier(carr);
                    capacity.setCapacity((long) capacityNum);
                    capacity.setBlumeScore((long) blumeScore);
                    capacity.setProcessId(processId);
//                    capacity.setCarrier(row.getCell(5).getStringCellValue());
//                    capacity.setCapacity((long) row.getCell(6).getNumericCellValue());
//                    capacity.setBlumeScore((long) row2.getCell(1).getNumericCellValue());
                    capacityRepo.save(capacity);
                }

            }
            catch(Exception e){
//                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return capacityRepo.findAll();
    }

    @Override
    public List<Capacity> findAll() {
        return capacityRepo.findAll();
    }

    @Override
    public void deleteByProcessId(ProcessId processId) {
        capacityRepo.deleteByProcessId(processId);
    }
}
