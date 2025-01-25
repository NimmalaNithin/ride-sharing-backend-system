package com.spring.ride.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ride.dto.DriverRequest;
import com.spring.ride.dto.RideRequest;
import com.spring.ride.dto.RideResponse;
import com.spring.ride.model.Driver;
import com.spring.ride.model.DriverStatus;
import com.spring.ride.service.RideServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RideController.class)
@ExtendWith(MockitoExtension.class)
class RideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RideServiceImpl rideServiceImpl;


    @Test
    void addDriverShouldReturnCreatedDriver() throws Exception {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setName("Nithin");
        driverRequest.setLocation(Map.of("x", 0.0, "y", 0.0));

        Driver driver = new Driver();
        driver.setName("Nithin");
        driver.setX(0.0);
        driver.setY(0.0);
        driver.setStatus(DriverStatus.AVAILABLE);

        when(rideServiceImpl.addDriver(any(DriverRequest.class))).thenReturn(driver);

        mockMvc.perform(post("/rides/driver")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(driverRequest))
                        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nithin"))
                .andExpect(jsonPath("$.x").value(0.0))
                .andExpect(jsonPath("$.y").value(0.0))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void addDriverShouldReturnBadRequestForInvalidInput() throws Exception {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setName("driver");

        mockMvc.perform(post("/rides/driver")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(driverRequest))
                        )
                .andExpect(status().isBadRequest());
    }


    @Test
    void requestRideShouldReturnRideResponse() throws Exception {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setName("nnr");
        rideRequest.setStartLocation(Map.of("x", 3.0, "y", 0.0));
        rideRequest.setEndLocation(Map.of("x", 3.0, "y", 8.0));

        RideResponse rideResponse = new RideResponse();
        rideResponse.setDriver("Nithin");
        rideResponse.setFare(8.0);
        rideResponse.setTripStartTime(null);

        when(rideServiceImpl.requestRide(any(RideRequest.class))).thenReturn(rideResponse);

        mockMvc.perform(post("/rides/requestRide")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(rideRequest))
                        )
                .andExpect(jsonPath("$.driver").value("Nithin"))
                .andExpect(jsonPath("$.fare").value(8.0))
                .andExpect(jsonPath("$.tripStartTime").doesNotExist());
    }

    @Test
    void requestRide_ShouldReturnBadRequestForInvalidInput() throws Exception {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setName("nnr");

        mockMvc.perform(post("/rides/requestRide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rideRequest)))
                .andExpect(status().isBadRequest());
    }
}