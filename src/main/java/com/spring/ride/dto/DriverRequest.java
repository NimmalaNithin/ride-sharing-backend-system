package com.spring.ride.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class DriverRequest {
    @NotEmpty(message = "enter valid name")
    private String name;
    @NotNull(message = "location required")
    private Map<String, Double> location;

    public DriverRequest() {}

    public DriverRequest(String name, Map<String, Double> location) {
        this.name = name;
        this.location = location;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<String, Double> getLocation() {
        return location;
    }
    public void setLocation(Map<String, Double> location) {
        this.location = location;
    }
}
