package com.enotes.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.Builder;

@Builder
public class GenericResponse {

	private HttpStatus responseStatus;
	private String status; //success and failed
	private String message; //saved success
	private Object data; //data
	
	public GenericResponse() {
		
	}
	
	public ResponseEntity<?> create() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("status", status);
		map.put("message", message);
		
		if(!ObjectUtils.isEmpty(data)) {
			map.put("data", data);
		}
		
		return new ResponseEntity<>(map, responseStatus);
	}
	
	public GenericResponse(HttpStatus responseStatus, String status, String message, Object data) {
		super();
		this.responseStatus = responseStatus;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public HttpStatus getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(HttpStatus responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
