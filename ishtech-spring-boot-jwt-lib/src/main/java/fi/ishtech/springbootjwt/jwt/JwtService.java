package fi.ishtech.springbootjwt.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import fi.ishtech.springbootjwt.userdetails.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

	private static final String STR_AUTHERIZATION = "Authorization";
	private static final String STR_BEARER = "Bearer ";

	@Value("${fi.ishtech.springbootjwt.jwt.secret}")
	private String jwtSecret;

	@Value("${fi.ishtech.springbootjwt.jwt.expirition-ms}")
	private Integer jwtExpirationMs;

	@Value("${fi.ishtech.springbootjwt.jwt.issuer}")
	private String issuer;

	@Value("${fi.ishtech.springbootjwt.login-by-email:true}")
	private boolean loginByEmail;

	public JwtResponse generateJwtResponse(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = generateJwtToken(userDetails);

		return JwtResponse.of(jwt);
	}

	public String generateJwtToken(UserDetails userDetails) {
		Date iat = new Date();
		Date exp = new Date(iat.getTime() + jwtExpirationMs);
		return generateJwtToken((UserDetailsImpl) userDetails, iat, exp);
	}

	private String generateJwtToken(UserDetailsImpl userDetails, Date iat, Date exp) {
		// @formatter:off
		return Jwts.builder()
				.subject((loginByEmail ? userDetails.getEmail() : userDetails.getUsername()))
				.issuedAt(iat)
				.expiration(exp)
				.issuer(issuer)
				.claim("userId", userDetails.getId())
				.claim("email", userDetails.getEmail())
				.claim("roles", userDetails.getRoleNames())
				.claim("fullName", userDetails.getFullName())
				.claim("lang", userDetails.getLang())
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

	/**
	 * Extracts the userId from JWT token.<br>
	 * Returns null if the claim is missing or blank.<br>
	 * Throws NumberFormatException if the value is not a valid number.
	 */
	public Long extractUserId(String token) {
		Object userIdObj = extractClaims(token).get("userId");
		if (userIdObj == null) {
			return null;
		}

		if (userIdObj instanceof Number) {
			return ((Number) userIdObj).longValue();
		}

		if (userIdObj instanceof String) {
			return NumberUtils.parseNumber((String) userIdObj, Long.class);
		}

		throw new NumberFormatException("Invalid userId: " + userIdObj);

	}

	public Long extractUserIdFromRequest(final HttpServletRequest request) {
		return extractUserId(extractJwtFromRequest(request));
	}

	public String extractJwtFromRequest(final HttpServletRequest request) {
		return extractJwtFromRequestHeader(request.getHeader(STR_AUTHERIZATION));
	}

	private String extractJwtFromRequestHeader(final String authHeader) {
		if (authHeader != null && authHeader.startsWith(STR_BEARER)) {
			return authHeader.substring(7);
		}

		return null;
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