# SIF — Sistema Interno da Fatec

O SIF é o sistema que usamos para gerenciar alunos, cursos, disciplinas, professores, produtos e pedidos. Para a avaliação de Encontrabilidade, a gente trabalhou em duas coisas que você vai encontrar nas listagens: **o campo de pesquisa e os filtros**.

## Integrantes

- Arthur Risso Pereira Rodovalho
- Henrique Silva do Nascimento
- Joao Pedro Rodrigues Monção

## Como rodar o SIF no seu computador

Se você só quer abrir o sistema e testar, comece pelo **modo demonstração**. Ele já vem com uma conta de teste e alguns registros fictícios. Você vai precisar do **JDK 17 ou superior**. O Maven Wrapper já vem no projeto e baixa as dependências na primeira vez que você rodar.

Extraia o ZIP ou clone o repositório. Depois, abra o terminal na pasta onde está o arquivo `pom.xml` e use o comando do seu sistema.

**Windows (PowerShell)**

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

**Linux ou macOS**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

Quando o sistema iniciar, deixe o terminal aberto e acesse [http://127.0.0.1:8080/login](http://127.0.0.1:8080/login). Entre com o usuário **`demo`** e a senha **`SifDemo2026!`**.

Nesse modo, os dados ficam em memória no H2. Se você cadastrar ou alterar alguma coisa, pode testar normalmente. Quando reiniciar o sistema, ele volta aos registros de exemplo.

Se a porta 8080 já estiver em uso, acrescente `"-Dspring-boot.run.arguments=--server.port=8081"` ao final do comando e abra [http://127.0.0.1:8081/login](http://127.0.0.1:8081/login).

## O que a gente fez para a avaliação

### Tópico 1 — Campo de pesquisa

Imagina que você quer encontrar um aluno, mas só lembra o primeiro nome. Na tela de **Alunos**, basta digitar parte do nome. Se você escrever `joao`, por exemplo, a busca encontra **João** mesmo sem o acento. A lista muda enquanto você digita e mostra quantos resultados encontrou. Se o JavaScript estiver desativado, é só usar o botão **Buscar**.

### Tópico 3 — Filtros de pesquisa

Agora imagina que apareceram dois alunos chamados João e você sabe que um deles faz **Logística**. Dá para manter `joao` no campo de pesquisa e selecionar o curso no filtro. A lista passa a mostrar só os alunos que atendem aos dois critérios. Você também pode usar os filtros com o campo de pesquisa vazio.

| Tela | O que dá para pesquisar | Filtros |
| --- | --- | --- |
| Alunos | Nome, CPF ou cidade | Curso |
| Cursos | Nome ou descrição | — |
| Disciplinas | Nome ou sigla | Curso e professor |
| Professores | Nome ou CPF | Graduação |
| Produtos | Descrição, marca ou modelo | Marca e faixa de preço |

Os cursos, professores, graduações e marcas que aparecem nos filtros vêm dos cadastros. As faixas de preço já estão definidas para você escolher. Se quiser voltar à lista completa, clique em **Limpar busca**. Se quiser abrir a mesma busca depois, copie o endereço da página. O texto e os filtros ficam na URL.

## Um teste rápido para ver os dois recursos funcionando

Entre com a conta `demo` e abra **Produtos**. Faça os passos na ordem, mantendo o que você já selecionou:

1. Digite `MOUSE`. Devem aparecer **3 resultados**.
2. Selecione a marca **Orbit**. Agora devem aparecer **2 resultados**.
3. Escolha **De R$ 100 a R$ 499,99** na faixa de preço. Deve sobrar só o **Mouse sem fio**.
4. Troque `MOUSE` por `xyz`. Vai aparecer uma mensagem de que a busca não encontrou resultados. Clique em **Limpar busca** para voltar aos **5 produtos iniciais**.

Para testar nas telas acadêmicas, pesquise `joao` em **Alunos** e selecione **Logística**. Deve sobrar apenas **João Ribeiro**. Em **Disciplinas**, selecione **Sistemas** e **Helena Costa**. O resultado esperado é **Programação Web**.

## O que acontece no código quando você pesquisa

Quando você digita ou muda um filtro, a página envia os critérios para o Spring Boot em uma requisição `GET`. O controlador recebe esses dados, o serviço prepara o texto da busca e o repositório pesquisa no banco usando os critérios juntos. O Thymeleaf monta a lista, e o JavaScript troca só a parte dos resultados na tela.

Usamos **Java 17, Spring Boot, Spring MVC, Spring Data JPA e Spring Security** no backend, **Thymeleaf, HTML, CSS e JavaScript** nas telas, **PostgreSQL e H2** como bancos e **Maven** para lidar com dependências, compilação e testes.

## Se você quiser rodar com PostgreSQL

Crie o banco `db_novosiga` no PostgreSQL e configure as variáveis conforme a sua instalação:

| Variável | O que colocar |
| --- | --- |
| `SIF_DB_URL` | Por padrão, `jdbc:postgresql://localhost:5432/db_novosiga` |
| `SIF_DB_USER` | Por padrão, `postgres` |
| `SIF_DB_PASSWORD` | A senha do seu usuário do PostgreSQL |

Rode `.\mvnw.cmd spring-boot:run` no Windows ou `./mvnw spring-boot:run` no Linux ou macOS. Na tela de login, entre com uma conta desse banco ou crie uma pela própria aplicação.

## Como rodar o teste do projeto

O teste verifica se a aplicação consegue iniciar com o perfil de demonstração. Na pasta do `pom.xml`, rode `.\mvnw.cmd test` no Windows ou `./mvnw test` no Linux ou macOS.
