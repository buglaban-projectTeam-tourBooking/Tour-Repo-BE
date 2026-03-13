package org.buglaban.travelapi.repository;

import org.buglaban.travelapi.model.Role;
import org.buglaban.travelapi.util.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserType userType);
}
