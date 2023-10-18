package com.blumeglobal.springor.service;

import com.blumeglobal.springor.models.Constraint;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ConstraintService {
    List<Constraint> save(MultipartFile file, ProcessId processId);
    List<Constraint> findAll();
    Map<String,Constraint> getConstraintMap(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
