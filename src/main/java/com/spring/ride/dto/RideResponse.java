package com.spring.ride.dto;

import java.time.LocalDateTime;

public class RideResponse {
    private LocalDateTime TripStartTime;
    private String driver;
    private Double fare;

    public RideResponse() {}

    public RideResponse(LocalDateTime tripStartTime, String driver, Double fare) {
        TripStartTime = tripStartTime;
        this.driver = driver;
        this.fare = fare;
    }

    public LocalDateTime getTripStartTime() {
        return TripStartTime;
    }

    public void setTripStartTime(LocalDateTime tripStartTime) {
        TripStartTime = tripStartTime;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }
}
