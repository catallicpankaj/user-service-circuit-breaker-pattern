package com.sample.userservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "url", "text" })
@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Support {

	@JsonProperty("url")
	public String url;
	@JsonProperty("text")
	public String text;

}