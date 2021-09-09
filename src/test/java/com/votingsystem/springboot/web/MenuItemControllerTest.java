package com.votingsystem.springboot.web;

import com.votingsystem.springboot.MenuItemTestData;
import com.votingsystem.springboot.UserTestData;
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

import java.util.List;

import static com.votingsystem.springboot.RestaurantTestData.RESTAURANT_ID1;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuItemControllerTest extends AbstractControllerTest {
    static final String URL = RestaurantController.URL + '/' + RESTAURANT_ID1 + "/menu/";

    @Autowired
    private MenuItemRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + MenuItemTestData.MENU_ITEM_ID1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuItemTestData.MATCHER.contentJson(MenuItemTestData.menuItem1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuItemTestData.MATCHER.contentJson(List.of(MenuItemTestData.menuItem1)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + MenuItemTestData.MENU_ITEM_ID1))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertFalse(repository.findById(MenuItemTestData.MENU_ITEM_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = MenuItemTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(URL + RESTAURANT_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());
        MenuItemTestData.MATCHER.assertMatch(updated, repository.findById(MenuItemTestData.MENU_ITEM_ID1).orElseThrow());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void create() throws Exception {
        MenuItem newMenuItem = MenuItemTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)))
                .andDo(print())
                .andExpect(status().isCreated());
        MenuItem created = MenuItemTestData.MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MenuItemTestData.MATCHER.assertMatch(created, newMenuItem);
        MenuItemTestData.MATCHER.assertMatch(repository.getById(newId), newMenuItem);
    }
}