package ru.project.my.eventmanager.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.project.my.eventmanager.controllers.dto.LocationDto;
import ru.project.my.eventmanager.converters.LocationDtoConverter;
import ru.project.my.eventmanager.services.LocationService;
import ru.project.my.eventmanager.services.model.Location;

import java.util.List;

@RestController
public class LocationController {
    private final LocationService locationService;
    private final LocationDtoConverter dtoConverter;

    public LocationController(LocationService locationService, LocationDtoConverter dtoConverter) {
        this.locationService = locationService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping("/locations")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(locations));
    }

    @PostMapping("/locations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationDto) {
        Location location = locationService.createLocation(dtoConverter.toLocation(locationDto, null));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(location));
    }

    @DeleteMapping("/locations/{locationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long locationId) {
        locationService.deleteLocation(locationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/locations/{locationId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long locationId) {
        Location location = locationService.getLocation(locationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(location));
    }

    @PutMapping("/locations/{locationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long locationId, @Valid @RequestBody LocationDto locationDto) {
        Location location = dtoConverter.toLocation(locationDto, locationId);

        location = locationService.updateLocation(location);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dtoConverter.toDto(location));
    }
}
