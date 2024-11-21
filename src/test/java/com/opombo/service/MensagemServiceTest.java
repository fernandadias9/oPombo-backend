package com.opombo.service;

import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.repository.MensagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MensagemServiceTest {

    @Mock
    private MensagemRepository mensagemRepository;

    @Mock
    private DenunciaService denunciaService;

    @Mock
    private ImagemService imagemService;

    @InjectMocks
    private MensagemService mensagemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void salvarComTextoMaiorQueLimiteLancaExcecao() {
        Mensagem mensagem = new Mensagem();
        mensagem.setTexto("A".repeat(301));

        OPomboException exception = assertThrows(OPomboException.class, () -> {
            mensagemService.salvar(mensagem);
        });

        assertEquals("Mensagem pode ter no máximo 300 caracteres.", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());

        verify(mensagemRepository, never()).save(any(Mensagem.class));
    }

    @Test
    void salvarMensagemValida() throws OPomboException {
        Mensagem mensagem = new Mensagem();
        mensagem.setTexto("Texto válido");

        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        Mensagem resultado = mensagemService.salvar(mensagem);

        assertNotNull(resultado);
        assertEquals("Texto válido", resultado.getTexto());

        verify(mensagemRepository, times(1)).save(mensagem);
    }

    @Test
    void buscarMensagemNaoEncontradaLancaExcecao() {
        String idMensagem = "123";
        when(mensagemRepository.findById(idMensagem)).thenReturn(Optional.empty());

        OPomboException exception = assertThrows(OPomboException.class, () -> {
            mensagemService.buscar(idMensagem);
        });

        assertEquals("Mensagem não encontrada!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void buscarMensagemExistente() throws OPomboException {
        String idMensagem = "123";
        Mensagem mensagem = new Mensagem();
        mensagem.setId(idMensagem);

        when(mensagemRepository.findById(idMensagem)).thenReturn(Optional.of(mensagem));

        Mensagem resultado = mensagemService.buscar(idMensagem);

        assertNotNull(resultado);
        assertEquals(idMensagem, resultado.getId());
    }

    @Test
    void listarMensagens() {
        when(mensagemRepository.findAll()).thenReturn(List.of(new Mensagem(), new Mensagem()));

        List<Mensagem> mensagens = mensagemService.listar();

        assertNotNull(mensagens);
        assertEquals(2, mensagens.size());
    }

    @Test
    void salvarImagemMensagemNaoEncontradaLancaExcecao() {
        String idMensagem = "123";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mensagemRepository.findById(idMensagem)).thenReturn(Optional.empty());

        OPomboException exception = assertThrows(OPomboException.class, () -> {
            mensagemService.salvarImagem(mockFile, idMensagem);
        });

        assertEquals("Mensagem não encontrada", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void salvarImagemMensagemValida() throws OPomboException {
        String idMensagem = "123";
        Mensagem mensagem = new Mensagem();
        mensagem.setId(idMensagem);
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mensagemRepository.findById(idMensagem)).thenReturn(Optional.of(mensagem));
        when(imagemService.processarImagem(mockFile)).thenReturn("imagemBase64");

        mensagemService.salvarImagem(mockFile, idMensagem);

        verify(imagemService, times(1)).processarImagem(mockFile);
        verify(mensagemRepository, times(1)).save(any(Mensagem.class));
    }

    @Test
    void curtirMensagemJaCurtidaRemoveCurtida() throws OPomboException {
        String idMensagem = "123";
        Usuario usuario = new Usuario();
        Mensagem mensagem = new Mensagem();
        mensagem.setId(idMensagem);
        mensagem.setTexto("Texto");
        mensagem.getUsuariosQueCurtiram().add(usuario);

        when(mensagemRepository.findById(idMensagem)).thenReturn(Optional.of(mensagem));
        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        mensagemService.curtir(idMensagem, usuario);

        assertFalse(mensagem.getUsuariosQueCurtiram().contains(usuario));
    }
}
