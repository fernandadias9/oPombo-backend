package com.opombo.model.dto;

import com.opombo.model.enums.MotivoDaDenuncia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DenunciaDTO {

    private String idMensagem;
    private String idUsuario;
    private MotivoDaDenuncia motivo;
    private LocalDateTime dataDenuncia;
    private String nomeDenunciante;
    private String conteudoMensagem;
}
