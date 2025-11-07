package rebelsrescue.fleet.spi.stubs;

import ddd.Stub;
import rebelsrescue.fleet.CargoCapacity;
import rebelsrescue.fleet.StarShip;
import rebelsrescue.fleet.spi.StarShipInventory;

import java.util.List;

import static java.util.Arrays.asList;

@Stub
public final class StarShipInventoryStub implements StarShipInventory {

    private static final List<StarShip> DEFAULT_STARSHIPS = asList(
            new StarShip("X-Wing", 0, CargoCapacity.of("100")),
            new StarShip("Millennium Falcon", 6, CargoCapacity.of("100000")),
            new StarShip("Rebel transport", 90, CargoCapacity.of("80000")),
            new StarShip("Mon Calamari Star Cruisers", 1200, CargoCapacity.of("200000")),
            new StarShip("CR90 corvette", 600, CargoCapacity.of("300000")),
            new StarShip("EF76 Nebulon-B escort frigate", 800, CargoCapacity.of("350000")));

    private final List<StarShip> starShips;

    public StarShipInventoryStub() {
        starShips = DEFAULT_STARSHIPS;
    }

    public StarShipInventoryStub(List<StarShip> starShips) {
        this.starShips = starShips;
    }

    public List<StarShip> starShips() {
        return starShips;
    }

}
