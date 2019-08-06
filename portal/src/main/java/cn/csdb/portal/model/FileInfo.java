package cn.csdb.portal.model;

/**
 * 关联数据集的文件数据信息
 * Created by shiba on 2019/2/13.
 */

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "t_fileInfo")
public class FileInfo {
    @Id
    private String id;
    @Field("file_name")
    private String file_name; // 文件名称
    @Field("file_path")
    private String file_path; // 文件绝对路径
    @Field("size")
    private String size; // 文件大小
    @Field("resourceId")
    private String resourceId; // 关联所属的数据集ID
    @Field("time")
    private Date time; // 文件与数据集发生关联关系的时间
    @Field("previewType")
    private String previewType; // 预览格式
    @Field("fileStatus")
    private String fileStatus; // 0:删除 1:在用

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPreviewType() {
        return previewType;
    }

    public void setPreviewType(String previewType) {
        this.previewType = previewType;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }
}
