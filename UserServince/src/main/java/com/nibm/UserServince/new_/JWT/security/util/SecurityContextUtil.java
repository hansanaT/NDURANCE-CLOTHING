package com.nibm.UserServince.new_.JWT.security.util;

import com.nibm.UserServince.io.entity.UserEntity;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Slf4j
public class SecurityContextUtil {
    private SecurityContextUtil() {

    }

    public static void setSecurityContext(SignedJWT signedJWT) {
        try {
            if(signedJWT == null) return;

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if(claims == null) return;

            String username = claims.getSubject();
            if (username == null)
                return;
            List<String> authorities = claims.getStringListClaim("authorities");
            UserEntity applicationUser = new UserEntity();
            applicationUser.setFirstName(username);
            applicationUser.setId(claims.getLongClaim("publicId"));
            applicationUser.setAddresses(Arrays.asList());


            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(applicationUser, null, createAuthorities(authorities));
            auth.setDetails(signedJWT.serialize());

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            log.error("Error setting security context ", e);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
