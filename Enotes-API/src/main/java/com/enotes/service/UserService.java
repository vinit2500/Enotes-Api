package com.enotes.service;

import com.enotes.dto.PasswordChngeRequest;
import com.enotes.dto.PswdResetRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

	public void changePassword(PasswordChngeRequest passwordChngeRequest);

	public void sendEmailPasswordReset(String email,HttpServletRequest request) throws Exception;

	public void verifyPasswordResetLink(Integer uid, String code) throws Exception;

	public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception;
}
