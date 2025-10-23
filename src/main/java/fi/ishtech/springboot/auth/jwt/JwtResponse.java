package fi.ishtech.springboot.auth.jwt;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class JwtResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = -6607232579525545805L;

	@JsonProperty("token_type")
	@Builder.Default
	private String tokenType = "Bearer";

	@JsonProperty("access_token")
	private String accessToken;

	public static JwtResponse of(String accessToken) {
		// @formatter:off
		return JwtResponse.builder()
				.accessToken(accessToken)
				.build();
		// @formatter:on
	}

}