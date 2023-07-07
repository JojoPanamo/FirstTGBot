package com.example.JojoBotTG1.service;

import com.example.JojoBotTG1.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    } catch (TelegramApiException e) {
                        log.error("Error occurred: " + e.getMessage());
                    }
                    break;
                case "/weather" :
                    try {
                        weatherCommandReceived(chatId);
                    } catch (TelegramApiException e) {
                        log.error("Error occurred: " + e.getMessage());
                    }
                    break;
                //default: sendMessage(chatId, "Сорян, пока не могу понять чего ты мне напиcал");

                }
            }
        }

    public void startCommandReceived(long chatId, String name) throws TelegramApiException{

        String answer = "Приветик, " + name;
        sendMessage(chatId, answer);
        log.info("replied to user " + name);

    }
    public void weatherCommandReceived(long chatId) throws TelegramApiException{

        String answer1 = "Погодка сегодня не очень вот ссылка:" +
                " https://www.google.com/search?q=%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0&rlz=1C5CHFA_enTR1010TR1010&oq=&gs_lcrp=EgZjaHJvbWUqCQgAECMYJxjqAjIJCAAQIxgnGOoCMgkIARAjGCcY6gIyCQgCECMYJxjqAjIJCAMQIxgnGOoCMgkIBBAjGCcY6gIyCQgFECMYJxjqAjIJCAYQIxgnGOoCMgkIBxAjGCcY" +
                "6gLSAQoxNjczNDZqMGo3qAIIsAIB&sourceid=chrome&ie=UTF-8";
        sendMessage(chatId, answer1);

    }

    private void sendMessage (long chatId, String TextToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(TextToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

    }

}
