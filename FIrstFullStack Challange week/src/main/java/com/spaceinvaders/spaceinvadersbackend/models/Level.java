package com.spaceinvaders.spaceinvadersbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Level {

    @Id
    @GeneratedValue
    @Column(name = "level_id")
    private Long levelId;

    private int level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @JsonBackReference
    private SpaceStation spaceStation;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Dock> docks;

    public Level() {}

    public Level(int level, SpaceStation spaceStation) {
        this.level = level;
        this.spaceStation = spaceStation;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public SpaceStation getSpaceStation() {
        return spaceStation;
    }

    public void setSpaceStation(SpaceStation spaceStation) {
        this.spaceStation = spaceStation;
    }

    public List<Dock> getDocks() {
        return docks;
    }

    public void setDocks(List<Dock> docks) {
        this.docks = docks;
    }
}
