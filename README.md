# SIF — Sistema Interno da Fatec

O SIF é uma aplicação web para gerenciar alunos, cursos, disciplinas, professores, produtos e pedidos. Para a **Avaliação Final de Encontrabilidade em Projetos Web**, trabalhamos em dois recursos que ajudam a localizar registros nas listagens: **campo de pesquisa** e **filtros de pesquisa**.

## Integrantes

- Arthur Risso Pereira Rodovalho
- Henrique Silva do Nascimento
- Joao Pedro Rodrigues Monção

## Como executar o projeto

Para testar o sistema, use o **modo demonstração**. Ele já vem com registros fictícios e uma conta de acesso. Você só precisa do **JDK 17 ou superior**. O Maven Wrapper acompanha o projeto e baixa as dependências na primeira execução.

1. Extraia o ZIP ou clone o repositório.
2. Abra o terminal na pasta que contém o arquivo `pom.xml`.
3. Execute o comando correspondente ao seu sistema:

   **Windows (PowerShell)**

   ```powershell
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
   ```

   **Linux ou macOS**

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
   ```

4. Quando a aplicação terminar de iniciar, mantenha o terminal aberto e acesse [http://127.0.0.1:8080/login](http://127.0.0.1:8080/login).
5. Entre com a conta abaixo:

   | Usuário | Senha |
   | --- | --- |
   | `demo` | `SifDemo2026!` |

O modo demonstração usa **H2 em memória** e roda localmente. Ao reiniciar a aplicação, os registros de exemplo são restaurados.

Se a porta 8080 estiver ocupada, acrescente `"-Dspring-boot.run.arguments=--server.port=8081"` ao final do comando e acesse `http://127.0.0.1:8081/login`.

## Recursos da avaliação

### Tópico 1 — Campo de pesquisa

Digite parte do que você procura. A lista é atualizada enquanto você digita e mostra quantos registros foram encontrados. Por exemplo, pesquisar `joao` encontra cadastros com o nome **João**, mesmo sem o acento. O botão **Buscar** também funciona com o JavaScript desativado.

### Tópico 3 — Filtros de pesquisa

Os filtros podem ser usados sozinhos ou junto com uma palavra-chave. As opções de curso, professor, graduação e marca vêm dos cadastros do sistema. Em Produtos, as faixas de preço já vêm definidas para facilitar a seleção.

| Tela | O que você pode pesquisar | Filtros disponíveis |
| --- | --- | --- |
| Alunos | Nome, CPF ou cidade | Curso |
| Cursos | Nome ou descrição | — |
| Disciplinas | Nome ou sigla | Curso e professor |
| Professores | Nome ou CPF | Graduação |
| Produtos | Descrição, marca ou modelo | Marca e faixa de preço |

Se nenhum registro corresponder aos critérios, o sistema mostra uma mensagem. Clique em **Limpar busca** para remover o texto e os filtros e voltar à lista completa. Os critérios também ficam na URL, o que permite reabrir a mesma busca com uma conta que tenha acesso à tela.

## Roteiro para testar e apresentar

Entre com a conta `demo` e siga os passos abaixo. Os resultados correspondem aos dados de exemplo carregados ao iniciar o sistema.

| Onde | O que fazer | O que deve aparecer |
| --- | --- | --- |
| **Produtos** | Digite `MOUSE` e aguarde a atualização. | **3 resultados** |
| **Produtos** | Mantenha `MOUSE` e selecione a marca **Orbit**. | **2 resultados** |
| **Produtos** | Mantenha a busca e a marca. Selecione **De R$ 100 a R$ 499,99**. | Apenas **Mouse sem fio** |
| **Produtos** | Troque o termo por `xyz`. Depois clique em **Limpar busca**. | Uma mensagem de nenhum resultado e, após limpar, os **5 produtos iniciais** |
| **Alunos** | Digite `joao` sem acento. Depois selecione **Logística**. | **2 alunos** na busca inicial e apenas **João Ribeiro** após aplicar o filtro |
| **Disciplinas** | Selecione o curso **Sistemas** e a professora **Helena Costa**. | Apenas **Programação Web** |

## Como a busca funciona no código

1. A tela envia a palavra-chave e os filtros selecionados como parâmetros de uma requisição `GET`.
2. O controlador recebe os critérios. O serviço normaliza o texto da busca, e o repositório consulta o banco com os critérios combinados.
3. O Thymeleaf monta a listagem. Quando o JavaScript está ativo, ele atualiza a área de resultados sem recarregar a página inteira. O botão **Buscar** também permite enviar o formulário normalmente.

**Tecnologias utilizadas:** Java 17, Spring Boot, Spring MVC, Spring Data JPA, Spring Security, Thymeleaf, HTML, CSS, JavaScript, PostgreSQL, H2 e Maven.

## Como usar o PostgreSQL

Para usar o banco do projeto, disponibilize o PostgreSQL com o banco `db_novosiga`. Configure as variáveis abaixo conforme sua instalação:

| Variável | Configuração |
| --- | --- |
| `SIF_DB_URL` | Padrão `jdbc:postgresql://localhost:5432/db_novosiga` |
| `SIF_DB_USER` | Padrão `postgres` |
| `SIF_DB_PASSWORD` | Senha do usuário do PostgreSQL |

Execute `.\mvnw.cmd spring-boot:run` no Windows ou `./mvnw spring-boot:run` no Linux ou macOS. Na tela de login, use uma conta cadastrada nesse banco ou crie uma pela própria aplicação.

## Teste de inicialização

O projeto inclui um teste que verifica se a aplicação inicia com o perfil de demonstração. Na pasta do `pom.xml`, execute `.\mvnw.cmd test` no Windows ou `./mvnw test` no Linux ou macOS.
