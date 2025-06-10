package com.tutorial.descuentos_personas_service.controller;

import com.tutorial.descuentos_personas_service.entity.GroupDiscount;
import com.tutorial.descuentos_personas_service.repository.GroupDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class GroupDiscountController {

    private final GroupDiscountRepository repository;

    @GetMapping("/grupo")
    public ResponseEntity<Integer> getGroupDiscount(@RequestParam("size") Integer size) {
        int discount = repository.findBySize(size)
                .map(GroupDiscount::getDiscountValue)
                .orElse(0);
        return ResponseEntity.ok(discount);
    }
}
