package com.votingsystem.springboot.web;

import com.votingsystem.springboot.AuthUser;
import com.votingsystem.springboot.model.Role;
import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.repository.UserRepository;
import com.votingsystem.springboot.util.ValidationUtil;
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
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(AccountController.URL)
public class AccountController {
    public static final String URL = "/api/account";

    private final UserRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser);
        repository.deleteById(authUser.id());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("register {}", user);
        ValidationUtil.checkNew(user);
        user.setRoles(Set.of(Role.USER));
        user = repository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/account")
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} to {}", authUser, user);
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(user, oldUser.id());
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
        repository.save(user);
    }
}