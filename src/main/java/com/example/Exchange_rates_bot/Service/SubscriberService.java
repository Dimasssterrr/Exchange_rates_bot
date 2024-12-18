package com.example.Exchange_rates_bot.Service;

import com.example.Exchange_rates_bot.entity.Currency;
import com.example.Exchange_rates_bot.entity.Subscriber;
import com.example.Exchange_rates_bot.entity.Subscription;
import com.example.Exchange_rates_bot.repository.CurrencyRepository;
import com.example.Exchange_rates_bot.repository.SubscriberRepository;
import com.example.Exchange_rates_bot.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final CurrencyRepository currencyRepository;
    private final SubscriptionRepository subscriptionRepository;
    public void createSubscriber(Message message) {
        Subscriber subscriber = new Subscriber();
        subscriber.setTelegramId(message.getFrom().getId());
        subscriber.setName(message.getFrom().getUserName());
        if (subscriberRepository.findByTelegramId(message.getFrom().getId()) == null) {
            subscriberRepository.save(subscriber);
        }
    }

    public Subscription subscribeCourse(Message message) {
        Subscriber subscriber = subscriberRepository.findByTelegramId(message.getFrom().getId());
        Double price = Double.parseDouble(message.getText().replaceAll("[^0-9]", "").replaceAll(",", ".").trim());
        String charCode = getCharCode(message);
        if (!checkCharCode(message)) {
            return null;
        }
        Subscription subscriptionDb = subscriptionRepository.findByCharCode(charCode);
        if (subscriptionDb != null) {
            subscriptionDb.setPrice(price);
            subscriptionDb.setSubscriber(subscriber);
            subscriptionDb.setCharCode(charCode);
            subscriptionRepository.save(subscriptionDb);
            return subscriptionDb;
        }
        Subscription subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setPrice(price);
        subscription.setCharCode(charCode);
        subscriptionRepository.save(subscription);
        return subscription;
    }
    public List<Subscription> getAllSubscription(Long subscriberId) {
    Subscriber subscriber = subscriberRepository.findByTelegramId(subscriberId);
    return subscriptionRepository.findAllBySubscriberId(subscriber.getId());
    }

    public Subscription cancelSubscription(Message message) {
        String charCode = getCharCode(message);
        Subscription subscription = subscriptionRepository.findByCharCode(charCode);
        if (subscription == null) {
            return null;
        }
        Subscriber subscriber = subscriberRepository.findByTelegramId(message.getFrom().getId());
        if(subscription.getCharCode().equals(charCode) && subscriber.getId().equals(subscription.getSubscriber().getId())) {
            subscriptionRepository.delete(subscription);
        }
        return subscription;
    }
    public List<Subscription> cancelAllSubscriptions(Message message) {
        Subscriber subscriber = subscriberRepository.findByTelegramId(message.getFrom().getId());
        subscriptionRepository.deleteAllBySubscriberId(subscriber.getId());
        return subscriptionRepository.findAllBySubscriberId(subscriber.getId());
    }
    public boolean checkCharCode(Message message) {
        Currency currency = currencyRepository.findByCharCode(getCharCode(message));
        return currency != null;
    }

    public String getCharCode(Message message) {
        return message.getText().replaceAll("/\\S+\\s*","").replaceAll(",",".").replaceAll("[0-9]","").trim();
    }
    public Map<Subscriber, List<Currency>> getListSubscriberCurrency(Update update) {
        Subscriber subscriber = subscriberRepository.findByTelegramId(update.getMessage().getFrom().getId());
        List<Subscription> subscriptions = subscriptionRepository.findAllBySubscriberId(subscriber.getId());
        Map<Subscriber, List<Currency>> subscriberListCurrency = new HashMap<>();
        List<Currency> currencies = new ArrayList<>();
        subscriptions.forEach(subscription -> {
            currencies.add(currencyRepository.findByCharCode(subscription.getCharCode()));
        });
        subscriberListCurrency.put(subscriber,currencies);
        return subscriberListCurrency;
    }
    public void dailyMessageCurrency() {

    }
}
