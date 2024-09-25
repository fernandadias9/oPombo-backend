package com.opombo.service;

import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import com.opombo.model.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest

@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveFalharAoSalvarUsuarioComNomeEmBranco() {
        // Dado
        Usuario usuario = new Usuario();
        usuario.setCpf("12345678900");
        usuario.setEmail("teste@teste.com");
        usuario.setNome("");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        // Quando & Então
        // Tenta salvar o usuário no banco e espera-se que ocorra um erro de validação
        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }
}
