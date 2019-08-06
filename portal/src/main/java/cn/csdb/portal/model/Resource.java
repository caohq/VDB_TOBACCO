package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: DataSync
 * @description: resource class资源类
 * @author: xiajl
 * @create: 2018-10-23 13:13
 **/
@Document(collection = "t_resource")
public class Resource {
    @Id
    private String id;
    @Field("imagePath")
    private String imagePath; //图片地址
    @Field("status")
    private String status; //状态 删除 -2 未完成 -1 审核未通过 0 待审核 1 审核通过 2
    @Field("dataSourceId")
    private String dataSourceId; //数据源
    @Field("catalogId")
    private String catalogId; //分类ID
    @Field("resState")
    private String resState; //资源填写状态
    @Field("publicType")
    private String publicType; //发布类型(关系数据库/文件类型)
    @Field("publicContent")
    private String publicContent; //发布内容
    @Field("filePath")
    private String filePath; //文件路径
    @Field("fileName")
    private String fileName; //文件名称
    @Field("fieldComs")
    private String fieldComs; //字段注释
    @Field("subjectCode")
    private String subjectCode; //主题库代码
    @Field("auditTime")
    private Date auditTime; // 审核时间

    //以下是元数据信息
    @Field("pid")
    private String pid; //数据集标识
    @Field("title")
    private String title; //数据集名称
    @Field("introduction")
    private String introduction; //简介
    @Field("keyword")
    private String keyword; //资源关键词
    @Field("taxonomy")
    private String taxonomy; //分类
    @Field("dataFormat")
    private String dataFormat; //格式
    @Field("startTime")
    private Date startTime; //开始时间
    @Field("endTime")
    private Date endTime; //结束时间
    @Field("createdByOrganization")
    private String createdByOrganization; //创建机构
    @Field("createdBy")
    private String createdBy; //创建人员
    @Field("creationDate")
    private Date creationDate; //创建日期
    @Field("organizationName")
    private String organizationName; //机构
    @Field("email")
    private String email; //邮件
    @Field("phoneNum")
    private String phoneNum; //电话
    @Field("updateDate")
    private Date updateDate; //最新发布日期
    @Field("citation")
    private String citation; //引用格式
    @Field("toMemorySize")
    private String toMemorySize;  //总存储量
    @Field("toFilesMemorySize")
    private long toFilesMemorySize;// 文件存储量
    @Field("toFilesNumber")
    private long toFilesNumber;  //总文件数
    @Field("toRdbMemorySize")
    private long toRdbMemorySize; // 关系型数据存储量
    @Field("toRecordNumber")
    private long toRecordNumber; //关系型数据总条目数
    @Field("userGroupId")
    private String userGroupId;
    @Field("publishOrgnization")
    private String publishOrgnization;//发布机构
    @Field("createOrgnization")
    private String createOrgnization;
    @Field("createPerson")
    private String createPerson;
    @Field("creator_createTime")
    private Date creatorCreateTime;
    @Field("createTime")
    private Date createTime;
    @Field("vCount") //浏览次数
    private Integer vCount;
    @Field("dCount") //下载次数
    private Integer dCount;

    //用户自定义的扩展元数据项
    @Field("extMetadata")
    private List<Map<String, Object>> extMetadata;

    public String getPublishOrgnization() {
        return publishOrgnization;
    }

    public void setPublishOrgnization(String publishOrgnization) {
        this.publishOrgnization = publishOrgnization;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getResState() {
        return resState;
    }

    public void setResState(String resState) {
        this.resState = resState;
    }

    public String getPublicType() {
        return publicType;
    }

    public void setPublicType(String publicType) {
        this.publicType = publicType;
    }

    public String getPublicContent() {
        return publicContent;
    }

    public void setPublicContent(String publicContent) {
        this.publicContent = publicContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFieldComs() {
        return fieldComs;
    }

    public void setFieldComs(String fieldComs) {
        this.fieldComs = fieldComs;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getToMemorySize() {
        return toMemorySize;
    }

    public void setToMemorySize(String toMemorySize) {
        this.toMemorySize = toMemorySize;
    }

    public long getToFilesMemorySize() {
        return toFilesMemorySize;
    }

    public void setToFilesMemorySize(long toFilesMemorySize) {
        this.toFilesMemorySize = toFilesMemorySize;
    }

    public long getToRdbMemorySize() {
        return toRdbMemorySize;
    }

    public void setToRdbMemorySize(long toRdbMemorySize) {
        this.toRdbMemorySize = toRdbMemorySize;
    }

    public long getToFilesNumber() {
        return toFilesNumber;
    }

    public void setToFilesNumber(long toFilesNumber) {
        this.toFilesNumber = toFilesNumber;
    }

    public long getToRecordNumber() {
        return toRecordNumber;
    }

    public void setToRecordNumber(long toRecordNumber) {
        this.toRecordNumber = toRecordNumber;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Date getCreatorCreateTime() {
        return creatorCreateTime;
    }

    public void setCreatorCreateTime(Date creatorCreateTime) {
        this.creatorCreateTime = creatorCreateTime;
    }

    public Integer getvCount() {
        return vCount;
    }

    public void setvCount(Integer vCount) {
        this.vCount = vCount;
    }

    public Integer getdCount() {
        return dCount;
    }

    public void setdCount(Integer dCount) {
        this.dCount = dCount;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public List<Map<String, Object>> getExtMetadata() {
        return extMetadata;
    }

    public void setExtMetadata(List<Map<String, Object>> extMetadata) {
        this.extMetadata = extMetadata;
    }
}
