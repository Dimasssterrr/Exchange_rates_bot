package com.example.Exchange_rates_bot.bot.command;

import com.example.Exchange_rates_bot.Service.CurrencyService;
import com.example.Exchange_rates_bot.Service.SubscriberService;
import com.example.Exchange_rates_bot.bot.kyeBoard.KyeBoard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@Slf4j
public class StartCommand extends Command {
    public final KyeBoard kyeBoard;
    public final CurrencyService currencyService;
    public final SubscriberService subscriberService;

    public StartCommand(KyeBoard kyeBoard, CurrencyService currencyService, SubscriberService subscriberService) {
        super("start", "Запускает бота");
        this.kyeBoard = kyeBoard;
        this.currencyService = currencyService;
        this.subscriberService = subscriberService;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        message.setText(happyNewYear() + readText("./src/main/resources/text/text.txt"));
        message.setReplyMarkup(kyeBoard.addKeyBoard());
        happyNewYear();
        try {
            absSender.execute(sendPhotoMessage(message));
            super.processMessage(absSender,message,strings);
            subscriberService.createSubscriber(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
    public String happyNewYear() {
        if(LocalDateTime.now().isBefore(LocalDateTime.now().plusDays(25))) {
            return "&#127876; &#127876; &#127876;";
        }
        return "";
    }
    public SendPhoto sendPhotoMessage(Message message) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(message.getChatId());
        InputFile inputFile = new InputFile().setMedia(new File("./src/main/resources/image/in.jpg"));
        photo.setPhoto(inputFile);
        return photo;
    }
    public static String readText(String way) {
        Path path = Paths.get(way);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
