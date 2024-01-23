package com.cajeroautomatico.cuenta.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionATMResponse {

    private Long nroTransaccion;
    private Long nroCuenta;
    private String tipoTransaccion;
    private String monedaTransaccion;
    private Double monto;
    private Double tipoCambio;
    private String monedaCuenta;
    private Double montoFinal;
    private String estado;
    private String fecha;

}
