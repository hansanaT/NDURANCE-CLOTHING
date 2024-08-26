package com.ndurance.product_service.security;

import com.ndurance.product_service.SpringApplicationContext;
import com.ndurance.product_service.JWT.security.TokenConverter;
import com.ndurance.product_service.model.UserRest;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.text.ParseException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = getAuthentication(req);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws ParseException, JOSEException {

        String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);

        if (authorizationHeader == null) {
            return null;
        }

        String signedToken = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");
        TokenConverter tokenConverter = (TokenConverter) SpringApplicationContext.getBean("tokenConverter");
        String token = tokenConverter.decryptToken(signedToken);
        UserRest user = tokenConverter.validateTokenSignature(token, request);

        if(user != null){
            return new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());

        }else {
            return null;
        }

    }

}
