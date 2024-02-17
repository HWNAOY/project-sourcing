package com.blumeglobal.springor.models;

import jakarta.persistence.*;

@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carrier;
    private Long laneid;
    private Long commitment;
    private Long rate;
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

    public Long getLaneid() {
        return laneid;
    }

    public void setLaneid(Long laneid) {
        this.laneid = laneid;
    }

    public Long getCommitment() {
        return commitment;
    }

    public void setCommitment(Long commitment) {
        this.commitment = commitment;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(ProcessId processId) {
        this.processId = processId;
    }
}
