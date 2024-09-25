package com.opombo.controller;

import com.opombo.model.dto.ListaMensagensDTO;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.filtro.MensagemFiltro;
import com.opombo.model.filtro.UsuarioFiltro;
import com.opombo.service.MensagemService;
import com.opombo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/mensagem")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Mensagem> salvar(@Valid @RequestBody Mensagem novaMensagem) {
        novaMensagem = mensagemService.salvar(novaMensagem);
        return ResponseEntity.ok(novaMensagem);
    }

    @GetMapping(path = "/{id}")
    public Mensagem buscar(@PathVariable String id) {
        return mensagemService.buscar(id);
    }

    @GetMapping
    public ResponseEntity<List<ListaMensagensDTO>> listarMensagens() {
        List<ListaMensagensDTO> mensagens = mensagemService.listarMensagens();
        return ResponseEntity.ok(mensagens);
    }

    @GetMapping("/todas")
    public List<Mensagem> listar() {
        return mensagemService.listar();
    }

    @PostMapping("/filtro")
    public List<Mensagem> pesquisarComFiltro(@RequestBody MensagemFiltro filtro) {
        return mensagemService.listarComFiltro(filtro);
    }

    @PutMapping(path = "/curtir/{idUsuario}/{idMensagem}")
    public ResponseEntity<Void> curtir(@PathVariable String idUsuario, @PathVariable String idMensagem) {
        Usuario usuario = this.usuarioService.buscar(idUsuario);
        mensagemService.curtir(idMensagem, usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/curtidas/{id}")
    public ResponseEntity<Set<Usuario>> obterUsuariosQueCurtiram(@PathVariable String id) {
        Set<Usuario> usuarios = mensagemService.obterUsuariosQueCurtiram(id);
        return ResponseEntity.ok(usuarios);
    }
    
    @PutMapping(path = "/bloqueio/{id}")
    public ResponseEntity<Void> bloquear(@PathVariable String id) {
        Mensagem mensagem = buscar(id);
        mensagemService.bloquearOuDesbloquearMensagem(mensagem);
        return ResponseEntity.ok().build();
    }
}
