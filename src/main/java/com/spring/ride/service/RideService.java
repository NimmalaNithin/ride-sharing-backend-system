package com.spring.ride.service;

import com.spring.ride.dto.DriverRequest;
import com.spring.ride.dto.RideRequest;
import com.spring.ride.dto.RideResponse;
import com.spring.ride.model.Driver;

public interface RideService {
    Driver addDriver(DriverRequest driverRequest);
    RideResponse requestRide(RideRequest rideRequest);
}
