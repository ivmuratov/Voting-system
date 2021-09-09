package com.votingsystem.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "menu_item", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"restaurant_id", "name"}, name = "menu_unique_name_idx"),
        @UniqueConstraint(columnNames = {"restaurant_id", "registered"}, name = "menu_per_day_idx")
})
public class MenuItem extends BaseEntity {
    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private LocalDate registered = LocalDate.now();

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(max = 120)
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10000L, max = 1_000_000L)
    private Long price;

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
