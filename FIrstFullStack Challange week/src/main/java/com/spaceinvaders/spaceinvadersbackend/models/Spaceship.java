package com.spaceinvaders.spaceinvadersbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Spaceship {

    @Id
    @GeneratedValue
    @Column(name = "ship_id")
    private Long shipId;

    private String shipName;

    @Enumerated(EnumType.STRING)
    private SpaceshipType shipType;

    @Enumerated(EnumType.STRING)
    private Size size;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private CustomUser owner;

    @OneToMany(mappedBy = "spaceship")
    @JsonBackReference
    private List<Reservation> reservations;


    public Spaceship() {}

    public Spaceship(String shipName, SpaceshipType shipType, Size size, String imagePath) {
        this.shipName = shipName;
        this.shipType = shipType;
        this.size = size;
        this.imagePath = imagePath;
    }

    public Long getShipId() {
        return shipId;
    }

    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public SpaceshipType getShipType() {
        return shipType;
    }

    public void setShipType(SpaceshipType shipType) {
        this.shipType = shipType;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public CustomUser getOwner() {
        return owner;
    }

    public void setOwner(CustomUser owner) {
        this.owner = owner;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
