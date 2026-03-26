package org.buglaban.travelapi.model;
// ROLE ENTITY - Vai trò người dùng (Admin, Staff, Customer)
import jakarta.persistence.*;
import lombok.*;
import org.buglaban.travelapi.util.UserType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private UserType roleName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Quan hệ One-to-Many với User
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
}