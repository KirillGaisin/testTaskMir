package com.example.testtaskmirphonebook.model;

import com.example.testtaskmirphonebook.entity.UserEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class User {
    private String name;
    private String dateOfBirth;
    private List<Phone> phones;

    public static User toModel(UserEntity entity) {
        User model = new User();
        model.setName(entity.getName());
        model.setDateOfBirth(entity.getDateOfBirth());
        model.setPhones(entity.getPhones().stream().map(Phone::toModel).collect(Collectors.toList()));
        return model;
    }
}
