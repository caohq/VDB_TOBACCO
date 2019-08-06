package cn.csdb.portal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "t_user")
public class User {
    @Id
    private String id;
    @Field("userName")
    private String userName;
    @Field("loginId")
    private String loginId;
    @Field("password")
    private String password;
    @Field("subjectCode")
    private String subjectCode;
    @Field("createTime")
    private String createTime;
    @Field("role")
    private String role;
    @Field("stat")
    private int stat;  //0：数据有效，1：数据无效
    @Field("groups")
    private String groups;
    @Field("address")
    private String address;
    @Field("createType")
    private String createType;//默认dataExplore

    @Field("userRealName")
    private String userRealName; // 用户真实姓名
    @Field("idCard")
    private String idCard;//证件号
    @Field("sex")
    private String sex;
    @Field("birthday")
    private Date birthday;
    @Field("phoneNo")
    private String phoneNo;
    @Field("email")
    private String email;
    @Field("workName")
    private String workName; //单位名称
    @Field("workAddress")
    private String workAddress; //单位地址
    @Field("laboratory")
    private String laboratory;   //研究室
    @Field("laboratoryDirec")
    private String laboratoryDirec; //研究方向

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getLaboratoryDirec() {
        return laboratoryDirec;
    }

    public void setLaboratoryDirec(String laboratoryDirec) {
        this.laboratoryDirec = laboratoryDirec;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", createTime='" + createTime + '\'' +
                ", role='" + role + '\'' +
                ", stat=" + stat +
                ", groups='" + groups + '\'' +
                ", address='" + address + '\'' +
                ", createType='" + createType + '\'' +
                ", idCard='" + idCard + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", workName='" + workName + '\'' +
                ", workAddress='" + workAddress + '\'' +
                ", laboratory='" + laboratory + '\'' +
                ", laboratoryDirec='" + laboratoryDirec + '\'' +
                '}';
    }
}
