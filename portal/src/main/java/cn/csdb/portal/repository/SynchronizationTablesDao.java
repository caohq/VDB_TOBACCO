package cn.csdb.portal.repository;

import cn.csdb.portal.model.Period;
import cn.csdb.portal.model.SynchronizationTable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;


@Repository
public class SynchronizationTablesDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void save(SynchronizationTable synchronizeTable) {
        mongoTemplate.save(synchronizeTable);
    }

    public List<SynchronizationTable> selectAllOfDataAssembler() {
        List<SynchronizationTable> all = mongoTemplate.find(new Query(Criteria.where("systemName").is("DataAssembler")), SynchronizationTable.class);
        return all;
    }

    /**
     * @Description: 根据表名和subjectCode查询该表是否同步
     * @Param: [subjectCode, tableName]
     * @return: cn.csdb.portal.model.SynchronizationTables
     * @Author: zcy
     * @Date: 2019/5/15
     */
    public SynchronizationTable selectOneBySubjectCodeAndTableName(String subjectCode, String tableName) {
        SynchronizationTable synchronizationTables = mongoTemplate.findOne(new Query(Criteria.where("subjectCode").is(subjectCode).and("tableName").is(tableName)), SynchronizationTable.class);
        return synchronizationTables;
    }

    public List<SynchronizationTable> selectSynchronizeInfo(String loginId, String subjectCode) {
        List<SynchronizationTable> synchronizationTables = mongoTemplate.find(new Query(Criteria.where("loginId").is(loginId).and("subjectCode").is(subjectCode)), SynchronizationTable.class);
        return synchronizationTables;
    }

    public void updateByIdAndFrequency(String synchronizeId, String frequency) {
        long dataTime = Period.valueOf(frequency).getDataTime();
        Update update = Update.update("frequency", dataTime);
        SynchronizationTable id = mongoTemplate.findAndModify(new Query(Criteria.where("_id").is(synchronizeId)), update, SynchronizationTable.class);
    }
}
