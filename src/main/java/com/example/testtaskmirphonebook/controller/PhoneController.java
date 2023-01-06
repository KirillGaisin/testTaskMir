package com.example.testtaskmirphonebook.controller;

import com.example.testtaskmirphonebook.entity.PhoneEntity;
import com.example.testtaskmirphonebook.exception.PhoneNumberNotFoundException;
import com.example.testtaskmirphonebook.exception.PhoneNumberNotUniqueException;
import com.example.testtaskmirphonebook.exception.UserNotFoundException;
import com.example.testtaskmirphonebook.service.PhoneService;
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

import java.util.UUID;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;

@RestController
@RequestMapping("/phones")
@Slf4j
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    /**
     * POST метод, добавляющий телефон к существующему пользователю
     * @param phone - телефон, который следует добавить
     * @param userId - id пользователя, к которому добавляется телефон
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Добавить телефон к существующему пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Номер телефона уже есть у данного пользователя"),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @PostMapping
    public ResponseEntity<Object> addPhone(@RequestBody @Parameter(
            name = "phone", in = DEFAULT, description = "Номер телефона") PhoneEntity phone, @RequestParam @Parameter(
            name = "userId", in = QUERY, description = "id пользователя") UUID userId) {
        try {
            log.info(String.format("addPhone %s userId %s", phone, userId));
            return ResponseEntity.ok(phoneService.addPhone(phone, userId));
        } catch (PhoneNumberNotUniqueException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE метод, удаляющий телефон у данного пользователя
     * @param phone - телефон, который следует удалить
     * @param userName - пользователь, у которого следует удалить телефон
     * @return spring web ResponseEntity
     */
    @Operation(summary = "Удалить данный номер телефона у данного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "404", description = "Номер телефона не найден"),
            @ApiResponse(responseCode = "400", description = "Неизвестная ошибка")
    })
    @DeleteMapping("/{userName}")
    public ResponseEntity<Object> deletePhone(@RequestParam @Parameter(
            name = "phone", in = QUERY, description = "Номер телефона") PhoneEntity phone, @Parameter(
            name = "userName", in = PATH, description = "Имя пользователя") @PathVariable String userName) {
        try {
            log.info(String.format("phone %s deleted for user %s", phone, userName));
            return ResponseEntity.ok(phoneService.deletePhone(userName, phone));
        } catch (UserNotFoundException | PhoneNumberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
