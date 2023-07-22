package com.enigma.eprocurement.repository;

import com.enigma.eprocurement.entity.Role;
import com.enigma.eprocurement.entity.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRole(ERole role);

}
