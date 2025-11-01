package rebelsrescue.controllers;

import rebelsrescue.fleet.StarShip;

import java.util.Map;

public record StarShipResource(String name, int capacity, int passengersCapacity, Map<String, String> deprecations) {
    public StarShipResource(StarShip starShip) {
        this(
                starShip.name(),
                starShip.passengersCapacity(),
                starShip.passengersCapacity(),
                Map.of("capacity", "passengersCapacity"));
    }
}
