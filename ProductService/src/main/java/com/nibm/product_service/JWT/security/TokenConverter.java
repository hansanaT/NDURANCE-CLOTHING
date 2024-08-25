package com.nibm.product_service.JWT.security;

import com.nibm.product_service.feign_client.UserClient;
import com.nibm.product_service.model.UserRest;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {

    private final JwtConfiguration jwtConfiguration;
    private final UserClient userClient;


    public String decryptToken(String encryptedToken) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(encryptedToken);
        DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
        jweObject.decrypt(directDecrypter);
        return jweObject.getPayload().toSignedJWT().serialize();
    }


    public UserRest validateTokenSignature(String signedToken, HttpServletRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(signedToken);

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        String subject = claimsSet.getSubject();
        List<String> _authorities = claimsSet.getStringListClaim("authorities"); // Custom claim
        List<GrantedAuthority> authorities = new ArrayList<>();

        String issuer = claimsSet.getIssuer();
        Date expiration = claimsSet.getExpirationTime();

        Date todayDate = new Date();
        if (expiration.before(todayDate)) {
            return null;
        }

        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
        if (!signedJWT.verify(new RSASSAVerifier(publicKey))) {
            return null;
        }

        String authorizationHeader = request.getHeader("Authorization");
        UserRest user = userClient.getCustomerById(subject, authorizationHeader);

        if (user == null) {
            return null;
        }

        _authorities.forEach(auth->{
            authorities.add(new SimpleGrantedAuthority(auth));
        });
        user.setAuthorities(authorities);
        return user;
    }


}
