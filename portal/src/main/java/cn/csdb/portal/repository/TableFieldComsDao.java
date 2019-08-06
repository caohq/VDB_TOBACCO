package cn.csdb.portal.repository;

import cn.csdb.portal.model.Described_Table;
import cn.csdb.portal.model.ResCatalog_Mongo;
import cn.csdb.portal.model.TableFieldComs;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by shibaoping on 2018/10/29 18:07.
 */
@Repository
public class TableFieldComsDao {

    @Resource
    private MongoTemplate mongoTemplate;


    public List<TableFieldComs> getTableFieldComsByUriEx(int uriHash) {
        return mongoTemplate.find(new Query(Criteria.where("uriHash").is(uriHash)),TableFieldComs.class);
    }

    public void updateFieldComs(TableFieldComs tableFieldComs,String subjectCode,String dbName,String tableName,String state) {

        mongoTemplate.findAndModify(new Query(Criteria.where("id").is(tableFieldComs.getId())),
                new Update().set("fieldComs", tableFieldComs.getFieldComs()).set("updateTime", tableFieldComs.getUpdateTime())
        ,TableFieldComs.class);
        Described_Table described_table = new Described_Table();
        described_table.setTableName(tableName);
        described_table.setSubjectCode(subjectCode);
        described_table.setDbName(dbName);
        mongoTemplate.save(tableFieldComs);
        if("1".equals(state)){
            List<Described_Table> d = queryIsDescribe(dbName,tableName);
            if(d.size()==0){
                mongoTemplate.save(described_table);
            }else{

            }
        }else{

        }
    }

    public void saveTableFieldComs(TableFieldComs tableFieldComs,String subjectCode,String dbName,String tableName,String state) {
        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"id")));
        List<TableFieldComs> resList = this.mongoTemplate.find(query, TableFieldComs.class);
        if(resList.size()==0){
            tableFieldComs.setId(1);
        }else{
            TableFieldComs res = resList.get(0);
            tableFieldComs.setId(res.getId()+1);
        }
        Described_Table described_table = new Described_Table();
        described_table.setTableName(tableName);
        described_table.setSubjectCode(subjectCode);
        described_table.setDbName(dbName);
        mongoTemplate.save(tableFieldComs);
        if("1".equals(state)){
        List<Described_Table> d = queryIsDescribe(dbName,tableName);
            if(d.size()==0){
                mongoTemplate.save(described_table);
            }else{

            }
        }else{

        }
    }

    public List<Described_Table> queryDescribeTable(String dbName){
        return mongoTemplate.find(new Query(Criteria.where("dbName").is(dbName)),Described_Table.class);
    }

    public List<Described_Table> queryIsDescribe(String dbName,String tableName){
        return mongoTemplate.find(new Query(Criteria.where("dbName").is(dbName).and("tableName").is(tableName)),Described_Table.class);
    }
}
