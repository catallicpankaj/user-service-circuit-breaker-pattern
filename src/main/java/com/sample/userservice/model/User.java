package com.sample.userservice.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data", "support" })
@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@JsonProperty("data")
	public Data data;
	
	@JsonProperty("support")
	public Support support;
	
	public Integer randomValue;

}