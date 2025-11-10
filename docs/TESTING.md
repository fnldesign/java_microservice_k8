# Guia de Testes

Este projeto possui testes unitários e testes de integração separados, permitindo executá-los independentemente em pipelines de CI/CD.

## Estrutura dos Testes

### Testes Unitários
Arquivos que **NÃO** terminam com `IntegrationTest.java`:
- `TodoControllerTest.java` - Testes unitários do controller com Mockito
- `TodoTest.java` - Testes da entidade Todo
- `SecurityConfigTest.java` - Teste de configuração de segurança
- `JavaMicroserviceK8ApplicationTests.java` - Teste de contexto

### Testes de Integração
Arquivos que terminam com `IntegrationTest.java`:
- `TodoControllerIntegrationTest.java` - Testes de integração do CRUD de TODOs
- `ApiControllerIntegrationTest.java` - Testes de integração dos endpoints de API
- `TodoRepositoryTest.java` - Testes de integração do repositório

## Comandos para Executar Testes

### 1. Executar TODOS os testes (padrão)
```bash
mvn test
```
ou
```bash
mvn clean verify
```

### 2. Executar APENAS testes unitários
```bash
mvn test -Punit-tests
```
ou
```bash
mvn clean test -Punit-tests
```

**Útil para**: Verificação rápida de lógica de negócio sem dependências externas.

### 3. Executar APENAS testes de integração
```bash
mvn verify -Pintegration-tests
```
ou
```bash
mvn clean verify -Pintegration-tests
```

**Útil para**: Testar com banco de dados H2 in-memory e contexto Spring completo.

### 4. Executar todos os testes (explicitamente)
```bash
mvn verify -Pall-tests
```

### 5. Pular todos os testes (build sem testes)
```bash
mvn package -DskipTests
```

## Exemplo de Pipeline CI/CD

### GitHub Actions

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  unit-tests:
    name: Unit Tests
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Cache Maven packages
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: ${{ runner.os }}-m2
          
      - name: Run Unit Tests
        run: mvn clean test -Punit-tests

  integration-tests:
    name: Integration Tests
    runs-on: ubuntu-latest
    needs: unit-tests
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Cache Maven packages
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: ${{ runner.os }}-m2
          
      - name: Run Integration Tests
        run: mvn clean verify -Pintegration-tests

  build:
    name: Build and Package
    runs-on: ubuntu-latest
    needs: [unit-tests, integration-tests]
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Build with Maven
        run: mvn clean package -DskipTests
        
      - name: Build Docker Image
        run: docker build -t java-microservice-k8:${{ github.sha }} .
```

### GitLab CI

```yaml
stages:
  - test
  - build
  - deploy

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=.m2/repository"

cache:
  paths:
    - .m2/repository

unit-tests:
  stage: test
  image: maven:3.8.8-eclipse-temurin-17
  script:
    - mvn clean test -Punit-tests
  artifacts:
    reports:
      junit:
        - target/surefire-reports/TEST-*.xml

integration-tests:
  stage: test
  image: maven:3.8.8-eclipse-temurin-17
  script:
    - mvn clean verify -Pintegration-tests
  artifacts:
    reports:
      junit:
        - target/failsafe-reports/TEST-*.xml

build:
  stage: build
  image: maven:3.8.8-eclipse-temurin-17
  script:
    - mvn clean package -DskipTests
  artifacts:
    paths:
      - target/*.jar
    expire_in: 1 week
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven 3.8.8'
        jdk 'JDK 17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Unit Tests') {
            steps {
                sh 'mvn clean test -Punit-tests'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Integration Tests') {
            steps {
                sh 'mvn clean verify -Pintegration-tests'
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                sh 'docker build -t java-microservice-k8:${BUILD_NUMBER} .'
            }
        }
    }
}
```

## Relatórios de Teste

### Surefire (Testes Unitários)
Os relatórios são gerados em: `target/surefire-reports/`

### Failsafe (Testes de Integração)
Os relatórios são gerados em: `target/failsafe-reports/`

## Configuração de Testes

### application-test.properties
Localizado em `src/test/resources/application-test.properties`, contém configurações específicas para testes:

```properties
# Test API Key
api.key=test-api-key

# H2 In-Memory Database for Tests
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
```

## Boas Práticas

1. **Execute testes unitários primeiro**: São mais rápidos e identificam problemas de lógica rapidamente
2. **Execute testes de integração após unitários**: Garantem que a aplicação funciona como um todo
3. **Use cache de dependências no CI/CD**: Reduz tempo de build
4. **Publique relatórios de teste**: Facilita identificação de falhas
5. **Falhe rápido**: Configure o CI/CD para parar no primeiro erro

## Troubleshooting

### Testes de integração falhando localmente
- Verifique se nenhuma aplicação está usando a porta 8080
- Certifique-se de ter Java 17 instalado
- Limpe o cache Maven: `mvn clean`

### Testes passam localmente mas falham no CI/CD
- Verifique a versão do Java no CI/CD
- Confirme que o `application-test.properties` está sendo usado
- Verifique logs detalhados com: `mvn test -X`

## Estatísticas dos Testes

| Categoria | Quantidade | Descrição |
|-----------|------------|-----------|
| Testes Unitários | 28 | Testes sem dependências externas |
| Testes de Integração | 14 | Testes com Spring Context |
| **Total** | **42** | Cobertura completa da aplicação |

## Cobertura de Código

Para gerar relatório de cobertura com JaCoCo:

```bash
mvn clean verify jacoco:report
```

Relatório disponível em: `target/site/jacoco/index.html`
