package com.blumeglobal.springor.models;

import jakarta.persistence.*;

@Entity
public class ProcessId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String processId;
    private Double solverTime;
    private Double finalCost;

    public ProcessId(String processId) {
        this.processId = processId;
    }

    public ProcessId() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Double getSolverTime() {
        return solverTime;
    }

    public void setSolverTime(Double solverTime) {
        this.solverTime = solverTime;
    }

    public Double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Double finalCost) {
        this.finalCost = finalCost;
    }
}
