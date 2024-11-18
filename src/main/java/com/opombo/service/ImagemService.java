package com.opombo.service;

import com.opombo.exception.OPomboException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImagemService {

    public String processarImagem(MultipartFile file) throws OPomboException {
        byte[] imagemBytes;

        try {
            imagemBytes = file.getBytes();
        } catch (IOException e) {
            throw new OPomboException("Erro ao processar imagem", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return Base64.getEncoder().encodeToString(imagemBytes);
    }
}
