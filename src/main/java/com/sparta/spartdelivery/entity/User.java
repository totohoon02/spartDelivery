package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
@Builder
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

    public User(String email, String password, UserRoleEnum role, Integer point, String userName, String phoneNumber, String address, Integer storeId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = point;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.storeId = storeId;
    }

    public void updateStoreId(Integer storeId){
        this.storeId = storeId;
    }

    public Integer depositPoint(Integer totalPrice) {
        // totalPrice가 null이거나 음수인 경우의 검증 로직 추가
        if (totalPrice == null) {
            throw new IllegalArgumentException("totalPrice는 null일 수 없습니다.");
        }
        if (totalPrice < 0) {
            throw new IllegalArgumentException("totalPrice는 음수일 수 없습니다.");
        }

        try {
            // Math.addExact를 사용하여 안전한 덧셈 수행
            this.point = Math.addExact(this.point, totalPrice);
        } catch (ArithmeticException e) {
            // 오버플로우 발생 시, 적절한 예외 처리
            throw new ArithmeticException("포인트 증가로 인해 오버플로우가 발생했습니다.");
        }

        return point;
    }

    public Integer withdrawPoint(Integer totalPrice) throws IllegalArgumentException{
        if (this.point < totalPrice) {
            throw new IllegalArgumentException("포인트가 충분하지 않습니다.");
        }
        this.point -= totalPrice;
        return point;
    }


}



