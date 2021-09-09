package com.votingsystem.springboot;

import com.votingsystem.springboot.model.Restaurant;
import com.votingsystem.springboot.model.Vote;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "registered", "menu", "votes");
    public static final MatcherFactory.Matcher<Restaurant> WITH_MENU_MATCHER = MatcherFactory.usingAssertions(Restaurant.class,
            (a, e) -> assertThat(a).usingRecursiveComparison()
                    .ignoringFields("registered", "menu.restaurant", "votes").isEqualTo(e),
            (a, e) -> {
                throw new UnsupportedOperationException();
            });
    public static final MatcherFactory.Matcher<Restaurant> WITH_VOTES_MATCHER = MatcherFactory.usingAssertions(Restaurant.class,
            (a, e) -> assertThat(a).usingRecursiveComparison()
                    .ignoringFields("registered", "menu", "votes.restaurant").isEqualTo(e),
            (a, e) -> {
                throw new UnsupportedOperationException();
            });

    public static final int RESTAURANT_ID1 = 1;
    public static final int RESTAURANT_ID2 = 2;
    public static final int RESTAURANT_ID3 = 3;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_ID1, "Restaurant 1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_ID2, "Restaurant 2");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_ID3, "Restaurant 3");

    static {
        restaurant1.setMenu(List.of(MenuItemTestData.menuItem1));
        restaurant2.setMenu(List.of(MenuItemTestData.menuItem2));
        restaurant3.setMenu(List.of(MenuItemTestData.menuItem3));
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_ID1, "Update Restaurant 1");
    }

    public static Restaurant getVoted() {
        Restaurant voted = restaurant1;
        voted.setVotes(List.of(new Vote(1, UserTestData.USER_ID, restaurant1)));
        return voted;
    }
}

