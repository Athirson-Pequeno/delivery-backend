package com.tizo.delivery.controller;

import com.tizo.delivery.docs.ExampleJsons;
import com.tizo.delivery.model.dto.PageResponseDto;
import com.tizo.delivery.model.dto.order.OrderRequestDto;
import com.tizo.delivery.model.dto.order.OrderResponseDto;
import com.tizo.delivery.service.OrderService;
import com.tizo.delivery.util.OrderModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;
    private final OrderModelAssembler assembler = new OrderModelAssembler();

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Criar pedido", description = "Cria um novo pedido para a loja informada")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(value = ExampleJsons.ORDER))
            )
    })
    @PostMapping("/{storeID}/{orderID}")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto orderRequestDto,
            @PathVariable String storeID,
            @PathVariable String orderID) {
        OrderResponseDto order = orderService.createOrder(storeID, orderID, orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar pedidos por loja", description = "Retorna todos os pedidos de uma loja paginados")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = ExampleJsons.ORDERS_PAGED))
            )
    })
    @GetMapping("/{storeID}")
    public ResponseEntity<PagedModel<EntityModel<OrderResponseDto>>> getOrdersByStoreId(
            @PathVariable String storeID,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        PageResponseDto<OrderResponseDto> orders = orderService.getOrdersByStoreId(storeID, page, size);
        PagedModel<EntityModel<OrderResponseDto>> model = assembler.toPagedModel(orders, storeID, page, size);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico de uma loja pelo ID do pedido")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = ExampleJsons.ORDER))
            ),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{storeId}/{orderId}")
    public ResponseEntity<EntityModel<OrderResponseDto>> getOrderById(
            @PathVariable String storeId,
            @PathVariable String orderId) {

        OrderResponseDto orderDto = orderService.getOrderById(storeId, orderId);
        if (orderDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EntityModel<OrderResponseDto> model = assembler.toModel(orderDto, storeId);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @Operation(summary = "Pedidos do dia", description = "Retorna os pedidos da loja filtrados pelo dia atual")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedidos do dia",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = ExampleJsons.ORDERS_TODAY))
            )
    })
    @GetMapping("/{storeId}/today")
    public ResponseEntity<PageResponseDto<OrderResponseDto>> getOrdersByDate(
            @PathVariable String storeId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "50") @Max(50) Integer size) {

        Page<OrderResponseDto> orders = orderService.findByStoreAndDate(storeId, size, page);
        PageResponseDto<OrderResponseDto> pageResponseDto = new PageResponseDto<>(orders);
        return ResponseEntity.ok(pageResponseDto);
    }

}
