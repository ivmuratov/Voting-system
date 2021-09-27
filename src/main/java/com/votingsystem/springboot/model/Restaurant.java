package com.votingsystem.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
@Table(name = "restaurant")
public class Restaurant extends BaseEntity {
    @Schema(description = "Name restaurant", example = "New Restaurant")
    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    @Size(max = 120)
    private String name;

    @Schema(description = "Menu for the restaurant", accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    @ToString.Exclude
    private List<MenuItem> menu;

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }
}