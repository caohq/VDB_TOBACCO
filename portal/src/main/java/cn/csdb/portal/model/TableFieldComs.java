package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
@Document(collection = "t_table_field_coms")
public class TableFieldComs {
    @Id
    private String objectId;
    @Field("id")
    private int id;
    @Field("uriEx")
    private String uriEx; //数据库访问地址+数据库+表明
    @Field("uriHash")
    private int uriHash;//访问地址hash，方便查找匹配
    @Field("fieldComs")
    private String fieldComs; //注解
    @Field("createTime")
    private Date createTime;
    @Field("updateTime")
    private Date updateTime;
    @Field("status")
    private int status; //状态 0有效

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUriEx() {
        return uriEx;
    }

    public void setUriEx(String uriEx) {
        this.uriEx = uriEx;
    }

    public int getUriHash() {
        return uriHash;
    }

    public void setUriHash(int uriHash) {
        this.uriHash = uriHash;
    }

    public String getFieldComs() {
        return fieldComs;
    }

    public void setFieldComs(String fieldComs) {
        this.fieldComs = fieldComs;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
