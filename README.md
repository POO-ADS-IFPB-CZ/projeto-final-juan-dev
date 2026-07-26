# Sistema de Ordem de Serviço - Oficina Mecânica

Projeto final da disciplina de Programação Orientada a Objetos. Sistema desktop para gerenciamento de ordens de serviço de uma oficina mecânica, com cadastro de clientes, veículos, equipes, mecânicos, serviços, peças e ordens de serviço completas.

## Tecnologias

- **Java**
- **Swing** (interface gráfica)
- **JDBC** com **PostgreSQL** (banco de dados hospedado no [Supabase](https://supabase.com))
- **Maven** (gerenciamento de dependências e build)
- Arquitetura **MVC + DAO**

## Estrutura do projeto

```
src/main/java/
├── model/        → Entidades de domínio (Cliente, Veiculo, OrdemServico, etc.)
├── dao/          → Acesso a dados via JDBC (um DAO por entidade persistida)
├── controller/   → Regras de negócio e validações, ponte entre View e DAO
├── view/         → Telas Swing (uma por entidade + TelaPrincipal como menu)
└── Main.java     → Ponto de entrada da aplicação
```

## Entidades e relacionamentos

- **Cliente** 1:N **Veiculo**
- **Equipe** 1:N **Mecanico**
- **OrdemServico** N:1 **Veiculo**, N:1 **Equipe**
- **OrdemServico** N:N **Servico** (via `ItemServico`)
- **OrdemServico** N:N **Peca** (via `ItemPeca`)

## Como rodar o projeto

### 1. Configurar o banco de dados (Supabase)

Veja o passo a passo completo em [`CONFIGURACAO_SUPABASE.md`](./CONFIGURACAO_SUPABASE.md).

Resumo:
1. Rode o script `schema_os_mecanica_postgres.sql` no SQL Editor do seu projeto Supabase
2. Pegue a string de conexão (Direct Connection, porta 5432) em Project Settings → Database
3. Copie `src/main/resources/config.properties.example` para `src/main/resources/config.properties` e preencha com seus dados reais (host, usuário, senha)

> O arquivo `config.properties` contém credenciais sensíveis e **não é versionado** (já está no `.gitignore`). Apenas o `.example`, sem dados reais, vai para o repositório.

### 2. Rodar com Maven

Pelo terminal, na raiz do projeto:

```bash
mvn compile exec:java
```

Ou gerar um `.jar` executável com as dependências embutidas:

```bash
mvn package
java -jar target/os-mecanica.jar
```

### 3. Rodar pelo IntelliJ IDEA

1. Abra a pasta do projeto no IntelliJ (ele detecta o `pom.xml` automaticamente e baixa as dependências)
2. Aguarde o Maven sincronizar (ícone de progresso no canto inferior direito)
3. Abra `src/main/java/Main.java`
4. Clique no ▶️ verde ao lado do método `main`

## Funcionalidades

- CRUD completo de Cliente, Veículo, Equipe, Mecânico, Serviço e Peça
- Cadastro de Ordem de Serviço com adição dinâmica de itens (serviços e peças), cálculo automático do valor total
- Baixa automática de estoque ao usar peças em uma OS
- Transações no banco (salvar OS + itens é uma operação atômica: ou tudo é salvo, ou nada é)
- Validações de regra de negócio e tratamento de erros (ex: impedir exclusão de cliente com veículos cadastrados)

## Autor

Juan — Disciplina de Programação Orientada a Objetos
