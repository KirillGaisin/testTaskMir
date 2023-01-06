package com.example.testtaskmirphonebook.service;

import com.example.testtaskmirphonebook.entity.PhoneEntity;
import com.example.testtaskmirphonebook.entity.UserEntity;
import com.example.testtaskmirphonebook.exception.PhoneNumberNotFoundException;
import com.example.testtaskmirphonebook.exception.PhoneNumberNotUniqueException;
import com.example.testtaskmirphonebook.exception.UserNotFoundException;
import com.example.testtaskmirphonebook.model.Phone;
import com.example.testtaskmirphonebook.repository.PhoneRepo;
import com.example.testtaskmirphonebook.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhoneService {

    @Autowired
    private PhoneRepo phoneRepo;
    @Autowired
    private UserRepo userRepo;

    public Phone addPhone(PhoneEntity phone, UUID userId) throws UserNotFoundException, PhoneNumberNotUniqueException {
        UserEntity user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (user.getPhones().contains(phone)) {
            throw new PhoneNumberNotUniqueException("У пользователя уже есть данный номер телефона");
        }
        phone.setUser(user);
        return Phone.toModel(phoneRepo.save(phone));
    }

    public Phone deletePhone(String userName, PhoneEntity phone) throws UserNotFoundException, PhoneNumberNotFoundException {
        UserEntity user = userRepo.findByName(userName).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (phone == null) {
            throw new PhoneNumberNotFoundException("Номера телефона не найден");
        }
        if (!user.getPhones().contains(phone)) {
            throw new PhoneNumberNotFoundException("У пользователя нет данного номера телефона");
        }
        phoneRepo.deleteByUserId(user.getId());
        return Phone.toModel(phone);
    }
}
