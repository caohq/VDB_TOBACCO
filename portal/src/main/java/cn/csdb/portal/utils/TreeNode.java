package cn.csdb.portal.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jinbao
 * @Date 2019/3/5 11:22
 * @Description 获取树信息
 **/
public class TreeNode {


    private int countJsNodes = 0;
    /**
     * 获取当前目录下所有的目录（不包含文件），
     * 如果为异步加载则只获取当前目录，不深度遍历。
     *
     * @param rootPath 当前目录
     * @param async    是否异步加载树
     * @return jsTree插件格式节点信息
     */
    public JSONObject jsTreeNodes(String rootPath, boolean async) {
        JSONObject jsonObject = new JSONObject();
        List<JsTreeNode> jsTreeNodes = new ArrayList<>(36);
        File rootFile = new File(rootPath);
        boolean directory = false;
        if(!rootFile.exists()){
            jsonObject.put("code","error");
            jsonObject.put("message",rootFile.getPath()+":不存在");
            return jsonObject;
        }
        if (countJsNodes == 0) {
            String rootFilePath = rootFile.getPath();
            String rootFileName = rootFile.getName();
            String rootParentPath = rootFile.getParentFile().getPath();
            jsTreeNodes.add(new TreeNode.JsTreeNode(rootFilePath, rootParentPath, rootFileName, "true", "", "false"));
            countJsNodes++;
        }
        File[] files = rootFile.listFiles();
        for (File file : files) {
            directory = file.isDirectory();
            String path = file.getPath();
            String name = file.getName();
            if(directory){
                JsTreeNode jsTreeNode = new JsTreeNode(path, rootPath, name, "false", directory + "", "false");
                jsTreeNodes.add(jsTreeNode);
            }
            // 是否异步加载目录
            // async = false 深度查找所有目录
            if (!async && directory) {
                JSONObject jsonObject1 = jsTreeNodes(path, false);
                jsTreeNodes.addAll((List<JsTreeNode>) jsonObject1.get("data"));
            }
        }
        jsonObject.put("code","success");
        jsonObject.put("message","节点生成成功");
        jsonObject.put("data",jsTreeNodes);
        return jsonObject;

    }

    /**
     * jsTree 节点类
     */
    public class JsTreeNode {
        // 当前节点
        private String id;
        // 父节点
        private String pid;
        // 当前节点名称
        private String name;
        // 默认是否展开
        private String open;
        // 是否为父节点
        private String isParent;
        //
        private String checked;

        public JsTreeNode() {
        }

        public JsTreeNode(String id, String pid, String name, String open, String isParent, String checked) {
            super();
            this.id = id;
            this.pid = pid;
            this.name = name;
            this.open = open;
            this.isParent = isParent;
            this.checked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getIsParent() {
            return isParent;
        }

        public void setIsParent(String isParent) {
            this.isParent = isParent;
        }

        public String getChecked() {
            return checked;
        }

        public void setChecked(String checked) {
            this.checked = checked;
        }
    }
}
