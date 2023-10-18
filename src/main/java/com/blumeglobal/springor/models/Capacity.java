package com.blumeglobal.springor.models;

import jakarta.persistence.*;

@Entity
public class Capacity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carrier;
    private Long capacity;
    private Long blumeScore;
    @ManyToOne
    @JoinColumn(name = "process_id")
    private ProcessId processId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getBlumeScore() {
        return blumeScore;
    }

    public void setBlumeScore(Long blumeScore) {
        this.blumeScore = blumeScore;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(ProcessId processId) {
        this.processId = processId;
    }
}
