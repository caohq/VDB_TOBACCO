package cn.csdb.portal.model;

import java.util.Date;

/**
 * Created by pirate on 2018/10/22.
 */
public class ResCatalog {
    private int id;
    private int parentid;
    private String name;
    private int level;
    private int nodeorder;
    private Date updatetime;
    private Date createtime;

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNodeorder() {
        return nodeorder;
    }

    public void setNodeorder(int nodeorder) {
        this.nodeorder = nodeorder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}