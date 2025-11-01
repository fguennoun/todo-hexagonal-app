package rebelsrescue.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rebelsrescue.fleet.api.AssembleAFleet;
import rebelsrescue.fleet.spi.Fleets;

import java.net.URISyntaxException;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/rescueFleets")
public class RescueFleetController {
    private AssembleAFleet assembleAFleet;
    private Fleets fleets;

    public RescueFleetController(AssembleAFleet assembleAFleet, Fleets fleets) {
        this.assembleAFleet = assembleAFleet;
        this.fleets = fleets;
    }

    @PostMapping
    public ResponseEntity<FleetResource> assembleAFleet(@RequestBody RescueFleetRequest rescueFleetRequest) throws URISyntaxException {
        var fleet = assembleAFleet.forPassengers(rescueFleetRequest.numberOfPassengers);
        return created(fromMethodCall(on(this.getClass()).getFleetById(fleet.id())).build().toUri())
                .body(new FleetResource(fleet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FleetResource> getFleetById(@PathVariable UUID id) {
        return ok(new FleetResource(fleets.getById(id)));
    }
}
