package com.votingsystem.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"voting_date", "user_id"}, name = "unique_user_vote_per_day")})
public class Vote extends BaseEntity {
    @Column(name = "voting_date", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDate votingDate = LocalDate.now();

    @Column(name = "user_id", nullable = false)
    @ToString.Exclude
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference(value = "votes-restaurant")
    @ToString.Exclude
    private Restaurant restaurant;

    public Vote(Integer id, Integer userId, Restaurant restaurant) {
        super(id);
        this.userId = userId;
        this.restaurant = restaurant;
    }

    public Vote(Integer userId, Restaurant restaurant) {
        this.userId = userId;
        this.restaurant = restaurant;
    }
}