package cn.csdb.portal.controller;

import cn.csdb.portal.model.Group;
import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.User;
import cn.csdb.portal.service.GroupService;
import cn.csdb.portal.service.SubjectMgmtService;
import cn.csdb.portal.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: DataSync
 * @description: user group controller
 * @author: xiajl
 * @create: 2018-10-30 11:31
 **/
@RequestMapping("/group")
@Controller
public class GroupController {
    @Resource
    private GroupService groupService;

    @Resource
    private UserService userService;

    @Resource
    private SubjectMgmtService subjectMgmtService;

    private Logger logger= LoggerFactory.getLogger(GroupController.class);

    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model){
        logger.info("进入用户组列表页面");

        List<User> list = userService.getAllByRole("普通用户");
        model.addAttribute("list",list);

        List<Group> groupList = groupService.getGroupList();
        model.addAttribute("allGroupList", groupList);

        List<Subject> subjectList = subjectMgmtService.getSubjectCodeList();
        model.addAttribute("subjectList", subjectList);

        return "group";
    }


    @ResponseBody
    @RequestMapping("/getPageData")
    public JSONObject getPageData(HttpServletRequest request, @RequestParam(value = "groupName",required = false) String groupName,
                                  @RequestParam(value = "pageNo",defaultValue = "1") int pageNo,
                                  @RequestParam(value = "pageSize",defaultValue = "10") int pageSize )
    {
        List<Group> list = groupService.getListByPage(groupName,pageNo,pageSize);
        long count = groupService.countByPage(groupName);
        JSONObject json = new JSONObject();
        json.put("list",list);
        json.put("totalCount",count);
        json.put("currentPage",pageNo);
        json.put("pageSize",pageSize);
        json.put("totalPages",count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        return json;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(@PathVariable("id") String id){
        JSONObject jsonObject = new JSONObject();
        Group group = groupService.get(id);
        groupService.delete(group);
        jsonObject.put("result","ok");
        return jsonObject;
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addGroup(Group group) {
        group.setCreateTime(new Date());
        groupService.add(group);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        return jsonObject;
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateGroup(Group group) {
        Group newGroup  = groupService.get(group.getId());
        group.setUsers(newGroup.getUsers());
        group.setCreateTime(new Date());
        groupService.update(group);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        return jsonObject;
    }


    @RequestMapping("/info")
    @ResponseBody
    public JSONObject getGroupInfo(@RequestParam(name = "id", defaultValue = "") String id) {
        Group group = groupService.get(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("group", group);
        return jsonObject;
    }


    @RequestMapping("/getUserList")
    @ResponseBody
    public JSONObject getUserList() {
        List<User> list = userService.getAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", list);
        return jsonObject;
    }

    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public JSONObject updateUsers(String id, String[] users) {
        Group group = groupService.get(id);
        //此用户组中原有用户列表
        List<String> oldUsers = group.getUsers();
        List<String> list = Arrays.asList(users);


        if (list.contains("null"))
            list = null;
        group.setUsers(list);
        //groupService.update(group);
        groupService.updateUsersAndGroups(oldUsers,list,group);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping(value = "isExist",method = RequestMethod.GET)
    public boolean isExist(@RequestParam("groupName") String groupName)
    {
        return !(groupService.isExist(groupName));
    }

}
