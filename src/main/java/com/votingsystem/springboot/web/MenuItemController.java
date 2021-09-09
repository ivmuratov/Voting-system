package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.MenuItem;
import com.votingsystem.springboot.repository.MenuItemRepository;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(MenuItemController.URL)
public class MenuItemController {
    public static final String URL = RestaurantController.URL + "/{restaurantId}/menu";

    private MenuItemRepository repository;

    private RestaurantRepository restaurantRepo;

    @GetMapping
    public List<MenuItem> getMenu(@PathVariable int restaurantId) {
        List<MenuItem> menu = repository.getAll(restaurantId).orElseThrow();
        log.info("get all by restaurant id={}", restaurantId);
        return menu;
    }

    @GetMapping("/{menuItemId}")
    public MenuItem get(@PathVariable int restaurantId, @PathVariable int menuItemId) {
        MenuItem menuItem = repository.get(restaurantId, menuItemId).orElseThrow();
        log.info("get {} by restaurant id={}", menuItem, restaurantId);
        return menuItem;
    }

    @DeleteMapping("/{menuItemId}")
    public void delete(@PathVariable int restaurantId, @PathVariable int menuItemId) {
        ValidationUtil.checkNotFound(restaurantRepo, restaurantId);
        ValidationUtil.checkNotFoundWithId(repository.delete(restaurantId, menuItemId) != 0, menuItemId);
        log.info("delete menu id={} by restaurant id={}", menuItemId, restaurantId);
    }

    @PutMapping("/{menuItemId}")
    @Transactional
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId, @PathVariable int menuItemId) {
        ValidationUtil.checkNotFound(restaurantRepo, restaurantId);
        ValidationUtil.assureIdConsistent(menuItem, menuItemId);
        menuItem.setRestaurant(restaurantRepo.getById(restaurantId));
        repository.save(menuItem);
        log.info("update {} by restaurant id={}", menuItem, restaurantId);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<MenuItem> create(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId) {
        ValidationUtil.checkNotFound(restaurantRepo, restaurantId);
        ValidationUtil.checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepo.getById(restaurantId));
        MenuItem created = repository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL)
                .buildAndExpand(created.getId()).toUri();
        log.info("create {} for restaurant id={}", created, restaurantId);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}