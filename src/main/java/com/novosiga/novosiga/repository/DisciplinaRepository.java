package com.novosiga.novosiga.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.novosiga.novosiga.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {

    // Busca por texto (nome ou sigla) e filtros opcionais por professor e curso.
    @Query("""
            SELECT d FROM Disciplina d
            WHERE (:q = ''
                OR locate(:q, translate(lower(d.nomeDisciplina), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0
                OR locate(:q, translate(lower(d.siglaDisciplina), 'áàâãäéèêëíìîïóòôõöúùûüçñ', 'aaaaaeeeeiiiiooooouuuucn')) > 0)
            AND (:professorId IS NULL OR d.professor.idProfessor = :professorId)
            AND (:cursoId IS NULL OR d.curso.idCurso = :cursoId)
            """)
    List<Disciplina> buscar(@Param("q") String q,
                            @Param("professorId") Integer professorId,
                            @Param("cursoId") Integer cursoId,
                            Sort sort);
}
