package com.ndurance.user_servince.service;

import com.ndurance.user_servince.entity.UserEntity;
import com.ndurance.user_servince.shared.dto.UserDto;
import com.ndurance.user_servince.shared.model.request.UserPasswordReset;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface UserService extends UserDetailsService{
	UserDto createUser(UserDto user);
	UserDto updateUserProfile(MultipartFile file, String userid) throws IOException;
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto updateUser(String userId, UserDto user);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);
	boolean verifyEmailToken(String token);
	boolean requestPasswordReset(String email);
	boolean resetPassword(String token, String password);
	Resource loadImageAsResource(String imageName) throws MalformedURLException;
	UserEntity getUserByE(String email);
	Resource getImage(String userId) throws MalformedURLException;
	void resetPassWord(UserPasswordReset userPasswordReset);
}
