package com.example.Exchange_rates_bot.bot.command;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
@Service
public class GetPriceCommand extends Command {
    public GetPriceCommand() {
        super("СПИСОК", "Возвращает список валют");
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        message.setText("Выдаю список валют");
        super.processMessage(absSender,message,strings);
    }
}
