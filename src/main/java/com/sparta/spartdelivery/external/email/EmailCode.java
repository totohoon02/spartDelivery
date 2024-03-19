package com.sparta.spartdelivery.external.email;

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
@Table(name = "EmailCode")
public class EmailCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String emailCode; // 이메일 인증코드

    public EmailCode(String to, String ePw) {
        this.email = to;
        this.emailCode = ePw;
    }
}
