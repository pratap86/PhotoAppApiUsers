package com.pratap.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pratap.photoapp.api.users.data.AlbumsServiceClient;
import com.pratap.photoapp.api.users.data.UserEntity;
import com.pratap.photoapp.api.users.data.UserRepository;
import com.pratap.photoapp.api.users.shared.UserDto;
import com.pratap.photoapp.api.users.ui.model.AlbumResponseModel;

@Service
public class UsersServiceImpl implements UsersService {

	private static Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
	
	UserRepository userRepository;

	BCryptPasswordEncoder bcryptPasswordEncoder;

//	RestTemplate restTemplate;
	
	AlbumsServiceClient albumsServiceClient;
	
	Environment env;

	@Autowired
	public UsersServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder,
			AlbumsServiceClient albumsServiceClient, Environment env) {
		this.userRepository = userRepository;
		this.bcryptPasswordEncoder = bcryptPasswordEncoder;
		this.albumsServiceClient = albumsServiceClient;
		this.env = env;
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
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		return modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException("User Not Found By Id : " + userId);
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);

		/*
		String albumsUrl = String.format(env.getProperty("albums.url"), userId);
		
		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
        */
		
		/*
		List<AlbumResponseModel> albumsList = null;
		try {
			albumsList = albumsServiceClient.getAlbums(userId);
		} catch (FeignException e) {
			logger.error(e.getLocalizedMessage());
		}
		*/
		logger.info("Before calling albums microservice");
		List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
		logger.info("After calling albums microservice");
        userDto.setAlbums(albumsList);
		return userDto;
	}

}
