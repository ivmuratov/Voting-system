package com.votingsystem.springboot;

import com.votingsystem.springboot.to.VoteTo;

import java.time.LocalDate;

import static com.votingsystem.springboot.RestaurantTestData.restaurant1;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static final int VOTE_OF_VOTED_USER_ID = 1;
    public static final VoteTo voteToOfVotedUser = new VoteTo(VOTE_OF_VOTED_USER_ID, LocalDate.now(), restaurant1.getName());
}
