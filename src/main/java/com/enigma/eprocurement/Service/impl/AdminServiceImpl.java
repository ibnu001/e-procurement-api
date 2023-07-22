package com.enigma.eprocurement.Service.impl;

import com.enigma.eprocurement.Service.AdminService;
import com.enigma.eprocurement.entity.Admin;
import com.enigma.eprocurement.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin create(Admin admin) {
        try {
            return adminRepository.save(admin);
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exist");
        }
    }
}
