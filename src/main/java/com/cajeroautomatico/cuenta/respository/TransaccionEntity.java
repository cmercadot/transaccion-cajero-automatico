package com.cajeroautomatico.cuenta.respository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="transaccion_atm")
public class TransaccionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroTransaccion;
    private Long nroCuenta;
    private String tipoTransaccion;
    private String monedaTransaccion;
    private Double monto;
    private Double tipoCambio;
    private String monedaCuenta;
    private Double montoFinal;
    private String estado;
    private Timestamp fecha;
}
