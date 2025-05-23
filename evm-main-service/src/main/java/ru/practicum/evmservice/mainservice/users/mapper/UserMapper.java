package ru.practicum.evmservice.mainservice.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.evmservice.mainservice.users.dto.NewUserRequestDto;
import ru.practicum.evmservice.mainservice.users.dto.UserDto;
import ru.practicum.evmservice.mainservice.users.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    User toUser(NewUserRequestDto newUserRequestDto);

    List<UserDto> toUserDtoList(List<User> userList);
}
