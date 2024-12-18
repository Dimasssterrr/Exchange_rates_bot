package com.example.Exchange_rates_bot.bot;

import com.example.Exchange_rates_bot.Service.CurrencyService;
import com.example.Exchange_rates_bot.bot.kyeBoard.ButtonName;
import com.example.Exchange_rates_bot.entity.Currency;
import com.example.Exchange_rates_bot.entity.Newsletter;
import com.example.Exchange_rates_bot.entity.Subscriber;
import com.example.Exchange_rates_bot.entity.Subscription;
import com.example.Exchange_rates_bot.repository.CurrencyRepository;
import com.example.Exchange_rates_bot.repository.NewsLetterRepository;
import com.example.Exchange_rates_bot.repository.SubscriberRepository;
import com.example.Exchange_rates_bot.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ExchangeRatesBot extends TelegramLongPollingCommandBot {
    private final String botUserName;
    private final CurrencyService service;
    private final CurrencyRepository currencyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriberRepository subscriberRepository;
    private final NewsLetterRepository newsLetterRepository;

    public ExchangeRatesBot(@Value("${telegram.bot.username}") String botUserName,
                            @Value("${telegram.bot.token}") String botToken,
                            List<IBotCommand> commands, CurrencyService service, CurrencyRepository currencyRepository, SubscriptionRepository subscriptionRepository, SubscriberRepository subscriberRepository, NewsLetterRepository newsLetterRepository) {
        super(botToken);
        this.botUserName = botUserName;
        this.service = service;
        this.currencyRepository = currencyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriberRepository = subscriberRepository;
        this.newsLetterRepository = newsLetterRepository;
        commands.forEach(this::register);
    }
    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.getCallbackQuery() != null) {
            return;
        }
        if(update.getMessage().getFrom().getIsBot()) {
            return;
        }
        executeAnswer(update.getMessage().getChatId(),"Неверная команда. Попробуйте еще раз &#x1F609! &#127384; Или воспользуйтесь /help &#128071;");
    }

    @Override
    public void processInvalidCommandUpdate(Update update) {
        super.processInvalidCommandUpdate(update);
    }

    @Override
    public boolean filter(Message message) {
        return super.filter(message);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
        updates.forEach(update -> {
            if (update.getMessage() != null && update.getMessage().isCommand()) {
                if (update.getMessage().getText().contains("Бот помогает:")) {
                    pinMessage(update);
                }
            }
            if (update.getCallbackQuery() == null) {
                return;
            }
            handelButtonClickAllCurrency(update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
            handelButtonClickConvert(update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
            handleButtonClickCurrencyUSD(update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
            handelButtonClickCurrencyEUR(update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
            handelButtonSubscribeRate(update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
            handelButtonSubscribeDaily(update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
        });
    }

    public void handelButtonClickAllCurrency(String callBackData, Long chatId) {
        StringBuilder currency = new StringBuilder();
        try {
            service.getAllCurrency().forEach(currencyDto -> {
                if (!currencyDto.getName().contains("СДР")) {
                    currency.append("за ").append(currencyDto.getNominal()).append(" ").append(currencyDto.getName()).append("/").append(currencyDto.getCharCode()).append("/").append("<b>").append(currencyDto.getValue()).append(" руб.").append("</b>").append(" \n");
                }
            });
        } catch (Exception e) {
            log.error(e.toString());
        }

        if (callBackData.equals(String.valueOf(ButtonName.СПИСОК))) {
            executeAnswer(chatId,"&#x1F30D <b> Официальный курс валют Центробака на " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "</b> \n" + currency);
        }
    }
    public void handleButtonClickCurrencyUSD(String callBackData, Long chatId) {
        if(callBackData.equals(String.valueOf(ButtonName.ДОЛАР))) {
            executeAnswer(chatId,"&#128176;&#128178; Курс доллара на " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss \n")) + "<b>" + currencyRepository.findByCharCode("USD").getValue() + " руб.</b>");;
        }
    }
    public void handelButtonClickCurrencyEUR(String callBackData, Long chatId) {
        if(callBackData.equals(String.valueOf(ButtonName.ЕВРО))) {
            executeAnswer(chatId, "&#8364; Курс евро на " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss \n")) + "<b>" + currencyRepository.findByCharCode("EUR").getValue() + " руб.</b>");
        }
    }
    public void handelButtonClickConvert(String callBackData, Long chatId) {
        if(callBackData.equals(String.valueOf(ButtonName.КОНВЕРТИРОВАТЬ))) {
            executeAnswer(chatId,"Напишите &#9997 мне <b>/convert</b> и сумму которую хотите конвертировать в рубли, затем укажите трехбуквенный код валюты. Пример: /convert 100 USD");
        }
    }
    public void handelButtonSubscribeRate(String updateDate, Long chatId) {
        if(updateDate.equals(String.valueOf(ButtonName.ПОДПИСАТЬСЯ))) {
            executeAnswer(chatId,"Для подписки на курс определенной валюты, напишите мне /subscribe курс на который хотите подписаться и трехбуквенный код валюты. Пример: /subscribe 100 USD");
        }
    }
    public void handelButtonSubscribeDaily(String updateDate, Long chatId) {
        if(updateDate.equals(String.valueOf(ButtonName.РАССЫЛКА))) {
            executeAnswer(chatId, "Для подписки на ежедневную рассылку курсов валют, напишите мне ниже /receive_cost [трехбуквенный код].(Пример: /receive_cost USD). Подписаться можно как на одну валюту, так и на весь список, который предлагается ЦБ.");
        }
    }
    public void executeAnswer(Long id, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(id);
        answer.setText(text);
        answer.enableHtml(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    public void pinMessage(Update update) {
        sendApiMethodAsync(PinChatMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .messageId(update.getMessage().getMessageId())
                .build());
    }

    @Scheduled(cron = "${app.mailing time}")
    @Async
    public void receiveCostEveryDay() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        subscribers.forEach(subscriber -> {
            StringBuilder textAnswerCurrencyCost = new StringBuilder();
            textAnswerCurrencyCost.append("Курсы валют &#128185; &#128182; &#128181; на ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append(": \n");
            List<Newsletter> newsletters = newsLetterRepository.findAllBySubscriberId(subscriber.getId());
            if(newsletters.isEmpty()) {
                return;
            }
            newsletters.forEach(subscription -> {
                Currency currency = currencyRepository.findByCharCode(subscription.getCharCode());
                textAnswerCurrencyCost.append(currency.getCharCode()).append(" - ").append(currency.getValue()).append(" руб. \n");
            });
            executeAnswer(subscriber.getTelegramId(),textAnswerCurrencyCost.toString());
        });
    }
    @Scheduled(cron = "${app.notification time}")
    @Async
    public void timeBuyMessage () {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        List<Currency> currencies = currencyRepository.findAll();
        subscriptions.forEach(subscription -> {
            currencies.forEach(currency -> {
                if(subscription.getCharCode().equals(currency.getCharCode()) && subscription.getPrice() > currency.getValue()) {
                    Subscriber subscriber = subscriberRepository.findById(subscription.getSubscriber().getId()).orElseThrow();
                    executeAnswer(subscriber.getTelegramId(),"Пора покупать! &#128175; " + currency.getName() + " стоит <b> " + currency.getValue() + "</b> руб.");
                }
            });
        });
    }
}
