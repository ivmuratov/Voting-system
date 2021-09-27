package com.votingsystem.springboot.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class VoteTo extends BaseTo {
    private LocalDate votingDate;

    private String nameOfVotedRestaurant;

    public VoteTo(Integer id, LocalDate votingDate, String nameOfVotedRestaurant) {
        this.id = id;
        this.votingDate = votingDate;
        this.nameOfVotedRestaurant = nameOfVotedRestaurant;
    }
}