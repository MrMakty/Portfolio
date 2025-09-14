package com.spaceinvaders.spaceinvadersbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private CustomUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    @JsonBackReference
    private Spaceship spaceship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dock_id")
    @JsonBackReference
    private Dock dock;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTimestamp;

    private Boolean isStandingHere;

    public Reservation() {}

    public Reservation(Spaceship spaceship, Dock dock, Date startTimestamp, Date endTimestamp, Boolean isStandingHere) {
        this.spaceship = spaceship;
        this.dock = dock;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.isStandingHere = isStandingHere;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public void setSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Boolean getStandingHere() {
        return isStandingHere;
    }

    public void setStandingHere(Boolean standingHere) {
        isStandingHere = standingHere;
    }
}
