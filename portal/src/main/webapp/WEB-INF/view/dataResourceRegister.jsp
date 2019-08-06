<%--
  Created by IntelliJ IDEA.
  User: shibaoping
  Date: 2018/10/29
  Time: 15:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>分布式资源整合系统-静态资源注册</title>
    <link href="${ctx}/resources/css/select2.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/jstree/dist/themes/default/style.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/jstree/dist/themes/default/style.min.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/customJstree.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-datepicker/css/datepicker3.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css">
</head>
<body>
<!-- BEGIN CONTAINER -->
<div class="page-content">
    <h3 class="page-title">
        静态数据注册
    </h3>
    <div class="page-bar">
        <ul class="page-breadcrumb">
            <li>
                <i class="fa fa-home"></i>
                <a href="#">资源服务注册</a>
                <i class="fa fa-angle-right"></i>
            </li>
            <li>
                <a href="#">静态数据注册</a>
            </li>
        </ul>
    </div>
    <!-- BEGIN CONTENT -->
    <div class="row">
        <div class="col-md-12">
            <div>
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div id="resourceChooseDiv">
                        </div>
                        <div id="alreadyResourceChooseDiv">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="staticSourceTableChoiceModal" class="modal fade" tabindex="-1" data-width="200">
        <div class="modal-dialog" style="min-width:600px;width:auto;max-width: 55%">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                            id="editTableFieldComsCloseId"></button>
                    <h4 class="modal-title" id="relationalDatabaseModalTitle">编辑表字段注释</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="portlet box green-haze" style="border:0;">
                                <div class="portlet-title">
                                    <ul class="nav nav-tabs" style="float:left;">
                                        <li class="active">
                                            <a href="#editTableFieldComsId" data-toggle="tab"
                                               id="editTableDataAndComsButtonId" aria-expanded="true">
                                                编辑 </a>
                                        </li>
                                        <li class="">
                                            <a href="#previewTableDataAndComsId" id="previewTableDataAndComsButtonId"
                                               data-toggle="tab" aria-expanded="false">
                                                预览 </a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="tab-content"
                                     style="background-color: white;min-height:300px;max-height:70%;padding-top: 20px ; overflow: scroll;">
                                    <div class="tab-pane active" id="editTableFieldComsId">
                                    </div>
                                    <div class="tab-pane" id="previewTableDataAndComsId">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="editTableFieldComsSaveId" data-dismiss="modal" class="btn green">保存
                    </button>
                    <%--<button type="button" data-dismiss="modal" id="editTableFieldComsCancelId" class="btn default">取消</button>--%>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="./tableFieldComsTmpl.jsp" %>

<script type="text/html" id="systemTmpl">
    {{each list}}
    <tr>
        <td style="text-align: center">{{(currentPage-1)*pageSize+$index+1}}</td>
        <td><a href="javascript:viewData('{{$value.templateId}}');">{{$value.templateName}}</a>
        </td>
        <td style="text-align: center">{{$value.creator}}</td>
        <td style="text-align: center">{{dateFormat($value.createDate)}}</td>
        <td style="text-align: center">{{$value.memo}}</td>
        <td id="{{$value.templateId}}" style="text-align: center">
            <button type="button" class="btn default btn-xs purple updateButton"
                    onclick="selectData('{{$value.templateId}}')"><i class="fa fa-edit"></i>&nbsp;&nbsp;选择
            </button>
            &nbsp;&nbsp;
        </td>
    </tr>
    {{/each}}
</script>
</body>

<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/artTemplate/template.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/jquery-validation/js/jquery.validate.min.js"></script>
    <script type="text/javascript"
            src="${ctx}/resources/bundles/jquery-validation/js/additional-methods.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-json/dist/jquery.json.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/select2/select2.min.js"></script>
    <script src="${ctx}/resources/bundles/jstree/dist/jstree.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/js/dataRegisterEditTableFieldComs.js"></script>
    <script src="${ctx}/resources/js/regex.js"></script>
    <script src="${ctx}/resources/js/metaTemplate.js"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script>
        var ctx = '${ctx}', edit = false;
        $(function () {
            chooseTable(7)
        });
        function chooseTable(dataSourceId) {
            $("#dataSourceId").val(dataSourceId);
            $.ajax({
                type: "GET",
                url: '${ctx}/relationalDatabaseTableList',
                data: {dataSourceId: dataSourceId},
                dataType: "json",
                success: function (data) {
                    var html = "<div class='form-group'> <label class='control-label col-md-3'>数据源名称 </label>" +
                        "<label class='col-md-3' style='padding-top: 9px'>" + data.dataSourceName + " </label> </div>";
                    html += "<div class='form-group'>" +
                        "<label class='control-label col-md-3'>选择表资源</label>"
                        "<div class='col-md-9'>" +
                        "<div class='icheck-list' style='padding-top: 7px'>";
                    var list = data.list;
                    for (var i = 0; i < list.length; i++) {
                        html += "<label class='col-md-4' style='padding-left: 0px'><input type='checkbox' name='mapTable' onclick=\"staticSourceTableChoice(1,this," + dataSourceId + ",'" + list[i] + "','dataResource')\" value='" + list[i] + "'>&nbsp;" + list[i] + "</label>"
                    }
                    html += "</div><input type='text' class='form-control' name='maptableinput' id='maptableinput' style='display:none;'/></div></div>";
                    $("#resourceChooseDiv").html(html);
                   /* var sqlHtml = "<div class='form-group'> <label class='control-label col-md-3'>sql查询 </label> " +
                        "<div class='col-md-4'> " +
                        "<input type='text' class='form-control' name='sqlStr' id='sqlStr' onchange='validSqlStr(this.value,this.id)'/> </div>" +
                        "<div class='col-md-5' style='margin-top:7px'><a onclick='editSqlFieldComs(0)' >" +
                        "编辑预览&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='newSqlStr()' >" +
                        "<span class='glyphicon glyphicon-plus'></span>&nbsp;sql查询</a></div></div>";
                    $("#showSqlStr").html(sqlHtml);*/

                },
            });
        }

        function newSqlStr() {
            var result = true;
            $(":input[name^='sqlStr']").each(function () {
                if (!$(this).val() || !$(this).val().trim()) {
                    toastr["error"]("错误！", "请先完成当前sql编辑");
                    result = false;
                    return;
                }
            });
            if (!result) {
                return;
            }
            if (!validSqlStrResult) {
                toastr["error"]("错误！", "请先通过当前sql校验");
                return;
            }
            sqlNum++;
            var html = "<div class='form-group' id='sql" + sqlNum + "'> <label class='control-label col-md-3'>sql查询" + sqlNum + " </label> " +
                "<div class='col-md-4'> " +
                "<input type='text' class='form-control' name='sqlStr' id='sqlStr" + sqlNum + "' onchange='validSqlStr(this.value,this.id);'/>" +
                "</div><div class='col-md-5' style='margin-top:7px'>" +
                "<a onclick='editSqlFieldComs(" + sqlNum + ")'>" +
                "编辑预览&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<a onclick='deleteSqlStr(" + sqlNum + ")'>" +
                "<span class='glyphicon glyphicon-remove'></span>&nbsp;删除sql</a></div></div>";
            $("#showSqlStr").append(html);
        }

        function deleteSqlStr(sqlId) {
            $("#sql" + sqlId).remove();
        }

        function showFileSource(currentPage) {
            $("#generateRelationalDatabase").html("");
            $("#showSqlStr").html("");
            $.ajax({
                type: "GET",
                url: ctx + '/fileSourceList',
                data: {currentPage: currentPage},
                dataType: "json",
                success: function (data) {
                    var totalPages = data.totalPages;
                    var currentPage = data.currentPage;
                    var html = "<div class='form-group'><label class='control-label col-md-3'>选择文件</label><div class='col-md-9'><table class='table table-hover'><thead> <tr> <th>#</th> <th>数据源名称</th> <th>数据源类型</th> <th>操作</th> </tr> </thead><tbody>";
                    var dataSrcs = data.dataSrcs;
                    for (var i = 0; i < dataSrcs.length; i++) {
                        html += "<tr><td>" + (i + 1) + "</td>" + "<td>" + dataSrcs[i].dataSourceName + "</td>" + "<td>" + dataSrcs[i].dataSourceType + "</td>" + "<td><a href='javascript:void(0)' onclick='chooseFile(" + dataSrcs[i].dataSourceId + "," + '"' + dataSrcs[i].dataSourceName + '"' + ")'>选择</a></td></tr>"
                    }
                    html += "</tbody></table><div align='center'> <ul class='pagination'> <li> <a href='javascript:void(0)' onclick='showFileSource(1)'> < </a> </li>";
                    if (currentPage == totalPages && totalPages > 4) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage - 4) + ")'>" + (currentPage - 4) + "</a> </li>";
                    }
                    if (currentPage > totalPages - 2 && currentPage > 4) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage - 3) + ")'>" + (currentPage - 3) + "</a> </li>";
                    }
                    if (currentPage > 2) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage - 2) + ")'>" + (currentPage - 2) + "</a> </li>";
                    }
                    if (currentPage > 1) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage - 1) + ")'>" + (currentPage - 1) + "</a> </li>";
                    }
                    html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + currentPage + ")'>" + currentPage + "</a> </li>";
                    if (totalPages > currentPage) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage + 1) + ")'>" + (currentPage + 1) + "</a> </li>";
                    }
                    if (totalPages > (currentPage + 1)) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage + 2) + ")'>" + (currentPage + 2) + "</a> </li>";
                    }
                    if (currentPage < 3 && totalPages > 4) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage + 3) + ")'>" + (currentPage + 3) + "</a> </li>";
                    }
                    if (currentPage < 2 && totalPages > 4) {
                        html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + (currentPage + 4) + ")'>" + (currentPage + 4) + "</a> </li>";
                    }

                    html += "<li> <a href='javascript:void(0)' onclick='showFileSource(" + totalPages + ")'> > </a> </li></ul></div></div>" +
                        "</div>";

                    $("#resourceChooseDiv").html(html);
                    $("#onlineUploadDiv").remove();
                    var timestamp = Date.parse(new Date());
                    var appendhtml = '<div class="form-group" id="onlineUploadDiv"><label class="control-label col-md-2"></label><label class="control-label col-md-1"><input type="button" onclick="selectFile();" class="btn green button-previous" id="selectFileButton" value="上传文件 + "></label>' +
                        '<div class="col-md-9"><input style="display:none;" id="fileupload" type="file" name="files[]" multiple > ' +
                        ' <table id="uploadedFilesTable" class="table table-striped table-hover table-bordered" fileDir="' + timestamp + '"> <thead>' +
                        '<tr> <th class="col-md-4">文件名称</th> <th class="col-md-1">大小</th>' +
                        '<th class="col-md-2">类型</th> <th class="col-md-4">描述</th> <th class="col-md-1">操作</th> <th>原始名称</th></tr>' +
                        '</thead> <tbody>' +
                        '</tbody> </table>' +
                        '</div><div><div class="col-md-3"></div><input class="col-md-9" type="text" class="form-control" name="filePath" id="filePath" style="display:none;"/></div></div>';
                    $("#resourceChooseDiv").after(appendhtml);


                    var uploadedFilesTable = $('#uploadedFilesTable');
                    var oTable = uploadedFilesTable.dataTable({
                        "searching": false,
                        "paging": false,
                        "info": false,
                        "language": {
                            "emptyTable": "无上传文件"
                        },
                        "columnDefs": [
                            {
                                "targets": [5],
                                "visible": false
                            }
                        ]
                    });
                    var nEditing = null;

                    uploadedFilesTable.on('click', '.deleteFileButton', function (e) {
                        e.preventDefault();

                        if (confirm("确定要删除改文件吗 ?") == false) {
                            return;
                        }

                        var nRow = $(this).parents('tr')[0];
                        oTable.fnDeleteRow(nRow);
                    });

                    uploadedFilesTable.on('click', '.cancelFileButton', function (e) {
                        e.preventDefault();
                        restoreRow(oTable, nEditing);
                        nEditing = null;
                    });

                    uploadedFilesTable.on('click', '.editFileButton', function (e) {
                        e.preventDefault();

                        /* Get the row as a parent of the link that was clicked on */
                        var nRow = $(this).parents('tr')[0];

                        if (nEditing !== null && nEditing != nRow) {
                            /* Currently editing - but not this row - restore the old before continuing to edit mode */
                            restoreRow(oTable, nEditing);
                            editRow(oTable, nRow);
                            nEditing = nRow;
                        } else if (nEditing == nRow && this.innerHTML == '<i class="glyphicon glyphicon-ok"></i>') {
                            /* Editing this row and want to save it */
                            saveRow(oTable, nEditing);
                            nEditing = null;
                        } else {
                            /* No edit in progress - let's start one */
                            editRow(oTable, nRow);
                            nEditing = nRow;
                        }
                    });


                    function restoreRow(oTable, nRow) {
                        var aData = oTable.fnGetData(nRow);
                        var jqTds = $('>td', nRow);

                        for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                            oTable.fnUpdate(aData[i], nRow, i, false);
                        }

                        oTable.fnDraw();
                    }

                    function editRow(oTable, nRow) {
                        var aData = oTable.fnGetData(nRow);
                        var jqTds = $('>td', nRow);
                        jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                        jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[3] + '">';
                        jqTds[4].innerHTML = '<a class="editFileButton" href=""><i class="glyphicon glyphicon-ok"></i></a>&nbsp;&nbsp;<a class="cancelFileButton" href=""><i class="fa fa-mail-reply"></i></a>';
                    }

                    function saveRow(oTable, nRow) {
                        var jqInputs = $('input', nRow);
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 3, false);
                        oTable.fnUpdate('<a class="editFileButton" href=""><i class="glyphicon glyphicon-pencil"></i></a>&nbsp;&nbsp;<a class="deleteFileButton" href=""><i class="glyphicon glyphicon-remove"></i></a>', nRow, 4, false);
                        oTable.fnDraw();
                    }

                    function cancelEditRow(oTable, nRow) {
                        var jqInputs = $('input', nRow);
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                        oTable.fnUpdate('<a class="editFileButton" href=""><i class="glyphicon glyphicon-pencil"></i></a>', nRow, 4, false);
                        oTable.fnDraw();
                    }


                    $(function () {
                        $('#fileupload').fileupload({
                            dataType: 'json',
                            url: ctx + '/fileupload',
                            method: 'POST',
                            formData: {fileDir: $("#uploadedFilesTable").attr("fileDir")},
//                            autoupload: false,
                            add: function (e, data) {
                                var continueFlag = true;
                                var file = data.files[0];
                                if (file.size > 30000000) {
                                    toastr["error"](file.name + "过大！", "上传文件大小不能超过30MB");
                                    continueFlag = false;
                                } else {
                                    var aiNew = oTable.fnAddData([file.name, file.size, file.type, '', '<a class="editFileButton" href=""><i class="glyphicon glyphicon-pencil"></i></a>&nbsp;&nbsp;<a class="deleteFileButton" href=""><i class="glyphicon glyphicon-remove"></i></a>', file.name]);
                                    $("#filePath")
                                        .closest('.form-group').removeClass('has-error');
                                    $("#filePath").next(".help-block").remove();

                                }
                                if (continueFlag == false) {
                                    return true;
                                }
                                data.submit();
                            },
                            done: function (e, data) {
                                $("#form_wizard_1").find(".button-save").removeAttr("disabled");
                            },

                        });

                    });

                },
            });
        }


        function chooseFile(dataSourceId, sourceName) {
            $("#dataSourceId").val(dataSourceId);
            var html = "<div class='form-group'> <label class='control-label col-md-3'>数据源名称 </label>" +
                "<label class='col-md-3' style='padding-top: 9px'>" + sourceName + " </label> </div>";
            html += "<div class='form-group'><label class='control-label col-md-3'>选择文件资源</label>" +
                "<div class='col-md-9'>" +
                "<div id='jstree' style='overflow:scroll; width:600px; height:400px;'></div>" +
                "<div id='fileDescribeDiv'><div>" +
                "</div></div>";
            $("#resourceChooseDiv").html(html);
            $('#jstree').jstree({
                'core': {
                    'data': function (node, cb) {
                        var children;
                        if (node.id == '#') {
                            children = initFileTree(dataSourceId);
                        } else {
                            children = getFileList(node.id, dataSourceId);
                        }
                        generateChildJson(children);
                        cb.call(this, children);
                    }
                },
                "plugins": [
                    "checkbox", "wholerow"
                ]
            }).bind('select_node.jstree', function (e, data) {
                var fileId = data.node.id;
                var fileName = data.node.text;
                var str = fileId.replace(/%_%/g, "/");
                var isContain = false;
                $("#fileDescribeDiv").find("input").each(function () {
                    if ($(this).attr("name") == fileId)
                        isContain = true;
                });
                if (isContain == false) {
                    $("#fileDescribeDiv").append('<div name="' + fileId + '"><div class="col-md-8"><div style="padding-top:5px">' + str + '&nbsp;文件描述</div><input name="' + fileId + '" class="form-control" style="margin:3px" type="text"></div><div class="col-md-4"></div></div>');
                    $("#filePath")
                        .closest('.form-group').removeClass('has-error');
                    $("#filePath").next(".help-block").remove();
                };
                $("#form_wizard_1").find(".button-save").removeAttr("disabled");
            }).bind("deselect_node.jstree", function (e, data) {
                var fileId = data.node.id;
                var fileName = data.node.text;
                $("div[name='" + fileId + "']").remove();
                $("#form_wizard_1").find(".button-save").removeAttr("disabled");
            });
        }
        function initFileTree(dataSourceId) {
            var root;
            $.ajax({
                type: "GET",
                url: '${ctx}/fileSourceFileList',
                data: {dataSourceId: dataSourceId},
                dataType: "json",
                async: false,
                success: function (data) {
                    root = data;
                }
            });
            return root;
        }

        function getFileList(folderPath, dataSourceId) {
            var children;
            $.ajax({
                url: "${ctx}/treeNode",
                type: "get",
                data: {'filePath': folderPath, 'dataSourceId': dataSourceId},
                dataType: "json",
                async: false,
                success: function (data) {
                    children = data;
                }
            });
            return children;
        }
        function generateChildJson(childArray) {
            for (var i = 0; i < childArray.length; i++) {
                var child = childArray[i];
                if (child.type == 'directory') {
                    child.children = true;
                    child.icon = "jstree-folder";
                } else {
                    child.icon = "jstree-file";
                }
            }
        }


        function saveDataResourcefunc() {

            //xiajl20171026
//            return true;

            /*$(".uploadButton").each(function(index,element){
             $(this).click();
             });*/
            $('input[name="choosefile"]').val("");

            var mapTableStr = "";


            $("#resourceChooseDiv").find("input[type='checkbox']").each(function () {
                if ($(this).attr("checked") != null) {
                    mapTableStr = mapTableStr + $(this).attr("checked");
                }
            });
            if (mapTableStr != "") {
                $("#maptableinput").val(mapTableStr);
            }
            var r = true;
            var validSql = true;
            $(":input[name^='sqlStr']").each(function () {
                if (!validSqlStr($(this).val(), $(this).attr("id"))) {
                    validSql = false;
                }
            });
            if (checkContentForm() && validSql) {
                if ($("#dataSourceId").val() == "") {
                    $("#dataSourceId").val("0");
                }
                var resource = {}, dataResource = {}, params = [], mappedTable = [], sqlStr = [], filePaths;
                var formData = $('#submit_form').serializeArray()
                for (var i = 0; i < formData.length; i++) {
                    if (formData[i].name == 'centerCatalogId') {
                        if($("#centerCheckbox").prop('checked')){
                            resource.centerCatalogId = formData[i].value;
                        }
                        continue;
                    } else if (formData[i].name == 'localCatalogId') {
                        if($("#localCheckbox").prop('checked')){
                            resource.localCatalogId = formData[i].value;
                        }
                        continue;
                    } else if (formData[i].name == 'license') {
                        resource.licId = formData[i].value;
                        continue;
                    } else if (formData[i].name == 'resState') {
                        resource.resState = formData[i].value;
                        continue;
                    } else if (formData[i].name == 'mapTable') {
                        continue;
                    } else if (formData[i].name == 'sqlStr') {
                        continue;
                    } else if (formData[i].name == 'filePath') {
                        filePaths = formData[i].value;
                        continue;
                    } else if (formData[i].name == 'resTitle') {
                        resource.resName = formData[i].value;
                        continue;
                    } else if (formData[i].name == 'dataSourceId') {
                        resource.dataSourceId = formData[i].value;
                        continue;
                    }
                    dataResource[formData[i].name] = formData[i].value;
                }
                $(".icheck-list ").find("input[type='checkbox'][name='mapTable']").each(function () {
                    if (this.checked) {
                        params.push({
                            "publicType": "table",
                            "publicContent": $(this).val(),
                            "fieldComs": $(this).attr("coms")
                        });
                    }
                });

                $(":input[name^='sqlStr']").each(function () {
                    if ($(this).val() && $(this).val().trim() != "") {
                        sqlStr.push({
                            "publicType": "sql",
                            "publicContent": $(this).val(),
                            "fieldComs": $(this).attr("coms")
                        });
                    }
                });
                if (dataResource.resourceType == '文件数据源') {
                    var filePathArray = filePaths.split(',');
                    for (var i = 0; i < filePathArray.length; i++) {
                        if (filePathArray[i]) {
                            var param = {};
                            param.filePath = filePathArray[i];
                            param.fileNotes = $("input[name='" + filePathArray[i] + "']").val();
                            param.publicType = "file";
                            params.push(param);
                        }
                    }

                    var dTable = $('#uploadedFilesTable').dataTable();
                    var nTrs = dTable.fnGetNodes();
                    for (var i = 0; i < nTrs.length; i++) {
                        var param = {};
                        param.filePath = dTable.fnGetData(nTrs[i])[0];
                        param.originalFilePath = dTable.fnGetData(nTrs[i])[5];
                        param.fileNotes = dTable.fnGetData(nTrs[i])[3];
                        param.fileTimestampDir = $("#uploadedFilesTable").attr("fileDir");
                        param.publicType = "file";
                        params.push(param);
                    }

                    if (params.length > 0) {
                        $('input[name="choosefile"]').val(params);
                    }
                } else {
                    for (var i = 0; i < mappedTable.length; i++) {
                        params.push(mappedTable[i]);
                    }
                    for (var i = 0; i < sqlStr.length; i++) {
                        params.push(sqlStr[i]);
                    }
                }
                resource.dataResourceStaticList = params;
                resource.resType = '静态数据';
                //插入数据
                var fieldComs = [];

                if ($("#resourceId").val())
                    resource.resourceId = $("#resourceId").val();
                var options = {
                    url: ctx + '/resource',
                    async: false,
                    dataType: "json",
                    data: {"resource": JSON.stringify(resource)},
                    beforeSend: function () {
                        Metronic.blockUI({
                            target: '#tab1',
                            boxed: true,
                            message: '保存中...'
                        });
                    },
                    success: function (data) {
                        if (data.head.code == 200) {
                            if ($('#uploadedFilesTable') != "" && $('#uploadedFilesTable') != undefined) {
                                var dTable = $('#uploadedFilesTable').dataTable();
                                var nTrs = dTable.fnGetNodes();
                                for (var i = 0; i < nTrs.length; i++) {
                                    var tempfilename = dTable.fnGetData(nTrs[i])[0];
                                    dTable.fnUpdate(tempfilename, nTrs[i], 5, false);
                                }
                            }
                            $("#resourceId").val(data.body.resourceId);
                            //xiajl20171105
                            //alert( "resourceId: " +$("#resourceId").val());
                            $("#form_wizard_1").find(".button-save").attr("disabled", true);
                            toastr["success"]("保存成功！", "数据保存");
                            r = true;
                        } else {
                            toastr["error"]("保存失败！", "数据保存");
                            r = false;
                        }


                    },
                    error: function () {
                        toastr["error"]("保存失败！", "数据保存");
                        r = false;
                    },
                    complete: function () {
                        Metronic.unblockUI("#tab1");
                    }
                };
                $('#submit_form').ajaxSubmit(options);
            } else {
                r = false;
                $("span[name='sqlfalse']").each(function () {
                    if ($(this).length > 0) {
                        $(this).attr("style", "");
                        $(this).html("sql语句验证失败");
                    }
                });
            }


            return r;
        }

        function selectFile() {
            $("#fileupload").click();
        }

    </script>
</div>
</html>
