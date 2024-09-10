package com.opombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.opombo.model.enums.TipoDeUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.br.CPF;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Usuario {

    @Id
    @UuidGenerator
    private String id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Digite um email válido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @CPF
    private String cpf;

    @Enumerated(EnumType.STRING)
    private TipoDeUsuario tipo;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "curtidas",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_mensagem")
    )
    private Set<Mensagem> mensagensCurtidas = new HashSet<>();
}