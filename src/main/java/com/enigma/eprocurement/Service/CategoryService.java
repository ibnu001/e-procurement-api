package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.entity.Category;
import com.enigma.eprocurement.entity.Role;
import com.enigma.eprocurement.entity.constant.ERole;

public interface CategoryService {

    Category getOrSave(String name);

}
