package com.enigma.eprocurement.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CustomIdGenerator implements IdentifierGenerator {
    private static Integer counter = 1;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String prefix = "KB";
        String code = String.format("%s%02d", prefix, counter);
        counter++;
        return code;
    }
}
