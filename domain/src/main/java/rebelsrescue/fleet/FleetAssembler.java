package rebelsrescue.fleet;

import ddd.DomainService;
import rebelsrescue.fleet.api.AssembleAFleet;
import rebelsrescue.fleet.events.DomainEventPublisher;
import rebelsrescue.fleet.events.FleetAssembledEvent;
import rebelsrescue.fleet.spi.Fleets;
import rebelsrescue.fleet.spi.StarShipInventory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparingInt;
import static rebelsrescue.fleet.specifications.StarShipSpecifications.*;

/**
 * Domain Service responsible for assembling rescue fleets.
 * Implements the business logic for selecting optimal starships.
 */
@DomainService
public class FleetAssembler implements AssembleAFleet {
    private final StarShipInventory starshipsInventory;
    private final Fleets fleets;
    private final DomainEventPublisher eventPublisher;

    public FleetAssembler(StarShipInventory starShipsInventory, Fleets fleets, DomainEventPublisher eventPublisher) {
        this.starshipsInventory = starShipsInventory;
        this.fleets = fleets;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Fleet forPassengers(int numberOfPassengers) {
        PassengerCount passengerCount = new PassengerCount(numberOfPassengers);

        List<StarShip> suitableStarShips = getSuitableStarShips();

        if (suitableStarShips.isEmpty()) {
            throw new InsufficientStarShipsException(numberOfPassengers, 0);
        }

        List<StarShip> selectedStarShips = selectStarShips(passengerCount, suitableStarShips);
        Fleet fleet = new Fleet(selectedStarShips);
        Fleet savedFleet = fleets.save(fleet);

        // Publish domain event
        publishFleetAssembledEvent(savedFleet, numberOfPassengers);

        return savedFleet;
    }

    private List<StarShip> selectStarShips(PassengerCount passengerCount, List<StarShip> starShips) {
        List<StarShip> rescueStarShips = new ArrayList<>();
        int remainingPassengers = passengerCount.value();

        // Sort by passenger capacity (smallest first) for optimal selection
        List<StarShip> mutableStarShips = new ArrayList<>(starShips);
        mutableStarShips.sort(comparingInt(StarShip::passengersCapacity));

        for (StarShip starShip : mutableStarShips) {
            if (remainingPassengers <= 0) {
                break;
            }
            rescueStarShips.add(starShip);
            remainingPassengers -= starShip.passengersCapacity();
        }

        if (remainingPassengers > 0) {
            int availableCapacity = StarShip.totalPassengerCapacity(mutableStarShips);
            throw new InsufficientStarShipsException(passengerCount.value(), availableCapacity);
        }

        return rescueStarShips;
    }

    private List<StarShip> getSuitableStarShips() {
        return starshipsInventory.starShips().stream()
                .filter(isSuitableForRescue())
                .toList();
    }

    private void publishFleetAssembledEvent(Fleet fleet, int requestedPassengers) {
        FleetAssembledEvent event = new FleetAssembledEvent(
            fleet.id(),
            fleet.size(),
            fleet.totalPassengerCapacity(),
            requestedPassengers
        );
        eventPublisher.publish(event);
    }
}
