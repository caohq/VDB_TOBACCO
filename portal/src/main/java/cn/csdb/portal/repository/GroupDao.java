package cn.csdb.portal.repository;

import cn.csdb.portal.model.Group;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @program: DataSync
 * @description: user group Dao
 * @author: xiajl
 * @create: 2018-10-30 10:17
 **/
@Repository
public class GroupDao {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * Function Description: 增加保存group
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/30 10:54
     */
    public void add(Group group){
        mongoTemplate.save(group);
    }

    public void update(Group group){
        mongoTemplate.save(group);
    }

    /**
     * Function Description: 删除group记录,同时也要删除用户表中相关联的group记录字段;
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/30 10:58
     */
    public void delete(String id){

        DBObject query = QueryBuilder.start().and("_id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        mongoTemplate.remove(basicQuery, Group.class);
    }

    public void delete(Group group){
        mongoTemplate.remove(group);
    }

    /**
     * Function Description: 获取Group
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/30 13:40
     */
    public Group get(String id){
        return mongoTemplate.findById(id,Group.class);
    }

    /**
     * Function Description: 获取分组列表
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/30 11:13
     */
    public List<Group> getListByPage(String groupName, int pageNo, int pageSize){
        QueryBuilder queryBuilder = QueryBuilder.start();

        if (StringUtils.isNotEmpty(groupName)){
            queryBuilder = queryBuilder.and("groupName").regex(Pattern.compile("^.*"+groupName+".*$"));
        }

        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "createTime");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip((pageNo-1)*pageSize );
        basicQuery.limit(pageSize);
        return mongoTemplate.find(basicQuery, Group.class);
    }

    /**
     * Function Description:获取个数
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/30 11:15
     */
    public long countByPage(String groupName){
        QueryBuilder queryBuilder = QueryBuilder.start();

        if (StringUtils.isNotEmpty(groupName)){
            queryBuilder = queryBuilder.and("groupName").regex(Pattern.compile("^.*"+groupName+".*$"));
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        return mongoTemplate.count(basicQuery,Group.class);
    }

    public List<Group> getGroupList() {
        return mongoTemplate.findAll(Group.class);
    }

    public List<Group> getAll(){
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"createTime");
        List<Sort.Order> orders = Lists.newArrayList(order);
        List<Group> list = mongoTemplate.find(basicQuery, Group.class);
        return list;
    }

    /**
     * 是否存在同名的用户组(唯一性判断)
     * @param groupName
     * @return
     */
    public boolean exist(String groupName){
        boolean result = false;
        QueryBuilder queryBuilder = QueryBuilder.start();

        if (StringUtils.isNotEmpty(groupName)){
            queryBuilder = queryBuilder.and("groupName").is(groupName);
        }

        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        List<Group> list = mongoTemplate.find(basicQuery,Group.class);
        if (list.size() > 0)
            result = true;
        return result;
    }
}
