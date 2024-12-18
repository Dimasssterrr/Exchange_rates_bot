package com.example.Exchange_rates_bot.bot.command;

import com.example.Exchange_rates_bot.Service.NewsLetterService;
import com.example.Exchange_rates_bot.Service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class ReceiveCostEveryDay extends Command {
    private final NewsLetterService newsLetterService;
    private final SubscriberService subscriberService;

    public ReceiveCostEveryDay(SubscriberService subscriberService, NewsLetterService newsLetterService) {
        super("receive_cost", "Отправляет стоимость каждый день");

        this.newsLetterService = newsLetterService;
        this.subscriberService = subscriberService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        newsLetterService.createNewsletter(message);
        String text = "";
        if(subscriberService.checkCharCode(message)) {
          text =   NewsLetterService.isCreate.get()?
                 "Теперь Вам ежедневно будут приходить курсы валют, которые Вы указали) &#128065;" : "Такая подписка уже существует";
        } else {
            text = "Не верно указан трехбуквенный код,такой валюты не существует &#129335. Попробуйте еще раз &#x1F609;!";
        }
        message.setText(text);
        super.processMessage(absSender,message,strings);
    }
}
