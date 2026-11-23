package com.novosiga.novosiga.config;

import java.util.List;

import com.novosiga.novosiga.model.Aluno;
import com.novosiga.novosiga.model.Curso;
import com.novosiga.novosiga.model.Disciplina;
import com.novosiga.novosiga.model.Produto;
import com.novosiga.novosiga.model.Professor;
import com.novosiga.novosiga.model.Usuario;
import com.novosiga.novosiga.repository.AlunoRepository;
import com.novosiga.novosiga.repository.CursoRepository;
import com.novosiga.novosiga.repository.DisciplinaRepository;
import com.novosiga.novosiga.repository.ProdutoRepository;
import com.novosiga.novosiga.repository.ProfessorRepository;
import com.novosiga.novosiga.service.UsuarioService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
@Profile("demo")
public class DadosDemonstracao {
    @Bean
    ApplicationRunner carregarDadosDemonstracao(CursoRepository cursos, ProfessorRepository professores,
                                                AlunoRepository alunos, DisciplinaRepository disciplinas,
                                                ProdutoRepository produtos, UsuarioService usuarios) {
        return args -> {
            var sistemas = cursos.save(new Curso(null, "Sistemas", "Tecnologia em Sistemas para Internet", "2400 h"));
            var logistica = cursos.save(new Curso(null, "Logística", "Gestão de operações e distribuição", "2400 h"));

            var helena = professores.save(new Professor(null, "Helena Costa", "00000000101", "000000101",
                    "11000000101", "Rua Exemplo, 10", "Mestrado", null, null));
            var andre = professores.save(new Professor(null, "André Lima", "00000000102", "000000102",
                    "11000000102", "Rua Exemplo, 20", "Doutorado", null, null));

            alunos.saveAll(List.of(
                    aluno("João Almeida", "São Paulo", "00000000001", sistemas),
                    aluno("João Ribeiro", "Sorocaba", "00000000002", logistica),
                    aluno("Marina Santos", "São Paulo", "00000000003", sistemas)
            ));

            disciplinas.saveAll(List.of(
                    new Disciplina(null, "Programação Web", "PW", "80 h", helena, sistemas),
                    new Disciplina(null, "Banco de Dados", "BD", "80 h", andre, sistemas),
                    new Disciplina(null, "Gestão de Estoque", "GE", "60 h", helena, logistica)
            ));

            produtos.saveAll(List.of(
                    produto("Mouse óptico", "Orbit", "M100", 49.90),
                    produto("Mouse sem fio", "Orbit", "M200", 129.90),
                    produto("Mouse ergonômico", "Nexo", "Ergo", 299.90),
                    produto("Monitor LED", "Nexo", "View", 500.00),
                    produto("Teclado compacto", "Orbit", "K100", 100.00)
            ));

            usuarios.save(new Usuario(null, "Conta de demonstração", "00000000000",
                    "demo", "SifDemo2026!", "ROLE_USER"));
        };
    }

    private Aluno aluno(String nome, String cidade, String cpf, Curso curso) {
        return new Aluno(null, nome, "Rua Exemplo, 1", "Centro", cidade, "SP",
                "00000000", "11000000000", cpf, null, null, curso);
    }

    private Produto produto(String descricao, String marca, String modelo, double valor) {
        return new Produto(null, descricao, marca, modelo, valor, null, null, null);
    }
}
