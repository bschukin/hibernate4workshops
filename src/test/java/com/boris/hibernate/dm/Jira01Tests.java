package com.boris.hibernate.dm;

import com.boris.HelloHibernateAndJpa;
import com.boris.model.dm.Department;
import com.boris.model.dm.Gender;
import com.boris.repository.MasterARepository;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringApplicationConfiguration(classes = HelloHibernateAndJpa.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class Jira01Tests extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private MasterARepository masterARepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    HibernateTemplate hibernateTemplate;

    private static class Counter {
        int count = 0;
    }


    @Test
    public void testLoadGenders() {

        SQLStatementCountValidator.reset();

        hibernateTemplate.loadAll(Gender.class).forEach(
          g-> System.out.println(g.getGender())
        );
        SQLStatementCountValidator.assertSelectCount(1);

    }

    @Test
    public void testLoadJiraDepartment() {

        SQLStatementCountValidator.reset();

        //hibernateTemplate.loadAll(Department.class).forEach(
          //g-> System.out.println(g.getId()
          //)
        //);

        Department jd1 = hibernateTemplate.load(Department.class, 3);

        System.out.println(jd1.getName());
        System.out.println(jd1.getParent().getName());
        System.out.println(jd1.getParent().getParent().getName());

        SQLStatementCountValidator.assertSelectCount(2);

    }


}
