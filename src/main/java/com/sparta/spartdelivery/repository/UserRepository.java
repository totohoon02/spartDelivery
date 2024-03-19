package com.sparta.spartdelivery.repository;

import com.sparta.spartdelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;;

public interface UserRepository extends JpaRepository<User, Long> {
}
