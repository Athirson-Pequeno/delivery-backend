package com.tizo.delivery.service;

import com.tizo.delivery.model.delivery.Delivery;
import com.tizo.delivery.model.dto.delivery.NewDeliveryDTO;
import com.tizo.delivery.model.store.Store;
import com.tizo.delivery.repository.DeliveryRepository;
import com.tizo.delivery.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final StoreRepository storeRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, StoreRepository storeRepository) {
        this.deliveryRepository = deliveryRepository;
        this.storeRepository = storeRepository;
    }

    public Set<Delivery> getAllDeliveriesByStore(String storeID) {
        return this.deliveryRepository.findByStoreId(storeID).orElseThrow(() -> new EntityNotFoundException("Lista de bairros não encontrado"));
    }

    public void saveNeighborhood(String storeId, List<NewDeliveryDTO> neighborhoods) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada, id: " + storeId));

        Set<Delivery> deliveries = new HashSet<>();

        for (NewDeliveryDTO neighborhood : neighborhoods) {
            deliveries.add(new Delivery(neighborhood.neighborhood(), neighborhood.tax(), store));
        }

        deliveryRepository.saveAll(deliveries);
    }
}
