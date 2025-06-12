package ru.practicum.evmservice.mainservice.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evmservice.mainservice.exceptions.NotFoundException;
import ru.practicum.evmservice.mainservice.users.dto.NewUserRequestDto;
import ru.practicum.evmservice.mainservice.users.dto.UserDto;
import ru.practicum.evmservice.mainservice.users.mapper.UserMapper;
import ru.practicum.evmservice.mainservice.users.model.User;
import ru.practicum.evmservice.mainservice.users.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(Integer[] ids, int from, int size) {
        List<User> users;

        if (ids == null || ids.length == 0) {
            users = userRepository.findAllWithOffsetAndLimit(from, size);
        } else {
            users = userRepository.findAllByIdsInWithOffsetAndLimit(ids, from, size);
        }

        if (users.isEmpty()) {
            return List.of();
        }

        return UserMapper.INSTANCE.toUserDtoList(users);
    }

    @Override
    public UserDto createUser(NewUserRequestDto newUserRequestDto) {
        User user = userRepository.save(UserMapper.INSTANCE.toUser(newUserRequestDto));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
    }
}
