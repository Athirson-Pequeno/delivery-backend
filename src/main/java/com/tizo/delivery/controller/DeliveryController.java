package com.tizo.delivery.controller;

import com.tizo.delivery.model.delivery.Delivery;
import com.tizo.delivery.model.dto.delivery.NewDeliveryDTO;
import com.tizo.delivery.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/store/configs")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<Delivery>> getAllDeliveries(@PathVariable String id) {
        Set<Delivery> deliveries = deliveryService.getAllDeliveriesByStore(id);
        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addDeliveries(@PathVariable String id, @RequestBody List<NewDeliveryDTO> neighborhoods) {
        try {
            deliveryService.saveNeighborhood(id, neighborhoods);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
