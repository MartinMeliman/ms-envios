package com.marketplace.ms_envios.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Entity @Table(name="seguimiento_envio")
public class SeguimientoEnvio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=30) private String estado;
    @Column(nullable=false, length=300) private String descripcion;
    @Column(name="registrado_en", updatable=false) private LocalDateTime registradoEn;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="envio_id", nullable=false) private Envio envio;
    @PrePersist public void pre(){ registradoEn=LocalDateTime.now(); }
}
