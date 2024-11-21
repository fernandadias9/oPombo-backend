package com.opombo.service;

import com.opombo.auth.RSAMensagemEncoder;
import com.opombo.exception.OPomboException;
import com.opombo.model.dto.ListaMensagensDTO;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.filtro.MensagemFiltro;
import com.opombo.model.repository.MensagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private ImagemService imagemService;

    public Mensagem salvar(Mensagem mensagem, MultipartFile imagem) throws OPomboException {

        if (mensagem.getTexto().length() > 300) {
            throw new OPomboException("Mensagem pode ter no máximo 300 caracteres.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(imagem != null && !imagem.isEmpty()) {
            try {
                String imagemBase64 = imagemService.processarImagem(imagem);
                mensagem.setImagem(imagemBase64);
            } catch (Exception e) {
                throw new OPomboException("Erro ao processar a imagem.", HttpStatus.BAD_REQUEST);
            }
        }

        return mensagemRepository.save(mensagem);
    }

//    public Mensagem salvar(Mensagem mensagem) throws OPomboException {
//
//        if (mensagem.getTexto().length() > 300) {
//            throw new OPomboException("Mensagem pode ter no máximo 300 caracteres.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return mensagemRepository.save(mensagem);
//    }

    public Mensagem buscar(String id) throws OPomboException {

        Mensagem mensagem = mensagemRepository.findById(id).orElseThrow(() -> new OPomboException("Mensagem não encontrada!", HttpStatus.NOT_FOUND));

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

    @Transactional
    public void curtir(String idMensagem, Usuario usuario) throws OPomboException {
        Mensagem mensagem = buscar(idMensagem);
        if (mensagem != null) {
            Set<Usuario> usuariosQueCurtiram = mensagem.getUsuariosQueCurtiram();

            if (usuariosQueCurtiram.contains(usuario)) {
                usuariosQueCurtiram.remove(usuario);
            } else {
                usuariosQueCurtiram.add(usuario);
            }
            mensagemRepository.save(mensagem);
            mensagem.setQtdeLikes(usuariosQueCurtiram.size());
        } else {
            throw new OPomboException("Mensagem não encontrada", HttpStatus.BAD_REQUEST);
        }
    }

    public void bloquearOuDesbloquearMensagem(Mensagem mensagem) throws OPomboException {
        if (mensagem.getBloqueado() == false) {
            mensagem.setBloqueado(true);
        } else {
            mensagem.setBloqueado(false);
        }
        mensagemRepository.save(mensagem);
    }

    public Set<Usuario> obterUsuariosQueCurtiram(String idMensagem) throws OPomboException {
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

    public void salvarImagem(MultipartFile imagem, String idMensagem) throws OPomboException {

        Mensagem imagemMensage = mensagemRepository.findById(idMensagem).orElseThrow(() -> new OPomboException("Mensagem não encontrada", HttpStatus.NOT_FOUND));

        String imagemBase64 = imagemService.processarImagem(imagem);

        imagemMensage.setImagem(imagemBase64);

        mensagemRepository.save(imagemMensage);
    }
}
