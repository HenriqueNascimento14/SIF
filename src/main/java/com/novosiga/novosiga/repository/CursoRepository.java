package com.novosiga.novosiga.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.novosiga.novosiga.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

    // Busca por texto (nome ou descricao). q = "" ignora a busca.
    @Query("""
            SELECT c FROM Curso c
            WHERE (:q = ''
                OR locate(:q, translate(lower(c.nomeCurso), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0
                OR locate(:q, translate(lower(c.descricaoCurso), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0)
            """)
    List<Curso> buscar(@Param("q") String q, Sort sort);
}
