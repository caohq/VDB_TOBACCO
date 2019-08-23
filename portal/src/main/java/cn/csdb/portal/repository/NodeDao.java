package cn.csdb.portal.repository;

import cn.csdb.portal.model.Node;
import cn.csdb.portal.model.Subject;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Repository
public class NodeDao {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private CreatedTablesByExcelDao createdTablesByExcelDao;

    public long queryNodeCode(String nodeCode, String subjectCode) {
        long cntOfTheCode = mongoTemplate.count(new Query(Criteria.where("subjectCode").is(subjectCode).and("nodeCode").is(nodeCode)), "t_node");
        return cntOfTheCode;
    }

    /**
     * @Description: 主题库列表--烟草
     * @Param: []
     * @return: java.util.List<cn.csdb.portal.model.ThemesGallery>
     * @Author: zcy
     * @Date: 2019/8/7
     */
    public List<Node> selectAllNode(String subjectCode, String nodeName, int pageNo, int pageSize) {
        QueryBuilder queryBuilder = new QueryBuilder();
        if (StringUtils.isNotEmpty(nodeName)) {
            queryBuilder = queryBuilder.and("nodeName").regex(Pattern.compile("^.*" + nodeName + ".*$"));
        }

        DBObject dbObject = queryBuilder.and("subjectCode").is(subjectCode).get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        Sort.Order so = new Sort.Order(Sort.Direction.DESC, "serialNo");
        List<Sort.Order> sos = new ArrayList<>();
        sos.add(so);
        basicQuery.with(new Sort(sos));
        basicQuery.skip((pageNo - 1) * pageSize);
        basicQuery.limit(pageSize);
        return mongoTemplate.find(basicQuery, Node.class);

    }

    public int countNodeList(String nodeName, String subjectCode) {
        QueryBuilder queryBuilder = new QueryBuilder();
        if (StringUtils.isNotEmpty(nodeName)) {
            queryBuilder = queryBuilder.and("nodeName").regex(Pattern.compile("^.*" + nodeName + ".*$"));
        }
        DBObject dbObject = queryBuilder.and("subjectCode").is(subjectCode).get();
        BasicQuery basicQuery = new BasicQuery(dbObject);
        List<Node> list = mongoTemplate.find(basicQuery, Node.class);
        return list.size();
    }

    public String getLastSerialNo(String subjectCode) {
        DBObject dbObject = QueryBuilder.start().get();
        BasicQuery query = new BasicQuery(dbObject);
        Sort.Direction direction = false ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(new Sort(direction, "_id"));
        query.addCriteria(Criteria.where("subjectCode").is(subjectCode));
        List<Node> nodes = mongoTemplate.find(query, Node.class);

        if (nodes.size() == 0) {
            return "0";
        }

        return nodes.get(0).getSerialNo();
    }

    /**
     * @Description: 新增主题库
     * @Param: [themesGallery]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/8/7
     */
    public JSONObject addNode(Node node, Subject subject) {
//        判断nodeCode是否重复
        List<Node> list = mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subject.getSubjectCode()).and("nodeCode").is(node.getNodeCode())), Node.class);
        JSONObject jsonObject = new JSONObject();
        if (list.size() == 0) {
//            windows测试路径,部署时注释
            String path = subject.getFilePath() + "\\" + node.getNodeCode();
//            linux下路径
//            String path=subject.getFilePath()+"/"+node.getSubjectCode();

//            创建主题库文件夹，/home/themecode
            File f1 = new File(path);
            if (!f1.exists()) {
                f1.mkdirs();
            }
//            windows测试路径,部署时注释
            String path2 = path + "\\file";
            String path3 = path + "\\db";
//             linux下路径
//            String path2=path+"/file";
//            String path3=path+"/db";

            File file = new File(path2);
            File fileDB = new File(path3);
            if (!file.exists() && !fileDB.exists()) {
                file.mkdirs();
                fileDB.mkdirs();
            }

            node.setSubjectCode(subject.getSubjectCode());
            node.setFilePath(path2);
            node.setDbPath(path3);

            mongoTemplate.insert(node);
            jsonObject.put("result", "ok");
        } else {
            jsonObject.put("result", "exist");
        }
        return jsonObject;
    }

    /**
     * @Description: 根据id查找主题库
     * @Param: [id]
     * @return: cn.csdb.portal.model.ThemesGallery
     * @Author: zcy
     * @Date: 2019/8/7
     */
    public Node findById(String id) {
        return mongoTemplate.findById(id, Node.class);
    }

    /**
     * @Description: 根据id修改主题库信息
     * @Param: [themesGallery]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/8/8
     */
    public JSONObject updateNodeById(Node node) {
        JSONObject jsonObject = new JSONObject();
//        判断themeCode是否重复
//        List<Node> list = mongoTemplate.find(new Query(Criteria.where("id").is(node.getId())), Node.class);
//        if (list.size() == 1) {
            Update update = Update.update("id", node.getId()).set("nodeName", node.getNodeName())
                    .set("nodeURL", node.getNodeURL());
            mongoTemplate.updateFirst(new Query(Criteria.where("id").is(node.getId())), update, Node.class);
            jsonObject.put("result", "ok");
//        } else {
//            jsonObject.put("result", "exist");
//        }

        return jsonObject;
    }

    public List<Node> getAll() {
        return mongoTemplate.findAll(Node.class);
    }

    /**
     * @Description: 根据id删除主题库
     * @Param: [id]
     * @return: com.alibaba.fastjson.JSONObject
     * @Author: zcy
     * @Date: 2019/8/8
     */
    public JSONObject deleteNodeById(String id,Subject subject) {
        JSONObject jsonObject = new JSONObject();
//        删除节点的同时，删除文件夹
        Node node = mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), Node.class);
//         部署时注释
         File file3=new File(subject.getFilePath()+"\\"+node.getNodeCode());
//        File file3=new File(subject.getFilePath()+"/"+node.getNodeCode());
        if(file3.exists()){
            delFile(file3);
        }
//删除节点记录
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)), Node.class);
//        删除该节点下根据excel文件创建表的记录
        createdTablesByExcelDao.deleteCreatedTablesByCode(subject.getSubjectCode(),node.getNodeCode());
            jsonObject.put("result", "ok");
        return jsonObject;
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

    public Node findByNodeCode(String nodeCode) {
        return mongoTemplate.findOne(new Query(Criteria.where("nodeCode").is(nodeCode)), Node.class);
    }

    public List findBySubjectCode(String subjectCode) {
        return mongoTemplate.find(new Query(Criteria.where("subjectCode").is(subjectCode)), Node.class);
    }

}
