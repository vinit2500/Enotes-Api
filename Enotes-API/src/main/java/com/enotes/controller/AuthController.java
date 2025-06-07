package com.enotes.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.LoginRequest;
import com.enotes.dto.LoginResponse;
import com.enotes.dto.UserDto;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		String url = CommonUtils.getUrl(request);
		Boolean register = userService.register(userDto, url);
		if (register) {
			return CommonUtils.createBuildResponseMessage("Register successfull", HttpStatus.CREATED);
		}
		return CommonUtils.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

		LoginResponse loginResponse = userService.login(loginRequest);
		if (ObjectUtils.isEmpty(loginResponse)) {
			return CommonUtils.createErrorResponseMessage("Invalid credential", HttpStatus.BAD_REQUEST);
		}
		return CommonUtils.createBuildResponse(loginResponse, HttpStatus.OK);
	}
}
