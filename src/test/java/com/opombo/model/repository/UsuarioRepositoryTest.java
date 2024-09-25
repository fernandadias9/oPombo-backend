package com.opombo.model.repository;

import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

//Anotação usada para subir uma base local (ex.: MySQL)
@SpringBootTest

//Caso queira usar a base H2 utilize
//@DataJpaTest

@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void testInserirTodosCamposPreenchidos() {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Teste");
        usuario.setCpf("40331786940");
        usuario.setEmail("usuario@email.com");
        usuario.setTipo(TipoDeUsuario.ADMINISTRADOR);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getNome()).isEqualTo("Usuario Teste");
        assertThat(usuarioSalvo.getCpf()).isEqualTo("40331786940");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
        assertThat(usuarioSalvo.getTipo()).isEqualTo(TipoDeUsuario.ADMINISTRADOR);
    }

    public void testInserirComNomeEmBranco() {
        Usuario usuario = new Usuario();
        usuario.setCpf("40331786940");
        usuario.setEmail("usuario@email.com");
        usuario.setTipo(TipoDeUsuario.ADMINISTRADOR);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getNome()).isEqualTo("Usuario Teste");
        assertThat(usuarioSalvo.getCpf()).isEqualTo("40331786940");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
        assertThat(usuarioSalvo.getTipo()).isEqualTo(TipoDeUsuario.ADMINISTRADOR);
    }
}
