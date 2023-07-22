package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.RoleService;
import com.enigma.eprocurement.entity.Role;
import com.enigma.eprocurement.entity.constant.ERole;
import com.enigma.eprocurement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findByRole(role).orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));
    }
}
