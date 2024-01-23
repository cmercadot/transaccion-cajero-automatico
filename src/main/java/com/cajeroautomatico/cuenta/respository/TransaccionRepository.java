package com.cajeroautomatico.cuenta.respository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<TransaccionEntity,Long> {

}
