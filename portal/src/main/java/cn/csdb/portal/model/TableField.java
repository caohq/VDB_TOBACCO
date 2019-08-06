package cn.csdb.portal.model;

import java.util.List;

/**
 * @ClassName TableField
 * @Description 创建表属性
 * @Author jinbao
 * @Date 2019/1/4 15:07
 * @Version 1.0
 **/
public class TableField {


    private String oldField;
    private String oldComment;
    private String field;
    private String comment;
    private String type;
    private String length;
    private String pk;
    private String insertNum;


    public String getOldField() {
        return oldField;
    }

    public void setOldField(String oldField) {
        this.oldField = oldField;
    }

    public String getOldComment() {
        return oldComment;
    }

    public void setOldComment(String oldComment) {
        this.oldComment = oldComment;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getInsertNum() {
        return insertNum;
    }

    public void setInsertNum(String insertNum) {
        this.insertNum = insertNum;
    }
}
