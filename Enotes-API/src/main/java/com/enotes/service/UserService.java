package com.enotes.service;

import java.io.UnsupportedEncodingException;

import com.enotes.dto.UserDto;

import jakarta.mail.MessagingException;

public interface UserService {

	public Boolean register(UserDto userDto) throws UnsupportedEncodingException, MessagingException;
}
