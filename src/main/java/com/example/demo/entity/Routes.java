package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "routes")
public class Routes {
    @Id
    @Column(name="laneID")
    private  Long LaneID;

    @Column(name="f_rom")
    private String from;

    @Column(name="t_o")
    private String to;

    @Column(name="volume")
    private Integer volume;


    public Long getLaneID() {
        return LaneID;
    }

    public void setLaneID(Long laneID) {
        LaneID = laneID;
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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
