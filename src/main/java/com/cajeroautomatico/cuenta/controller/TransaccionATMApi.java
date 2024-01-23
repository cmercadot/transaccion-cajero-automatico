package com.cajeroautomatico.cuenta.controller;

import com.cajeroautomatico.cuenta.dto.MensajeResponse;
import com.cajeroautomatico.cuenta.dto.TransaccionATMRequest;
import com.cajeroautomatico.cuenta.dto.TransaccionATMResponse;
import com.cajeroautomatico.cuenta.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/transaccion-atm-api")
@RestController
public class TransaccionATMApi {

    TransaccionService transaccionService;
    public TransaccionATMApi(TransaccionService transaccionService){
        this.transaccionService=transaccionService;
    }
    @PostMapping
    public ResponseEntity transaccionCajeroAutomatico(@RequestBody TransaccionATMRequest request ){
        TransaccionATMResponse transaccionATMResponse=transaccionService.transaccionCajeroAutomatico(request);
        if(transaccionATMResponse.getMontoFinal()<0){
            return ResponseEntity.ok(MensajeResponse.builder().mensaje("No tiene saldo suficiente.").build());
        }
        return ResponseEntity.ok(transaccionATMResponse);

    }
}
