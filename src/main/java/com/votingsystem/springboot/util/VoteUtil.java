package com.votingsystem.springboot.util;

import com.votingsystem.springboot.model.Vote;
import com.votingsystem.springboot.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class VoteUtil {
    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.id(), vote.getVotingDate(), vote.getRestaurant().getName());
    }

    public static List<VoteTo> getTos(List<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::createTo)
                .toList();
    }
}