package com.enotes.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.enotes.dto.EmailRequest;
import com.enotes.dto.PasswordChngeRequest;
import com.enotes.dto.PswdResetRequest;
import com.enotes.entity.User;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.UserRepository;
import com.enotes.service.EmailService;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public void changePassword(PasswordChngeRequest passwordChngeRequest) {
		User loggedInUser = CommonUtils.getLoggedInUser();

		if (!passwordEncoder.matches(passwordChngeRequest.getOldPassword(), loggedInUser.getPassword())) {
			throw new IllegalArgumentException("Old Password is incorrect !!");
		}
		String encodePassword = passwordEncoder.encode(passwordChngeRequest.getNewPassword());
		loggedInUser.setPassword(encodePassword);
		userRepository.save(loggedInUser);
	}

	@Override
	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {

		User user = userRepository.findByEmail(email);

		if (ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFoundException("Invalid email");
		}

		// Generate unique password reset token
		String passwordResetToken = UUID.randomUUID().toString();
		user.getStatus().setPasswordResetToken(passwordResetToken);
		User updateUser = userRepository.save(user);

		String url = CommonUtils.getUrl(request);
		sendEmailRequest(updateUser, url);
	}

	private void sendEmailRequest(User user, String url) throws Exception {

		String message = "Hi <b>[[username]]</b> " + "<br><p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>"
				+ "<p><a href=[[url]]>Change my password</a></p>"
				+ "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p><br>" + "Thanks,<br>Enotes.com";

		message = message.replace("[[username]]", user.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-link?uid=" + user.getId() + "&&code="
				+ user.getStatus().getPasswordResetToken());

		// EmailRequest emailRequest =
		// EmailRequest.builder().to(user.getEmail()).title("Password Reset")
		// .subject("Password Reset link").message(message).build();
		//
		EmailRequest er = new EmailRequest();
		er.setMessage(message);
		er.setTo(user.getEmail());
		er.setTitle("password reset");
		er.setSubject("password reset link");

		// send password reset email to user
		emailService.send(er);
	}

	@Override
	public void verifyPasswordResetLink(Integer uid, String code) throws Exception {
		User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("invalid user"));
		verifyPasswordResetCode(user.getStatus().getPasswordResetToken(), code);
	}

	@Override
	public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception {
		User user = userRepository.findById(pswdResetRequest.getUid())
				.orElseThrow(() -> new ResourceNotFoundException("invalid user"));

		String encodePassword = passwordEncoder.encode(pswdResetRequest.getNewPassword());
		user.setPassword(encodePassword);
		user.getStatus().setPasswordResetToken(null);
		userRepository.save(user);

	}

	private void verifyPasswordResetCode(String existToken, String reqToken) {

		// request token not null
		if (StringUtils.hasText(reqToken)) {

			// password already reset
			if (StringUtils.hasText(existToken)) {
				throw new IllegalArgumentException("Already password reset");
			}

			// user request token changes means token missmatch
			if (!existToken.equals(reqToken)) {
				throw new IllegalArgumentException("Invalid url");
			}
		} else {
			throw new IllegalArgumentException("Invalid token or code");
		}
	}

}
