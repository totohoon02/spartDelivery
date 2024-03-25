//package com.sparta.spartdelivery.entity;
//
//import com.sparta.spartdelivery.enums.UserRoleEnum;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "User")
//public class  User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer userId;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;
//
//    @Column
//    private Integer point;
//
//    @Column(nullable = false)
//    private String userName;
//
//    private String phoneNumber;
//
//    private String address;
//
//    @Column
//    private Integer storeId;
//
//    public User(String email, String password, String username, UserRoleEnum role) {
//        this.email = email;
//        this.password = password;
//        this.userName = username;
//        this.role = role;
//    }
//
//    public void updateStoreId(Integer storeId){
//        this.storeId = storeId;
//    }
//
//
//}
//
//
//

package com.sparta.spartdelivery.entity;

import com.sparta.spartdelivery.enums.UserRoleEnum;
import com.sparta.spartdelivery.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
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

    @Column()
    private String password;

    @Setter
    @Column()
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Setter
    @Column
    private Integer point;

    @Column(nullable = false)
    private String userName;

    @Setter
    private String phoneNumber;

    @Setter
    private String address;

    @Column
    private Integer storeId;

    @Column(columnDefinition = "ENUM('GOOGLE', 'NORMAL') DEFAULT 'NORMAL'")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String id;

    public User(String email, String password, String username, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.userName = username;
        this.role = role;
    }

    public void updateStoreId(Integer storeId){
        this.storeId = storeId;
    }

    public void addPoint(Integer point) {
        this.point += point;
    }
    public void subtractPoint(Integer point) {
        this.point -= point;
    }


}



