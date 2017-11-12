package com.boris.hibernate.onetomany;

import com.boris.HelloHibernateAndJpa;
import com.boris.model.oneToN.A;
import com.boris.model.oneToN.B;
import com.boris.model.oneToN.C;
import com.boris.repository.MasterARepository;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
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
public class OneToManyNoBackRefCascadeTests extends AbstractTransactionalTestNGSpringContextTests {

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
    //тест показывает что при маппинге 1-N (без обратных связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL)
    //при сохранениии мастера каскадно проинсертятся слейвы (c апдейтами по id A)
    public void testCascaseInsertSlavesNoBackRefs() {

        SQLStatementCountValidator.reset();

        A aaa = new A("Category A");
        aaa.getSlaves().add(new B("B A1"));
        aaa.getSlaves().add(new B("B A2"));

        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        Counter counter = new Counter();
       // System.out.println(hibernateTemplate.find("from A a left join fetch a.slaves"));

        Criteria criteria = hibernateTemplate.getSessionFactory().getCurrentSession().createCriteria(A.class);
        //criteria.setFetchMode("slaves", FetchMode.JOIN);
        System.out.println(criteria.list());

        //System.out.println(hibernateTemplate.findByCriteria((DetachedCriteria) criteria));

        for (A a : masterARepository.findAll()) {
            logger.info(a.toString());
            a.getSlaves().forEach(
                    b -> {
                        System.out.println(b.getId());
                        counter.count++;
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
    public void testCascaseInsertSlavesNoBackRefsWithoutUpdates() {

        SQLStatementCountValidator.reset();

        A aaa = new A("Category A");
        aaa.getOtherSlaves().add(new C("C A1"));
        aaa.getOtherSlaves().add(new C("C A2"));

        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        Counter counter = new Counter();
        for (A a : masterARepository.findAll()) {
            logger.info(a.toString());
            a.getOtherSlaves().forEach(
                    c -> {
                        System.out.println(c.getId());
                        counter.count++;
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
    public void testLoadLazySlavesWith1Select() {

        A aaa = new A("Category A");
        aaa.getSlaves().add(new B("B A1"));
        aaa.getSlaves().add(new B("B A2"));
        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        SQLStatementCountValidator.reset();

        for (A a : masterARepository.findAll())
            System.out.println(a);

        //к слейвам не обращалимсь - токмо 1 селект
        SQLStatementCountValidator.assertSelectCount(1);

        Counter counter = new Counter();
        SQLStatementCountValidator.reset();
        for (A a : masterARepository.findAll()) {

            a.getSlaves().forEach(b ->
                    {
                        System.out.println(b);
                        counter.count++;
                    }
            );

        }
        Assert.assertEquals(2, counter.count);
        //обращались к слейвам ровно 1 один раз
        SQLStatementCountValidator.assertSelectCount(2);

        //каунт на коллекции - обращения к базе нет!
        SQLStatementCountValidator.reset();
        for (A a : masterARepository.findAll()) {
            System.out.println(a.getSlaves().size());
        }
        SQLStatementCountValidator.assertSelectCount(1);

    }


    //тест показывает что при маппинге 1-N (без обратных связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL, LAZY)
    //слейвы загрузсятся одним запросом при первом обращении
    @Test(invocationCount = 1)
    public void testUpdateSlaves() {


        A aaa = new A("A1");
        aaa.getSlaves().add(new B("B1"));
        aaa.getSlaves().add(new B("B2"));
        hibernateTemplate.save(aaa);
       hibernateTemplate.flush();

        SQLStatementCountValidator.reset();
        B b = aaa.getSlaves().stream().findFirst().orElse(null);
        b.setName("xxx");

        //вызывать явно сейв не требуется...
        //hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        SQLStatementCountValidator.assertUpdateCount(1);

    }

    //тест показывает что при маппинге 1-N (без обратных связей от слейва на мастера)
    //и настройке каскада @OneToMany(cascade = CascadeType.ALL, LAZY)
    //доавбление слейва выполнится в один инсерт
    @Test(invocationCount = 1)
    public void testAddNewSlavesWithOnlyInsert() {


        A aaa = new A("A1");
        aaa.getOtherSlaves().add(new C("C1"));
        aaa.getOtherSlaves().add(new C("С2"));
        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();

        hibernateTemplate.clear();

        aaa = hibernateTemplate.get(A.class, aaa.getId());
        SQLStatementCountValidator.reset();
        C c = new C("C3");

        aaa.getOtherSlaves().add(c);

        //вызывать явно сейв не требуется...
        hibernateTemplate.save(aaa);
        hibernateTemplate.flush();


        //хиберней обновляет слейва в три захода
        SQLStatementCountValidator.assertInsertCount(1);
        SQLStatementCountValidator.assertUpdateCount(0);

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
