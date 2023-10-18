package com.blumeglobal.springor.service;

import com.blumeglobal.springor.models.Location;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface LocationService {
    List<Location> save(MultipartFile file, ProcessId processId);
    List<Location> findAll();
    Map<String,Location> getLocationMap(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
