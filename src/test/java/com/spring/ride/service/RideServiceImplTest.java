package com.spring.ride.service;

import com.spring.ride.dto.DriverRequest;
import com.spring.ride.dto.RideRequest;
import com.spring.ride.dto.RideResponse;
import com.spring.ride.exception.DriverNotFoundException;
import com.spring.ride.model.Driver;
import com.spring.ride.model.DriverStatus;
import com.spring.ride.repository.DriverRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class RideServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    private AutoCloseable autoCloseable;
    private RideServiceImpl rideServiceImpl;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {

        autoCloseable = MockitoAnnotations.openMocks(this);
        rideServiceImpl = new RideServiceImpl(driverRepository);

        Field farePerUnit = RideServiceImpl.class.getDeclaredField("farePerUnit");
        farePerUnit.setAccessible(true);
        farePerUnit.set(rideServiceImpl, 1.0);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void addDriverShouldSaveAndReturnDriver() {
    // Arrange
        // input setup
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setName("driver1");
        driverRequest.setLocation(Map.of("x", 1.0, "y", 1.0));

        // expected output
        Driver driver = new Driver();
        driver.setName("driver1");
        driver.setX(0.0);
        driver.setY(3.0);
        driver.setStatus(DriverStatus.AVAILABLE);

        // Mocking Repository behaviour.
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

    // Act
        Driver savedDriver = rideServiceImpl.addDriver(driverRequest);

    // Assert
        assertNotNull(savedDriver);
        assertEquals(driver.getName(), savedDriver.getName());
        assertEquals(driver.getX(), savedDriver.getX());
        assertEquals(driver.getY(), savedDriver.getY());
        assertEquals(driver.getStatus(), savedDriver.getStatus());

        verify(driverRepository, times(1)).save(any(Driver.class));

    }

    @Test
    void requestRideShouldAssignNearestDriverWhenMultipleDriversAvailable() {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setName("rider");
        rideRequest.setStartLocation(Map.of("x", 3.0, "y", 0.0));
        rideRequest.setEndLocation(Map.of("x", 3.0, "y", 8.0));

        Driver driver1 = new Driver();
        driver1.setName("driver1");
        driver1.setX(0.0);
        driver1.setY(0.0);
        driver1.setStatus(DriverStatus.AVAILABLE);

        Driver driver2 = new Driver();
        driver2.setName("driver2");
        driver2.setX(10.0);
        driver2.setY(0.0);
        driver2.setStatus(DriverStatus.AVAILABLE);

        when(driverRepository.findAll()).thenReturn(List.of(driver1,driver2));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver1);

        RideResponse rideResponse = rideServiceImpl.requestRide(rideRequest);

        assertNotNull(rideResponse);
        assertEquals(driver1.getName(), rideResponse.getDriver());
        assertEquals(rideServiceImpl.getFarePerUnit()*8.0, rideResponse.getFare());
        assertNotNull(rideResponse.getTripStartTime());

        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void requestRideShouldIgnoreUnavailableDriversWhenMultipleDriversAvailable() {
        // Arrange
        RideRequest rideRequest = new RideRequest();
        rideRequest.setName("rider");
        rideRequest.setStartLocation(Map.of("x", 2.0, "y", 0.0));
        rideRequest.setEndLocation(Map.of("x", 10.0, "y", 10.0));

        Driver driver1 = new Driver();
        driver1.setName("Driver 1");
        driver1.setX(0.0);
        driver1.setY(0.0);
        driver1.setStatus(DriverStatus.BUSY);

        Driver driver2 = new Driver();
        driver2.setName("Driver 2");
        driver2.setX(10.0);
        driver2.setY(0.0);
        driver2.setStatus(DriverStatus.AVAILABLE);

        // Mock the driver repository
        when(driverRepository.findAll()).thenReturn(List.of(driver1, driver2));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver2);

        // Act
        RideResponse rideResponse= rideServiceImpl.requestRide(rideRequest);

        // Assert
        assertNotNull(rideResponse);
        assertEquals("Driver 2", rideResponse.getDriver());
        assertEquals(rideServiceImpl.getFarePerUnit()*Math.sqrt(164), rideResponse.getFare());
        assertNotNull(rideResponse.getTripStartTime());

        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void requestRideShouldThrowExceptionWhenNoAvailableDrivers() {
        RideRequest rideRequest = new RideRequest();
        rideRequest.setName("rider");
        rideRequest.setStartLocation(Map.of("x", 3.0, "y", 0.0));
        rideRequest.setEndLocation(Map.of("x", 3.0, "y", 10.0));

        when(driverRepository.findAll()).thenReturn(List.of());

        assertThrows(DriverNotFoundException.class, () -> rideServiceImpl.requestRide(rideRequest));
        verify(driverRepository, never()).save(any(Driver.class));
    }


}