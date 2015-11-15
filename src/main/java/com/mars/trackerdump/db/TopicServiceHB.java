package com.mars.trackerdump.db;

import com.mars.trackerdump.config.HibernateUtil;
import com.mars.trackerdump.entity.Topic;
import java.util.Arrays;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TopicServiceHB implements TopicService {

    String dbName = null;

    public TopicServiceHB setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    @Override
    public <S extends Topic> S save(S s) {
        save(Arrays.asList(s));
        return s;
    }

    @Override
    public <S extends Topic> Iterable<S> save(Iterable<S> itrbl) {
        Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory(dbName).openSession();
        try {
            for (S item : itrbl) {
                trns = session.beginTransaction();
                session.save(item);
                session.getTransaction().commit();
            }
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
        } finally {
            session.flush();
            session.close();
        }
        return itrbl;
    }

    @Override
    public Topic findOne(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Topic> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Topic> findAll(Iterable<Long> itrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Topic t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Iterable<? extends Topic> itrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
