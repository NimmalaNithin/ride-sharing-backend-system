package com.spring.ride.controller;

import com.spring.ride.dto.DriverRequest;
import com.spring.ride.dto.RideRequest;
import com.spring.ride.dto.RideResponse;
import com.spring.ride.model.Driver;
import com.spring.ride.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rides")
public class RideController {

    public RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/driver")
    public ResponseEntity<Driver> addDriver(@Valid @RequestBody DriverRequest driverRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rideService.addDriver(driverRequest));
    }

    @PostMapping("/requestRide")
    public ResponseEntity<RideResponse> requestRide(@Valid @RequestBody RideRequest rideRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rideService.requestRide(rideRequest));
    }
}
