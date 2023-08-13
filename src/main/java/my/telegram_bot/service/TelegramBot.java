package my.telegram_bot.service;

import my.telegram_bot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public TelegramBot(BotConfig config) {
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
                    startCommandReceived( chatId, update.getMessage().getChat().getUserName() );
                    break;

                default: sendMessage( chatId," Команда не поддерживается" );

            }
        }

    }

    private void startCommandReceived(long chatId, String name)  {
        String answer = "Hi " + name;

        sendMessage( chatId, answer );

    }

    private void sendMessage(long chatId, String message)  {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId( String.valueOf( chatId ) );
        sendMessage.setText( message );


        try{
            execute( sendMessage );
        } catch(TelegramApiException e){
            e.printStackTrace();
        }

    }
}
