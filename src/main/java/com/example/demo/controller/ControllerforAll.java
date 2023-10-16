package com.example.demo.controller;

import com.example.demo.entity.Locations;
import com.example.demo.entity.Routes;
import com.example.demo.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.service.ServiceforAll;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class ControllerforAll {
    @Autowired
    private ServiceforAll Service1;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file){
        if(Helper.checkExcelFormat(file))
        {
            this.Service1.save(file);
            return ResponseEntity.ok(Map.of("message","File is uploaded and Data is saved to db"));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Upload Excel File");
        }
    }
    @RequestMapping("/routes")
    public List<Routes>getAllRoutes()
    {
        return this.Service1.getAllRoutes();
    }
    @RequestMapping("/locations")
    public List<Locations>getAllLocations()
    {
        return this.Service1.getAllLocations();
    }

}
