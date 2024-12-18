package com.example.Exchange_rates_bot.bot.command;

import com.example.Exchange_rates_bot.Service.SubscriberService;
import com.example.Exchange_rates_bot.entity.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class SubscribeCommand extends Command {

    private final SubscriberService subscriberService;

    public SubscribeCommand(SubscriberService subscriberService) {
        super("subscribe", "Подписывает на стоимость валюты");
        this.subscriberService = subscriberService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        String text = "";
        Subscription subscription = subscriberService.subscribeCourse(message);
        text = subscriberService.checkCharCode(message) ? "Отлично! Вы подписаны на " + subscription.getCharCode() + ", стоимость <b> " + subscription.getPrice() + "</b> руб. &#128065;" : "Не верно указан трехбуквенный код,такой валюты не существует &#129335. Попробуйте еще раз &#x1F609;!";
        message.setText(text);
        super.processMessage(absSender, message, strings);
    }
}
