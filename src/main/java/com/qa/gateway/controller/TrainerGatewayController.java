package com.qa.gateway.controller;

import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.EurekaClient;
import com.qa.gateway.entities.Account;
import com.qa.gateway.entities.Constants;
import com.qa.gateway.entities.CreateAccount;
import com.qa.gateway.entities.Trainer;
@RestController
public class TrainerGatewayController {
	
//	private TrainerGatewayServiceImpl srvc;
	private RestTemplateBuilder rest;
	private EurekaClient client;

	public TrainerGatewayController(RestTemplateBuilder rest, EurekaClient client) {
		
		this.rest = rest;
		this.client = client;
	}
	
	@GetMapping("/getTrainer/{trainerId}")
	public Trainer getTrainer(@PathVariable Long trainerId) {
		HttpEntity<Long> entity = new HttpEntity<>(trainerId);
		return this.rest.build().exchange(client.getNextServerFromEureka(Constants.GETTER, false).getHomePageUrl()+Constants.GET_TRAINER_PATH, 
				HttpMethod.GET, entity, Trainer.class).getBody();
	}
	
	@GetMapping("/getAllTrainers")
	public List<Trainer> getAllTrainers(){
		return this.rest.build().exchange(client.getNextServerFromEureka(Constants.GETTER, false).getHomePageUrl()+Constants.GET_ALL_TRAINERS_PATH, 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Trainer>>(){}).getBody();	
	}
	
	@DeleteMapping("/deleteTrainer/{trainerId}")
	public String deleteTrainer(@PathVariable Long trainerId) {
		HttpEntity<Long> entity = new HttpEntity<>(trainerId);
		return this.rest.build().exchange(client.getNextServerFromEureka(Constants.GETTER, false).getHomePageUrl()+Constants.DELETE_TRAINER_PATH, 
				HttpMethod.DELETE, entity, String.class).getBody();
	}
	private String checkTrainer(Trainer trainer) {
		HttpEntity<Trainer> entity = new HttpEntity<>(trainer);
		return this.rest.build().exchange(client.getNextServerFromEureka(Constants.PLAN, false).getHomePageUrl()+Constants.CHECK_VALID_PATH, 
				HttpMethod.PUT, entity, String.class).getBody();
	}	
	private String saveTrainer(Trainer trainer) {
		HttpEntity<Trainer> entity = new HttpEntity<>(trainer);
		this.rest.build().exchange(client.getNextServerFromEureka(Constants.GETTER, false).getHomePageUrl()+Constants.CREATE_TRAINER_PATH, 
				HttpMethod.POST, entity, String.class).getBody();
		return Constants.VALID_MESSAGE;
	}	

}
