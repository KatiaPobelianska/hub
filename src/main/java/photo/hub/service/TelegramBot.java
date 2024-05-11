package photo.hub.service;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import photo.hub.config.BotConfig;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    @Value("${gpt.key}")
    private String apiKey;
    @Value("${gpt.model}")
    private String model;
    @Value("${gpt.url}")
    private String url;

    @Autowired
    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                String message = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                if (message.equals("/start")) {
                    sendMessage(chatId, "ask me something");
                } else {
                    sendChatAction(chatId, ActionType.TYPING);
                    sendMessage(chatId, chatGpt(message));
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    public String chatGpt(String message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONObject systemMessageObj = new JSONObject();
        systemMessageObj.put("role", "system");
        systemMessageObj.put("content", "");

        JSONObject userMessageObj = new JSONObject();
        userMessageObj.put("role", "user");
        userMessageObj.put("content", message);

        JSONArray messageArr = new JSONArray();
        messageArr.put(systemMessageObj);
        messageArr.put(userMessageObj);

        JSONObject bodyObj = new JSONObject();
        bodyObj.put("model", model);
        bodyObj.put("messages", messageArr);

        HttpEntity<String> requestEntity = new HttpEntity<>(bodyObj.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String response = responseEntity.getBody();
            return extractContentFromResponse(response);
        } else {
            return null;
        }
    }

    public String extractContentFromResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end).replaceAll("\\\\n", "").replaceAll("\\\\n\\\\n", "");
    }

    @SneakyThrows
    public void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        sendMessage.setText(message);
        execute(sendMessage);
    }

    @SneakyThrows
    private void sendChatAction(long chatId, ActionType actionType) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(actionType);
        execute(sendChatAction);
    }
}
