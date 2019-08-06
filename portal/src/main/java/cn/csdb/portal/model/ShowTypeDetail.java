package cn.csdb.portal.model;

import java.util.List;

/**
 * Created by shiba on 2019/4/24.
 */
public class ShowTypeDetail {
    private int status; //最外层保存使用，确定--删除相关列的status等于1的数据，取消，删除相关列status=2的数据
    private int type;  //字段显示类型,0--文本类型，1--URL，2--字典枚举，3---关联数据表，4---文件类型
    private String columnName; //枚举暂用（sql）
    private String optionMode;  //选项模式，
    private String relationTable; //枚举使用（sql）
    private String relationColumnK; //枚举使用列1（sql）
    private String relationColumnV; //字典枚举，列2
    private String address;//缺省值，主路径
    private String Separator;//分隔符
    private List<EnumData> enumData; //枚举类型对应关系，关联表使用（表名、列名）

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOptionMode() {
        return optionMode;
    }

    public void setOptionMode(String optionMode) {
        this.optionMode = optionMode;
    }

    public String getRelationTable() {
        return relationTable;
    }

    public void setRelationTable(String relationTable) {
        this.relationTable = relationTable;
    }

    public String getRelationColumnK() {
        return relationColumnK;
    }

    public void setRelationColumnK(String relationColumnK) {
        this.relationColumnK = relationColumnK;
    }

    public String getRelationColumnV() {
        return relationColumnV;
    }

    public void setRelationColumnV(String relationColumnV) {
        this.relationColumnV = relationColumnV;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeparator() {
        return Separator;
    }

    public void setSeparator(String separator) {
        Separator = separator;
    }

    public List<EnumData> getEnumData() {
        return enumData;
    }

    public void setEnumData(List<EnumData> enumData) {
        this.enumData = enumData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
