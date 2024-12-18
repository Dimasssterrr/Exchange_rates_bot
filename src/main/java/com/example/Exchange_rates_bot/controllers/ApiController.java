package com.example.Exchange_rates_bot.controllers;

import com.example.Exchange_rates_bot.Service.CurrencyService;
import com.example.Exchange_rates_bot.dto.CurrencyDto;
import com.example.Exchange_rates_bot.dto.Valute;
import com.example.Exchange_rates_bot.handlerXML.HandlerXML;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("api/currency")
@RequiredArgsConstructor
public class ApiController {

    private final HandlerXML handlerXML;
    private final CurrencyService service;
    @GetMapping()
    ResponseEntity<List<Valute>> getCurrency() throws JAXBException, FileNotFoundException, MalformedURLException {
        return ResponseEntity.ok(handlerXML.getValuteList());
    }
    @PostMapping("/create")
    ResponseEntity<List<CurrencyDto>> createCurrency() throws MalformedURLException, JAXBException {
        return new ResponseEntity<>(service.createCurrency(), HttpStatus.CREATED);
    }
}
