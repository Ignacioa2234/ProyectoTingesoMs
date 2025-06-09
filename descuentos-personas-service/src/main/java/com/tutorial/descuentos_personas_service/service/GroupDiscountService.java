package com.tutorial.descuentos_personas_service.service;

import com.tutorial.descuentos_personas_service.entity.GroupDiscount;
import com.tutorial.descuentos_personas_service.repository.GroupDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupDiscountService {

    private final GroupDiscountRepository repository;

    public int getDiscountForSize(int groupSize) {
        return repository.findBySize(groupSize)
                .map(GroupDiscount::getDiscountValue)
                .orElse(0);
    }
}
