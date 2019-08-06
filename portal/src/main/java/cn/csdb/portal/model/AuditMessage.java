package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 数据集审核过程信息
 *
 * @program: DataSync
 * @author: huangwei
 * @create: 2018-11-22 10:52
 **/
@Document(collection = "t_auditMessage")
public class AuditMessage {
    @Id
    private String id;
    @Field("resourceId")
    private String resourceId; // 数据集ID
    @Field("auditCom")
    private String auditCom; // 审核详情
    @Field("auditTime")
    private Date auditTime; // 审核时间
    @Field("auditPerson")
    private String auditPerson; // 审核人员

    public String getAuditPerson() {
        return auditPerson;
    }

    public void setAuditPerson(String auditPerson) {
        this.auditPerson = auditPerson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getAuditCom() {
        return auditCom;
    }

    public void setAuditCom(String auditCom) {
        this.auditCom = auditCom;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
}
