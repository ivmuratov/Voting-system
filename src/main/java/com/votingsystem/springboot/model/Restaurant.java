package com.votingsystem.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "restaurant")
public class Restaurant extends BaseEntity {
    @Schema(description = "Date of registered", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private LocalDateTime registered = LocalDateTime.now();

    @Schema(description = "Name restaurant", example = "New Restaurant")
    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    @Size(max = 120)
    private String name;

    @Schema(description = "Votes for the restaurant", accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "votes-restaurant")
    @ToString.Exclude
    private List<Vote> votes;

    @Schema(description = "Menu for the restaurant", accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference(value = "menu")
    @ToString.Exclude
    private List<MenuItem> menu;

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }
}