package com.pratap.photoapp.api.users.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * 
 * @author Pratap Narayan
 * A central place to handle Feign Error, like 404(Not Found), 400(Bad Request)
 *
 */
@Component
public class FeignErrorDecoder implements ErrorDecoder {

	@Autowired
	Environment env;
	
	@Override
	public Exception decode(String methodKey, Response response) {

		switch (response.status()) {
		case 400: {
			//Do something
			//return new BadException
			break;
		}
		case 404: {
			if(methodKey.contains("getAlbums")) {
				return new ResponseStatusException(HttpStatus.valueOf(response.status()), env.getProperty("albums.exceptions.albums-not-found"));
			}
			break;
		}
		default:
			return new Exception(response.reason());
		}
		return null;
	}

}
