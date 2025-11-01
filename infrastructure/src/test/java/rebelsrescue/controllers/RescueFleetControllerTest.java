package rebelsrescue.controllers;

import ddd.Stub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rebelsrescue.configuration.DomainConfiguration;
import rebelsrescue.fleet.Fleet;
import rebelsrescue.fleet.StarShip;
import rebelsrescue.fleet.spi.Fleets;

import java.math.BigDecimal;

import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RescueFleetController.class)
@Import(DomainConfiguration.class)
final class RescueFleetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Fleets fleets;

    @Test
    void should_assemble_a_rescue_fleet() throws Exception {
        mockMvc.perform(
                        post("/rescueFleets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"numberOfPassengers\" : 5 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.starships").value(hasSize(1)))
                .andExpect(jsonPath("$.starships[0].name").value("Millennium Falcon"))
                .andExpect(jsonPath("$.starships[0].capacity").value("6"));
    }

    @Test
    void should_return_a_fleet_given_an_id() throws Exception {
        Fleet fleet = new Fleet(singletonList(
                new StarShip("Millennium Falcon", 6, new BigDecimal("100000"))
        ));
        fleets.save(fleet);

        mockMvc.perform(
                        get("/rescueFleets/%s".formatted(fleet.id()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fleet.id().toString()))
                .andExpect(jsonPath("$.starships").value(hasSize(fleet.starships().size())))
                .andExpect(jsonPath("$.starships[0].name").value(fleet.starships().get(0).name()))
                .andExpect(jsonPath("$.starships[0].capacity").value(fleet.starships().get(0).passengersCapacity()));
    }

    @TestConfiguration
    @ComponentScan(
            basePackageClasses = {Fleet.class},
            includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Stub.class})})
    static class StubConfiguration {
    }

}
