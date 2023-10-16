package com.example.demo.service;

import com.example.demo.entity.Locations;
import com.example.demo.entity.Routes;
import com.example.demo.helper.Helper;
import com.example.demo.repo.LocationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.repo.RoutesRepo;

import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Service

public class ServiceforAll {
    @Autowired
    private RoutesRepo routesRepo;
    @Autowired
    private LocationsRepo locationsRepo;
    public void save(MultipartFile file)
    {
        try {
            List<Routes>routes=Helper.convertExceltoList(file.getInputStream());
            List<Locations>locations=Helper.convertExceltoList1(file.getInputStream());
            this.routesRepo.saveAll(routes);
            this.locationsRepo.saveAll(locations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Routes> getAllRoutes(){
        return this.routesRepo.findAll();
    }
    public  List<Locations> getAllLocations()
    {
        return this.locationsRepo.findAll();
    }
}
