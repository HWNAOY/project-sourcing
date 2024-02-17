package com.blumeglobal.springor.models;

import jakarta.persistence.*;

@Entity
public class FinalOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carrier;
    private Long laneid;
    private Double shipments;
    private Long commitment;
    private Double specificCost;
    @ManyToOne
    @JoinColumn(name = "process_id")
    private ProcessId processId;

    public FinalOutput() {
    }

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

    public Double getShipments() {
        return shipments;
    }

    public void setShipments(Double shipments) {
        this.shipments = shipments;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(ProcessId processId) {
        this.processId = processId;
    }

    public Long getCommitment() {
        return commitment;
    }

    public void setCommitment(Long commitment) {
        this.commitment = commitment;
    }

    public Double getSpecificCost() {
        return specificCost;
    }

    public void setSpecificCost(Double specificCost) {
        this.specificCost = specificCost;
    }
}
