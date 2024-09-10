package com.opombo.controller;

import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.filtro.UsuarioFiltro;
import com.opombo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> salvar(@Valid @RequestBody Usuario novoUsuario) throws OPomboException {
        novoUsuario = usuarioService.salvar(novoUsuario);
        return ResponseEntity.ok(novoUsuario);
    }

    @PutMapping
    public ResponseEntity<Usuario> atualizar(@Valid @RequestBody Usuario usuario) {
        usuario = usuarioService.atualizar(usuario);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping(path = "/{id}")
    public  Usuario buscar(@PathVariable String id) {
        return usuarioService.buscar(id);
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @PostMapping("/filtro")
    public List<Usuario> pesquisarComFiltro(@RequestBody UsuarioFiltro filtro) {
        return usuarioService.listarComFiltro(filtro);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mensagensCurtidas/{id}")
    public ResponseEntity<Set<Mensagem>> obterMensagensCurtidas(@PathVariable String id) {
        Set<Mensagem> mensagensCurtidas = usuarioService.obterMensagensCurtidas(id);
        return ResponseEntity.ok(mensagensCurtidas);
    }
}
