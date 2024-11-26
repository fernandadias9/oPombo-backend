package com.opombo.model.repository;

import com.opombo.model.dto.DenunciaDTO;
import com.opombo.model.dto.ListaDenunciasDTO;
import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.DenunciaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, DenunciaPK>, JpaSpecificationExecutor<Denuncia> {

    @Query("SELECT new com.opombo.model.dto.DenunciaDTO(" +
            "d.mensagem.id, " +
            "d.usuario.id, " +
            "d.motivo, " +
            "d.data, " +  // dataDenuncia
            "d.usuario.nome, " +  // nomeDenunciante
            "d.mensagem.texto) " +  // conteudoMensagem
            "FROM Denuncia d")
    List<DenunciaDTO> findAllDenunciasDTO();

    @Query("SELECT d FROM Denuncia d WHERE d.mensagem.id = :idMensagem")
    List<Denuncia> findByMensagemId(@Param("idMensagem") String idMensagem);
}
