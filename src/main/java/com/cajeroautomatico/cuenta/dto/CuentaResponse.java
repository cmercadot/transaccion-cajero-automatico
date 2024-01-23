package com.cajeroautomatico.cuenta.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class CuentaResponse {

    private  Long nroCuenta;
    private  Long codCliente;
    private  String nombreCuenta;
    private  Double saldoCuenta;
    private  String monedaCuenta;
    private  String estado;
}
