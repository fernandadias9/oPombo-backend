package com.opombo.model.filtro;

import com.opombo.model.entity.Usuario;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UsuarioFiltro extends BaseFiltro implements Specification<Usuario> {

    private String nome;
    private String email;
    private String cpf;
    private String tipoDeUsuario;

    public boolean temFiltro() {
        return  (filtroValido(this.nome))
                || (filtroValido(this.email))
                || (filtroValido(this.cpf))
                || (filtroValido(this.tipoDeUsuario));
    }

    @Override
    public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if(filtroValido(this.getNome())) {
            predicates.add(cb.like(root.get("nome"), "%" + this.getNome() + "%"));
        }

        if(filtroValido(this.getEmail())) {
            predicates.add(cb.like(root.get("email"), "%" + this.getEmail() + "%"));
        }

        if(filtroValido(this.getCpf())) {
            predicates.add(cb.like(root.get("cpf"), "%" + this.getCpf() + "%"));
        }

        if(filtroValido(this.getTipoDeUsuario())) {
            predicates.add(cb.like(root.get("tipoDeUsuario()"), "%" + this.getTipoDeUsuario() + "%"));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
