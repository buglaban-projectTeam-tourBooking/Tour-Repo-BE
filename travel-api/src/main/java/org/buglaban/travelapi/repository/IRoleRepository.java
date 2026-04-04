package org.buglaban.travelapi.repository;

import jakarta.validation.constraints.Email;
import org.buglaban.travelapi.model.Role;
import org.buglaban.travelapi.util.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserType userType);

    @Query(value = "select rl.*\n" +
            "from users us\n" +
            "inner join roles rl on rl.id = us.role_id\n" +
            "where us.email = ?1",nativeQuery = true )
    Role getRoleByIdUser(@Email(message = "email invalid format") String email);
}
