package com.ndurance.user_servince.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndurance.user_servince.SpringApplicationContext;
import com.ndurance.user_servince.entity.UserEntity;
import com.ndurance.user_servince.security.jwt.token.creator.TokenCreator;
import com.ndurance.user_servince.service.impl.UserServiceImpl;
import com.ndurance.user_servince.shared.dto.UserDto;
import com.ndurance.user_servince.shared.model.request.UserLoginRequestModel;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
        UserEntity userEntity = userService.getUserByE(userName);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", encryptToken)
                .httpOnly(false) //if set to true, js can't access
                .secure(true)     // Cookie is sent only over HTTPS
                .path("/")        // Cookie is valid for the entire domain
                .maxAge(7 * 24 * 60 * 60)  // Cookie expires in 7 days
                .sameSite("None") // Sends the cookie on cross-site requests
                .build();

        ResponseCookie userIdCookie = ResponseCookie.from("userId", userEntity.getUserId())
                .httpOnly(false) //if set to true, js can't access
                .secure(true)     // Cookie is sent only over HTTPS
                .path("/")        // Cookie is valid for the entire domain
                .maxAge(7 * 24 * 60 * 60)  // Cookie expires in 7 days
                .sameSite("None") // Sends the cookie on cross-site requests
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        res.addHeader(HttpHeaders.SET_COOKIE, userIdCookie.toString());

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + encryptToken);
        res.addHeader("UserID", userEntity.getUserId());

    }

}
