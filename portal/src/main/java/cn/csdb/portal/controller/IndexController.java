package cn.csdb.portal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiajl on 2018/9/19.
 */
@Controller
public class IndexController {
    private Logger logger= LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/")
    public String index(HttpServletRequest request)
    {
        logger.info("进入portal的首页");
        return "index";
    }
    @RequestMapping("/aaa")
    public String sindex(HttpServletRequest request)
    {
        logger.info("进入portal的首页");
        return "aaa";
    }
}
