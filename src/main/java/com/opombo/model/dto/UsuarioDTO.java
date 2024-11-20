package com.opombo.model.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private String id;
    private String nome;
    private String email;
    private String cpf;
    private String tipo;
    private String senha;
    private String fotoPerfil;
}
