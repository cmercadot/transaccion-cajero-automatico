package com.cajeroautomatico.cuenta.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;


public class PageableConverter {

    public static   <T, R> Page<R> castObjectTo(Page<T> page, java.util.function.Function<T, R> convertidor) {
        List<R> contenidoConvertido = page.getContent().stream().map(convertidor).toList();
        return new PageImpl<>(contenidoConvertido, page.getPageable(), page.getTotalElements());
    }
}
