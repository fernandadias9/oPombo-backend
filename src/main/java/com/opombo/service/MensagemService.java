package com.opombo.service;

import com.opombo.model.dto.ListaMensagensDTO;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.filtro.MensagemFiltro;
import com.opombo.model.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private DenunciaService denunciaService;

    public Mensagem salvar(Mensagem mensagem) {
        return mensagemRepository.save(mensagem);
    }

    public Mensagem buscar(String id) {
        return mensagemRepository.findById(id).get();
    }

    public List<Mensagem> listar() {
        return mensagemRepository.findAll();
    }

    public List<Mensagem> listarComFiltro(MensagemFiltro filtros) {
        if (filtros.temPaginacao()) {
            int pageNumber = filtros.getPagina();
            int pageSize = filtros.getLimite();
            PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
            return mensagemRepository.findAll(filtros, pagina).toList();
        }
        return mensagemRepository.findAll(filtros);
    }

    public void curtir(String idMensagem, Usuario usuario) {
        Mensagem mensagem = buscar(idMensagem);
        if (mensagem != null) {
            Set<Usuario> usuariosQueCurtiram = mensagem.getUsuariosQueCurtiram();
            if (usuariosQueCurtiram.contains(usuario)) {
                usuariosQueCurtiram.remove(usuario);
            } else {
                usuariosQueCurtiram.add(usuario);
            }
            mensagem.setQtdeLikes(usuariosQueCurtiram.size());
            salvar(mensagem);
        }
    }

    public void bloquearOuDesbloquearMensagem(Mensagem mensagem) {
        if (mensagem.getBloqueado() == false) {
            mensagem.setBloqueado(true);
        } else {
            mensagem.setBloqueado(false);
        }
        salvar(mensagem);
    }

    public Set<Usuario> obterUsuariosQueCurtiram(String idMensagem) {
        Mensagem mensagem = buscar(idMensagem);
        if (mensagem != null) {
            return mensagem.getUsuariosQueCurtiram();
        }
        return new HashSet<>();
    }

    public List<ListaMensagensDTO> listarMensagens() {
        List<Mensagem> mensagens = mensagemRepository.findAll();

        return mensagens.stream().map(mensagem -> {
            int quantidadeDenuncias = denunciaService.buscarDenunciasPorMensagem(mensagem.getId()).size();
            if (mensagem.getBloqueado()) {
                mensagem.setTexto("Bloqueada pelo administrador.");
            }
            return new ListaMensagensDTO(
                    mensagem.getId(),
                    mensagem.getTexto(),
                    mensagem.getQtdeLikes(),
                    mensagem.getPublicador().getId(),
                    mensagem.getPublicador().getNome(),
                    quantidadeDenuncias
            );
        }).collect(Collectors.toList());
    }
}
