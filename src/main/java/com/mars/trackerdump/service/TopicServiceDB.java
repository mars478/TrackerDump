package com.mars.trackerdump.service;

import com.mars.trackerdump.db.DbConnection;
import com.mars.trackerdump.db.dialect.DiaPG;
import com.mars.trackerdump.entity.Topic;
import com.mars.trackerdump.log.Loggable;
import com.mars.trackerdump.prop.Prop;
import java.sql.ResultSet;
import org.springframework.stereotype.Repository;

@Repository
public class TopicServiceDB extends Loggable implements TopicService {

    static {
        new DiaPG();
    }

    String dbName = null;
    DbConnection dbc = null;

    final String CONNECTION_STRING;

    String username = "";
    String password = "";

    public TopicServiceDB() {
        CONNECTION_STRING = Prop.getValue("conn_str");
        username = Prop.getValue("username");
        password = Prop.getValue("password");

        dbc = new DbConnection(username, password, CONNECTION_STRING);
        /*
         try (ResultSet rs = new Query(dbc).execPrepared(sql, par).rs();) {
         if (rs.next()) {
         photoRegno = rs.getBytes("photo");
         }
         } catch (Exception e) {
         log(e);
         }*/
    }

    @Override
    public <S extends Topic> S save(S s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends Topic> Iterable<S> save(Iterable<S> itrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public TopicService setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

}
