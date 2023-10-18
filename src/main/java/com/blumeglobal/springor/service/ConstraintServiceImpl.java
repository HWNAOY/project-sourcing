package com.blumeglobal.springor.service;

import com.blumeglobal.springor.repository.ConstraintRepo;
import com.blumeglobal.springor.Utility.CellHandle;
import com.blumeglobal.springor.models.Constraint;
import com.blumeglobal.springor.models.ProcessId;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConstraintServiceImpl implements ConstraintService{

    @Autowired
    private ConstraintRepo constraintRepo;
    @Autowired
    private CellHandle cellHandle;

    public List<Constraint> save(MultipartFile file, ProcessId processId){
        if(file!=null && !file.isEmpty()){
            try{
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet  = workbook.getSheetAt(4);
                int rn = 1;
                for(int ri = rn; ri <= sheet.getLastRowNum(); ri++){
                    Row row = sheet.getRow(ri);
                    if(row==null)
                        break;
                    Constraint constraint = new Constraint();
                    double laneid = (double) cellHandle.getmcell(row.getCell(0),0 );
                    String carr = (String) cellHandle.getmcell(row.getCell(1), 1);
                    String constraintType = (String) cellHandle.getmcell(row.getCell(2),1);

                    String ttype = (String) cellHandle.getmcell(row.getCell(3),1);
                    if(constraintType.isEmpty() || ttype.isEmpty())
                        break;
                    String minimax = (String) cellHandle.getmcell(row.getCell(4),1);
                    double value = (double) cellHandle.getmcell(row.getCell(5),0);
                    String description = (String) cellHandle.getmcell(row.getCell(6), 1);
                    constraint.setLaneid((long)laneid);
                    constraint.setCarrier(carr);
                    constraint.setConstraintType(constraintType);
                    constraint.setType(ttype);
                    constraint.setMinimax(minimax);
                    constraint.setValue((long)value);
//                    constraint.setLaneid((long) row.getCell(0).getNumericCellValue());
//                    constraint.setCarrier(row.getCell(1).getStringCellValue());
//                    constraint.setConstraintType(row.getCell(2).getStringCellValue());
//                    constraint.setType(row.getCell(3).getStringCellValue());
//                    constraint.setMinimax(row.getCell(4).getStringCellValue());
//                    constraint.setValue((long) row.getCell(5).getNumericCellValue());
//                    constraint.setDescription(row.getCell(6).getStringCellValue());
                    constraint.setDescription(description);
                    constraint.setProcessId(processId);

                    constraintRepo.save(constraint);
                }

            }
            catch(Exception e){
//                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return constraintRepo.findAll();
    }

    @Override
    public List<Constraint> findAll() {
        return constraintRepo.findAll();
    }

    @Override
    public Map<String, Constraint> getConstraintMap(ProcessId processId) {
        List<Constraint> constraints = constraintRepo.findByProcessId(processId);
        Map<String,Constraint> mp = new HashMap<>();
        for(Constraint constraint:constraints){
            Long laneid = constraint.getLaneid();
            String carrier = constraint.getCarrier();
            String ttype = constraint.getType();
            mp.put(laneid+"_"+carrier+"_"+ttype,constraint);
        }
        return mp;
    }

    @Override
    public void deleteByProcessId(ProcessId processId) {
        constraintRepo.deleteByProcessId(processId);
    }
}
