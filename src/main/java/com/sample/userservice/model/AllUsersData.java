package com.sample.userservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "email", "first_name", "last_name", "avatar" })
@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUsersData {

	@JsonProperty("id")
	public Integer id;
	
	@JsonProperty("email")
	public String email;
	
	@JsonProperty("first_name")
	public String firstName;

}