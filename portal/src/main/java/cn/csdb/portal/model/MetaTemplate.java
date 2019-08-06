package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 元数据模板 xiajl20190423
 */
@Document(collection = "t_meta_template")
public class MetaTemplate {
    @Id
    private String id;
    @Field("name")
    private String metaTemplateName; //元数据模板名称
    @Field("metaTemplateCreateDate")
    private Date metaTemplateCreateDate; //元数据模板创建时间
    @Field("metaTemplateCreator")
    private String metaTemplateCreator; //元数据模板创建者
    @Field("memo")
    private String memo; //元数据模板备注

    @Field("subjectCode")
    private String subjectCode;

    @Field("title")
    private String title; //数据集名称
    @Field("introduction")
    private String introduction; //简介
    @Field("imagePath")
    private String imagePath; //图片地址
    @Field("keyword")
    private String keyword; //资源关键词
    @Field("catalogId")
    private String catalogId; //分类ID
    @Field("startTime")
    private Date startTime; //开始时间
    @Field("endTime")
    private Date endTime; //结束时间
    @Field("createdByOrganization")
    private String createdByOrganization; //版权声明
    @Field("createOrgnization")
    private String createOrgnization; //创建者机构
    @Field("createPerson")
    private String createPerson; //创建人员
    @Field("creator_createTime")
    private Date creatorCreateTime; //元数据中的创建日期
    @Field("publishOrgnization")
    private String publishOrgnization; //发布机构
    @Field("email")
    private String email; //发布者邮箱号
    @Field("phoneNum")
    private String phoneNum; //发布者电话电话
    @Field("extMetadata")
    private List<Map<String, Object>> extMetadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getMetaTemplateCreateDate() {
        return metaTemplateCreateDate;
    }

    public void setMetaTemplateCreateDate(Date metaTemplateCreateDate) {
        this.metaTemplateCreateDate = metaTemplateCreateDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreatedByOrganization() {
        return createdByOrganization;
    }

    public void setCreatedByOrganization(String createdByOrganization) {
        this.createdByOrganization = createdByOrganization;
    }

    public String getCreateOrgnization() {
        return createOrgnization;
    }

    public void setCreateOrgnization(String createOrgnization) {
        this.createOrgnization = createOrgnization;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreatorCreateTime() {
        return creatorCreateTime;
    }

    public void setCreatorCreateTime(Date creatorCreateTime) {
        this.creatorCreateTime = creatorCreateTime;
    }

    public String getPublishOrgnization() {
        return publishOrgnization;
    }

    public void setPublishOrgnization(String publishOrgnization) {
        this.publishOrgnization = publishOrgnization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public List<Map<String, Object>> getExtMetadata() {
        return extMetadata;
    }

    public void setExtMetadata(List<Map<String, Object>> extMetadata) {
        this.extMetadata = extMetadata;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getMetaTemplateName() {
        return metaTemplateName;
    }

    public void setMetaTemplateName(String metaTemplateName) {
        this.metaTemplateName = metaTemplateName;
    }

    public String getMetaTemplateCreator() {
        return metaTemplateCreator;
    }

    public void setMetaTemplateCreator(String metaTemplateCreator) {
        this.metaTemplateCreator = metaTemplateCreator;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}

