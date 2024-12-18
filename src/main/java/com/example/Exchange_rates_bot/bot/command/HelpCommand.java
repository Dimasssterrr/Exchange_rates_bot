package com.example.Exchange_rates_bot.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
@Component
@Slf4j
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help","Список команд");
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        message.setText(StartCommand.readText("./src/main/resources/text/description.txt"));
        super.processMessage(absSender,message,strings);
    }
}
