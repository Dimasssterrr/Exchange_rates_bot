package com.example.Exchange_rates_bot.repository;

import com.example.Exchange_rates_bot.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCharCode(String charCode);
}
