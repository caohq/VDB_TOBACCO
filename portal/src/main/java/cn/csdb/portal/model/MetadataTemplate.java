package cn.csdb.portal.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * 元数据信息
 *
 * @program: DataSync
 * @author: xiajl
 * @create: 2019-03-08 13:58
 **/
@Document(collection = "metadata_template")
public class MetadataTemplate {
    @Id
    private String id;
    @Field("extField")
    private String extField; // 元数据字段英文名称
    @Field("extFieldName")
    private String extFieldName; // 元数据字段中文名称
    @Field("type")
    private String type; // 元数据类型
    @Field("sortOrder")
    private int sortOrder; // 元数据序列
    @Field("isMust")
    private int isMust; // 是否必须
    @Field("enumdata")
    private String enumdata; // 枚举数据
    @Field("remark")
    private String remark; // 元数据注释
    private List<String> enumdataList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtField() {
        return extField;
    }

    public void setExtField(String extField) {
        this.extField = extField;
    }

    public String getExtFieldName() {
        return extFieldName;
    }

    public void setExtFieldName(String extFieldName) {
        this.extFieldName = extFieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getIsMust() {
        return isMust;
    }

    public void setIsMust(int isMust) {
        this.isMust = isMust;
    }

    public String getEnumdata() {
        return enumdata;
    }

    public void setEnumdata(String enumdata) {
        this.enumdata = enumdata;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getEnumdataList() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(enumdata)){
            String[] data = enumdata.split(",");
            list = Lists.newArrayList(data);
        }
         return list;
    }

}
