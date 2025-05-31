package com.enotes.exception;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.enotes.util.CommonUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	 return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}	
	
	//Pre defined exception NullPointerException
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		 return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
					
	}
	
	// try catch mai nhi hona chaiye khi ResourceNotFoundException 
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(Exception e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		 return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException e) {
//		return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
		 return CommonUtils.createErrorResponse(e.getErrors(),  HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ExistsDataException.class)
	public ResponseEntity<?> handleValidationException(ExistsDataException e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		 return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		 return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		 return CommonUtils.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//
//		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
//		Map<String, Object> error = new HashMap<>();
//		allErrors.stream().forEach(er -> {
//			String msg = er.getDefaultMessage();
//		 	String field = ((FieldError)(er)).getField();
//		 	error.put(field,msg);
//		});
//		
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}
}
