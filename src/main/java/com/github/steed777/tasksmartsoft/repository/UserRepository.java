package com.github.steed777.tasksmartsoft.repository;

import com.github.steed777.tasksmartsoft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
