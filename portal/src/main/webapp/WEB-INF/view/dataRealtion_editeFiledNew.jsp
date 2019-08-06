<%--
  Created by IntelliJ IDEA.
  User: shibaoping
  Date: 2018/10/30
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>

<html>

<head>
    <title>数据发布管理系统</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/jstree/dist/themes/default/style.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/dataConfig.css">
    <%--<link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-fileinput/css/bootstrap.min.css">--%>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/bundles/bootstrap-fileinput/css/fileinput.min.css">
    <link href="${ctx}/resources/bundles/layerJs/theme/default/layer.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctx}/resources/bundles/font-awesome-4.7.0/css/font-awesome.min.css">

    <style type="text/css">
        .qiehuan_div li a.active {
            background: #2a6ebf !important;
            margin-right: 1px !important;
            color: #fff !important;
        }

        .div_oneSel {
            /*margin-bottom: 10px;*/
        }

        .select_cass {
            font-size: 14px;
            font-weight: normal;
            color: #333333;
            background-color: white;
            border: 1px solid #e5e5e5;
            box-shadow: none;
            height: 34px;
            padding: 6px 12px;
            display: inline;
            width: 200px;
            margin: 5px 5px 5px 5px;
        }

        form label.regislabel {
            padding: 0px 0px 0px 30px;
            font-size: 10px;
            text-align: right;
            vertical-align: middle;
        }

        table {
            border-collapse: inherit !important;
        }

        table td {
            border-right: none !important;
            border-left: none !important;
            border-bottom: none !important;
        }

        .fa {
            font-size: 14px !important;
        }

    </style>
</head>

<body>

<div class="qiehuan_div">
    <ul id="tabDescribe">
        <li class="active" value="0"><a id="nodescribe" href="#undescribe" data-toggle="tab">待描述数据表</a></li>
        <li class="active" value="1"><a id="described" href="#isdescribe" data-toggle="tab">已描述数据表</a></li>
    </ul>
</div>
<div class="tab-content" style="background-color: white;">
    <div class="tab-pane active" id="undescribe" style="min-height: 400px;overflow: hidden">

    </div>

    <div class="tab-pane" id="isdescribe" style="min-height: 400px;overflow: hidden">

    </div>
    <%--数据配置页面--%>
    <div class="tab-pane" id="staticSourceTableChoiceModalNew"
         style="min-height: 500px;overflow: hidden;display: none;">
        <div style="min-height:500px;width:auto;max-width: 100%">
            <div>
                <div>
                    <h4 id="relationalDatabaseModalTitle">编辑表字段注释</h4>
                </div>
                <div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="portlet box green-haze" style="border:0;">
                                <div class="portlet-title" style="background-color: white;">
                                    <ul class="nav nav-tabs" style="width:100%;padding-left: 0%;margin-top: 0px;">
                                        <li class="active">
                                            <a href="#editTableFieldComsId" data-toggle="tab"
                                               id="editTableDataAndComsButtonId" aria-expanded="true">
                                                <button id="btn_eidt" type="button" class="btn btn-primary">
                                                    编辑
                                                </button>
                                            </a>
                                        </li>
                                        <li class="" style="padding-left: 0px;">
                                            <a href="#previewTableDataAndComsId"
                                               id="previewTableDataAndComsButtonId"
                                               data-toggle="tab" aria-expanded="false">
                                                <button id="btn_check" type="button" class="btn btn-default">
                                                    预览
                                                </button>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                                <div class="tab-content"
                                     style="background-color: white;max-height:70%;max-width:100%; ">
                                    <div>
                                        <label>表名：</label><span id="span_tableName"></span> <label
                                            style="margin-left: 30px;"> <label style="color:red;">*</label>
                                        备注：</label><input type="text" value="" name="tableComment" id="tableComment"
                                                          class="form-control" style="display: inline;width: 180px;">
                                    </div>
                                    <div class="tab-pane active" id="editTableFieldComsId"
                                         style="width:99%;max-height:70%;overflow: auto;">
                                    </div>

                                    <div class="tab-pane" id="previewTableDataAndComsId"
                                         style="width:99%;max-height:70%;overflow: auto;">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="text-align: center">
                    <button type="button" class="btn btn-success" id="editTableFieldComsSaveId" data-dismiss="modal"
                            onclick="fun_saveTbaleFields()">保存
                    </button>
                    <button type="button" data-dismiss="modal" class="btn default" id="editTableFieldComsCloseId"
                            onclick="func_cancelBtn()">返回
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<%--弹窗--%>
<div id="showTypeDataModal" class="modal fade" tabindex="-1" data-width="200" style="position:fixed;">
    <div class="modal-dialog" style="height:600px;width:auto;max-width: 40%">
        <div class="modal-content">
            <div class="modal-header">
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true"--%>
                <%--id="showTypeDataModalClosedId"></button>--%>
                <h4 class="modal-title" id="showTypeDataModalTitle">显示类型</h4>
            </div>
            <div class="modal-body" style="width: 100%">
                <div class="row">
                    <div class="col-md-12">
                        <div class="portlet box green-haze" style="border:0;">
                            <div class="tab-content" style="background-color: white;max-height:70%;max-width:100%; ">
                                <div class="tab-pane active" id="showTypeDataDetail"
                                     style="width:99%;max-height:70%;overflow: hidden;overflow-y: auto">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<input type="hidden" id="subjectCode" value="${sessionScope.SubjectCode}"/>
<input type="hidden" id="FtpFilePath" value="${sessionScope.FtpFilePath}"/>

<script type="text/html" id="addSheetTable">
    <div id="div_addID" style="margin-left: 168px;">
        <select name="relationTable" class="relationTable select_cass">
            <option value="null" name="">--请选择表名--</option>
            {{each list as value i}}
            <option value="{{value}}">{{value}}</option>
            {{/each}}
        </select>
        <select name="relationColumn" class="relationColumn select_cass">
            <option value="null" name="">--请选择列名--</option>
        </select>
        <span id="remove_id"><i class="fa fa-trash" onclick="func_removeSheetDate(this)"></i></span>
    </div>
</script>

<script type="text/html" id="templTypeURL">
    <div id="model_URL" style="">
        <form id="form_URL" class="form-horizontal">
            <input type="text" name="DisplayType" value="1" style="display: none">
            {{each listData as value}}
            <input type="text" name="tableName" value="{{value.tableName}}" style="display: none">
            <input type="text" name="columnName" value="{{value.columnName}}" style="display: none">
            <input type="text" name="tableComment" value="{{value.clumnCommet}}" style="display: none">
            <div class="form-group">
                <label class="col-sm-3 control-label regislabel">字段标题:</label>
                <div class="col-sm-8" style="margin-top: 8px">
                    {{value.columnName}}
                    {{if value.clumnCommet !="" && value.clumnCommet!=null }}
                ({{value.clumnCommet}})
                {{/if}}
                {{/each}}
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label regislabel">选项模式:</label>
                <div class="col-sm-8" style="margin-top: 8px">
                    <input type="radio" value="1" checked="checked" name="optionMode"/><label>网页链接</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" value="2" name="optionMode"/><label>邮箱链接</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" value="3" name="optionMode"/><label>FTP链接</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" value="4" name="optionMode"/><label>图片类型</label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label regislabel">缺省值:</label>
                <div class="col-sm-8">
                    <input type="text" name="address" id="url_id" class="form-control">
                </div>
                <br/>
            </div>
            <div class="modal-footer">
                <input type="button" onclick="func_saveTypeURL()" value="确定" class="btn btn-success"
                       data-dismiss="modal">
                <input type="button" value="取消" class="btn btn-success" data-dismiss="modal" onclick="cancel_btn();">
            </div>
        </form>

    </div>
</script>

<script type="text/html" id="templTypeEnum">
    <div id="model_enum" style="">
        <form id="form_Enum" class="form-horizontal">
            <input type="text" name="DisplayType" value="2" style="display: none">
            {{each listData as value}}
            <input type="text" name="tableName" value="{{value.tableName}}" style="display: none">
            <input type="text" name="columnName" value="{{value.columnName}}" style="display: none">
            <input type="text" name="tableComment" value="{{value.clumnCommet}}" style="display: none">
            <div class="form-group">
                <label class="col-sm-3 control-label regislabel">字段标题:</label>
                <div class="col-sm-8" style="margin-top: 8px">{{value.columnName}}
                    {{if value.clumnCommet!="" && value.clumnCommet!=null }}
                ({{value.clumnCommet}})
                {{/if}}
                {{/each}}
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label regislabel">选项模式:</label>
                <div class="col-sm-8" style="margin-top: 8px">
                    <input type="radio" value="1" name="optionMode" checked="checked"
                           onclick="selectTypeEnum(this.value)"/>来自文本串 &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="radio" value="2" name="optionMode" onclick="selectTypeEnum(this.value)"/>来自sql表字段
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label regislabel">选项:</label>
                <div class="col-sm-8">
                    <input id="enum_text" name="enumData" type="text" class="form-control"
                           placeholder="各选项采用“值=标题”的格式，选项之间采用逗号分隔，例如：male=男,female=女">
                    <input id="enum_sql" type="text" name="address" class="form-control" style="display: none"
                           placeholder="输入一个sql语句，查询出两个字段分别为值和标题，如：select value,title from yourTable">
                </div>
            </div>
            <div class="modal-footer">
                <input type="button" onclick="func_saveTypeEnum()" value="确定" class="btn btn-success"
                       data-dismiss="modal">
                <input type="button" value="取消" class="btn btn-success" data-dismiss="modal" onclick="cancel_btn();">
            </div>
        </form>
    </div>
</script>

<script type="text/html" id="templTypeDatasheet">
    <div id="model_Datasheet" style="">
        <div>
            <form id="form_DataSheet" class="form-horizontal">
                <input type="text" id="DisplayType" name="DisplayType" value="3" style="display: none">
                {{each listData as value}}
                <input type="text" id="sheetDataTable1" name="tableName" value="{{value.tableName}}"
                       style="display: none">
                <input type="text" id="sheetDatacolumn1" name="columnName" value="{{value.columnName}}"
                       style="display: none">
                <input type="text" name="tableComment" value="{{value.clumnCommet}}" style="display: none">
                <div class="form-group">
                    <label class="col-sm-3 control-label regislabel">字段标题:</label>
                    <div class="col-sm-8" style="margin-top: 8px">{{value.columnName}}
                        {{if value.clumnCommet!="" && value.clumnCommet!=null }}
                    ({{value.clumnCommet}})
                    {{/if}}
                {{/each}}
                    </div>
                </div>
                <div id="exit_div" style="display:none;" class="form-group">
                    <label class="col-sm-3 control-label regislabel">已关联表</label>
                    <div id="exit_table" style="margin-left: 185px;margin-bottom: 20px">
                    </div>
                    <div id="addTable_id_edit" onclick="func_addSheetTable()"
                         style="display: none;margin-left: 550px;margin-bottom: 20px;"><a><i
                            class="fa fa-plus"></i>增加</a></div>
                </div>
                <div id="FirstSheetId" class="form-group">
                    <label class="col-sm-3 control-label regislabel">关联字段:</label>
                    <div class="form-group">
                    <select id="checkedDataSheetTable" name="relationTable" class="relationTable select_cass">
                        <option value="null" name="">--请选择表名--</option>
                        {{each list as value i}}
                        <option value="{{value}}">{{value}}</option>
                        {{/each}}
                    </select>
                        <select id="tableColunm_id" name="relationColumn" class="form-control select_cass">
                        <option value="null" name="">--请选择列名--</option>
                    </select>
                        <span id="addTable_id" onclick="func_addSheetTable()"><a><i class="fa fa-plus"></i>增加</a></span>
                    </div>
                </div>
                <div id="addTableList" style="margin-top: -30px;">

                </div>
                <div class="modal-footer">
                    <input type="button" onclick="func_saveTypeDatasheet()" value="确定" class="btn btn-success"
                           data-dismiss="modal">
                    <input type="button" value="取消" class="btn btn-success" data-dismiss="modal" onclick="cancel_btn();">
                </div>
            </form>

        </div>
    </div>
</script>

<script type="text/html" id="templTypeFile">
    <div id="model_file" style="">
        <div>
            <form id="form_File" class="form-horizontal">
                <input type="text" name="DisplayType" value="4" style="display: none">
                {{each listData as value}}
                <input type="text" name="tableName" value="{{value.tableName}}" style="display: none">
                <input type="text" name="columnName" value="{{value.columnName}}" style="display: none">
                <input type="text" name="tableComment" value="{{value.clumnCommet}}" style="display: none">
                <div class="form-group">
                    <label class="col-sm-3 control-label regislabel">字段标题:</label>
                    <div class="col-sm-8" style="margin-top: 8px">{{value.columnName}}
                        {{if value.clumnCommet!="" && value.clumnCommet!=null }}
                    ({{value.clumnCommet}})
                    {{/if}}
                    {{/each}}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label regislabel">显示类型:</label>
                    <div class="col-sm-8" style="margin-top: 8px">
                        <input type="radio" value="1" name="optionMode" checked="checked"/>附件列表 &nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" value="2" name="optionMode"/>图片列表 &nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" value="3" name="optionMode"/>视频列表
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label regislabel">主路径:</label>
                    <div class="col-sm-8">
                        {{each listData as value}}
                        <input id="main_address" type="text" name="address" class="form-control"
                               value="{{value.filePath}}"/>
                        {{/each}}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label regislabel">分隔符:</label>
                    <div class="col-sm-8">
                        <select id="separator_id" name="Separator" class="form-control">
                            <option value=";" name="">;</option>
                            <option value=",">,</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="button" onclick="func_saveTypeFile()" value="确定" class="btn btn-success"
                           data-dismiss="modal">
                    <input type="button" value="取消" class="btn btn-success" data-dismiss="modal"
                           onclick="cancel_btn();">
                </div>
            </form>

        </div>
    </div>
</script>

<%@ include file="./tableFieldComsTmpl.jsp" %>
</body>
<%--为了加快页面加载速度，请把js文件放到这个div里--%>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/artTemplate/template.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/jquery-validation/js/jquery.validate.min.js"></script>
    <script type="text/javascript"
            src="${ctx}/resources/bundles/jquery-validation/js/additional-methods.min.js"></script>
    <script src="${ctx}/resources/js/jquery.json.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/select2/select2.min.js"></script>
    <script src="${ctx}/resources/bundles/jstree/dist/jstree.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-fileinput/js/fileinput.min.js"></script>

    <script src="${ctx}/resources/bundles/layerJs/layer.js"></script>
    <script src="${ctx}/resources/bundles/context/context.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-closable-tab/bootstrap-closable-tab.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/dataRegisterEditTableFieldComs.js"></script>
    <%--
        <script src="${ctx}/resources/js/metaTemplate.js"></script>
    --%>
    <script>
        var ctx = '${ctx}', edit = false;
        var sub = '${sessionScope.SubjectCode}';
        var filePath = "${filePath}";
        $(function () {
            chooseTable(sub, 0);
            $(".qiehuan_div li:eq(0) a").addClass("active");
        });

        var sub1 = '${sessionScope.SubjectCode}';
        $("#tabDescribe li").click(function () {
            var flag = $(this).val();
            $(this).siblings().removeClass("active");
            $(this).siblings().find("a").removeClass("active");
            $(this).addClass("active");
            $(this).find("a").addClass("active");
            chooseTable(sub1, flag);
        });
        $("#described").click(function () {
            $("#undescribe").hide();
            $("#isdescribe").show();
            $("#staticSourceTableChoiceModalNew").hide();
        });
        $("#nodescribe").click(function () {
            $("#isdescribe").hide();
            $("#undescribe").show();
            $("#staticSourceTableChoiceModalNew").hide();
        });

        function chooseTable(subjectCode, flag) {
            $.ajax({
                type: "GET",
                url: '${ctx}/relationalDatabaseTableList',
                data: {"subjectCode": subjectCode, "flag": flag},
                dataType: "json",
                success: function (data) {
                    var html = "<div class='form-group'>" +
                        "<div class='col-md-12'>" +
                        "<div class='icheck-list' style='padding-top: 7px;width: 100%;'>";
                    var list = data.list;
                    for (var i = 0; i < list.length; i++) {
                        html += "<span style='width:33%;height:40px;padding-left: 0px;font-size: 15px' class='col-md-6'>" +
                            "<input hidden type='radio' id='" + list[i] + "' name='mapTable'  value='" + list[i] + "'><label for='" + list[i] + "'> <a href='#' onclick=\"staticSourceTableChoice_edit(1,this" + ",'" + sub1 + "','" + list[i] + "','dataResource','" + flag + "')\">" + list[i] + "</a></label></span>"
                    }
                    html += "</div><input type='text' class='form-control' name='maptableinput' id='maptableinput' style='display:none;'/></div></div>";
                    if (flag == '0') {
                        $("#undescribe").html(html);
                    } else {
                        $("#isdescribe").html(html);
                    }
                }
            });
        }

        $("#btn_eidt").click(function () {
            $("#btn_check").removeClass("btn btn-primary");
            $("#btn_eidt").removeClass("btn btn-default");

            $("#btn_eidt").addClass("btn btn-primary");
            $("#btn_check").addClass("btn btn-default");

        });

        $("#btn_check").click(function () {
            $("#btn_check").removeClass("btn btn-default");
            $("#btn_eidt").removeClass("btn btn-primary");

            $("#btn_check").addClass("btn btn-primary");
            $("#btn_eidt").addClass("btn btn-default");
        });


        //    显示类型
        $("body").delegate("select", "change", function () {
            var _this = this;
            $(_this).addClass('selShow');
            var myOption = $(_this).find('option:selected').attr('on');
            var tableName = $(_this).parent().siblings().attr("tableName");
            var columnName = $(_this).parent().siblings().attr("fieldName");
            var clumnCommet = $(_this).parent().siblings().eq(1).find("input").val();
            var data = {};
            data["columnName"] = columnName;
            data["tableName"] = tableName;
            data["clumnCommet"] = clumnCommet;
            data["filePath"] = filePath;
            var listData = [];
            listData.push(data);
            typeTemplate(myOption, listData, columnName);
        });

        function typeTemplate(myOption, listData, columnName) {
            if (myOption === '1') {

            }
            if (myOption === '2') {
                $("#showTypeDataDetail").empty();
                $('#showTypeDataModal').modal({backdrop: "static"});
                var html = template("templTypeURL", {"listData": listData});
                $("#showTypeDataDetail").append(html);

            }
            if (myOption === '3') {
                $("#showTypeDataDetail").empty();
                // $("#showTypeDataModal").modal("show");
                $('#showTypeDataModal').modal({backdrop: "static"});
                var html = template("templTypeEnum", {"listData": listData});
                $("#showTypeDataDetail").append(html);

            }
            if (myOption === '4') {
                var tableName = "";
                if ($(".fieldComsKey")) {
                    $(".fieldComsKey").each(function (index, value, array) {
                        tableName = $(value).attr("tablename");
                    })
                }
                getSheetTable(tableName, columnName, listData);

            }
            if (myOption === '5') {
                $("#showTypeDataDetail").empty();
                $('#showTypeDataModal').modal({backdrop: "static"});
                var html = template("templTypeFile", {"listData": listData});
                $("#showTypeDataDetail").append(html);
            }
        }

        //关联数据表
        function getSheetTable(tableName, columnName, listData) {
            $("#showTypeDataDetail").empty();
            $('#showTypeDataModal').modal({backdrop: "static"});
            $.ajax({
                type: "post",
                url: "${ctx}/getDatasheetTable",
                data: {"tableName": tableName, "subjectCode": sub, "columnName": columnName},
                dataType: "json",
                async: false,
                success: function (data) {
                    data["listData"] = listData;
                    var html = template("templTypeDatasheet", data);
                    $("#showTypeDataDetail").append(html);
                    $("#checkedDataSheetTable").on("change", function () {
                        var tName = $("#checkedDataSheetTable option:selected").val();//选中的值

                        //级联查询表字段
                        $.ajax({
                            type: "post",
                            url: "${ctx}/getDatasheetTableColumn",
                            data: {"tableName": tName, "subjectCode": sub},
                            dataType: "json",
                            success: function (data) {
                                $("#tableColunm_id").html("");
                                var column = data.list;
                                var s = "";
                                for (var i = 0; i < column.length; i++) {
                                    if (column[i] === "PORTALID") {

                                    } else {
                                        s += "<option value='" + column[i] + "'>" + column[i] + "</option>"
                                    }
                                }
                                $("#tableColunm_id").append(s);
                            }
                        })

                    })
                }
            });
        }

        function cancel_btn() {
            $('.selShow').find('option').eq(0).attr("selected", true);
            $('.selShow').removeClass("selShow");
        }

        //    保存URL显示类型
        function func_saveTypeURL() {
            $('.selShow').removeClass("selShow");
            var data = $("#form_URL").serialize();
            $.ajax({
                type: "post",
                url: "${ctx}/saveTypeURL",
                data: data,
                dataType: "json",
                success: function (data) {
                    toastr.success("保存成功！");
                }
            });
        }

        //字典枚举类型保存
        function func_saveTypeEnum() {
            var data = $("#form_Enum").serialize();
            $.ajax({
                type: "post",
                url: "${ctx}/saveTypeEnum",
                data: data,
                dataType: "json",
                success: function (data) {
                    toastr.success("保存成功！");
                }
            });
        }

        //枚举字典
        function selectTypeEnum(value) {
            if (value === "1") {
                $("#enum_sql").hide();
                $("#enum_text").show();
            }
            if (value === "2") {
                $("#enum_sql").show();
                $("#enum_text").hide();
            }
        }

        //    点击最外层取消按钮
        function func_cancelBtn() {
            var tableName = "";
            if ($(".fieldComsKey")) {
                $(".fieldComsKey").each(function (index, value, array) {
                    tableName = $(value).attr("tablename");
                })
            }
            $.ajax({
                type: "post",
                url: "${ctx}/deleteStatusTwo",
                data: {"tableName": tableName},
                dataType: "json",
                success: function (data) {
                    // toastr.success("保存成功！");
                }
            });
            if ($(".qiehuan_div li:eq(0) a").hasClass("active")) {
                $("#isdescribe").hide();
                $("#undescribe").show();
                $(".qiehuan_div").show();
                $("#staticSourceTableChoiceModalNew").hide();
            } else {
                $("#isdescribe").show();
                $("#undescribe").hide();
                $(".qiehuan_div").show();
                $("#staticSourceTableChoiceModalNew").hide();
            }

        }

        //    关联数据表
        function func_saveTypeDatasheet() {
            debugger;
            var tName = $("#checkedDataSheetTable option:selected").val();
            var tColumn = $("#tableColunm_id option:selected").val();
            if (tName === "null") {
                toastr.warning("请选择表名！");
                return;
            }
            if (tColumn === "null") {
                toastr.warning("请选择列名！");
                return;
            }
            if (tName !== "null" && tColumn !== "null") {
                var datas = [];
                var tables = [];
                var columns = [];
                $("#form_DataSheet select").each(function () {
                    datas.push($(this).val());
                });
                for (var i = 0; i < datas.length; i++) {
                    if (i % 2 === 0) {
                        if (datas[i] === null || datas[i] === "null") {
                            toastr.warning("请选择表名！");
                            return;
                        } else {
                            tables.push(datas[i]);
                        }
                    } else {
                        if (datas[i] === null || datas[i] === "null") {
                            toastr.warning("请选择列明名！");
                            return;
                        } else {
                            columns.push(datas[i]);
                        }
                    }
                }
                var ary = tables;
                var nary = ary.sort();
                for (var i = 0; i < ary.length; i++) {
                    if (nary[i] == nary[i + 1]) {
                        toastr.warning("表名不能重复选择！");
                        return;
                    }
                }

                var DisplayType = $("#DisplayType").val();
                var tableName = $("#sheetDataTable1").val();
                var columnName = $("#sheetDatacolumn1").val();

                $.ajax({
                    type: "post",
                    url: "${ctx}/saveTypeDatasheet",
                    data: {
                        "DisplayType": DisplayType,
                        "tableName": tableName,
                        "columnName": columnName,
                        "tables": tables.join(","),
                        "columns": columns.join(",")
                    },
                    dataType: "json",
                    success: function (data) {
                        toastr.success("保存成功！");
                    }
                });
            }
        }

        //    文件类型
        function func_saveTypeFile() {
            var data = $("#form_File").serialize();
            $.ajax({
                type: "post",
                url: "${ctx}/saveTypeFile",
                data: data,
                dataType: "json",
                success: function (data) {
                    toastr.success("保存成功！");
                }
            });
        }

        //    查询出该表关联的数据表
        function getDatasheetTable() {
            var tableName = "";
            if ($(".fieldComsKey")) {
                $(".fieldComsKey").each(function (index, value, array) {
                    tableName = $(value).attr("tablename");
                })
            }
            $.ajax({
                type: "post",
                url: "${ctx}/getDatasheetTable",
                data: {"tableName": tableName, "subjectCode": sub},
                dataType: "json",
                success: function (data) {
                    return data.list;
                }
            });
        }

        //增加多个关联表字段
        function func_addSheetTable() {
            debugger
            if ($("#checkedDataSheetTable").val() !== "null" && $("#tableColunm_id").val() !== "null") {

                var tables = [];
                $("select.relationTable").each(function () {
                    tables.push($(this).val());
                });

                var tableName = $("#sheetDataTable1").val();
                var columnName = $("#sheetDatacolumn1").val();
                var datas = [];
                $("#form_DataSheet select").each(function () {
                    datas.push($(this).val());
                });
                for (var i = 0; i < datas.length; i++) {
                    if (i % 2 === 0) {
                        if (datas[i] === null || datas[i] === "null") {
                            toastr.warning("请选择表名！");
                            return;
                        }
                    } else {
                        if (datas[i] === null || datas[i] === "null") {
                            toastr.warning("请选择列明名！");
                            return;
                        }
                    }
                }

                $.ajax({
                    type: "post",
                    url: "${ctx}/getDatasheetTable",
                    data: {"tableName": tableName, "subjectCode": sub, "columnName": columnName},
                    dataType: "json",
                    success: function (data) {
                        var html = template("addSheetTable", data);
                        $("#addTableList").append(html);
                        $("#div_addID .relationTable").on("change", function () {
                            var _this = this;
                            var tName = $(_this).find('option:selected').text();//选中的值
                            //级联查询表字段
                            $.ajax({
                                type: "post",
                                url: "${ctx}/getDatasheetTableColumn",
                                data: {"tableName": tName, "subjectCode": sub},
                                dataType: "json",
                                success: function (data) {
                                    $(_this).parent().find('.relationColumn').html("");
                                    var column = data.list;
                                    var s = "";
                                    for (var i = 0; i < column.length; i++) {
                                        if (column[i] === "PORTALID") {

                                        } else {
                                            s += "<option value='" + column[i] + "'>" + column[i] + "</option>"
                                        }
                                    }
                                    $(_this).parent().find('.relationColumn').append(s);
                                }
                            })

                        })
                    }
                });
            } else {
                toastr.warning("请填写完整再增加关联表！");
            }

        }

        //    删除关联表字段
        function func_removeSheetDate(i) {
            $(i).parent().parent().remove();
        }

        //回显数据
        function getshowTypeData(tableName) {
            $("#tableComment").val("");
            $.ajax({
                type: "post",
                url: "${ctx}/getShowTypeInfMsg",
                data: {"tableName": tableName, "subjectCode": curDataSubjectCode},
                dataType: "json",
                success: function (data) {
                    if (data.alert === 1) {
                        var showTypeInfmsg = data.showTypeInfmsg;
                        $("#tableComment").val(showTypeInfmsg.tableComment);
                        var showTypeDetailList = data.showTypeInfmsg.showTypeDetailList;

                        if ($(".fieldComsKey")) {
                            $(".fieldComsKey").each(function (index, value, array) {
                                var columnName = $(value).attr("fieldName");
                                var columnComment = $($(".fieldComsValue")[index]).val();

                                for (var i = 0; i < showTypeDetailList.length; i++) {
                                    if (showTypeDetailList[i].columnName === columnName && showTypeDetailList[i].status === 1) {
                                        var type = showTypeDetailList[i].type + 1;
                                        if (showTypeDetailList[i].type !== 0) {
                                            var s = "<button onclick=\" func_checkTypeDetail('" + columnName + "','" + showTypeDetailList[i].type + "','" + columnComment + "')\" class='btn default'>详情</button>";
                                            $("#" + columnName).append(s);
                                            $("#" + columnName).show();
                                        } else {
                                            $("#" + columnName).hide();
                                        }
                                        $($(".sel")[index]).val(type);
                                    }
                                }

                            });
                        }
                    }
                }
            });
        }

        // 回显数据
        function func_checkTypeDetail(columnName, type, columnComment) {
            var tableName = $("#span_tableName").html();
            var data = {};
            data["columnName"] = columnName;
            data["tableName"] = tableName;
            data["clumnCommet"] = columnComment;
            var listData = [];
            listData.push(data);
            $.ajax({
                type: "post",
                url: "${ctx}/getShowTypeInfMsg",
                data: {"tableName": tableName, "subjectCode": sub},
                dataType: "json",
                async: false,
                success: function (data) {
                    var showTypeInfmsg = data.showTypeInfmsg;
                    var showTypeDetailList = data.showTypeInfmsg.showTypeDetailList;
                    for (var i = 0; i < showTypeDetailList.length; i++) {
                        if (showTypeDetailList[i].columnName === columnName && showTypeDetailList[i].status === 1) {
                            var option = '';
                            option = showTypeDetailList[i].type + 1 + '';
                            if (showTypeDetailList[i].type === 1) {
                                typeTemplate(option, listData, columnName);
                                if (showTypeDetailList[i].optionMode === "1") {
                                    // $("#selectSite option[value='1']").attr("selected", "selected");
                                    $('input:radio[name=optionMode]')[0].checked = true;
                                }
                                if (showTypeDetailList[i].optionMode === "2") {
                                    $('input:radio[name=optionMode]')[1].checked = true;
                                }
                                if (showTypeDetailList[i].optionMode === "3") {
                                    $('input:radio[name=optionMode]')[2].checked = true;
                                }
                                if (showTypeDetailList[i].optionMode === "4") {
                                    $('input:radio[name=optionMode]')[3].checked = true;
                                }
                                $("#url_id").val(showTypeDetailList[i].address);
                            }
                            if (showTypeDetailList[i].type === 2) {
                                typeTemplate(option, listData, columnName);
                                if (showTypeDetailList[i].optionMode === "1") {
                                    $('input:radio[name=optionMode]')[0].checked = true;
                                    var enmus = showTypeDetailList[i].enumData;
                                    var s = "";
                                    for (var k = 0; k < enmus.length; k++) {
                                        s += enmus[k].key + "=" + enmus[k].value + ","
                                    }
                                    s = s.substr(0, s.length - 1);
                                    $("#enum_text").val(s);
                                }
                                if (showTypeDetailList[i].optionMode === "2") {
                                    $('input:radio[name=optionMode]')[1].checked = true;
                                    var s = "select " + showTypeDetailList[i].relationColumnK + "," + showTypeDetailList[i].relationColumnV + " from " + showTypeDetailList[i].relationTable;
                                    $("#enum_sql").show();
                                    $("#enum_text").hide();
                                    $("#enum_sql").val(s);
                                }
                            }

                            if (showTypeDetailList[i].type === 3) {
                                // getSheetTable(tableName, columnName, listData);
                                typeTemplate(option, listData, columnName);
                                debugger;
                                var enmus = showTypeDetailList[i].enumData;
                                var sheetTable = new Array();
                                $("#exit_table").html("");
                                if (enmus !== null || enmus !== "") {
                                    $.ajax({
                                        type: "post",
                                        url: "${ctx}/getDatasheetTable",
                                        data: {"tableName": tableName, "subjectCode": sub, "columnName": columnName},
                                        dataType: "json",
                                        async: false,
                                        success: function (data) {
                                            sheetTable = data.list;
                                        }
                                    });
                                    var ss = "";
                                    for (var k = 0; k < enmus.length; k++) {
                                        var s_Table = enmus[k].key;
                                        var s_columnN = enmus[k].value;
                                        var s_columns = new Array();
                                        $.ajax({
                                            type: "post",
                                            url: "${ctx}/getDatasheetTableColumn",
                                            data: {"tableName": s_Table, "subjectCode": sub},
                                            dataType: "json",
                                            async: false,
                                            success: function (data) {
                                                s_columns = data.list;
                                            }
                                        });

                                        ss = "<div class='div_oneSel'><select class='select_cass'>";
                                        for (var j = 0; j < sheetTable.length; j++) {
                                            if (s_Table === sheetTable[j]) {
                                                ss += "<option selected>" + sheetTable[j] + "</option>"
                                            } else {
                                                ss += "<option>" + sheetTable[j] + "</option>"
                                            }
                                        }
                                        ss += "</select><select class='select_cass relationColumn'>";
                                        for (var ii = 0; ii < s_columns.length; ii++) {
                                            if (s_columns[ii] === "PORTALID") {
                                            }
                                            if (s_columnN === s_columns[ii]) {
                                                ss += "<option selected>" + s_columns[ii] + "</option>"
                                            } else {
                                                ss += "<option>" + s_columns[ii] + "</option>"
                                            }
                                        }
                                        ss += "</select><span id='remove_id'><i class='fa fa-trash' onclick=\"func_removeSheetDate(this)\"></i></span></div>";
                                        $("#exit_table").append(ss);
                                    }
                                    $("#addTable_id_edit").show();
                                    $("#exit_div").show();
                                    $("#FirstSheetId").remove();
                                }
                                //    重新选择关联表
                                $("#exit_div #exit_table .select_cass").on("change", function () {
                                    var _this = this;
                                    var tName = $(_this).find('option:selected').text();//选中的值
                                    //级联查询表字段
                                    $.ajax({
                                        type: "post",
                                        url: "${ctx}/getDatasheetTableColumn",
                                        data: {"tableName": tName, "subjectCode": sub},
                                        dataType: "json",
                                        success: function (data) {
                                            $(_this).parent().find('.relationColumn').html("");
                                            var column = data.list;
                                            var s = "";
                                            for (var i = 0; i < column.length; i++) {
                                                if (column[i] === "PORTALID") {

                                                } else {
                                                    s += "<option value='" + column[i] + "'>" + column[i] + "</option>"
                                                }
                                            }
                                            $(_this).parent().find('.relationColumn').append(s);
                                        }
                                    })
                                });
                            }
                            if (showTypeDetailList[i].type === 4) {
                                typeTemplate(option, listData, columnName);
                                if (showTypeDetailList[i].optionMode === "1") {
                                    $('input:radio[name=optionMode]')[0].checked = true;
                                }
                                if (showTypeDetailList[i].optionMode === "2") {
                                    $('input:radio[name=optionMode]')[1].checked = true;
                                }
                                if (showTypeDetailList[i].optionMode === "3") {
                                    $('input:radio[name=optionMode]')[2].checked = true;
                                }
                                $("#main_address").val(showTypeDetailList[i].address);
                                $("#separator_id").val(showTypeDetailList[i].separator);
                            }
                        }
                    }
                }
            });
        }
    </script>
</div>

</html>

