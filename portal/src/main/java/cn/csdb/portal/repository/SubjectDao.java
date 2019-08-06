package cn.csdb.portal.repository;

import cn.csdb.portal.model.Subject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: DataSync
 * @description: Subject Dao class
 * @author: xiajl
 * @create: 2018-10-22 15:54
 **/
@Repository
public class SubjectDao {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * Function Description: 根据subjectCode查找对象
     *
     * @param:  subjectCode:专题库代码
     * @return: Sujbect对像
     * @auther: Administrator
     * @date:   2018/10/22 16:00
     */
    public Subject findBySubjectCode(String subjectCode){
        DBObject query = QueryBuilder.start().and("subjectCode").is(subjectCode).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Subject> list = mongoTemplate.find(basicQuery,Subject.class);
        if(list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * Function Description: 根据用户获取专题库信息
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/31 9:36
     */
    public Subject findByUser(String userName){
        DBObject query = QueryBuilder.start().and("admin").is(userName).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Subject> list = mongoTemplate.find(basicQuery,Subject.class);
        if(list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }

    public void save(Subject subject){
        mongoTemplate.save(subject);
    }


    /**
     * Function Description: validate subject code login
     * @param userName
     * @param password
     * @return loginStatus
     */
    public int validateLogin(String userName, String password)
    {
        int loginStatus = 0;
        DBObject query = QueryBuilder.start().and("admin").is(userName).and("adminPasswd").is(password).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Subject> list = mongoTemplate.find(basicQuery,Subject.class);
        if(list.size() != 0)
        {
            loginStatus = 1;
        }

        return loginStatus;
    }

       // 查询t_subject主题库
     public List<Subject> findAllsubject(){
        List<Subject> list=mongoTemplate.findAll(Subject.class);
        return list;
     }

}
