package com.opombo.service;

import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.repository.MensagemRepository;
import com.opombo.service.ImagemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MensagemServiceTest {

    @Mock
    private MensagemRepository mensagemRepository;

    @Mock
    private ImagemService imagemService;

    @InjectMocks
    private MensagemService mensagemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testeSalvarMensagemComSucesso() throws OPomboException {
        Mensagem mensagem = new Mensagem();
        mensagem.setTexto("Mensagem válida");

        MultipartFile imagemMock = mock(MultipartFile.class);
        when(imagemMock.isEmpty()).thenReturn(false);
        when(imagemService.processarImagem(imagemMock)).thenReturn("imagemBase64");

        when(mensagemRepository.save(mensagem)).thenReturn(mensagem);

        Mensagem resultado = mensagemService.salvar(mensagem, imagemMock);

        assertNotNull(resultado);
        assertEquals("imagemBase64", resultado.getImagem());

        verify(imagemService, times(1)).processarImagem(imagemMock);
        verify(mensagemRepository, times(1)).save(mensagem);
    }

    @Test
    public void testeSalvarMensagemComErroDeTamanho() {
        Mensagem mensagem = new Mensagem();
        String textoGrande = "A".repeat(301);
        mensagem.setTexto(textoGrande);

        MultipartFile imagemMock = mock(MultipartFile.class);
        when(imagemMock.isEmpty()).thenReturn(false);

        OPomboException exception = assertThrows(OPomboException.class, () -> {
            mensagemService.salvar(mensagem, imagemMock);
        });

        assertEquals("Mensagem pode ter no máximo 300 caracteres.", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());

        verify(mensagemRepository, times(0)).save(mensagem);
    }
}
