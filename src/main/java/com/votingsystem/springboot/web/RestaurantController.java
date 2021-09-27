package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.Restaurant;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(RestaurantController.URL)
@Tag(name = "Restaurant Controller", description = "Management restaurants")
public class RestaurantController {
    public static final String URL = "/api/restaurants";

    private final RestaurantRepository repository;

    @Operation(
            summary = "Get restaurant",
            description = "Allows you to get the selected restaurant by id"
    )
    @GetMapping(value = "/{restaurantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public Restaurant get(@PathVariable(name = "restaurantId") int id) {
        Restaurant restaurant = repository.findById(id).orElseThrow();
        log.info("get {}", restaurant);
        return restaurant;
    }

    @Operation(
            summary = "Get all restaurants",
            description = "Allows you to get all restaurants"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public List<Restaurant> getAll() {
        List<Restaurant> restaurantList = repository.findAll();
        log.info("get all");
        return restaurantList;
    }

    @Operation(
            summary = "Delete restaurant",
            description = "Allows you to delete the selected restaurant by id"
    )
    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Secured(value = {"ROLE_ADMIN"})
    public void delete(@PathVariable(name = "restaurantId") int id) {
        ValidationUtil.checkNotFoundWithId(repository.deleteById(id) != 0, id);
        log.info("delete restaurant id {}", id);
    }

    @Operation(
            summary = "Update restaurant",
            description = "Allows you to update the selected restaurant by id"
    )
    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Secured(value = {"ROLE_ADMIN"})
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable(name = "restaurantId") int id) {
        ValidationUtil.assureIdConsistent(restaurant, id);
        repository.save(restaurant);
        log.info("update {}", restaurant);
    }

    @Operation(
            summary = "Create restaurant",
            description = "Allows you to create a restaurant"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{restaurantId}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}