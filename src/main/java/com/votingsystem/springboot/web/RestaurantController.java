package com.votingsystem.springboot.web;

import com.votingsystem.springboot.AuthUser;
import com.votingsystem.springboot.model.Restaurant;
import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.model.Vote;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.repository.VoteRepository;
import com.votingsystem.springboot.util.ValidationUtil;
import com.votingsystem.springboot.util.exception.VoteException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.votingsystem.springboot.util.DateTimeUtil.isTimeToCancelVote;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(RestaurantController.URL)
@Tag(name = "Restaurant Controller", description = "Management restaurants")
public class RestaurantController {
    public static final String URL = "/api/restaurants";

    private final RestaurantRepository repository;

    private final VoteRepository voteRepo;

    @Operation(
            summary = "Get restaurant",
            description = "Allows you to get the selected restaurant by id"
    )
    @GetMapping("/{restaurantId}")
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
    @GetMapping
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
    @Secured(value = {"ROLE_ADMIN"})
    public void delete(@PathVariable(name = "restaurantId") int id) {
        ValidationUtil.checkNotFoundWithId(repository.deleteById(id) != 0, id);
        log.info("delete restaurant id {}", id);
    }

    @Operation(
            summary = "Update restaurant",
            description = "Allows you to update the selected restaurant by id"
    )
    @PutMapping("/{restaurantId}")
    @Transactional
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
    @PostMapping
    @Transactional
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

    @Operation(
            summary = "Vote for restaurant",
            description = "Allows you to vote the selected restaurant by id"
    )
    @PatchMapping("/{restaurantId}")
    @Transactional
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void doVote(@PathVariable(name = "restaurantId") int id, @AuthenticationPrincipal AuthUser authUser) {
        User user = authUser.getUser();
        Restaurant restaurant = repository.getById(id);
        voteRepo.save(new Vote(user, restaurant));
        log.info("{} voted for {} ", user, restaurant);
    }

    @Operation(
            summary = "Cancel vote",
            description = "Allows you to cancel a vote"
    )
    @PatchMapping("/cancel-vote")
    @Transactional
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public void cancelVote(@AuthenticationPrincipal AuthUser authUser) {
        if (!isTimeToCancelVote()) {
            throw new VoteException("Access to cancel vote after 11h is denied");
        }
        User user = authUser.getUser();
        Vote vote = voteRepo.get(user.id(), LocalDate.now()).orElseThrow();
        Restaurant restaurant = vote.getRestaurant();
        voteRepo.delete(vote);
        log.info("{} cancel vote for {}", user, restaurant);
    }
}