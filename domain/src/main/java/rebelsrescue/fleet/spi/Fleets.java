package rebelsrescue.fleet.spi;

import ddd.Repository;
import rebelsrescue.fleet.Fleet;
import rebelsrescue.fleet.FleetNotFoundException;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Fleet Aggregate Root.
 * Follows DDD Repository pattern - provides collection-like interface.
 */
@Repository
public interface Fleets {

    /**
     * Find a fleet by its ID.
     * @throws FleetNotFoundException if fleet is not found
     */
    Fleet getById(UUID id);

    /**
     * Find a fleet by its ID, returning Optional.
     */
    Optional<Fleet> findById(UUID id);

    /**
     * Save a fleet (create or update).
     */
    Fleet save(Fleet fleet);

    /**
     * Check if a fleet exists.
     */
    boolean exists(UUID id);
}
