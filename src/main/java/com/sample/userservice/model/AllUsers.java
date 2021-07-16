package com.sample.userservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllUsers {
	
	@JsonProperty("page")
	public Integer page;
	
	@JsonProperty("per_page")
	public Integer perPage;
	
	@JsonProperty("total")
	public Integer total;
	
	@JsonProperty("total_pages")
	public Integer totalPages;
	
	@JsonProperty("data")
	public List<AllUsersData> data;
	
	@JsonProperty("support")
	public Support support;
}
