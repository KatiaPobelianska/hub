package photo.hub.service;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import photo.hub.config.BotConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Autowired
    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            if (update.getMessage().hasText()){
                String message = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                if (message.equals("/start")){
                    sendMessage(chatId, "ask me something");
                    return;
                }else {
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

    @SneakyThrows
    public String chatGpt(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "";
        String model = "gpt-3.5-turbo-16k";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setRequestProperty("Content-Type", "application/json");

        JSONObject bodyObj = new JSONObject();
        bodyObj.put("model", model);
        JSONArray messageArr = new JSONArray();
        JSONObject systemMessageObj = new JSONObject();

        systemMessageObj.put("role", "system");
        systemMessageObj.put("content", "");

        messageArr.put(systemMessageObj);

        JSONObject userMessageObj = new JSONObject();
        userMessageObj.put("role", "user");
        userMessageObj.put("content", message);
        messageArr.put(userMessageObj);

        bodyObj.put("messages", messageArr);

        String body = bodyObj.toString();
        con.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return extractContentFromResponse(response.toString());
    }

    public String extractContentFromResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end).replaceAll("\\\\n", "").replaceAll("\\\\n\\\\n", "");
    }
    @SneakyThrows
    public void sendMessage(long chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        sendMessage.setText(message);
        execute(sendMessage);
    }
    @SneakyThrows
    private void sendChatAction(long chatId, ActionType actionType){
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(actionType);
        execute(sendChatAction);
    }
}
