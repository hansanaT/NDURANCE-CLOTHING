package com.nibm.UserServince.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nibm.UserServince.SpringApplicationContext;
import com.nibm.UserServince.new_.JWT.security.token.creator.TokenCreator;
import com.nibm.UserServince.service.impl.UserServiceImpl;
import com.nibm.UserServince.shared.dto.UserDto;
import com.nibm.UserServince.ui.model.request.UserLoginRequestModel;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
            HttpServletResponse res) throws AuthenticationException {
        try {

            UserLoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserLoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
 
//		byte[] secretKeyBytes = SecurityConstants.getTokenSecret().getBytes();
//		SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
//        Instant now = Instant.now();
//
//        String userName = ((UserPrincipal) auth.getPrincipal()).getUsername();
//        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
//        UserDto userDto = userService.getUser(userName);
//
//        String token = Jwts.builder()
//                .subject(userName)
//                .expiration(Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
//                .issuedAt(Date.from(now))
//                .signWith(secretKey)
//                .compact();
//
//        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
//        res.addHeader("UserID", userDto.getUserId());

        String userName = ((UserPrincipal) auth.getPrincipal()).getUsername();
        TokenCreator tokenCreator = (TokenCreator) SpringApplicationContext.getBean("tokenCreator");

        SignedJWT signedJWT = null;
        try {
            signedJWT = tokenCreator.createSignedJWT(auth);
        } catch (NoSuchAlgorithmException | JOSEException e) {
            throw new RuntimeException(e);
        }

        String encryptToken = null;
        try {
            encryptToken = tokenCreator.encryptToken(signedJWT);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        UserServiceImpl userService = (UserServiceImpl) SpringApplicationContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUser(userName);

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + encryptToken);
        res.addHeader("UserID", userDto.getUserId());

    }

}
