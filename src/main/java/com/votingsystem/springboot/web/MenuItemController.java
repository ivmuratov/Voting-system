package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.MenuItem;
import com.votingsystem.springboot.repository.MenuItemRepository;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@Tag(name = "Menu Item Controller", description = "Management menu")
public class MenuItemController {
    public static final String URL = RestaurantController.URL + "/{restaurantId}/menu";

    private MenuItemRepository repository;

    private RestaurantRepository restaurantRepo;

    @Operation(
            summary = "Get restaurant menu",
            description = "Allows you to get menu the selected restaurant by restaurant id"
    )
    @GetMapping
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public List<MenuItem> getMenu(@PathVariable int restaurantId) {
        List<MenuItem> menu = repository.getAll(restaurantId).orElseThrow();
        log.info("get all by restaurant id={}", restaurantId);
        return menu;
    }

    @Operation(
            summary = "Get restaurant menu item",
            description = "Allows you to get menu item the selected restaurant by restaurant id and menu item id"
    )
    @GetMapping("/{menuItemId}")
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public MenuItem get(@PathVariable int restaurantId, @PathVariable int menuItemId) {
        MenuItem menuItem = repository.get(restaurantId, menuItemId).orElseThrow();
        log.info("get {} by restaurant id={}", menuItem, restaurantId);
        return menuItem;
    }

    @Operation(
            summary = "Delete restaurant menu item",
            description = "Allows you to delete menu item the selected restaurant by restaurant id and menu item id"
    )
    @DeleteMapping("/{menuItemId}")
    @Secured(value = {"ROLE_ADMIN"})
    public void delete(@PathVariable int restaurantId, @PathVariable int menuItemId) {
        ValidationUtil.checkNotFound(restaurantRepo, restaurantId);
        ValidationUtil.checkNotFoundWithId(repository.delete(restaurantId, menuItemId) != 0, menuItemId);
        log.info("delete menu id={} by restaurant id={}", menuItemId, restaurantId);
    }

    @Operation(
            summary = "Update restaurant menu item",
            description = "Allows you to update menu item the selected restaurant by restaurant id and menu item id"
    )
    @PutMapping("/{menuItemId}")
    @Transactional
    @Secured(value = {"ROLE_ADMIN"})
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId, @PathVariable int menuItemId) {
        ValidationUtil.checkNotFound(restaurantRepo, restaurantId);
        ValidationUtil.assureIdConsistent(menuItem, menuItemId);
        menuItem.setRestaurant(restaurantRepo.getById(restaurantId));
        repository.save(menuItem);
        log.info("update {} by restaurant id={}", menuItem, restaurantId);
    }

    @Operation(
            summary = "Create restaurant menu item",
            description = "Allows you to create menu item the selected restaurant by restaurant id"
    )
    @PostMapping
    @Transactional
    @Secured(value = {"ROLE_ADMIN"})
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