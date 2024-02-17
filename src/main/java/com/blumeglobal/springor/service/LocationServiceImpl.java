package com.blumeglobal.springor.service;

import com.blumeglobal.springor.Utility.CellHandle;
import com.blumeglobal.springor.repository.LocationRepo;
import com.blumeglobal.springor.models.Location;
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
public class LocationServiceImpl implements LocationService{
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private CellHandle cellHandle;

    public List<Location> save(MultipartFile file, ProcessId processId){
        if(file!=null && !file.isEmpty()){
            try{
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet3  = workbook.getSheetAt(1);
                int rn = 1;
                for(int ri = rn; ri <= sheet3.getLastRowNum(); ri++){
                    Row row = sheet3.getRow(ri);
                    if(row==null)
                        break;
                    Location location = new Location();
                    String loc = (String) cellHandle.getmcell(row.getCell(0), 1);
                    if(loc.isEmpty())
                        break;
                    Double latitude = (Double) cellHandle.getmcell(row.getCell(1),0);
                    Double longitude = (Double) cellHandle.getmcell(row.getCell(2),0);
                    location.setLocation(loc);
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
//                    location.setLocation(row.getCell(0).getStringCellValue());
//                    location.setLatitude(row.getCell(1).getNumericCellValue());
//                    location.setLongitude(row.getCell(2).getNumericCellValue());
                    location.setProcessId(processId);
                    locationRepo.save(location);
                }

            }
            catch(Exception e){
//                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return locationRepo.findAll();
    }

    @Override
    public List<Location> findAll() {
        return locationRepo.findAll();
    }

    @Override
    public Map<String, Location> getLocationMap(ProcessId processId) {
        List<Location> locations = locationRepo.findByProcessId(processId);
        Map<String,Location> mp = new HashMap<>();
        for(Location location:locations){
            String loc = location.getLocation();
            mp.put(loc,location);
        }
        return mp;
    }

    @Override
    public void deleteByProcessId(ProcessId processId) {
        locationRepo.deleteByProcessId(processId);
    }
}
