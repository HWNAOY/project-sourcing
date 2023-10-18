package com.blumeglobal.springor.models;

import jakarta.persistence.*;

@Entity
@Table(name="MyConstraint")
public class Constraint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long laneid;
    private String carrier;

    private String type;

    private String constraintType;
    private String minimax;

    private Long value;

    private String description;
    @ManyToOne
    @JoinColumn(name = "process_id")
    private ProcessId processId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLaneid() {
        return laneid;
    }

    public void setLaneid(Long laneid) {
        this.laneid = laneid;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getMinimax() {
        return minimax;
    }

    public void setMinimax(String minimax) {
        this.minimax = minimax;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(ProcessId processId) {
        this.processId = processId;
    }
}
