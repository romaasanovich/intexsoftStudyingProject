package com.intexsoft.bookservice.dao.repository;

import com.intexsoft.bookservice.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM user WHERE login =?1", nativeQuery = true)
    User findByUsername(String username);
}
