package cn.csdb.portal.controller;

import cn.csdb.portal.model.Node;
import cn.csdb.portal.service.NodeService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/node")
public class NodeController {

    @Resource
    private NodeService nodeService;

    @RequestMapping("/")
    public String toTheme(){
        return "node";
    }

    /** 
    * @Description: 判断nodeCode是否重复
    * @Param: [session, nodeCode] 
    * @return: boolean 
    * @Author: zcy
    * @Date: 2019/8/19 
    */ 
    @RequestMapping(value = "/queryNodeCode")
    @ResponseBody
    public boolean querySubjectCode(HttpSession session, @RequestParam(required = true) String nodeCode) {
        String subjectCode=session.getAttribute("SubjectCode").toString();
         long cntOfTheCode = nodeService.queryNodeCode(nodeCode,subjectCode);
        boolean retValue = false;
        if (cntOfTheCode > 0) {
            retValue = false;
        } else {
            retValue = true;
        }
        return retValue;
    }
    
    /** 
    * @Description: 获取主题库列表
    * @Param: [themeName, pageNo, pageSize] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/7 
    */ 
    @RequestMapping("/getNodeList")
    @ResponseBody
    public JSONObject getThemeList(@RequestParam(value = "nodeName",required = false) String nodeName,
                                   @RequestParam(value = "subjectCode",required = false) String subjectCode,
                                   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize ){
        JSONObject jsonObject=new JSONObject();
        List<Node> list=nodeService.selectAllThemesGallery(nodeName,subjectCode,pageNo,pageSize);
        int count=nodeService.countThemeList(nodeName,subjectCode);
        int totalPages=count%pageSize==0 ? count/pageSize:count/pageSize+1;
        jsonObject.put("list",list);
        jsonObject.put("totalCount", count);
        jsonObject.put("currentPage", pageNo);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("totalPages", totalPages);
        return jsonObject;
    }

    @RequestMapping(value = "/getNextSerialNo")
    @ResponseBody
    public String getNextSerialNo(String subjectCode) {
        String nextSerialNo = "";
        String lastSerialNo = "";
        lastSerialNo = nodeService.getLastSerialNo(subjectCode);
        nextSerialNo = Integer.parseInt(lastSerialNo) + 1 + "";

        return nextSerialNo;
    }

    /**
    * @Description: 新增主题库
    * @Param: [themesGallery]
    * @return: com.alibaba.fastjson.JSONObject
    * @Author: zcy
    * @Date: 2019/8/7
    */
    @RequestMapping(value = "/addNode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addGroup(Node node,HttpSession session) {
        String subjectCode=session.getAttribute("SubjectCode").toString();
        node.setCreateTime(new Date());
        JSONObject jsonObject =nodeService.addNode(node,subjectCode);
        return jsonObject;
    }

    /** 
    * @Description: 修改主题库信息 
    * @Param: [id] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/8 
    */ 
    @RequestMapping("/queryNodeById")
    @ResponseBody
    public JSONObject queryNodeById(@RequestParam(name = "id", defaultValue = "") String id) {
        Node themesGallery = nodeService.findById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("node", themesGallery);
        return jsonObject;
    }

/** 
* @Description: 保存修改的主题库信息 
* @Param: [themesGallery] 
* @return: com.alibaba.fastjson.JSONObject 
* @Author: zcy
* @Date: 2019/8/8 
*/ 
    @RequestMapping(value = "/updateNode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateNode(Node node) {
        node.setCreateTime(new Date());
        JSONObject jsonObject =nodeService.updateNodeById(node);
        return jsonObject;
    }

    /** 
    * @Description: 根据主题库id删除主题库
    * @Param: [id] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/8 
    */ 
    @RequestMapping("/deleteNode")
    @ResponseBody
    public JSONObject deleteThemeById(@RequestParam(required = true) String id,HttpSession session){
        String subjectCode=session.getAttribute("SubjectCode").toString();
        JSONObject jsonObject=  nodeService.deleteThemeById(subjectCode,id);

        return jsonObject;
    }
}
