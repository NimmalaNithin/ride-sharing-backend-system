package com.spring.ride.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.ride.dto.DriverRequest;
import com.spring.ride.dto.RideRequest;
import com.spring.ride.service.RideServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RideServiceImpl rideServiceImpl;

    @Test
    public void handleDriverNotFoundExceptionShouldReturnNotFound() throws Exception {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setName("John");
        rideRequest.setStartLocation(Map.of("x", 0.0, "y", 0.0));
        rideRequest.setEndLocation(Map.of("x", 5.0, "y", 5.0));

        when(rideServiceImpl.requestRide(any(RideRequest.class)))
                .thenThrow(new DriverNotFoundException("Driver Not Found"));

        mockMvc.perform(post("/rides/requestRide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rideRequest))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Driver Not Found"));
    }

    @Test
    void handleHttpMessageNotReadableExceptionShouldReturnBadRequest() throws Exception {
        String json = "{\"name\":\"nithin\",\"location\":{\"x\":1.0,\"y\":\"2e\"}";

        mockMvc.perform(post("/rides/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("invalid json datatypes"));
    }

    @Test
    void handleValidatonExceptionShouldReturnValidationErrors() throws Exception {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setName("");
        driverRequest.setLocation(null);

        mockMvc.perform(post("/rides/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.location").exists());
    }

    @Test
    void handleGenericException_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/rides/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }


}