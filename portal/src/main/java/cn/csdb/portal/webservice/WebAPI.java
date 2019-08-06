package cn.csdb.portal.webservice;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.service.SubjectService;
import cn.csdb.portal.service.UserService;
import cn.csdb.portal.utils.MD5Util;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: DataSync
 * @description: a webservice method
 * @author: xiajl
 * @create: 2018-10-22 16:22
 **/
@RestController
@RequestMapping("/api")
public class WebAPI {
    private static final Logger logger = LogManager.getLogger(WebAPI.class);

    @Resource
    private SubjectService subjectService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/getSubject/{subjectCode}", method = RequestMethod.GET)
    public JSONObject getSubject(@PathVariable("subjectCode") String subjectCode) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = subjectService.findBySubjectCode(subjectCode);
        jsonObject.put("data", subject);
        return jsonObject;
    }


    @RequestMapping(value = "/getSubjectByUser/{userName}", method = RequestMethod.GET)
    public JSONObject getSubjectByUser(@PathVariable("userName") String userName) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = subjectService.findByUser(userName);
        jsonObject.put("data", subject);
        return jsonObject;
    }

    /**
     * Function Description: subject user login
     *
     * @param request
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/clientLogin", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject validateLogin(HttpServletRequest request, @RequestParam(required = true) String userName, @RequestParam(required = true) String password) {
        logger.info("enterring validateLogin");
        logger.info("userName = " + userName + ", password = " + password);

        JSONObject loginObject = new JSONObject();
        String loginNotice = "";
        int loginStatus = 0; // log success or not， 0 ：success, 1: failed, notice : username or password is wrong
        String securityPassword = MD5Util.encryptPassword(userName, password, "cnic.cn");
        loginStatus = userService.validateLogin(userName, securityPassword);

        if (loginStatus == 0) {
            loginNotice = "登录失败：用户名或者密码错误";
        } else {
            loginNotice = "登录成功！";
        }

        logger.info("loginStatus = " + loginStatus + ", loginNotice = " + loginNotice);

        loginObject.put("loginStatus", loginStatus);
        return loginObject;
    }

}
