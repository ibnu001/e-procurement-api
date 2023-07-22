package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.entity.Role;
import com.enigma.eprocurement.entity.constant.ERole;

public interface RoleService {

    Role getOrSave(ERole role);

}

