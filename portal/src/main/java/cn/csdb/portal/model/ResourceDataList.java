package cn.csdb.portal.model;

/**
 * @Author jinbao
 * @Date 2019/4/26 19:06
 * @Description 数据集 绑定的实体数据
 **/
public class ResourceDataList {

    private String resourceId; // 数据集Id
    private String sqlDataList; // sql表名
    private String fileDataList;// 文件路径

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSqlDataList() {
        return sqlDataList;
    }

    public void setSqlDataList(String sqlDataList) {
        this.sqlDataList = sqlDataList;
    }

    public String getFileDataList() {
        return fileDataList;
    }

    public void setFileDataList(String fileDataList) {
        this.fileDataList = fileDataList;
    }
}
