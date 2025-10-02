package com.tizo.delivery.util;

import com.tizo.delivery.model.dto.order.OrderItemRequestDto;
import com.tizo.delivery.model.dto.order.ProductOrderExtrasGroupsDto;
import com.tizo.delivery.model.dto.order.ProductOrdersExtrasDto;
import com.tizo.delivery.model.order.Order;
import com.tizo.delivery.model.order.OrderItem;
import com.tizo.delivery.model.order.OrderItemExtra;
import com.tizo.delivery.model.order.OrderItemExtraGroup;
import com.tizo.delivery.model.product.Product;
import com.tizo.delivery.model.product.ProductExtras;
import com.tizo.delivery.model.product.ProductExtrasGroup;

import java.util.HashSet;
import java.util.Set;

public class OrderItemFactory {
    public static OrderItem create(Product product, OrderItemRequestDto itemDto, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(itemDto.productId());
        orderItem.setQuantity(itemDto.quantity());
        orderItem.setSize(itemDto.productSize().getSize());
        orderItem.setSizeDescription(itemDto.productSize().getSizeName());
        orderItem.setProductName(product.getName());
        orderItem.setOrder(order);

        // calcula preço
        orderItem.setUnitPrice(product.getProductSize().stream().filter(size -> size.getId().equals(itemDto.productSize().getId())).findFirst().orElseThrow(() -> new RuntimeException("Size not found")).getPrice());

        // adiciona extrasGroup
        Set<OrderItemExtraGroup> extrasGroups = new HashSet<>();

        if (itemDto.extraGroup() != null) {
            for (ProductOrderExtrasGroupsDto groupDto : itemDto.extraGroup()) {
                ProductExtrasGroup productGroup = product.getExtrasGroups().stream()
                        .filter(g -> g.getId().equals(groupDto.extraGroupId()))
                        .findFirst()
                        .orElse(null);

                if (productGroup == null) continue; // grupo não existe no produto

                Set<OrderItemExtra> extras = new HashSet<>();
                for (ProductOrdersExtrasDto extraDto : groupDto.extras()) {
                    ProductExtras productExtra = product.getExtras().stream()
                            .filter(e -> e.getId().equals(extraDto.extraId()))
                            .findFirst()
                            .orElse(null);

                    if (productExtra == null) continue; // extra não existe

                    OrderItemExtra orderExtra = new OrderItemExtra(
                            productExtra.getName(),
                            productExtra.getValue(),
                            productExtra.getLimit(),
                            extraDto.extraQuantity()
                    );
                    extras.add(orderExtra);
                }

                OrderItemExtraGroup orderGroup = new OrderItemExtraGroup(
                        productGroup.getName(),
                        orderItem,
                        extras
                );

                for (OrderItemExtra extra : extras) {
                    extra.setOrderItemExtraGroup(orderGroup);
                }

                extrasGroups.add(orderGroup);
            }
        }

        orderItem.setExtrasGroup(extrasGroups);


        return orderItem;
    }
}
