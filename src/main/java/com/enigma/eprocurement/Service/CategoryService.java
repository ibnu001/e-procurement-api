package com.enigma.eprocurement.Service;

import com.enigma.eprocurement.entity.Category;

public interface CategoryService {

    Category getOrSave(String name);

}
