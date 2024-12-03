package com.opombo.service;

import com.opombo.exception.OPomboException;
import com.opombo.model.dto.DenunciaDTO;
import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.DenunciaPK;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.MotivoDaDenuncia;
import com.opombo.model.enums.TipoDeUsuario;
import com.opombo.model.repository.DenunciaRepository;
import com.opombo.model.repository.MensagemRepository;
import com.opombo.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DenunciaServiceTest {
    @InjectMocks
    private DenunciaService denunciaService;

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MensagemRepository mensagemRepository;

    private Usuario mockUsuario;
    private Mensagem mockMensagem;
    private DenunciaDTO denunciaDTO;
    private DenunciaPK denunciaPK;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUsuario = new Usuario();
        mockUsuario.setId("usuario123");
        mockUsuario.setNome("João");
        mockUsuario.setEmail("joao@email.com");
        mockUsuario.setCpf("123.456.789-00");
        mockUsuario.setTipo(TipoDeUsuario.USUARIO);

        mockMensagem = new Mensagem();
        mockMensagem.setId("mensagem123");
        mockMensagem.setTexto("Texto da mensagem");

        denunciaDTO = new DenunciaDTO();
        denunciaDTO.setIdUsuario("usuario123");
        denunciaDTO.setIdMensagem("mensagem123");
        denunciaDTO.setMotivo(MotivoDaDenuncia.PUBLICAO_OFENSIVA);

        denunciaPK = new DenunciaPK();
        denunciaPK.setIdUsuario("usuario123");
        denunciaPK.setIdMensagem("mensagem123");
    }

    @Test
    @DisplayName("Deve denunciar mensagem com sucesso")
    void testDenunciarMensagem() {
        when(usuarioRepository.findById("usuario123")).thenReturn(Optional.of(mockUsuario));
        when(mensagemRepository.findById("mensagem123")).thenReturn(Optional.of(mockMensagem));
        when(denunciaRepository.save(any(Denuncia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Denuncia result = denunciaService.denunciarMensagem(denunciaDTO);

        assertNotNull(result);
        assertEquals("usuario123", result.getUsuario().getId());
        assertEquals("mensagem123", result.getMensagem().getId());
        assertEquals(MotivoDaDenuncia.PUBLICAO_OFENSIVA, result.getMotivo());

        verify(usuarioRepository, times(1)).findById("usuario123");
        verify(mensagemRepository, times(1)).findById("mensagem123");
        verify(denunciaRepository, times(1)).save(any(Denuncia.class));
    }

    @Test
    @DisplayName("Deve atualizar uma denúncia com sucesso")
    void testAtualizarDenuncia() throws OPomboException {
        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaPK);
        denuncia.setFoiAnalisada(false);

        when(denunciaRepository.findById(denunciaPK)).thenReturn(Optional.of(denuncia));
        when(denunciaRepository.save(any(Denuncia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Denuncia result = denunciaService.atualizarDenuncia(denunciaPK, true);

        assertNotNull(result);
        assertTrue(result.getFoiAnalisada());

        verify(denunciaRepository, times(1)).findById(denunciaPK);
        verify(denunciaRepository, times(1)).save(denuncia);
    }

    @Test
    @DisplayName("Deve falhar pois não é o denunciante que está excluindo")
    void testExcluirApenasCriadorPodeExcluir() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId("usuario456");

        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaPK);
        denuncia.setUsuario(outroUsuario);

        when(usuarioRepository.findById("usuario123")).thenReturn(Optional.of(mockUsuario));
        when(mensagemRepository.findById("mensagem123")).thenReturn(Optional.of(mockMensagem));
        when(denunciaRepository.findById(denunciaPK)).thenReturn(Optional.of(denuncia));

        OPomboException exception = assertThrows(OPomboException.class,
                () -> denunciaService.excluir("usuario123", "mensagem123"));

        assertEquals("Apenas o usuário que criou a denúncia pode excluí-la", exception.getMessage());
    }
}
