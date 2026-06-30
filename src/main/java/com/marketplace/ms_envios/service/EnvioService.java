package com.marketplace.ms_envios.service;
import com.marketplace.ms_envios.model.*;
import com.marketplace.ms_envios.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
@Slf4j @Service @RequiredArgsConstructor
public class EnvioService {
    private final EnvioRepository envioRepository;
    public List<Envio> obtenerTodos(){ return envioRepository.findAll(); }
    public Optional<Envio> obtenerPorId(Long id){ return envioRepository.findById(id); }
    public Optional<Envio> obtenerPorPedido(Long pid){ return envioRepository.findByPedidoId(pid); }
    public Envio guardar(Envio e){
        if(envioRepository.findByPedidoId(e.getPedidoId()).isPresent()) throw new RuntimeException("Ya existe un envio para el pedido ID: "+e.getPedidoId());
        e.setCodigoSeguimiento("ENV-"+System.currentTimeMillis());
        log.info("Creando envio para pedido ID: {}", e.getPedidoId());
        return envioRepository.save(e);
    }
    public Optional<Envio> actualizarEstado(Long id, String estado, String desc){
        return envioRepository.findById(id).map(e->{
            e.setEstado(estado);
            SeguimientoEnvio s=new SeguimientoEnvio(); s.setEstado(estado); s.setDescripcion(desc!=null?desc:estado); s.setEnvio(e);
            if(e.getSeguimientos()==null) e.setSeguimientos(new ArrayList<>());
            e.getSeguimientos().add(s); return envioRepository.save(e);
        });
    }
}
