package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.model.request.AuthRequest;
import com.enigma.eprocurement.model.response.LoginResponse;
import com.enigma.eprocurement.model.response.RegisterResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    RegisterResponse registerAdmin(AuthRequest request);
    RegisterResponse registerVendor(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
