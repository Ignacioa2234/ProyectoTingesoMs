package com.tutorial.descuentos_personas_service.repository;

import com.tutorial.descuentos_personas_service.entity.GroupDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupDiscountRepository extends JpaRepository<GroupDiscount, Long> {

    @Query("SELECT g FROM GroupDiscount g " +
            "WHERE g.minSize <= :size " +
            "  AND (g.maxSize IS NULL OR g.maxSize >= :size)")
    Optional<GroupDiscount> findBySize(@Param("size") Integer size);
}
