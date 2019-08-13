package cn.csdb.portal.repository;

import cn.csdb.portal.controller.SubjectMgmtController;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.ThemesGallery;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class SubjectMgmtDao {
    private static int rowsPerPage = 10;
    private static final Logger logger = LogManager.getLogger(SubjectMgmtController.class);
    private JdbcTemplate jdbcTemplate;

    // create ftp directory
    @Value("#{systemPro['ftpServerAddr']}")
    private String ftpServerAddr;

    @Value("#{systemPro['ftpServerPort']}")
    private int ftpServerPort;

    @Value("#{systemPro['ftpRootPath']}")
    private String ftpRootPath;

    @Value("#{systemPro['dbUserName']}")
    private String dbUserName;

    @Value("#{systemPro['dbPassword']}")
    private String dbPassword;

    @Value("#{systemPro['dbHost']}")
    private String dbHost;

    @Value("#{systemPro['dbPort']}")
    private String dbPort;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Function Description:
     *
     * @param subject, the wrapped object which contains information of the subject ot be added
     * @return retValue, retValue inform user if create db info success or not
     */
    public int addSubject(Subject subject) {
        logger.info("enterring SubjectMgmtDao-addSubject");
        logger.info("ftpRootPath = " + ftpRootPath);
        String ftpFilePath = ftpRootPath + subject.getFtpUser() + "/";
        logger.info("ftpFilePath = " + ftpFilePath);
        subject.setFtpFilePath(ftpFilePath);
        subject.setDbName(subject.getSubjectCode());
        subject.setDbUserName(dbUserName);
        subject.setDbPassword(dbPassword);
        subject.setDbHost(dbHost);
        subject.setDbPort(dbPort);

        //create db and ftp
        createDb(subject.getSubjectCode());
        //createFtpUser(subject.getFtpUser(), subject.getFtpPassword());
        //createFtpPath(subject.getFtpUser(), subject.getFtpPassword());

        Runtime runtime = Runtime.getRuntime();
        try {
            System.out.println("-----------------------");
            String command1 = "chmod 777 /etc/vsftpd/vftpuseradd";
            runtime.exec(command1).waitFor();
            Process process = runtime.exec("/etc/vsftpd/vftpuseradd " + subject.getFtpUser() + " " + subject.getFtpPassword());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("create db, ftp user, ftp path completed!");

        //insert subject into mongodb

//        判断subjectCode是否重复
        List<Subject> list = mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subject.getSubjectCode())), Subject.class);
        int addedRowCnt = 0;
        if (list.size() == 0) {
//            subjectCode不重复，创建节点文件夹
            ThemesGallery themesGallery = mongoTemplate.findOne(new Query(Criteria.where("themeCode").is(subject.getThemeCode())), ThemesGallery.class);
//                      windows测试路径,部署时注释
//            String path = themesGallery.getFilePath() + "\\" + subject.getSubjectCode();

            String path = themesGallery.getFilePath() + "/" + subject.getSubjectCode();
            File f1 = new File(path);
            if (!f1.exists()) {
                f1.mkdirs();
            }
//            String path1=path+"\\db";
//            String path2=path+"\\file";
            String path1=path+"/db";
            String path2=path+"/file";
            File fileDB=new File(path1);
            File file=new File(path2);

            if (!file.exists() && !fileDB.exists()) {
                file.mkdirs();
                fileDB.mkdirs();
            }

            subject.setFilePath(path2);
            subject.setDbPath(path1);
            addedRowCnt = 1;
            mongoTemplate.insert(subject);
        }
        return addedRowCnt;
    }

    /**
     * Function Description:
     *
     * @param dbName, the db'name to be created
     * @return retValue, retValue inform user if create db info success or not
     */
    private boolean createDb(String dbName) {
        logger.info("enterring SubjectMgmtDao-createDb, dbName = " + dbName);

        boolean retValue = false;
        String createDB = " create database " + " if not exists " + dbName;
        try {
            jdbcTemplate.execute(createDB);
            logger.info("createDB success!");
            retValue = true;
        } catch (Exception e) {
            logger.info("createDB failed!");
            e.printStackTrace();
        }

        logger.info("createDB completed!");

        return retValue;
    }

    /**
     * Function Description:
     *
     * @param ftpUser
     * @param ftpPassword
     * @return retValue, retValue inform user if create ftp info success or not
     */
    private boolean createFtpUser(String ftpUser, String ftpPassword) {
        logger.info("create ftp user and password.");
        String insertSql = "insert into vu_list.vuser(name, password) values(?, ?)";
        Object[] args = new Object[]{ftpUser, ftpPassword};

        int insertedRows = 0;
        boolean retValue = false;

        try {
            insertedRows = jdbcTemplate.update(insertSql, args);
            if (insertedRows == 1) {
                logger.info("create ftp user and password success!");
                retValue = true;
            } else {
                logger.info("create ftp user and password failed!");
                retValue = false;
            }
        } catch (Exception e) {
            logger.info("create ftp user and password failed!");
            retValue = false;

            e.printStackTrace();
        }

        logger.info("create ftp user and password completed!");

        return retValue;
    }

    private boolean createFtpPath(String ftpUser, String ftpPassword) {
        logger.info("create ftp path");
        logger.info("ftpServerAddr = " + ftpServerAddr + ", ftpServerPort = " + ftpServerPort);
        boolean retValue = false;
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ftpServerAddr, ftpServerPort);
            ftpClient.login(ftpUser, ftpPassword);
            String ftpDirName = ftpUser;
            logger.info("create ftp path, ftpDirName = " + ftpDirName);
            boolean isCreated = ftpClient.makeDirectory(ftpDirName);
            if (isCreated) {
                retValue = true;
                logger.info("create ftp path success!");
            } else {
                retValue = false;
                logger.info("create ftp path failed!");
            }
        } catch (Exception e) {
            logger.info("failed to create ftp path!");
            e.printStackTrace();
        }

        logger.info("create ftp path completed!");

        return retValue;
    }

    /**
     * Function Description:
     *
     * @param id, the id of Subject to be deleted
     * @return deletedRowCnt, the count of rows deleted
     */
    public int deleteSubject(String id) {
        logger.info("Delete Subject: subject id to be deleted : " + id);

        //delete db and ftp info， and image
        Subject subject = findSubjectById(id);
        logger.info("Delete Subject: delete db info.");
        deleteDb(subject.getSubjectCode());
        logger.info("Delete Subject: db info completed.");
        logger.info("Delete Subject: delete ftp info.");
        deleteFtpUser(subject.getFtpUser(), subject.getFtpPassword());
        logger.info("Delete Subject: ftp info completed.");
        logger.info("Delete Subject: delete subject'image");
        deleteImage(subject.getImagePath());
        logger.info("Delete Subject: delete subject'image completed.");

        logger.info("Delete Subject: delete ftp directory");
        deleteFtpDir(subject.getFtpUser(), subject.getFtpPassword());
        logger.info("Delete Subject: delete ftp directory completed.");

        logger.info("Delete Subject: delete subject db record");
        int deletedRowCnt = 0;
//        删除节点的同时，删除文件夹
         ThemesGallery themesGallery=mongoTemplate.findOne(new Query(Criteria.where("themeCode").is(subject.getThemeCode())),ThemesGallery.class);
//         部署时注释
//         File file3=new File(themesGallery.getFilePath()+"\\"+subject.getSubjectCode());
        File file3=new File(themesGallery.getFilePath()+"/"+subject.getSubjectCode());
        if(file3.exists()){
            delFile(file3);
        }
        WriteResult wr = mongoTemplate.remove(subject, "t_subject");
        deletedRowCnt = wr.getN();
        logger.info("Delete Subject: delete subject db record completed.");

        return deletedRowCnt;
    }
/** 
* @Description: 删除文件夹
* @Param: [file] 
* @return: boolean 
* @Author: zcy
* @Date: 2019/8/9 
*/ 
    public boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    /**
     * Function Description:
     *
     * @param dbName, the name of database to be deleted
     */
    private void deleteDb(String dbName) {
        logger.info("Delete Db: deleting db info, dbName = " + dbName);
        String deleteDbSql = "drop database " + dbName;
        /*Object[] args = new Object[]{dbName};*/
        try {
            int deleteDbCnt = jdbcTemplate.update(deleteDbSql);
            if (deleteDbCnt == 1) {
                logger.info("Delete Db: elete db success!");
            } else {
                logger.info("Delete Db: delete db failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Delete Db: delete db failed!");
        }

        logger.info("Delete Db: delete db info completed!");
    }

    /**
     * Function Description:
     *
     * @param ftpUser,     ftp user name
     * @param ftpPassword, ftp password
     * @author zzl
     * @date 2018/10/23
     */
    private void deleteFtpUser(String ftpUser, String ftpPassword) {
        logger.info("deleting ftp info, ftpUser = " + ftpUser + ", ftpPassword = " + ftpPassword);
        String deleteFtpInfo = "delete from vu_list.vuser where name = ? and password=?";
        Object[] objs = new Object[]{ftpUser, ftpPassword};
        try {
            jdbcTemplate.update(deleteFtpInfo, objs);
            logger.info("delete ftp info success!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("delete ftp info failed!");
        }
        logger.info("delete ftp info completed!");
    }

    /**
     * Function Description:
     *
     * @param imagePath, the image' path which is about to be deleted
     */
    private void deleteImage(String imagePath) {
        logger.info("image path to be deleted : " + imagePath);
        try {
            File imagePathFileObj = new File(imagePath);
            imagePathFileObj.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("delete image completed!");
    }

    private void deleteFtpDir(String ftpUser, String ftpPassword) {
        logger.info("delete ftp path");
        logger.info("ftpServerAddr = " + ftpServerAddr + ", ftpServerPort = " + ftpServerPort);

        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpServerAddr, ftpServerPort);
            ftpClient.login(ftpUser, ftpPassword);
            String ftpDirName = ftpUser;
            logger.info("delete ftp path, ftpDirName = " + ftpDirName);
            boolean isDeleted = ftpClient.removeDirectory(ftpDirName);
            if (isDeleted) {
                logger.info("delete ftp path success!");
            } else {
                logger.info("delete ftp path failed!");
            }
        } catch (Exception e) {
            logger.info("delete ftp path failed!");
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        logger.info("delete ftp path completed!");
    }

    /**
     * Function Description:
     *
     * @param subject the subject to be updated
     * @return updatedRowCnt the count of rows updated
     * @author zzl
     * @date 2018/10/23
     */
    public int updateSubject(Subject subject) {
        logger.info("update subject");
        logger.info("subject to be updated : " + subject);

        int updatedRowCnt = 1;

        try {
            //reserverd, not userd.
            updateFtp(subject.getFtpUser(), subject.getFtpPassword());

            Query query = new Query();
            query.addCriteria(Criteria.where("subjectCode").is(subject.getSubjectCode()));

            Update update = Update.update("subjectName", subject.getSubjectName());
            update.set("brief", subject.getBrief());
            update.set("adminPasswd", subject.getAdminPasswd());
            update.set("contact", subject.getContact());
            update.set("phone", subject.getPhone());
            update.set("email", subject.getEmail());
            update.set("serialNo", subject.getSerialNo());
            update.set("imagePath", subject.getImagePath());
            update.set("themeCode", subject.getThemeCode());
            update.set("subjectURL",subject.getSubjectURL());
            mongoTemplate.upsert(query, update, "t_subject");

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("update subject completed!");

        return updatedRowCnt;
    }

    /**
     * Function Description: reserved, now not used
     *
     * @param newDbName
     * @author zzl
     * @date 2018/10/23
     */
    private void updateDb(String newDbName) {
    }

    /**
     * Function Description: reserved, now not used
     *
     * @param newFtpUser, newFtpPasswd
     * @author zzl
     * @date 2018/10/23
     */
    private void updateFtp(String newFtpUser, String newFtpPasswd) {
    }

    /**
     * Function Description:
     *
     * @param pageNumber
     * @return List<Subject>
     * @author zzl
     * @date 2018/10/23
     */
    public List<Subject> querySubject(String subjectNameFilter, int pageNumber) {
        //input parameter check， including lower bound and upper bound
        if (pageNumber < 1) {
            return null;
        }
        long totalPages = 1;
        totalPages = getTotalPages(subjectNameFilter);
        if (pageNumber > totalPages) {
            return null;
        }

        //query intended page
        int startRowNum = 0;
        startRowNum = (pageNumber - 1) * rowsPerPage;

        DBObject dbObject = null;
        if (!(subjectNameFilter.equals(""))) {
            dbObject = QueryBuilder.start().and("subjectName").regex(Pattern.compile("^.*" + subjectNameFilter + ".*$")).get();
        } else {
            dbObject = QueryBuilder.start().get();
        }
        Query query = new BasicQuery(dbObject).skip(startRowNum).limit(rowsPerPage); // paging

        Sort.Direction direction = false ? Sort.Direction.ASC : Sort.Direction.DESC; // sort by _id desc, that is , reverse order by insert time
        query.with(new Sort(direction, "_id"));

        List<Subject> subjectsOfThisPage = mongoTemplate.find(query, Subject.class);
        logger.info(subjectsOfThisPage);

        return subjectsOfThisPage;
    }

    /**
     * Function Description:
     *
     * @return totalPages
     */
    public long getTotalPages(String subjectNameFilter) {
        //query the count of all documents in t_subject collection
        DBObject dbObject = QueryBuilder.start().and("subjectName").regex(Pattern.compile("^.*" + subjectNameFilter + ".*$")).get();
        Query query = new BasicQuery(dbObject);
        long totalRows = mongoTemplate.count(query, "t_subject");
        long totalPages = 0;
        totalPages = totalRows / rowsPerPage + (totalRows % rowsPerPage == 0 ? 0 : 1);

        return totalPages;
    }

    public long getTotalSubject(String subjectNameFilter) {
        DBObject dbObject = QueryBuilder.start().and("subjectName").regex(Pattern.compile("^.*" + subjectNameFilter + ".*$")).get();
        Query query = new BasicQuery(dbObject);
        long totalRows = mongoTemplate.count(query, "t_subject");

        return totalRows;
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
        DBObject dbObject = QueryBuilder.start().and("id").is(id).get();
        Query query = new BasicQuery(dbObject);
        List<Subject> subjects = mongoTemplate.find(query, Subject.class);
        logger.info(subjects);

        return subjects.get(0);
    }

    /**
     *
     */
    public long querySubjectCode(String subjectCode) {
        DBObject dbObject = QueryBuilder.start().and("subjectCode").is(subjectCode).get();
        Query query = new BasicQuery(dbObject);
        long cntOfTheCode = mongoTemplate.count(query, "t_subject");

        return cntOfTheCode;
    }

    public long queryAdmin(String admin) {
        DBObject dbObject = QueryBuilder.start().and("loginId").is(admin).get();
        Query query = new BasicQuery(dbObject);
        //long cntOfAdminInSubject = mongoTemplate.count(query, "t_subject");
        long cntOfAdminInUser = mongoTemplate.count(query, "t_user");

        return cntOfAdminInUser;
    }

    /**
     * Function Description:
     *
     * @param: [code]
     * @return: cn.csdb.portal.model.Subject
     * @auther: hw
     * @date: 2018/10/22 15:03
     */
    public Subject findByCode(String code) {
        DBObject query = QueryBuilder.start().and("subjectCode").is(code).get();
        BasicQuery basicQuery = new BasicQuery(query);
        List<Subject> subjects = mongoTemplate.find(basicQuery, Subject.class);
        if (subjects.size() > 0) {
            return subjects.get(0);
        } else
            return null;
    }


    public List<Subject> getSubjectCodeList() {
        return mongoTemplate.findAll(Subject.class);
    }

    /**
     * Function Description : this function is designed to get default value for serialNo field in addSubjectDialog
     *
     * @return retValue, next serial no after last inserted record, the last inserted record's serialNo plus 1,
     */
    public String getLastSerialNo() {
        DBObject dbObject = QueryBuilder.start().get();
        BasicQuery query = new BasicQuery(dbObject);
        Sort.Direction direction = false ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(new Sort(direction, "_id"));

        List<Subject> subjects = mongoTemplate.find(query, Subject.class);

        if (subjects.size() == 0) {
            return "0";
        }

        return subjects.get(0).getSerialNo();
    }
}
