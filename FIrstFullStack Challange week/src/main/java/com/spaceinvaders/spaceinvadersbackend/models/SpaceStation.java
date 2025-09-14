package com.spaceinvaders.spaceinvadersbackend.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class SpaceStation {

    @Id
    @GeneratedValue
    private Long stationId;

    private String name;
    private String description;

    @OneToMany(mappedBy = "spaceStation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Level> levels;

    public SpaceStation() {}

    public SpaceStation(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }
}