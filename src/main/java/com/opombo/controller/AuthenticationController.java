package com.opombo.controller;

import com.opombo.auth.AuthenticationService;
import com.opombo.exception.OPomboException;
import com.opombo.model.entity.Usuario;
import com.opombo.model.enums.TipoDeUsuario;
import com.opombo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método de login padronizado -> Basic Auth
     *
     *  O parâmetro Authentication já encapsula login (username) e senha (password)
     *  Basic <Base64 encoded username and password>
     * @param authentication
     * @return o JWT gerado
     */
    @PostMapping("authenticate")
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }

    @PostMapping("/novo-usuario")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void registrarUsuario(@RequestBody Usuario novoUsuario) throws OPomboException {
        String senhaCifrada = passwordEncoder.encode(novoUsuario.getSenha());

        novoUsuario.setSenha(senhaCifrada);
        novoUsuario.setTipo(TipoDeUsuario.USUARIO);

        usuarioService.salvar(novoUsuario);

//        String salt = authenticationService.generateSalt();
//
//        String senhaComSalt = salt + novoUsuario.getSenha();
//
//        String senhaCifrada = passwordEncoder.encode(senhaComSalt);
//
//        novoUsuario.setSalt(salt);
//        novoUsuario.setSenha(senhaCifrada);
//        novoUsuario.setTipo(TipoDeUsuario.USUARIO);
//
//        usuarioService.salvar(novoUsuario);
    }
}
