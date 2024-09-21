package com.opombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListaMensagensDTO {

    private String id;
    private String texto;
    private Integer quantidadeCurtidas;
    private String idUsuario;
    private String nomeUsuario;
    private Integer quantidadeDenuncias;
}
