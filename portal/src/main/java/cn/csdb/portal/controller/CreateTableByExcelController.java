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
        createdTablesByCSVService.getDbFile(subjectCode);
        return "403";
    }

//     根据 /home/ ThemeCode /节点名称/ db目录下的csv文件，
//    怎么判断哪些csv文件已经建表成功，哪些还未建表

}
