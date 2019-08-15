package cn.csdb.portal.repository;

import cn.csdb.portal.model.CreatedTables;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CreatedTablesByExcelDao {

    @Resource
    private MongoTemplate mongoTemplate;
    /**
    * @Description: 判断该excel文件是否已经创建表
    * @Param: [themeCode, subjectCode, fileName]
    * @return: boolean
    * @Author: zcy
    * @Date: 2019/8/15
    */
     public boolean IsCreatedTable(String themeCode,String subjectCode,String fileName){
       List<CreatedTables> list= mongoTemplate.find(new Query(Criteria.where("themeCode").is(themeCode).and("subjectCode").is(subjectCode).and("fileName").is(fileName)),CreatedTables.class);
        if(list.size()==0){
            return false;
        }else{
            return true;
        }
     }

     public void addCreatedTables(CreatedTables createdTables){
         mongoTemplate.insert(createdTables);
     }
}
