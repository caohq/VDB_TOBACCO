package cn.csdb.portal.model;

/**
 * @program: DataSync
 * @description:
 * @author: hw
 * @create: 2018-09-27 14:50
 **/
public class TableInfo {
    private String columnName;
    private String columnNameLabel;
    private String dataType;
    private String columnComment;


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameLabel() {
        return columnNameLabel;
    }

    public void setColumnNameLabel(String columnNameLabel) {
        this.columnNameLabel = columnNameLabel;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableInfo tableInfo = (TableInfo) o;
        return columnName != null ? columnName.equals(tableInfo.columnName) : tableInfo.columnName == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (columnNameLabel != null ? columnNameLabel.hashCode() : 0);
        return result;
    }
}
