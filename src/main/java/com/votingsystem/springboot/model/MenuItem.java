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
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "menu_unique_name_idx")})
public class MenuItem extends BaseEntity {
    @Schema(description = "Date of registered", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private LocalDateTime registered = LocalDateTime.now();

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
    @JsonBackReference(value = "menu")
    @ToString.Exclude
    private Restaurant restaurant;

    public MenuItem(Integer id, String name, Long price) {
        super(id);
        this.name = name;
        this.price = price;
    }
}