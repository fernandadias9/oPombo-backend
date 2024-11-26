package com.opombo.model.filtro;

import com.opombo.model.entity.Denuncia;
import com.opombo.model.entity.Denuncia;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DenunciaFiltro extends BaseFiltro implements Specification<Denuncia> {

    private String texto;
    private String idDenunciante;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private String motivoDaDenuncia;
    private Boolean foiAnalisada;

    public boolean temFiltro() {
        return  (filtroValido(this.texto))
                || (filtroValido(this.idDenunciante))
                || (dataInicial != null)
                || (dataFinal != null)
                || (filtroValido(motivoDaDenuncia))
                || foiAnalisada;
    }

    @Override
    public Predicate toPredicate(Root<Denuncia> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if(this.getTexto() != null && this.getTexto().trim().length() > 0) {
            predicates.add(cb.like(root.join("mensagem").get("texto"), "%" + this.getTexto() + "%"));
        }

        if(this.getIdDenunciante() != null && this.getIdDenunciante().trim().length() > 0) {
            predicates.add(cb.like(root.get("usuario").get("id"), "%" + this.getIdDenunciante() + "%"));
        }

        aplicarFiltroPeriodo(root, cb, predicates, this.getDataInicial(), this.getDataFinal(), "data");

        if(this.getMotivoDaDenuncia() != null && this.getMotivoDaDenuncia().trim().length() > 0) {
            predicates.add(cb.like(root.get("motivo"), "%" + this.getMotivoDaDenuncia() + "%"));
        }

        if(this.getFoiAnalisada() != null) {
            predicates.add(cb.equal(root.get("foiAnalisada"), this.getFoiAnalisada()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
