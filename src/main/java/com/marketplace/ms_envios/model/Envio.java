package com.marketplace.ms_envios.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Entity @Table(name="envios")
public class Envio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotNull @Column(name="pedido_id", nullable=false, unique=true) private Long pedidoId;
    @NotNull @Column(name="usuario_id", nullable=false) private Long usuarioId;
    // Estados: PREPARANDO, EN_CAMINO, ENTREGADO
    @Column(nullable=false, length=30) private String estado="PREPARANDO";
    @NotBlank @Column(name="direccion_destino", nullable=false, length=300) private String direccionDestino;
    @Column(name="codigo_seguimiento", unique=true, length=100) private String codigoSeguimiento;
    @Column(name="creado_en", updatable=false) private LocalDateTime creadoEn;
    @OneToMany(mappedBy="envio", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true) private List<SeguimientoEnvio> seguimientos;
    @PrePersist public void pre(){ creadoEn=LocalDateTime.now(); }
}
