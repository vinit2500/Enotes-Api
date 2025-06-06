package com.enotes.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.UserDto;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtils;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) throws UnsupportedEncodingException, MessagingException {
		Boolean register = userService.register(userDto);
		if (register) {
			return CommonUtils.createBuildResponseMessage("Register successfull", HttpStatus.CREATED);
		}
		return CommonUtils.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
