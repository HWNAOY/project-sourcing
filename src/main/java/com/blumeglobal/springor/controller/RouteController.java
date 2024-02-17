package com.blumeglobal.springor.controller;

import com.blumeglobal.springor.models.*;
import com.blumeglobal.springor.repository.*;
import com.blumeglobal.springor.ORhelper.SolveOR;
import com.blumeglobal.springor.exception.EntityNotFoundException;
import com.blumeglobal.springor.service.*;
import com.blumeglobal.springor.models.*;
import com.blumeglobal.springor.repository.*;
import com.blumeglobal.springor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class RouteController {

    @Autowired
    private LaneService laneService;
    @Autowired
    private LaneRepo laneRepo;
    @Autowired
    private CapacityService capacityService;
    @Autowired
    private CapacityRepo capacityRepo;
    @Autowired
    private ConstraintService constraintService;
    @Autowired
    private ConstraintRepo constraintRepo;
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private ResponseRepo responseRepo;
    @Autowired
    private ProcessIdRepo processIdRepo;
    @Autowired
    private FileService fileService;
    @Autowired
    private SolveOR solveOR;
    @PostMapping("/upload")
    void newLane(@RequestParam("file") MultipartFile file){

        ProcessId processId = new ProcessId(UUID.randomUUID().toString());
        fileService.saveAll(file,processId);
//        processIdRepo.save(processId);
//        locationService.save(file,processId);
//        laneService.save(file,processId);
//        capacityService.save(file,processId);
//        constraintService.save(file,processId);
//        responseService.save(file,processId);

    }
    @DeleteMapping("/remove/{id}")
    String deleteUpload(@PathVariable Long id){
        ProcessId processId = processIdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        fileService.delete(processId);
        return "Entries with Process id: "+id+" deleted";
    }
    @GetMapping("/output")
    List<FinalOutput> getOutput(){
        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
        try {
            return solveOR.solution(lastid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("The final result is "+result);
//        return result;
    }
    @GetMapping("/output/{id}")
    List<FinalOutput> getOutput(@PathVariable Long id){
        ProcessId processId = processIdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        try {
            return solveOR.solution(processId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("The final result is "+result);
//        return result;
    }
    @GetMapping("/lanes/{id}")
    List<Lanes> getLane(@PathVariable Long id){
        //ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
//        Optional<ProcessId> lastid = processIdRepo.findById(processId);
        ProcessId processId = processIdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return laneRepo.findByProcessId(processId);
    }
    @GetMapping("/lanes")
    List<Lanes> getLane(){
        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
//        Optional<ProcessId> lastid = processIdRepo.findById(processId);
        return laneRepo.findByProcessId(lastid);
    }

    @GetMapping("/capacity")
    List<Capacity> getCapacity(){
        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
        return capacityRepo.findByProcessId(lastid);
    }
    @GetMapping("/capacity/{id}")
    List<Capacity> getCapacity(@PathVariable Long id){
        ProcessId processId = processIdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return capacityRepo.findByProcessId(processId);
    }
    @GetMapping("/constraint")
    List<Constraint> getConstraint(){
        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
        return constraintRepo.findByProcessId(lastid);
    }
    @GetMapping("/location")
    List<Location> getLocation(){
        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
        return locationRepo.findByProcessId(lastid);
    }
    @GetMapping("/response")
    List<Response> getResponse(){
        ProcessId lastid = processIdRepo.findFirstByOrderByIdDesc();
        return responseRepo.findByProcessId(lastid);
    }
    @GetMapping("/processId")
    List<ProcessId> getProcessId(){
        return processIdRepo.findAll();
    }
    @GetMapping("/processId/{id}")
    ProcessId getProcessId(@PathVariable Long id){
        return processIdRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }
}
