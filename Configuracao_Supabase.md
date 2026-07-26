# Configurando o projeto com Supabase (PostgreSQL)

## 1. Pegar a string de conexão no Supabase

1. Acesse seu projeto no [supabase.com](https://supabase.com)
2. Vá em **Project Settings** (ícone de engrenagem) → **Database**
3. Em **Connection string**, selecione a aba **JDBC**
4. Escolha **Direct connection** (porta 5432)
5. Copie a connection string — algo como:
   ```
   jdbc:postgresql://db.xxxxxxxxxxxx.supabase.co:5432/postgres?sslmode=require
   ```
6. A senha é a que você definiu **na criação do projeto** (se esqueceu, dá pra resetar em Database → Settings → Reset database password)

## 2. Configurar o arquivo de credenciais (sem expor a senha no código)

Em vez de deixar a senha escrita direto no código-fonte, o projeto lê os dados de conexão de um arquivo externo (`config.properties`), que **não vai para o Git**.

1. Vá até `src/main/resources/`
2. Copie o arquivo `config.properties.example` e renomeie a cópia para `config.properties` (mesma pasta)
3. Abra `config.properties` e preencha com os dados reais do seu projeto Supabase:

```properties
db.host=db.xxxxxxxxxxxx.supabase.co
db.port=5432
db.database=postgres
db.user=postgres
db.password=sua_senha_real_aqui
```

O `config.properties` já está listado no `.gitignore`, então mesmo que você esqueça, ele não vai ser commitado por acidente. Só o `config.properties.example` (sem dados reais) é versionado — é o "molde" que qualquer pessoa usa pra saber quais campos preencher ao clonar o repositório.

## 3. Adicionar o driver JDBC do PostgreSQL

Diferente do MySQL, agora você precisa do driver `postgresql` (não mais `mysql-connector-j`).

### Se estiver usando Maven (`pom.xml`):
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.7</version>
</dependency>
```

### Se estiver usando Gradle (`build.gradle`):
```groovy
implementation 'org.postgresql:postgresql:42.7.7'
```

### Se for adicionar o `.jar` manualmente (sem Maven/Gradle):
1. Baixe em: https://jdbc.postgresql.org/download/
2. No seu IDE (IntelliJ/Eclipse/NetBeans), adicione o `.jar` ao classpath/build path do projeto

## 4. Rodar o schema no banco

1. No painel do Supabase, vá em **SQL Editor** (ícone à esquerda)
2. Clique em **New query**
3. Cole o conteúdo do arquivo `schema_os_mecanica_postgres.sql`
4. Clique em **Run**

Isso cria todas as 9 tabelas, os índices e já insere alguns dados de exemplo pra você testar.

## 5. Testar a conexão

Rode a classe `Main.java`. Se tudo estiver certo, a Tela Principal abre e, ao clicar em "Clientes", a tabela já deve mostrar os 2 clientes de exemplo (João Silva e Maria Souza).

Se der erro de conexão, confira:
- Senha está correta?
- Sua rede/firewall da faculdade bloqueia a porta 5432? (Se sim, veja a alternativa "Connection Pooling" abaixo)
- O `sslmode=require` está na URL?

## Diferenças técnicas MySQL → PostgreSQL que já foram ajustadas no código

| Item | MySQL | PostgreSQL |
|---|---|---|
| Auto incremento | `AUTO_INCREMENT` | `SERIAL` |
| Driver JDBC | `mysql-connector-j` | `org.postgresql:postgresql` |
| Prefixo da URL | `jdbc:mysql://` | `jdbc:postgresql://` |
| Erro de FK violada | `errorCode == 1451` | `SQLState == "23503"` |
| Erro de UNIQUE violado | mensagem contém "Duplicate entry" | `SQLState == "23505"` |

Essas mudanças já foram aplicadas em `ConnectionFactory.java` e nos `Controller`s (`ClienteController`, `VeiculoController`, `EquipeController`, `MecanicoController`, `ServicoController`, `PecaController`).

## Caso a porta 5432 seja bloqueada (rede da faculdade, eduroam, etc.)

Algumas redes institucionais bloqueiam portas não-padrão como a 5432. Se isso acontecer, use o **Connection Pooling** do Supabase (porta 6543) como alternativa — a URL muda ligeiramente:

```
jdbc:postgresql://aws-0-[regiao].pooler.supabase.com:6543/postgres?sslmode=require
```

E o usuário também muda de formato: `postgres.[PROJECT-REF]` em vez de só `postgres`. Essa informação também está disponível no mesmo painel de Connection String, na aba "Transaction pooler" ou "Session pooler".

## Por que isso é mais seguro?

Como o repositório vai para o GitHub Classroom (visível para o professor, e potencialmente público), deixar a senha do banco escrita direto no código é um risco — qualquer pessoa com acesso ao repositório veria a senha.

Com o arquivo `config.properties` fora do Git:
- A senha real fica só na sua máquina
- O código-fonte (`ConnectionFactory.java`) não contém nenhuma credencial, só a *lógica* de como ler a configuração
- Se precisar trocar a senha, basta editar o arquivo `.properties` — não precisa mexer no código nem recompilar manualmente

Essa é uma prática comum em projetos profissionais reais (geralmente combinada com variáveis de ambiente em produção, mas para um projeto acadêmico rodando localmente, o arquivo `.properties` ignorado pelo Git já resolve bem o problema).
