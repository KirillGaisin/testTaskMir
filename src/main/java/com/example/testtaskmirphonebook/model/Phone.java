package com.example.testtaskmirphonebook.model;

import com.example.testtaskmirphonebook.entity.PhoneEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class Phone {
    private UUID id;
    private String phoneNumber;

    public static Phone toModel(PhoneEntity entity) {
        Phone model = new Phone();
        model.setId(entity.getId());
        model.setPhoneNumber(entity.getPhone());
        return model;
    }
}
