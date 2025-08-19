package com.cvconnect.repository;

import com.cvconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isDeleted = false AND u.accessMethod LIKE '%LOCAL%'")
    Optional<User> findByUsernameLogin(String username);
}
