package com.sample.userservice.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.sample.userservice.model.AllUsers;
import com.sample.userservice.model.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("userService")
@Slf4j
public class UserService {

	@Autowired
	@Qualifier("webClientSSL")
	WebClient webClient;
	
	@Value("${sample.api.url}")
	private String sampleApiUrl;
	
	@Autowired
	SampleFailureService sampleFailureService;

	@Autowired
	CacheManager cacheManager;

	/**
	 * Invoking sampleFailureService and Fetching data from another third party service.
	 * Get data from both the above calls and set the random value 
	 * from sampleFailureService mono to the userData object 
	 * and return the mono object.
	 * 
	 * @param id
	 * @param delayTime
	 * @return
	 */
	public Mono<User> getUserDetails(String id, String delayTime) {
		URI getUserUri = UriComponentsBuilder.fromUriString(this.sampleApiUrl)
														.path("/{userId}")
														.queryParam("delay", delayTime)
														.build(id);
		Mono<Integer> monoSampleServiceResponse = sampleFailureService.getSomeValue();
		log.debug("Response recevied from sampleserviceresponse");
		Mono<User> monoUserDataService = webClient.get()
												.uri(getUserUri)
												.headers(header -> {
													header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
												  })
												.retrieve()
												.bodyToMono(User.class);

		Mono<User> mergedMonoData = Mono.zip(monoUserDataService, monoSampleServiceResponse,
												(userData, sampleServiceResponse) -> {
														userData.setRandomValue(sampleServiceResponse);
														return userData;
											});
		return mergedMonoData;
	}

	public Mono<AllUsers> getAllUserDetails(String pageNumber, String delayTime) {
		URI getAllUserUri = UriComponentsBuilder
				.fromUriString(this.sampleApiUrl + "?page=" + pageNumber + "&delay=" + delayTime).build().toUri();
		Mono<AllUsers> allUsersData = webClient.get().uri(getAllUserUri).headers(header -> {
			header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		}).retrieve().bodyToMono(AllUsers.class).log();
		return allUsersData;
	}

	public Mono<User> getAdditionalDetails(String id, String delayTime, String pageNumber) {
		return getAllUserDetails(pageNumber, delayTime)
				.flatMap(users -> Flux.fromIterable(users.getData()).filter(itr -> itr.getId().toString().equals(id))
						.next())
				.map(t -> getUserDetails(t.getId().toString(), delayTime)).flatMap(mapper -> mapper).log();
	}

}
