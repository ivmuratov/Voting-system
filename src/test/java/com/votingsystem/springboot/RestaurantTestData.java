package com.votingsystem.springboot;

import com.votingsystem.springboot.model.Restaurant;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menu", "votes");

    public static final int RESTAURANT_ID1 = 1;
    public static final int RESTAURANT_ID2 = 2;
    public static final int RESTAURANT_ID3 = 3;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_ID1, "Restaurant 1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_ID2, "Restaurant 2");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_ID3, "Restaurant 3");

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_ID1, "Update Restaurant 1");
    }
}