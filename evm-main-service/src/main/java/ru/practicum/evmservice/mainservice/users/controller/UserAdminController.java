package ru.practicum.evmservice.mainservice.users.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.evmservice.mainservice.users.dto.NewUserRequestDto;
import ru.practicum.evmservice.mainservice.users.dto.UserDto;
import ru.practicum.evmservice.mainservice.users.service.UserService;

import java.util.List;

/**
 * API для работы с пользователями
 */
@Slf4j
@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserAdminController {
    private final UserService userService;

    /**
     * Получение информации о пользователях
     * <p>
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size   количество элементов в наборе
     *             Default value : 10
     * @return список пользователей в формате UserDto
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "") Integer[] ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("getUsers called with {} ids (from {} to {})", ids.length, from, size);
        return userService.getUsers(ids, from, size);
    }

    /**
     * Добавление нового пользователя
     *
     * @param newUserRequestDto Данные добавляемого пользователя
     * @return данные пользователя в формате UserDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequestDto newUserRequestDto) {
        log.info("createUser called with {}", newUserRequestDto);
        return userService.createUser(newUserRequestDto);
    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("deleteUser called with {}", userId);
        userService.deleteUser(userId);
    }

}
