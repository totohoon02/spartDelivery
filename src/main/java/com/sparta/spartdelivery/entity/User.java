package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private Integer point;

    @Column(nullable = false)
    private String userName;

    private String phoneNumber;

    private String address;

    @Column
    private Integer storeId;

    public User(String email, String password, String username, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.userName = username;
        this.role = role;
    }
}



