package com.blumeglobal.springor.service;

import com.blumeglobal.springor.models.Capacity;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CapacityService {
    List<Capacity> save(MultipartFile file, ProcessId processId);
    List<Capacity> findAll();
    void deleteByProcessId(ProcessId processId);
}
