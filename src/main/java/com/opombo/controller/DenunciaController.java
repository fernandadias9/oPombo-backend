package com.opombo.controller;

import com.opombo.model.dto.DenunciaDTO;
import com.opombo.model.entity.Denuncia;
import com.opombo.model.filtro.DenunciaFiltro;
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
    public ResponseEntity<Denuncia> denunciarMensagem(@Valid @RequestBody DenunciaDTO denunciaDTO) {
        Denuncia denuncia = denunciaService.denunciarMensagem(denunciaDTO);
        return ResponseEntity.ok(denuncia);
    }

    @GetMapping(path = "/{id}")
    public Denuncia buscar(@PathVariable String id) {
        return denunciaService.buscar(id);
    }

    @GetMapping
    public List<DenunciaDTO> listarDTO() { return denunciaService.listarDTO(); }

    @GetMapping("/todas")
    public List<Denuncia> listar() {
        return denunciaService.listar();
    }

    @PostMapping("/filtro")
    public List<Denuncia> pesquisarComFiltro(@RequestBody DenunciaFiltro filtro) {
        return denunciaService.listarComFiltro(filtro);
    }

    @GetMapping("/mensagem/{idMensagem}")
    public ResponseEntity<List<Denuncia>> buscarDenunciasPorMensagem(@PathVariable String idMensagem) {
        List<Denuncia> denuncias = denunciaService.buscarDenunciasPorMensagem(idMensagem);
        return ResponseEntity.ok(denuncias);
    }
}
