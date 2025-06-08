package com.enotes.service;

import java.io.UnsupportedEncodingException;

import com.enotes.dto.LoginRequest;
import com.enotes.dto.LoginResponse;
import com.enotes.dto.UserDto;

import jakarta.mail.MessagingException;

public interface AuthService {

	public Boolean register(UserDto userDto, String url) throws UnsupportedEncodingException, MessagingException;

	public LoginResponse login(LoginRequest loginRequest);
}
