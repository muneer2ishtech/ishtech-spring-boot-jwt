package fi.ishtech.springboot.auth.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

	@Value("${fi.istech.springboot.auth.jwt.secret}")
	private String jwtSecret;

	@Value("${fi.istech.springboot.auth.jwt.expirition-ms}")
	private Integer jwtExpirationMs;

	@Value("${fi.istech.springboot.auth.jwt.issuer:auth.springboot.ishtech.fi}")
	private String issuer;

	public String generateToken(UserDetails userDetails) {
		Date iat = new Date();
		Date exp = new Date(iat.getTime() + jwtExpirationMs);
		return generateJwtToken(userDetails, iat, exp);
	}

	private String generateJwtToken(UserDetails userDetails, Date iat, Date exp) {
		// @formatter:off
		return Jwts.builder()
				.subject((userDetails.getUsername()))
				.issuedAt(iat)
				.expiration(exp)
				.issuer(issuer)
				.signWith(jwtKey())
				.compact();
		// @formatter:on
	}

	public boolean validateJwtToken(String token) {
		try {
			parseSignedClaims(token);
			return true;
		} catch (IllegalArgumentException | JwtException e) {
			log.error("Error in parsing JWT", e);
		} catch (Exception e) {
			log.error("Error in parsing JWT", e);
		}
		return false;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
		return claimsResolvers.apply(extractClaims(token));
	}

	private Claims extractClaims(String token) {
		return parseSignedClaims(token).getPayload();
	}

	private Jws<Claims> parseSignedClaims(String token) {
		try {
			// @formatter:off
			return Jwts
					.parser()
					.verifyWith((SecretKey) jwtKey())
					.build()
					.parseSignedClaims(token);
			// @formatter:on
		} catch (IllegalArgumentException | JwtException e) {
			log.error("Error in parsing JWT", e);
			throw e;
		} catch (Exception e) {
			log.error("Error in parsing JWT", e);
			throw new JwtException("Error in parsing JWT", e);
		}
	}

	private Key jwtKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

}
