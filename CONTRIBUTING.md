# Guia de Contribuição

Obrigado por considerar contribuir para o **java-microservice-k8**! 🎉

Este documento fornece diretrizes para contribuir com o projeto.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Contribuir](#como-contribuir)
- [Processo de Development](#processo-de-development)
- [Padrões de Código](#padrões-de-código)
- [Testes](#testes)
- [Commits](#commits)
- [Pull Requests](#pull-requests)

## 🤝 Código de Conduta

Este projeto adere a um código de conduta. Ao participar, você concorda em manter um ambiente respeitoso e inclusivo para todos.

### Nossos Valores

- **Respeito**: Trate todos com respeito e consideração
- **Colaboração**: Trabalhe junto para melhorar o projeto
- **Inclusão**: Acolha contribuidores de todos os níveis
- **Qualidade**: Mantenha altos padrões de código

## 🚀 Como Contribuir

Existem várias maneiras de contribuir:

### 1. Reportar Bugs

Se você encontrar um bug, por favor:

1. Verifique se já não existe uma issue aberta sobre o problema
2. Crie uma nova issue incluindo:
   - Descrição clara do problema
   - Passos para reproduzir
   - Comportamento esperado vs comportamento atual
   - Ambiente (SO, versão do Java, etc.)
   - Logs relevantes

### 2. Sugerir Melhorias

Para sugerir novas funcionalidades ou melhorias:

1. Abra uma issue descrevendo a sugestão
2. Explique o problema que você quer resolver
3. Descreva a solução proposta
4. Discuta alternativas consideradas

### 3. Contribuir com Código

Siga o processo abaixo para contribuir com código.

## 💻 Processo de Development

### 1. Fork e Clone

```bash
# Fork o repositório no GitHub
# Clone o seu fork
git clone https://github.com/SEU-USUARIO/java-microservice-k8.git
cd java-microservice-k8
```

### 2. Configure o Ambiente

```bash
# Instale as dependências
mvn clean install

# Execute os testes
mvn test
```

### 3. Crie uma Branch

```bash
# Crie uma branch para sua feature/fix
git checkout -b feature/minha-feature
# ou
git checkout -b fix/meu-bugfix
```

### 4. Faça suas Mudanças

- Escreva código limpo e bem documentado
- Siga os padrões de código do projeto
- Adicione/atualize testes
- Atualize a documentação se necessário

### 5. Execute os Testes

```bash
# Testes unitários
mvn test -Punit-tests

# Testes de integração
mvn verify -Pintegration-tests

# Todos os testes
mvn verify
```

### 6. Commit e Push

```bash
# Adicione suas mudanças
git add .

# Commit com mensagem descritiva
git commit -m "feat: adiciona nova funcionalidade X"

# Push para seu fork
git push origin feature/minha-feature
```

### 7. Abra um Pull Request

1. Vá para o repositório original no GitHub
2. Clique em "New Pull Request"
3. Selecione sua branch
4. Preencha o template de PR
5. Aguarde review

## 📝 Padrões de Código

### Java

- **Versão**: Java 17
- **Style Guide**: Siga as convenções do Google Java Style Guide
- **Indentação**: 2 ou 4 espaços (mantenha consistência)
- **Line Length**: Máximo 120 caracteres

### Estrutura de Pacotes

```
com.example.microservice
├── config          # Configurações (Security, OpenAPI, etc.)
├── controller      # Controllers REST
├── model           # Entidades JPA
├── repository      # Repositories Spring Data
└── service         # Camada de serviço (se necessário)
```

### Nomenclatura

- **Classes**: PascalCase (`TodoController`, `SecurityConfig`)
- **Métodos**: camelCase (`createTodo`, `getAllTodos`)
- **Constantes**: UPPER_SNAKE_CASE (`API_KEY_HEADER`)
- **Variáveis**: camelCase (`todoRepository`, `apiKey`)

### Documentação

- Use JavaDoc para classes e métodos públicos
- Documente lógica complexa com comentários inline
- Mantenha o README.md atualizado

```java
/**
 * Controller para gerenciar operações CRUD de TODOs.
 * 
 * @author Nome do Autor
 * @since 1.0
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    // ...
}
```

## 🧪 Testes

### Requisitos

- **Cobertura mínima**: 80% para novas funcionalidades
- **Testes unitários**: Obrigatório para lógica de negócio
- **Testes de integração**: Obrigatório para endpoints REST

### Estrutura de Testes

```
src/test/java/com/example/microservice
├── controller
│   ├── TodoControllerTest.java              # Testes unitários
│   └── TodoControllerIntegrationTest.java   # Testes de integração
├── model
│   └── TodoTest.java                         # Testes do modelo
└── repository
    └── TodoRepositoryTest.java              # Testes do repositório
```

### Convenções de Testes

```java
@Test
@DisplayName("Should create TODO successfully")
void shouldCreateTodoSuccessfully() {
    // Given (Arrange)
    Todo todo = new Todo();
    todo.setTitle("Test");
    
    // When (Act)
    Todo result = todoService.create(todo);
    
    // Then (Assert)
    assertNotNull(result.getId());
    assertEquals("Test", result.getTitle());
}
```

## 📦 Commits

Use **Conventional Commits** para mensagens de commit:

### Formato

```
<tipo>(<escopo>): <descrição curta>

<corpo opcional>

<rodapé opcional>
```

### Tipos

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Mudanças na documentação
- `style`: Formatação, sem mudança de código
- `refactor`: Refatoração de código
- `test`: Adicionar ou corrigir testes
- `chore`: Tarefas de manutenção

### Exemplos

```bash
feat(api): adiciona endpoint de filtro por status

Implementa endpoint GET /api/todos?completed=true para filtrar
TODOs por status de conclusão.

Closes #123
```

```bash
fix(security): corrige validação de API key

A validação estava permitindo requisições sem header X-API-Key.
Agora retorna 403 corretamente.
```

```bash
docs(readme): atualiza instruções de instalação

Adiciona seção sobre instalação do metrics-server.
```

```bash
test(controller): adiciona testes de integração

Adiciona 10 novos testes de integração para TodoController
cobrindo todos os endpoints CRUD.
```

## 🔀 Pull Requests

### Checklist Antes de Abrir PR

- [ ] Código segue os padrões do projeto
- [ ] Todos os testes passam (`mvn verify`)
- [ ] Novos testes foram adicionados (se aplicável)
- [ ] Documentação foi atualizada (se aplicável)
- [ ] Commit messages seguem Conventional Commits
- [ ] Branch está atualizada com `main`
- [ ] Não há conflitos de merge

### Template de PR

Ao abrir um PR, inclua:

```markdown
## Descrição
Breve descrição das mudanças.

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova funcionalidade
- [ ] Breaking change
- [ ] Documentação

## Como Testar
Passos para testar as mudanças:
1. ...
2. ...

## Checklist
- [ ] Testes passam localmente
- [ ] Código documentado
- [ ] README atualizado

## Issues Relacionadas
Closes #123
```

### Processo de Review

1. **Automated Checks**: CI/CD executa testes automaticamente
2. **Code Review**: Mantenedores revisam o código
3. **Feedback**: Discussão e possíveis ajustes
4. **Approval**: Aprovação de pelo menos 1 mantenedor
5. **Merge**: Merge para a branch principal

## 🏗️ Estrutura do Projeto

```
java-microservice-k8/
├── .azure/                      # Azure DevOps configs
├── k8s/                         # Manifests Kubernetes
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── secret.yaml
│   └── persistent-volume.yaml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/microservice/
│   │   │       ├── config/          # Configurações
│   │   │       ├── controller/      # REST Controllers
│   │   │       ├── model/           # Entidades JPA
│   │   │       └── repository/      # Spring Data Repos
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback-spring.xml
│   └── test/                    # Testes
├── Dockerfile                   # Multi-stage build
├── pom.xml                      # Maven config
├── README.md                    # Documentação principal
├── TESTING.md                   # Guia de testes
├── DOCKER_KIND_SETUP.md        # Setup Docker/kind
├── CONTRIBUTING.md             # Este arquivo
└── LICENSE                     # Licença MIT
```

## 📞 Contato

Se tiver dúvidas sobre como contribuir:

- Abra uma issue com a tag `question`
- Entre em contato com os mantenedores

### 👨‍💻 Mantenedor

**Flavio Lopes** - Engenheiro de Dados

- 💼 [LinkedIn: Flavio Lopes](https://www.linkedin.com/in/flavionlopes/)
- 🐙 [GitHub: @fnldesign](https://github.com/fnldesign)

### 📚 Dados na Prática

Este projeto faz parte da iniciativa **Dados na Prática**, uma comunidade dedicada a compartilhar conhecimento em Engenharia de Dados, DevOps e Cloud Computing.

- 🏢 [LinkedIn: Dados na Prática](https://www.linkedin.com/company/dados-na-pratica)
- 🔗 [GitHub: @dadosnapratica](https://github.com/dadosnapratica)

Junte-se à comunidade para aprender e compartilhar experiências sobre tecnologias de dados e boas práticas de desenvolvimento!

## 🙏 Agradecimentos

Obrigado por dedicar seu tempo para melhorar este projeto!

Toda contribuição, grande ou pequena, é muito apreciada. 💙

---

**Happy Coding!** 🚀
