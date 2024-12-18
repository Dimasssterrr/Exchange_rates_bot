package com.example.Exchange_rates_bot.bot.command;

import com.example.Exchange_rates_bot.Service.NewsLetterService;
import com.example.Exchange_rates_bot.entity.Newsletter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@Slf4j
public class CancelReceiveCostEveryDayCommand extends Command{
    private final NewsLetterService newsLetterService;
    public CancelReceiveCostEveryDayCommand(NewsLetterService newsLetterService) {
        super("cancel_receive","Отменяет ежедневную рассылку курсов валют");
        this.newsLetterService = newsLetterService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        String text = "";
        if (message.getText().toLowerCase().contains("all")) {
           List<Newsletter> newsletters = newsLetterService.cancelAllNewsLatter(message);
            text = newsletters.isEmpty() ? "Отсутствуют активные подписки, на рассылку курсов валют!" : "Отменены все подписки на рассылку курсов валют!";
            message.setText(text);
        } else {
            Newsletter newsletter = newsLetterService.cancelNewsLatter(message);
            text = newsletter == null ? "У Вас нет подписки, или не указана валюта &#129335;. Попробуйте еще раз &#128521;!" : "Отменена подписка на рассылку курса " + newsletter.getCharCode();
            message.setText(text);
        }
        super.processMessage(absSender, message, strings);
    }
}
