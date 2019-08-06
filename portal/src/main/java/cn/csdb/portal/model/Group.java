package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * 组织表
 *
 * @author: xiajl
 * @create: 2018-10-30 10:11
 **/
@Document(collection = "t_group")
public class Group {
    @Id
    private String id;
    @Field("groupName")
    private String groupName; // 组织名称
    @Field("desc")
    private String desc; // 组织信息描述
    @Field("createTime")
    private Date createTime; // 创建时间
    @Field("users")
    private List<String> users; // 包含的用户

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
