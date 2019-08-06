package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by shiba on 2019/4/23.
 */
@Document(collection = "sdo_relation_disable")
public class SdoRelationDisable {
    @Id
    private String id;
    @Field("sdo_id")
    private String sdoId;
    @Field("sdo_name")
    private String sdoName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSdoId() {
        return sdoId;
    }

    public void setSdoId(String sdoId) {
        this.sdoId = sdoId;
    }

    public String getSdoName() {
        return sdoName;
    }

    public void setSdoName(String sdoName) {
        this.sdoName = sdoName;
    }
}
