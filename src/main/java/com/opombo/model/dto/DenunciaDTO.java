package com.opombo.model.dto;

import com.opombo.model.enums.MotivoDaDenuncia;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DenunciaDTO {

    private String idMensagem;
    private String idUsuario;
    private MotivoDaDenuncia motivo;
}
