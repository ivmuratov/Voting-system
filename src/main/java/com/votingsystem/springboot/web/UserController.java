package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.repository.UserRepository;
import com.votingsystem.springboot.to.UserTo;
import com.votingsystem.springboot.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.votingsystem.springboot.config.WebSecurityConfig.PASSWORD_ENCODER;
import static com.votingsystem.springboot.util.UserUtil.*;

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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        List<User> userList = repository.findAll();
        log.info("get  all");
        return userList;
    }

    @Operation(
            summary = "Get user",
            description = "Allows you to get the selected user by id"
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable int id) {
        User user = repository.findById(id).orElseThrow();
        log.info("get {}", user);
        return user;
    }

    @Operation(
            summary = "Get user by email",
            description = "Allows you to get the selected user by email"
    )
    @GetMapping(value = "/by-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getByEmail(String email) {
        User user = repository.findByEmailIgnoreCase(email).orElseThrow();
        log.info("get by email {}", email);
        return user;
    }

    @Operation(
            summary = "Delete user",
            description = "Allows you to delete the selected user by id"
    )
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        ValidationUtil.checkNotFoundWithId(repository.deleteById(id) != 0, id);
        log.info("delete user id={}", id);
    }

    @Operation(
            summary = "Update user",
            description = "Allows you to update the selected user by id"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, @PathVariable int id) {
        User user = get(id);
        ValidationUtil.assureIdConsistent(userTo, user.id());
        User updateUser = prepareToAndSave(updateFromTo(user, userTo), repository, PASSWORD_ENCODER);
        log.info("update {} to {}", user, updateUser);
    }

    @Operation(
            summary = "Create user",
            description = "Allows you to create a user"
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> create(@Valid @RequestBody UserTo userTo) {
        ValidationUtil.checkNew(userTo);
        User created = prepareToAndSave(createNewFromTo(userTo), repository, PASSWORD_ENCODER);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}