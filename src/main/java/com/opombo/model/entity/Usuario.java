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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 3667682428012659277L;

    @Id
    @UuidGenerator
    private String id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Digite um email válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    @Column(length = 4000)
    private String senha;

    @NotBlank(message = "CPF é obrigatório")
    @CPF
    private String cpf;

    @Enumerated(EnumType.STRING)
    private TipoDeUsuario tipo;

    @Column(columnDefinition = "LONGTEXT")
    private String fotoPerfil;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "curtidas",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_mensagem")
    )
    private Set<Mensagem> mensagensCurtidas = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();

        list.add(new SimpleGrantedAuthority(tipo.toString()));

        return list;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}