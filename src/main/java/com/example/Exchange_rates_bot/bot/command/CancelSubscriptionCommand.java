package com.example.Exchange_rates_bot.bot.command;

import com.example.Exchange_rates_bot.Service.SubscriberService;
import com.example.Exchange_rates_bot.entity.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@Slf4j
public class CancelSubscriptionCommand extends Command {

    private final SubscriberService subscriberService;

    public CancelSubscriptionCommand(SubscriberService subscriberService) {
        super("cancel", "Отменяет подписки");
        this.subscriberService = subscriberService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        Subscription subscription = subscriberService.cancelSubscription(message);
        String text = "";
        if(message.getText().toLowerCase().contains("all")) {
            List<Subscription> subscriptions = subscriberService.cancelAllSubscriptions(message);
            text = subscriptions.isEmpty() ? "У Вас отсутствуют активные подписки" : "Все подписки отменены!";
            message.setText(text);
            super.processMessage(absSender,message,strings);
            return;
        }
        text = subscription == null? "У Вас нет подписки, или не указана валюта &#129335;. Попробуйте еще раз &#128521;!":"Подписка на " + subscription.getCharCode() + " отменена!";
        message.setText(text);
        super.processMessage(absSender,message,strings);
    }
}
