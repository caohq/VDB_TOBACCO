package cn.csdb.portal.controller;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.model.ThemesGallery;
import cn.csdb.portal.service.SubjectMgmtService;
import cn.csdb.portal.service.ThemeService;
import cn.csdb.portal.utils.MD5Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/subjectMgmt")
public class SubjectMgmtController {
    private SubjectMgmtService subjectMgmtService;
    private static final Logger logger = LogManager.getLogger(SubjectMgmtController.class);

    @Value("#{systemPro['imagesPath']}")
    private String imagesPath;

	@Value("#{systemPro['ftpServerAddr']}")
	private String ftpServerAddr;

	@Resource
    private ThemeService themeService;

    @Autowired
    public void setProjectLibService(SubjectMgmtService subjectService) {
        this.subjectMgmtService = subjectService;
    }

    /**
     * Function Description: add subject to db
     *
     * @param subject, the wrapped object which contains information of the subject ot be added
     * @return redirectStr, the request is redirected to querySubject interface
     */
    @RequestMapping(value = "/addSubject", method = RequestMethod.POST)
    @ResponseBody
    public String addSubject(HttpServletRequest request, Subject subject, @RequestParam("image") MultipartFile image) {
//        create ftp enviroment aotomatic
        Runtime runtime = Runtime.getRuntime();
//        try {
//
//            System.out.println("-----------------------");
//            String command1 = "chmod 777 /etc/vsftpd/vftpuseradd";
//            Runtime.getRuntime().exec(command1).waitFor();
////            Process process = runtime.exec("vftpuseradd "+subject.getFtpUser()+" "+subject.getFtpPassword());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //input parameter check
        logger.info("enterring subjectMgmt-addSubject.");
        logger.info(subject);
        logger.info("image(" + image + ")");

        //save image
        logger.info("saving image");
        String imagePath = saveImage(request, image);
        subject.setImagePath(imagePath);
        logger.info("image saved");

        //generate ftp user account and password
        logger.info("generate ftp user and password for the subject");
        generateFtpInfo(subject);
        logger.info("generate ftp user and password completed!");

		subject.setFtpServerAddr(ftpServerAddr);
        String addSubjectNotice = subjectMgmtService.addSubject(subject);
        logger.info("subject added, addSubjectNotice : " + addSubjectNotice);

      /*  long totalPages = subjectMgmtService.getTotalPages();
        String redirectStr = "redirect:/subjectMgmt/querySubject?pageNum=" + totalPages;
        logger.info("redirect request to querySubject : " + redirectStr);*/
        return addSubjectNotice;
    }

    /**
     * Function Description: generate ftp user and password for subject to be added
     *
     * @param subject, the subject to be added
     */
    private void generateFtpInfo(Subject subject) {
        String ftpUser = "ftpUser" + subject.getSubjectCode();
        String ftpPassword = "ftpPassword" + subject.getSubjectCode();

        subject.setFtpUser(ftpUser);
        subject.setFtpPassword(ftpPassword);
    }

    /**
     * Function Description: save image binary data to local filesystem and return the path
     *
     * @param image， the image to be stored
     * @return imageFilePath, the absolute local filesystem path of the image, may be a path like {imagesPath}/{SubjectCode}/img1.jpg, here {imagesPath} represent the save images`s system path ,images{SubjectCode} represent the current user.
     */
    private String saveImage(HttpServletRequest request, MultipartFile image) {
        logger.info("save image file, image = " + image);
        String subjectCode = request.getParameter("subjectCode");
        String imagesPath = this.imagesPath;
        imagesPath += "/" + subjectCode;
        if (!(new File(imagesPath).exists())) {
            new File(imagesPath).mkdir();
        }

        String fileName = image.getOriginalFilename();

        // check the input file's path to ensure user input a real image
        String imageFilePath = "";
        if (!(fileName == null || fileName.equals(""))) {
            imageFilePath = imagesPath + "/" + fileName;
        } else {
            return "";
        }

        // if the /SubjectImages/ directory doesn't exsit, then create it.
        File imageFilePathObj = new File(imageFilePath);
        if (!imageFilePathObj.getParentFile().exists()) {
            imageFilePathObj.getParentFile().mkdirs();
        }

        // store the image to local filesystem
        try {
            image.transferTo(imageFilePathObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("image saved path : " + imageFilePath);
        logger.info("save image file completed!");
        String replace = imageFilePathObj.getPath().replace("\\", "/");
        replace = replace.replace("/home/userimg", "/imagesPath");
        return replace;
    }

    /**
     * Function Description:
     *
     * @param id,      the id of Subject to be deleted
     * @param pageNum, the page which contains the deleted subject, this parameter is designed for request redirect
     * @return redirectStr, the request is redirected to querySubject interface
     */
    @RequestMapping(value = "/deleteSubject")
    @ResponseBody
    public int deleteSubject(HttpServletRequest request, @RequestParam(required = true) String id, @RequestParam(required = true) int pageNum) {
        logger.info("SubjectMgmtController-deleteSubject, id = " + id + ", currentPage = " + pageNum);

        int deletedRowCnt = subjectMgmtService.deleteSubject(id);
        logger.info("SubjectMgmtController-deleteSubject，deletedRowCnt = " + deletedRowCnt);

        //return the count of subject already deleted
        return deletedRowCnt;
    }

    /**
     * Function Description:
     *
     * @param subject the subject to be updated
     * @param image   the image field
     * @return redirectStr, the request is redirected to querySubject interface
     */
    @RequestMapping(value = "/updateSubject", method = RequestMethod.POST)
    @ResponseBody
    public String updateSubject(HttpServletRequest request, Subject subject, @RequestParam(name = "image", required = false) MultipartFile image) {
        logger.info("SubjectMgmtController-updateSubject");
        logger.info("SubjectMgmtController-updateSubject -" + subject);
        logger.info("updating image");
        String newImagePath = "";
        try {
            newImagePath = updateImage(request, subject, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        subject.setImagePath(newImagePath);
        logger.info("updated image");
        Subject subjectByCode = subjectMgmtService.findByCode(subject.getSubjectCode());
        if (!Strings.isBlank(subject.getAdminPasswd())) {
            String securityPassword = MD5Util.encryptPassword(subject.getAdmin(), subject.getAdminPasswd(), "cnic.cn");
            subject.setAdminPasswd(securityPassword);
        } else {
            subject.setAdminPasswd(subjectByCode.getAdminPasswd());
        }
        String updateSubjectNotice = subjectMgmtService.updateSubject(subject);
        logger.info("update subject completed. updateSubjectNotice = " + updateSubjectNotice);

        return updateSubjectNotice;
    }

    /**
     * Function Description: update image is designed to save the file user selected on UpdateSubjectDialog
     *
     * @param subject the subject to be updated
     * @param image   the image to be saved
     * @return imagePath the absolute local filesystem path of the image
     */
    private String updateImage(HttpServletRequest request, Subject subject, MultipartFile image) {
        logger.info("updating image");

        String imagePath = "";

        Subject tmpSubject = subjectMgmtService.findSubjectById(subject.getId());
        if ((image != null) && (image.getOriginalFilename() != "")) {
            deleteImage(tmpSubject.getImagePath());
            imagePath = saveImage(request, image);
        } else if ((image != null) && (image.getOriginalFilename() == null)) {
            imagePath = saveImage(request, image);
        } else {
            imagePath = tmpSubject.getImagePath();
        }

        return imagePath;
    }

    /**
     * Function Description: when user select a image on UpdateSubjectDialog, we must delete the previous one before store the new image
     *
     * @param imagePath
     */
    private void deleteImage(String imagePath) {
        logger.info("updateSubject - image path to be deleted : " + imagePath);

        try {
            File imagePathFileObj = new File(imagePath);
            imagePathFileObj.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("updateSubject - delete image completed!");
    }

    @RequestMapping(value = "/subjectIndex")
    public ModelAndView subjectIndex(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("subjectMgmt");
        return mv;
    }

    @RequestMapping(value = "/subjectDes")
    public ModelAndView subjectDes(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("subjectDesigned");
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = "/querySubject")
    public JSONObject querySubject(HttpServletRequest request, @RequestParam(value = "subjectNameFilter") String subjectNameFilter, @RequestParam(value = "pageNum", required = true) int currentPage) {
        logger.info("enterring SubjectMgmtController-querySubject[currentPage = " + currentPage + "]");

        long totalPages = 0;
        totalPages = subjectMgmtService.getTotalPages(subjectNameFilter);
        List<Subject> subjectsOfThisPage = subjectMgmtService.querySubject(subjectNameFilter, currentPage);

        logger.info("queried subject - " + subjectsOfThisPage);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalPages", totalPages);
        jsonObject.put("pageNum", currentPage);
        jsonObject.put("pageSize", 10);
        jsonObject.put("total", subjectMgmtService.getTotalSubject(subjectNameFilter));
        jsonObject.put("list", subjectsOfThisPage);

        logger.info("jsonObject = " + jsonObject);
        logger.info("querySubject done!");

        return jsonObject;
    }

    /**
     * Function Description:
     *
     * @param id
     * @return Subject's json representation
     */
    @RequestMapping(value = "/querySubjectById")
    @ResponseBody
    public JSONObject querySubjectById(HttpServletRequest request, @RequestParam(required = true) String id) {
        logger.info("enterring SubjectMgmtController-querySubjectById");
        logger.info("id = " + id);
        Subject subject = subjectMgmtService.findSubjectById(id);
        List<ThemesGallery> list=themeService.getAll();
        logger.info("queried subject - " + subject);
        return (JSONObject) JSON.toJSON(subject);
    }

    @RequestMapping(value = "/findSubjectAndThemeById")
    @ResponseBody
    public JSONObject findSubjectAndThemeById(String id) {
        Subject subject = subjectMgmtService.findSubjectById(id);
        List<ThemesGallery> list=themeService.getAll();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("subject",subject);
        jsonObject.put("list",list);
        return jsonObject;
    }


    /**
     * Function Description: check if the subject code has been used
     *
     * @param subjectCode, the subject code user give
     * @return 1/0, 1 exists, 0 no
     */
    @RequestMapping(value = "/querySubjectCode")
    @ResponseBody
    public boolean querySubjectCode(HttpServletRequest request, @RequestParam(required = true) String subjectCode) {
        logger.info("enterring SubjectMgmtController-querySubjectCode");
        logger.info("subjectCode = " + subjectCode);
        long cntOfTheCode = subjectMgmtService.querySubjectCode(subjectCode);
        logger.info("queried subjectCodeCnt - cntOfTheCode = " + cntOfTheCode);

        boolean retValue = false;
        if (cntOfTheCode > 0) {
            retValue = false;
        } else {
            retValue = true;
        }
        return retValue;
    }

    /**
     * Function Description: check if the subject code has been used
     *
     * @param admin, admin
     * @return 1/0, 1 exists, 0 no
     */
    @RequestMapping(value = "/queryAdmin")
    @ResponseBody
    public boolean queryAdmin(HttpServletRequest request, @RequestParam(required = true) String admin) {
        logger.info("enterring SubjectMgmtController-queryUserName");
        logger.info("admin = " + admin);
        long cntOfAdmin = subjectMgmtService.queryAdmin(admin.trim());
        logger.info("queried userNameCnt - cntOfUserName = " + cntOfAdmin);

        boolean retValue = false;
        if (cntOfAdmin > 0) {
            retValue = false;
        } else {
            retValue = true;
        }

        return retValue;
    }

    @RequestMapping(value = "/getNextSerialNo")
    @ResponseBody
    public String getNextSerialNo(HttpServletRequest request) {
        String nextSerialNo = "";
        String lastSerialNo = "";
        lastSerialNo = subjectMgmtService.getLastSerialNo();
        nextSerialNo = Integer.parseInt(lastSerialNo) + 1 + "";

        logger.info("lastSerialNo = " + lastSerialNo);
        logger.info("nextSerialNo = " + nextSerialNo);

        return nextSerialNo;
    }

    @RequestMapping(value = "/getNextSerialNoAndTheme")
    @ResponseBody
    public JSONObject getNextSerialNoAndTheme(HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject();
        String nextSerialNo = "";
        String lastSerialNo = "";
        lastSerialNo = subjectMgmtService.getLastSerialNo();
        nextSerialNo = Integer.parseInt(lastSerialNo) + 1 + "";

        logger.info("lastSerialNo = " + lastSerialNo);
        logger.info("nextSerialNo = " + nextSerialNo);
        List<ThemesGallery> list=themeService.getAll();
        jsonObject.put("list",list);
        jsonObject.put("nextSerialNo",nextSerialNo);
        return jsonObject;
    }
}
