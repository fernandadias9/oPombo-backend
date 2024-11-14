package com.opombo.service;

import com.opombo.auth.AuthenticationService;
import com.opombo.exception.OPomboException;
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

import java.util.List;
import java.util.Set;


@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado" + username)
                );
    }

    public Usuario salvar(Usuario usuario) throws OPomboException {
        if(usuarioRepository.findByCpf(usuario.getCpf()) != null) {
            throw new OPomboException("CPF já cadastrado na base de dados.", HttpStatus.BAD_REQUEST);
        }

        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new OPomboException("Email já cadastrado na base de dados.", HttpStatus.BAD_REQUEST);
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscar(String id) {
        return usuarioRepository.findById(id).get();
    }

    public List<Usuario> listar() throws OPomboException {
//        if(!authenticationService.getUsuarioAutenticado().getTipo().equals(TipoDeUsuario.ADMINISTRADOR)) {
//            throw new OPomboException("Usuário não autorizado", HttpStatus.UNAUTHORIZED);
//        }
        return usuarioRepository.findAll();
    }

    public Set<Mensagem> obterMensagensCurtidas(String idUsuario) {
        Usuario usuario = buscar(idUsuario);
        return usuario.getMensagensCurtidas();
    }

    public List<Usuario> listarComFiltro(UsuarioFiltro filtros) {
        if(filtros.temPaginacao()) {
            int pageNumber = filtros.getPagina();
            int pageSize = filtros.getLimite();

            PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
            return usuarioRepository.findAll(filtros, pagina).toList();
        }
        return usuarioRepository.findAll(filtros);
    }
}
