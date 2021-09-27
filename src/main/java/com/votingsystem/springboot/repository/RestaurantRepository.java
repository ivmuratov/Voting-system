package com.votingsystem.springboot.repository;

import com.votingsystem.springboot.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=?1")
    int deleteById(int id);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name)=LOWER(?1)")
    Optional<Restaurant> getByName(String name);
}