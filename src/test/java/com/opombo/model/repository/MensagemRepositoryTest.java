package com.opombo.model.repository;

import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

@SpringBootTest

@ActiveProfiles("test")
class MensagemRepositoryTest {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario publicador;

    private Mensagem mensagem;

    @BeforeEach
    public void setUp() {
        publicador = new Usuario();
        publicador.setNome("João Silva");
        publicador.setEmail("joao.silva@example.com");
        publicador.setCpf("00732529050");
        publicador.setSenha("senha123");
        publicador.setTipo(TipoDeUsuario.USUARIO);
        usuarioRepository.save(publicador);

        mensagem = new Mensagem();
        mensagem.setTexto("Mensagem original");
        mensagem.setPublicador(publicador);
        mensagemRepository.save(mensagem);
    }


    @Test
    @DisplayName("Deve salvar uma nova mensagem com sucesso")
    public void deveSalvarMensagemComSucesso() {
        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setTexto("Esta é uma nova mensagem de teste.");
        novaMensagem.setPublicador(publicador);

        Mensagem mensagemSalva = mensagemRepository.save(novaMensagem);

        Optional<Mensagem> mensagemBuscada = mensagemRepository.findById(mensagemSalva.getId());
        assertThat(mensagemBuscada).isPresent();
        assertThat(mensagemSalva.getId()).isNotNull();
        assertThat(mensagemSalva.getCriadoEm()).isNotNull();
        assertThat(mensagemSalva.getTexto()).isEqualTo("Esta é uma nova mensagem de teste.");
        assertThat(mensagemSalva.getPublicador().getNome()).isEqualTo("João Silva");
        assertThat(mensagemSalva.getQtdeLikes()).isEqualTo(0);
        assertThat(mensagemSalva.getUsuariosQueCurtiram()).isEmpty();
        assertThat(mensagemSalva.getBloqueado()).isEqualTo(false);
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o texto está em branco")
    void deveFalharAoSalvarMensagemComTextoEmBranco() {
        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setTexto("");
        novaMensagem.setPublicador(publicador);

        assertThatThrownBy(() -> mensagemRepository.saveAndFlush(novaMensagem))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Mensagem é obrigatória");
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o texto é nulo")
    public void deveFalharQuandoTextoForNulo() {
        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setTexto(null);
        novaMensagem.setPublicador(publicador);

        assertThatThrownBy(() -> mensagemRepository.saveAndFlush(novaMensagem))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Mensagem é obrigatória");
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o texto contém mais de 300 caracteres")
    public void deveFalharQuandoTextoForMaiorQue300Caracteres() {
        String texto = "a".repeat(301);

        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setTexto(texto);
        novaMensagem.setPublicador(publicador);

        assertThrows(ConstraintViolationException.class, () -> {
            mensagemRepository.saveAndFlush(novaMensagem);
        });
    }

    @Test
    @DisplayName("Deve atualizar uma nova mensagem com sucesso")
    public void deveAtualizarMensagemComSucesso() {
        mensagem.setTexto("Mensagem atualizada com sucesso");
        Mensagem mensagemAtualizada = mensagemRepository.saveAndFlush(mensagem);

        Optional<Mensagem> mensagemBuscada = mensagemRepository.findById(mensagemAtualizada.getId());
        assertThat(mensagemBuscada).isPresent();
        assertThat(mensagemBuscada.get().getTexto()).isEqualTo("Mensagem atualizada com sucesso");
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o texto é nulo")
    public void deveFalharQuandoAtualizarComTextoNulo() {
        mensagem.setTexto(null);

        assertThatThrownBy(() -> mensagemRepository.saveAndFlush(mensagem))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Mensagem é obrigatória");
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o texto está em branco")
    public void deveFalharQuandoAtualizarComTextoVazio() {
        mensagem.setTexto("");

        assertThatThrownBy(() -> mensagemRepository.saveAndFlush(mensagem))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Mensagem é obrigatória");
    }

    @Test
    @DisplayName("Deve lançar um excessão pois o texto contém mais de 300 caracteres")
    public void deveFalharQuandoAtualizarComTextoMaiorQue300Caracteres() {
        String texto = "a".repeat(301);
        mensagem.setTexto(texto);

        assertThrows(ConstraintViolationException.class, () -> {
            mensagemRepository.saveAndFlush(mensagem);
        });
    }

    @Test
    @DisplayName("Deve excluir mensagem com sucesso")
    public void deveExcluirMensagemComSucesso() {
        String mensagemId = mensagem.getId();
        mensagemRepository.deleteById(mensagemId);

        Optional<Mensagem> mensagemBuscada = mensagemRepository.findById(mensagemId);
        assertThat(mensagemBuscada).isNotPresent();
    }

    @Test
    @DisplayName("Deve falhar pois id não existe")
    public void deveFalharAoTentarExcluirMensagemComIdInexistente() {
        String idInexistente = "af61adfadf648";

        assertThat(mensagemRepository.findById(idInexistente)).isNotPresent();
    }

    @Test
    @DisplayName("Deve falhar pois id está nulo")
    public void deveFalharAoTentarExcluirMensagemComIdNulo() {
        String idNulo = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> mensagemRepository.deleteById(idNulo));
    }
}