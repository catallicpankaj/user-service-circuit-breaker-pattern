package com.sample.userservice.model;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

import lombok.Builder;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalData {

	private String lastName;
	private String avatar;
}
