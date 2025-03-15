package com.guisebastiao.apispringboot.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guisebastiao.apispringboot.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  Optional<User> findByNameAndEmailAndPassword(String name, String email, String password);
}
