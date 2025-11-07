package rebelsrescue.fleet;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PassengerCountTest {

    @Test
    void should_create_valid_passenger_count() {
        PassengerCount count = new PassengerCount(100);
        assertThat(count.value()).isEqualTo(100);
    }

    @Test
    void should_reject_zero_passengers() {
        assertThatThrownBy(() -> new PassengerCount(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passenger count must be positive");
    }

    @Test
    void should_reject_negative_passengers() {
        assertThatThrownBy(() -> new PassengerCount(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passenger count must be positive");
    }

    @Test
    void should_reject_too_large_passenger_count() {
        assertThatThrownBy(() -> new PassengerCount(1_000_001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Passenger count too large");
    }

    @Test
    void should_compare_passenger_counts() {
        PassengerCount count = new PassengerCount(100);
        assertThat(count.isGreaterThan(50)).isTrue();
        assertThat(count.isGreaterThan(100)).isFalse();
        assertThat(count.isGreaterThan(150)).isFalse();
    }
}

