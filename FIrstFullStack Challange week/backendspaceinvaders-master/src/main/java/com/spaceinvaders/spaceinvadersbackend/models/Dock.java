package com.spaceinvaders.spaceinvadersbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Dock {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Size spaceSize;
    private boolean inMaintenance;

    @OneToMany(mappedBy = "dock")
    @JsonManagedReference
    private List<Reservation> reservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    @JsonBackReference
    private Level level;

    public Dock() {}

    public Dock(Size spaceSize, boolean inMaintenance, Level level) {
        this.spaceSize = spaceSize;
        this.inMaintenance = inMaintenance;
        this.level = level;
    }

    public Size getSpaceSize() {
        return spaceSize;
    }

    public void setSpaceSize(Size spaceSize) {
        this.spaceSize = spaceSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInMaintenance() {
        return inMaintenance;
    }

    public void setInMaintenance(boolean inMaintenance) {
        this.inMaintenance = inMaintenance;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
