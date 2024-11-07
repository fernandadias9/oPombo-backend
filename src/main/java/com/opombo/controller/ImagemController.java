package com.opombo.controller;

import com.opombo.exception.OPomboException;
import com.opombo.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/imagem")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            String base64 = imagemService.processarImagem(file);
            return ResponseEntity.ok(base64);
        } catch (OPomboException e) {
            return ResponseEntity.status(500).body("Erro ao processar imagem: " + e.getMessage());
        }
    }
}
