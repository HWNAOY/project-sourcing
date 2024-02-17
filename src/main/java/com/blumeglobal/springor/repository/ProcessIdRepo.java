package com.blumeglobal.springor.repository;

import com.blumeglobal.springor.models.ProcessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessIdRepo extends JpaRepository<ProcessId,Long> {
    ProcessId findFirstByOrderByIdDesc();
    void deleteByProcessId(ProcessId processId);
}
