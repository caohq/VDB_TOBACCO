package cn.csdb.portal.controller;

import cn.csdb.portal.model.ResCatalog_Mongo;
import cn.csdb.portal.service.ResCatalogService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by pirate on 2018/10/22.
 */
@Controller
public class ResCatalogController {

    @Resource
    public ResCatalogService resCatalogService;

    @RequestMapping("/resCatalogRegist")
    public String resCatalogRegist() {
        System.out.println("资源目录注册");
        return "resCatalogRegist";
    }

    @RequestMapping("/resCatalogSubmit")
    @ResponseBody
    public String resCatalogSubmit(String packageTreeData, String deleteNodeArray) {
        JSONArray arry = JSONArray.parseArray(deleteNodeArray);
//        删除节点
        for (int i = 0; i < arry.size(); i++) {
            String nid = arry.get(i).toString();
            resCatalogService.deleteLocalResCatalog(Integer.parseInt(nid));
        }
        JSONArray ja = JSONArray.parseArray(packageTreeData);
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            ResCatalog_Mongo resCatalog = new ResCatalog_Mongo();
//              System.out.println(jo.get("text")+":"+jo.get("parent"));
            if (jo.get("id").toString().indexOf("_") < 0) {
                resCatalog.setRid(Integer.parseInt(jo.get("id").toString()));
            }else{
                resCatalog.setRid(Integer.parseInt(jo.get("id").toString().split("_")[1]));
            }
            resCatalog.setName(jo.get("text").toString());
            resCatalog.setLevel(Integer.parseInt(jo.get("level").toString()));
            String order = jo.get("order").toString();
            if (order.indexOf("_") > 0) {
                order = order.split("_")[1];
            }
            resCatalog.setNodeorder(Integer.parseInt(order));
            if (!jo.get("parent").equals("#")) {
                resCatalog.setParentid(Integer.parseInt(jo.get("parent").toString()));
            }
            Date createTime = new Date();
            resCatalog.setUpdatetime(createTime);
            resCatalog.setCreatetime(createTime);
            int nodeid = 0;
            if (jo.get("id").toString().indexOf("_") < 0) {//不存在_
                nodeid = resCatalogService.updateLocalResCatalog(resCatalog);
            } else {
                nodeid = resCatalogService.savaLocalResCatalog(resCatalog);
            }

//            if (i < ja.size() - 1) {
//                for (int j = i + 1; j < ja.size(); j++) {
//                    JSONObject jb = ja.getJSONObject(j);
//                    if (jb.get("parent").toString().equals(jo.get("id").toString())) {
//                        jb.put("parent", nodeid);
//                    }
//                }
//            }

        }
        return "success";
    }

    @RequestMapping("/resCatalogList")
    public ModelAndView resCatalogList() {
        ModelAndView modelAndView = new ModelAndView("resCatalogList");
        modelAndView.addObject("resCatalogList", resCatalogService.getLocalResCatalogAllRoot());
        return modelAndView;
    }

    @RequestMapping(value = "getLocalResCatalogList", method = RequestMethod.GET)
    @ResponseBody
    public List<ResCatalog_Mongo> getLocalResCatalogList() {
        return resCatalogService.getLocalResCatalogAll();
    }

    /*@RequestMapping(value = "getCenterResCatalogList", method = RequestMethod.GET)
    @ResponseBody
    public List<ResCatalog_Mongo> getCenterResCatalogList() {
        return resCatalogService.getCenterResCatalogAll();
    }
*/
    @RequestMapping("/resCatalog")
    public ModelAndView showResCatalog() {
        ModelAndView modelAndView = new ModelAndView("resCatalog");
//            JSONObject jsonObject = resCatalogService.getResCatalogTree(false);
//            modelAndView.addObject("treeJson",jsonObject);

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getLocalResCatalog")
    public JSONObject getLocalResCatalog(boolean editable) {
        JSONObject jsonObject = resCatalogService.getLocalResCatalogTree(editable,"local");
        return jsonObject;
    }

}
