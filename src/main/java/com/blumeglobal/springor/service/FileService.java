package com.blumeglobal.springor.service;

import com.blumeglobal.springor.models.ProcessId;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void saveAll(MultipartFile file, ProcessId processId);
    void delete(ProcessId processId);
}
