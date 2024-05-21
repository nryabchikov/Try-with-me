package by.ryabchikov.coursework.mapper;

import by.ryabchikov.coursework.dto.user.AnotherUserProfile;
import by.ryabchikov.coursework.dto.user.LoginUserDTO;
import by.ryabchikov.coursework.dto.user.RegistrationUserDTO;
import by.ryabchikov.coursework.dto.user.UserProfileDTO;
import by.ryabchikov.coursework.model.user.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegistrationUserDTO registrationUserDTO);
    User toUser(LoginUserDTO loginUserDTO);
    User toUser(UserProfileDTO userProfileDTO);

    AnotherUserProfile toAnotherUserProfile(User user);
}
