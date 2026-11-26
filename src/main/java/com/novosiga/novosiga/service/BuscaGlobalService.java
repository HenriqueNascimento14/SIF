package com.novosiga.novosiga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novosiga.novosiga.dto.SugestaoBusca;
import com.novosiga.novosiga.model.Aluno;
import com.novosiga.novosiga.model.Curso;
import com.novosiga.novosiga.model.Disciplina;
import com.novosiga.novosiga.model.Produto;
import com.novosiga.novosiga.model.Professor;

@Service
public class BuscaGlobalService {

    private static final int LIMITE_POR_TIPO = 4;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProdutoService produtoService;

    @Transactional(readOnly = true)
    public List<SugestaoBusca> buscar(String q) {
        String termo = TextoBusca.normalizar(q);
        List<SugestaoBusca> sugestoes = new ArrayList<>();
        if (termo.isBlank()) {
            return sugestoes;
        }

        alunoService.buscar(termo, null, Sort.by("nomeAluno")).stream()
                .limit(LIMITE_POR_TIPO)
                .forEach(aluno -> sugestoes.add(deAluno(aluno)));

        cursoService.buscar(termo, Sort.by("nomeCurso")).stream()
                .limit(LIMITE_POR_TIPO)
                .forEach(curso -> sugestoes.add(deCurso(curso)));

        disciplinaService.buscar(termo, null, null, Sort.by("nomeDisciplina")).stream()
                .limit(LIMITE_POR_TIPO)
                .forEach(disciplina -> sugestoes.add(deDisciplina(disciplina)));

        professorService.buscar(termo, null, Sort.by("nomeProfessor")).stream()
                .limit(LIMITE_POR_TIPO)
                .forEach(professor -> sugestoes.add(deProfessor(professor)));

        produtoService.buscar(termo, null, null, null, Sort.by("descricaoProduto")).stream()
                .limit(LIMITE_POR_TIPO)
                .forEach(produto -> sugestoes.add(deProduto(produto)));

        return sugestoes;
    }

    private static SugestaoBusca deAluno(Aluno aluno) {
        String curso = aluno.getCurso() != null ? aluno.getCurso().getNomeCurso() : aluno.getCidadeAluno();
        return new SugestaoBusca(
                "Aluno",
                aluno.getNomeAluno(),
                curso,
                "/alunos/editar/" + aluno.getIdAluno());
    }

    private static SugestaoBusca deCurso(Curso curso) {
        return new SugestaoBusca(
                "Curso",
                curso.getNomeCurso(),
                curso.getDescricaoCurso(),
                "/cursos/editar/" + curso.getIdCurso());
    }

    private static SugestaoBusca deDisciplina(Disciplina disciplina) {
        String detalhe = disciplina.getSiglaDisciplina();
        if (disciplina.getCurso() != null) {
            detalhe = detalhe + " · " + disciplina.getCurso().getNomeCurso();
        }
        return new SugestaoBusca(
                "Disciplina",
                disciplina.getNomeDisciplina(),
                detalhe,
                "/disciplinas/editar/" + disciplina.getIdDisciplina());
    }

    private static SugestaoBusca deProfessor(Professor professor) {
        return new SugestaoBusca(
                "Professor",
                professor.getNomeProfessor(),
                professor.getGraduacaoProfessor(),
                "/professores/editar/" + professor.getIdProfessor());
    }

    private static SugestaoBusca deProduto(Produto produto) {
        return new SugestaoBusca(
                "Produto",
                produto.getDescricaoProduto(),
                produto.getMarcaProduto() + " · " + produto.getModeloProduto(),
                "/produtos/editar/" + produto.getIdProduto());
    }
}
