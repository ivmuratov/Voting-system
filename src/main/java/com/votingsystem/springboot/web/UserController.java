package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.repository.UserRepository;
import com.votingsystem.springboot.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(UserController.URL)
@Tag(name = "User Controller", description = "Management users")
public class UserController {
    public static final String URL = "/api/admin/users";

    private final UserRepository repository;

    @Operation(
            summary = "Get all users",
            description = "Allows you to get all users"
    )
    @GetMapping
    public List<User> getAll() {
        List<User> userList = repository.findAll();
        log.info("get  all");
        return userList;
    }

    @Operation(
            summary = "Get user",
            description = "Allows you to get the selected user by id"
    )
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        User user = repository.findById(id).orElseThrow();
        log.info("get {}", user);
        return user;
    }

    @Operation(
            summary = "Get user by email",
            description = "Allows you to get the selected user by email"
    )
    @GetMapping("/by-email")
    public User getByEmail(String email) {
        User user = repository.findByEmailIgnoreCase(email).orElseThrow();
        log.info("get by email {}", email);
        return user;
    }

    @Operation(
            summary = "Delete restaurant",
            description = "Allows you to delete the selected user by id"
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        ValidationUtil.checkNotFoundWithId(repository.deleteById(id) != 0, id);
        log.info("delete user id={}", id);
    }

    @Operation(
            summary = "Update restaurant",
            description = "Allows you to update the selected user by id"
    )
    @PutMapping("/{id}")
    @Transactional
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        ValidationUtil.assureIdConsistent(user, id);
        repository.save(user);
        log.info("update {}", user);
    }

    @Operation(
            summary = "Create user",
            description = "Allows you to create a user"
    )
    @PostMapping
    @Transactional
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        ValidationUtil.checkNew(user);
        User created = repository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}