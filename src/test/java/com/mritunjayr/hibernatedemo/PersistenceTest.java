package com.mritunjayr.hibernatedemo;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * This is testing for a simple application using Testng framework and db connection by hibernate configuration and
 * saving a state of java POJOs and validating saved state.
 */
public class PersistenceTest {

    Session session;
    Logger logger;

    /**
     * this method initialises hibernate session object to interact with db and logger.
     */
    @BeforeClass
    public void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        session = new MetadataSources(registry).buildMetadata().buildSessionFactory().openSession();
        logger = LoggerFactory.getLogger(PersistenceTest.class);
        logger.info("Logger initialized.");
    }

    @AfterClass
    public void afterClass() {
        session.close();
        logger.info("Hibernate session closed.");
    }

    @Test
    public void saveMessage() {
        Message message = new
                Message("Hello World!");
        Transaction tx = session.beginTransaction();
        session.save(message);
        tx.commit();
    }

    @Test(dependsOnMethods = {"saveMessage"})
    public void testMessage() {
        String SELECT = "from Message";
        List<Message> messages = session.createQuery(SELECT, Message.class).list();
        assertEquals(messages.size(), 1);

        messages.stream().map(x -> x.getMessage()).forEach(logger::info);
    }

}