package cn.csdb.portal.service;

import cn.csdb.portal.model.ResCatalog_Mongo;
import cn.csdb.portal.repository.ResCatalogDao;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shibaoping on 2018/10/22.
 */
@Service
public class ResCatalogService {
    @Resource
    private ResCatalogDao resCatalogDao;

    @Transactional
    public int savaLocalResCatalog(ResCatalog_Mongo resCatalog) {
        return resCatalogDao.insertLocalResCatalog(resCatalog);
    }

    @Transactional
    public int updateLocalResCatalog(ResCatalog_Mongo resCatalog) {
        int i = resCatalogDao.updateLocalResCatalog(resCatalog);
        if (i > 0) {
            return resCatalog.getRid();
        } else {
            return -1;
        }
    }

    @Transactional
    public List<ResCatalog_Mongo> getLocalResCatalogAllRoot() {
        return resCatalogDao.getLocalResCatalogAllRoot();
    }

    public JSONObject getLocalResCatalogTree(boolean editable, String catalogType) {
        JSONObject jsonObject = new JSONObject();
//        core
        JSONObject coreJson = new JSONObject();
        coreJson.put("animation", 0);
        if (editable == true) {
            coreJson.put("check_callback", true);
        } else {
            coreJson.put("check_callback", false);
        }
        JSONObject themeJson = new JSONObject();
        themeJson.put("dots", false);
        themeJson.put("icons", true);
        themeJson.put("stripes", false);
        coreJson.put("theme", themeJson);
        ResCatalog_Mongo rootNode = resCatalogDao.getLocalRootNode();
        JSONObject dataJson = treeNodePackage(rootNode.getRid(), 4, catalogType, rootNode.getObjectId());
        coreJson.put("data", dataJson);
        jsonObject.put("core", coreJson);
//        type
        JSONObject typeJson = new JSONObject();
        JSONObject weilJson = new JSONObject();
        weilJson.put("max_children", 1);
        //设置目录的深度
        weilJson.put("max_depth", 4);
        List valid_children1 = Arrays.asList("root");
        weilJson.put("valid_children", valid_children1);
        typeJson.put("#", weilJson);
        JSONObject rootJson = new JSONObject();
        rootJson.put("icon", "glyphicon glyphicon-th-list");
        List<String> valid_children2 = Arrays.asList("default");
        rootJson.put("valid_children", valid_children2);
        typeJson.put("types", rootJson);
        jsonObject.put("types", typeJson);
//        plugins
        if (editable == true) {
            List pluginList = Arrays.asList("contextmenu", "dnd", "state", "types", "wholerow");
            jsonObject.put("plugins", pluginList);
        } else {
            List pluginList = Arrays.asList("dnd", "state", "types", "wholerow");
            jsonObject.put("plugins", pluginList);
        }
        return jsonObject;
    }

    public JSONObject treeNodePackage(int resCatalogId, int depth, String catalogType, String objectId) {
        JSONObject jsonObject = new JSONObject();
        ResCatalog_Mongo resCatalog = resCatalogDao.getLocalResCatalogNodeById(objectId);
        jsonObject.put("id", resCatalog.getRid());
        jsonObject.put("text", resCatalog.getName());
        if (resCatalog.getLevel() == 1) {
            jsonObject.put("icon", "glyphicon glyphicon-home");
        } else if (resCatalog.getLevel() == 2) {
            jsonObject.put("icon", "/resources/img/first.png");
        } else if (resCatalog.getLevel() == 3) {
            jsonObject.put("icon", "/resources/img/second.png");
        } else {
            jsonObject.put("icon", "/resources/img/third.png");
        }

        if (resCatalog.getLevel() < depth) {
            JSONArray ja = new JSONArray();
            List<ResCatalog_Mongo> resCatalogs = resCatalogDao.getLocalResCatalogChildrens(resCatalogId);
            if (resCatalogs.size() > 0) {
                for (int i = 0; i < resCatalogs.size(); i++) {
                    ja.add(treeNodePackage(resCatalogs.get(i).getRid(), depth, catalogType, resCatalogs.get(i).getObjectId()));
                }
            }
            jsonObject.put("children", ja);
        }
        return jsonObject;
    }

    public int deleteLocalResCatalog(int id) {
        return resCatalogDao.deleteLocalResCatalog(id);
    }

    public List<ResCatalog_Mongo> getLocalResCatalogAll() {
        return resCatalogDao.getLocalResCatalogAll();
    }

    public ResCatalog_Mongo getLocalResCatalogNodeById(String resCatalogId) {
        return resCatalogDao.getLocalResCatalogNodeById(resCatalogId);
    }

    public ResCatalog_Mongo selectResCatalogNodeByRid(String rid) {
        return resCatalogDao.selectResCatalogNodeByRid(rid);
    }

}
