package cn.csdb.portal.controller;

import cn.csdb.portal.service.CreatedTablesByJsonNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CreateTableByExcelController {

//    @Autowired
//    private CreatedTablesByExcelService createdTablesByCSVService;
    @Autowired
    private CreatedTablesByJsonNewService createdTablesByJsonNewService;

    /**
     * 接到文件生成excel
     */
    @RequestMapping("/ceshi")
    public String ceshi(HttpServletRequest request) {
//        JSONObject jsonObject = new JSONObject();
//        String subjectCode=request.getSession().getAttribute("SubjectCode").toString();

//        触发遍历所有主题库下的所有节点的JSON文件，建表
        createdTablesByJsonNewService.ergodicSubjectList();
        return "403";
    }

//

}
