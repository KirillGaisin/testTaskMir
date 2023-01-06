package com.example.testtaskmirphonebook.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class PhoneEntity {

    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;
    @Column
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
