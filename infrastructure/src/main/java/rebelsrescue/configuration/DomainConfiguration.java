package rebelsrescue.configuration;

import ddd.DomainService;
import ddd.Stub;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import rebelsrescue.fleet.Fleet;
import rebelsrescue.fleet.spi.stubs.StarShipInventoryStub;

@Configuration
@ComponentScan(
        basePackageClasses = {Fleet.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class, Stub.class})},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {StarShipInventoryStub.class})})
public class DomainConfiguration {
}
