# SIF · Sistema Interno da Fatec

Aplicação web para gerenciar alunos, cursos, disciplinas, professores, produtos e pedidos.

**Integrantes:** 
Arthur Risso Pereira Rodovalho
Henrique Silva do Nascimento
Joao Pedro Rodrigues Monção

## Executar para a apresentação

Requer **JDK 17 ou superior**. O Maven Wrapper já acompanha o projeto e baixa as dependências na primeira execução.

Na pasta que contém o `pom.xml`, execute um dos comandos abaixo.

| Sistema | Comando |
| --- | --- |
| Windows (PowerShell) | `.\mvnw.cmd spring-boot:run -D"spring-boot.run.profiles=demo"` |
| Linux ou macOS | `./mvnw spring-boot:run -Dspring-boot.run.profiles=demo` |

Abra [localhost:8080/login](http://localhost:8080/login) e entre com o usuário **demo** e a senha **SifDemo2026!**. O perfil `demo` roda apenas na interface local e usa H2 em memória. Ele já abre com registros fictícios e reinicia os dados quando a aplicação é encerrada.

Se a porta 8080 estiver ocupada, acrescente `-D"spring-boot.run.arguments=--server.port=8081"` ao comando e altere a porta no navegador.

### Usar o PostgreSQL do projeto

Fora do perfil `demo`, o sistema usa o PostgreSQL. Disponibilize o banco `db_novosiga` e configure as variáveis abaixo para sua instalação. Depois execute o mesmo comando sem a opção de perfil e faça login com uma conta existente ou crie uma conta pela página de login.

| Variável | Configuração |
| --- | --- |
| `SIF_DB_URL` | Padrão: `jdbc:postgresql://localhost:5432/db_novosiga` |
| `SIF_DB_USER` | Padrão: `postgres` |
| `SIF_DB_PASSWORD` | Senha do usuário do banco |

## Tópico 1 · Campo de pesquisa

Digite parte do nome ou do termo que procura. Os resultados são atualizados enquanto você digita, mesmo que a palavra seja escrita sem acento ou em maiúsculas. A lista mostra quantos registros correspondem à busca. O botão **Buscar** também funciona com o JavaScript desativado.

| Tela | Campos pesquisados |
| --- | --- |
| Alunos | Nome, CPF ou cidade |
| Cursos | Nome ou descrição |
| Disciplinas | Nome ou sigla |
| Professores | Nome ou CPF |
| Produtos | Descrição, marca ou modelo |

## Tópico 3 · Filtros de pesquisa

Os filtros podem ser usados sozinhos ou em conjunto com o campo de pesquisa. Cursos, professores, graduações e marcas são obtidos dos cadastros. Em Produtos, as faixas predefinidas permitem escolher um intervalo sem digitar valores.

| Tela | Filtros |
| --- | --- |
| Alunos | Curso |
| Disciplinas | Curso e professor |
| Professores | Graduação |
| Produtos | Marca e faixa de preço |

A opção **Limpar busca** volta à listagem completa. Ao abrir um link copiado da barra de endereço dentro do SIF, os critérios são recuperados da URL, desde que o usuário tenha acesso à tela.

## Roteiro rápido para demonstrar

1. Em **Produtos**, digite `MOUSE`. Aparecem 3 produtos.
2. Com o termo preenchido, selecione **Orbit**. Aparecem 2 produtos. Escolha **De R$ 100 a R$ 499,99**. Sobra apenas **Mouse sem fio**.
3. Digite `xyz` para mostrar a mensagem de nenhum resultado e clique em **Limpar busca** para voltar aos 5 produtos iniciais.
4. Em **Alunos**, procure `joao` sem acento. Aparecem dois alunos. Selecione **Logística** para encontrar apenas **João Ribeiro**.
5. Em **Disciplinas**, combine **Sistemas** e **Helena Costa** para encontrar **Programação Web**.

## Implementação

- **Java 17, Spring Boot e Spring MVC:** os controladores recebem os critérios em parâmetros GET.
- **Spring Data JPA:** os repositórios buscam os registros no banco e combinam os filtros. Os serviços normalizam o termo digitado.
- **Thymeleaf, HTML, CSS e JavaScript:** a tela apresenta os controles e o resumo dos resultados. O JavaScript solicita a mesma listagem ao servidor e atualiza a área de resultados, mantendo o foco no campo em uso.
- **Spring Security, PostgreSQL, H2 e Maven:** autenticação, banco da aplicação, modo de demonstração e compilação, respectivamente.

Para rodar o teste de inicialização use `./mvnw test` no Linux ou macOS ou `.\mvnw.cmd test` no Windows.
