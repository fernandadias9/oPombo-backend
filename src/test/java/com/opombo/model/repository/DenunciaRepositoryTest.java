package com.opombo.model.repository;

import com.opombo.model.dto.DenunciaDTO;
import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.DenunciaPK;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.MotivoDaDenuncia;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest

@ActiveProfiles("test")
public class DenunciaRepositoryTest {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Test");
        usuarioTest.setEmail("test@mail.com");
        usuarioTest.setCpf("34348230005");
        usuarioTest.setSenha("test123");
        usuarioRepository.save(usuarioTest);

        Mensagem mensagemTest = new Mensagem();
        mensagemTest.setTexto("Mensagem de teste");
        mensagemTest.setPublicador(usuarioTest);
        mensagemRepository.save(mensagemTest);

        Denuncia denunciaTeste = new Denuncia();
        denunciaTeste.setMensagem(mensagemTest);
        denunciaTeste.setUsuario(usuarioTest);
        denunciaTeste.setMotivo(MotivoDaDenuncia.INFORMACAO_FALSA);
        denunciaRepository.save(denunciaTeste);

    }

    @AfterEach
    public void tearDown() {
        denunciaRepository.deleteAll();
        mensagemRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    public void testeCriarDenunciaMotivoNaoPreenchido() {
        Usuario usuarioTest1 = new Usuario();
        usuarioTest1.setNome("Test");
        usuarioTest1.setEmail("test@mail.com");
        usuarioTest1.setCpf("34348230005");
        usuarioTest1.setSenha("test123");
        usuarioRepository.saveAndFlush(usuarioTest1);

        Mensagem mensagemTest1 = new Mensagem();
        mensagemTest1.setTexto("Mensagem de teste");
        mensagemTest1.setPublicador(usuarioTest1);
        mensagemRepository.saveAndFlush(mensagemTest1);

        Denuncia denunciaTeste1 = new Denuncia();
        denunciaTeste1.setMensagem(mensagemTest1);
        denunciaTeste1.setUsuario(usuarioTest1);

        DenunciaPK pk = new DenunciaPK();
        pk.setIdMensagem(mensagemTest1.getId());
        pk.setIdUsuario(usuarioTest1.getId());
        denunciaTeste1.setId(pk);

        assertThatThrownBy(() -> denunciaRepository.saveAndFlush(denunciaTeste1)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Motivo é obrigatório");
    }


}
