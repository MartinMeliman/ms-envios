package com.marketplace.ms_envios.repository;
import com.marketplace.ms_envios.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EnvioRepository extends JpaRepository<Envio,Long> {
    Optional<Envio> findByPedidoId(Long pedidoId);
}
