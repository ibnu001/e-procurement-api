package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.RoleService;
import com.enigma.eprocurement.entity.Role;
import com.enigma.eprocurement.entity.constant.ERole;
import com.enigma.eprocurement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findByRole(role).orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));
    }
}
