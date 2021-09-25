package com.votingsystem.springboot.web;

import com.votingsystem.springboot.AuthUser;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.votingsystem.springboot.config.WebSecurityConfig.PASSWORD_ENCODER;
import static com.votingsystem.springboot.util.UserUtil.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(AccountController.URL)
@Tag(name = "Account Controller", description = "Management account")
public class AccountController {
    public static final String URL = "/api/account";

    private final UserRepository repository;

    @Operation(
            summary = "View you account",
            description = "Allows you to view your account details"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @Operation(
            summary = "Delete you account",
            description = "Allows you to delete your account"
    )
    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        repository.deleteById(authUser.id());
        log.info("delete {}", authUser);
    }

    @Operation(
            summary = "Register new account",
            description = "Allows you to register your new account"
    )
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        ValidationUtil.checkNew(userTo);
        User newUser = prepareToAndSave(createNewFromTo(userTo), repository, PASSWORD_ENCODER);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/account")
                .build().toUri();
        log.info("register {}", newUser);
        return ResponseEntity.created(uriOfNewResource).body(newUser);
    }

    @Operation(
            summary = "Update you account",
            description = "Allows you to update your account"
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(userTo, oldUser.id());
        if (userTo.getPassword() == null) {
            userTo.setPassword(oldUser.getPassword());
        }
        prepareToAndSave(updateFromTo(oldUser, userTo), repository, PASSWORD_ENCODER);
        log.info("update {} to {}", authUser, userTo);
    }
}