# CI/CD Pipeline com GitHub Actions

Este documento descreve a estratégia de CI/CD implementada para o projeto **java-microservice-k8** usando GitHub Actions.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Workflows Disponíveis](#workflows-disponíveis)
- [Configuração Inicial](#configuração-inicial)
- [Fluxo de Desenvolvimento](#fluxo-de-desenvolvimento)
- [Deploy em Kubernetes](#deploy-em-kubernetes)
- [Troubleshooting](#troubleshooting)

## 🎯 Visão Geral

Como este é um projeto Kubernetes que não pode ser deployado em plataformas serverless gratuitas (Vercel, Netlify, etc.), nossa estratégia de CI/CD foca em:

1. ✅ **Build e Testes Automatizados** em cada push/PR
2. 🐳 **Publicação de Imagens Docker** no GitHub Container Registry (gratuito)
3. 📦 **Releases Automatizadas** com artefatos prontos para deploy
4. 🔒 **Verificação de Segurança** com Trivy
5. 📊 **Relatórios de Qualidade** e cobertura de testes

### Por que não usar Vercel/Netlify?

- **Vercel/Netlify**: Focados em aplicações serverless, frontend e edge functions
- **Nosso projeto**: Requer Kubernetes (StatefulSet, PersistentVolumes, Secrets)
- **Solução**: Publicar imagens Docker no **ghcr.io** (GitHub Container Registry) para deploy em qualquer cluster Kubernetes

## 🔄 Workflows Disponíveis

### 1. CI - Build and Test (`ci.yml`)

**Trigger**: Push ou Pull Request para `main`, `master` ou `develop`

**Jobs**:
- 🧪 **Test**: Executa testes unitários e de integração
- 🔨 **Build**: Compila o projeto e gera o JAR
- 🐳 **Docker**: Constrói e testa a imagem Docker
- 📊 **Quality**: Verifica formatação e qualidade do código
- 🔒 **Security**: Escaneia vulnerabilidades com Trivy

```yaml
# Executado automaticamente em:
- push para main/master/develop
- pull requests para main/master/develop
```

**O que valida**:
- ✅ Todos os 42 testes passam (unitários + integração)
- ✅ Build Maven é bem-sucedido
- ✅ Imagem Docker pode ser construída
- ✅ Container inicia corretamente
- ✅ Não há vulnerabilidades críticas

### 2. Release - Build and Publish (`release.yml`)

**Trigger**: Push de tag `v*.*.*` ou execução manual

**Jobs**:
- 📝 **Release**: Cria release no GitHub com changelog automático
- 🐳 **Build and Publish**: Publica imagem no GitHub Container Registry
- 📦 **Publish Artifacts**: Anexa JAR e manifests K8s ao release

```yaml
# Criar uma release:
git tag v1.0.0
git push origin v1.0.0

# Ou executar manualmente no GitHub Actions
```

**O que gera**:
- 🐳 Imagem Docker em `ghcr.io/SEU-USUARIO/java-microservice-k8:v1.0.0`
- 📦 Artefatos: JAR, manifests K8s, documentação
- 📋 Changelog automático baseado em commits/PRs

### 3. Docker - Publish Latest (`docker-latest.yml`)

**Trigger**: Push para `main`/`master` ou execução manual

**Jobs**:
- 🐳 Publica imagem `latest` no GitHub Container Registry
- Suporta multi-arquitetura (amd64, arm64)

```yaml
# Executado automaticamente quando merge para main/master
```

### 4. Dependency Update Check (`dependency-update.yml`)

**Trigger**: Toda segunda-feira às 9h UTC ou execução manual

**Jobs**:
- 📊 Verifica atualizações de dependências Maven
- 🔒 Escaneia vulnerabilidades de segurança com OWASP Dependency Check
- 📄 Gera relatórios disponíveis como artefatos

## ⚙️ Configuração Inicial

### 1. Habilitar GitHub Container Registry

O GitHub Container Registry (ghcr.io) já está habilitado por padrão para repositórios públicos e privados.

### 2. Tornar Imagens Públicas (Opcional)

Após a primeira publicação:

1. Vá para **Packages** na sua conta/organização
2. Selecione o package `java-microservice-k8`
3. Clique em **Package settings**
4. Em **Danger Zone**, clique em **Change visibility**
5. Selecione **Public**

### 3. Configurar Secrets (se necessário)

Para repositórios privados, não é necessário configurar secrets adicionais - o `GITHUB_TOKEN` é suficiente.

## 🚀 Fluxo de Desenvolvimento

### Branch Strategy

```
main/master (produção)
  ↑
  └── develop (desenvolvimento)
       ↑
       └── feature/* (features)
       └── fix/* (bugfixes)
```

### Desenvolvimento de Features

```bash
# 1. Criar branch de feature
git checkout -b feature/minha-feature

# 2. Desenvolver e commitar (Conventional Commits)
git commit -m "feat(api): adiciona endpoint de filtro"

# 3. Push e criar PR
git push origin feature/minha-feature

# 4. CI executa automaticamente
# - Testes unitários
# - Testes de integração
# - Build Docker
# - Verificação de segurança

# 5. Após aprovação, merge para develop
# 6. CI executa novamente
# 7. Imagem 'develop-SHA' publicada no ghcr.io
```

### Criar Release

```bash
# 1. Merge develop → main
git checkout main
git merge develop

# 2. Criar tag de versão
git tag v1.0.0

# 3. Push da tag
git push origin v1.0.0

# 4. GitHub Actions:
# - Cria release no GitHub
# - Publica imagens Docker:
#   - ghcr.io/seu-usuario/java-microservice-k8:v1.0.0
#   - ghcr.io/seu-usuario/java-microservice-k8:1.0
#   - ghcr.io/seu-usuario/java-microservice-k8:1
#   - ghcr.io/seu-usuario/java-microservice-k8:latest
# - Anexa artefatos (JAR, K8s manifests, docs)
```

## 🐳 Usar Imagens Publicadas

### Pull da Imagem

```bash
# Última versão
docker pull ghcr.io/SEU-USUARIO/java-microservice-k8:latest

# Versão específica
docker pull ghcr.io/SEU-USUARIO/java-microservice-k8:v1.0.0

# Para repositórios privados (fazer login primeiro)
echo $GITHUB_TOKEN | docker login ghcr.io -u SEU-USUARIO --password-stdin
```

### Atualizar Deployment do Kubernetes

```yaml
# k8s/deployment.yaml
spec:
  containers:
  - name: java-microservice-k8
    image: ghcr.io/SEU-USUARIO/java-microservice-k8:v1.0.0
    # ou :latest para desenvolvimento
```

## ☸️ Deploy em Kubernetes

### Opção 1: Cluster Local (kind, minikube, Docker Desktop)

```bash
# 1. Pull da imagem
docker pull ghcr.io/SEU-USUARIO/java-microservice-k8:v1.0.0

# 2. Carregar no kind
kind load docker-image ghcr.io/SEU-USUARIO/java-microservice-k8:v1.0.0 --name kind-cluster

# 3. Atualizar deployment.yaml com a nova imagem
# 4. Apply
kubectl apply -f k8s/
```

### Opção 2: Cluster Cloud (GKE, EKS, AKS)

```bash
# 1. O cluster puxa a imagem diretamente do ghcr.io
# (para repos privados, configurar imagePullSecrets)

# 2. Apply
kubectl apply -f k8s/

# 3. Verificar
kubectl get pods
kubectl logs -f <pod-name>
```

### Opção 3: ArgoCD / GitOps

```yaml
# argocd-app.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: java-microservice-k8
spec:
  source:
    repoURL: https://github.com/SEU-USUARIO/java-microservice-k8
    targetRevision: main
    path: k8s
  destination:
    server: https://kubernetes.default.svc
    namespace: default
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
```

## 🔒 ImagePullSecrets (Repos Privados)

Se o repositório for privado, criar secret para pull:

```bash
# 1. Criar Personal Access Token no GitHub
# Settings → Developer settings → Personal access tokens → Tokens (classic)
# Permissões: read:packages

# 2. Criar secret no K8s
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=SEU-USUARIO \
  --docker-password=SEU-GITHUB_TOKEN \
  --docker-email=seu-email@example.com

# 3. Atualizar deployment.yaml
spec:
  imagePullSecrets:
  - name: ghcr-secret
  containers:
  - name: java-microservice-k8
    image: ghcr.io/SEU-USUARIO/java-microservice-k8:v1.0.0
```

## 📊 Badges para README

Adicione badges do CI/CD ao README:

```markdown
![CI](https://github.com/SEU-USUARIO/java-microservice-k8/workflows/CI%20-%20Build%20and%20Test/badge.svg)
![Release](https://github.com/SEU-USUARIO/java-microservice-k8/workflows/Release%20-%20Build%20and%20Publish/badge.svg)
![Docker](https://img.shields.io/badge/docker-ghcr.io-blue?logo=docker)
```

## 🔍 Visualizar Resultados

### GitHub Actions

1. Vá para **Actions** no repositório
2. Selecione o workflow desejado
3. Clique em um run específico para ver logs detalhados

### Container Registry

1. Vá para **Packages** na conta/organização
2. Visualize todas as imagens publicadas
3. Veja tags, tamanhos, e datas de publicação

### Releases

1. Vá para **Releases** no repositório
2. Veja todas as versões publicadas
3. Baixe artefatos (JAR, K8s manifests, bundle completo)

## 🐛 Troubleshooting

### Build Falha no CI

```bash
# Reproduzir localmente
mvn clean verify

# Verificar logs no GitHub Actions
```

### Imagem não publica no ghcr.io

**Problema**: Erro de permissão ao publicar

**Solução**:
1. Verificar se `permissions` está correto no workflow
2. Para repos de organizações, habilitar packages no Settings

### Tests Falham no CI mas passam localmente

**Possíveis causas**:
- Diferenças de timezone (usar UTC nos testes)
- Dependências de ordem de execução
- Estado compartilhado entre testes

**Solução**:
```bash
# Executar testes em ordem aleatória
mvn test -Dsurefire.runOrder=random
```

### Release não cria changelog

**Problema**: Changelog vazio

**Solução**:
- Usar Conventional Commits (feat:, fix:, docs:)
- Adicionar labels aos PRs (feature, bug, documentation)

## 📈 Métricas e Monitoramento

### Tempo de Build

- **Testes unitários**: ~10 segundos
- **Testes integração**: ~30 segundos
- **Build Maven**: ~40 segundos
- **Build Docker**: ~2-3 minutos
- **Publicar multi-arch**: ~5-7 minutos

### Custos

- ✅ **GitHub Actions**: 2000 minutos/mês grátis (repos públicos: ilimitado)
- ✅ **GitHub Container Registry**: Gratuito para repos públicos
- ✅ **GitHub Storage**: 500MB grátis (repos públicos: ilimitado)

## 🎯 Próximos Passos

### Melhorias Sugeridas

1. **Kubernetes Deployment Automation**
   - Configurar ArgoCD para GitOps
   - Criar environments (dev, staging, prod)

2. **Monitoramento**
   - Integrar Prometheus + Grafana
   - Adicionar alertas no Slack/Discord

3. **Code Coverage**
   - Integrar JaCoCo para coverage
   - Publicar relatórios no Codecov.io

4. **Performance Testing**
   - Adicionar testes de carga com K6
   - Benchmark de performance em cada release

5. **Multi-Environment**
   - Criar namespaces para dev/staging/prod
   - Configurar diferentes secrets por ambiente

## 📚 Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [Docker Multi-Platform Images](https://docs.docker.com/build/building/multi-platform/)

---

**Happy CI/CD!** 🚀
