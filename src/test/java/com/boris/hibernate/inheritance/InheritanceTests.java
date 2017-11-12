package com.boris.hibernate.inheritance;

import com.boris.HelloHibernateAndJpa;
import com.boris.model.inheritance.Master1;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

import javax.transaction.Transactional;

/**
 * Created by Щукин on 10.08.2017.
 */
@SpringApplicationConfiguration(classes = HelloHibernateAndJpa.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class InheritanceTests extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    HibernateTemplate hibernateTemplate;


    @Test
    public void testStart()
    {
            hibernateTemplate.load(Master1.class, 11);
    }

}
