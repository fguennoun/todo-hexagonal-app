package rebelsrescue.fleet;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import rebelsrescue.fleet.api.AssembleAFleet;
import rebelsrescue.fleet.events.FleetAssembledEvent;
import rebelsrescue.fleet.events.stubs.InMemoryEventPublisher;
import rebelsrescue.fleet.spi.Fleets;
import rebelsrescue.fleet.spi.StarShipInventory;
import rebelsrescue.fleet.spi.stubs.InMemoryFleets;
import rebelsrescue.fleet.spi.stubs.StarShipInventoryStub;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rebelsrescue.fleet.specifications.StarShipSpecifications.*;

class AssembleAFleetFunctionalTest {

    @Test
    void should_assemble_a_fleet_for_1050_passengers() {
        //Given
        var starShips = List.of(
                new StarShip("no-passenger-ship", 0, CargoCapacity.of("1000")),
                new StarShip("xs", 10, CargoCapacity.of("1000")),
                new StarShip("s", 50, CargoCapacity.of("50000")),
                new StarShip("m", 200, CargoCapacity.of("70000")),
                new StarShip("l", 800, CargoCapacity.of("150000")),
                new StarShip("xl", 2000, CargoCapacity.of("500000")));
        var numberOfPassengers = 1050;

        StarShipInventory starShipsInventory = new StarShipInventoryStub(starShips);
        Fleets fleets = new InMemoryFleets();
        InMemoryEventPublisher eventPublisher = new InMemoryEventPublisher();
        AssembleAFleet assembleAFleet = new FleetAssembler(starShipsInventory, fleets, eventPublisher);

        //When
        Fleet fleet = assembleAFleet.forPassengers(numberOfPassengers);

        //Then
        System.out.println(fleet);
        assertThat(fleet.starships())
                .has(enoughCapacityForThePassengers(numberOfPassengers))
                .allMatch(hasPassengerCapacity())
                .allMatch(hasSufficientCargoCapacity(), "hasSufficientCargoCapacity");

        assertThat(fleets.getById(fleet.id())).isEqualTo(fleet);
        assertThat(fleet.canAccommodate(numberOfPassengers)).isTrue();

        // Verify domain event was published
        List<FleetAssembledEvent> events = eventPublisher.getEventsOfType(FleetAssembledEvent.class);
        assertThat(events).hasSize(1);
        assertThat(events.get(0).fleetId()).isEqualTo(fleet.id());
        assertThat(events.get(0).requestedPassengers()).isEqualTo(numberOfPassengers);
    }

    @Test
    void should_throw_exception_when_insufficient_starships() {
        // Given
        var starShips = List.of(
                new StarShip("small", 10, CargoCapacity.of("150000")));
        var numberOfPassengers = 100;

        StarShipInventory starShipsInventory = new StarShipInventoryStub(starShips);
        Fleets fleets = new InMemoryFleets();
        InMemoryEventPublisher eventPublisher = new InMemoryEventPublisher();
        AssembleAFleet assembleAFleet = new FleetAssembler(starShipsInventory, fleets, eventPublisher);

        // When/Then
        assertThatThrownBy(() -> assembleAFleet.forPassengers(numberOfPassengers))
                .isInstanceOf(InsufficientStarShipsException.class)
                .hasMessageContaining("Cannot assemble fleet");
    }

    @Test
    void should_throw_exception_for_invalid_passenger_count() {
        StarShipInventory starShipsInventory = new StarShipInventoryStub();
        Fleets fleets = new InMemoryFleets();
        InMemoryEventPublisher eventPublisher = new InMemoryEventPublisher();
        AssembleAFleet assembleAFleet = new FleetAssembler(starShipsInventory, fleets, eventPublisher);

        assertThatThrownBy(() -> assembleAFleet.forPassengers(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passenger count must be positive");

        assertThatThrownBy(() -> assembleAFleet.forPassengers(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passenger count must be positive");
    }

    private Predicate<? super StarShip> hasPassengerCapacity() {
        return starShip -> starShip.passengersCapacity() > 0;
    }


    private Condition<? super List<? extends StarShip>> enoughCapacityForThePassengers(int numberOfPassengers) {
        return new Condition<>(
                starShips ->
                        starShips.stream()
                                .map(StarShip::passengersCapacity)
                                .reduce(0, Integer::sum) >= numberOfPassengers,
                "passengersCapacity check");
    }

}
