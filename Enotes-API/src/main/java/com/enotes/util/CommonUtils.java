package com.enotes.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.enotes.handler.GenericResponse;

public class CommonUtils {

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {

		GenericResponse res = new GenericResponse();

		res.setResponseStatus(status);
		res.setStatus("success");
		res.setMessage("success");
		res.setData(data);

		ResponseEntity<?> response = res.create();
		return response;
	}

	public static ResponseEntity<?> createBuildResponseMessage( String message, HttpStatus status) {

		GenericResponse res = new GenericResponse();

		res.setResponseStatus(status);
		res.setStatus("success");
		res.setMessage(message);

		ResponseEntity<?> response = res.create();
		return response;
	}
	
	
	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {

		GenericResponse res = new GenericResponse();
		res.setResponseStatus(status);
		res.setStatus("failed");
		res.setMessage("failed");
		res.setData(data);

		ResponseEntity<?> response = res.create();
		return response;
	}
	

	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {

		GenericResponse res = new GenericResponse();
		res.setResponseStatus(status);
		res.setStatus("failed");
		res.setMessage(message);

		ResponseEntity<?> response = res.create();
		return response;
	}
	
	

}
