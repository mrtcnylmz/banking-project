package com.mrtcnylmz.bankingsystem.Services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CurrencyExchangeService {
	
	public double exchange(String sourceCurrency, String targetCurrency, double amount) throws Exception {
		
		//Accessing to the api service for current exchange rates.
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", "apikey 5qx4WWOHrr9PAwWFLCB7aj:7bQ5LKO3NWFCKPjrtn2lEK");
		headers.add("content-type", "application/json");
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		
		if (!(sourceCurrency.equals("GAU") || targetCurrency.equals("GAU"))) {
			String exchangeUrl = "https://api.collectapi.com/economy/exchange?int=" + amount + "&to=" + targetCurrency + "&base=" + sourceCurrency;
			ResponseEntity<String> apiResponse = restTemplate.exchange(exchangeUrl, HttpMethod.GET, requestEntity, String.class);
			JsonNode responseNode = null;
			
			try {
				responseNode = new ObjectMapper().readTree(apiResponse.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if (responseNode.get("success").asBoolean()) {
				return responseNode.get("result").get("data").get(0).get("calculated").asDouble();
			}else {
				throw new Exception("Failed to process currency exchange.");
			}
		}else {
			String goldUrl = "https://api.collectapi.com/economy/goldPrice";
			ResponseEntity<String> apiGoldResponse = restTemplate.exchange(goldUrl, HttpMethod.GET, requestEntity, String.class);
			JsonNode goldResponseNode = null;
			JsonNode responseNode = null;
			
			try {
				goldResponseNode = new ObjectMapper().readTree(apiGoldResponse.getBody());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			if (!(goldResponseNode.get("success").asBoolean())) {
				throw new Exception("Failed to process currency exchange.");
			}
			
			if (sourceCurrency.equals("GAU")){
				double goldToTryResult = amount * goldResponseNode.get("result").get(0).get("buying").asDouble();
				
				if (targetCurrency.equals("TRY")) {
					return goldToTryResult;
				}else {
					String fromTryToTargetUrl = "https://api.collectapi.com/economy/exchange?int=" + goldToTryResult + "&to=" + targetCurrency + "&base=" + "TRY";
					ResponseEntity<String> apiTryToTargetResponse = restTemplate.exchange(fromTryToTargetUrl, HttpMethod.GET, requestEntity, String.class);
					
					try {
						responseNode = new ObjectMapper().readTree(apiTryToTargetResponse.getBody());
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					
					if (responseNode.get("success").asBoolean()) {
						return responseNode.get("result").get("data").get(0).get("calculated").asDouble();
					}else {
						throw new Exception("Failed to process currency exchange.");
					}
				}
			}else {
				double goldSellingPrice = goldResponseNode.get("result").get(0).get("selling").asDouble();
				if (sourceCurrency.equals("TRY")) {
					return amount / goldSellingPrice;
				}else {
					String fromSourceToTryUrl = "https://api.collectapi.com/economy/exchange?int=" + amount + "&to=" + "TRY" + "&base=" + sourceCurrency;
					ResponseEntity<String> apiSourceToTryResponse = restTemplate.exchange(fromSourceToTryUrl, HttpMethod.GET, requestEntity, String.class);
					
					try {
						responseNode = new ObjectMapper().readTree(apiSourceToTryResponse.getBody());
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					
					if (!responseNode.get("success").asBoolean()) {
						throw new Exception("Failed to process currency exchange.");
					}
					
					double sourceToTryResult = responseNode.get("result").get("data").get(0).get("calculated").asDouble();
					
					return sourceToTryResult / goldSellingPrice;
				}
			}
		}
	}
}
