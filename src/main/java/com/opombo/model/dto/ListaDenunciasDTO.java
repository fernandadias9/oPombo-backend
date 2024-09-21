package com.opombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListaDenunciasDTO {

    private String idMensagem;
    private Integer quantidadeDenuncias;
    private Integer quantidadeDenunciasNaoAnalisadas;
    private Integer quantidadeDenunciasAnalisadas;
}
