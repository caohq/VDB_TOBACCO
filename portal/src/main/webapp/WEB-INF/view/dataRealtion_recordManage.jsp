<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2019/3/7
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>数据记录管理</title>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.css" rel="stylesheet" type="text/css"/>
    <%--<link rel="Stylesheet" href="${ctx}/resources/css/common.css"/>--%>
    <%--<link rel="Stylesheet" href="${ctx}/resources/css/jquery.jerichotab.css"/>--%>
    <link rel="stylesheet" href="${ctx}/resources/bundles/font-awesome-4.7.0/css/font-awesome.min.css">
    <link href="${ctx}/resources/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/css/home.css" type="text/css"/>
    <link href="${ctx}/resources/bundles/timepicker/jquery.timepicker.css" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-datepicker/css/datepicker.css"/>
    <link href="${ctx}/resources/css/timeStyle.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        /*超出隐藏*/
        #content_id table{
            table-layout: fixed;
        }
        #content_id table tr td{
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
        }

        #ul_div111 li.active a {
            background: #2a6ebf !important;
            color: #fff !important;
        }

        #ul_div111 li a {
            padding-left: 40px;
            padding-right: 40px;
            color: #406ec5;
            border: blue solid 1px;
            margin-right: 1px;
        }

        #ul_div111 li {
            background-color: #FFFFff;
        }

        .input-group {
            position: relative;
            display: table;
            border-collapse: separate;
            width: 12%;
            height: 30px;
        }

        .input-group .form-control {
            display: table-cell;
        }

        .input-group .input-group-btn button {
            background: #4183ce;
            border: none;
        }

        .input-group input {
            border: none;
            border-radius: 5px;
        }

        .fa {
            font-size: 14px !important;
        }

    </style>

</head>
<body>
<div style="">


    <!-- 此处是相关代码 -->
    <div style="float: left; width: 80%;overflow: hidden;">
        <ul style="" id="ul_div111" class="nav nav-tabs activeTabs" role="tablist">

        </ul>
    </div>
    <div class="input-group" style="float: left;margin-left: 1%;">
        <input type="text" name="searchKey" class="form-control" placeholder="输入关键词" value=""/>
        <span class="input-group-btn">
                    <button type="button" id="btnSearch" onclick="searchTableData(this)" style="width: 31px;height: 34px"><img
                            src="${ctx}/resources/img/ico06.png"     style="width: 20px; height: 21px;"></button>
                </span>
    </div>
    <div id="btn_addTableData" style="float:right;width:5%;margin-right: 1%;display: none;">
        <button class="btn btn-primary" type="button"  onclick="add_function()"><a href="#" style="color: white;"><i
                class="fa fa-plus"></i>增加</a></button>
    </div>

    <div id="content_id" class="tab-content activeTabs" style="min-height:500px;">

    </div>
    <%--新增表数据--%>
    <div id="staticAddData" class="modal fade" tabindex="-1" data-width="200editTableFieldComsId">
        <div class="modal-dialog" style="width:600px;width:auto;max-width: 50%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                            id="addTableData"></button>
                    <h4 class="modal-title" id="adddata1">新增数据</h4>

                </div>

                <div class="modal-body" style="overflow:auto;max-height: 500px;">
                    <div class="tab-content"
                         style="background-color: white;max-height:60%;padding-top: -10px ;">
                        <div class="tab-pane active" id="adddata" style=" ">
                            <table id="addTable" class="table table-bordered data-table" style="border: 0">
                                <thead class="table_tr">
                                <th style="width:20%;">字段名</th>
                                <th style="width:20%;">字段类型</th>
                                <th style="width:20%;">注释</th>
                                <th style="width:40%;">字段值</th>
                                </thead>
                                <tbody id="addtbody">

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div id="add_div" class="modal-footer">

                </div>
            </div>
        </div>
    </div>

    <%--修改数据--%>
    <div id="staticUpdateData" class="modal fade" tabindex="-1" data-width="200editTableFieldComsId">
        <div class="modal-dialog" style="width:600px;width:auto;max-width: 50%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                            id="editTableFieldComsCloseId11"></button>
                    <h4 class="modal-title" id="title_id1">表数据编辑</h4>

                </div>

                <div class="modal-body" style="overflow:auto;max-height: 500px;">

                    <div class="tab-content"
                         style="background-color: white;max-height:50%;padding-top: -10px ;">
                        <div style="margin-left: 1%;margin-right: 1%;width:98%;">
                            <table class="table table-bordered data-table" border="0">
                                <thead>
                                <tr class='table_tr'>
                                    <th>字段名</th>
                                    <th>字段类型</th>
                                    <th>注释</th>
                                    <th>字段值</th>
                                </tr>
                                </thead>
                                <tbody id="update_tbody"></tbody>
                            </table>

                        </div>
                    </div>
                </div>

                <div id="update_div" class="modal-footer">
                    <%--<button id="updatebtn"  style="width: 80px;height: 30px; border: 1px solid #cad9ea;">保存</button>--%>
                </div>
            </div>
        </div>
    </div>

    <%--表数据查看详情--%>
    <div id="staticShowDataDetail" class="modal fade" tabindex="-1" data-width="200editTableFieldComsId">
        <div class="modal-dialog" style="max-width:600px;width:auto;max-width: 50%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                            id="editTableFieldComsCloseId1"></button>
                    <h4 class="modal-title" id="checkDetial">查看详情</h4>

                </div>

                <div class="modal-body" style="overflow:auto;max-height: 500px;width: 100%;">
                    <div class="tab-content"
                         style="background-color: white;padding-top: -10px ;">
                        <div class="tab-pane active" id="checkData1"
                             style="width: 98%;margin-right: 1%;margin-left: 1%;">
                            <table spellcheck="0" cellspacing="0" border="0" class="table table-bordered data-table"
                                   id="checkTable" style="text-align: center;">
                                <thead>
                                <tr class="table_tr">
                                    <th style="width:20%;text-align: center;">字段名</th>
                                    <th style="width:20%;text-align: center;">字段类型</th>
                                    <th style="width:20%;text-align: center;">注释</th>
                                    <th style="width:40%;text-align: center;">字段值</th>
                                </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn btn-success">关闭</button>
                </div>
            </div>
        </div>
    </div>

</div>
<script type="text/html" id="tableNameTempl">
    {{each list as value i}}
    <li class="l3-menu">
        <a href="javaScript:void(0)" onclick="editTableData(this,1)" id="{{value.tableName}}"
           title="{{value.tableComment}}" style="white-space: nowrap;text-overflow: ellipsis;overflow: hidden;">
            <i class="fa fa-angle-right"></i>{{value.tableName}}</a>
    </li>
    {{/each}}
</script>

<%--查看数据--%>
<script type="text/html" id="checkdataTmpl">
    {{each data as value i}}
    <tr>
        <%--{{if (value.dataType=="datetime" || value.dataType=="DATE" ||value.dataType=="TIMESTAMP" || value.dataType=="datetime2") &&--%>
        <%--value.data!=null && value.data!=""}}--%>
        <%--<td>{{value.colName}}</td>--%>
        <%--<td>{{value.dataType}}</td>--%>
        <%--<td>{{value.columnComment}}</td>--%>
        <%--<td>{{value.data}}</td>--%>
        <%--{{else}}--%>
        <td>{{value.colName}}</td>
        <td>{{value.dataType}}</td>
        <td>{{value.columnComment}}</td>
        <td>{{value.data}}</td>
        <%--{{/if}}--%>
    </tr>
    {{/each}}
</script>

<%--新增数据--%>
<script type="text/html" id="adddataTmpl">
    {{each dataComposeDemoList as value i}}
    <tr>
        {{if value.pkColumn=="true" && value.autoAdd=="true"}}
        {{else}}
        <td>{{value.colName}}</td>
        <td>{{value.dataType}}</td>
        <td>{{value.columnComment}}</td>
        <td id="{{value.colName}}_td">
            {{if value.dataType=="date"}}
            <input class='selectDate' style='width: 100%;height:100%;' id="{{value.colName}}" type='text'
                   placeholder='请选择'/>
            {{else if value.dataType=="time"}}
            <input class='selectTime' style='width: 100%;height:100%;' id="{{value.colName}}" type='text'
                   placeholder='请选择'/>
            {{else if value.dataType=="datetime" || value.dataType=="DATE" || value.dataType=="TIMESTAMP" || value.dataType=="datetime2"}}
            <input class='selectDateTime' style='width: 100%;height:100%;' id="{{value.colName}}" type='text'
                   placeholder='请选择'/>
            {{else}}
            <input type="text" style='width: 100%;height:100%;' id="{{value.colName}}" dataType="{{value.dataType}}"
                   onblur="func_blur(this)" class='form-control' name="{{value.colName}}"/>
            <p id="{{value.colName}}_id" style='display: none;color:red;font-size: 12px;'></p>
            {{/if}}
        </td>
        {{/if}}
    </tr>
    {{/each}}
</script>

<%--显示表数据--%>
<script type="text/html" id="showTableDataTheadTmpl">
    <table class='table table-hover biaoge' spellcheck='0' border='0'>
        {{each datas as itemList i}}
        {{if i == 0}}
        <thead>
        <tr class="table_tr">
            {{each itemList as item j}}
            {{if j<5}}
            <td>{{item.columnName}}<br/>{{item.columnComment}}</td>
            {{else}}
            <td style="display: none">{{item.columnName}}<br/>{{item.columnComment}}</td>
            {{/if}}
            {{/each}}
            <td>操作</td>
        </tr>
        </thead>
        {{/if}}
        {{/each}}

        <tbody>
        {{each datas as itemList n}}
        {{if n>0}}
        <tr>
            {{each itemList as item k}}
            {{if k<5}}
            {{if (item.dataType=="datetime" || item.dataType=="DATE" || item.dataType=="datetime2") &&
            item.data!=null && item.data!=" "}}
            <td title="{{subStringDate(item.data)}}"
                style="white-space: nowrap;text-overflow: ellipsis;overflow: hidden;">
                {{subStringDate(item.data)}}
            </td>
            {{else}}
            <td title="{{item.data}}" style="white-space: nowrap;text-overflow: ellipsis;overflow: hidden;">
                {{item.data}}
            </td>
            {{/if}}
            {{else}}
            {{ if (item.dataType=="datetime" || item.dataType=="DATE" || item.dataType=="datetime2") &&
            item.data!=null && item.data!=" " }}
            <td title="{{subStringDate(item.data)}}" style="display: none;">{{subStringDate(item.data)}}</td>
            {{else}}
            <td title="{{item.data}}" style="display: none;">{{item.data}}</td>
            {{/if}}
            {{/if}}
            {{/each}}
            <td align='center'>
                <table class='0' cellspacing='0' border='0' align='scenter'>
                    <tr>
                        <td class='bianji'>
                            <a src='#' onclick='updateData(this)'>
                                <i class='fa fa-pencil-square-o' aria-hidden='true'></i>修改</a></td>
                        <td width='1'></td>
                        <td class='chakan'><a href='#' onclick='checkDada(this)'>
                            <i class='fa fa-eye' aria-hidden='true'></i>查看</a></td>
                        <td width='1'></td>
                        <td class='shanchu'><a href='#' onclick='deleteDate(this)'>
                            <i class='fa fa-trash-o fa-fw' aria-hidden='true'></i>删除</a></td>
                    </tr>
                </table>
            </td>
        </tr>
        {{/if}}
        {{/each}}
        </tbody>
    </table>


    {{each tablist as item j}}
    <div class='review-item clearfix'>
        <div id='page_div{{item.tableName}}' style='padding-top: 25px; float: left;'>当前第&nbsp;
            <span style='color:blue;' id='currentPageNo{{item.tableName}}'></span>&nbsp;页,&nbsp;共&nbsp;
            <span style='color:blue;' id='totalPages{{item.tableName}}'></span>页，
            <span style='color:blue;' id='totalCount{{item.tableName}}'></span>
            条数据
        </div>
        <div style='float: right;'>
            <div id='pagination{{item.tableName}}'>
            </div>
        </div>
    </div>
    {{/each}}
</script>

<%--更新数据--%>
<script id="toUpdateDataTmpl" type="text/html">
    {{each dataComposeDemoList as value i}}
    <tr>
        <td>{{value.colName}}</td>
        <td>{{value.dataType}}</td>
        <td>{{value.columnComment}}</td>
        <td id="{{value.colName}}_update">
            {{if value.dataType=="date"}}
            {{if value.data!=="" && value.data!==null}}
            <input class='selectDate' style='width: 100%;height:100%;' id="{{value.colName}}_coldata" type='text'
                   value="{{value.data}}"/>
            {{else}}
            <input class='selectDate' style='width: 100%;height:100%;' id="{{value.colName}}_coldata" type='text'
                   placeholder="请选择"/>
            {{/if}}
            {{else if value.dataType=="time"}}
            {{if value.data!=="" && value.data!==null}}
            <input class='selectTime' style='width: 100%;height:100%;' data-format="hh:mm:ss" id="{{value.colName}}_coldata" type='text'
                   value="{{value.data}}"/>
            {{else}}
            <input class='selectTime' style='width: 100%;height:100%;' id="{{value.colName}}_coldata" type='text'
                   placeholder="请选择" />
            {{/if}}
            {{else if value.dataType=="datetime" || value.dataType=="DATE" || value.dataType=="TIMESTAMP" || value.dataType=="datetime2"}}
            {{if value.data!=null && value.data!=""}}
            <input class='selectDateTime' style='width: 100%;height:100%;' id="{{value.colName}}_coldata"
                   type='text'
                   value="{{value.data}}"/>
            {{else}}
            <input class='selectDateTime' style='width: 100%;height:100%;' id="{{value.colName}}_coldata"
                   type='text'
                   placeholder="请选择"/>
            {{/if}}
            {{else}}
            <input type="text" style='width: 100%;height:100%;' id="{{value.colName}}_coldata"
                   dataType="{{value.dataType}}" value="{{value.data}}" class='form-control'
                   onblur="func_blur(this)" name="{{value.colName}}"/>
            <p id="{{value.colName}}_id" style='display: none;color:red;font-size: 12px;'></p>
            {{/if}}
        </td>
    </tr>
    {{/each}}
</script>

</body>
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/bootstrap-closable-tab/bootstrap-closable-tab.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrap-datetimepicker/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrap-datetimepicker/bootstrap-datetimepicker.zh-CN.js"></script>
    <script href="${ctx}/resources/bundles/timepicker/jquery.timepicker.js" type="text/css"></script>

    <script type="text/javascript">

        var subjectCode = '${sessionScope.SubjectCode}';
        var userName = "${sessionScope.userName}";
        var oldDataList;
        closableTab.afterCloseTab = function (item){
            if (!$(".nav.nav-tabs.activeTabs li")[0]) {
                $("#btn_addTableData").hide();
            }
        };
        $(function () {
            $.ajax({
                url: "${ctx}/showTable",
                type: "post",
                dataType: "json",
                data: {"subjectCode": subjectCode},
                success: function (data) {
                    $("#alltableName").html("");
                    if (data.list != null) {
                        var searchKey = "";
                        var html = template("tableNameTempl", data);
                        editTable_func(subjectCode, data.list[0].tableName, 1, searchKey);
                        $("#alltableName").append(html);
                        $("#alltableName").show();
                        $("#btn_addTableData").show();

                    }
                }
            });
        });


        function editTableData(i) {
            var searchKey = "";
            $("#btn_addTableData").show();
            var tableName = $(i).attr("id");
            var pageNo = 1;
            editTable_func(subjectCode, tableName, pageNo, searchKey);
            if($(".input-group input").val()!==null){
                $(".input-group input").val("");
                $(".input-group input").attr("placeholder","输入关键词");
            }

        }

        function searchTableData(i) {
            var searchKey = $(i).parent().siblings("input").val().trim();
            var tableName = $("#ul_div111 li.active a").text();

            $("#btn_addTableData").show();
            var pageNo = 1;
            clickPageButton(subjectCode, tableName, pageNo, searchKey);
        }

        //数据编辑
        function editTable_func(subjectCode, tableName, pageNo, searchKey) {
            debugger;
            var ids = "#tab_container_" + tableName;
            $.ajax({
                type: "post",
                url: "${ctx}/showTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName, "pageNo": pageNo, "searchKey": searchKey.trim()},
                dataType: "json",
                success: function (data) {
                    var columnName = data.columns;
                    var columnComment = data.columnComment;
                    var dataArry = data.dataDatil;
                    var tableComment = data.tableComment;

                    if (dataArry !== null) {
                        var columnList = [];
                        var tablist = [];
                        for (var i = 0; i < columnName.length; i++) {
                            columnList.push({columnName: columnName[i], columnComment: columnComment[i]});
                        }
                        tablist.push({tableName: tableName, subjectCode: subjectCode});
                        data.dataDatil.unshift(columnList);
                        template.helper("subStringDate", subStringDate);
                        var html = template("showTableDataTheadTmpl", {
                            "datas": data.dataDatil,
                            "isSync": data.isSync,
                            "tablist": tablist
                        });

                        var item = {'id': tableName, 'name': tableName, 'closable': true, 'template': html};
                        closableTab.addTab(item); // 执行创建页签
                        fun_limit(subjectCode, tableName, data, searchKey);
                        closableTab.addTabComment({
                            'id': tableName,
                            'tableComment': tableComment,
                            'closable': true,
                            'template': html
                        });
                        if (data.isSync !== 0) {
                            $(".bianji").hide();
                        }
                    } else {
                        $(ids).html(" ");
                        var click = "";
                        if (searchKey === "" || searchKey == null) {
                            click = " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>该表暂时没有数据</h5>";
                        } else {
                            click = " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>没有查询到相关数据</h5>";
                        }
                        $(ids).append(click);
                    }

                }
            });
        }



        /**
         * 分页
         * **/
        function fun_limit(subjectCode, tableName, data, searchKey) {
            var currentPageNo = "#currentPageNo" + tableName;
            var totalPage = "#totalPages" + tableName;
            var totalCount = "#totalCount" + tableName;
            var pagination = "#pagination" + tableName;

            $(currentPageNo).html(data.currentPage);
            $(totalPage).html(data.totalPages);
            $(totalCount).html(data.totalCount);

            var s = pagination + " .bootpag";
            if ($(s).length != 0) {
                $(pagination).off();
                $(pagination).empty();
            }
            $(pagination).bootpag({
                total: data.totalPages,
                page: data.currentPage,
                maxVisible: 5,
                leaps: true,
                firstLastUse: true,
                first: '首页',
                last: '尾页',
                wrapClass: 'pagination',
                activeClass: 'active',
                disabledClass: 'disabled',
                nextClass: 'next',
                prevClass: 'prev',
                lastClass: 'last',
                firstClass: 'first'
            }).on('page', function (event, num) {
                clickPageButton(subjectCode, tableName, num, searchKey);
            });
        }

        //各个页签分页
        function clickPageButton(subjectCode, tableName, num, searchKey) {
            var ids = "#tab_container_" + tableName;
            $(ids).html("");
            $.ajax({
                type: "post",
                url: "${ctx}/showTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName, "pageNo": num, "searchKey": searchKey},
                dataType: "json",
                success: function (data) {
                    var columnName = data.columns;
                    var columnComment = data.columnComment;
                    var dataArry = data.dataDatil;
                    var dataType = data.dataType;
                    var tableComment = data.tableComment;
                    if (dataArry.length > 0) {
                        var columnList = [];
                        var tablist = [];
                        for (var i = 0; i < columnName.length; i++) {
                            columnList.push({columnName: columnName[i], columnComment: columnComment[i]});
                        }
                        tablist.push({tableName: tableName, subjectCode: subjectCode});
                        data.dataDatil.unshift(columnList);
                        template.helper("dateFormat", formatDate);
                        var html = template("showTableDataTheadTmpl", {
                            "datas": data.dataDatil,
                            "isSync": data.isSync,
                            "tablist": tablist
                        });

                        //点击页码，不用新建页签
                        // var item = {'id': tableName, 'name': tableName, 'closable': true, 'template': html};
                        // closableTab.addTab(item); // 执行创建页签
                        $(ids).append(html);
                        fun_limit(subjectCode, tableName, data, searchKey);
                        if (data.isSync !== 0) {
                            $(".bianji").hide();
                        }
                    } else {
                        $(ids).html(" ");
                        var click = "";
                        if (searchKey === "" || searchKey == null) {
                            click = " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>该表暂时没有数据</h5>";
                        } else {
                            click = " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>没有查询到相关数据</h5>";
                        }
                        $(ids).append(click);
                    }
                }
            });
        }

        /**
         * 新增加函数
         * **/
        function add_function() {
            var tableName = $("#ul_div111 li.active a").text();
            $.ajax({
                url: "${ctx}/toaddTableData",
                type: "post",
                dataType: "json",
                data: {"subjectCode": subjectCode, "tableName": tableName},
                success: function (data) {
                    $("#addTable tbody").html(" ");
                    $("#add_div").html(" ");
                    var dataComposeDemoList = data.dataComposeDemoList;
                    var enumColumn = [];
                    var strs2 = data.COLUMN_NAME;
                    var dataTypeArr = data.DATA_TYPE;
                    var autoAddArr = data.autoAdd;
                    var pkColumnArr = data.pkColumn;

                    var html = template("adddataTmpl", data);
                    $("#addTable tbody").append(html);
                    debugger
                    //枚举类型
                    if (data.alert === 1) {
                        var enumDataList = data.enumDataList;
                        for (var i = 0; i < strs2.length; i++) {
                            for (var k = 0; k < enumDataList.length; k++) {
                                if (strs2[i] === enumDataList[k].key) {
                                    enumColumn.push(strs2[i]);
                                    var sVal = enumDataList[k].value.split("|_|");
                                    var selects = "<select class='form-control' style='width: 100%;height:100%;'  name='" + strs2[i] + "' id='" + strs2[i] + "'>";
                                    for (var kk = 0; kk < sVal.length; kk++) {
                                        if (sVal[kk] !== "" && sVal[kk] !== null) {
                                            selects += "<option>" + sVal[kk] + "</option>";
                                        }
                                    }
                                    selects += "</select>";
                                    $("#" + strs2[i] + "_td").children().remove();
                                    $("#" + strs2[i] + "_td").append(selects);
                                }

                            }
                        }
                    }

                    var s_add = "<button id='addbtn' class='btn btn-success' data-dismiss='modal' onclick=\"addTablefuntion('" + dataTypeArr + "','" + strs2 + "','" + pkColumnArr + "','" + autoAddArr + "','" + enumColumn + "')\">保存</button>";
                    s_add += "<button type='button' data-dismiss='modal' class='btn btn-danger'>取消</button> ";
                    $("#add_div").append(s_add);  //增加按钮
                    $("#staticAddData").modal("show");
                    $('.selectDate').datetimepicker({
                        minView: "month",//选择日期后，不会再跳转去选择时分秒
                        language: 'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "yyyy-mm-dd"
                    });
                    $('.selectDateTime').datetimepicker({
                        language: 'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        minuteStep: 1,
                        format: "yyyy-mm-dd hh:ii:ss"
                    });
                    $(".selectTime").bind('click',function(event){timePacker($(this),event)});
                }
            })
        }

        //点击增加按钮增加数据
        function addTablefuntion(dataTypeArr, strs2, pkColumnArr, autoAddArr, enumColumn) {
            debugger
            var tableName = $("#ul_div111 li.active a").text();
            //获得当前页码
            var currentPage = $("#currentPageNo"+tableName +"").html();

            var dataArr = [];
            var S_dataType=dataTypeArr.split(",");
            var S_column=strs2.split(",");
            var S_pkColumnArr=pkColumnArr.split(",");
            var S_autoAddArr=autoAddArr.split(",");
            var enumColumns = enumColumn.split(",");

            $('#addTable input').each(function () {
                dataArr.push($(this).val());
            });
            var data1 = [];
            var datacon = [];
            for (var i = 0, ii = 0; i < S_column.length; i++) {
                //隐藏字段赋值
                if (S_pkColumnArr[i] === "true" && S_autoAddArr[i] === "true") {
                    datacon[i] = "";
                } else {
                    var flag = 0;
                    for (var k = 0; k < enumColumns.length; k++) {
                        if (S_column[i] === enumColumns[k]) {
                            flag = 1;
                            datacon[i] = $("#" + S_column[i] + " option:selected").val();
                        }
                    }
                    if (flag === 0) {
                        datacon[i] = dataArr[ii];
                        ii++;
                    }
                }
            }

            for (var i = 0; i < S_column.length; i++) {
                var data2 = {};
                if (datacon[i] !== "" && datacon[i] !== null) {
                    //枚举类型跳过类型判断
                    for (var k = 0; k < enumColumns.length; k++) {
                        if (S_column[i] !== enumColumns[k]) {
                            //char类型判断
                            if (datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null" && S_dataType[i] === "char") {
                                var bytesCount = 0;
                                var str = datacon[i];
                                for (var j = 0; j < datacon[i].length; j++) {
                                    var c = str.charAt(j);
                                    if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                        bytesCount += 1;
                                    } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                        bytesCount += 2;
                                    } else {
                                        bytesCount += 1;
                                    }
                                }
                                if (bytesCount.length > 255) {
                                    toastr.warning(S_column[i] + "字段超出范围！");
                                    return;
                                }
                            }

                            //text类型判断
                            if (datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null" && S_dataType[i] === "text") {
                                var bytesCount = 0;
                                var str = datacon[i];
                                for (var j = 0; j < datacon[i].length; j++) {
                                    var c = str.charAt(j);
                                    if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                        bytesCount += 1;
                                    } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                        bytesCount += 2;
                                    } else {
                                        bytesCount += 1;
                                    }
                                }
                                if (bytesCount > 65535) {
                                    toastr.warning(S_column[i] + " 字段超出范围！");
                                    return;
                                }
                            }

                            //longtext类型判断
                            if (datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null" && S_dataType[i] === "longtext") {
                                var bytesCount = 0;
                                var str = datacon[i];
                                for (var j = 0; j < datacon[i].length; j++) {
                                    var c = str.charAt(j);
                                    if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                        bytesCount += 1;
                                    } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                        bytesCount += 2;
                                    } else {
                                        bytesCount += 1;
                                    }
                                }
                                if (bytesCount > 4294967295) {
                                    toastr.warning(S_column[i] + " 字段超出范围！");
                                    return;
                                }
                            }

                            //mediumtext类型判断
                            if (datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null" && S_dataType[i] === "mediumtext") {
                                var bytesCount = 0;
                                var str = datacon[i];
                                for (var j = 0; j < datacon[i].length; j++) {
                                    var c = str.charAt(j);
                                    if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                        bytesCount += 1;
                                    } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                        bytesCount += 2;
                                    } else {
                                        bytesCount += 1;
                                    }
                                }
                                if (bytesCount > 16777215) {
                                    toastr.warning(S_column[i] + " 字段超出范围！");
                                    return;
                                }
                            }

                            // bit数据类型判断
                            if (dataArr[i] !== "" && datacon[i] !== null && S_dataType[i] === "bit") {
                                if (dataArr[i] !== "0" && dataArr[i] !== "1") {
                                    toastr.warning(S_column[i] + " 字段应是boolean类型！");
                                    return;
                                }
                            }

                            if (datacon[i] !== null && datacon[i] !== "" && S_dataType[i] === "tinyint") {
                                if (!isNaN(datacon[i])) {
                                    if (parseInt(datacon[i]) > 127 || parseInt(datacon[i]) < -128) {
                                        toastr.warning(S_column[i] + " 字段超出范围！");
                                        return;
                                    }
                                } else {
                                    toastr.warning(S_column[i] + " 字段应是tinyint类型！");
                                    return;
                                }
                            }

                            //smallint数据类型判断
                            if (S_dataType[i] === "smallint" && datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null") {
                                if (!isNaN(datacon[i])) {
                                    var result = datacon[i].match(/^(-|\+)?\d+$/);
                                    if (result == null) {
                                        //警告消息提示s，默认背景为橘黄色
                                        toastr.warning(S_column[i] + " 字段应是smallint类型！");
                                        return;
                                    } else {
                                        if (parseInt(datacon[i]) > 32767 || parseInt(datacon[i]) < -32768) {
                                            toastr.warning(S_column[i] + " 字段超出范围！");
                                            return;
                                        }
                                    }
                                } else {
                                    toastr.warning(S_column[i] + " 字段应是数字类型！");
                                    return;
                                }
                            }

                            //mediumint数据类型判断
                            if (S_dataType[i] === "mediumint" && datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null") {
                                if (!isNaN(datacon[i])) {
                                    var result = datacon[i].match(/^(-|\+)?\d+$/);
                                    if (result == null) {
                                        //警告消息提示s，默认背景为橘黄色
                                        toastr.warning(S_column[i] + " 字段应是mediumint类型！");
                                        return;
                                    } else {
                                        if (parseInt(datacon[i]) > 8388607 || parseInt(datacon[i]) < -8388608) {
                                            toastr.warning(S_column[i] + " 字段超出范围！");
                                            return;
                                        }
                                    }
                                } else {
                                    toastr.warning(S_column[i] + " 字段应是数字类型！");
                                    return;
                                }
                            }
                            //bigint数据类型判断
                            if (S_dataType[i] === "bigint" && datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null") {
                                if (!isNaN(datacon[i])) {
                                    var result = datacon[i].match(/^(-|\+)?\d+$/);
                                    if (result == null) {
                                        //警告消息提示s，默认背景为橘黄色
                                        toastr.warning(S_column[i] + " 字段应是bigint类型！");
                                        return;
                                    }
                                    // else {
                                    //     if (datacon[i] > '9223372036854775807' || datacon[i] > '-9223372036854775808') {
                                    //         toastr.warning(S_column[i]+" 字段超出范围！");
                                    //         return;
                                    //     }
                                    // }
                                } else {
                                    toastr.warning(S_column[i] + " 字段应是数字类型！");
                                    return;
                                }
                            }
                            //int数据类型
                            if (datacon[i] !== "" && datacon[i] !== null && (S_dataType[i] === "int" || S_dataType[i] === "integer")) {
                                if (!isNaN(datacon[i])) {
                                    var reg = /^-?\d+$/;
                                    if (!reg.exec(datacon[i])) {
                                        toastr.warning(S_column[i] + " 字段应是int或integer类型！");
                                        return;
                                    }
                                } else {
                                    toastr.warning(S_column[i] + " 字段应是数字类型！");
                                    return;
                                }
                            }

                            //float数据类型
                            if (datacon[i] !== "" && datacon[i] !== null && S_dataType[i] === "float") {
                                if (isNaN(datacon[i])) {
                                    toastr.warning(S_column[i] + " 字段应是float类型！");
                                    return;
                                }
                            }

                            //double数据类型
                            if (datacon[i] !== "" && datacon[i] !== null && S_dataType[i] === "double") {
                                if (isNaN(datacon[i])) {
                                    toastr.warning(S_column[i] + " 字段应是double类型！");
                                    return;
                                }
                            }
                            if (datacon[i] !== "" && datacon[i] !== null && S_dataType[i] === "decimal") {
                                var pattern = /^(-?\d+)(\.\d+)?$/;    //浮点数
                                var reg = new RegExp(pattern);
                                if (!reg.test(datacon[i])) {
                                    toastr.warning(S_column + " 字段是decimal类型！ ");
                                    return;
                                }
                            }
                        }
                    }
                    data2.columnName = S_column[i];
                    data2.columnValue = datacon[i];
                    data1.push(data2);
                } else {
                    data2.columnName = S_column[i];
                    data2.columnValue = datacon[i];
                    data1.push(data2);
                }
            }

            $.ajax({
                url: "${ctx}/addTableData",
                type: "POST",
                data: {
                    "subjectCode": subjectCode,
                    "tableName": tableName,
                    "addData": JSON.stringify(data1),
                    "enumnCoumn": enumColumns.join(",")
                },
                dataType: "json",
                success: function (data) {
                    if (data.data === "0") {
                        toastr.error("添加数据失败，" + data.prikey + "的值已存在！");
                    } else if (data.data === "1") {
                        toastr.success("添加成功!");
                        var searchKey = $(".input-group input").val().trim();
                        // var searchKey = "";
                        clickPageButton(subjectCode, tableName, currentPage, searchKey);
                        $("#staticAddData").modal("hide");
                    } else if (data.data === "-1") {
                        toastr.error("添加数据失败，" + data.prikey + "不能为空！");
                    } else if (data.data === "-3") {
                        toastr.error("添加数据失败！");
                    } else if (data.data === "-2"){
                        var arr = data.dataResult.split("+");
                        if (arr[0] === "-2") {
                            toastr.error("添加数据失败，" + arr[1] + " 列不能为空！");
                        }
                    }else{
                        toastr.error(data.addResult.message);
                    }
                },
                error: function () {
                    alert("error!!!");
                }
            })
        }

        //点击编辑按钮
        function updateData(i) {
            debugger
            $("#form_id").html(" ");
            $("#update_tbody").html(" ");
            $("#update_div").html("");
            var tableName = $("#ul_div111 li.active a").text();

            var s = $(i).parent().parent().parent().parent().parent().siblings().size();
            var oldData = new Array();
            // var d = [];
            oldDataList=new Array();
            for (var j = 0; j < s; j++) {
                var ss = $(i).parent().parent().parent().parent().parent().siblings().eq(j).text().replace(/[\r\n]/g, "");
                oldData.push(ss.trim());//获得所有td
                // d.push(ss.trim() + "|_|");
                oldDataList[j]=ss.trim();
            }
            $.ajax({
                type: "post",
                url: "${ctx}/toupdateTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName,"oldData": JSON.stringify(oldData)},
                dataType: "json",
                success: function (data) {
                    debugger
                    var dataTypeArr = data.DATA_TYPE;
                    var strs2 = data.COLUMN_NAME;
                    var strs = data.enumData;
                    var enumColumn = [];//
                    var html = template("toUpdateDataTmpl", data);
                    $("#update_tbody").append(html);
                    var enumDataList = data.enumDataList;
                    if (data.alert === 1) {
                        for (var j = 0; j < strs2.length; j++) {
                            for (var k = 0; k < enumDataList.length; k++) {
                                if (strs2[j] === enumDataList[k].key) {
                                    enumColumn.push(strs2[j]);
                                    var sVal = enumDataList[k].value.split("|_|");
                                    var getinput_id = strs2[j] + "_coldata";
                                    var selects = "<select class='form-control' style='width: 100%;height:100%;'  name='" + strs2[j] + "' id='" + getinput_id + "'>";
                                    for (var kk = 0; kk < sVal.length; kk++) {
                                        if (strs[j] === sVal[kk]) {
                                            selects += "<option selected>" + sVal[kk] + "</option>";
                                        } else {
                                            if (sVal[kk] !== "" && sVal[kk] !== null) {
                                                selects += "<option>" + sVal[kk] + "</option>";
                                            }
                                        }
                                    }
                                    selects += "</select>";
                                    $("#" + strs2[j] + "_update").children().remove();
                                    $("#" + strs2[j] + "_update").append(selects);
                                }
                            }
                        }
                    }

                    var s_save = "<button id='btn_save'  class='btn btn-success' data-dismiss='modal' onclick=\" saveData('" + dataTypeArr + "','" + strs2 + "','" + enumColumn + "')\">保存</button> ";
                    s_save += "<button type='button' data-dismiss='modal' class='btn btn-danger'>取消</button> ";

                    $("#update_div").append(s_save);
                    $("#staticUpdateData").modal("show");

                    $('.selectDate').datetimepicker({
                        minView: "month",//选择日期后，不会再跳转去选择时分秒
                        language: 'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "yyyy-mm-dd"
                    });
                    $('.selectDateTime').datetimepicker({
                        //第一种
                        language: 'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "yyyy-mm-dd hh:ii:ss",
                        minView: 0,
                        minuteStep: 1
                    });
                    $(".selectTime").bind('click',function(event){timePacker($(this),event)});
                }
            });
        }

        //这里是失去焦点时的事件
        function func_blur(i){
            var dataType =$(i).attr("dataType");
            var dataValue = $(i).attr("value");
            var columnName =$(i).attr("name");
            var ids="#"+columnName+"_id";
            $(ids).hide();
            var warns;

                //char类型判断
                if (dataValue !== null && dataValue !== ""  && dataType === "char") {
                    var bytesCount = 0;
                    var str = dataValue;
                    for (var j = 0; j < dataValue.length; j++) {
                        var c = str.charAt(j);
                        if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                            bytesCount += 1;
                        } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                            bytesCount += 2;
                        } else {
                            bytesCount += 1;
                        }
                    }
                    if (bytesCount.length > 255) {
                        // toastr.warning(columnName[i]+" 字段超出范围！");
                        warns=columnName+" 字段超出范围！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //text类型判断
                if (dataValue !== null && dataValue !== ""  && dataType === "text") {
                    var bytesCount = 0;
                    var str = dataValue;
                    for (var j = 0; j < dataValue.length; j++) {
                        var c = str.charAt(j);
                        if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                            bytesCount += 1;
                        } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                            bytesCount += 2;
                        } else {
                            bytesCount += 1;
                        }
                    }
                    if (bytesCount > 65535) {
                        // toastr.warning(columnName[i]+" 字段超出范围！");
                        warns=columnName+" 字段超出范围！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //longtext类型判断
                if (dataValue !== null && dataValue !== "" && dataType === "longtext") {
                    var bytesCount = 0;
                    var str = dataValue;
                    for (var j = 0; j < dataValue.length; j++) {
                        var c = str.charAt(j);
                        if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                            bytesCount += 1;
                        } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                            bytesCount += 2;
                        } else {
                            bytesCount += 1;
                        }
                    }
                    if (bytesCount > 4294967295) {
                        // toastr.warning(columnName[i]+" 字段超出范围！");
                        warns=columnName+" 字段超出范围！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //mediumtext类型判断
                if (dataValue !== null && dataValue !== "" && dataType === "mediumtext") {
                    var bytesCount = 0;
                    var str = dataValue;
                    for (var j = 0; j < dataValue.length; j++) {
                        var c = str.charAt(j);
                        if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                            bytesCount += 1;
                        } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                            bytesCount += 2;
                        } else {
                            bytesCount += 1;
                        }
                    }
                    if (bytesCount > 16777215) {
                        // toastr.warning(columnName[i]+" 字段超出范围！");
                        warns=columnName+" 字段超出范围！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //bit数据类型判断
                if (dataValue !== null && dataValue !== ""  && dataType === "bit") {
                    if (dataValue !== "0" && dataValue !== "1") {
                        // toastr.warning(columnName+" 字段应是bit类型！");
                        warns=columnName+" 字段应是bit类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //tinyint数据类型判断
                if (dataValue !== null && dataValue !== "" && dataType === "tinyint") {
                    if (!isNaN(dataValue)) {
                        if (parseInt(dataValue) > 127 || parseInt(dataValue) < -128) {
                            toastr.warning(columnName[i]+" 字段超出范围！");
                            return;
                        }
                    } else {
                        // toastr.warning(columnName[i]+" 字段应是tinyint等类型！");
                        warns=columnName+" 字段应是tinyint等类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //smallint数据类型判断
                if (dataValue !== null && dataValue !== "" && dataType === "smallint") {
                    if (!isNaN(dataValue)) {
                        var result = dataValue.match(/^(-|\+)?\d+$/);
                        if (result == null) {
                            //警告消息提示s，默认背景为橘黄色
                            // toastr.warning(columnName[i]+" 字段应是smallint类型！");
                            warns=columnName+" 字段应是smallint类型！";
                            $(ids).html(warns);
                            $(ids).show();
                            return;
                        } else {
                            if (parseInt(dataValue) > 32767 || parseInt(dataValue) < -32768) {
                                // toastr.warning(columnName+" 字段超出范围！");
                                warns=columnName+" 字段超出范围！";
                                $(ids).html(warns);
                                $(ids).show();
                                return;
                            }
                        }
                    } else {
                        // toastr.warning(columnName[i]+" 该字段应是数字类型！");
                        warns=columnName+" 该字段应是数字类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //mediumint数据类型判断
                if (dataType === "mediumint" && dataValue !== null && dataValue !==" ") {
                    if (!isNaN(dataValue)) {
                        var result = dataValue.match(/^(-|\+)?\d+$/);
                        if (result == null) {
                            //警告消息提示s，默认背景为橘黄色
                            // toastr.warning(columnName[i]+" 字段应是mediumint类型！");
                            warns=columnName+" 字段应是mediumint类型！";
                            $(ids).html(warns);
                            $(ids).show();
                            return;
                        } else {
                            if (parseInt(checkdataArr[i]) > 8388607 || parseInt(checkdataArr[i]) < -8388608) {
                                // toastr.warning(columnName[i]+" 字段超出范围！");
                                warns=columnName+" 字段超出范围！";
                                $(ids).html(warns);
                                $(ids).show();
                                return;
                            }
                        }
                    } else {
                        // toastr.warning(columnName[i]+" 字段应是数字类型！");
                        warns=columnName+" 字段应是数字类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //bigint数据类型判断
                if (dataType === "bigint" && dataValue !== null && dataValue !=="") {
                    if (!isNaN(dataValue)) {
                        var result = dataValue.match(/^(-|\+)?\d+$/);
                        if (result == null) {
                            //警告消息提示s，默认背景为橘黄色
                            // toastr.warning(columnName[i]+" 字段应是bigint类型！");
                            warns=columnName +" 字段应是bigint类型！";
                            $(ids).html(warns);
                            $(ids).show();
                            return;
                        }
                    } else {
                        // toastr.warning(columnName[i]+" 字段应是数字类型！");
                        warns=columnName+" 字段应是数字类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }
                //int数据类型判断

                if ((dataType === "int" || dataType === "integer") && dataValue !== null && dataValue !=="") {
                    if (!isNaN(dataValue)) {
                        var result =dataValue.match(/^(-|\+)?\d+$/);
                        if (result == null) {
                            //警告消息提示s，默认背景为橘黄色
                            // toastr.warning(columnName +" 字段应是int或integer等类型！");
                            warns=columnName +" 字段应是int类型！";
                            $(ids).html(warns);
                            $(ids).show();
                            return;
                        } else {
                            if (parseInt(dataValue) > 2147483647 || parseInt(dataValue) < -2147483648) {
                                // toastr.warning(columnName+" 字段超出范围！");
                                warns=columnName+" 字段超出范围！";
                                $(ids).html(warns);
                                $(ids).show();
                                return;
                            }
                        }
                    } else {
                        // toastr.warning(columnName+" 字段应是数字类型！");
                        warns=columnName+" 字段应是数字类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }

                //float数据类型判断
                if (dataType === "float" && dataValue !== null && dataValue !=="") {
                    if (isNaN(dataValue)) {
                        // toastr.warning(columnName[i]+" 字段应是float类型！");
                        warns=columnName+" 字段应是float类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }
                //double数据类型判断
                if (dataType === "double" && dataValue !== null && dataValue !=="") {
                    if (isNaN(dataValue)) {
                        // toastr.warning(columnName[i]+" 字段应是double类型！");
                        warns=columnName+" 字段应是double类型！";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }
                }
                //decimal数据类型判断

                if (dataType === "decimal" && dataValue !== null && dataValue !=="") {
                    var pattern = /^(-?\d+)(\.\d+)?$/;    //浮点数
                    var reg = new RegExp(pattern);
                    if (!reg.test(dataValue)) {
                        warns = columnName + " 字段是decimal类型！ ";
                        $(ids).html(warns);
                        $(ids).show();
                        return;
                    }

                }
        }

        //修改数据点击保存
        function saveData(dataType, alert_column, enumColumn) {
            debugger;
            var tableName = $("#ul_div111 li.active a").text();
            //获得当前页码
            var currentPage = $("#currentPageNo" + tableName + "").html();
            var dataTypeArr = dataType.split(",");
            var columnName=alert_column.split(",");
            var checkdataArr = [];
            var checkdataArrs=[];
            // var oldArr = oldData.split("|_|,");
            // oldArr[oldArr.length - 1] = oldArr[oldArr.length - 1].split("|_|")[0];
            JSON.stringify(oldDataList)
            var enumColumns = enumColumn.split(",");
            for (var i = 0; i < columnName.length; i++) {
                var s = document.getElementById(columnName[i] + "_coldata").value;
                var coldata = {};
                coldata["name"] = columnName[i];
                coldata["value"] = s;
                checkdataArrs.push(coldata);
                checkdataArr.push(s);
            }

            for (var i = 0; i < checkdataArr.length; i++) {
                for (var k = 0; k < enumColumns.length; k++) {
                    if (enumColumns[k] !== columnName[i]) {

                        //char类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "char") {
                            var bytesCount = 0;
                            var str = checkdataArr[i];
                            for (var j = 0; j < checkdataArr[i].length; j++) {
                                var c = str.charAt(j);
                                if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                    bytesCount += 1;
                                } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                    bytesCount += 2;
                                } else {
                                    bytesCount += 1;
                                }
                            }
                            if (bytesCount.length > 255) {
                                toastr.warning(columnName[i] + " 字段超出范围！");
                                return;
                            }
                        }

                        //text类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "text") {
                            var bytesCount = 0;
                            var str = checkdataArr[i];
                            for (var j = 0; j < checkdataArr[i].length; j++) {
                                var c = str.charAt(j);
                                if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                    bytesCount += 1;
                                } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                    bytesCount += 2;
                                } else {
                                    bytesCount += 1;
                                }
                            }
                            if (bytesCount > 65535) {
                                toastr.warning(columnName[i] + " 字段超出范围！");
                                return;
                            }
                        }

                        //longtext类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "longtext") {
                            var bytesCount = 0;
                            var str = checkdataArr[i];
                            for (var j = 0; j < checkdataArr[i].length; j++) {
                                var c = str.charAt(j);
                                if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                    bytesCount += 1;
                                } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                    bytesCount += 2;
                                } else {
                                    bytesCount += 1;
                                }
                            }
                            if (bytesCount > 4294967295) {
                                toastr.warning(columnName[i] + " 字段超出范围！");
                                return;
                            }
                        }
                        //mediumtext类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "mediumtext") {
                            var bytesCount = 0;
                            var str = checkdataArr[i];
                            for (var j = 0; j < checkdataArr[i].length; j++) {
                                var c = str.charAt(j);
                                if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                                    bytesCount += 1;
                                } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                                    bytesCount += 2;
                                } else {
                                    bytesCount += 1;
                                }
                            }
                            if (bytesCount > 16777215) {
                                toastr.warning(columnName[i] + " 字段超出范围！");
                                return;
                            }
                        }

                        //bit数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "bit") {
                            if (checkdataArr[i] !== "0" && checkdataArr[i] !== "1") {
                                toastr.warning(columnName[i] + " 字段应是bit类型！");
                                return;
                            }
                        }
                        //tinyint数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "tinyint") {
                            if (!isNaN(checkdataArr[i])) {
                                if (parseInt(checkdataArr[i]) > 127 || parseInt(checkdataArr[i]) < -128) {
                                    toastr.warning(columnName[i] + " 字段超出范围！");
                                    return;
                                }
                            } else {
                                toastr.warning(columnName[i] + " 字段应是tinyint等类型！");
                                return;
                            }
                        }

                        //smallint数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "smallint") {
                            if (!isNaN(checkdataArr[i])) {
                                var result = checkdataArr[i].match(/^(-|\+)?\d+$/);
                                if (result == null) {
                                    //警告消息提示s，默认背景为橘黄色
                                    toastr.warning(columnName[i] + " 字段应是smallint类型！");
                                    return;
                                } else {
                                    if (parseInt(checkdataArr[i]) > 32767 || parseInt(checkdataArr[i]) < -32768) {
                                        toastr.warning(columnName[i] + " 字段超出范围！");
                                        return;
                                    }
                                }
                            } else {
                                toastr.warning(columnName[i] + " 该字段应是数字类型！");
                                return;
                            }
                        }

                        //mediumint数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "mediumint") {
                            if (!isNaN(checkdataArr[i])) {
                                var result = checkdataArr[i].match(/^(-|\+)?\d+$/);
                                if (result == null) {
                                    //警告消息提示s，默认背景为橘黄色
                                    toastr.warning(columnName[i] + " 字段应是mediumint类型！");
                                    return;
                                } else {
                                    if (parseInt(checkdataArr[i]) > 8388607 || parseInt(checkdataArr[i]) < -8388608) {
                                        toastr.warning(columnName[i] + " 字段超出范围！");
                                        return;
                                    }
                                }
                            } else {
                                toastr.warning(columnName[i] + " 字段应是数字类型！");
                                return;
                            }
                        }

                        //bigint数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "bigint") {
                            if (!isNaN(checkdataArr[i])) {
                                var result = checkdataArr[i].match(/^(-|\+)?\d+$/);
                                if (result == null) {
                                    //警告消息提示s，默认背景为橘黄色
                                    toastr.warning(columnName[i] + " 字段应是bigint类型！");
                                    return;
                                }
                            } else {
                                toastr.warning(columnName[i] + " 字段应是数字类型！");
                                return;
                            }
                        }

                        //int数据类型判断
                        if ((dataTypeArr[i] === "int" || dataTypeArr[i] === "integer") && checkdataArr[i] !== null && checkdataArr[i] !== "") {
                            if (!isNaN(checkdataArr[i])) {
                                var result = checkdataArr[i].match(/^(-|\+)?\d+$/);
                                if (result == null) {
                                    //警告消息提示s，默认背景为橘黄色
                                    toastr.warning(columnName[i] + " 字段应是int或integer等类型！");
                                    return;
                                } else {
                                    if (parseInt(checkdataArr[i]) > 2147483647 || parseInt(checkdataArr[i]) < -2147483648) {
                                        toastr.warning(columnName[i] + " 字段超出范围！");
                                        return;
                                    }
                                }
                            } else {
                                toastr.warning(columnName[i] + " 字段应是数字类型！");
                                return;
                            }
                        }

                        //float数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "float") {
                            if (isNaN(checkdataArr[i])) {
                                toastr.warning(columnName[i] + " 字段应是float类型！");
                                return;
                            }
                        }

                        //double数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "double") {
                            if (isNaN(checkdataArr[i])) {
                                toastr.warning(columnName[i] + " 字段应是double类型！");
                                return;
                            }
                        }

                        //decimal数据类型判断
                        if (checkdataArr[i] !== null && checkdataArr[i] !== "" && dataTypeArr[i] === "decimal") {
                            // var col_type_str = S_columnType[i].split(",");
                            // var m = col_type_str[0].split("(")[1];
                            // var s = col_type_str[1].split(")")[0];
                            // var n = m - s;
                            // var pattern = /^(-?\d+)(\.\d+)?$/;    //浮点数
                            //
                            // var reg = new RegExp(pattern);
                            // if (reg.test(checkdataArr[i])) {
                            //     var ss = checkdataArr[i].split(".");
                            //     if (ss[0].length > n) {
                            //         toastr.warning(columnName[i]+" 字段数据超出范围！ ");
                            //         return;
                            //     }
                            // } else {
                            //     toastr.warning(columnName[i]+" 字段是decimal类型！ ");
                            //     return;
                            // }
                            var pattern = /^(-?\d+)(\.\d+)?$/;    //浮点数
                            var reg = new RegExp(pattern);
                            if (!reg.test(checkdataArr[i])) {
                                toastr.warning(columnName[i] + " 字段是decimal类型！ ");
                                return;
                            }
                        }
                    }
                }
            }

            $.ajax({
                url: "${ctx}/saveTableData",
                type: "POST",
                data: {"newdata": JSON.stringify(checkdataArrs),
                    "subjectCode": subjectCode,
                    "tableName": tableName,
                    "enumColumn": enumColumns.join(","),
                    "oldData": JSON.stringify(oldDataList)
                },
                dataType: "json",
                success: function (data) {
                    if (data.data === "1") {
                        //成功消息提示，默认背景为浅绿色
                        toastr.success("更新成功!");
                        $("#staticUpdateData").modal("hide");
                        // var searchKey = "";
                        var searchKey = $(".input-group input").val().trim();
                        clickPageButton(subjectCode, tableName, currentPage, searchKey);
                    } else if (data.data === "-1") {
                        toastr.error("主键重复！");
                    } else if (data.data === "-2") {
                        toastr.error("更新数据失败，" + data.col + " 列不能为空！");
                    }else if(data.data==="0"){
                        toastr.error("更新数据失败！");
                    }else{
                        toastr.error(data.updateResult.message);
                    }
                },
                error: function () {
                    alert("error!!!!!");
                }
            })
        }

        //删除数据
        function deleteDate(i) {
            var tableName = $("#ul_div111 li.active a").text();
            bootbox.setDefaults("locale","zh_CN");
            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>", function (r) {
                if (r) {
                    var currentPage = $("#currentPageNo"+tableName +"").html();

                    //是否有同胞元素
                    if ($(i).parent().parent().parent().parent().parent().parent().siblings().size() == 0 && currentPage !== "1") {
                        currentPage = currentPage - 1;
                    }
                    var s = $(i).parent().parent().parent().parent().parent().siblings().size();
                    var oldData = new Array();
                    for (var j = 0; j < s; j++) {
                        var ss = $(i).parent().parent().parent().parent().parent().siblings().eq(j).text().replace(/[\r\n]/g, "");
                        oldData.push(ss.trim());//获得所有td
                    }

                    $.ajax({
                        url: "${ctx}/deleteData",
                        type: "POST",
                        dataType: "json",
                        data: {
                            "subjectCode": subjectCode,
                            "tableName": tableName,
                            "oldData": JSON.stringify(oldData)
                        },
                        success: function (data) {
                            if (data.deleteReuslt === "1") {
                                toastr.success("删除成功!");
                                var searchKey = $(".input-group input").val();
                                clickPageButton(subjectCode, tableName, currentPage, searchKey);

                            }else{
                                toastr.error(data.deleteReuslt.message);
                            }
                        }
                    })
                }
            })
        }

        //查看详情
        function checkDada(i) {
            var tableName = $("#ul_div111 li.active a").text();
            $("#checkTable tbody").html(" ");
            var s = $(i).parent().parent().parent().parent().parent().siblings().size();
            var oldData = new Array();
            for (var j = 0; j < s; j++) {
                oldData.push($(i).parent().parent().parent().parent().parent().siblings().eq(j).text().replace(/[\r\n]/g, "").trim());//获得所有td
            }
            $.ajax({
                type: "post",
                url: "${ctx}/toCheckTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName, "oldData": JSON.stringify(oldData)},
                dataType: "json",
                success: function (data) {
                    var html = template("checkdataTmpl", data);
                    $("#checkTable tbody").append(html);
                    $("#staticShowDataDetail").modal("show");
                }
            });
        }

        $("#ul_div111 li a").live("click",function () {
            if($(".input-group input").val()!==null){
                $(".input-group input").val("");
                $(".input-group input").attr("placeholder","输入关键词");
            }
        })

        //时间方法time
        function timePacker(dom,e) {
            var hours = null;//存储 "时"
            var minutes = null;//存储 "分"
            var second=null;   //存储秒
            var clientY = dom.offset().top + dom.height();//获取位置
            var clientX = dom.offset().left;
            var date = new Date();
            var nowHours = date.getHours();
            var nowMinutes = date.getMinutes();
            var time_hm=/^(0\d{1}|\d{1}|1\d{1}|2[0-3]):([0-5]\d{1})$/; //时间正则，防止手动输入的时间不符合规范
            var inputText = dom.is("input") ? dom.val():dom.text();
            //插件容器布局
            var html = '';
            html += '<div class="timePacker" style="z-index: 10052">';
            html += '<div class="timePacker-hours" style="display: block;">';
            html += '<div class="timePacker-title"><span>小时</span></div>';
            html += '<div class="timePacker-content">';
            html += '<ul>';
            var i = 0;
            while (i < 24)
            {
                //var text = i < 10 ? "0" + i : i;
                if(inputText !== "" && Number(inputText.split(":")[0]) === i){
                    html += '<li class="hoursList timePackerSelect">'+i+'</li>';
                    hours = Number(inputText.split(":")[0]);
                }else{
                    if(i === nowHours){
                        html += '<li class="hoursList" style="color: #007BDB;">'+i+'</li>';
                    }else{
                        html += '<li class="hoursList">'+i+'</li>';
                    }
                }
                i++;
            }
            html += '</ul>';
            html +=  '</div>';
            html += '</div>';
            html += '<div class="timePacker-minutes" style="display: none;">';
            html += '<div class="timePacker-title"><span>分钟</span><span class="timePacker-back-hours" title="返回小时选择"><img src="../resources/img/backTime.png"/> </span></div>';
            html += '<div class="timePacker-content">';
            html += '<ul>';
            var m = 0;
            while (m < 60)
            {
                var textM = m < 10 ? "0" + m : m;
                if(inputText !== "" && Number(inputText.split(":")[1]) === textM){
                    html += '<li class="mList timePackerSelect">'+textM+'</li>';
                    minutes = Number(inputText.split(":")[1]);
                }else{
                    if(m === nowMinutes){
                        html += '<li class="mList" style="color: #007BDB;">'+textM+'</li>';
                    }else{
                        html += '<li class="mList">'+textM+'</li>';
                    }
                }
                m++;
            }
            html += '</ul>';
            html +=  '</div>';
            html += '</div>';

            html += '<div class="timePacker-second" style="display: none;" id="ss_id">';
            html += '<div class="timePacker-title"><span>秒</span><span class="timePacker-back-minutes" title="返回分钟选择"><img src="../resources/img/backTime.png"/> </span></div>';
            html += '<div class="timePacker-content">';
            html += '<ul>';
            var s = 0;
            while (s< 60)
            {
                var textM = s < 10 ? "0" + s : s;
                if(inputText !== "" && Number(inputText.split(":")[1]) === textM){
                    html += '<li class="sList timePackerSelect">'+textM+'</li>';
                    second = Number(inputText.split(":")[1]);
                }else{
                    if(s === nowMinutes){
                        html += '<li class="sList" style="color: #007BDB;">'+textM+'</li>';
                    }else{
                        html += '<li class="sList">'+textM+'</li>';
                    }
                }
                s++;
            }
            html += '</ul>';
            html +=  '</div>';
            html += '</div>';
            html += '</div>';
            if($(".timePacker").length > 0){
                $(".timePacker").remove();
            }
            $("body").append(html);
            $(".timePacker").css({
                position:"absolute",
                top:clientY,
                left:clientX
            });
            var _con = $(".timePacker"); // 设置目标区域,如果当前鼠标点击非此插件区域则移除插件
            $(document).mouseup(function(e){
                if(!_con.is(e.target) && _con.has(e.target).length === 0){ // Mark 1
                    _con.remove();
                }
            });
            //小时选择
            $(".hoursList").bind('click',function () {
                $(this).addClass("timePackerSelect").siblings().removeClass("timePackerSelect");
                hours = $(this).text();
                var timer = setTimeout(function () {
                    $(".timePacker-hours").css("display","none");
                    $(".timePacker-minutes").fadeIn();
                    if(minutes !== null){
                        var getTime = hours + ":" + minutes;
                        if(time_hm.test(getTime)){
                            dom.removeClass("errorStyle");
                        }
                        dom.is("input") ? dom.val(getTime):dom.text(getTime);
                    }
                    clearTimeout(timer);
                },100);
            });
            //返回小时选择
            $(".timePacker-back-hours").bind('click',function () {
                var timer = setTimeout(function () {
                    $(".timePacker-minutes").css("display","none");
                    $(".timePacker-second").css("display","none");
                    $(".timePacker-hours").fadeIn();
                    clearTimeout(timer);
                },500);
            });
            //返回小时选择
            $(".timePacker-back-minutes").bind('click',function () {
                var timer = setTimeout(function () {
                    $(".timePacker-second").css("display","none");
                    $(".timePacker-minutes").fadeIn();
                    clearTimeout(timer);
                },500);
            });

//  //分钟选择
            $(".mList").bind('click',function () {
                $(this).addClass("timePackerSelect").siblings().removeClass("timePackerSelect");
                minutes = $(this).text();
                var timer = setTimeout(function () {
                    $(".timePacker-minutes").css("display","none");
                    $(".timePacker-second").fadeIn();
                    if(second !== null){
                        var getTime = hours + ":" + minutes + ":" + second;
                        if(time_hm.test(getTime)){
                            dom.removeClass("errorStyle");
                        }
                        dom.is("input") ? dom.val(getTime):dom.text(getTime);
                    }
                    clearTimeout(timer);
                },100);
            })

            //秒的选择
            $(".sList").bind('click',function () {
                $(this).addClass("timePackerSelect").siblings().removeClass("timePackerSelect");
                second = $(this).text();
                var timer = setTimeout(function () {
                    var getTime = hours + ":" + minutes +":"+second;
                    if(time_hm.test(getTime)){
                        dom.removeClass("errorStyle");
                    }
                    dom.is("input") ? dom.val(getTime):dom.text(getTime);
                    clearTimeout(timer);
                    _con.remove();
                },500);
            })
        }


    </script>
</div>
</html>
