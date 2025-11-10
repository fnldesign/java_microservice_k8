# Guia de Instalação: Docker, kind e kubectl

Este guia contém instruções detalhadas para instalar e configurar as ferramentas necessárias para executar o microserviço Java no Kubernetes local com kind.

## 📋 Índice

- [Windows](#windows)
  - [Docker Desktop](#docker-desktop-windows)
  - [kubectl](#kubectl-windows)
  - [kind](#kind-windows)
- [Linux](#linux)
  - [Docker Engine](#docker-engine-linux)
  - [kubectl](#kubectl-linux)
  - [kind](#kind-linux)
- [Verificação da Instalação](#verificação-da-instalação)
- [Solução de Problemas](#solução-de-problemas)

---

## Windows

### Docker Desktop (Windows)

Docker Desktop inclui Docker Engine, Docker CLI e Docker Compose.

#### Requisitos

- Windows 10 64-bit: Pro, Enterprise ou Education (Build 19041 ou superior)
- WSL 2 habilitado (recomendado)
- Virtualização habilitada no BIOS

#### Instalação

1. **Baixar Docker Desktop:**
   - Acesse: <https://www.docker.com/products/docker-desktop>
   - Clique em "Download for Windows"

2. **Instalar:**
   - Execute o instalador `Docker Desktop Installer.exe`
   - Siga o assistente de instalação
   - Marque a opção "Use WSL 2 instead of Hyper-V" (recomendado)
   - Clique em "Ok" e aguarde a instalação

3. **Iniciar Docker Desktop:**
   - Procure por "Docker Desktop" no menu Iniciar
   - Execute o aplicativo
   - Aceite os termos de serviço
   - Aguarde o Docker inicializar (ícone fica estável na bandeja)

4. **Verificar instalação:**

```powershell
docker --version
docker run hello-world
```

#### Configuração WSL 2 (Recomendado)

1. **Habilitar WSL 2:**

```powershell
# Execute como Administrador
wsl --install
wsl --set-default-version 2
```

2. **Reinicie o computador**

3. **Configurar Docker Desktop:**
   - Abra Docker Desktop
   - Settings → General
   - Marque "Use the WSL 2 based engine"
   - Settings → Resources → WSL Integration
   - Habilite integração com suas distribuições WSL

### kubectl (Windows)

kubectl é a ferramenta de linha de comando do Kubernetes.

#### Instalação via Chocolatey (Recomendado)

```powershell
# Instalar Chocolatey (se não tiver)
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Instalar kubectl
choco install kubernetes-cli
```

#### Instalação Manual

1. **Baixar kubectl:**

```powershell
# Criar diretório (se não existir)
New-Item -ItemType Directory -Force -Path C:\kubectl

# Baixar última versão
Invoke-WebRequest -Uri "https://dl.k8s.io/release/v1.28.0/bin/windows/amd64/kubectl.exe" -OutFile "C:\kubectl\kubectl.exe"
```

2. **Adicionar ao PATH:**
   - Abra "Variáveis de Ambiente" (Win + R → `sysdm.cpl` → Avançado → Variáveis de Ambiente)
   - Em "Variáveis do sistema", selecione "Path" → Editar
   - Clique em "Novo" e adicione: `C:\kubectl`
   - Clique em "OK" para salvar

3. **Verificar instalação:**

```powershell
kubectl version --client
```

### kind (Windows)

kind (Kubernetes IN Docker) permite executar clusters Kubernetes locais usando contêineres Docker.

#### Instalação via Chocolatey (Recomendado)

```powershell
choco install kind
```

#### Instalação Manual

```powershell
# Baixar kind
curl.exe -Lo C:\kubectl\kind.exe https://kind.sigs.k8s.io/dl/v0.20.0/kind-windows-amd64

# Verificar instalação
kind --version
```

#### Criar cluster kind

```powershell
# Criar cluster
kind create cluster --name kind-cluster

# Verificar
kubectl cluster-info --context kind-kind-cluster
kubectl get nodes
```

---

## Linux

### Docker Engine (Linux)

#### Ubuntu/Debian

```bash
# Atualizar pacotes
sudo apt-get update

# Instalar dependências
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Adicionar chave GPG oficial do Docker
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Adicionar repositório
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Instalar Docker Engine
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Adicionar usuário ao grupo docker (evita usar sudo)
sudo usermod -aG docker $USER

# Aplicar mudanças de grupo (ou faça logout/login)
newgrp docker

# Verificar instalação
docker --version
docker run hello-world
```

#### Fedora/RHEL/CentOS

```bash
# Remover versões antigas
sudo dnf remove docker \
                docker-client \
                docker-client-latest \
                docker-common \
                docker-latest \
                docker-latest-logrotate \
                docker-logrotate \
                docker-engine

# Adicionar repositório
sudo dnf -y install dnf-plugins-core
sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo

# Instalar Docker Engine
sudo dnf install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Iniciar Docker
sudo systemctl start docker
sudo systemctl enable docker

# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER
newgrp docker

# Verificar instalação
docker --version
docker run hello-world
```

### kubectl (Linux)

#### Instalação via Package Manager

**Ubuntu/Debian:**

```bash
# Adicionar chave e repositório
sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates curl

curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.28/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg

echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.28/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list

# Instalar kubectl
sudo apt-get update
sudo apt-get install -y kubectl
```

**Fedora/RHEL/CentOS:**

```bash
# Adicionar repositório
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://pkgs.k8s.io/core:/stable:/v1.28/rpm/
enabled=1
gpgcheck=1
gpgkey=https://pkgs.k8s.io/core:/stable:/v1.28/rpm/repodata/repomd.xml.key
EOF

# Instalar kubectl
sudo dnf install -y kubectl
```

#### Instalação Manual (qualquer distribuição)

```bash
# Baixar última versão
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"

# Validar binário (opcional)
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl.sha256"
echo "$(cat kubectl.sha256)  kubectl" | sha256sum --check

# Instalar
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# Verificar
kubectl version --client
```

### kind (Linux)

#### Instalação

```bash
# Baixar kind
curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64

# Tornar executável
chmod +x ./kind

# Mover para PATH
sudo mv ./kind /usr/local/bin/kind

# Verificar instalação
kind --version
```

#### Criar cluster kind

```bash
# Criar cluster
kind create cluster --name kind-cluster

# Verificar
kubectl cluster-info --context kind-kind-cluster
kubectl get nodes
```

---

## Verificação da Instalação

Após instalar todas as ferramentas, execute os seguintes comandos para verificar:

### Verificar Docker

```bash
# Windows (PowerShell) ou Linux (Bash)
docker --version
# Saída esperada: Docker version 24.x.x, build xxxxx

docker ps
# Saída esperada: lista vazia ou contêineres em execução

docker run hello-world
# Saída esperada: mensagem "Hello from Docker!"
```

### Verificar kubectl

```bash
kubectl version --client
# Saída esperada: Client Version: v1.28.x

kubectl config view
# Saída esperada: configuração do kubectl (pode estar vazia inicialmente)
```

### Verificar kind

```bash
kind --version
# Saída esperada: kind v0.20.0 go1.x.x linux/amd64

kind get clusters
# Saída esperada: lista de clusters (vazia se ainda não criou nenhum)
```

### Criar e Testar Cluster kind

```bash
# Criar cluster de teste
kind create cluster --name test-cluster

# Verificar se foi criado
kind get clusters
# Saída esperada: test-cluster

# Verificar nodes
kubectl get nodes
# Saída esperada: 1 node com status Ready

# Ver informações do cluster
kubectl cluster-info --context kind-test-cluster

# Deletar cluster de teste
kind delete cluster --name test-cluster
```

---

## Solução de Problemas

### Windows

#### Docker Desktop não inicia

**Problema:** Docker Desktop fica em "Starting..." indefinidamente

**Soluções:**

1. **Verificar WSL 2:**

```powershell
wsl --list --verbose
# Certifique-se que há pelo menos uma distribuição com VERSION 2
```

2. **Reiniciar WSL:**

```powershell
wsl --shutdown
# Aguarde 10 segundos e inicie Docker Desktop novamente
```

3. **Verificar Hyper-V (se não usar WSL 2):**
   - Painel de Controle → Programas → Ativar/Desativar recursos do Windows
   - Marque "Hyper-V" e "Contêineres do Windows"
   - Reinicie o computador

#### "docker: command not found" no PowerShell

**Solução:**

```powershell
# Adicionar Docker ao PATH manualmente
$env:Path += ";C:\Program Files\Docker\Docker\resources\bin"

# Ou reinicie o PowerShell/terminal após instalação
```

#### kind create cluster falha com erro de rede

**Solução:**

```powershell
# Desabilitar IPv6 temporariamente
# Docker Desktop → Settings → Docker Engine
# Adicione: "ipv6": false
# Apply & Restart
```

### Linux

#### Erro de permissão ao executar docker

**Problema:** `permission denied while trying to connect to the Docker daemon socket`

**Solução:**

```bash
# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER

# Aplicar mudanças
newgrp docker

# Ou faça logout e login novamente

# Verificar
docker run hello-world
```

#### Docker service não inicia

**Solução:**

```bash
# Verificar status
sudo systemctl status docker

# Iniciar manualmente
sudo systemctl start docker

# Habilitar inicialização automática
sudo systemctl enable docker

# Ver logs se houver erro
sudo journalctl -u docker.service
```

#### kind create cluster falha com erro "failed to create cluster"

**Soluções:**

1. **Verificar se Docker está rodando:**

```bash
docker ps
```

2. **Verificar se há clusters com mesmo nome:**

```bash
kind get clusters
kind delete cluster --name kind-cluster
```

3. **Criar cluster com configuração específica:**

```bash
cat <<EOF | kind create cluster --name kind-cluster --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
EOF
```

### kubectl não conecta ao cluster

**Problema:** `The connection to the server localhost:8080 was refused`

**Solução:**

```bash
# Verificar contexto
kubectl config get-contexts

# Definir contexto correto
kubectl config use-context kind-kind-cluster

# Verificar arquivo de configuração
kubectl config view

# Se necessário, recriar o cluster
kind delete cluster --name kind-cluster
kind create cluster --name kind-cluster
```

---

## Comandos Úteis

### Docker

```bash
# Ver contêineres em execução
docker ps

# Ver todas as imagens
docker images

# Remover contêineres parados
docker container prune

# Remover imagens não utilizadas
docker image prune

# Ver uso de espaço
docker system df

# Limpar tudo (cuidado!)
docker system prune -a
```

### kind

```bash
# Listar clusters
kind get clusters

# Ver nodes de um cluster
kind get nodes --name kind-cluster

# Carregar imagem Docker no cluster
kind load docker-image <imagem:tag> --name kind-cluster

# Exportar logs do cluster
kind export logs --name kind-cluster

# Deletar cluster
kind delete cluster --name kind-cluster

# Deletar todos os clusters
kind delete clusters --all
```

### kubectl

```bash
# Ver contextos disponíveis
kubectl config get-contexts

# Mudar de contexto
kubectl config use-context <nome-contexto>

# Ver recursos no cluster
kubectl get all -A

# Ver nodes
kubectl get nodes

# Aplicar manifesto
kubectl apply -f <arquivo.yaml>

# Ver logs de um pod
kubectl logs <nome-pod>

# Executar comando em pod
kubectl exec -it <nome-pod> -- /bin/sh

# Port-forward
kubectl port-forward svc/<nome-servico> 8080:8080
```

---

## Recursos Adicionais

- **Docker Docs:** <https://docs.docker.com/>
- **kind Docs:** <https://kind.sigs.k8s.io/>
- **kubectl Docs:** <https://kubernetes.io/docs/reference/kubectl/>
- **Kubernetes Learning:** <https://kubernetes.io/docs/tutorials/>

---

## Próximos Passos

Após instalar e verificar todas as ferramentas, volte para o [README.md](README.md) principal para continuar com o build e deploy do microserviço.
