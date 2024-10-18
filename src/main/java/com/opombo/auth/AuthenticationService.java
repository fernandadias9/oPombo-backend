package com.opombo.auth;

import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

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

            if (principal instanceof Usuario) {
                UserDetails userDetails = (Usuario) principal;
                usuarioAutenticado = (Usuario) userDetails;
            }
        }

        if (usuarioAutenticado == null) {
            throw new OPomboException("Usuário não encontrado", HttpStatus.UNAUTHORIZED);
        }
        return usuarioAutenticado;
    }
}
