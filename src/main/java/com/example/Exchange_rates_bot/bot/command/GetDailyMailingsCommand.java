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
public class GetDailyMailingsCommand extends Command{
    private final NewsLetterService newsLetterService;
    public GetDailyMailingsCommand(NewsLetterService newsLetterService) {
        super("get_daily", "Возращает список валют, ежедневной рассылки");
        this.newsLetterService = newsLetterService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {

        List<Newsletter> newsletters = newsLetterService.getDailyMailings(message);
        if (newsletters.isEmpty()) {
            message.setText("У Вас нет подписок на ежедневную рассылку курсов! &#128181;&#128183;");
        } else {
            StringBuilder list = new StringBuilder();
            list.append("Валюты на которые будет приходить ежедневная рассылка:");
            newsletters.forEach(newsletter -> {
                list.append("\n").append(newsletter.getCharCode());
            });
            message.setText(list.toString());
        }
        super.processMessage(absSender, message, strings);
    }
}
