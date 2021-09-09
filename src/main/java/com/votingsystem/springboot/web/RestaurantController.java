package com.votingsystem.springboot.web;

import com.votingsystem.springboot.AuthUser;
import com.votingsystem.springboot.model.Restaurant;
import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.model.Vote;
import com.votingsystem.springboot.model.VotingHistory;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.repository.VoteRepository;
import com.votingsystem.springboot.repository.VotingHistoryRepository;
import com.votingsystem.springboot.util.ValidationUtil;
import com.votingsystem.springboot.util.exception.VoteException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.votingsystem.springboot.model.TransactionType.CANCEL_VOTE;
import static com.votingsystem.springboot.model.TransactionType.VOTE;
import static com.votingsystem.springboot.util.DateTimeUtil.isTimeToCancelVote;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(RestaurantController.URL)
public class RestaurantController {
    public static final String URL = "/api/restaurants";

    private final RestaurantRepository repository;

    private final VotingHistoryRepository votingHistoryRepo;

    private final VoteRepository voteRepo;

    @GetMapping("/{restaurantId}")
    public Restaurant get(@PathVariable(name = "restaurantId") int id) {
        Restaurant restaurant = repository.findById(id).orElseThrow();
        log.info("get {}", restaurant);
        return restaurant;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        List<Restaurant> restaurantList = repository.findAll();
        log.info("get all");
        return restaurantList;
    }

    @GetMapping("/{restaurantId}/with-menu")
    public Restaurant getWithMenu(@PathVariable(name = "restaurantId") int id) {
        Restaurant restaurant = repository.getMenu(id).orElseThrow();
        log.info("get {} with menu", restaurant);
        return restaurant;
    }

    @DeleteMapping("/{restaurantId}")
    public void delete(@PathVariable(name = "restaurantId") int id) {
        ValidationUtil.checkNotFoundWithId(repository.deleteById(id) != 0, id);
        log.info("delete restaurant id {}", id);
    }

    @PutMapping("/{restaurantId}")
    @Transactional
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable(name = "restaurantId") int id) {
        ValidationUtil.assureIdConsistent(restaurant, id);
        repository.save(restaurant);
        log.info("update {}", restaurant);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        ValidationUtil.checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{restaurantId}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping("/{restaurantId}")
    @Transactional
    public void doVote(@PathVariable(name = "restaurantId") int id, @AuthenticationPrincipal AuthUser authUser) {
        User user = authUser.getUser();
        Restaurant restaurant = repository.getById(id);
        voteRepo.save(new Vote(user.id(), restaurant));
        votingHistoryRepo.save(new VotingHistory(VOTE, user, restaurant));
        log.info("{} voted for {} ", user, restaurant);
    }

    @PatchMapping("/cancel-vote")
    @Transactional
    public void cancelVote(@AuthenticationPrincipal AuthUser authUser) {
        if (!isTimeToCancelVote()) {
            throw new VoteException("Access to cancel vote after 11h is denied");
        }
        User user = authUser.getUser();
        Vote vote = voteRepo.get(user.id(), LocalDate.now()).orElseThrow();
        Restaurant restaurant = vote.getRestaurant();
        voteRepo.deleteById(vote.id());
        votingHistoryRepo.save(new VotingHistory(CANCEL_VOTE, user, restaurant));
        log.info("{} cancel vote for {}", user, restaurant);
    }
}