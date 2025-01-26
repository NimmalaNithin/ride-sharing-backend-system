# Ride-Sharing Backend System
This is a basic ride-sharing backend system built using Spring Boot. It supports adding drivers, requesting rides by users which assigns drivers based on proximity, and calculates fares.

## Table of Contents
1. [Project Overview](#project-overview)
2. [Requirements](#requirements)
3. [Features](#features)
4. [Endpoints](#endpoints)
5. [Installation](#installation)

## Project Overview
The ride-sharing backend allows passengers to request rides, and the system assigns the nearest available driver to the passenger. Drivers can be added with their location and availability, and when a ride is requested, the system calculates the fare based on distance.

## Requirements
- **Spring Boot**: The project is built using Spring Boot to set up the backend application and manage dependencies.
- **Spring Web**: This project uses Spring Web to implement RESTful endpoints.
- **In-memory Storage**: Data for drivers and rides is stored in-memory using H2 database.
- **Java 8 or higher**: Java is used for building the backend application.

## Features
- **Driver Management**: Add drivers to the system, each with an ID, name, and current location (x, y coordinates).
- **Ride Requests**: Passengers can request a ride by providing start location and end destination.
- **Driver Assignment**: The system assigns the nearest available driver to the passenger’s location.
- **Fare Calculation**: The fare is calculated based on the distance between the pickup and destination, using a fixed rate per unit distance.

## Endpoints
All Endpoints can be tested on postman.

### Add Driver
**Endpoint:** `POST /api/rides/addDriver`
This endpoint allows adding a driver to the system.

**Request Body:**
```json
{
  "name": "Nithin",
  "location": { "x": 5, "y": 5 }
}

**Response:**
{
    "id": "c1eae587-c0c5-476f-bab2-553eb6d080e2",
    "name": "Nithin",
    "x": 5.0,
    "y": 5.0,
    "status": "AVAILABLE"
}


**Endpoint:** `POST /api/rides/requestRide`
This endpoint allows a passenger to request a ride by providing their start location and end location. The system assigns the nearest available driver. If no driver is available the system informs it to user.

**Request Body:**
```json
{
    "name":"rider",
    "startLocation":{"x":3,"y":3},
    "endLocation":{"x":7,"y":15}
}

**Response:**(if a driver is available)
{
    "driver": "Nithin",
    "fare": 12.649110640673518,
    "tripStartTime": "2025-01-25T18:55:11.6734583"
}

**Response:**(if a driver is not available)
{
    "status": 404,
    "message": "No available driver available"
}

## Installation
Follow these steps to set up the project locally:

**Clone the repository:**
git clone https://github.com/yourusername/ride-sharing-backend.git

**Navigate to the project directory:**
cd ride-sharing-backend

**Build the project:**
./mvnw clean install

**Run the application:**
./mvnw spring-boot:run

The backend will be running at http://localhost:8080.

