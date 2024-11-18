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
import org.junit.jupiter.api.DisplayName;
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
    public void setUp() {
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

        DenunciaPK denunciaPK = new DenunciaPK();
        denunciaPK.setIdMensagem(mensagemTest.getId());
        denunciaPK.setIdUsuario(usuarioTest.getId());

        Denuncia denunciaTeste = new Denuncia();
        denunciaTeste.setId(denunciaPK);
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
    @DisplayName("Deve lançar um excessão, pois o motivo está em branco.")
    public void testeCriarDenunciaMotivoNaoPreenchido() {
        Usuario usuarioTest = new Usuario();
        usuarioTest.setNome("Test denúncia sem motivo");
        usuarioTest.setEmail("test1@mail.com");
        usuarioTest.setCpf("33375125003");
        usuarioTest.setSenha("test01");
        usuarioRepository.save(usuarioTest);

        Mensagem mensagemTest = new Mensagem();
        mensagemTest.setTexto("Mensagem de teste");
        mensagemTest.setPublicador(usuarioTest);
        mensagemRepository.save(mensagemTest);

        DenunciaPK denunciaPK = new DenunciaPK();
        denunciaPK.setIdMensagem(mensagemTest.getId());
        denunciaPK.setIdUsuario(usuarioTest.getId());

        Denuncia denunciaTeste = new Denuncia();
        denunciaTeste.setId(denunciaPK);
        denunciaTeste.setMensagem(mensagemTest);
        denunciaTeste.setUsuario(usuarioTest);

        assertThatThrownBy(() -> denunciaRepository.saveAndFlush(denunciaTeste)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Motivo é obrigatório");
    }

    @Test
    @DisplayName("Deve retornar todas as denúncias como DenunciaDTO")
    public void testFindAllDenunciasDTO() {
        Denuncia denuncia = denunciaRepository.findAll().get(0);
        String idMensagem = denuncia.getMensagem().getId();
        String idUsuario = denuncia.getUsuario().getId();

        List<DenunciaDTO> denunciasDTO = denunciaRepository.findAllDenunciasDTO();

        assertFalse(denunciasDTO.isEmpty());

        DenunciaDTO denunciaDTO = denunciasDTO.get(0);
        assertEquals(idMensagem, denunciaDTO.getIdMensagem());
        assertEquals(idUsuario, denunciaDTO.getIdUsuario());
        assertEquals(MotivoDaDenuncia.INFORMACAO_FALSA, denunciaDTO.getMotivo());
    }

    @Test
    @DisplayName("Deve retornar denúncias associadas a uma mensagem específica pelo ID")
    public void testFindByMensagemId() {
        String idMensagem = mensagemRepository.findAll().get(0).getId();
        List<Denuncia> denuncias = denunciaRepository.findByMensagemId(idMensagem);

        assertFalse(denuncias.isEmpty());

        for (Denuncia denuncia : denuncias) {
            assertEquals(idMensagem, denuncia.getMensagem().getId());
        }
    }


}
