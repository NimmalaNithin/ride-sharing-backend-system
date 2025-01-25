package com.spring.ride.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class RideRequest {
    @NotEmpty(message="enter a valid name")
    private String name;
    @NotNull(message = "start locaion required")
    private Map<String, Double> startLocation;
    @NotNull(message = "end locaion required")
    private Map<String, Double> endLocation;

    public RideRequest() {}

    public RideRequest(String name, Map<String, Double> startLocation, Map<String, Double> endLocation) {
        this.name = name;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Map<String, Double> startLocation) {
        this.startLocation = startLocation;
    }

    public Map<String, Double> getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Map<String, Double> endLocation) {
        this.endLocation = endLocation;
    }
}
