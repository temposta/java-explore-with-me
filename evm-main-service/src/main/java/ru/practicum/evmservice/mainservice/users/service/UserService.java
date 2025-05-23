package ru.practicum.evmservice.mainservice.users.service;

import ru.practicum.evmservice.mainservice.users.dto.NewUserRequestDto;
import ru.practicum.evmservice.mainservice.users.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(Integer[] ids, int from, int size);

    UserDto createUser(NewUserRequestDto newUserRequestDto);

    void deleteUser(Integer userId);
}
