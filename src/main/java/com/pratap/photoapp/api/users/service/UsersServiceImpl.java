package com.pratap.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pratap.photoapp.api.users.data.UserEntity;
import com.pratap.photoapp.api.users.data.UserRepository;
import com.pratap.photoapp.api.users.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

	UserRepository userRepository;

	BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
	public UsersServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		return modelMapper.map(userEntity, UserDto.class);
	}

}
