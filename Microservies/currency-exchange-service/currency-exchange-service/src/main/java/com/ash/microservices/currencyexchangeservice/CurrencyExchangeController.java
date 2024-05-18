package com.ash.microservices.currencyexchangeservice;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

	private CurrencyExchangeRepository repository;

	@Autowired
	private Environment environment;

	public CurrencyExchangeController(CurrencyExchangeRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/")
	public String helloWorld() {
		return "hello world";
	}
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
		//CurrencyExchange currencyExchange = new CurrencyExchange(10001L, from, to, BigDecimal.valueOf(50));
		CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);
		if(currencyExchange == null) {
			throw new RuntimeException("unable to find data for " + 
					from + " to " + to);
		}
		String port = environment.getProperty("local.server.port");
		currencyExchange.setEnvironment(port);
		return currencyExchange;
	}

}
