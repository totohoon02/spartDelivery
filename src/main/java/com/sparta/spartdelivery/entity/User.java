package com.sparta.spartdelivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User") // ERD에 따라 시작만 대문자인 User로 작성
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId; // 변수명 작성룰에 따라 Snake가 아닌 Camel케이스로 작성

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private int point = 1000000; // 100만 포인트를 여기서 지급

    @Column(nullable = true) // 고객일 경우 null
    private Integer storeId;

    public User(String email, String password, String username, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.userName = username;
        this.role = role;
    }

}