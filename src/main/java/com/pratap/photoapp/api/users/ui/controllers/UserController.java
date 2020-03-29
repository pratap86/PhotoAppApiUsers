package com.pratap.photoapp.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pratap.photoapp.api.users.service.UsersService;
import com.pratap.photoapp.api.users.shared.UserDto;
import com.pratap.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.pratap.photoapp.api.users.ui.model.CreateUserResponseModel;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UsersService usersService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping("/status/check")
	public String getStatus() {
		return "working";
	}

	@PostMapping(
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
			)
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto serviceUserDetails = usersService.createUser(userDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(serviceUserDetails, CreateUserResponseModel.class));
	}
}
