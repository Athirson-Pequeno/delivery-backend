package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.auth.AuthCredentialsDto;
import com.tizo.delivery.model.dto.auth.AuthResponseDto;
import com.tizo.delivery.model.dto.auth.RefreshRequestDto;
import com.tizo.delivery.model.dto.store.RegisterStoreDto;
import com.tizo.delivery.model.dto.store.ResponseStoreDto;
import com.tizo.delivery.model.exception.ErrorResponse;
import com.tizo.delivery.service.AuthService;
import com.tizo.delivery.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints para login, registro e gerenciamento de usuários")
public class AuthController {

    private final AuthService authService;

    private final StoreService storeService;

    public AuthController(AuthService authService, StoreService storeService) {
        this.authService = authService;
        this.storeService = storeService;
    }

    @Operation(summary = "Cadastrar nova loja", description = "Cria uma nova loja no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loja criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseStoreDto.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "    \"id\": \"5eea5710-b410-4192-85e0-d6ceb863a1eb\",\n" +
                                            "    \"name\": \"Bar da carne\",\n" +
                                            "    \"slug\": \"bar-da-carne\",\n" +
                                            "    \"phoneNumber\": \"5511999998888\",\n" +
                                            "    \"address\": {\n" +
                                            "        \"street\": \"Rua1\",\n" +
                                            "        \"number\": \"01\",\n" +
                                            "        \"neighborhood\": \"Bairro Y\",\n" +
                                            "        \"city\": \"Cidade X\",\n" +
                                            "        \"cep\": \"54321-000\"\n" +
                                            "    }\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "O email enviado já está cadastrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/store/signing")
    public ResponseEntity<ResponseStoreDto> signingStore(@RequestBody RegisterStoreDto registerStoreDto) {
        ResponseStoreDto store = storeService.createStore(registerStoreDto);
        return new ResponseEntity<>(store, HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar gerente", description = "Registra um gerente para a loja informada (role MANAGER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gerente registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"accessToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\",\n" +
                                            "  \"refreshToken\": \"dGhpcy1pcy1hLXJlZnJlc2gtdG9rZW4=\",\n" +
                                            "  \"storeId\": \"123\",\n" +
                                            "  \"storeSlug\": \"loja-exemplo\"\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito (email já existe)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/manager/{storeId}")
    public ResponseEntity<AuthResponseDto> registerManager(
            @PathVariable String storeId,
            @Valid @RequestBody AuthCredentialsDto request) {

        AuthResponseDto response = authService.registerManager(request, storeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar funcionário", description = "Registra um funcionário para a loja informada (role EMPLOYEE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito (email já existe)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/employee/{storeId}")
    public ResponseEntity<AuthResponseDto> registerEmployee(
            @PathVariable String storeId,
            @Valid @RequestBody AuthCredentialsDto request) {

        AuthResponseDto response = authService.registerEmployee(request, storeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login", description = "Autentica usuário e retorna tokens de acesso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthCredentialsDto request) {
        AuthResponseDto response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Atualizar token", description = "Gera um novo token a partir do refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
