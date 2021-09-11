package com.votingsystem.springboot.web;

import com.votingsystem.springboot.RestaurantTestData;
import com.votingsystem.springboot.UserTestData;
import com.votingsystem.springboot.model.Restaurant;
import com.votingsystem.springboot.model.Vote;
import com.votingsystem.springboot.repository.RestaurantRepository;
import com.votingsystem.springboot.repository.VoteRepository;
import com.votingsystem.springboot.util.DateTimeUtil;
import com.votingsystem.springboot.util.JsonUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.votingsystem.springboot.RestaurantTestData.*;
import static com.votingsystem.springboot.UserTestData.votedUser;
import static com.votingsystem.springboot.util.DateTimeUtil.BORDER_TIME_FOR_CANCEL_OF_VOTING;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestaurantControllerTest extends AbstractControllerTest {
    static final String URL = RestaurantController.URL + '/';

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private VoteRepository voteRepo;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + RESTAURANT_ID1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(restaurant1, restaurant2, restaurant3));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + RESTAURANT_ID1))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertFalse(repository.findById(RESTAURANT_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void create() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());
        Restaurant created = MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
        MATCHER.assertMatch(repository.getById(newId), newRestaurant);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(URL + RESTAURANT_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertMatch(updated, repository.findById(RESTAURANT_ID1).orElseThrow());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + RESTAURANT_ID1 + "/with-menu"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(WITH_MENU_MATCHER.contentJson(restaurant1));
    }

    @Order(1)
    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void doVote() throws Exception {
        Restaurant voted = getVoted();
        perform(MockMvcRequestBuilders.patch(URL + RestaurantTestData.RESTAURANT_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(voted)))
                .andExpect(status().isOk())
                .andDo(print());
        WITH_VOTES_MATCHER.assertMatch(voted, repository.findById(RESTAURANT_ID1).orElseThrow());
    }

    @Test
    @WithUserDetails(value = UserTestData.VOTED_USER_MAIL)
    void forbiddenCancelVote() throws Exception {
        voteRepo.save(new Vote(votedUser, RestaurantTestData.restaurant3));
        DateTimeUtil.useFixedClockAt(LocalDateTime.of(LocalDate.now(), BORDER_TIME_FOR_CANCEL_OF_VOTING.plusSeconds(1)));
        Throwable thrown = assertThrows(NestedServletException.class, this::cancelVote);
        assertNotNull(thrown.getMessage());
        DateTimeUtil.useSystemDefaultZoneClock();
    }

    @Test
    @WithUserDetails(value = UserTestData.VOTED_USER_MAIL)
    void allowedCancelVote() throws Exception {
        voteRepo.save(new Vote(votedUser, RestaurantTestData.restaurant3));
        DateTimeUtil.useFixedClockAt(LocalDateTime.of(LocalDate.now(), BORDER_TIME_FOR_CANCEL_OF_VOTING.minusSeconds(10)));
        cancelVote();
        DateTimeUtil.useSystemDefaultZoneClock();
    }

    void cancelVote() throws Exception {
        perform(MockMvcRequestBuilders.patch(URL + "cancel-vote"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}