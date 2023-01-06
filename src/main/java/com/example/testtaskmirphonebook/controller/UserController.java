package com.example.testtaskmirphonebook.controller;


import com.example.testtaskmirphonebook.entity.UserEntity;
import com.example.testtaskmirphonebook.exception.IllegalStrategyException;
import com.example.testtaskmirphonebook.exception.UserNotFoundException;
import com.example.testtaskmirphonebook.exception.UserNotUniqueException;
import com.example.testtaskmirphonebook.model.User;
import com.example.testtaskmirphonebook.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET метод для получения всех пользователей в справочнике
     *
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET метод для получения всех пользователей в справочнике, отсортированных по переданной стратегии
     *
     * @param sortBy - стратегия сортировки (по имени или по дате рождения)
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Получить всех пользователей",
            description = "Получить всех пользователей, отсортированных по переданному параметру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @GetMapping("/all/sort_by_{sortBy}")
    public ResponseEntity getAllUsersSorted(@PathVariable @Parameter(
            name = "sortBy", in = PATH, description = "Поле, по которому сортируются пользователи") String sortBy) {
        try {
            return ResponseEntity.ok(userService.getAllUsersSorted(sortBy));
        } catch (IllegalStrategyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST метод, добавляющий нового пользователя
     *
     * @param user - сущность пользователя, которого следует добавить
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Добавить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно добавлен",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Такой пользователь уже существует"),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody @Parameter(
            name = "user", in = DEFAULT, description = "Сущность пользователя") UserEntity user) {
        try {
            userService.addUser(user);
            log.info(String.format("addUser %s id %s date of birth %s", user.getName(), user.getId(), user.getDateOfBirth()));
            return ResponseEntity.ok().body(String.format("Пользователь %s успешно добавлен", user.getName()));
        } catch (UserNotUniqueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET метод, получающий пользователя по имени
     *
     * @param name - имя пользователя
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Получить пользователя по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким именем не найден"),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @GetMapping
    public ResponseEntity<Object> getUserByName(@RequestParam @Parameter(
            name = "name", in = QUERY, description = "Имя пользователя") String name) {
        try {
            return ResponseEntity.ok(userService.getUser(name));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT метод для изменения имени пользователя
     *
     * @param userName имя пользователя
     * @param newName  имя, на которое следует изменить имя пользователя
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Обновить ФИО пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ФИО успешно изменены",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с такими ФИО не найден"),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @PutMapping
    public ResponseEntity<Object> updateUsername(@RequestParam @Parameter(
            name = "userName", in = QUERY, description = "ФИО пользователя") String userName, @RequestBody @Parameter(
            name = "newName", in = DEFAULT, description = "Новые ФИО") String newName) {
        try {
            return ResponseEntity.ok(userService.updateUsername(userName, newName));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserNotUniqueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE метод для удаления пользователя из справочника
     *
     * @param id - id пользователя, которого следует удалить
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Удалить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким id не найден"),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable @Parameter(
            name = "id", in = PATH, description = "id пользователя") UUID id) {
        try {
            log.info(String.format("deleteUser id %s", id));
            return ResponseEntity.ok(String.format("Пользователь %s успешно удален", userService.deleteUser(id).getName()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
