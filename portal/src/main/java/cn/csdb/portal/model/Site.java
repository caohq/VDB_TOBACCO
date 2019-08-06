package cn.csdb.portal.model;

/**
 * @program: DataSync
 * @description:
 * @author: huangwei
 * @create: 2018-10-15 14:38
 **/
public class Site {
    private int id;
    private String siteMarker;
    private String filePath;
    private String dbName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiteMarker() {
        return siteMarker;
    }

    public void setSiteMarker(String siteMarker) {
        this.siteMarker = siteMarker;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
