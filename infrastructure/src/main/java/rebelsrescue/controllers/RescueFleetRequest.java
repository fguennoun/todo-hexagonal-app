package rebelsrescue.controllers;

/**
 * DTO for rescue fleet requests.
 */
public class RescueFleetRequest {
    public Integer numberOfPassengers;

    public RescueFleetRequest() {
    }

    public RescueFleetRequest(Integer numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }
}
