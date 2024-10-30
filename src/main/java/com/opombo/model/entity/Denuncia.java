package com.opombo.model.entity;

import com.opombo.model.enums.MotivoDaDenuncia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class Denuncia {

    @EmbeddedId
    private DenunciaPK id = new DenunciaPK();

    @ManyToOne
    @MapsId("idMensagem")
    @JoinColumn(name = "id_mensagem")
    private Mensagem mensagem;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @CreationTimestamp
    private LocalDateTime data;

    @NotNull(message = "Motivo é obrigatório")
    @Enumerated(EnumType.STRING)
    private MotivoDaDenuncia motivo;

    private boolean foiAnalisada = false;
}
