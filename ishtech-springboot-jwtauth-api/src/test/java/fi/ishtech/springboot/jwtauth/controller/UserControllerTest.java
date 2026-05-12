package fi.ishtech.springboot.jwtauth.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fi.ishtech.springboot.jwtauth.dto.UserProfileDto;
import fi.ishtech.springboot.jwtauth.enums.StandardUserRoleEnum;
import fi.ishtech.springboot.jwtauth.service.AuthInfoService;
import fi.ishtech.springboot.jwtauth.service.UserProfileService;
import fi.ishtech.springboot.jwtauth.userdetails.UserDetailsImpl;

/**
 * Test class for {@link UserController}
 *
 * @author Muneer Ahmed Syed
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserProfileService userProfileService;

	@MockitoBean
	private AuthInfoService authInfoService;

	private UserProfileDto sampleUserProfile;

	private UserDetails sampleUserDetails;

	@BeforeEach
	void setUp() {
		sampleUserProfile = new UserProfileDto();
		sampleUserProfile.setId(100L);
		sampleUserProfile.setFirstName("Testi");
		sampleUserProfile.setLastName("Besti");

		sampleUserDetails = UserDetailsImpl.of(100L, "testi", "test@test.com", "pass", true,
				List.of(StandardUserRoleEnum.asRole(StandardUserRoleEnum.USER)), "Testi Besti", "en");
	}

	@Test
	@Order(1)
	void testFindUserProfileByIdByAdmin() throws Exception {
		when(userProfileService.findOneByIdAndMapToVoOrElseThrow(100L)).thenReturn(sampleUserProfile);

		// @formatter:off
		mockMvc.perform(get("/api/v1/users/100")
				.with(user("admin").authorities(() -> "ROLE_ADMIN")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Testi"));
		// @formatter:on
	}

	@Test
	@Order(2)
	void testFindUserProfileByUserIdBySelf() throws Exception {
		when(authInfoService.getUserId()).thenReturn(100L);
		when(userProfileService.findOneByIdAndMapToVoOrElseThrow(100L)).thenReturn(sampleUserProfile);

		// @formatter:off
		mockMvc.perform(get("/api/v1/users/100")
				.with(user(sampleUserDetails)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Testi"));
		// @formatter:on
	}

	@Test
	@Order(3)
	void testFindUserProfileByMeBySelf() throws Exception {
		when(authInfoService.getUserId()).thenReturn(100L);
		when(userProfileService.findOneByIdAndMapToVoOrElseThrow(100L)).thenReturn(sampleUserProfile);

		// @formatter:off
		mockMvc.perform(get("/api/v1/users/me")
				.with(user(sampleUserDetails)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Testi"));
		// @formatter:on
	}

}