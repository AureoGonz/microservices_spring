package com.triceracode.auth.endpoint;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triceracode.core.entity.User;

@RestController
@RequestMapping("user")
public class UserInfoController {

	@GetMapping(path = "info")
	public ResponseEntity<User> getUserInfo(Principal principal){
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
