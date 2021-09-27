package com.votingsystem.springboot.web;

import com.votingsystem.springboot.AuthUser;
import com.votingsystem.springboot.model.Restaurant;
import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.model.Vote;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.repository.VoteRepository;
import com.votingsystem.springboot.to.VoteTo;
import com.votingsystem.springboot.util.VoteUtil;
import com.votingsystem.springboot.util.exception.VoteException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.votingsystem.springboot.util.DateTimeUtil.isDeadlineForVoteChangeHasCome;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(VoteController.URL)
@Tag(name = "Vote Controller", description = "Voting")
public class VoteController {
    public static final String URL = "/api/votes";

    private final VoteRepository repository;

    private final RestaurantRepository restaurantRepo;

    @Operation(
            summary = "Vote for restaurant",
            description = "Allows you to vote the selected restaurant by name"
    )
    @PostMapping
    @Transactional
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Vote> create(@RequestParam @Parameter(example = "restaurant 1") String nameOfRestaurant, @AuthenticationPrincipal AuthUser authUser) {
        Restaurant restaurant = restaurantRepo.getByName(nameOfRestaurant).orElseThrow();
        User user = authUser.getUser();
        Vote created = repository.save(new Vote(user, restaurant));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("{} voted for {} ", user, restaurant);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Change vote",
            description = "Allows you to change a vote the selected restaurant by name"
    )
    @PutMapping
    @Transactional
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void change(@RequestParam @Parameter(example = "restaurant 2") String nameOfRestaurant, @AuthenticationPrincipal AuthUser authUser) {
        if (isDeadlineForVoteChangeHasCome()) {
            throw new VoteException("Access to cancel vote after 11h is denied");
        }
        Restaurant restaurant = restaurantRepo.getByName(nameOfRestaurant).orElseThrow();
        User user = authUser.getUser();
        Vote oldVote = repository.getTodayVote(user.id()).orElseThrow();
        oldVote.setRestaurant(restaurant);
        repository.save(oldVote);
        Restaurant newRestaurant = oldVote.getRestaurant();
        log.info("{} change vote to {}", user, newRestaurant);
    }

    @Operation(
            summary = "Get voting history",
            description = "Allows you to get a voting history"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VoteTo> getHistory(@AuthenticationPrincipal AuthUser authUser) {
        User user = authUser.getUser();
        List<VoteTo> votingHistory = VoteUtil.getTos(repository.getHistory(user.id()));
        log.info("{} get voting history", user);
        return votingHistory;
    }
}