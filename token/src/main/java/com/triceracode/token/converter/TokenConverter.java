package com.triceracode.token.converter;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.property.JwtConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {
	
	private final JwtConfiguration jwtConfiguration;
	
	@SneakyThrows
	public String decryptToken(String encryptedToken) {
		JWEObject jweObject = JWEObject.parse(encryptedToken);
		DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
		jweObject.decrypt(directDecrypter);
		return jweObject.getPayload().toSignedJWT().serialize();
	}
	
	@SneakyThrows
	public void validateTokenSignature(String signedToken) {
		SignedJWT signedJWT = SignedJWT.parse(signedToken);
		RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
		if(!signedJWT.verify(new RSASSAVerifier(publicKey)))
			throw new AccessDeniedException("firma de token no aceptada");
	}

}
