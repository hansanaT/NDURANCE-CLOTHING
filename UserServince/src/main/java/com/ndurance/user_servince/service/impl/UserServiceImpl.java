package com.ndurance.user_servince.service.impl;

import com.ndurance.user_servince.exceptions.UserServiceException;
import com.ndurance.user_servince.repository.AuthorityRepository;
import com.ndurance.user_servince.repository.PasswordResetTokenRepository;
import com.ndurance.user_servince.repository.RoleRepository;
import com.ndurance.user_servince.repository.UserRepository;
import com.ndurance.user_servince.security.UserPrincipal;
import com.ndurance.user_servince.shared.Utils;
import com.ndurance.user_servince.entity.PasswordResetTokenEntity;
import com.ndurance.user_servince.entity.RoleEntity;
import com.ndurance.user_servince.entity.UserEntity;
import com.ndurance.user_servince.service.UserService;
import com.ndurance.user_servince.shared.AmazonSES;
import com.ndurance.user_servince.shared.dto.AddressDTO;
import com.ndurance.user_servince.shared.dto.UserDto;
import com.ndurance.user_servince.shared.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    UserRepository userRepository;

	@Autowired
    Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	AmazonSES amazonSES;
	
	@Autowired
    RoleRepository roleRepository;

	@Autowired
    AuthorityRepository authorityRepo;
	private final ModelMapper modelMapper = new ModelMapper();

	private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";

	@Override
	public UserDto createUser(UserDto user) {
		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new UserServiceException("Record already exists");

		for(int i=0;i<user.getAddresses().size();i++)
		{
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}

		//BeanUtils.copyProperties(user, userEntity);
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));

		Collection<RoleEntity> roleEntities = new HashSet<>();
		for(String role: user.getRoles()) {
			RoleEntity roleEntity = roleRepository.findByName(role);
			if(roleEntity !=null) {
				roleEntities.add(roleEntity);
			}
		}

		userEntity.setRoles(roleEntities);
		UserEntity storedUserDetails = userRepository.save(userEntity);
		UserDto returnValue  = modelMapper.map(storedUserDetails, UserDto.class);

		// Send an email message to user to verify their email address
		//amazonSES.verifyEmail(returnValue);

		return returnValue;
	}

	@Override
	public UserDto createUser(UserDto user, MultipartFile file) throws IOException {

		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new UserServiceException("Record already exists");

		for(int i=0;i<user.getAddresses().size();i++)
		{
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, address);
		}

		//BeanUtils.copyProperties(user, userEntity);
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));

		Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
		Files.createDirectories(uploadPath);

		String fileName = utils.generateUserId(20) + file.getOriginalFilename();
		userEntity.setProfilePic(fileName);

		Path filePath = uploadPath.resolve(Objects.requireNonNull(fileName));
		file.transferTo(filePath.toFile());
		
		// Set roles 
		Collection<RoleEntity> roleEntities = new HashSet<>();
		for(String role: user.getRoles()) {
			RoleEntity roleEntity = roleRepository.findByName(role);
			if(roleEntity !=null) {
				roleEntities.add(roleEntity);
			}
		}
		
		userEntity.setRoles(roleEntities);

		UserEntity storedUserDetails = userRepository.save(userEntity);
		UserDto returnValue  = modelMapper.map(storedUserDetails, UserDto.class);
		
        // Send an email message to user to verify their email address
		//amazonSES.verifyEmail(returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		return new UserPrincipal(userEntity);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException("User with ID: " + userId + " not found");

		return modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserDto returnValue = new UserDto();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		UserEntity updatedUserDetails = userRepository.save(userEntity);
		returnValue = new ModelMapper().map(updatedUserDetails, UserDto.class);

		return returnValue;
	}

	@Transactional
	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userRepository.delete(userEntity);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		
		if(page>0) page = page-1;
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		
        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }
		
		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
	    boolean returnValue = false;

        // Find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = Utils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		
        boolean returnValue = false;
        
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            return returnValue;
        }
        
        String token = new Utils().generatePasswordResetToken(userEntity.getUserId());
        
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
        
        returnValue = new AmazonSES().sendPasswordResetRequest(
                userEntity.getFirstName(), 
                userEntity.getEmail(),
                token);
        
		return returnValue;
	}

	@Override
	public boolean resetPassword(String token, String password) {
        boolean returnValue = false;
        
        if( Utils.hasTokenExpired(token) )
        {
            return returnValue;
        }
 
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        
        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);
 
        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }
   
        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        
        return returnValue;
	}

	@Override
	public UserEntity getUserByE(String email) {
		return userRepository.findByEmail(email);
	}

}
