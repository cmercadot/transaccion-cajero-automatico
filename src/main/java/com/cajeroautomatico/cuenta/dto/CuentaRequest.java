package com.cajeroautomatico.cuenta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuentaRequest {
    private  Long nroCuenta;
    private  Long codCliente;
    private  String nombreCuenta;
    private  Double saldoCuenta;
    private  String monedaCuenta;
    private  String estado;
}
