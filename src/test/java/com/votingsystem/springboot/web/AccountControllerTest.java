package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.repository.UserRepository;
import com.votingsystem.springboot.to.UserTo;
import com.votingsystem.springboot.util.JsonUtil;
import com.votingsystem.springboot.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.votingsystem.springboot.UserTestData.*;
import static com.votingsystem.springboot.UserTestData.USER_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends AbstractControllerTest {
    static final String URL = AccountController.URL;

    @Autowired
    private UserRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER.contentJson(admin));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL))
                .andDo(print())
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(repository.findAll(), admin, votedUser);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = UserUtil.asTo(getNew());
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());
        User registered = MATCHER.readFromJson(action);
        int newId = registered.id();
        newUser.setId(newId);
        MATCHER.assertMatch(registered, newUser);
        MATCHER.assertMatch(repository.findById(newId).orElseThrow(), newUser);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        UserTo updatedTo = UserUtil.asTo(getUpdated());
        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(repository.findById(USER_ID).orElseThrow(), UserUtil.updateFromTo(new User(user), updatedTo));
    }
}