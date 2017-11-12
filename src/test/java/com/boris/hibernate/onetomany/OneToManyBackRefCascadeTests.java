package com.boris.hibernate.onetomany;

import com.boris.HelloHibernateAndJpa;
import com.boris.model.oneToN.*;
import com.boris.repository.MasterARepository;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringApplicationConfiguration(classes = HelloHibernateAndJpa.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class OneToManyBackRefCascadeTests extends AbstractTransactionalTestNGSpringContextTests {

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
    //тест показывает что при маппинге 1-N (c обратными связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL)
    //при сохранениии мастера каскадно проинсертятся слейвы (c апдейтами по id A)
    public void testCascaseInsertSlavesWithBackRefs() {

        SQLStatementCountValidator.reset();

        A aaa = new A("Category A");
        aaa.getSlaves3().add(new D("D A1"));
        aaa.getSlaves3().add(new D("D A2"));

        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        Counter counter = new Counter();
        for (A a : masterARepository.findAll()) {
            logger.info(a.toString());
            a.getSlaves3().forEach(
                    b -> {
                        System.out.println(b.getId());
                        counter.count++;
                        Assert.assertEquals(b.getMaster(), a);
                    }
            );
        }

        Assert.assertEquals(2, counter.count);
        SQLStatementCountValidator.assertInsertCount(3);
        //2 апдейта на вставку айдишников мастера в Слейвы Б
        SQLStatementCountValidator.assertUpdateCount(2);

    }

    @Test
    //тест показывает что при маппинге 1-N (без обратных связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL)
    //при сохранениии мастера каскадно проинсертятся слейвы (без апдейтов по id A)
    public void testCascaseInsertSlavesBackRefsWithoutUpdates() {

        SQLStatementCountValidator.reset();

        A aaa = new A("Category A");
        aaa.getSlaves4().add(new E("E A1"));
        aaa.getSlaves4().add(new E("E A2"));

        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        Counter counter = new Counter();
        for (A a : masterARepository.findAll()) {
            logger.info(a.toString());
            a.getSlaves4().forEach(
                    e -> {
                        System.out.println(e.getId());
                        counter.count++;
                        Assert.assertEquals(e.getMaster(), a);
                    }
            );
        }
        Assert.assertEquals(counter.count, 2);
        SQLStatementCountValidator.assertInsertCount(3);
        //2 апдейта на вставку айдишников мастера в Слейвы Б
        SQLStatementCountValidator.assertUpdateCount(0);

    }

    @Test
    //тест показывает что при маппинге 1-N (без обратных связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL, LAZY)
    //слейвы загрузсятся одним запросом при первом обращении

    public void testLoadLazyBackRefsSlavesWith1Select() {

        A aaa = new A("Category A");
        aaa.getSlaves3().add(new D("B A1"));
        aaa.getSlaves3().add(new D("B A2"));
        hibernateTemplate.save(aaa);

        SQLStatementCountValidator.reset();

        for (A a : masterARepository.findAll())
            System.out.println(a);

        //к слейвам не обращалимсь - токмо 1 селект
        SQLStatementCountValidator.assertSelectCount(1);

        SQLStatementCountValidator.reset();
        Counter counter = new Counter();
        for (A a : masterARepository.findAll()) {
            System.out.println(a);
            a.getSlaves3().forEach(b ->{
                    System.out.println(b);
            counter.count++;}
            );

        }

        //обращались к слейвам ровно 1 один раз
        SQLStatementCountValidator.assertSelectCount(2);

        //каунт на коллекции - обращение к базе есть!
        SQLStatementCountValidator.reset();
        for (A a : masterARepository.findAll()) {
            System.out.println(a.getSlaves().size());
        }
        SQLStatementCountValidator.assertSelectCount(2);

    }


    //тест показывает что при маппинге 1-N (с обратными связями от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL, LAZY)
    @Test(invocationCount = 1)
    //todo: переписать на класс D E
    public void testUpdateSlaves() {


        A aaa = new A("A1");
        aaa.getSlaves().add(new B("B1"));
        aaa.getSlaves().add(new B("B2"));
        hibernateTemplate.save(aaa);
        //hibernateTemplate.flush();

        /*SQLStatementCountValidator.reset();
        B b = aaa.getSlaves().stream().findFirst().orElse(null);
        b.setName("xxx");

        //вызывать явно сейв не требуется...
        //hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        //хиберней обновляет слейва в три захода
        SQLStatementCountValidator.assertUpdateCount(3);

*/
    }

    @Test(invocationCount = 1)
    //тест показывает что при маппинге 1-N (без обратных связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL, LAZY) !без delete orpnan!
    //слейвы удаляются одним запросом при первом обращении
    public void testCasacadeDeleteSlavesWithoutOrhpan() {

        SQLStatementCountValidator.reset();

        A aaa = new A("A1");
        aaa.getSlaves().add(new B("B1"));
        aaa.getSlaves().add(new B("B2"));
        hibernateTemplate.save(aaa);

        SQLStatementCountValidator.assertInsertCount(3);

        hibernateTemplate.delete(aaa);
        hibernateTemplate.flush();
        SQLStatementCountValidator.assertDeleteCount(3);


    }
}
