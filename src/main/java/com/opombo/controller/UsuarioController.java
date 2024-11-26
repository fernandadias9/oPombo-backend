package com.opombo.controller;

import com.opombo.auth.AuthenticationService;
import com.opombo.exception.OPomboException;
import com.opombo.model.dto.UsuarioDTO;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.filtro.UsuarioFiltro;
import com.opombo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationService authService;

    @PutMapping(path = "/atualizar")
    public ResponseEntity<Usuario> atualizar(@Valid @RequestBody UsuarioDTO usuarioDTO) throws OPomboException {
        Usuario usuarioAtualizado = usuarioService.atualizar(usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @GetMapping("/autenticado")
    public Usuario usuarioAutenticado() throws OPomboException {
        Usuario usuario = authService.getUsuarioAutenticado();

        String profilePictureUrl = null;

        return usuario;
    }

    @GetMapping(path = "/{id}")
    public  Usuario buscar(@PathVariable String id) {
        return usuarioService.buscar(id);
    }

    @GetMapping
    public List<Usuario> listar() throws OPomboException {
        return usuarioService.listar();
    }

    @PostMapping("/filtro")
    public List<Usuario> pesquisarComFiltro(@RequestBody UsuarioFiltro filtro) {
        return usuarioService.listarComFiltro(filtro);
    }

    @GetMapping("/mensagensCurtidas/{id}")
    public ResponseEntity<Set<Mensagem>> obterMensagensCurtidas(@PathVariable String id) throws OPomboException {
        Set<Mensagem> mensagensCurtidas = usuarioService.obterMensagensCurtidas(id);
        return ResponseEntity.ok(mensagensCurtidas);
    }

    @PostMapping("/upload")
    public void fazerUploadFotoPerfil(@RequestParam("imagem") MultipartFile imagem, String idUsuario) throws OPomboException {

        if(imagem == null) {
            throw new OPomboException("Arquivo inválido.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
        if (usuarioAutenticado == null ) {
            throw new OPomboException("Usuário não encontrado.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        usuarioService.salvarFotoPerfil(imagem, idUsuario);
    }

//    @GetMapping("/sair")
//    public void logout() {
//
//    }
}
