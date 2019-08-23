package cn.csdb.portal.controller;

import cn.csdb.portal.service.CreatedTablesByExcelService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CreateTableByExcelController {

    @Autowired
    private CreatedTablesByExcelService createdTablesByCSVService;

    /**
     * 接到文件生成excel
     */
    @RequestMapping("/ceshi")
    public String ceshi(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
//
        String subjectCode=request.getSession().getAttribute("SubjectCode").toString();
        createdTablesByCSVService.ergodicNodeList(subjectCode);
        return "403";
    }

//

}
