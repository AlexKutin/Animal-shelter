package pro.sky.animalshelter.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnimalShelterBotConfiguration {

    @Bean
    public TelegramBot animalShelter(@Value("${telegram.bot.token}") String botToken) {
        return new TelegramBot(botToken);
    }

}

