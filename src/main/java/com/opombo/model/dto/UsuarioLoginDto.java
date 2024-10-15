package com.opombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioLoginDto {
    private Long id;
    private String login;
    private String password;
}
