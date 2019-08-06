package cn.csdb.portal.controller;

import cn.csdb.portal.model.Group;
import cn.csdb.portal.model.MetadataTemplate;
import cn.csdb.portal.service.MetadataTemplateService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @program: DataSync
 * @description: metadata controller
 * @author: xiajl
 * @create: 2019-03-14 10:04
 **/
@Controller
public class MetadataController {
    private Logger logger= LoggerFactory.getLogger(MetadataController.class);
    @Resource
    private MetadataTemplateService metadataTemplateService;

    @RequestMapping("/admin/metadata")
    public String metadata(HttpServletRequest request, Model model)
    {
        return "metadata";
    }

    @ResponseBody
    @RequestMapping("/admin/metadata/getList")
    public JSONObject getList(@RequestParam(name = "extField") String extField, @RequestParam(name = "extFieldName") String extFieldName ){
        JSONObject jsonObject = new JSONObject();
        List<MetadataTemplate> list = metadataTemplateService.getList(extField,extFieldName);
        jsonObject.put("list",list);
        return jsonObject;
    }

    @RequestMapping(value = "/admin/metadata/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject add(MetadataTemplate metadataTemplate) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(metadataTemplate.getExtField()) || StringUtils.isEmpty(metadataTemplate.getExtFieldName())){
            jsonObject.put("result", "error");
        }else
        {
            metadataTemplateService.save(metadataTemplate);
            jsonObject.put("result", "ok");
        }
        return jsonObject;
    }

    @RequestMapping(value = "/admin/metadata/update", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject update(MetadataTemplate metadataTemplate) {
        metadataTemplateService.save(metadataTemplate);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        return jsonObject;
    }


    @RequestMapping(value = "/admin/metadata/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(@PathVariable("id") String id){
        JSONObject jsonObject = new JSONObject();
        metadataTemplateService.delete(id);
        jsonObject.put("result","ok");
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping(value = "/admin/metadata/isExist",method = RequestMethod.GET)
    public boolean isExist(@RequestParam("extField") String extField)
    {
        if (!extField.startsWith("ext_")){
            extField = "ext_" + extField;
        }
        return !(metadataTemplateService.isExist(extField));
    }


    @ResponseBody
    @RequestMapping(value = "/admin/metadata/getMaxOrder",method = RequestMethod.GET)
    public JSONObject getMaxOrder()
    {
        JSONObject jsonObject = new JSONObject();
        Integer result = metadataTemplateService.getMaxSortOrder();
        jsonObject.put("data",result+1);
        return jsonObject;
    }


    @RequestMapping("/admin/metadata/info")
    @ResponseBody
    public JSONObject info(@RequestParam(name = "id", defaultValue = "") String id) {
        MetadataTemplate metadataTemplate = metadataTemplateService.get(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("metadata", metadataTemplate);
        return jsonObject;
    }
}
