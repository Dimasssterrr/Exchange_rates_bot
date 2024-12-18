package com.example.Exchange_rates_bot.bot.command;

import com.example.Exchange_rates_bot.Service.CurrencyService;
import com.example.Exchange_rates_bot.Service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
public class ConvertCurrencyCommand extends Command {
    private final CurrencyService currencyService;
    private final SubscriberService subscriberService;

    public ConvertCurrencyCommand(CurrencyService currencyService, SubscriberService subscriberService) {
        super("convert", "Конвертирует указанную сумму");
        this.currencyService = currencyService;
        this.subscriberService = subscriberService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        String text = "";
        if (subscriberService.getCharCode(message).isEmpty()) {
            text = "&#10060; Вы не указали трехбуквенный код валюты! &#129335. Попробуйте еще раз &#x1F609;!";
            message.setText(text);
            super.processMessage(absSender, message, strings);
            return;
        }
        if (!subscriberService.checkCharCode(message)) {
            text = "&#10060; Не верно указан трехбуквенный код,такой валюты не существует. Или валюта не указана &#129335. Попробуйте еще раз &#x1F609;!";
            message.setText(text);
            super.processMessage(absSender, message, strings);
            return;
        }

        Double value = currencyService.convertCurrency(message);
        text = value == null ? "&#10060; Не указана сумма которую Вы хотите конвертировать.Попробуйте снова &#x1F609" : "Отлично! Конвертируем...";
        message.setText(text);
        super.processMessage(absSender, message, strings);
        if (value != null) {
            message.setText("Получается <b> " + value + " руб. </b>");
            super.processMessage(absSender, message, strings);
        }
    }
}
