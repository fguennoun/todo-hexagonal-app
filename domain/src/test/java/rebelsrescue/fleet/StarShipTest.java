package rebelsrescue.fleet;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StarShipTest {

    @Test
    void should_create_valid_starship() {
        StarShip starShip = new StarShip("X-Wing", 4, CargoCapacity.of(100));

        assertThat(starShip.name()).isEqualTo("X-Wing");
        assertThat(starShip.passengersCapacity()).isEqualTo(4);
        assertThat(starShip.cargoCapacity().value().longValue()).isEqualTo(100);
    }

    @Test
    void should_reject_blank_name() {
        assertThatThrownBy(() -> new StarShip("", 4, CargoCapacity.of(100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("StarShip name cannot be blank");
    }

    @Test
    void should_reject_null_name() {
        assertThatThrownBy(() -> new StarShip(null, 4, CargoCapacity.of(100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("StarShip name cannot be blank");
    }

    @Test
    void should_reject_negative_passenger_capacity() {
        assertThatThrownBy(() -> new StarShip("X-Wing", -1, CargoCapacity.of(100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passengers capacity cannot be negative");
    }

    @Test
    void should_calculate_total_passenger_capacity() {
        var starShips = java.util.List.of(
            new StarShip("Ship1", 100, CargoCapacity.of(1000)),
            new StarShip("Ship2", 200, CargoCapacity.of(2000)),
            new StarShip("Ship3", 300, CargoCapacity.of(3000))
        );

        int total = StarShip.totalPassengerCapacity(starShips);
        assertThat(total).isEqualTo(600);
    }
}

