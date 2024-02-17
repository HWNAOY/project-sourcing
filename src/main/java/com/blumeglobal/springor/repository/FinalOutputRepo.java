package com.blumeglobal.springor.repository;

import com.blumeglobal.springor.models.FinalOutput;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FinalOutputRepo extends JpaRepository<FinalOutput,Long> {
    List<FinalOutput> findByProcessId(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
