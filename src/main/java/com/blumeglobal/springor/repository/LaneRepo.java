package com.blumeglobal.springor.repository;

import com.blumeglobal.springor.models.Lanes;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LaneRepo extends JpaRepository<Lanes,Long> {
    List<Lanes> findByProcessId(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
