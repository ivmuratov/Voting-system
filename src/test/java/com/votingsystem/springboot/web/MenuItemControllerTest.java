package com.votingsystem.springboot.web;

import com.votingsystem.springboot.model.MenuItem;
import com.votingsystem.springboot.repository.MenuItemRepository;
import com.votingsystem.springboot.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.votingsystem.springboot.MenuItemTestData.*;
import static com.votingsystem.springboot.RestaurantTestData.RESTAURANT_ID1;
import static com.votingsystem.springboot.UserTestData.ADMIN_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuItemControllerTest extends AbstractControllerTest {
    static final String URL = RestaurantController.URL + '/' + RESTAURANT_ID1 + "/menu-items/";

    @Autowired
    private MenuItemRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + MENU_ITEM_ID1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(menuItem1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(menuItem1, menuItem2, menuItem7));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + MENU_ITEM_ID1))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertFalse(repository.findById(MENU_ITEM_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        perform(MockMvcRequestBuilders.put(URL + RESTAURANT_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MATCHER.assertMatch(updated, repository.findById(MENU_ITEM_ID1).orElseThrow());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        MenuItem newMenuItem = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)))
                .andDo(print())
                .andExpect(status().isCreated());
        MenuItem created = MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MATCHER.assertMatch(created, newMenuItem);
        MATCHER.assertMatch(repository.getById(newId), newMenuItem);
    }
}