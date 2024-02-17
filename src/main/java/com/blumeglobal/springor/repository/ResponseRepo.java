package com.blumeglobal.springor.repository;

import com.blumeglobal.springor.models.ProcessId;
import com.blumeglobal.springor.models.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResponseRepo extends JpaRepository<Response,Long> {
    List<Response> findByProcessId(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
