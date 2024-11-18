package com.opombo.auth;

import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Usuario;
import com.opombo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.getGenerateToken(authentication);
    }

    public Usuario getUsuarioAutenticado() throws OPomboException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioAutenticado = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            Jwt jwt = (Jwt) principal;
            String login = jwt.getClaim("sub");

            usuarioAutenticado = usuarioRepository.findByEmail(login)
                    .orElseThrow(() -> new OPomboException("Usuário não encontrado", HttpStatus.UNAUTHORIZED));
        }

        if(usuarioAutenticado == null) {
            throw new OPomboException("Usuário não encontrado", HttpStatus.UNAUTHORIZED);
        }
        return usuarioAutenticado;
    }
}
