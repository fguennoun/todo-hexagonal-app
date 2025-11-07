package rebelsrescue.fleet.spi.stubs;

import ddd.Stub;
import rebelsrescue.fleet.Fleet;
import rebelsrescue.fleet.FleetNotFoundException;
import rebelsrescue.fleet.spi.Fleets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Stub
public class InMemoryFleets implements Fleets {
    private final Map<UUID, Fleet> fleets = new HashMap<>();

    @Override
    public Fleet getById(UUID id) {
        Fleet fleet = fleets.get(id);
        if (fleet == null) {
            throw new FleetNotFoundException(id.toString());
        }
        return fleet;
    }

    @Override
    public Optional<Fleet> findById(UUID id) {
        return Optional.ofNullable(fleets.get(id));
    }

    @Override
    public Fleet save(Fleet fleet) {
        fleets.put(fleet.id(), fleet);
        return fleet;
    }

    @Override
    public boolean exists(UUID id) {
        return fleets.containsKey(id);
    }
}
