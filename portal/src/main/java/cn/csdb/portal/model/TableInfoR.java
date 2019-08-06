package cn.csdb.portal.model;

import java.util.List;

/**
 * @program: DataSync
 * @description:
 * @author: hw
 * @create: 2018-09-27 14:50
 **/
public class TableInfoR {
    private String tableName;
    private List<TableInfo> tableInfos;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<TableInfo> getTableInfos() {
        return tableInfos;
    }

    public void setTableInfos(List<TableInfo> tableInfos) {
        this.tableInfos = tableInfos;
    }
}
