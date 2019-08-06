package cn.csdb.portal.controller;

import cn.csdb.portal.model.MetaTemplate;
import cn.csdb.portal.service.MetaTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @program: DataExplore
 * @description: meta template Controller
 * @author: xiajl
 * @create: 2019-04-24 10:31
 **/
@Controller
@RequestMapping("/metaTemplate")
public class MetaTemplateController {
    @Resource
    private MetaTemplateService metaTemplateService;

    @ResponseBody
    @RequestMapping(value = "/add")
    public JSONObject add(@ModelAttribute MetaTemplate metaTemplate, @RequestParam(name = "subjectCode") String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        metaTemplate.setSubjectCode(subjectCode);
        metaTemplateService.add(metaTemplate);
        jsonObject.put("result", "true");
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping(value = "/getList")
    public JSONObject getList(@RequestParam(name = "subjectCode") String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        List<MetaTemplate> list = metaTemplateService.getList(subjectCode);
        jsonObject.put("list", list);
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping(value = "/list")
    public JSONObject list(@RequestParam(name = "subjectCode") String subjectCode,
                           @RequestParam(name="pageNo",defaultValue = "1") int pageNo,
                           @RequestParam(name="pageSize",defaultValue = "10") int pageSize){

        List<MetaTemplate> list = metaTemplateService.getListByPage(subjectCode,pageNo,pageSize);
        long count = metaTemplateService.countByPage(subjectCode);
        JSONObject json = new JSONObject();
        json.put("list",list);
        json.put("totalCount",count);
        json.put("currentPage",pageNo);
        json.put("pageSize",pageSize);
        json.put("totalPages",count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        return json;
    }


    @RequestMapping(value = "/templateDetail/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject viewData(@PathVariable("id") String id) {
        JSONObject result = new JSONObject();
        MetaTemplate  metaTemplate = metaTemplateService.get(id);
        result.put("metaTemplate",metaTemplate);
        return result;
    }



    @RequestMapping("/toMateTemMsgList")
    public String getDataMsgList() {
//        String subjectCode = session.getAttribute("SubjectCode").toString();
//        model.addAttribute("subjectCode",subjectCode);
        return "MateTemMsgList";
    }
    @ResponseBody
    @RequestMapping(value = "/getAllList")
    public JSONObject getAllist(HttpSession session) {
        String subjectCode = session.getAttribute("SubjectCode").toString();
        JSONObject jsonObject = new JSONObject();
        List<MetaTemplate> list = metaTemplateService.getAllList(subjectCode);
        jsonObject.put("list", list);
        return jsonObject;
    }

    @RequestMapping("/getDetail")
    @ResponseBody
    public JSONObject getDetail(String id) {
        JSONObject jsonObject = new JSONObject();
        MetaTemplate metaTemplate = metaTemplateService.get(id);
        List<Map<String, Object>> extMetadata = metaTemplate.getExtMetadata();
        List<String> extList = new ArrayList<>();
        if(extMetadata!=null) {
            for (int i = 0; i < extMetadata.size(); i++) {
                Map<String, Object> map = extMetadata.get(i);
                Set<String> set = map.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    extList.add(map.get(it.next()).toString());
                }
            }
        }
        jsonObject.put("list", metaTemplate);
        jsonObject.put("extList",extList);
        return jsonObject;
    }

    @RequestMapping("/deleteMetaTem")
    @ResponseBody
    public JSONObject deleteMetaTem(String id){
        JSONObject jsonObject=new JSONObject();
         metaTemplateService.deleteById(id);
         jsonObject.put("success","success");
        return jsonObject;
    }

}
