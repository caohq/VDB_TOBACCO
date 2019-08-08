package cn.csdb.portal.service;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.User;
import cn.csdb.portal.repository.SubjectMgmtDao;
import cn.csdb.portal.repository.UserDao;
import cn.csdb.portal.utils.MD5Util;
import cn.csdb.portal.utils.dataSrc.DruidUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SubjectMgmtService {
    @Resource
    private SubjectMgmtDao subjectMgmtDao;
    @Resource
    private UserDao userDao;

    /*@Autowired
    public void setSubjectMgmtDao(SubjectMgmtDao subjectMgmtDao)
    {
        this.subjectMgmtDao = subjectMgmtDao;
    }*/

    /**
     * Function Description:
     *
     * @param subject, the wrapped object which contains information of the subject ot be added
     * @return addSubjectNotice, a notice inform user if create subject success or not
     */
    @Transactional
    public String addSubject(Subject subject) {
        String addSubjectNotice = "";

        //添加 数据节点理员 账号
        String admin = subject.getAdmin();
        String adminPasswd = subject.getAdminPasswd();
        User subjectUser = new User();
        subjectUser.setUserName(admin);
        subjectUser.setLoginId(admin);
        subjectUser.setPassword(MD5Util.encryptPassword(admin, adminPasswd, "cnic.cn"));
        subjectUser.setSubjectCode(subject.getSubjectCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        subjectUser.setCreateTime(sdf.format(new Date()));
        subjectUser.setRole("数据节点管理员");
        subjectUser.setStat(0); //0：数据有效，1：数据无效
        subjectUser.setGroups("");
        userDao.addUser(subjectUser);

        // 添加 数据节点
        int addedRowCnt = subjectMgmtDao.addSubject(subject);
        if (addedRowCnt == 1) {
            addSubjectNotice = "1";
        } else {
            addSubjectNotice = "0";
        }
        return addSubjectNotice;
    }


    /**
     * Function Description:
     *
     * @param id, the id of Subject to be deleted
     * @return deleteSubjectNotice, a notice inform user the deletion result
     * @author zzl
     * @date 2018/10/23
     */
    @Transactional
    public int deleteSubject(String id) {
        String deleteSubjectNotice = "";
        Subject subject = subjectMgmtDao.findSubjectById(id);
        //删除对应的数据库连接池
        DruidUtil druidUtil = DruidUtil.getInstance();
        if(druidUtil.containsId(subject.getDbHost()+subject.getDbPort()+subject.getDbName())){
            druidUtil.removeDruidDataSource(subject);
        }
        String admin = subject.getAdmin();
        userDao.deleteUserByLoginId(admin);

        int deletedRowCnt = subjectMgmtDao.deleteSubject(id);

        return deletedRowCnt;
    }

    /**
     * Function Description:
     *
     * @param subject the subject to be updated
     * @return updatedSubjectNotice a notice which inform user update result.
     * @author zzl
     * @date 2018/10/23
     */
    @Transactional
    public String updateSubject(Subject subject) {
        String updatedSubjectNotice = "";

        String admin = subject.getAdmin();
        String adminPasswd = subject.getAdminPasswd();
        User subjectUser = new User();
        subjectUser.setUserName(admin);
        subjectUser.setLoginId(admin);
        subjectUser.setPassword(adminPasswd);
        subjectUser.setSubjectCode(subject.getSubjectCode());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        subjectUser.setCreateTime(sdf.format(new Date()));
        subjectUser.setRole("数据节点管理员");
        subjectUser.setStat(0); //0：数据有效，1：数据无效
        subjectUser.setGroups("");
        userDao.updateUser(subjectUser);

        int modifiedRowCnt = subjectMgmtDao.updateSubject(subject);
        if (modifiedRowCnt == 1) {
            updatedSubjectNotice = "1";
        } else {
            updatedSubjectNotice = "0";
        }

        return updatedSubjectNotice;
    }

    /**
     * Function Description:
     *
     * @param requestedPage
     * @return List<Subject>
     * @author zzl
     * @date 2018/10/23
     */
    public List<Subject> querySubject(String subjectNameFilter, int requestedPage) {
        return subjectMgmtDao.querySubject(subjectNameFilter, requestedPage);
    }

    /**
     * Function Description:
     *
     * @return totalPages
     * @author zzl
     * @date 2018/10/23
     */
    public long getTotalPages(String subjectNameFilter) {
        return subjectMgmtDao.getTotalPages(subjectNameFilter);
    }

    /**
     * Function Description:
     *
     * @param id
     * @return Subject
     * @author zzl
     * @date 2018/10/23
     */
    public Subject findSubjectById(String id) {
        return subjectMgmtDao.findSubjectById(id);
    }

    /**
     * Function Description:
     *
     * @param: [code]
     * @return: cn.csdb.portal.model.Subject
     * @auther: hw
     * @date: 2018/10/22 15:13
     */
    public Subject findByCode(String code) {
        return subjectMgmtDao.findByCode(code);
    }


    /**
     * Function Description:
     */
    public long querySubjectCode(String subjectCode) {
        return subjectMgmtDao.querySubjectCode(subjectCode);
    }


    public long queryAdmin(String userName) {
        return subjectMgmtDao.queryAdmin(userName);
    }

    public long getTotalSubject(String subjectNameFilter) {
        return subjectMgmtDao.getTotalSubject(subjectNameFilter);
    }

    public List<Subject> getSubjectCodeList() {
        return subjectMgmtDao.getSubjectCodeList();
    }

    public String getLastSerialNo() {
        return subjectMgmtDao.getLastSerialNo();
    }
}
