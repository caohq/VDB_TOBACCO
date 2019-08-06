package cn.csdb.portal.model;

public class DataComposeDemo {
  private  String colName;
  private  String columnComment;
  private  String autoAdd;
  private  String pkColumn;
  private  String dataType;
  private String columnType;
  private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getAutoAdd() {
        return autoAdd;
    }

    public void setAutoAdd(String autoAdd) {
        this.autoAdd = autoAdd;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
