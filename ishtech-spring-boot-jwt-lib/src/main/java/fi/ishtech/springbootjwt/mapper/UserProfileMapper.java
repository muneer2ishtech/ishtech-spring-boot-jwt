package fi.ishtech.springbootjwt.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fi.ishtech.springbootjwt.dto.SignupDto;
import fi.ishtech.springbootjwt.dto.UserProfileDto;
import fi.ishtech.springbootjwt.entity.UserProfile;

/**
 *
 * @author Muneer Ahmed Syed
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper {

	/**
	 *
	 * @param entity {@link UserProfile}
	 * @return {@link UserProfileDto}
	 */
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "id", target = "id")
	@Mapping(source = "firstName", target = "firstName")
	@Mapping(source = "middleName", target = "middleName")
	@Mapping(source = "lastName", target = "lastName")
	@Mapping(source = "defaultLang", target = "defaultLang")
	@Mapping(source = "email", target = "email")
	UserProfileDto toBriefDto(UserProfile entity);

	/**
	 *
	 * @param signupDto {@link SignupDto}
	 * @return new {@link UserProfile} entity
	 */
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "firstName", target = "firstName")
	@Mapping(source = "lastName", target = "lastName")
	@Mapping(source = "lang", target = "defaultLang", defaultValue = "en")
	UserProfile toNewEntity(SignupDto signupDto);

}