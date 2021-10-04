package ru.app;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/app.properties"));
        String token = properties.getProperty("bot.token");

        TelegramBot bot = new TelegramBot(token);
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                try {
                    Parsing_JSON parsing_json;
                    parsing_json = new Parsing_JSON(update.toString());
                    Commands commands = new Commands(parsing_json);
                    bot.execute(new SendMessage(update.message().chat().id(), commands.GetResult()));
                } catch (IOException | SQLException e) {
                    bot.execute(new SendMessage(update.message().chat().id(), e.getMessage()));
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}
