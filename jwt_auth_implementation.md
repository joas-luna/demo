# Manual Completo de Autenticação JWT Simplificada (Spring Boot)

Este guia apresenta o passo a passo detalhado de todas as alterações e criações necessárias para fazer a autenticação JWT simplificada (sem complexidade de roles/UserDetails) funcionar no projeto.

---

## 🏗️ 1. Dependências do Projeto (`build.gradle.kts`)
Adicionamos o suporte à segurança e à manipulação de JSON Web Tokens (JWT).

* **Spring Boot Starter Security:** Habilita os filtros de segurança e proteção de rotas HTTP.
* **Auth0 Java-JWT (4.4.0):** Biblioteca simples que fornece builders limpos para geração e verificação do token JWT.

No bloco `dependencies { ... }` do arquivo `build.gradle.kts`:
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.auth0:java-jwt:4.4.0")
    // ... outras dependências
}
```

---

## 🔒 2. Configurações Globais (`application.properties`)
Configuramos a chave secreta de assinatura na raiz das propriedades da aplicação, evitando salvar segredos estáticos no código.

```properties
# Segredo seguro de 256 bits para assinar/validar os tokens JWT
api.security.token.secret=9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b
```

---

## 🛡️ 3. Configuração do Spring Security (`SecurityConfig.java`)
Gerenciador centralizado da segurança. Ele decide o que é público e o que é privado.

* **Desativação de CSRF:** Como a autenticação é via token (stateless), desabilitamos a proteção de Cookie/Session para evitar bloqueios de requisições externas.
* **Sessão Stateless:** Define que a aplicação não mantém estado em sessão no servidor.
* **Mapeamento de Acessos (`authorizeHttpRequests`):**
  * **Públicos:** `/auth/login`, `POST /usuarios`, documentação OpenAPI/Swagger e console H2.
  * **Protegidos:** Qualquer outro endpoint da aplicação (`.anyRequest().authenticated()`).
* **Headers FrameOptions:** Desabilitado apenas para o console H2 renderizar corretamente seus painéis HTML baseados em `<frame>`.
* **Bean `PasswordEncoder`:** Registra o `BCryptPasswordEncoder` para ser usado na codificação de senhas em toda a aplicação.

---

## 🪙 4. Geração e Validação de Tokens (`TokenService.java`)
Gerencia a lógica pura do JSON Web Token utilizando o segredo definido no `application.properties`:

1. **Geração (`gerarToken`):**
   * Usa o e-mail do usuário como o dono/assunto do token (`withSubject`).
   * Configura o emissor como `"clinica-veterinaria-api"` (`withIssuer`).
   * Adiciona um tempo de expiração de **2 horas** a partir do horário local do Brasil.
   * Assina digitalmente o token com o algoritmo HMAC256 usando o segredo injetado.
2. **Validação (`obterSubject`):**
   * Decodifica o token enviado na requisição, verifica se a assinatura secreta bate e se o token não expirou.
   * Retorna o e-mail do dono se for válido.

---

## 🔍 5. Interceptador de Requisições (`SecurityFilter.java`)
Um filtro customizado (`OncePerRequestFilter`) que intercepta toda e qualquer chamada HTTP recebida:

1. **Extração do Cabeçalho:** Lê o cabeçalho `Authorization` e obtém o token JWT limpo (após a palavra chave `Bearer `).
2. **Identificação do Usuário:** Se houver um token válido, o `TokenService` extrai o e-mail associado.
3. **Autenticação:** O filtro consulta o usuário no banco de dados e registra a credencial autenticada no contexto do Spring Security (`SecurityContextHolder`) informando uma lista vazia de permissões (sem Roles):
   ```java
   var authentication = new UsernamePasswordAuthenticationToken(usuario.get(), null, List.of());
   SecurityContextHolder.getContext().setAuthentication(authentication);
   ```
4. **Cadeia de Filtros:** Libera a requisição para seguir seu curso normal.

---

## 🔑 6. Endpoint de Login (`AuthController.java`)
Controlador com a rota `/auth/login` que recebe dados de login no formato JSON (`LoginDTO`):

1. Recupera o usuário do banco através do e-mail.
2. Compara a senha informada com a senha criptografada guardada no banco usando:
   ```java
   passwordEncoder.matches(dados.senha(), usuario.getSenha())
   ```
3. Se os dados forem válidos, gera o token de acesso e retorna uma resposta `200 OK` contendo o `TokenDTO` (`{"token": "..."}`).

---

## 📝 7. Criptografia no Fluxo de Usuários (`UsuarioService.java`)
Garante que todas as senhas cadastradas ou editadas passem pelo codificador seguro:

* No `criaUsuario`, a senha enviada no formulário é codificada antes de salvar:
  ```java
  passwordEncoder.encode(usuarioDTO.senha())
  ```
* No `atualizaUsuario`, caso seja informada uma nova senha, ela também é criptografada antes do update.

---

## 🚀 Como Executar o Fluxo Completo de Testes (Manual)

### Passo 1: Cadastro de Usuário (Rota Pública)
Envie uma requisição para criar um usuário que será o dono dos pets:
* **Método:** `POST`
* **URL:** `http://localhost:8081/usuarios`
* **JSON:**
```json
{
  "nome": "Ana Maria",
  "email": "ana@example.com",
  "senha": "senhaSuperSegura123",
  "pais": "Brasil"
}
```

### Passo 2: Login e Obtenção do Token JWT (Rota Pública)
Envie as credenciais cadastradas para validar a senha e receber o token:
* **Método:** `POST`
* **URL:** `http://localhost:8081/auth/login`
* **JSON:**
```json
{
  "email": "ana@example.com",
  "senha": "senhaSuperSegura123"
}
```
* **Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Passo 3: Acesso a Rotas Protegidas (Autenticado)
Para buscar ou inserir dados protegidos (como listar os pets do sistema):
* **Método:** `GET`
* **URL:** `http://localhost:8081/pets`
* **Cabeçalhos HTTP (Headers):**
  * `Authorization`: `Bearer <COLE_O_TOKEN_AQUI>`
