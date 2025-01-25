package com.spring.ride.model;

import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "x",nullable = false)
    private Double x;

    @Column(name = "y",nullable = false)
    private Double y;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private DriverStatus status;

    @PrePersist
    public void prePersist() {
        status = DriverStatus.AVAILABLE;
    }

    public Driver() {}

    public Driver(UUID id, String name, Double x, Double y, DriverStatus status) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", status=" + status +
                '}';
    }
}















