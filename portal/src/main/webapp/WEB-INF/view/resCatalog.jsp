<%--
  Created by IntelliJ IDEA.
  User: shibaoping
  Date: 2018/10/30
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>资源目录管理</title>
    <link href="${ctx}/resources/bundles/rateit/src/rateit.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/jstree/dist/themes/default/style.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/layerJs/theme/default/layer.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <div class="col-md-4" >
            <button type="button" class="btn btn-success btn-sm" style="margin-bottom:5px" onclick="editTree();"><i
                    class="glyphicon glyphicon-pencil"></i> 修改
            </button>
        <div id="jstree_show" style="height:300px"></div>
    </div>
    <div class="col-md-1"></div>
    <div class="col-md-4" id="editRegon" style="height:600px;width:500px;">
    </div>



<div id="deleteNodeModal" class="modal fade" tabindex="-1" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">删除节点</h4>
            </div>
            <div class="modal-body" style="min-height: 100px">
                <div class="col-md-12" align="center" id="deleteContent" nodeid=""></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn green" data-dismiss="modal" onclick="confirmDeleteNode();"><i
                        class="glyphicon glyphicon-ok"></i>删除
                </button>
                <button type="button" data-dismiss="modal" class="btn default">取消</button>
            </div>
        </div>
    </div>
</div>
</body>
<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/rateit/src/jquery.rateit.js" type="text/javascript"></script>
    <script src="${ctx}/resources/bundles/artTemplate/template.js"></script>
    <script src="${ctx}/resources/js/subStrLength.js"></script>
    <script src="${ctx}/resources/bundles/layerJs/layer.js"></script>
    <script src="${ctx}/resources/bundles/jstree/dist/jstree.js" type="text/javascript"></script>
    <script type="text/javascript">
        var ctx = '${ctx}';
        var deleteNodeArray;
        $().ready(function () {
            $.ajax({
                url: ctx + "/getLocalResCatalog",
                type: "get",
                dataType: "json",
                data: {editable: false},
                beforeSend:function(data){
                    index = layer.load(1, {
                        shade: [0.5,'#fff'] //0.1透明度的白色背景
                    });
                },
                success: function (data) {
                    $('#jstree_show').jstree(data);
                    $("#layui-layer-shade"+index+"").remove();
                    $("#layui-layer"+index+"").remove();
                }
            })
        });

        // 所有节点都加载完后
        $("#jstree_show").on("ready.jstree", function (event, data) {
            data.instance.open_node(46); // 展开root节点
        });


        function editTree() {
            if ($("#editRegon").html().trim() != "") {
                return false;
            }
            deleteNodeArray = new Array();
            var html = '<div class="row" style="margin-bottom:5px"> ' +
                '<button type="button" class="btn btn-default btn-sm" onclick="jstree_create();"><i class="glyphicon glyphicon-asterisk"></i> 添加</button> ' +
                '<button type="button" class="btn btn-default btn-sm" onclick="jstree_rename();"><i class="glyphicon glyphicon-pencil"></i> 重命名</button> ' +
                '<button type="button" class="btn btn-default btn-sm" onclick="jstree_delete();"><i class="glyphicon glyphicon-remove"></i> 删除</button> ' +
                '</div> ' +
                '<div id="jstree_edit" style="overflow: auto;margin-bottom: 25px;"></div> ' +
                '<div id="jstree_edit" style="max-height:550px;margin-bottom: 25px;"></div> ' +
                '<button type="button" class="btn btn-default btn-sm" onclick="jstree_cancel();" style="margin-left:5px;margin-bottom: 20px;"><i class="glyphicon glyphicon-remove"></i> 取消</button>' +
                '<button type="button" class="btn btn-primary btn-sm" onclick="jstree_submit();" style="margin-left:5px;margin-bottom: 20px;"><i class="glyphicon glyphicon-ok"></i> 提交</button>'
            $("#editRegon").append(html);


            $(function () {
                var to = false;
                $('#demo_q').keyup(function () {
                    if (to) {
                        clearTimeout(to);
                    }
                    to = setTimeout(function () {
                        var v = $('#demo_q').val();
                        $('#jstree_edit').jstree(true).search(v);
                    }, 250);
                });

                $.ajax({
                    url: ctx + "/getLocalResCatalog",
                    type: "get",
                    dataType: "json",
                    data: {editable: true},
                    beforeSend:function(data){
                        index = layer.load(1, {
                            shade: [0.5,'#fff'] //0.1透明度的白色背景
                        });
                    },
                    success: function (data) {
                        $('#jstree_edit').jstree(data);
                        $("#layui-layer-shade"+index+"").remove();
                        $("#layui-layer"+index+"").remove();
                        $("#jstree_edit").on("ready.jstree", function (event, data) {
                            data.instance.open_node(46); // 展开root节点
                        });
                    }
                })
            })
        }

        function jstree_cancel() {
            $("#editRegon").html("");
        }

        function jstree_create() {
            var ref = $('#jstree_edit').jstree(true),
                sel = ref.get_selected();

            var object=ref._model.data;
            var maxid=0;
            var regPos = /^\d+(\.\d+)?$/; //非负浮点数
            var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
            Object.keys(object).forEach(function(key){
                if(parseInt(object[key].id)>parseInt(maxid) && (regPos.test(object[key].id) || regNeg.test(object[key].id))){
                    console.log(maxid);
                    maxid=object[key].id;
                }
            });
            ref._cnt=parseInt(maxid);//最大id,防止rid重复
            var selected = jstree_selectOne(sel);
            if (selected) {
                toastr["warning"](selected);
                return;
            }
            if (!sel.length) {
                return false;
            }
            sel = sel[0];

            sel = ref.create_node(sel);
            var selLevel = ref.get_selected("true")[0].parents.length;
            if (selLevel === 1) {
                ref.set_icon(sel, "${ctx}/resources/img/first.png");
            } else if (selLevel === 2) {
                ref.set_icon(sel, "${ctx}/resources/img/second.png");
            } else if (selLevel === 3) {
                ref.set_icon(sel, "${ctx}/resources/img/third.png");
            } else {
                ref.set_icon(sel, "glyphicon glyphicon-th-list");
            }
            if (sel) {
                ref.edit(sel);
            }
        }

        function jstree_rename() {
            var ref = $('#jstree_edit').jstree(true),
                sel = ref.get_selected();
            var selected = jstree_selectOne(sel);
            if (selected) {
                toastr["warning"](selected);
                return;
            }
            if (!sel.length) {
                return false;
            }
            sel = sel[0];
            ref.edit(sel);
        }

        function jstree_delete() {

            var ref = $('#jstree_edit').jstree(true);
            sel = ref.get_selected();
            var selected = jstree_selectOne(sel);
            if (selected) {
                toastr["warning"](selected);
                return;
            }
            if (!sel.length) {
                return false;
            }
            $("#deleteContent").attr("nodeid", sel[0]);
            bootbox.confirm("确认删除",function (r) {
                if(r){
                    confirmDeleteNode();
                }else{

                }
            })
        }

        function jstree_selectOne(ref) {
            return ref.length <= 1 ? ref.length === 1 ? false : "至少选中一个" : "至多选中一个";
        }

        function confirmDeleteNode() {
            var ref = $('#jstree_edit').jstree(true);
            sel = $('#jstree_edit').jstree("get_node", $("#deleteContent").attr("nodeid"));
            ref.delete_node(sel);
            // if (sel.id.indexOf("_") < 0) {
            deleteNodeArray.push(sel.id);
            // }
            if(sel.children_d.length!=0){
                for(var i=0;i<sel.children_d.length;i++){
                    deleteNodeArray.push(sel.children_d[i]);
                }
            }
        }

        function jstree_submit() {
            var treeData = $.jstree.reference('jstree_edit')._model.data;
            var packageTreeData = new Array();
            $.each(treeData, function (n, value) {
                if (value.parent != null) {
                    var nodeData = {
                        id: value.id,
                        text: value.text,
                        parent: value.parent,
                        level: value.parents.length,
                        order: n
                    };
                    if(value.parent.indexOf('_')!=-1){
                        nodeData.parent= nodeData.parent.substring(nodeData.parent.lastIndexOf('_')+1,nodeData.parent.length)
                    }
                    packageTreeData.push(nodeData);
                }
            });
            $.ajax({
                url: ctx + "/resCatalogSubmit",
                type: "post",
                data: {
                    packageTreeData: JSON.stringify(packageTreeData),
                    deleteNodeArray: JSON.stringify(deleteNodeArray)
                },
                beforeSend:function(data){
                    index = layer.load(1, {
                        shade: [0.5,'#fff'] //0.1透明度的白色背景
                    });
                },
                success: function (data) {
                    $("#layui-layer-shade"+index+"").remove();
                    $("#layui-layer"+index+"").remove();
                    window.location.href = ctx + "/resCatalog";
//                    toastr["success"]("资源目录添加成功！", "success！");
                }
            })
        }


    </script>

</div>

</html>
