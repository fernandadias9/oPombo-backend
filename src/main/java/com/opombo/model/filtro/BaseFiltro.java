package com.opombo.model.filtro;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public abstract class BaseFiltro {

    private int pagina;
    private int limite;

    public BaseFiltro() {
        this.limite = 0;
        this.pagina = 0;
    }

    public boolean temPaginacao() {
        return this.limite > 0 && this.pagina > 0;
    }

    public boolean filtroValido(String filtro) {
        return filtro != null && !filtro.isBlank() && !filtro.isEmpty();
    }

    public static void aplicarFiltroIntervalo(Root root, CriteriaBuilder cb, List<Predicate> predicates, Integer valorMinimo, Integer valorMaximo, String nomeAtributo) {
        if (valorMinimo != null && valorMaximo != null) {
            predicates.add(cb.between(root.get(nomeAtributo), valorMinimo, valorMaximo));
        } else if (valorMinimo != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(nomeAtributo), valorMinimo));
        } else if (valorMaximo != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(nomeAtributo), valorMaximo));
        }
    }

    public static void aplicarFiltroPeriodo(Root root, CriteriaBuilder cb, List<Predicate> predicates, LocalDate dataInicial, LocalDate dataFinal, String nomeAtributo) {
        if (dataInicial != null && dataFinal != null) {
            // Ambas as datas fornecidas
            LocalDateTime inicioDoDia = dataInicial.atStartOfDay();
            LocalDateTime finalDoDia = dataFinal.atTime(23, 59, 59, 999999999);
            predicates.add(cb.between(root.get(nomeAtributo), inicioDoDia, finalDoDia));
        } else if (dataInicial != null) {
            // Apenas dataInicial fornecida
            LocalDateTime inicioDoDia = dataInicial.atStartOfDay();
            predicates.add(cb.greaterThanOrEqualTo(root.get(nomeAtributo), inicioDoDia));
        } else if (dataFinal != null) {
            // Apenas dataFinal fornecida
            LocalDateTime finalDoDia = dataFinal.atTime(23, 59, 59, 999999999);
            predicates.add(cb.lessThanOrEqualTo(root.get(nomeAtributo), finalDoDia));
        }
    }
}
