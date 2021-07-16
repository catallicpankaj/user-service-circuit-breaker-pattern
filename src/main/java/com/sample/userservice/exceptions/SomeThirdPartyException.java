package com.sample.userservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SomeThirdPartyException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7879077921544146150L;
	
	
	private String code;
	private String message;
	

}
