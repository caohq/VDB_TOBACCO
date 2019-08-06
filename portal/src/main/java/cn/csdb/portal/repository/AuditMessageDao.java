package cn.csdb.portal.repository;

import cn.csdb.portal.model.AuditMessage;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-11-22 10:57
 **/
@Repository
public class AuditMessageDao {

    @Resource
    private MongoTemplate mongoTemplate;
    
    /**
     *
     * Function Description: 
     *
     * @param: [auditMessage]
     * @return: java.lang.String
     * @auther: hw
     * @date: 2018/11/22 11:01
     */
    public String save(AuditMessage auditMessage){
        mongoTemplate.save(auditMessage);
        return auditMessage.getId();
    }
    
    
    /**
     *
     * Function Description: 
     *
     * @param: [resourceId]
     * @return: java.util.List<cn.csdb.portal.model.AuditMessage>
     * @auther: hw
     * @date: 2018/11/22 16:02
     */
    public List<AuditMessage> getAuditMessageListByResourceId(String resourceId){
        QueryBuilder queryBuilder = QueryBuilder.start();
        queryBuilder = queryBuilder.and("resourceId").is(resourceId);
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "auditTime");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        List<AuditMessage> auditMessageList = mongoTemplate.find(basicQuery,AuditMessage.class);
        return auditMessageList;
    }
}
