package com.boris.hibernate.ids;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.SequenceGenerator;

import java.io.Serializable;

/**
 * Created by Щукин on 10.08.2017.
 */
public class MyGenerator  extends SequenceGenerator
{
    @Override
    public Serializable generate(SessionImplementor session, Object obj) {
        return 1000L;
    }
}