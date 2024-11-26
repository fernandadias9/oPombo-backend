package com.opombo.service;

import com.opombo.exception.OPomboException;
import com.opombo.model.dto.DenunciaDTO;
import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.DenunciaPK;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.MotivoDaDenuncia;
import com.opombo.model.filtro.DenunciaFiltro;
import com.opombo.model.repository.DenunciaRepository;
import com.opombo.model.repository.MensagemRepository;
import com.opombo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Denuncia denunciarMensagem(DenunciaDTO denunciaDTO) {
        Mensagem mensagem = this.mensagemRepository.findById(denunciaDTO.getIdMensagem()).get();
        Usuario usuario = this.usuarioRepository.findById(denunciaDTO.getIdUsuario()).get();

        DenunciaPK denunciaPK = new DenunciaPK();
        denunciaPK.setIdMensagem(denunciaDTO.getIdMensagem());
        denunciaPK.setIdUsuario(denunciaDTO.getIdUsuario());  // O id do usuário autenticado

        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaPK);
        denuncia.setMensagem(mensagem);
        denuncia.setUsuario(usuario);
        denuncia.setMotivo(denunciaDTO.getMotivo());
        denuncia.setFoiAnalisada(false);

        return denunciaRepository.save(denuncia);
    }

    public Denuncia atualizarDenuncia(DenunciaPK id, boolean foiAnalisada) throws OPomboException {
        // Busca a denúncia pelo ID
        Optional<Denuncia> optionalDenuncia = denunciaRepository.findById(id);

        if (optionalDenuncia.isEmpty()) {
            throw new OPomboException("Denúncia não encontrada", HttpStatus.NOT_FOUND);
        }

        // Atualiza o campo foiAnalisada
        Denuncia denuncia = optionalDenuncia.get();
        denuncia.setFoiAnalisada(foiAnalisada);

        // Salva as alterações
        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> listar() {

        return denunciaRepository.findAll();
    }

    public List<DenunciaDTO> listarDTO() {
        return denunciaRepository.findAllDenunciasDTO();
    }

    public List<Denuncia> listarComFiltro(DenunciaFiltro filtros) {
        if(filtros.temPaginacao()) {
            int pageNumber = filtros.getPagina();
            int pageSize = filtros.getLimite();

            PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
            return denunciaRepository.findAll(filtros, pagina).toList();
        }
        return denunciaRepository.findAll(filtros);
    }

    public Denuncia buscarPorId(DenunciaPK id) {
        Optional<Denuncia> denuncia = denunciaRepository.findById(id);
        return denuncia.orElseThrow(() -> new RuntimeException("Denúncia não encontrada com o ID fornecido."));
    }

    public List<Denuncia> buscarDenunciasPorMensagem(String idMensagem) {
        return denunciaRepository.findByMensagemId(idMensagem);
    }
}
