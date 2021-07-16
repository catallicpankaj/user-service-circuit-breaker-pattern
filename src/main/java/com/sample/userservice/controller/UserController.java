package com.sample.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.userservice.model.AdditionalData;
import com.sample.userservice.model.AllUsers;
import com.sample.userservice.model.User;
import com.sample.userservice.service.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class UserController {

	@Autowired
	@Qualifier("userService")
	UserService userService;

	@GetMapping("/user/{id}")
	public Mono<ResponseEntity<User>> getUserDetail(@PathVariable(name = "id") String id,
	        @RequestParam(name = "delayTime") String delayTime) {
		log.info("Getting UserDetail for Id-{}", id);
		return userService.getUserDetails(id, delayTime)
		        .map(user -> ResponseEntity.ok(user))
		        .defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping("/users")
	public Mono<ResponseEntity<AllUsers>> getAllUserDetail(@RequestParam(name = "pageNumber") String pageNumber,
	        @RequestParam(name = "delayTime") String delayTime) {
		log.info("Getting AllUsersDetail for pageNumber-{}", pageNumber);
		return userService.getAllUserDetails(pageNumber, delayTime)
		        .map(user -> ResponseEntity.ok(user))
		        .defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping("/user/{id}/additionalDetails")
	public Mono<ResponseEntity<AdditionalData>> getAdditionalDetailsForUser(@PathVariable(name = "id") String id,
	        @RequestParam(name = "delayTime") String delayTime, @RequestParam(name = "pageNumber") String pageNumber) {
		log.info("Getting AllUsersDetail for pageNumber-{}", pageNumber);
		return userService.getAdditionalDetails(id, delayTime, pageNumber).map(p -> {
			AdditionalData additionalData = AdditionalData.builder()
			        .lastName(p.getData().getLastName())
			        .avatar(p.getData().getAvatar())
			        .build();
			return ResponseEntity.ok(additionalData);
		}).defaultIfEmpty(ResponseEntity.notFound().build());

	}
}
