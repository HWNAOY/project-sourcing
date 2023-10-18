package com.blumeglobal.springor.service;

import com.blumeglobal.springor.Utility.CellHandle;
import com.blumeglobal.springor.repository.ResponseRepo;
import com.blumeglobal.springor.models.ProcessId;
import com.blumeglobal.springor.models.Response;
import org.antlr.v4.runtime.misc.Pair;
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
public class ResponseServiceImpl implements ResponseService{
    @Autowired
    private ResponseRepo responseRepo;
    @Autowired
    private CellHandle cellHandle;

    public List<Response> save(MultipartFile file, ProcessId processId){
        if(file!=null && !file.isEmpty()){
            try{
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet4  = workbook.getSheetAt(2);
                int rn = 1;
                for(int ri = rn; ri <= sheet4.getLastRowNum(); ri++){
                    Row row = sheet4.getRow(ri);
                    Response response = new Response();
                    String carr = (String) cellHandle.getmcell(row.getCell(0), 1);
                    double laneid = (double) cellHandle.getmcell(row.getCell(1),0 );
                    if(carr.isEmpty() && laneid==(long)0)
                        break;
                    double commitment = (double) cellHandle.getmcell(row.getCell(2),0 );
                    double rate = (double) cellHandle.getmcell(row.getCell(3),0 );
                    response.setCarrier(carr);
                    response.setLaneid((long)laneid);
                    response.setCommitment((long)commitment);
                    response.setRate((long)rate);
//                    response.setCarrier(row.getCell(0).getStringCellValue());
//                    response.setLaneid((long) row.getCell(1).getNumericCellValue());
//                    response.setCommitment((long) row.getCell(2).getNumericCellValue());
//                    response.setRate((long) row.getCell(3).getNumericCellValue());
                    response.setProcessId(processId);
                    responseRepo.save(response);
                }

            }
            catch(Exception e){
//                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return responseRepo.findAll();
    }

    @Override
    public List<Response> findAll() {
        return responseRepo.findAll();
    }

    @Override
    public Map<Pair<String, Long>, Response> getCarrierResponseMap(ProcessId processId) {
        List<Response> responses = responseRepo.findByProcessId(processId);
        Map<Pair<String, Long>,Response> ans = new HashMap<>();
        for(Response response:responses){
            String carrier = response.getCarrier();
            Long laneid = response.getLaneid();
            Pair<String,Long> p = new Pair<>(carrier,laneid);
            ans.put(p,response);
        }
        return ans;
    }

    @Override
    public void deleteByProcessId(ProcessId processId) {
        responseRepo.deleteByProcessId(processId);
    }
}
