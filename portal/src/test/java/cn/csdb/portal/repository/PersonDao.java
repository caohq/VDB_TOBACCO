package cn.csdb.portal.repository;

import cn.csdb.portal.model.Person;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.*;
import java.util.List;

/**
 * @program: DataSync
 * @description: a test class for person repository
 * @author: xiajl
 * @create: 2018-10-22 09:54
 **/
@Repository
public class PersonDao {
    @Resource
    private MongoTemplate mongoTemplate;

    public void save(Person person){
        mongoTemplate.save(person);
    }

    public List<Person> findAll(){
        return mongoTemplate.findAll(Person.class);
    }
}
