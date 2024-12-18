package com.example.Exchange_rates_bot.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
@Slf4j
public abstract class Command implements IBotCommand {
     private final String commandIdentifier;
     private final String description;

    public Command(String commandIdentifier, String description) {
        this.commandIdentifier = commandIdentifier;
        this.description = description;
    }

    @Override
    public String getCommandIdentifier() {
        return commandIdentifier;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(message.getText());
        answer.enableHtml(true);
        answer.setReplyMarkup(message.getReplyMarkup());
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
