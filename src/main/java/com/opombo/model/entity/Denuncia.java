package com.opombo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Denuncia {

    @EmbeddedId
    private DenunciaPK id;

    @ManyToOne
    @MapsId("idMensagem")
    @JoinColumn(name = "id_mensagem")
    private Mensagem mensagem;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private LocalDate data;

    // private Motivo motivo;

    private boolean foiAnalisada;


}
