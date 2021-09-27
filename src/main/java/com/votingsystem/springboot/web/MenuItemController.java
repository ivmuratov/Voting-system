package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.MenuItem;
import com.votingsystem.springboot.repository.MenuItemRepository;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.util.MenuItemUtil;
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

import static com.votingsystem.springboot.util.ValidationUtil.checkNotFoundWithId;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(MenuItemController.URL)
@Tag(name = "Menu Item Controller", description = "Management menu")
public class MenuItemController {
    public static final String URL = RestaurantController.URL + "/{restaurantId}/menu-items";

    private MenuItemRepository repository;

    private RestaurantRepository restaurantRepo;

    @Operation(
            summary = "Get restaurant menu",
            description = "Allows you to get menu the selected restaurant by restaurant id"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(value = {"ROLE_ADMIN"})
    public List<MenuItem> getAll(@PathVariable int restaurantId) {
        List<MenuItem> menu = repository.getAll(restaurantId);
        log.info("get all by restaurant id={}", restaurantId);
        return menu;
    }

    @Operation(
            summary = "Get restaurant menu for today",
            description = "Allows you to get menu for today the selected restaurant by restaurant id"
    )
    @GetMapping(value = "/for-today", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    public List<MenuItem> getAllForToday(@PathVariable int restaurantId) {
        List<MenuItem> menu = repository.getAllForToday(restaurantId);
        log.info("get all for today by restaurant id={}", restaurantId);
        return menu;
    }

    @Operation(
            summary = "Get restaurant menu item",
            description = "Allows you to get menu item the selected restaurant by restaurant id and menu item id"
    )
    @GetMapping(value = "/{menuItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(value = {"ROLE_ADMIN"})
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
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Secured(value = {"ROLE_ADMIN"})
    public void delete(@PathVariable int restaurantId, @PathVariable int menuItemId) {
        checkNotFoundWithId(repository.delete(restaurantId, menuItemId) != 0, menuItemId);
        log.info("delete menu id={} by restaurant id={}", menuItemId, restaurantId);
    }

    @Operation(
            summary = "Update restaurant menu item",
            description = "Allows you to update menu item the selected restaurant by restaurant id and menu item id"
    )
    @PutMapping(value = "/{menuItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Secured(value = {"ROLE_ADMIN"})
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId, @PathVariable int menuItemId) {
        ValidationUtil.assureIdConsistent(menuItem, menuItemId);
        MenuItemUtil.checkOwn(menuItem, repository, restaurantId, menuItemId);
        menuItem.setCreated(repository.get(restaurantId, menuItemId).orElseThrow().getCreated());
        menuItem.setRestaurant(restaurantRepo.getById(restaurantId));
        checkNotFoundWithId(repository.save(menuItem), menuItem.id());
        log.info("update {} by restaurant id={}", menuItem, restaurantId);
    }

    @Operation(
            summary = "Create restaurant menu item",
            description = "Allows you to create menu item the selected restaurant by restaurant id"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Secured(value = {"ROLE_ADMIN"})
    public ResponseEntity<MenuItem> create(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId) {
        ValidationUtil.checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepo.getById(restaurantId));
        MenuItem created = repository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{menuItemId}")
                .buildAndExpand(created.getRestaurant().getId(), created.getId()).toUri();
        log.info("create {} for restaurant id={}", created, restaurantId);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}