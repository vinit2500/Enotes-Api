package com.enotes.service;

import com.enotes.exception.ResourceNotFoundException;

public interface HomeService {

	public Boolean verifyAccount(Integer userid, String verificationCode) throws ResourceNotFoundException;
}
