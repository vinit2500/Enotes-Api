package com.enotes.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.EmailRequest;
import com.enotes.dto.UserDto;
import com.enotes.entity.AccountStatus;
import com.enotes.entity.Role;
import com.enotes.entity.User;
import com.enotes.repository.RoleRepository;
import com.enotes.repository.UserRepository;
import com.enotes.service.EmailService;
import com.enotes.service.UserService;
import com.enotes.util.Validation;

import jakarta.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Validation validation;

	@Autowired
	private EmailService emailService;

	@Override
	public Boolean register(UserDto userDto, String url) throws UnsupportedEncodingException, MessagingException {
		validation.userValidation(userDto);
		User user = modelMapper.map(userDto, User.class);
		setRole(userDto, user);

		AccountStatus accountStatus = new AccountStatus();
		accountStatus.setIsActive(false);
		accountStatus.setVerificationCode(UUID.randomUUID().toString());

		user.setStatus(accountStatus);
		User saveduser = userRepository.save(user);

		if (!ObjectUtils.isEmpty(saveduser)) {
			emailSend(saveduser, url);
			return true;
		}
		return false;
	}

	private void emailSend(User saveduser, String url) throws UnsupportedEncodingException, MessagingException {
		String message = "Hi <b>[[username]]</b> " + "<br><p>Your account register successfully.</p>"
				+ "<p>Click the link below to verify and active your password</p>"
				+ "<a href='[[url]]' > Click Here </a><br/>" + "Thanks,<br>Enotes.com";

		message = message.replace("[[username]]", saveduser.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + saveduser.getId() + "&&code="
				+ saveduser.getStatus().getVerificationCode());

		EmailRequest emailRequest = new EmailRequest();
		emailRequest.setTo(saveduser.getEmail());
		emailRequest.setTitle("Account Created Successfully");
		emailRequest.setSubject("Account Create");
		emailRequest.setMessage(message);
		emailService.send(emailRequest);
	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleId);
		user.setRoles(roles);
	}

}
