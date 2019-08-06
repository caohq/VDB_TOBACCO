package cn.csdb.portal.service;

import cn.csdb.portal.repository.SynchronizationTablesDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SynchronizationTablesService {
    @Resource
    private SynchronizationTablesDao synchronizationTablesDao;

    /**
     * @Description: 判断该表是否是同步表
     * @Param: [tableName, subjectCode]
     * @return: int
     * @Author: zcy
     * @Date: 2019/5/13
     */
    public int isSynchronizationTable(String tableName, String subjectCode) {
        if (synchronizationTablesDao.selectOneBySubjectCodeAndTableName(subjectCode, tableName) != null) {
            return 1;
        }
        return 0;
    }

}
