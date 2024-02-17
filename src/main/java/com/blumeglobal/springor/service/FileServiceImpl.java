package com.blumeglobal.springor.service;

import com.blumeglobal.springor.repository.*;
import com.blumeglobal.springor.models.ProcessId;
import com.blumeglobal.springor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileServiceImpl implements FileService{
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
    private FinalOutputRepo finalOutputRepo;
    @Transactional(rollbackFor = Exception.class)
    public void saveAll(MultipartFile file, ProcessId processId){
        try {
            processIdRepo.save(processId);
            locationService.save(file, processId);
            laneService.save(file, processId);
            capacityService.save(file, processId);
            constraintService.save(file, processId);
            responseService.save(file, processId);
        }
        catch(Exception e){
            //System.err.println(e);
            throw new RuntimeException("RollBack "+e.getMessage());
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void delete(ProcessId processId){
        try {
            locationService.deleteByProcessId(processId);
            laneService.deleteByProcessId(processId);
            capacityService.deleteByProcessId(processId);
            constraintService.deleteByProcessId(processId);
            responseService.deleteByProcessId(processId);
            finalOutputRepo.deleteByProcessId(processId);
            processIdRepo.deleteById(processId.getId());

        }
        catch(Exception e){
            //System.err.println(e);
            throw new RuntimeException("RollBack "+e.getMessage());
        }
    }
}
