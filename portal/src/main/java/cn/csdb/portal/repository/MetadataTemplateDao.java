package cn.csdb.portal.repository;

import cn.csdb.portal.model.MetadataTemplate;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @program: DataSync
 * @description: metadata template dao
 * @author: xiajl
 * @create: 2019-03-08 14:08
 **/
@Repository
public class MetadataTemplateDao {
    @Resource
    private MongoTemplate mongoTemplate;

    //获取所有的扩展元数据字段信息
    public List<MetadataTemplate> getAll(){
        DBObject query = QueryBuilder.start().get();
        BasicQuery basicQuery = new BasicQuery(query);
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "sortOrder");
        List<Sort.Order> orders = Lists.newArrayList(order);
        basicQuery.with(new Sort(orders));
        List<MetadataTemplate> list = mongoTemplate.find(basicQuery, MetadataTemplate.class);
        return list;
    }

    public void save(MetadataTemplate metadataTemplate){
        if (!metadataTemplate.getExtField().startsWith("ext_")){
            String str = metadataTemplate.getExtField();
            metadataTemplate.setExtField("ext_" + str);
        }
        if (!metadataTemplate.getType().equals("List")){
            metadataTemplate.setEnumdata("");
        }
        mongoTemplate.save(metadataTemplate);
    }

    public void delete(String id){
        DBObject query = QueryBuilder.start().and("_id").is(id).get();
        BasicQuery basicQuery = new BasicQuery(query);
        mongoTemplate.remove(basicQuery,MetadataTemplate.class);
    }


    public MetadataTemplate get(String id){
        return mongoTemplate.findById(id,MetadataTemplate.class);
    }

    //根据字段名、字段中文名称查询
    public  List<MetadataTemplate> getList(String extField, String extFieldName){
        QueryBuilder queryBuilder = QueryBuilder.start();
        if (StringUtils.isNotEmpty(extField)){
            queryBuilder = queryBuilder.and("extField").regex(Pattern.compile("^.*" +extField +".*$"));
        }
        if (StringUtils.isNotEmpty(extFieldName)){
            queryBuilder = queryBuilder.and("extFieldName").regex(Pattern.compile("^.*" +extFieldName +".*$"));
        }
        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order order = new Sort.Order(Sort.Direction.ASC,"sortOrder");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(order);
        basicQuery.with(new Sort(sos));
        return mongoTemplate.find(basicQuery,MetadataTemplate.class);
    }

    public List<MetadataTemplate> findAll(){
        return mongoTemplate.findAll(MetadataTemplate.class);
    }


    public boolean exist(String extField){
        boolean result = false;
        QueryBuilder queryBuilder = QueryBuilder.start();

        if (org.apache.commons.lang.StringUtils.isNotEmpty(extField)){
            queryBuilder = queryBuilder.and("extField").is(extField);
        }

        DBObject dbObject = queryBuilder.get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        List<MetadataTemplate> list = mongoTemplate.find(basicQuery,MetadataTemplate.class);
        if (list.size() > 0){
            result = true;
        }
        return result;
    }

    //获取最大的序列号
    public Integer getMaxSortOrder(){
        List<MetadataTemplate> list = findAll();
        if (list.size() == 0)
        {
            return 0;
        }else
        {
            return list.get(list.size()-1).getSortOrder();
        }
    }

}
