package com.tienda.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void fechaCreacion(){
        this.fechaCreacion = LocalDateTime.now();
    }
}
