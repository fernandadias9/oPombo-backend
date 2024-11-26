package com.opombo.controller;

import com.opombo.auth.AuthenticationService;
import com.opombo.exception.OPomboException;
import com.opombo.model.dto.DenunciaDTO;
import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import com.opombo.model.filtro.DenunciaFiltro;
import com.opombo.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/denuncia")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    public ResponseEntity<Denuncia> denunciarMensagem(@Valid @RequestBody DenunciaDTO denunciaDTO) throws OPomboException {
        Usuario usuario = authService.getUsuarioAutenticado();
        denunciaDTO.setIdUsuario(usuario.getId());
        Denuncia denuncia = denunciaService.denunciarMensagem(denunciaDTO);
        return ResponseEntity.ok(denuncia);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Denuncia> atualizar(
            @RequestBody @Valid Denuncia denuncia) throws OPomboException {
        verificarPermissaoAdmin();
        Denuncia denunciaAtualizada = denunciaService.atualizar(denuncia);
        return ResponseEntity.ok(denunciaAtualizada);
    }

    @GetMapping(path = "/{id}")
    public Denuncia buscar(@PathVariable String id) {
        return denunciaService.buscar(id);
    }

    @GetMapping
    public List<DenunciaDTO> listarDTO() throws OPomboException {
        verificarPermissaoAdmin();
        return denunciaService.listarDTO();
    }

    @GetMapping("/todas")
    public List<Denuncia> listar() throws OPomboException {
        verificarPermissaoAdmin();
        return denunciaService.listar();
    }

    @PostMapping("/filtro")
    public List<Denuncia> pesquisarComFiltro(@RequestBody DenunciaFiltro filtro) throws OPomboException {
        verificarPermissaoAdmin();
        return denunciaService.listarComFiltro(filtro);
    }

    @GetMapping("/mensagem/{idMensagem}")
    public ResponseEntity<List<Denuncia>> buscarDenunciasPorMensagem(@PathVariable String idMensagem) {
        List<Denuncia> denuncias = denunciaService.buscarDenunciasPorMensagem(idMensagem);
        return ResponseEntity.ok(denuncias);
    }

    /**
     * Método para verificar se o usuário autenticado é administrador.
     * Lança uma exceção caso contrário.
     */
    private void verificarPermissaoAdmin() throws OPomboException {
        Usuario usuario = authService.getUsuarioAutenticado();
        if (usuario.getTipo() != TipoDeUsuario.ADMINISTRADOR) {
            throw new OPomboException("Usuário não é administrador.", HttpStatus.BAD_REQUEST);
        }
    }
}
