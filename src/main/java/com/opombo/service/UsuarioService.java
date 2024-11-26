package com.opombo.service;

import com.opombo.auth.RSAPasswordEncoder;
import com.opombo.exception.OPomboException;
import com.opombo.model.dto.UsuarioDTO;
import com.opombo.model.entity.Mensagem;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import com.opombo.model.filtro.UsuarioFiltro;
import com.opombo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;


@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private RSAPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado" + username)
                );
    }

    public Usuario salvar(Usuario usuario) throws OPomboException {
        if (usuarioRepository.findByCpf(usuario.getCpf()) != null) {
            throw new OPomboException("CPF já cadastrado na base de dados.", HttpStatus.BAD_REQUEST);
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new OPomboException("Email já cadastrado na base de dados.", HttpStatus.BAD_REQUEST);
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(UsuarioDTO usuarioDTO) throws OPomboException {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new OPomboException("Usuário não encontrado", HttpStatus.BAD_REQUEST));

        if (usuarioDTO.getNome() != null) {
            usuarioExistente.setNome(usuarioDTO.getNome());
        }
        if (usuarioDTO.getEmail() != null) {
            usuarioExistente.setEmail(usuarioDTO.getEmail());
        }
        if (usuarioDTO.getSenha() != null) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }
        if (!usuarioDTO.getFotoPerfil().isEmpty()) {
            usuarioExistente.setFotoPerfil(usuarioDTO.getFotoPerfil());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario buscar(String id) {

        return usuarioRepository.findById(id).get();
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public void excluir(String id) {
        usuarioRepository.deleteById(id);
    }

    public Set<Mensagem> obterMensagensCurtidas(String idUsuario) throws OPomboException {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() ->
                new OPomboException("Usuário não encontrado", HttpStatus.NOT_FOUND));
        return usuario.getMensagensCurtidas();
    }

    public List<Usuario> listarComFiltro(UsuarioFiltro filtros) {
        if (filtros.temPaginacao()) {
            int pageNumber = filtros.getPagina();
            int pageSize = filtros.getLimite();

            PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
            return usuarioRepository.findAll(filtros, pagina).toList();
        }
        return usuarioRepository.findAll(filtros);
    }

    public void salvarFotoPerfil(String imagemBase64, String idUsuario) throws OPomboException {

        Usuario usuarioFotoPerfil = usuarioRepository.findById(idUsuario).orElseThrow(() ->
                new OPomboException("Usuário não encontrado", HttpStatus.INTERNAL_SERVER_ERROR));

        usuarioFotoPerfil.setFotoPerfil(imagemBase64);

        usuarioRepository.save(usuarioFotoPerfil);
    }
}
