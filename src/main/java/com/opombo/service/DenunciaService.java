package com.opombo.service;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Denuncia denunciarMensagem(String idMensagem, String idUsuario, MotivoDaDenuncia motivo) {
        Mensagem mensagem = this.mensagemRepository.findById(idMensagem).get();
        Usuario usuario = this.usuarioRepository.findById(idUsuario).get();

        DenunciaPK denunciaPK = new DenunciaPK();
        denunciaPK.setIdMensagem(idMensagem);
        denunciaPK.setIdUsuario(idUsuario);

        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaPK);
        denuncia.setMensagem(mensagem);
        denuncia.setUsuario(usuario);
        denuncia.setMotivo(motivo);
        denuncia.setFoiAnalisada(false);
        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> listar() {
        return denunciaRepository.findAll();
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

    public Denuncia buscar(String id) {
        return denunciaRepository.findById(id).get();
    }
}
