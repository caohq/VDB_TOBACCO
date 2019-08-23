package cn.csdb.portal.service;

import cn.csdb.portal.model.Node;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.repository.NodeDao;
import cn.csdb.portal.repository.SubjectDao;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NodeService {
    @Resource
    private NodeDao nodeDao;

    @Resource
    private SubjectDao subjectDao;

    /**
     * Function Description:
     */
    public long queryNodeCode(String nodeCode,String subjectCode) {
        return nodeDao.queryNodeCode(nodeCode,subjectCode);
    }

    public String getLastSerialNo(String subjectCode) {
        return nodeDao.getLastSerialNo(subjectCode);
    }

    public List<Node> selectAllThemesGallery(String themeName, String subjectCode,int pageNo, int pageSize){
        return nodeDao.selectAllNode(subjectCode,themeName,pageNo,pageSize);
    }
    public int countThemeList(String themeName, String subjectCode){
        return nodeDao.countNodeList(themeName,subjectCode);
    }

    public JSONObject addNode(Node node,String subjectCode){
        Subject subject=subjectDao.findBySubjectCode(subjectCode);
       return nodeDao.addNode(node,subject);
    }

    public Node findById(String id){
        return nodeDao.findById(id);
    }

    public JSONObject updateNodeById(Node node){
        return nodeDao.updateNodeById(node);
    }

    public List<Node> getAll(){
        return nodeDao.getAll();
    }

    public JSONObject deleteThemeById(String subjectCode,String id){
        Subject subject=subjectDao.findBySubjectCode(subjectCode);
       return nodeDao.deleteNodeById(id,subject);
    }
}
