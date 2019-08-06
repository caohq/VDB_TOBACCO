package cn.csdb.portal.service;

import cn.csdb.portal.model.DataTask;
import cn.csdb.portal.repository.DataTaskDao;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Connection;
import java.util.List;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-10-15 10:15
 **/

@Service
public class DataTaskService {
    @Resource
    private DataTaskDao dataTaskDao;

    private Logger logger = LoggerFactory.getLogger(DataTaskService.class);

    @Transactional(readOnly = true)
    public DataTask get(int dataTaskId){
        return dataTaskDao.get(dataTaskId);
    }

    public void insertDataTask(final DataTask dataTask){
         dataTaskDao.insertDataTask(dataTask);
    }

}
