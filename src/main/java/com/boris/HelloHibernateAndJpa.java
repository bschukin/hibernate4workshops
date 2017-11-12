package com.boris;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.persistence.EntityManager;

@SpringBootApplication
@ImportResource("classpath:app-context.xml")
public class HelloHibernateAndJpa {

    @Autowired
    EntityManager entityManager;

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }



    @Bean
    public HibernateTemplate hibernateTemplate(SessionFactory sessionFactory) {
        return new HibernateTemplate(sessionFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloHibernateAndJpa.class, args);
    }


}
