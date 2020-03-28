package com.pratap.photoapp.api.users.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pratap.photoapp.api.users.data.UserEntity;
import com.pratap.photoapp.api.users.data.UserRepository;
import com.pratap.photoapp.api.users.shared.UserDto;
@Service
public class UserServiceImpl implements UsersService {

	UserRepository userRepository;
	
	BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bcryptPasswordEncoder = bcryptPasswordEncoder;
	}
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDto createUser(UserDto userDetails) {

		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bcryptPasswordEncoder.encode(userDetails.getPassword()));
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		UserEntity savedUserDetails = userRepository.save(userEntity);
		return modelMapper.map(savedUserDetails, UserDto.class);
	}

}
