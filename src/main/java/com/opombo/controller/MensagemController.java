package com.opombo.controller;

import com.opombo.auth.AuthenticationService;
import com.opombo.exception.OPomboException;
import com.opombo.model.dto.ListaMensagensDTO;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.filtro.MensagemFiltro;
import com.opombo.service.MensagemService;
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
@RequestMapping(path = "api/mensagem")
@CrossOrigin(origins = "http://localhost:4200")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    public ResponseEntity<Mensagem> salvar(@Valid @RequestBody Mensagem novaMensagem) throws OPomboException {
        novaMensagem = mensagemService.salvar(novaMensagem);
        return ResponseEntity.ok(novaMensagem);
    }

    @GetMapping(path = "/{id}")
    public Mensagem buscar(@PathVariable String id) throws OPomboException {
        return mensagemService.buscar(id);
    }

    @GetMapping
    public ResponseEntity<List<ListaMensagensDTO>> listarMensagens() {
        List<ListaMensagensDTO> mensagens = mensagemService.listarMensagens();
        return ResponseEntity.ok(mensagens);
    }

    @PostMapping("/filtro")
    public List<Mensagem> pesquisarComFiltro(@RequestBody MensagemFiltro filtro) {
        return mensagemService.listarComFiltro(filtro);
    }

    @PutMapping(path = "/curtir/{idUsuario}/{idMensagem}")
    public ResponseEntity<Void> curtir(@PathVariable String idUsuario, @PathVariable String idMensagem) throws OPomboException {
        Usuario usuario = this.usuarioService.buscar(idUsuario);
        mensagemService.curtir(idMensagem, usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/curtidas/{id}")
    public ResponseEntity<Set<Usuario>> obterUsuariosQueCurtiram(@PathVariable String id) throws OPomboException {
        Set<Usuario> usuarios = mensagemService.obterUsuariosQueCurtiram(id);
        return ResponseEntity.ok(usuarios);
    }
    
    @PutMapping(path = "/bloqueio/{id}")
    public ResponseEntity<Void> bloquear(@PathVariable String id) throws OPomboException {
        Mensagem mensagem = buscar(id);
        mensagemService.bloquearOuDesbloquearMensagem(mensagem);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public void fazerUploadFotoPerfil(@RequestParam("imagem") MultipartFile imagem, String idMensagem) throws OPomboException {

        if(imagem == null) {
            throw new OPomboException("Arquivo inválido.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
        if (usuarioAutenticado == null ) {
            throw new OPomboException("Usuário não encontrado.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        mensagemService.salvarImagem(imagem, idMensagem);
    }

}
