package com.blumeglobal.springor.service;

import com.blumeglobal.springor.models.Lanes;
import com.blumeglobal.springor.models.ProcessId;
import com.blumeglobal.springor.models.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LaneService {
    List<Lanes> save(MultipartFile file, ProcessId processId);
    List<Lanes> findAll();
    void deleteByProcessId(ProcessId processId);
}
