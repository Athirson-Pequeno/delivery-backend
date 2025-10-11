package com.tizo.delivery.docs;

import com.tizo.delivery.model.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, TYPE })
@Retention(RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Não autorizado",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acesso negado",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
})
public @interface CommonApiResponses {
}
