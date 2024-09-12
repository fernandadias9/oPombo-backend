package com.opombo.controller;

import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.MotivoDaDenuncia;
import com.opombo.model.filtro.DenunciaFiltro;
import com.opombo.model.filtro.MensagemFiltro;
import com.opombo.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/denuncia")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @PostMapping
    public ResponseEntity<Denuncia> denunciarMensagem(
            @RequestParam("idMensagem") String idMensagem,
            @RequestParam("idUsuario") String idUsuario,
            @RequestParam("motivo") MotivoDaDenuncia motivo
    ) {

        Mensagem mensagem = new Mensagem();
        mensagem.setId(idMensagem);

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);

        Denuncia denuncia = denunciaService.denunciarMensagem(mensagem, usuario, motivo);

        return ResponseEntity.ok(denuncia);
    }

    @GetMapping(path = "/{id}")
    public Denuncia buscar(@PathVariable String id) {
        return denunciaService.buscar(id);
    }

    @GetMapping
    public List<Denuncia> listar() {
        return denunciaService.listar();
    }

    @PostMapping("/filtro")
    public List<Denuncia> pesquisarComFiltro(@RequestBody DenunciaFiltro filtro) {
        return denunciaService.listarComFiltro(filtro);
    }
}
