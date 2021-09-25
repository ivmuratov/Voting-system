package com.votingsystem.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.votingsystem.springboot.model.Role;
import com.votingsystem.springboot.model.User;
import com.votingsystem.springboot.util.JsonUtil;

import java.util.List;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int VOTED_USER_ID = 3;
    public static final String USER_MAIL = "user@email.com";
    public static final String ADMIN_MAIL = "admin@email.com";
    public static final String VOTED_USER_MAIL = "voted.user@email.com";
    public static final User user = new User(USER_ID, "user", USER_MAIL, "user", List.of(Role.USER));
    public static final User admin = new User(ADMIN_ID, "admin", ADMIN_MAIL, "admin", List.of(Role.ADMIN, Role.USER));
    public static final User votedUser = new User(VOTED_USER_ID, "voted_user", VOTED_USER_MAIL, "voted_user", List.of(Role.USER));

    public static User getNew() {
        return new User(null, "new name", "new@gmail.com", "New_Last", List.of(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "user update", "user_update@email.com", "user update pswrd", List.of(Role.USER));
    }

    public static User getDuplicateEmail() {
        return new User(ADMIN_ID + 1, "new name", "admin@email.com", "new password", List.of(Role.USER));
    }

    public static String jsonWithPassword(User user, String passw) throws JsonProcessingException {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}