package com.opombo.model.entity;

import com.opombo.model.enums.MotivoDaDenuncia;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private MotivoDaDenuncia motivo;

    private boolean foiAnalisada;
}
