package com.votingsystem.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"created", "name", "restaurant_id"}, name = "unique_menu_item_for_restaurant_per_day_idx")})
public class MenuItem extends BaseEntity {
    @Schema(description = "Date of created", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "created", nullable = false, columnDefinition = "date default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private LocalDate created = LocalDate.now();

    @Schema(description = "Name menu item", example = "New menu item")
    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(max = 120)
    private String name;

    @Schema(description = "Menu item price in cents", example = "15000")
    @Column(name = "price", nullable = false)
    @NotNull
    private Long price;

    @Schema(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    @ToString.Exclude
    private Restaurant restaurant;

    public MenuItem(Integer id, String name, Long price) {
        super(id);
        this.name = name;
        this.price = price;
    }
}