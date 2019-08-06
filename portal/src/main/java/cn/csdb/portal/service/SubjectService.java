package cn.csdb.portal.service;

import cn.csdb.portal.model.Subject;
import cn.csdb.portal.repository.SubjectDao;
import org.springframework.stereotype.Service;
import cn.csdb.portal.utils.FileTreeNode;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: DataSync
 * @description: Subject Service class
 * @author: xiajl
 * @create: 2018-10-22 15:53
 **/
@Service
public class SubjectService {
    @Resource
    private SubjectDao subjectDao;

    /**
     * Function Description: 根据subjectCode查找对象
     *
     * @param:  subjectCode:专题库代码
     * @return: Sujbect对像
     * @auther: xiajl
     * @date:   2018/10/22 16:02
     */
    public Subject findBySubjectCode(String subjectCode){
        return subjectDao.findBySubjectCode(subjectCode);
    }

    /**
     * Function Description: 根据用户名查找对象
     *
     * @param:
     * @return:
     * @auther: xiajl
     * @date:   2018/10/31 9:37
     */
    public Subject findByUser(String userName){
        return subjectDao.findByUser(userName);
    }


    /**
     * Function Description: validate subject code login
     * @param userName
     * @param password
     * @return loginStatus
     */
    public int validateLogin(String userName, String password) {
        int loginStatus = 0;
        loginStatus = subjectDao.validateLogin(userName, password);

        return loginStatus;
    }

    public List<FileTreeNode> asynLoadingTree(String data, String path, String result){
        System.out.println("服务器系统:"+System.getProperties().getProperty("os.name"));
        String systemName=System.getProperties().getProperty("os.name");
        int isWindows=systemName.indexOf("Windows");
        List<FileTreeNode> nodeList=new ArrayList<FileTreeNode>();
        File dirFile = new File(path);//获取文件第一层
        File[] fs = dirFile.listFiles();
        if("init".equals(result)){
            nodeList.add(new FileTreeNode(path,"0",path,"true","true","false"));
        }
        for (int i = 0; i < fs.length; i++) {
            if(fs[i].isFile()){//当对象为文件时
                String pidStr="";
                if("-1".equals(isWindows+"")){//linux
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("/"));
                }else {
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(),pidStr,fs[i].getName(),"false","false","false"));
            }else if(fs[i].isDirectory()){//当对象为路径时
                String pidStr="";
                if("-1".equals(isWindows+"")){//linux
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("/"));
                }else {
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(),pidStr,fs[i].getName(),"false","true","false"));
                //  nodeList=loadingTree(fs[i].toString(),nodeList);
            }
        }
        return nodeList;
    }

    public  List<FileTreeNode> loadingTree(String path,List<FileTreeNode> nodeList){
        String systemName=System.getProperties().getProperty("os.name");
        File dirFile = new File(path);//获取文件第一层
        File[] fs = dirFile.listFiles();
        int isWindows=systemName.indexOf("Windows");
        for (int i = 0; i < fs.length; i++) {
            if(fs[i].isFile()){//当对象为文件时
                String pidStr="";
                if("-1".equals(isWindows+"")){//linux
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("/"));
                }else {
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(),pidStr,fs[i].getName(),"false","false","false"));
            }else if(fs[i].isDirectory()){//当对象为路径时
                String pidStr="";
                if("-1".equals(isWindows+"")){//linux
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("/"));
                }else {
                    pidStr=fs[i].toString().substring(0,fs[i].toString().lastIndexOf("\\"));
                }
                nodeList.add(new FileTreeNode(fs[i].toString(),pidStr,fs[i].getName(),"false","true","false"));
                // nodeList=loadingTree(fs[i].toString(),nodeList);
            }
        }

        return nodeList;
    }
}
