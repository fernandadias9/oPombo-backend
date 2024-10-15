package com.opombo.service;

import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Usuario;
import com.opombo.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;

import java.util.Optional;

@SpringBootTest

@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testeCpfDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setCpf("12345678900");
        when(usuarioRepository.findByCpf("12345678900")).thenReturn(new Usuario());

        OPomboException exception = assertThrows(OPomboException.class, () -> {
            usuarioService.salvar(usuario);
        });

        assertEquals("CPF já cadastrado na base de dados.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(usuarioRepository, times(1)).findByCpf("12345678900");
        verify(usuarioRepository, times(0)).save(usuario);
    }

    @Test
    public void testeEmailDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@teste.com");
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(new Usuario()));

        OPomboException exception = assertThrows(OPomboException.class, () -> {
            usuarioService.salvar(usuario);
        });

        assertEquals("Email já cadastrado na base de dados.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(usuarioRepository, times(1)).findByEmail("teste@teste.com");
        verify(usuarioRepository, times(0)).save(usuario);
    }
}
