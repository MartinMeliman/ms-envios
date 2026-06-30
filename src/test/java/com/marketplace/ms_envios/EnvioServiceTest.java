package com.marketplace.ms_envios;

import com.marketplace.ms_envios.model.Envio;
import com.marketplace.ms_envios.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.marketplace.ms_envios.service.EnvioService;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para EnvioService.
 * Patrón Given/When/Then con Mockito (sin BD real).
 */
@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock private EnvioRepository envioRepository;
    @InjectMocks private EnvioService envioService;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio();
        envio.setId(1L);
        envio.setPedidoId(10L);
        envio.setEstado("PREPARANDO");
    }

    @Test
    @DisplayName("obtenerTodos: debería retornar lista de envíos")
    void shouldReturnAllEnvios() {
        // GIVEN
        when(envioRepository.findAll()).thenReturn(List.of(envio));
        // WHEN
        List<Envio> resultado = envioService.obtenerTodos();
        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("obtenerPorId: debería retornar el envío cuando existe")
    void shouldReturnEnvioById() {
        // GIVEN
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        // WHEN
        Optional<Envio> resultado = envioService.obtenerPorId(1L);
        // THEN
        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getPedidoId());
    }

    @Test
    @DisplayName("obtenerPorPedido: debería retornar el envío del pedido")
    void shouldReturnEnvioByPedido() {
        // GIVEN
        when(envioRepository.findByPedidoId(10L)).thenReturn(Optional.of(envio));
        // WHEN
        Optional<Envio> resultado = envioService.obtenerPorPedido(10L);
        // THEN
        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("guardar: debería crear el envío y generar código de seguimiento")
    void shouldSaveEnvioAndGenerateTracking() {
        // GIVEN — no existe envío previo para el pedido
        when(envioRepository.findByPedidoId(10L)).thenReturn(Optional.empty());
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));
        // WHEN
        Envio resultado = envioService.guardar(envio);
        // THEN — se generó el código de seguimiento
        assertNotNull(resultado.getCodigoSeguimiento());
        assertTrue(resultado.getCodigoSeguimiento().startsWith("ENV-"));
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("guardar: debería lanzar excepción cuando ya existe envío para el pedido")
    void shouldThrowWhenEnvioDuplicated() {
        // GIVEN — regla de negocio: un envío por pedido
        when(envioRepository.findByPedidoId(10L)).thenReturn(Optional.of(envio));
        // WHEN + THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> envioService.guardar(envio));
        assertTrue(ex.getMessage().contains("Ya existe un envio"));
        verify(envioRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarEstado: debería actualizar el estado y agregar seguimiento")
    void shouldUpdateEstadoAndAddTracking() {
        // GIVEN
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));
        // WHEN
        Optional<Envio> resultado = envioService.actualizarEstado(1L, "EN_CAMINO", "Paquete en ruta");
        // THEN
        assertTrue(resultado.isPresent());
        assertEquals("EN_CAMINO", resultado.get().getEstado());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }
}
