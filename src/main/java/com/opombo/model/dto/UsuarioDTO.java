package com.opombo.model.dto;

import com.opombo.model.enums.TipoDeUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UsuarioDTO {

    private String id;
    private String nome;
    private String email;
    private String cpf;
    private TipoDeUsuario tipo;
    private String fotoPerfil;
    private String senha;
}
