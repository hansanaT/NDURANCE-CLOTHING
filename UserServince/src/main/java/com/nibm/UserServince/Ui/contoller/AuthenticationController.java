package com.nibm.UserServince.ui.contoller;

import com.nibm.UserServince.ui.model.request.LoginRequestModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
 
	@PostMapping("/users/login")
	public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel)
	{
		throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");
	}
}
