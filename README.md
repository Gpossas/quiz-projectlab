# Pré-requisitos
## 🔑 1. Configuração da API Key do Google Gemini
1. Crie uma chave de API no site: https://aistudio.google.com/app/apikey
2. Copie a chave gerada
3. Crie a variável de ambiente -> GOOGLE_API_KEY=valor_da_chave_gerada

## 2. Ter JDK instalado
https://www.oracle.com/br/java/technologies/downloads

## 3. Clonar o projeto
Execute no terminal: `git clone https://github.com/Gpossas/quiz-projectlab.git`

# Como executar o projeto Spring Boot

## Opção 1: Usuário final(apenas executar a aplicação) 
### Pré-requisitos
- Certifique-se de ter instalado: **Docker Desktop**
- Na raiz do projeto crie um arquivo .env com as variáveis que estão no arquivo .env.example
### Executando o projeto
- execute o comando docker `docker compose up -d`

### isso irá:
- Criar a imagem
- Criar o container com a aplicação backend e base de dados postgres
- Iniciar a aplicação


## Opção 2: Desenvolvedores(mudanças feitas ao código são refletidas na hora)
### Pré-requisitos
- Importar as variáveis de ambiente para o springboot

  - No intelliJ
    - importe pelas configurações

  - No vscode
    - Na raiz do projeto crie um arquivo .env com as variáveis que estão no arquivo .env.example
    - Execute no terminal: `export $(grep -v '^#' .env | xargs)`

### Executando o projeto
- No intelliJ
  - Execute a classe QuizAiApplication

- No vscode
  - Execute no terminal `./mvnw spring-boot:run`

# Documentação da API (Swagger)
- Execute a aplicação
- No navegador acesse: http://localhost:8080/swagger-ui.html

# Documentação endpoints Websockets

## Jogador entra na sala
```
SEND: /quizAI/sendPlayerJoin/{roomId}
 Payload: {
    "scoreId": "UUID"
 }
```

```
SUBSCRIBE: /topic/rooms/{roomId}/join 
 Response: {
    "scoreId": "UUID",
    "score": 0,
    "player": {
        "id": "UUID",
        "username": "string"
    }
 }
```

## Jogador sai da sala

```
SEND: /quizAI/sendPlayerLeft/{roomId}
 Payload: {
    "scoreId": "UUID"
 }
```

```
SUBSCRIBE: /topic/rooms/{roomId}/exit
 Response: {
    "scoreId": "UUID",
    "player": {
        "id": "UUID",
        "username": "string"
    }
 }
```

## Jogador pontua

```
SEND: /quizAI/sendPlayerScore/{roomId}
 Payload: {
    "scoreId": "UUID",
    "pointsEarned": 10
 }
```

```
SUBSCRIBE: /topic/rooms/{roomId}/update-score
 Response: {
    "scoreId": "UUID",
    "player": {
        "id": "UUID",
        "username": "string"
    }
    "pointsEarned": 10
 }
```

---


## Iniciar partida

#### Dono da sala envia pedido para iniciar partida
```
SEND: /quizAI/sendStartMatch/{roomId}
 Payload: {
    "playerId": "UUID"
 }
```

#### Contagem regressiva para começar o quiz
```
SUBSCRIBE: /topic/room/{roomId}/start-match-countdown
 Response: {
    "timeRemainingInSeconds": 5
 }
```

#### Servidor enviará a questão atual
```
SUBSCRIBE: /topic/room/{roomId}/question
 Response: {
    "questionId": "UUID",
    "description": "string",
    "answers": {
        "answerId": "UUID",
        "description": "string"
    }
 }
```

#### Contagem regressiva para a outra questão
```
SUBSCRIBE: /topic/room/{roomId}/question-countdown
 Response: {
    "timeRemainingInSeconds": 15
 }
```
