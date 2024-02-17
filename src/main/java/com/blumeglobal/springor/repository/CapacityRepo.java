package com.blumeglobal.springor.repository;

import com.blumeglobal.springor.models.Capacity;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CapacityRepo extends JpaRepository<Capacity,Long> {
    List<Capacity> findByProcessId(ProcessId processId);
    void deleteByProcessId(ProcessId processId);

}
