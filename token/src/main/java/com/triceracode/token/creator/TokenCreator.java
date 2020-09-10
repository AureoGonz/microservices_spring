package com.triceracode.token.creator;

import static com.triceracode.token.utils.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.triceracode.token.utils.Constants.ISSUER_TOKEN;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.triceracode.core.entity.User;
import com.triceracode.core.property.JwtConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenCreator {
	
	private final JwtConfiguration jwtConfiguration;
	
	@SneakyThrows
	public SignedJWT createSignedJWT(Authentication auth) {
		User user = (User) auth.getPrincipal();

		JWTClaimsSet jwtClaimsSet = createJWTClaimSet(auth, user);

		KeyPair rsaKeys = generateKeyPair();

		JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();

		SignedJWT signedJWT = new SignedJWT(
				new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(jwk).type(JOSEObjectType.JWT).build(), jwtClaimsSet);

		RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());

		signedJWT.sign(signer);

		return signedJWT;
	}
	
	private JWTClaimsSet createJWTClaimSet(Authentication auth, User user) {
		return new JWTClaimsSet.Builder().subject(user.getUsername())
				.claim("authorities",
						auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.claim("userId", user.getId())
				.issuer(ISSUER_TOKEN)
				.expirationTime(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000)).build();
	}
	
	@SneakyThrows
	private KeyPair generateKeyPair() {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		return generator.genKeyPair();
	}

	public String encryptedToken(SignedJWT signedJWT) throws JOSEException {
		DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
		JWEObject jweObject = new JWEObject(
				new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256).contentType("JWT").build(),
				new Payload(signedJWT));
		jweObject.encrypt(directEncrypter);

		return jweObject.serialize();
	}
}
