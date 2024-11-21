package com.opombo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Mensagem {

    @Id
    @UuidGenerator
    private String id;

    @NotBlank(message = "Mensagem é obrigatória")
    @Column(length = 4000)
    //@Size(min = 1, max = 300)
    private String texto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario publicador;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    private int qtdeLikes = 0;

    @Column(columnDefinition = "LONGTEXT")
    private String imagem;

    @ManyToMany
    @ToString.Exclude
    @JoinTable(
            name = "curtidas",
            joinColumns = @JoinColumn(name = "id_mensagem"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private Set<Usuario> usuariosQueCurtiram = new HashSet<>();

    private Boolean bloqueado = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensagem mensagem = (Mensagem) o;
        return qtdeLikes == mensagem.qtdeLikes && bloqueado == mensagem.bloqueado && Objects.equals(id, mensagem.id) && Objects.equals(texto, mensagem.texto) && Objects.equals(publicador, mensagem.publicador) && Objects.equals(criadoEm, mensagem.criadoEm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, texto, publicador, criadoEm, qtdeLikes, bloqueado);
    }
}
