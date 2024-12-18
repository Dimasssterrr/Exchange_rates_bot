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
public class GetSubscriptionCommand extends Command {

    private final SubscriberService subscriberService;

    public GetSubscriptionCommand(SubscriberService subscriberService) {
        super("get_subscription", "Возращает все подписки");
        this.subscriberService = subscriberService;
    }


    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        StringBuilder sub = new StringBuilder();
        List<Subscription> subscriptions = subscriberService.getAllSubscription(message.getFrom().getId());
        if (subscriptions.isEmpty()) {
            message.setText("У Вас нет подписок &#129335");
            super.processMessage(absSender,message,strings);
        }
        subscriptions.forEach(subscription -> {
            sub.append("\n").append(subscription.getCharCode()).append(" - ").append(subscription.getPrice()).append(" руб.");
        });
        message.setText("Вы подписаны на: " + sub);
        super.processMessage(absSender, message, strings);
    }
}
