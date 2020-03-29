package com.pratap.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pratap.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService {

	UserDto createUser(UserDto userDetails);
	UserDto getUserDetailsByEmail(String email);
}
