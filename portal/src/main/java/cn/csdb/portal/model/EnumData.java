package cn.csdb.portal.model;

/**
 * Created by shiba on 2019/4/24.
 */
public class EnumData {
    private String key; //枚举，关联数据表
    private String value; //枚举，关联字段

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
