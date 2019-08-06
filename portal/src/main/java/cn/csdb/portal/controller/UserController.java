package cn.csdb.portal.controller;

import cn.csdb.portal.model.User;
import cn.csdb.portal.service.UserService;
import cn.csdb.portal.utils.MD5Util;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/user")
public class UserController {
    private Logger logger= LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value="/queryUser")
    public JSONObject queryUser(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value="loginId", required = false, defaultValue = "") String loginId,
                                @RequestParam(value="userName", required = false, defaultValue = "") String userName,
                                @RequestParam(value="groups", required = false, defaultValue = "") String groups,
                                @RequestParam(value="curUserPageNum", required = false, defaultValue = "1") int curUserPageNum,
                                @RequestParam(value = "pageSize",required = false, defaultValue = "10") int pageSize)
    {
        logger.info("enter queryUser - parameters[loginId = " + loginId + ", userName = " + userName + ", groups = " + groups + "]");
        logger.info("enter queryUser - parameters[curUserPageNum = " + curUserPageNum + ", pageSize = " + pageSize + "]");

        List<User> users = userService.queryUser(loginId, userName, groups, curUserPageNum, pageSize);

        logger.info("query user result - users = " + users);

        // 准备返回数据
        JSONObject userQueryResult = new JSONObject();
        userQueryResult.put("list", users);
        userQueryResult.put("curUserPageNum", curUserPageNum);
        userQueryResult.put("pageSize", 10);
        long totalUsers = 0;
        try {
            totalUsers = userService.getTotalUsers(loginId, userName, groups);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        userQueryResult.put("totalUsers", totalUsers);
        long totalUserPages =  (totalUsers / pageSize + (totalUsers % pageSize == 0 ? 0 : 1));
        userQueryResult.put("totalUserPages", totalUserPages);
        logger.info("totalUsers = " + totalUsers + ", totalUserPages = " + totalUserPages);

        //设置http响应的编码
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");

        logger.info(userQueryResult.toJSONString());
        return userQueryResult;
    }

    @ResponseBody
    @RequestMapping(value="/addUser")
    public String addUser(HttpServletRequest request, User user)
    {
        logger.info("enter addUser - parameters[user = " + user + "]");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        user.setCreateTime(sdf.format(new Date()));
        user.setStat(1);
        user.setRole("普通用户");
        user.setCreateType("dataAssembler");
        logger.info("user to be added = " + user);

        int addedUserCnt = 0;
        String securityPassword = MD5Util.encryptPassword(user.getLoginId(), user.getPassword(), "cnic.cn");
        user.setPassword(securityPassword);
        try {
            addedUserCnt = userService.addUser(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        logger.info("after addUser - addedUserCnt = " + addedUserCnt);

        return "addUserNotice： add user successfully.";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteUser")
    public int deleteUser(HttpServletRequest request, String id)
    {
        logger.info("enterring deleteUser - id = " + id);
        int deletedUserCnt = userService.deleteUser(id);
        logger.info("deletedUserCnt = " + deletedUserCnt);
        return deletedUserCnt;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserById")
    public User getUserById(HttpServletRequest request, String id)
    {
        logger.info("enterring getUserById - id = " + id);
        User user = userService.getUserById(id);
        logger.info("user to be updated : user = " + user);

        return user;
    }

    @ResponseBody
    @RequestMapping(value = "/updateUser")
    public int updateUser(HttpServletRequest request, User user)
    {
        logger.info("enterring updateUser - user = " + user);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        user.setCreateTime(sdf.format(new Date()));
        user.setStat(1);
        user.setRole("普通用户");
        if (!Strings.isBlank(user.getPassword())) {
            String securityPassword = MD5Util.encryptPassword(user.getLoginId(), user.getPassword(), "cnic.cn");
            user.setPassword(securityPassword);
        } else {
            User userById = userService.getUserById(user.getId());
            user.setPassword(userById.getPassword());
        }
        int updatedUserCnt = userService.updateUser(user);
        logger.info("user cnt updated : updatedUserCnt = " + updatedUserCnt);

        return updatedUserCnt;
    }

    @RequestMapping(value = "/queryLoginId")
    @ResponseBody
    public boolean queryLoginId(HttpServletRequest request, @RequestParam(name="loginId", required = true) String loginId) {
        logger.info("enterring UserController-queryLoginId");
        logger.info("loginId = " + loginId);
        long loginIdCnt = userService.queryLoginId(loginId.trim());
        logger.info("queried loginIdCnt - loginIdCnt = " + loginIdCnt);

        boolean retValue = false;
        if (loginIdCnt > 0)
        {
            retValue = false;
        }
        else
        {
            retValue = true;
        }

        return retValue;
    }
}
