# Como executar o projeto Spring Boot
## 🔑 Configuração da API Key do Google Gemini
1. Crie uma chave de API no site: https://aistudio.google.com/app/apikey
2. Copie a chave gerada
3. Crie a variável de ambiente -> GOOGLE_API_KEY=valor_da_chave_gerada

## Executando o projeto
1. git clone https://github.com/Gpossas/quiz-projectlab.git
2. Abra o projeto na IDE e execute a classe QuizAiApplication com o método main

# Documentação da API (Swagger)
http://localhost:8080/swagger-ui.html

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