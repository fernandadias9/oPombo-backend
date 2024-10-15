package com.opombo.model.repository;

import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//Anotação usada para subir uma base local (ex.: MySQL)
@SpringBootTest

//Caso queira usar a base H2 utilize
//@DataJpaTest

@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve salvar um novo usuário com sucesso")
    public void testInserirTodosCamposPreenchidos() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Teste");
        usuario.setCpf("40331786940");
        usuario.setEmail("usuario@email.com");
        usuario.setSenha("senha123");  // Definindo a senha
        usuario.setTipo(TipoDeUsuario.ADMINISTRADOR);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getNome()).isEqualTo("Usuario Teste");
        assertThat(usuarioSalvo.getCpf()).isEqualTo("40331786940");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
        assertThat(usuarioSalvo.getSenha()).isEqualTo("senha123");
        assertThat(usuarioSalvo.getTipo()).isEqualTo(TipoDeUsuario.ADMINISTRADOR);
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o nome está em branco")
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

    @Test
    @DisplayName("Deve lançar um excessão pois o nome tem menos que 3 caracteres")
    void deveFalharAoSalvarUsuarioComNomeMenorQue3Caracteres() {
        Usuario usuario = new Usuario();
        usuario.setCpf("26253943669");
        usuario.setEmail("teste3caracteres@teste.com");
        usuario.setNome("Jo");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o nome tem mais que 100 caracteres")
    void deveFalharAoSalvarUsuarioComNomeMaiorQue100Caracteres() {
        Usuario usuario = new Usuario();
        usuario.setCpf("30218552610");
        usuario.setEmail("teste@teste.com");
        usuario.setNome("Esse nome tem que ter mais de cem caracteres para não passar no teste de validação do nome portanto vou ficar enrolando aqui.");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o email está em branco")
    void deveFalharAoSalvarUsuarioComEmailEmBranco() {
        // Dado
        Usuario usuario = new Usuario();
        usuario.setCpf("12345678900");
        usuario.setEmail("");
        usuario.setNome("Teste email");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o email é inválido")
    void deveFalharAoSalvarUsuarioComEmailInvalido() {
        Usuario usuario = new Usuario();
        usuario.setCpf("40683139622");
        usuario.setNome("Nome Valido");
        usuario.setEmail("emailinvalido");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o cpf está em branco")
    void deveFalharAoSalvarUsuarioComCpfEmBranco() {
        Usuario usuario = new Usuario();
        usuario.setCpf("");
        usuario.setEmail("testecpf@teste.com");
        usuario.setNome("Teste cpf");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o cpf é inválido")
    void deveFalharAoSalvarUsuarioComCpfInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Nome Valido");
        usuario.setEmail("usuario@testecpf.com");
        usuario.setCpf("12345678900");
        usuario.setTipo(TipoDeUsuario.USUARIO);

        assertThrows(ConstraintViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuario);
        });
    }
}
