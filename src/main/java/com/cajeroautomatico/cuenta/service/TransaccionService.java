package com.cajeroautomatico.cuenta.service;

import com.cajeroautomatico.cuenta.dto.*;

import com.cajeroautomatico.cuenta.respository.TransaccionEntity;
import com.cajeroautomatico.cuenta.respository.TransaccionRepository;
import org.hibernate.query.sqm.internal.SimpleDeleteQueryPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
public class TransaccionService {

    SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    TransaccionRepository transaccionRepository;
    String urlGestionCuenta="http://cuenta:8085/v1/cuenta";
    String urlGestionTasaDeCambio="http://tasa:8084/v1/tasa-de-cambio";
    public TransaccionATMResponse transaccionCajeroAutomatico(TransaccionATMRequest request){

        RestTemplate restTemplate=new RestTemplate();
        String urlGestionCuentaById=urlGestionCuenta+"/{nroCuenta}";
        ResponseEntity<CuentaResponse>  responseCuenta=restTemplate.getForEntity(urlGestionCuentaById,
                CuentaResponse.class,request.getNroCuenta());



        if(responseCuenta.getStatusCode().value()==200){
            CuentaResponse cuentaResponse=responseCuenta.getBody();
            String urlGestionTasaById=urlGestionTasaDeCambio+"/{moneda}";
            ResponseEntity<TasaDeCambioResponse>  responseTasa=restTemplate.getForEntity(urlGestionTasaById,
                    TasaDeCambioResponse.class,request.getMonedaTransaccion());

            if(responseTasa.getStatusCode().value()==200){
                Double tipoCambio=1.0d;
                Double montoFinal=0.0d;
                TasaDeCambioResponse tasaDeCambioResponse=responseTasa.getBody();
                if (request.getTipoTransaccion().equals(TipoTransaccion.DEPOSITAR.name() )){
                    tipoCambio=tasaDeCambioResponse.getTipoCambioCompra();
                }else{
                    tipoCambio=tasaDeCambioResponse.getTipoCambioVenta();
                }
                if ( request.getMonedaTransaccion().equals( cuentaResponse.getMonedaCuenta()) ){
                    tipoCambio=1.0d;
                    montoFinal=request.getMonto();
                }else{
                    if( request.getMonedaTransaccion().equals(Moneda.USD.name())
                            && cuentaResponse.getMonedaCuenta().equals(Moneda.BOB.name())){
                        montoFinal=request.getMonto()*tipoCambio;
                    }else {
                        montoFinal=request.getMonto()/tipoCambio;
                    }
                }
                if (request.getTipoTransaccion().equals(TipoTransaccion.RETIRAR.name() )){
                      Double diferencia=cuentaResponse.getSaldoCuenta()-montoFinal;
                      if (diferencia<0){
                            return TransaccionATMResponse.builder().montoFinal(diferencia).build();
                      }
                }

                TransaccionEntity transaccionEntity=TransaccionEntity.builder()
                        .nroCuenta( request.getNroCuenta())
                        .tipoTransaccion(request.getTipoTransaccion())
                        .monedaTransaccion(request.getMonedaTransaccion())
                        .monto( request.getMonto())
                        .tipoCambio(tipoCambio)
                        .monedaCuenta(cuentaResponse.getMonedaCuenta())
                        .montoFinal(montoFinal)
                        .estado("PROCESADO")
                        .fecha(new Timestamp( (new java.util.Date()).getTime()  ))
                        .build();
                actualizarSaldo(cuentaResponse,montoFinal,request.getTipoTransaccion());
                return convertirEntityAResponse(transaccionRepository.save(transaccionEntity));

            }

        }
        return TransaccionATMResponse.builder().build();
    }

    public TransaccionATMResponse convertirEntityAResponse(TransaccionEntity entity){
        return TransaccionATMResponse.builder()
                .nroTransaccion(entity.getNroTransaccion() )
                .nroCuenta( entity.getNroCuenta())
                .tipoTransaccion(entity.getTipoTransaccion())
                .monedaTransaccion(entity.getMonedaTransaccion())
                .monto( entity.getMonto())
                .tipoCambio(entity.getTipoCambio() )
                .monedaCuenta(entity.getMonedaCuenta())
                .montoFinal(entity.getMontoFinal())
                .estado(entity.getEstado())
                .fecha( f.format( entity.getFecha()))
                .build();

    }
    public void actualizarSaldo(CuentaResponse cuentaResponse,Double montoFinal,String tipoOperacion){
        Double saldoCuenta=0.0d;
        if(tipoOperacion.equals(TipoTransaccion.RETIRAR.name())){
            saldoCuenta=cuentaResponse.getSaldoCuenta()-montoFinal;
        }else{
            saldoCuenta=cuentaResponse.getSaldoCuenta()+montoFinal;
        }

        CuentaRequest request=CuentaRequest.builder()
                .codCliente(cuentaResponse.getCodCliente())
                .nroCuenta(cuentaResponse.getNroCuenta())
                .nombreCuenta(cuentaResponse.getNombreCuenta())
                .saldoCuenta(saldoCuenta)
                .monedaCuenta(cuentaResponse.getMonedaCuenta())
                .estado("ACTIVO")
                .build();


        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CuentaRequest> requestHttpEntity=new HttpEntity<CuentaRequest>(request,httpHeaders);
        RestTemplate restTemplate=new RestTemplate();

        ResponseEntity<CuentaResponse> cuentaResponseEnti=restTemplate.exchange(urlGestionCuenta, HttpMethod.PUT,requestHttpEntity,CuentaResponse.class);
        CuentaResponse cuentaResponse2=cuentaResponseEnti.getBody();
        System.out.println(cuentaResponse2);

    }

}
