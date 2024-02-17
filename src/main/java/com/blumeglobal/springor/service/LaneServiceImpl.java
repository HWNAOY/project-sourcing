package com.blumeglobal.springor.service;

import com.blumeglobal.springor.Utility.CellHandle;
import com.blumeglobal.springor.repository.LaneRepo;
import com.blumeglobal.springor.models.Lanes;
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
public class LaneServiceImpl implements LaneService{

    @Autowired
    private LaneRepo laneRepo;
    @Autowired
    private CellHandle cellHandle;

    public List<Lanes> save(MultipartFile file, ProcessId processId){
        if(file!=null && !file.isEmpty()){
            try{
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet  = workbook.getSheetAt(0);
                int rn = 1;
                for(int ri = rn; ri <= sheet.getLastRowNum(); ri++){
                    Row row = sheet.getRow(ri);
                    if(row==null)
                        break;
                    Lanes lane = new Lanes();
                    double laneid = (double) cellHandle.getmcell(row.getCell(0),0);
                    if (laneid == 0) {
                        break;
                    }
                    String ffrom = (String) cellHandle.getmcell(row.getCell(1),1);
                    String tto = (String) cellHandle.getmcell(row.getCell(2),1);
                    double volume = (double) cellHandle.getmcell(row.getCell(3),0);
                    lane.setLaneid((long)laneid);
                    lane.setFrom(ffrom);
                    lane.setTo(tto);
                    lane.setVolume((long)volume);
//                    lane.setLaneid((long) row.getCell(0).getNumericCellValue());
//                    lane.setFrom(row.getCell(1).getStringCellValue());
//                    lane.setTo(row.getCell(2).getStringCellValue());
//                    lane.setVolume((long) row.getCell(3).getNumericCellValue());
                    lane.setProcessId(processId);
                    laneRepo.save(lane);
                }

            }
            catch(Exception e){
//                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return laneRepo.findAll();
    }

    @Override
    public List<Lanes> findAll() {
        return laneRepo.findAll();
    }

    @Override
    public void deleteByProcessId(ProcessId processId) {
        laneRepo.deleteByProcessId(processId);
    }
}
