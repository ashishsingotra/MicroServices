package com.ash.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy proxy;

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(
			@PathVariable String from ,@PathVariable String to ,
			@PathVariable BigDecimal quantity ) {
		
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}",
				CurrencyConversion.class, uriVariables);
		
		CurrencyConversion currencyConversion = responseEntity.getBody();
//		currencyConversion.setQuantity(quantity);
//		currencyConversion.setTotalCalculatedAmount((quantity.multiply(currencyConversion.getConversionMultiple())));
//		
//		return currencyConversion;
		
		return new CurrencyConversion(currencyConversion.getId(),
				from,to,
				currencyConversion.getConversionMultiple(),
				currencyConversion.getEnvironment() + " response entity",
				quantity, 
				quantity.multiply(currencyConversion.getConversionMultiple()) );
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(
			@PathVariable String from ,@PathVariable String to ,
			@PathVariable BigDecimal quantity ) {
		
		
		
		CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
//		currencyConversion.setQuantity(quantity);
//		currencyConversion.setTotalCalculatedAmount((quantity.multiply(currencyConversion.getConversionMultiple())));
		
		return new CurrencyConversion(currencyConversion.getId(),
				from,to,
				currencyConversion.getConversionMultiple(),
				currencyConversion.getEnvironment() + " feign",
				quantity, 
				quantity.multiply(currencyConversion.getConversionMultiple()) );
	}
	
}
