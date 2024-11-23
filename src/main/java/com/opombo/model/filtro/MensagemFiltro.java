package com.opombo.model.filtro;

import com.opombo.model.entity.Mensagem;
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
public class MensagemFiltro extends BaseFiltro implements Specification<Mensagem> {

    private String texto;
    private String nomePublicador;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    public boolean temFiltro() {
        return  (filtroValido(this.texto))
                || (filtroValido(this.nomePublicador))
                || (dataInicial != null)
                || (dataFinal != null);
    }

    @Override
    public Predicate toPredicate(Root<Mensagem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if(this.getTexto() != null && this.getTexto().trim().length() > 0) {
            predicates.add(cb.like(root.get("texto"), "%" + this.getTexto() + "%"));
        }

        if(this.getNomePublicador() != null && this.getNomePublicador().trim().length() > 0) {
            //Predicado --> operador (comparação), atributo/coluna, valor
            //Forma 1: usando somente get
            predicates.add(cb.like(root.get("publicador").get("nome"), "%" + this.getNomePublicador() + "%"));
        }

        aplicarFiltroPeriodo(root, cb, predicates, this.getDataInicial(), this.getDataFinal(), "criadoEm");

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
