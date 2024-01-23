package com.cajeroautomatico.cuenta.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionATMRequest {

    @Schema(title = "numero de cuenta del clienmte",
            description = "Acepta solo numeros",
            example = "1000")
    private Long nroCuenta;
    @Schema(title = "El tipo de transaccion",
            description = "Acepta solo caracteres",
            example = "RETIRAR o DEPOSITAR")
    private String tipoTransaccion;
    @Schema(title = "Moneda de la transaccion",
            description = "Acepta solo caracteres",
            example = "USD o BOB")
    private String monedaTransaccion;
    @Schema(title = "Monto de la transaccion",
            description = "Acepta solo numeros",
            example = "1000")
    private Double monto;


}
