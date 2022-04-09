package springbook.learningtest.spring.jpa.config;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import springbook.learningtest.spring.jdbc.entity.Member;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = JpaAppConfig.class)
public class JpaAppConfigTest {

    @PersistenceUnit
    EntityManagerFactory emf;
    @Autowired
    ApplicationContext ctx;

    @Test
    public void initTest() {

        for (String name : ctx.getBeanDefinitionNames() ) {
            System.out.println(name);
        }

        EntityManager emf = this.emf.createEntityManager();

        EntityTransaction transaction = emf.getTransaction();
        transaction.begin();

        Member member = new Member(111,"name",2.3);
        emf.persist(member);
//        Member member2 = new Member(102,"name",2.3);
//        emf.persist(member2);

        transaction.commit();
    }

}