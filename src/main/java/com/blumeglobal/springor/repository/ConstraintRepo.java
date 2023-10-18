package com.blumeglobal.springor.repository;

import com.blumeglobal.springor.models.Constraint;
import com.blumeglobal.springor.models.ProcessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ConstraintRepo extends JpaRepository<Constraint,Long> {
    List<Constraint> findByProcessId(ProcessId processId);
    void deleteByProcessId(ProcessId processId);
}
