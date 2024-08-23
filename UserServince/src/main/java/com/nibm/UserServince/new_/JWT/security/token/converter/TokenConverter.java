package com.nibm.UserServince.new_.JWT.security.token.converter;

import com.nibm.UserServince.io.entity.UserEntity;
import com.nibm.UserServince.io.repository.UserRepository;
import com.nibm.UserServince.new_.JWT.security.property.JwtConfiguration;
import com.nibm.UserServince.service.impl.UserServiceImpl;
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
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {

    private final JwtConfiguration jwtConfiguration;
    private final UserRepository userRepo;
    private final UserServiceImpl userServiceImpl;

    public String decryptToken(String encryptedToken) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(encryptedToken);
        DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
        jweObject.decrypt(directDecrypter);
        return jweObject.getPayload().toSignedJWT().serialize();
    }

    public String passwordRestTokenValidating(String token) {
        try {
            String decryptUserToken = decryptToken(token);
            return validateTokenSignature(decryptUserToken, token);
        } catch (ParseException e) {
            return null;
        } catch (JOSEException e) {
            return null;
        }
    }

    public String validateTokenSignature(String signedToken, String token) throws ParseException, JOSEException {

        SignedJWT signedJWT = SignedJWT.parse(signedToken);

        UserEntity user = userRepo.findByEmail(signedJWT.getPayload().toJSONObject().get("sub").toString());

        if (user == null)
            return null;

//        if (user.getPasswordResetToken() == null)
//            return null;
//
//        if (!user.getPasswordResetToken().equals(token))
//            return null;

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        final Date expiration = claimsSet.getExpirationTime();
        Date todayDate = new Date();

        if (expiration.before(todayDate))
            return null;

        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
        if (!signedJWT.verify(new RSASSAVerifier(publicKey)))
            return null;

        String email = user.getEmail();
        if (email == null)
            return null;
        return email;
    }

    public String validateTokenSignature(String signedToken, HttpServletRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(signedToken);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        final Date expiration = claimsSet.getExpirationTime();
        Date todayDate = new Date();

        if (expiration.before(todayDate))
            return null;

        RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());

        if (!signedJWT.verify(new RSASSAVerifier(publicKey)))
            return null;

        UserEntity user = userRepo.findByEmail(signedJWT.getPayload().toJSONObject().get("sub").toString());
        if (user == null)
            return null;
        else
            return user.getEmail();
    }

}
