package com.example.Exchange_rates_bot.configuration;

import com.example.Exchange_rates_bot.bot.ExchangeRatesBot;
import com.example.Exchange_rates_bot.entity.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Slf4j
public class TelegramBotConfiguration {
    @Bean
    TelegramBotsApi telegramBotsApi (ExchangeRatesBot exchangeRatesBot) {
        TelegramBotsApi botsApi = null;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(exchangeRatesBot);
        } catch (TelegramApiException ex) {
            log.error("Error occurred while sending message to telegram!", ex);
        }
        return botsApi;
    }
}
