package com.blumeglobal.springor.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Lanes")
public class Lanes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "from_loc")
    private Long laneid;
    @Column(name = "src")
    private String from;

//    @ManyToOne
//    @JoinColumn(name = "to_loc")
    @Column(name= "dst")
    private String to;
    private Long volume;
    @ManyToOne
    @JoinColumn(name = "process_id")
    private ProcessId processId;

    public Lanes() {
    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(ProcessId processId) {
        this.processId = processId;
    }
}
