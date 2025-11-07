package rebelsrescue.fleet;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CargoCapacityTest {

    @Test
    void should_create_cargo_capacity_from_string() {
        CargoCapacity capacity = CargoCapacity.of("100000");
        assertThat(capacity.value().longValue()).isEqualTo(100000);
    }

    @Test
    void should_create_cargo_capacity_from_long() {
        CargoCapacity capacity = CargoCapacity.of(100000L);
        assertThat(capacity.value().longValue()).isEqualTo(100000);
    }

    @Test
    void should_reject_null_capacity() {
        assertThatThrownBy(() -> new CargoCapacity(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cargo capacity cannot be null");
    }

    @Test
    void should_compare_cargo_capacities() {
        CargoCapacity capacity1 = CargoCapacity.of(100000L);
        CargoCapacity capacity2 = CargoCapacity.of(50000L);

        assertThat(capacity1.isSufficientFor(capacity2)).isTrue();
        assertThat(capacity2.isSufficientFor(capacity1)).isFalse();
    }
}

