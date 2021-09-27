package com.votingsystem.springboot.repository;

import com.votingsystem.springboot.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1 AND m.created=CURRENT_DATE")
    List<MenuItem> getAllForToday(int restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1")
    List<MenuItem> getAll(int restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=?1 AND m.id=?2")
    Optional<MenuItem> get(int restaurantId, int menuId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem m WHERE m.restaurant.id=?1 AND m.id=?2")
    int delete(int restaurantId, int menuId);
}