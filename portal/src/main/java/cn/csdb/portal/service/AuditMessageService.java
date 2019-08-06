package cn.csdb.portal.service;

import cn.csdb.portal.model.AuditMessage;
import cn.csdb.portal.repository.AuditMessageDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-11-22 11:00
 **/
@Service
public class AuditMessageService {

    @Resource
    private AuditMessageDao auditMessageDao;

    /**
     *
     * Function Description:
     *
     * @param: [auditMessage]
     * @return: java.lang.String
     * @auther: hw
     * @date: 2018/11/22 11:12
     */
    public String save(AuditMessage auditMessage){
        return auditMessageDao.save(auditMessage);
    }

    /**
     *
     * Function Description:
     *
     * @param: [resourceId]
     * @return: java.util.List<cn.csdb.portal.model.AuditMessage>
     * @auther: hw
     * @date: 2018/11/22 11:12
     */
    public List<AuditMessage> getAuditMessageListByResourceId(String resourceId){
        return auditMessageDao.getAuditMessageListByResourceId(resourceId);
    }
}
