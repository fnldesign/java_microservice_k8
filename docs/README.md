# 📚 Documentação do Projeto

Esta pasta contém toda a documentação técnica do **java-microservice-k8**.

## 📑 Índice de Documentos

### 🏗️ [ARCHITECTURE.md](ARCHITECTURE.md)
Documentação completa da arquitetura do projeto.

**Conteúdo:**
- Visão geral da arquitetura cloud-native
- Diagramas detalhados do Kubernetes
- Fluxo de requisições (diagrama de sequência)
- Camadas da aplicação (Presentation, Security, Business, Data, Persistence)
- Especificações de Deployment e Storage
- Modelo de dados (ER Diagram)
- Componentes e dependências
- Estratégias de segurança
- Escalabilidade (horizontal, vertical, HPA)
- Métricas e observabilidade

**Para quem:** Arquitetos, desenvolvedores, DevOps

---

### 🔄 [CI-CD.md](CI-CD.md)
Guia completo do pipeline de CI/CD com GitHub Actions.

**Conteúdo:**
- Workflows automatizados (CI, Release, Docker, Dependency Update)
- Configuração do GitHub Container Registry
- Estratégia de branches e releases
- Deploy em diferentes ambientes (local, cloud)
- GitOps com ArgoCD
- Métricas e custos
- Troubleshooting

**Para quem:** DevOps, engenheiros de release

---

### 📖 [TESTING.md](TESTING.md)
Documentação de testes e estratégias de qualidade.

**Conteúdo:**
- Estrutura de testes (unitários e integração)
- Configuração de profiles Maven
- Testes unitários (28 testes) - Mockito
- Testes de integração (14 testes) - Spring Boot Test
- Exemplos de pipelines CI/CD (GitHub Actions, GitLab CI, Jenkins)
- Relatórios de cobertura
- Troubleshooting de testes

**Para quem:** Desenvolvedores, QA, engenheiros de CI/CD

---

### 🐳 [DOCKER_KIND_SETUP.md](DOCKER_KIND_SETUP.md)
Guia de instalação do ambiente de desenvolvimento.

**Conteúdo:**
- Instalação do Docker (Windows e Linux)
- Instalação do kind (Kubernetes in Docker)
- Instalação do kubectl
- Configuração de WSL2 (Windows)
- Verificação e troubleshooting
- Comandos úteis

**Para quem:** Desenvolvedores iniciantes, setup de ambiente

---

## 🗂️ Estrutura da Documentação

```
docs/
├── README.md                 # Este arquivo (índice)
├── ARCHITECTURE.md           # Arquitetura completa
├── CI-CD.md                  # Pipeline CI/CD
├── TESTING.md                # Testes e qualidade
└── DOCKER_KIND_SETUP.md      # Setup de ambiente
```

## 🔗 Outros Documentos

Na raiz do projeto você também encontra:

- **[README.md](../README.md)** - Visão geral e Quick Start
- **[CONTRIBUTING.md](../CONTRIBUTING.md)** - Guia de contribuição
- **[LICENSE](../LICENSE)** - Licença MIT

## 📖 Guia de Leitura Recomendado

### Para Novos Desenvolvedores

1. 📄 [README.md](../README.md) - Comece aqui!
2. 🐳 [DOCKER_KIND_SETUP.md](DOCKER_KIND_SETUP.md) - Configure seu ambiente
3. 🏗️ [ARCHITECTURE.md](ARCHITECTURE.md) - Entenda a arquitetura
4. 📖 [TESTING.md](TESTING.md) - Aprenda a rodar testes
5. 🤝 [CONTRIBUTING.md](../CONTRIBUTING.md) - Comece a contribuir

### Para DevOps/SRE

1. 🏗️ [ARCHITECTURE.md](ARCHITECTURE.md) - Entenda a infraestrutura
2. 🔄 [CI-CD.md](CI-CD.md) - Configure pipelines
3. 🐳 [DOCKER_KIND_SETUP.md](DOCKER_KIND_SETUP.md) - Setup de clusters

### Para Arquitetos

1. 🏗️ [ARCHITECTURE.md](ARCHITECTURE.md) - Arquitetura completa
2. 🔄 [CI-CD.md](CI-CD.md) - Estratégia de deploy
3. 📖 [TESTING.md](TESTING.md) - Estratégia de testes

## 🔍 Buscar Informações

| Você quer saber... | Veja este documento |
|-------------------|---------------------|
| Como deployar o projeto | [README.md](../README.md) - Seção "Quick Start" |
| Como funciona a arquitetura | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Como funcionam os testes | [TESTING.md](TESTING.md) |
| Como configurar CI/CD | [CI-CD.md](CI-CD.md) |
| Como instalar Docker/kind | [DOCKER_KIND_SETUP.md](DOCKER_KIND_SETUP.md) |
| Como contribuir | [CONTRIBUTING.md](../CONTRIBUTING.md) |
| Endpoints da API | [README.md](../README.md) - Seção "Endpoints" |
| Como usar Secrets no K8s | [ARCHITECTURE.md](ARCHITECTURE.md) - Seção "Segurança" |
| Como escalar a aplicação | [ARCHITECTURE.md](ARCHITECTURE.md) - Seção "Escalabilidade" |
| Como funcionam os workflows | [CI-CD.md](CI-CD.md) - Seção "Workflows" |

## 📝 Contribuindo com a Documentação

Encontrou um erro ou quer melhorar a documentação? Veja [CONTRIBUTING.md](../CONTRIBUTING.md) e abra uma issue ou PR!

---

<div align="center">

**Documentação mantida com ❤️ pela comunidade**

[⬆ Voltar para o README principal](../README.md)

</div>
