package cn.csdb.portal.controller;

import cn.csdb.portal.model.Group;
import cn.csdb.portal.model.ThemesGallery;
import cn.csdb.portal.service.ThemeService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/theme")
public class ThemeController {

    @Resource
    private ThemeService themeService;

    @RequestMapping("/")
    public String toTheme(){
        return "theme";
    }
    
    /** 
    * @Description: 获取主题库列表
    * @Param: [themeName, pageNo, pageSize] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/7 
    */ 
    @RequestMapping("/getThemeList")
    @ResponseBody
    public JSONObject getThemeList(@RequestParam(value = "groupName",required = false) String themeName,
                                   @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize ){
        JSONObject jsonObject=new JSONObject();
        List<ThemesGallery> list=themeService.selectAllThemesGallery(themeName,pageNo,pageSize);
        jsonObject.put("list",list);
        return jsonObject;
    }

    /**
    * @Description: 新增主题库
    * @Param: [themesGallery]
    * @return: com.alibaba.fastjson.JSONObject
    * @Author: zcy
    * @Date: 2019/8/7
    */
    @RequestMapping(value = "/addTheme", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addGroup(ThemesGallery themesGallery) {
        themesGallery.setCreateTime(new Date());
        JSONObject jsonObject =themeService.addTheme(themesGallery);
        return jsonObject;
    }

    /** 
    * @Description: 修改主题库信息 
    * @Param: [id] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/8 
    */ 
    @RequestMapping("/toUpdateTheme")
    @ResponseBody
    public JSONObject toUpdateTheme(@RequestParam(name = "id", defaultValue = "") String id) {
        ThemesGallery themesGallery = themeService.findById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("theme", themesGallery);
        return jsonObject;
    }

/** 
* @Description: 保存修改的主题库信息 
* @Param: [themesGallery] 
* @return: com.alibaba.fastjson.JSONObject 
* @Author: zcy
* @Date: 2019/8/8 
*/ 
    @RequestMapping(value = "/updateTheme", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateGroup(ThemesGallery themesGallery) {
//        Group newGroup  = groupService.get(group.getId());
//        group.setUsers(newGroup.getUsers());
        themesGallery.setCreateTime(new Date());
        JSONObject jsonObject =themeService.updateThemesGalleryById(themesGallery);
        return jsonObject;
    }

    /** 
    * @Description: 根据主题库id删除主题库
    * @Param: [id] 
    * @return: com.alibaba.fastjson.JSONObject 
    * @Author: zcy
    * @Date: 2019/8/8 
    */ 
    @RequestMapping("/deleteTheme/{id}")
    @ResponseBody
    public JSONObject deleteThemeById(@PathVariable("id") String id){
        JSONObject jsonObject=  themeService.deleteThemeById(id);

        return jsonObject;
    }
}
