---

# Afrodite Calendar 🗓️

Afrodite Calendar é uma aplicação web segura e estilosa para gerenciamento de contatos pessoais. Construída com Java/Spring Boot no backend, a aplicação é integrada com o Google Firebase para persistência de dados (Firestore) e autenticação de usuários.

## ✨ Funcionalidades

* **Autenticação Segura** : Login de usuário e gerenciamento de sessão via Firebase Authentication.
* **Listagem de Contatos** : Visualize todos os seus contatos com uma funcionalidade de busca em tempo real.
* **Adicionar Contatos** : Adicione novos contatos com validação que impede a duplicidade de telefones na sua agenda.
* **Remover Contatos** : Busque por um contato específico e remova-o com uma etapa de confirmação.
* **Interface Moderna** : UI responsiva com um tema neon, construída com HTML5, CSS3 e Bootstrap.

## 🛠️ Tecnologias Utilizadas

* **Backend** : Java 17+ / Spring Boot
* **Banco de Dados** : Google Firestore (NoSQL)
* **Autenticação** : Firebase Authentication (usando Tokens JWT)
* **Frontend** : HTML5, CSS3, JavaScript, Bootstrap 5
* **Segurança** : Spring Security
* **Testes** : JUnit 5, Mockito
* **Build** : Apache Maven

## 🚀 Configuração e Execução

Siga os passos abaixo para configurar e executar o projeto em sua máquina local.

### Pré-requisitos

* JDK 17 ou superior
* Apache Maven
* Uma conta no Google Firebase

### Passos para Configuração

1. **Clone o repositório:**
   **Bash**

   ```
   git clone <URL_DO_SEU_REPOSITORIO>
   cd <NOME_DO_REPOSITORIO>
   ```
2. **Configure o Projeto Firebase:**

   * Acesse o [Console do Firebase](https://console.firebase.google.com/) e crie um novo projeto.
   * No menu lateral, vá em **Build > Firestore Database** e crie um novo banco de dados (pode iniciar em modo de teste).
   * Vá em  **Build > Authentication** , clique em "Primeiros passos" e habilite o provedor de login  **E-mail/senha** .
   * Ainda em Authentication, na aba "Users", adicione um usuário. **Este será o e-mail e senha que você usará para fazer login na aplicação.**
3. **Configure a Chave de Serviço (serviceAccountKey):**

   * No seu projeto Firebase, clique no ícone de engrenagem (⚙️) e vá em  **Configurações do projeto** .
   * Vá para a aba  **Contas de serviço** .
   * Clique no botão  **"Gerar nova chave privada"** . Um arquivo `.json` será baixado.
   * Renomeie este arquivo para `serviceAccountKey.json` e mova-o para o diretório `src/main/resources/` do seu projeto.
4. **Configure a Chave de API Web:**

   * Nas  **Configurações do projeto** , na aba  **Geral** , role para baixo até a seção "Seus apps".
   * Copie a `apiKey` do objeto de configuração do Firebase para apps da Web.
   * Abra o arquivo `src/main/resources/application.properties`.
   * Cole a chave que você copiou no valor da propriedade `firebase.api.key`.

### Executando a Aplicação

Com tudo configurado, use o Maven para iniciar o servidor Spring Boot:

**Bash**

```
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080/login.html`.

### Executando os Testes

Para rodar a suíte de testes de unidade, execute o seguinte comando:

**Bash**

```
mvn test
```

## 📂 Estrutura do Projeto

```
.
├── src
│   ├── main
│   │   ├── java/com/engdesoftware/agenda
│   │   │   ├── config          # Configurações de segurança e Firebase
│   │   │   ├── controller      # Controladores REST que expõem a API
│   │   │   ├── dto             # Objetos de Transferência de Dados (ex: LoginRequest)
│   │   │   ├── model           # Entidades e lógica de negócio (Contato, Agenda)
│   │   │   └── service         # Camada de serviço com a lógica de persistência e autenticação
│   │   └── resources
│   │       ├── static          # Arquivos de frontend (HTML, CSS, JS, Imagens)
│   │       ├── application.properties
│   │       └── serviceAccountKey.json
│   └── test                    # Testes de unidade
└── ...
```
