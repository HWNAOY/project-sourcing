package com.blumeglobal.springor.service;

import com.blumeglobal.springor.models.ProcessId;
import com.blumeglobal.springor.models.Response;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ResponseService {
    List<Response> save(MultipartFile file, ProcessId processId);
    List<Response> findAll();
    Map<Pair<String,Long>,Response> getCarrierResponseMap(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
