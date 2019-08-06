<%--
  Created by IntelliJ IDEA.
  User: mengjinbao
  Date: 2019/3/18
  Time: 16:32
  用户管理文档
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title></title>
    <style>
        .form #bd-data tr.checked {
            background: #dddddd !important;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-fileinput/css/fileinput.min.css">
</head>
<body>
<%--弹窗页定义 开始--%>
<%--创建目录弹窗页--%>
<div id="addSonDirectory" class="modal fade" tabindex="-1" data-width="200">
    <div class="modal-dialog" style="min-width:600px;width:auto;max-width: 55%">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">新增目录名称</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" id="parentURI">
                <input id="directorName" placeholder="请输入目录名称"/>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="addDirectory(this)" class="btn green">创建目录
                </button>
            </div>
        </div>
    </div>
</div>
<%--重命名文件弹窗页--%>
<div id="renameDialog" class="modal fade" tabindex="-1" data-width="200">
    <div class="modal-dialog" style="min-width:600px;width:auto;max-width: 55%">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">重命名文件</h4>
            </div>
            <div class="modal-body">
                <input id="newName" placeholder="请输入新名称"/>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="reNameFile(this)" class="btn green">确定重命名
                </button>
            </div>
        </div>
    </div>
</div>
<%--新增文件弹窗页--%>
<div id="uploadFile" class="modal fade" tabindex="-1" data-width="200">
    <div class="modal-dialog" style="min-width:400px;width:auto;max-width: 35%">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">选择文件并上传</h4>
            </div>
            <div class="modal-body" style="height: 510px">
                <form enctype="multipart/form-data">
                    <div style="height: 400px">
                        <div class="file-loading">
                            <input id="file-5" type="file" multiple>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn default">退出</button>
            </div>
        </div>
    </div>
</div>
<%--弹窗页定义 结束--%>

<%--模板 开始--%>
<script type="text/html" id="fileNetList">
    {{if data != null}}
    {{each data as v i}}
    <tr>
        <td></td>
        <td class="{{v.fileType}} fileName {{if v.fileType == 'dir'}} file-name {{else}} file-file {{/if}}"
            path="{{v.filePath}}">
            <div class="text_name">{{v.fileName}}</div>
        </td>
        <td class="text-center">{{ if v.fileLength == null}} - {{else}} {{v.fileLength}} {{/if}}</td>
        <td class="text-center">{{v.fileLastModified}}</td>
    </tr>
    {{/each}}
    {{else}}
    <tr>
        <td class="text-center" colspan="4" style="height: 150px">当前目录下没有文件</td>
    </tr>
    {{/if}}
</script>

<script type="text/html" id="fileModules">
    ss
</script>
<%--模板结束--%>


<div class="form" id="FileOnNet">
    <div class="row">
        <div class="col-xs-6">
            <button type="button" class="btn btn-primary"
                    onclick='showUploadFileDialog($("#FileOnNet #fileBar span.modules").attr("path"))'><i
                    class="fa fa-upload"></i> 上传
            </button>
            <button type="button" class="btn btn-default" onclick="showAddFileDialog()"><i
                    class="fa fa-folder"></i> 新建文件夹
            </button>
        </div>
        <div class="col-xs-6 text-right file-search">
            <input id="searchName" type="text" placeholder="搜索您的文件">
            <button id="searchFile" type="button" class="btn btn-default" style="line-height: 7px;"><i
                    class="fa fa-search"></i> 搜索
            </button>
            <%-- <button type="button"><i class="fa fa-sort-alpha-asc"></i></button>
             <button type="button"><i class="fa fa-th-large"></i></button>--%>
        </div>
    </div>
    <div class="file-nav row">
        <div class="col-xs-12"><%--原值为：9--%>
            <div id="fileBar">
            </div>
            <input type="hidden" id="currentPath">
            <input type="hidden" id="currentName">
            <input type="hidden" id="copyCache">
        </div>
    </div>
    <table>
        <colgroup>
            <col style="width: 1%"/>
            <col style="width: 60%"/>
            <col style="width: 20%"/>
            <col style="width: 20%"/>
        </colgroup>
        <thead>
        <tr style="background-color: #5091dc">
            <td></td>
            <td style="color: white;">文件名</td>
            <td class="text-center" style="color: white;">大小</td>
            <td class="text-center" style="color: white;">修改日期</td>
        </tr>
        </thead>
    </table>
    <div class="file-list" style="overflow-y: scroll;height: 65%;">
        <table>
            <colgroup>
                <col style="width: 2%"/>
                <col style="width: 60%"/>
                <col style="width: 20%"/>
                <col style="width: 20%"/>
            </colgroup>
            <tbody id="bd-data">
        </table>
    </div>
</div>
</body>

<%--js 开始--%>
<div id="siteMeshJavaScript">
    <script type="text/javascript" src="${ctx}/resources/bundles/context/context.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrap-fileinput/js/fileinput.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrap-fileinput/js/locals/zh.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/layerJs/layer.js"></script>

    <script>
        <%--页面初始化需要加载的方法开始--%>
        (function () {
            var $body = $("body");
            var $ctx = "${ctx}";
            // 网盘功能模块  双击事件
            $body.on("dblclick", "#bd-data td.fileName", function (item) {
                var $td = "";
                if ($(item.target).is("div")) {
                    $td = $(item.target).parent();
                } else {
                    $td = $(item.target);
                }
                var fileType = $td.attr("class").split(" ")[0];
                var selectPath = $td.attr("path");
                if (fileType === "dir") {
                    // 类型为目录的打开
                    fileNet(selectPath);
                } else {
                    // 类型为文件的询问是否下载
                    var fileName = $td.text();
                    bootbox.confirm("<span style='font-size: 16px'>是否下载  " + fileName + "</span>", function (r) {
                        if (r) {
                            downloadFile(selectPath);
                        }
                    })
                }
            });


            // 选择多选框触发事件
            $body.on("click", ".fileNetCheck", function (item) {
                var $target = $(item.target);
                var id = $target.attr("id");
                if (id !== "undefined" && id === "all") {
                    var all = $target.is(":checked");
                    $("#FileOnNet table input.fileNetCheck").attr("checked", all);
                } else {
                    var current = $target.is(":checked");
                    if (!current) {
                        $("#FileOnNet table #all").attr("checked", false);
                    }
                }
            });


            // 鼠标点击事件
            $body.on("mousedown", "#FileOnNet table tr", function (e) {
                var which = e.which;
                // console.log(which); // 1 = 鼠标左键 left; 2 = 鼠标中键; 3 = 鼠标右键
                changeSelect(e);

                function getMenus() {
                    if (isDir) {
                        menus = menus.concat([
                            {
                                text: "打开", action: function () {
                                    fileNet($("#currentPath").data("currentPath"));
                                },
                                href: "javaScript:void(0);"
                            },
                            {
                                text: "上传到目录", action: function () {
                                    showUploadFileDialog($("#currentPath").data("currentPath"));
                                },
                                href: "javaScript:void(0);"
                            },
                            {divider: true}
                        ])
                    } else if (isFile) {
                        menus = menus.concat([
                            {
                                text: "下载", action: function () {
                                    downloadFile($("#currentPath").data("currentPath"));
                                }
                            }
                        ])
                    }
                    menus = menus.concat([
                        {
                            text: "复制", action: function () {
                                copyPath("copy")
                            },
                            href: "javaScript:void(0);"
                        },
                        {
                            text: "移动", action: function () {
                                copyPath("move")
                            },
                            href: "javaScript:void(0);"
                        }
                    ]);
                    if (copyCache !== undefined && copyCache !== "" && isDir) {
                        menus = menus.concat([{
                            text: "粘贴", action: function () {
                                pasteFile()
                            }
                        }]);
                    }
                    menus = menus.concat([
                        {divider: true},
                        {
                            text: "重命名", action: function () {
                                showRenameDialog()
                            },
                            href: "javaScript:void(0);"
                        },
                        {
                            text: "删除", action: function () {
                                deleteFile($("#currentPath").data("currentPath"));
                            },
                            href: "javaScript:void(0);"
                        }
                    ]);
                }

                if (which === 3) {
                    // 鼠标左键选中操作 右键替换默认菜单
                    context.init({preventDoubleContext: false});
                    var menus = [];
                    var copyCache = $("#copyCache").data("copyCache");
                    var isDir = $(e.currentTarget).find("td:eq(1).dir")[0];
                    var isFile = $(e.currentTarget).find("td:eq(1).file")[0];
                    getMenus();
                    context.destroy("#FileOnNet tbody tr");
                    context.attach("#FileOnNet tbody tr", menus);
                }
                return false;// 阻止链接跳转
            });

            $body.on("mousedown", "#FileOnNet table thead tr:eq(0)", function (e) {
                context.destroy("#FileOnNet table thead tr:eq(0)");
                var which = e.which;
                var copyCache = $("#copyCache").data("copyCache");
                if (which === 3 && (copyCache && copyCache !== "")) {
                    var menus = [{
                        text: "粘贴", action: function () {
                            $("#currentPath").data("currentPath", $.trim($("#fileBar span").attr("path")));
                            pasteFile()
                        }
                    }];
                    context.attach("#FileOnNet table thead tr:eq(0)", menus);
                }
            });


            // 初始化附件上传插件
            $("#file-5").fileinput({
                language: "zh",
                theme: 'fas',
                uploadUrl: "${ctx}/fileNet/addFile",
                showUpload: true,
                showCaption: false,
                // browseClass: "btn btn-primary btn-lg",
                browseClass: "btn btn-primary", //按钮样式
                dropZoneEnabled: true,//是否显示拖拽区域 默认显示
                fileType: "any",
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                overwriteInitial: false,
                hideThumbnailContent: true, // 隐藏文件的预览 以最小内容展示
                maxFileCount: 5, // 允许选中的文件数量
                maxFileSize: 1000000, // 允许选中的文件大小 KB
                uploadExtraData: function () {
                    return {
                        "parentURI": $.trim($("#parentURI").val())
                    }
                }

            }).on("filebatchselected", function (event, files) {
            }).on("fileuploaded", function (event, data) {
                fileNet($("#parentURI").val());
            });

            // 初始化文件搜索框
            $("#searchFile").on("click", function () {
                var val = $("#searchName").val();
                if ($.trim(val).length < 1) {
                    toastr["warning"]("请输入要检索的名称", "警告！");
                    return;
                }
                fileNetByName(val);
                /*
                本地页面搜索
                var $option = $("#bd-data tr td.fileName div.text_name");
                resetSelectedFile($option);
                searchFile($option, val);
                window.location.hash = "#reg_0";*/
            })
        })();

        /**
         * 选中当前tr
         * @param e
         */
        function changeSelect(e) {
            // 清除之前的选中
            $(e.currentTarget).siblings("[class*='checked']").attr("class", "");
            // 赋值现在的选中
            $(e.currentTarget).attr("class", "checked");
        }

        <%--页面初始化需要加载的方法结束--%>


        /**
         *网盘功能模块  目录获取
         *
         * @param selectPath 被选中的路径
         */
        function fileNet(selectPath) {
            $.ajax({
                type: "POST",
                url: "${ctx}/fileNet/fileList",
                dataType: "JSON",
                data: {
                    "rootPath": selectPath
                },
                success: function (data) {
                    createFileListHtml(data);
                }
            })
        }

        fileNet("");

        function createFileListHtml(data) {
            if (data.code === "error") {
                toastr["error"](data.message, "错误！");
                return;
            }
            var fileListHTML = template("fileNetList", {"data": data.data});
            var $bd = $("#bd-data");
            $bd.html("");
            $bd.append(fileListHTML);
            fileAddressBar(data);
            $("#FileOnNet table #all").attr("checked", false);
        }

        function fileNetByName(name) {
            $.ajax({
                type: "POST",
                url: "${ctx}/fileNet/fileNetByName",
                dataType: "JSON",
                data: {
                    rootPath: $("#fileBar span").attr("path"),
                    searchName: name
                },
                success: function (data) {
                    createFileListHtml(data);
                }
            })
        }

        // file address bar
        function fileAddressBar(data) {
            var a = "<a path='" + data.parent.filePath + "' onclick='returnParent(this)'>返回上级</a>&nbsp;|&nbsp;";
            var parentModules = data.parentModules;
            var length = parentModules.length;
            var modules = "";
            for (--length; length >= 0; length--) {
                var v = parentModules[length];
                if (length !== 0) {
                    modules += "<a path='" + v.path + "' class='modules' onclick='returnParent(this)'>" + v.name + "</a>&nbsp;>&nbsp;"
                } else {
                    modules += "<span path=" + v.path + " class='modules'>" + v.name + "</span>"
                }
            }
            var $fileBar = $("#fileBar");
            $fileBar.html("");
            $fileBar.append(a);
            $fileBar.append(modules);
        }


        /**
         * 返回到指定父级目录
         * @param item
         */
        function returnParent(item) {
            var $a = $(item);
            fileNet($a.attr("path"));
        }


        /**
         * 显示创建目录的弹窗
         * 将当前父路径赋值到指定位置
         */
        function showAddFileDialog() {
            var parentPath = $("#FileOnNet #fileBar span.modules").attr("path");
            $("#parentURI").val(parentPath);
            $("#addSonDirectory").modal("show");
        }


        // 增加目录
        function addDirectory(t) {
            var dirName = $.trim($("#directorName").val());
            if ("brother" === t) {
                dirName = $.trim($("#brotherDirectorName").val());
            }
            var parentURI = $("#parentURI").val();
            if (dirName === "") {
                toastr["warning"]("警告！", "请输入目录名称");
                return;
            }
            $.ajax({
                type: "POST",
                url: "${ctx}/fileNet/addDirectory",
                dataType: "JSON",
                data: {
                    "dirName": dirName,
                    "parentURI": parentURI
                },
                success: function (data) {
                    var jsonData = data;
                    if (jsonData.code === "error") {
                        toastr["error"]("错误！", jsonData.message);
                    } else {
                        toastr["success"]("成功！", jsonData.message);
                        $("#addSonDirectory").modal("hide");
                        $("#addBrotherDirectory").modal("hide");
                        fileNet(parentURI);
                    }
                }
            });
        }


        /**
         * 显示文件上传的弹窗
         * 将当前父路径赋值到指定位置
         */
        function showUploadFileDialog(parentPath) {
            $("#parentURI").val(parentPath);
            console.log(parentPath);
            $("#uploadFile").modal("show");
        }


        /**
         * 下载选择的文件
         * @param selectPath
         */
        function downloadFile(selectPath) {
            selectPath = encodeURIComponent(selectPath);
            var uri = "${ctx}/fileNet/downloadFile?selectPath=" + selectPath;
            window.open(uri);
        }


        /**
         * 显示重命名对话框 并赋值当前文件名称
         */
        function showRenameDialog() {
            var name = $.trim($("#currentName").data("currentName")).split(".")[0];
            $("#newName").val(name);
            $("#renameDialog").modal("show");
        }


        /**
         * 重命名文件
         *
         */
        function reNameFile() {
            var currentPath = $("#currentPath").data("currentPath");
            var newName = $.trim($("#newName").val());
            if (currentPath === "") {
                toastr["error"]("请选择被重命名的文件", "错误！");
                return false;
            }
            if (newName === "") {
                toastr["warning"]("请输入新名称", "警告！");
                return false;
            }

            $.ajax({
                type: "POST",
                url: "${ctx}/fileNet/renameFile",
                dataType: "JSON",
                data: {
                    "currentPath": currentPath,
                    "newName": newName
                },
                success: function (data) {
                    if (data.code === "success") {
                        toastr["success"]("重命名成功", "成功!");
                        fileNet($("#fileBar span").attr("path"));
                        $("#renameDialog").modal("hide");
                    } else {
                        toastr["error"](data.message, "重命名未成功");
                    }
                }

            });
        }


        /**
         * 获取被复制文件路径
         */
        function copyPath(operationType) {
            $("#copyCache").data("copyCache", $("#currentPath").data("currentPath"));
            $("#copyCache").data("operationType", operationType);
        }


        /**
         *将复制的文件粘贴到当前目录下
         */
        function pasteFile() {
            var currentPath = $("#currentPath").data("currentPath");
            var copyCache = $("#copyCache").data("copyCache");
            var operationType = $("#copyCache").data("operationType");
            var url = "";
            if (typeof copyCache === "undefined" || copyCache === "") {
                toastr["info"]("请选择要粘贴的文件", "提示！");
                return false;
            }
            if ("copy" === operationType) {
                url = "/fileNet/copyFile";
            } else if ("move" === operationType) {
                url = "/fileNet/moveFile"
            }
            if (url === "") {
                toastr["error"]("错误！");
                return false;
            }
            $.ajax({
                type: "POST",
                dataType: "JSON",
                url: "${ctx}" + url,
                data: {
                    "oldFile": copyCache,
                    "newFile": currentPath
                },
                beforeSend: function () {
                    index = layer.load(1, {
                        shade: [0.5, '#fff'] //0.1透明度的白色背景
                    });
                },
                success: function (data) {
                    if (data.code === "error") {
                        toastr["error"](data.message, "错误！");
                    } else {
                        $("#copyCache").data("copyCache", "");
                        toastr["success"](data.message, "成功！");
                        fileNet($("#fileBar span").attr("path"));
                    }
                },
                complete: function () {
                    $("#layui-layer-shade" + index + "").remove();
                    $("#layui-layer" + index + "").remove();
                },
                error: function (returndata) {
                    $("#layui-layer-shade" + index + "").remove();
                    $("#layui-layer" + index + "").remove();
                    toastr["error"]("错误！", returndata);
                }
            })
        }


        /**
         * 删除选定的文件
         */
        function deleteFile(deletePath) {
            if (typeof deletePath === "undefined") {
                toastr["warning"]("当前未选中文件", "警告！");
                return false;
            }
            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>", function (r) {
                if (r) {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/fileNet/deleteFile",
                        dataType: "JSON",
                        data: {
                            "deletePath": deletePath
                        },
                        beforeSend: function () {
                            index = layer.load(1, {
                                shade: [0.5, '#fff'] //0.1透明度的白色背景
                            });
                        },
                        success: function (data) {
                            if (data.code === "error") {
                                toastr["error"](data.message, "错误！");
                            } else {
                                $("#copyCache").data("copyCache", "");
                                toastr["success"](data.message, "成功！");
                                fileNet($("#fileBar span").attr("path"));
                            }
                        },
                        complete: function () {
                            fileNet($("#FileOnNet #fileBar span.modules").attr("path"));
                            $("#layui-layer-shade" + index + "").remove();
                            $("#layui-layer" + index + "").remove();
                        },
                        error: function (returndata) {
                            $("#layui-layer-shade" + index + "").remove();
                            $("#layui-layer" + index + "").remove();
                            toastr["error"]("错误！", returndata);
                        }
                    })
                }
            })
        }

        function resetSelectedFile(divs) {
            divs.removeAttrs("id");
            divs.each(function (index, value) {
                var $span = $(value).find("span");
                if ($span[0]) {
                    $span.replaceWith($span.text());
                    $(value).html($.trim($(value).html()));
                }
            })
        }

        function searchFile(divs, val) {
            var regId = 0;
            divs.each(function (index, value) {
                var $_this = $(this);
                var reg = new RegExp(val, "gi");
                var regId = 0;
                var text = value.innerText;
                if (reg.test(text)) {
                    $_this.attr("id", "reg_" + regId);
                    regId++;
                    $_this.html($_this.html().replace(reg, "<span style='color:red;font-weight: bold;'>" + val + "</span>"));
                }
            });
        }
    </script>
</div>
<%--js结束--%>


</html>