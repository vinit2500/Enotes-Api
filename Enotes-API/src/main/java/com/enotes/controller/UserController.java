package com.enotes.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.PasswordChngeRequest;
import com.enotes.dto.UserResponse;
import com.enotes.entity.User;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile() {
		User loggedInUser = CommonUtils.getLoggedInUser();
		UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
		return CommonUtils.createBuildResponse(userResponse, HttpStatus.OK);
	}
	
	@PostMapping("/chge-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngeRequest passwordRequest) {
		userService.changePassword(passwordRequest);
		return CommonUtils.createBuildResponseMessage("Password change success", HttpStatus.OK);
	}
}
