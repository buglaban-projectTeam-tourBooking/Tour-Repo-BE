package org.buglaban.travelapi.repository;

import jakarta.validation.constraints.Email;
import org.buglaban.travelapi.model.Role;
import org.buglaban.travelapi.model.User;
import org.buglaban.travelapi.util.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository <User, Long> {

    Optional<User> findByEmail(@Email(message = "email invalid format") String email);
    Optional<User> findById(long id);
}
