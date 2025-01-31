package com.spring.ride.service;

import com.spring.ride.exception.DriverNotFoundException;
import com.spring.ride.dto.DriverRequest;
import com.spring.ride.dto.RideRequest;
import com.spring.ride.dto.RideResponse;
import com.spring.ride.model.Driver;
import com.spring.ride.model.DriverStatus;
import com.spring.ride.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RideServiceImpl implements RideService {

    private DriverRepository driverRepository;
    @Value("${farePerUnit}")
    private Double farePerUnit;

    @Autowired
    public RideServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Double getFarePerUnit() {
        return farePerUnit;
    }

    @Override
    public Driver addDriver(DriverRequest driverRequest) {
        Driver driver = new Driver();
        driver.setName(driverRequest.getName());

        driver.setX(driverRequest.getLocation().get("x"));
        driver.setY(driverRequest.getLocation().get("y"));

        return driverRepository.save(driver);
    }

    @Override
    public RideResponse requestRide(RideRequest rideRequest) {
        RideResponse rideResponse = new RideResponse();
        Driver driver = searchNearestDriver(
                rideRequest.getStartLocation().get("x"),
                rideRequest.getStartLocation().get("y")
        );

        Double fare = calculateFare(rideRequest.getStartLocation(), rideRequest.getEndLocation());

        rideResponse.setDriver(driver.getName());
        rideResponse.setFare(fare);
        rideResponse.setTripStartTime(LocalDateTime.now());

        return rideResponse;
    }

    private Driver searchNearestDriver(Double riderStartX, Double riderStartY) {
        List<Driver> drivers = driverRepository.findAll();
        Driver nearestDriver = null;
        Double nearestDriverDistance = Double.MAX_VALUE;
        for(Driver driver : drivers) {
            if(driver.getStatus().equals(DriverStatus.AVAILABLE)) {
                Double distance = calculateDistance(riderStartX, riderStartY, driver.getX(), driver.getY());
                if (distance < nearestDriverDistance) {
                    nearestDriver = driver;
                    nearestDriverDistance = distance;
                }
            }
        }
        if(nearestDriver == null) {
            throw new DriverNotFoundException("No available driver available");
        }
        nearestDriver.setStatus(DriverStatus.BUSY);
        driverRepository.save(nearestDriver);
        return nearestDriver;
    }

    private Double calculateDistance(Double x1, Double y1, Double x2, Double y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }

    private Double calculateFare(Map<String, Double> startLocation, Map<String, Double> endLocation) {
        Double distance = calculateDistance(startLocation.get("x"),startLocation.get("y"),endLocation.get("x"),endLocation.get("y"));
        return distance*farePerUnit;
    }
}

