package com.votingsystem.springboot.web;

import com.votingsystem.springboot.util.DateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.votingsystem.springboot.RestaurantTestData.restaurant1;
import static com.votingsystem.springboot.RestaurantTestData.restaurant3;
import static com.votingsystem.springboot.UserTestData.USER_MAIL;
import static com.votingsystem.springboot.UserTestData.VOTED_USER_MAIL;
import static com.votingsystem.springboot.VoteTestData.MATCHER;
import static com.votingsystem.springboot.VoteTestData.voteToOfVotedUser;
import static com.votingsystem.springboot.util.DateTimeUtil.DEADLINE_FOR_VOTE_CHANGE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {
    static final String URL = VoteController.URL;

    ResultActions create() throws Exception {
        return perform(MockMvcRequestBuilders.post(URL + "?nameOfRestaurant=" + restaurant1.getName()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    ResultActions change() throws Exception {
        return perform(MockMvcRequestBuilders.put(URL + "?nameOfRestaurant=" + restaurant3.getName()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void allowedCreate() throws Exception {
        perform(MockMvcRequestBuilders.post(URL + "?nameOfRestaurant=" + restaurant1.getName()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = VOTED_USER_MAIL)
    void forbiddenCreate() throws Exception {
        Throwable thrown = assertThrows(NestedServletException.class, this::create);
        assertNotNull(thrown.getMessage());
    }

    @Test
    @WithUserDetails(value = VOTED_USER_MAIL)
    void forbiddenChange() throws Exception {
        DateTimeUtil.useFixedClockAt(LocalDateTime.of(LocalDate.now(), DEADLINE_FOR_VOTE_CHANGE).plusSeconds(1));
        Throwable thrown = assertThrows(NestedServletException.class, this::change);
        assertNotNull(thrown.getMessage());
        DateTimeUtil.useSystemDefaultZoneClock();
    }

    @Test
    @WithUserDetails(value = VOTED_USER_MAIL)
    void allowedChange() throws Exception {
        DateTimeUtil.useFixedClockAt(LocalDateTime.of(LocalDate.now(), DEADLINE_FOR_VOTE_CHANGE).minusSeconds(5));
        change();
        DateTimeUtil.useSystemDefaultZoneClock();
    }

    @Test
    @WithUserDetails(value = VOTED_USER_MAIL)
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MATCHER.contentJson(List.of(voteToOfVotedUser)));
    }
}