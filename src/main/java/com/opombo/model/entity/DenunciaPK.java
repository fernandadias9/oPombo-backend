package com.opombo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class DenunciaPK implements Serializable {

    @Column(name = "id_mensagem")
    Integer idMensagem;

    @Column(name = "id_usuario")
    Integer idUsuario;
}
