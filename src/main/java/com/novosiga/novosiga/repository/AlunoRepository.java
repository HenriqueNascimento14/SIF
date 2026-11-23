package com.novosiga.novosiga.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.novosiga.novosiga.dto.AlunoCursoDTO;
import com.novosiga.novosiga.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

    @Query("""
            SELECT new com.novosiga.novosiga.dto.AlunoCursoDTO(
                a.nomeAluno,
                c.nomeCurso
            )
            FROM Aluno a
            JOIN a.curso c
            ORDER BY c.nomeCurso
            """)
    List<AlunoCursoDTO> listarAlunosPorCurso();

    @Query("""
            SELECT a FROM Aluno a
            WHERE (:q = ''
                OR locate(:q, translate(lower(a.nomeAluno), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0
                OR locate(:q, translate(lower(a.cpfAluno), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0
                OR locate(:q, translate(lower(a.cidadeAluno), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0)
            AND (:cursoId IS NULL OR a.curso.idCurso = :cursoId)
            """)
    List<Aluno> buscar(@Param("q") String q,
            @Param("cursoId") Integer cursoId,
            Sort sort);

}
