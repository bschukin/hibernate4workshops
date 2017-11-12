package com.boris.hibernate.ids;

import com.boris.HelloHibernateAndJpa;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * Created by Щукин on 10.08.2017.
 */
@SpringApplicationConfiguration(classes = HelloHibernateAndJpa.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class IdGenerationTests extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    HibernateTemplate hibernateTemplate;


    @Test
    public void testGenerateIdAsIdentity()
    {
        Aid a = new Aid();
        long  id  = (long) hibernateTemplate.save(a);
        Assert.assertEquals(id, 1);

        id  = (long) hibernateTemplate.save(new  Aid());
        Assert.assertEquals(id, 2);
    }

    @Test
    public void testGenerateIdFromSequence()
    {
        long  id  = (long) hibernateTemplate.save(new Bid());
        Assert.assertEquals(id, 1);

        id  = (long) hibernateTemplate.save(new  Bid());
        Assert.assertEquals(id, 2);
    }

    @Test
    public void testGenerateIdWithCustomGenerator()
    {
        long id = (long) hibernateTemplate.save(new Cid1());
        System.out.println(id);
    }


    @Entity(name = "Aid")
    public class Aid {

        protected Long id;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }


    @Entity(name = "Bid")
    public class Bid {

        protected Long id;

        @Id
        @GeneratedValue(generator = "SEQ_GEN")
        @javax.persistence.SequenceGenerator(
                name="SEQ_GEN",
                sequenceName="bid_sequence",
                allocationSize=20
        )
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }


    @Entity(name = "Cid1")
    public class Cid1 {

        protected Long id;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GEN_2")
        @GenericGenerator(name = "ID_GEN_2",
                strategy = "com.boris.hibernate.ids.MyGenerator")
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }



}
