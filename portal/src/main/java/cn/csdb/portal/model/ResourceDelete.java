package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by shiba on 2019/3/15.
 */
@Document(collection = "t_resource_delete")
public class ResourceDelete {
    @Id
    private String id;
    @Field("resourceId")
    private String resourceId; //删除的数据集id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
