package com.leedae.mockaroo.repository;

import com.leedae.mockaroo.doamin.User;
import com.leedae.mockaroo.doamin.constant.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);
    boolean existsByEmail(String email);
}