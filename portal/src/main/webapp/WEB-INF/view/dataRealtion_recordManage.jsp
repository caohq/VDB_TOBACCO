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
        {{if value.colName=="PORTALID"}}
        {{else if value.dataType=="datetime"}}
        <td>{{value.colName}}</td>
        <td>{{value.dataType}}</td>
        <td>{{value.columnComment}}</td>
        <td>{{dateFormat(value.data)}}</td>
        {{else}}
        <td>{{value.colName}}</td>
        <td>{{value.dataType}}</td>
        <td>{{value.columnComment}}</td>
        <td>{{value.data}}</td>
        {{/if}}
    </tr>
    {{/each}}
</script>

<%--新增数据--%>
<script type="text/html" id="adddataTmpl">
    {{each data as value i}}
    <tr>
        {{if value.colName=="PORTALID"}}
        {{else if value.pkColumn=="PRI" && value.autoAdd=="auto_increment"}}
        {{else}}
        <td>{{value.colName}}</td>
        <td>{{value.dataType}}</td>
        <td>{{value.columnComment}}</td>
        <td><input type="text" /></td>
        {{/if}}
    </tr>
    {{/each}}
</script>

<%--显示表数据--%>
<script type="text/html" id="showTableDataTestTheadTmpl">
    {{each data as value i}}
    <tr>
        {{include 'scoreTemplate' $value}}   <!--引入子模板-->
        <td>操作</td>
    </tr>
    {{/each}}
</script>
<script id="scoreTemplate" type="text/html">
    {{each dataDatil}}
    <td>{{$value.tabledata1}}</td>
    <td>{{$value.tabledata2}}</td>
    <td>{{$value.tabledata3}}</td>
    <td>{{$value.tabledata4}}</td>
    <td>{{$value.tabledata5}}</td>
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
                    var html = template("tableNameTempl", data);
                    $("#alltableName").append(html);
                    $("#alltableName").show();
                    if (data.list != null) {
                        $("#btn_addTableData").show();
                        var searchKey = "";
                        editTable_func(subjectCode, data.list[0].tableName, 1, searchKey);
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
            var searchKey = $(i).parent().siblings("input").val();
            var tableName = $("#ul_div111 li.active a").text();

            $("#btn_addTableData").show();
            var pageNo = 1;
            // editTable_func(subjectCode, tableName, pageNo,searchKey);
            clickPageButton(subjectCode, tableName, pageNo, searchKey);
        }

        //数据编辑
        function editTable_func(subjectCode, tableName, pageNo, searchKey) {
            debugger;
            var ids = "#tab_container_" + tableName;
            $.ajax({
                type: "post",
                url: "${ctx}/showTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName, "pageNo": pageNo, "searchKey": searchKey},
                dataType: "json",
                success: function (data) {
                    var arr = data.columns;
                    var dataType = data.dataType;
                    var columnComment = data.columnComment;
                    var dataArry = data.dataDatil;
                    console.log(dataArry);
                    var count=dataArry.length;
                    var delPORTALID;
                    var tabs = "";
                    var s = " ";
                    var tableComment = data.tableComment;
                    s = "<table id='" + tableName + "' class='table table-hover biaoge' spellcheck='0' border='0'>" +
                        "<thead ><tr class='table_tr'>";
                    //表头
                    var il = 0;
                    if (dataArry.length > 0) {
                        for (var i = 0; i < arr.length; i++) {
                            if (il < 5) {
                                if (arr[i] === "PORTALID") {
                                    s += "<td style='display:none;' title=" + arr[i] + ">" + arr[i] + "</td>";
                                } else {
                                    s += "<td style='width:15%;'>" + arr[i] + "<br/><p title=" + columnComment[i] + ">" + columnComment[i] + "</p></td>";
                                    il++;
                                }
                            } else {
                                s += "<td style='display:none;'title=" + arr[i] + ">" + arr[i] + "</td>";
                            }
                        }
                    }
                    var ss = "";
                    if (dataArry.length > 0) {
                        ss += "<tbody>";
                        for (var key in dataArry) {
                            ss+="<tr>";
                            var d = dataArry[key];
                            var i = 0;
                            var j = 0;
                            for (var k in d) {
                                if (j < 5) {
                                    if (k === arr[i]) {
                                        if (dataType[i] === "datetime" && d[k] !== null && d[k] !== " ") {
                                            var date = d[k].split(".");
                                            d[k] = date[0];
                                        }
                                        if (k === "PORTALID") {
                                            delPORTALID = d[k];
                                            ss += "<td  style='display:none;' title='" + d[k] + "'>" + d[k] + "</td>";
                                        } else {
                                            ss += "<td title='" + d[k] + "' style='word-break:keep-all;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'><xmp style='font-family: Arial, 微软雅黑;'>" + d[k] + "</xmp></td>";
                                            j++;
                                        }
                                    } else {
                                        if (dataType[i] === "datetime" && d[arr[i]] !== null && d[arr[i]] !== " ") {
                                            var date = d[arr[i]].split(".");
                                            d[arr[i]] = date[0];
                                        }
                                        if (arr[i] === "PORTALID") {
                                            delPORTALID = d[arr[i]];
                                            ss += "<td style='display:none;' title='" + d[arr[i]] + "'>" + d[arr[i]] + "</td>";
                                        } else {
                                            ss += "<td title='" + d[arr[i]] + "' style='word-break:keep-all;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'><xmp style='font-family: Arial, 微软雅黑;'>" + d[arr[i]] + "</xmp></td>";
                                            j++;
                                        }
                                    }
                                    i++;
                                } else {
                                    if (k === arr[i]) {
                                        if (k === "PORTALID") {
                                            delPORTALID = d[k];
                                        }
                                    } else {
                                        if (arr[i] === "PORTALID") {
                                            delPORTALID = d[arr[i]];
                                        }
                                    }
                                    i++;
                                }
                            }
                            ss += "<td align='center'><table class='0' cellspacing='0' border='0' align='center'><tr>";
                            if (data.isSync === 0) {
                                ss += "<td class='bianji'><a src='#' onclick=\" updateData('" + delPORTALID + "','" + tableName + "','" + subjectCode + "')\">" +
                                    "<i class='fa fa-pencil-square-o' aria-hidden='true'></i>修改</a></td><td width='1'></td>";
                            }
                            ss += "<td class='chakan'><a href='#' onclick=\"checkDada('" + delPORTALID + "','" + tableName + "','" + subjectCode + "')\">" +
                                "<i class='fa fa-eye' aria-hidden='true'></i>查看</a></td><td width='1'></td>" +
                                "<td class='shanchu'><a href='#' onclick=\"deleteDate('" + delPORTALID + "','" + tableName + "','" + subjectCode + "','"+ count +"')\">" +
                                "<i class='fa fa-trash-o fa-fw' aria-hidden='true'></i>删除</a></td></tr></table></td></tr>";
                        }
                        ss += "</tbody>";
                        s += "<td style='width:22%;'>操作</td></tr></thead>";
                        tabs = s + ss + "</table>";

                        tabs += "<div class='review-item clearfix'><div id='page_div" + tableName + "' style='padding-top: 25px; float: left;'>" +
                            "当前第&nbsp;<span style='color:blue;' id='currentPageNo" + tableName + "'></span>&nbsp;页,&nbsp;共&nbsp;<span style='color:blue;' id='totalPages" + tableName + "'></span>页，<span style='color:blue;' id='totalCount" + tableName + "'></span>" +
                            "条数据</div><div style='float: right;' ><div id='pagination" + tableName + "'></div></div></div>";

                        var item = {'id': tableName, 'name': tableName, 'closable': true, 'template': tabs};
                        closableTab.addTab(item); // 执行创建页签
                        fun_limit(subjectCode, tableName, data, searchKey);
                        closableTab.addTabComment({
                            'id': tableName,
                            'tableComment': tableComment,
                            'closable': true,
                            'template': tabs
                        })
                    } else {
                        tabs = "";
                        $(ids).html(" ");
                        var item = {'id': tableName, 'name': tableName, 'closable': true, 'template': tabs};
                        // 执行创建页签
                        closableTab.addTab(item);
                        var searchKey1=$(".input-group input").val();
                        var click="";
                        if(searchKey1===""||searchKey1==null){
                            click= " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>该表暂时没有数据</h5>";
                        }else{
                            click = " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>没有查询到相关数据</h5>";
                        }
                        $(ids).append(click);
                    }
                }
            });
        }

        //表数据，template模板
        function showTableDataTestTmpl(subjectCode, tableName, pageNo) {
            var ids = "#tab_container_" + tableName;
            $.ajax({
                type: "post",
                url: "${ctx}/showTableDataTestTmpl",
                data: {"subjectCode": subjectCode, "tableName": tableName, "pageNo": pageNo},
                dataType: "json",
                success: function (data) {
                    data.push("subjectCode",subjectCode);
                    data.push("tableName",tableName);
                    data.push("pageNo",pageNo);
                    var arr = data.columns;
                    // var dataType = data.dataType;
                    var columnComment = data.columnComment;
                    var dataArry = data.dataDatil;

                     var s = "<table id='" + tableName + "' class='table table-hover biaoge' spellcheck='0' border='0'>" +
                        "<thead><tr class='table_tr'>";
                    var il = 0;
                    if (dataArry.length > 0) {
                        for (var i = 0; i < arr.length; i++) {
                            if (il < 5) {
                                if (arr[i] === "PORTALID") {
                                    s += "<td style='display:none;' title=" + arr[i] + ">" + arr[i] + "</td>";
                                } else {
                                    s += "<td style='width:15%;'>" + arr[i] + "<br/><p title=" + columnComment[i] + ">" + columnComment[i] + "</p></td>";
                                    il++;
                                }
                            } else {
                               break;
                            }
                        }
                    }

                     var html=template("showTableDataTestTheadTmpl",data);
                }
            })
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
                    var arr = data.columns;
                    var dataType = data.dataType;
                    var columnComment = data.columnComment;
                    var dataArry = data.dataDatil;
                    var count=dataArry.length;
                    var delPORTALID;
                    var tabs = "";
                    var s = " ";
                    s = "<table id='" + tableName + "' class='table table-hover biaoge' spellcheck='0' border='0' style='width:100%;'>" +
                        "<thead ><tr tr class='table_tr'>";
                    //表头
                    var il = 0;
                    if (dataArry.length > 0) {
                        for (var i = 0; i < arr.length; i++) {
                            if (il < 5) {
                                if (arr[i] === "PORTALID") {
                                    s += "<td style='display:none;' title=" + arr[i] + ">" + arr[i] + "</td>";
                                } else {
                                    s += "<td style='width:15%;'>" + arr[i] + "<br/><p title=" + columnComment[i] + ">" + columnComment[i] + "</p></td>";
                                    il++;
                                }
                            } else {
                                s += "<td style='display:none;'title=" + arr[i] + ">" + arr[i] + "</td>";
                            }
                        }
                    }
                    var ss = "";
                    if (dataArry.length > 0) {
                        for (var key in dataArry) {
                            ss += "<tbody><tr>";
                            var d = dataArry[key];
                            var i = 0;
                            var j = 0;
                            for (var k in d) {
                                if (j < 5) {
                                    if (k === arr[i]) {
                                        if (dataType[i] === "datetime" && d[k] !== null && d[k] !== " ") {
                                            var date = d[k].split(".");
                                            d[k] = date[0];
                                        }
                                        if (k === "PORTALID") {
                                            delPORTALID = d[k];
                                            ss += "<td  style='display:none;' title='" + d[k] + "'>" + d[k] + "</td>";
                                        } else {
                                            ss += "<td title='" + d[k] + "' style='word-break:keep-all;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'><xmp style='font-family: Arial, 微软雅黑;'>" + d[k] + "</xmp></td>";
                                            j++;
                                        }
                                    } else {
                                        if (dataType[i] === "datetime" && d[arr[i]] !== null && d[arr[i]] !== " ") {
                                            var date = d[arr[i]].split(".");
                                            d[arr[i]] = date[0];
                                        }
                                        if (arr[i] === "PORTALID") {
                                            delPORTALID = d[arr[i]];
                                            ss += "<td style='display:none;' title='" + d[arr[i]] + "'>" + d[arr[i]] + "</td>";
                                        } else {
                                            ss += "<td title='" + d[arr[i]] + "' style='word-break:keep-all;white-space: nowrap;text-overflow: ellipsis;overflow: hidden;'><xmp style='font-family: Arial, 微软雅黑;'>" + d[arr[i]] + "</xmp></td>";
                                            j++;
                                        }
                                    }
                                    i++;
                                } else {
                                    if (k === arr[i]) {
                                        if (k === "PORTALID") {
                                            delPORTALID = d[k];
                                        }
                                    } else {
                                        if (arr[i] === "PORTALID") {
                                            delPORTALID = d[arr[i]];
                                        }
                                    }
                                    i++;
                                }
                            }
                            ss += "<td align='center'><table class='0' cellspacing='0' border='0' align='center'><tr>";
                            if (data.isSync === 0) {
                                ss += "<td class='bianji'><a src='#' onclick=\" updateData('" + delPORTALID + "','" + tableName + "','" + subjectCode + "')\">" +
                                    "<i class='fa fa-pencil-square-o' aria-hidden='true'></i>修改</a></td><td width='1'></td>";
                            }
                            ss += "<td class='chakan'><a href='#' onclick=\"checkDada('" + delPORTALID + "','" + tableName + "','" + subjectCode + "')\">" +
                                "<i class='fa fa-eye' aria-hidden='true'></i>查看</a></td><td width='1'></td>" +
                                "<td class='shanchu'><a href='#' onclick=\"deleteDate('" + delPORTALID + "','" + tableName + "','" + subjectCode + "','"+ count +"')\">" +
                                "<i class='fa fa-trash-o fa-fw' aria-hidden='true'></i>删除</a></td></tr></table></td></tr>";
                        }
                        ss += "</tbody>";
                        s += "<td style='width:22%;'>操作</td></tr></thead>";
                        tabs = s + ss + "</table>";

                        tabs += "<div class='review-item clearfix'><div id='page_div" + tableName + "' style='padding-top: 25px; float: left;'>" +
                            "当前第&nbsp;<span style='color:blue;' id='currentPageNo" + tableName + "'></span>&nbsp;页,&nbsp;共&nbsp;<span style='color:blue;' id='totalPages" + tableName + "'></span>页，<span style='color:blue;' id='totalCount" + tableName + "'></span>" +
                            "条数据</div><div style='float: right;' ><div id='pagination" + tableName + "'></div></div></div>";
                        $(ids).append(tabs);
                        fun_limit(subjectCode, tableName, data, searchKey);
                    } else {
                        $(ids).html(" ");
                        var click="";
                        if(searchKey===""||searchKey==null){
                            click= " <h5 style='font-size: 20px;text-align: center;margin-right: 30%;'>该表暂时没有数据</h5>";
                        }else{
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
                    var strs2 = data.COLUMN_NAME;
                    var dataTypeArr = data.DATA_TYPE;
                    var columnComments = data.COLUMN_COMMENT;
                    var autoAddArr = data.autoAdd;
                    var pkColumnArr = data.pkColumn;
                    var s = "";
                    var enumColumn = [];
                    for (var i = 0; i < strs2.length; i++) {
                        if (pkColumnArr[i] === "PRI" && autoAddArr[i] === "auto_increment") {
                            // s += "<input style='display:none;'  type='text' name=" + strs2[i] + " value='0'/>";
                        } else {
                            if (strs2[i] === "PORTALID") {
                                s += "<input style='display:none;' class='" + dataTypeArr[i] + "' type='text' name=" + strs2[i] + " value='0'/>";
                            } else {
                                s += "<tr><td>" + strs2[i] + "</td><td>" + dataTypeArr[i] + "</td><td>" + columnComments[i] + "</td>";

                                if (dataTypeArr[i] === "datetime") {
                                    s += "<td><input class='selectDataTime' style='width: 100%;height:100%;' id='" + strs2[i] + "' type='text'  placeholder='请选择'  /></td></tr>";
                                } else if (dataTypeArr[i] === "date") {
                                    s += "<td><input class='selectData'  style='width: 100%;height:100%;' id='" + strs2[i] + "' type='text'  placeholder='请选择'/></td></tr>";
                                } else if (dataTypeArr[i] === "time") {
                                    s += "<td><input class='DataTime' style='width: 100%;height:100%;' id='" + strs2[i] + "' type='text'  placeholder='请选择'  /></td></tr>";
                                } else {
                                    var flag = 0;
                                    if (data.alert === 1) {
                                        var enumDataList = data.enumDataList;
                                        for (var k = 0; k < enumDataList.length; k++) {
                                            if (strs2[i] === enumDataList[k].key) {
                                                flag = 1;
                                                enumColumn.push(strs2[i]);
                                                var sVal = enumDataList[k].value.split("|_|");
                                                var selects = "<select class='form-control' style='width: 100%;height:100%;'  name='" + strs2[i] + "' id='" + strs2[i] + "'>";
                                                for (var kk = 0; kk < sVal.length; kk++) {
                                                    if (sVal[kk] !== "" && sVal[kk] !== null) {
                                                        selects += "<option>" + sVal[kk] + "</option>";
                                                    }
                                                }
                                                selects += "</select>";
                                                s += "<td>" + selects + "</td></tr>";

                                            }
                                        }
                                    }
                                    if (flag === 0) {
                                        s += "<td><input  id='" + strs2[i] + "' class='form-control' style='width:100%;height=100%'  name=" + strs2[i] + "  dataType='" + dataTypeArr[i] + "' onblur=\"func_blur(this)\"/><p id='" + strs2[i] + "_id' style='display: none;color:red;font-size: 10px;'></p></td></tr>";
                                    }
                                }
                            }
                        }
                    }
                    var s_add = " <button id='addbtn' class='btn btn-success' data-dismiss='modal' onclick=\"addTablefuntion('" + dataTypeArr + "','" + strs2 + "','" + pkColumnArr + "','" + autoAddArr + "','" + enumColumn + "')\">保存</button>";
                        s_add+="<button type='button' data-dismiss='modal' class='btn default'>取消</button> ";
                    $("#addTable tbody").append(s);
                    $("#add_div").append(s_add);
                    $("#staticAddData").modal("show");
                    $('.selectData').datetimepicker({
                        minView: "month",//选择日期后，不会再跳转去选择时分秒
                        language:'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "yyyy-mm-dd"
                    });
                    $('.selectDataTime').datetimepicker({
                        language:'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        minuteStep:1,
                        format: "yyyy-mm-dd hh:ii:ss"
                    });
                    $('.DataTime').datetimepicker({
                        //第一种
                        language:'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "hh:ii:ss",
                        minView: 0,
                        minuteStep:1,
                        startView: 0
                    });
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
                if (S_column[i] === "PORTALID") {
                    datacon[i] = "";
                } else if (S_pkColumnArr[i] === "PRI" && S_autoAddArr[i] === "auto_increment") {
                    datacon[i] = "";
                } else {
                    var flag = 0;
                    // alert(enumColumns.length+".."+$("#" + S_column[i] + " option:selected").val());
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
                            //时间类型判断
                            // if (S_dataType[i] === "date" && datacon[i] !== null && datacon[i] !== "" && datacon[i] !== "null") {
                            //     // var reg = /^(\d{4})-(\d{2})-(\d{2})$/;
                            //     var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
                            //     var regExp = new RegExp(reg);
                            //     if (!regExp.test(datacon[i])) {
                            //         toastr.warning(S_column[i] + " 字段是时间格式,正确格式应为: xxxx-xx-xx ");
                            //         return;
                            //     }
                            // }
                            //
                            // if (datacon[i] !== "" && S_dataType[i] === "datetime") {
                            //     var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/;
                            //     var regExp = new RegExp(reg);
                            //     if (!regExp.test(datacon[i]) && datacon[i] !== null && datacon[i] !== "") {
                            //         toastr.warning(S_column[i] + " 字段正确格式应为: xxxx-xx-xx xx:xx:xx ");
                            //         return;
                            //     }
                            // }

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
                        toastr.error("添加数据失败，"+ data.prikey+"的值已存在！");
                    } else if (data.data === "1") {
                        toastr.success("添加成功!");
                        var searchKey=$(".input-group input").val();
                        // var searchKey = "";
                        clickPageButton(subjectCode, tableName, currentPage, searchKey);
                        $("#staticAddData").modal("hide");
                    } else if (data.data === "-1") {
                        toastr.error("添加数据失败，"+ data.prikey +"不能为空！");
                    }else if(data.data==="-3"){
                        toastr.error("添加数据失败！");
                    } else {
                        var arr = data.data.split("+");
                        if (arr[0] === "-2") {
                            toastr.error("添加数据失败，" + arr[1] + " 列不能为空！");
                        }
                    }
                },
                error: function () {
                    alert("error!!!");
                }
            })
        }

        function updateData(delPORTALID,tableName,subjectCode) {
            $("#form_id").html(" ");
            $("#update_tbody").html(" ");
            $("#update_div").html("");
            //获得当前页码
            var currentPage = $("#currentPageNo"+tableName +"").html();
            $.ajax({
                type: "post",
                url: "${ctx}/toupdateTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName, "PORTALID": delPORTALID},
                dataType: "json",
                success: function (data) {
                    var dataTypeArr = data.DATA_TYPE;
                    var columnComments = data.COLUMN_COMMENT;
                    var COLUMN_TYPE = data.COLUMN_TYPE;
                    var strs2=data.COLUMN_NAME;
                    var strs = data.data;
                    var delPORTALID="";
                    var s_tbody="";
                    var enumColumn = [];
                    for (var i = 0; i < strs2.length; i++) {
                        var getinput_id=strs2[i]+"_coldata";
                        if (strs2[i] === "PORTALID") {
                            delPORTALID = strs[i];
                        }else{
                            s_tbody+= "<tr><td style='width:20%;'>" + strs2[i] + "</td>" +
                                "<td style='width:20%;'> " + COLUMN_TYPE[i] + "</td>" +
                                "<td style='width:20%;'>" + columnComments[i] + "</td>";
                            if(dataTypeArr[i]==="datetime") {
                                if(strs[i]!==" " && strs[i]!==null) {
                                    var date = strs[i].split(".");
                                    strs[i] = date[0];
                                    s_tbody += "<td style='width:40%;'><input class='selectDataTime' id='" + getinput_id + "' type='text' style='width:100%;height=100%' placeholder='请选择' title='" + strs[i] + "' value='" + strs[i] + "' /></td></tr>";
                                }else{
                                    s_tbody += "<td  style='width:40%;'><input class='selectDataTime' id='" + getinput_id + "' type='text' style='width:100%;height=100%' placeholder='请选择' title='" + strs[i] + "' value='" + strs[i] + "' /></td></tr>";
                                }
                            }else if(dataTypeArr[i]==="date"){
                                s_tbody+="<td  style='width:40%;'><input class='selectData' id='"+ getinput_id +"' type='text' style='width:100%;height=100%' placeholder='请选择' title='" + strs[i] + "' value='" + strs[i] + "'  /></td></tr>";
                            }else if(dataTypeArr[i]==="time"){
                                s_tbody += "<td  style='width:40%;'><input class='selectTime' id='" + getinput_id + "' type='text' style='width:100%;height=100%' placeholder='请选择' title='" + strs[i] + "' value='" + strs[i] + "' /></td></tr>";
                            }else {
                                var enumDataList = data.enumDataList;
                                var flag = 0;
                                for (var k = 0; k < enumDataList.length; k++) {
                                    if (strs2[i] === enumDataList[k].key) {
                                        flag = 1;
                                        enumColumn.push(strs2[i]);
                                        var sVal = enumDataList[k].value.split("|_|");
                                        var selects = "<select class='form-control' style='width: 100%;height:100%;'  name='" + strs2[i] + "' id='" + getinput_id + "'>";
                                        for (var kk = 0; kk < sVal.length; kk++) {
                                            if (strs[i] === sVal[kk]) {
                                                selects += "<option selected>" + sVal[kk] + "</option>";
                                            } else {
                                                if (sVal[kk] !== "" && sVal[kk] !== null) {
                                                    selects += "<option>" + sVal[kk] + "</option>";
                                                }
                                            }

                                        }
                                        selects += "</select>";
                                        s_tbody += "<td>" + selects + "</td></tr>";

                                    }
                                }
                                if (flag === 0) {
                                    s_tbody += "<td  style='width:40%;'><input title='" + strs[i] + "' class='form-control'  type='text' id='" + getinput_id + "' style='width:100%;height=100%'   name=" + strs2[i] + " value='" + strs[i] + "' dataType='" + dataTypeArr[i] + "' onblur=\"func_blur(this)\"/><p id='" + strs2[i] + "_id' style='display: none;color:red;font-size: 10px;'></p></td></tr>";

                                }
                            }
                        }
                    }
                    var s_save = "<button id='btn_save'  class='btn btn-success' data-dismiss='modal' onclick=\" saveData('" + tableName + "','" + subjectCode + "','" + dataTypeArr + "','" + currentPage + "','" + strs2 + "','" + delPORTALID + "','" + enumColumn + "')\">保存</button> ";
                    s_save+="<button type='button' data-dismiss='modal' class='btn default'>取消</button> ";
                    $("#update_tbody").append(s_tbody);
                    $("#update_div").append(s_save);
                    $("#staticUpdateData").modal("show");

                    $('.selectData').datetimepicker({
                        minView: "month",//选择日期后，不会再跳转去选择时分秒
                        language:'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "yyyy-mm-dd"
                    });
                    $('.selectDataTime').datetimepicker({
                        //第一种
                        language:'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "yyyy-mm-dd hh:ii:ss",
                        minView: 0,
                        minuteStep:1
                    });
                    $('.selectTime').datetimepicker({
                        //第一种
                        language:'zh-CN',
                        autoclose: true,//选中之后自动隐藏日期选择框
                        clearBtn: true,//清除按钮
                        todayBtn: false,//今日按钮
                        format: "hh:ii:ss",
                        // timeFormat: 'hh:mm:ss:l',
                        minView: 0,
                        minuteStep:1,
                        pickDate:false,
                        startView: 0
                    });
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

                //date数据类型判断
                // if (dataType === "date" &&dataValue !== null && dataValue !=="") {
                //     var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
                //     var regExp = new RegExp(reg);
                //     if (!regExp.test(checkdataArr[i])) {
                //         toastr.warning(columnName[i]+" 字段是时间格式,正确格式应为: xxxx-xx-xx ");
                //         return;
                //     }
                // }
                //datetime数据类型判断
                // if (dataTypeArr[i] === "datetime" && checkdataArr[i] !== null && checkdataArr[i] !== "" && checkdataArr[i] !== "null") {
                //     var reg = /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/;
                //     var regExp = new RegExp(reg);
                //     if (!regExp.test(checkdataArr[i])) {
                //         toastr.warning(columnName[i]+" 字段正确格式应为: xxxx-xx-xx xx:xx:xx ");
                //         return;
                //     }
                // }
                //decimal数据类型判断

                if (dataType === "decimal" && dataValue !== null && dataValue !=="") {
                    // var col_type_str = S_columnType[i].split(",");
                    // var m = col_type_str[0].split("(")[1];
                    // var s = col_type_str[1].split(")")[0];
                    // var n = m - s;
                    // var pattern = /^(-?\d+)(\.\d+)?$/;    //浮点数
                    //     var reg = new RegExp(pattern);
                    //     if (reg.test(checkdataArr[i])) {
                    //     var ss = checkdataArr[i].split(".");
                    //     if (ss[0].length > n) {
                    //         toastr.warning(columnName[i]+" 字段数据超出范围！ ");
                    //         return;
                    //     }
                    // } else {
                    //     toastr.warning(columnName[i]+" 字段是decimal类型！ ");
                    // }
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
        function saveData(tableName, subjectCode, dataType, currentPage, alert_column, delPORTALID, enumColumn) {
            debugger
            var dataTypeArr = dataType.split(",");
            var columnName=alert_column.split(",");
            var checkdataArr = [];
            var checkdataArrs=[];
            var enumColumns = enumColumn.split(",");
            for(var i =0; i <columnName.length; i++){
                if(columnName[i]==="PORTALID"){
                    var coldata={};
                    coldata["name"]=columnName[i];
                    coldata["value"]=delPORTALID;
                    checkdataArrs.push(coldata);
                    checkdataArr.push(delPORTALID);
                }else{
                   var s= document.getElementById(columnName[i]+"_coldata").value;
                    var coldata={};
                    coldata["name"]=columnName[i];
                    coldata["value"]=s;
                    checkdataArrs.push(coldata);
                    checkdataArr.push(s);
                }
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
                    "delPORTALID": delPORTALID,
                    "enumColumn": enumColumns.join(",")
                },
                dataType: "json",
                success: function (data) {
                    if (data.data === "0") {
                        //错误消息提示，默认背景为浅红色
                        toastr.error("更新数据失败!");
                    } else if (data.data === "1") {
                        //成功消息提示，默认背景为浅绿色
                        toastr.success("更新成功!");
                        $("#staticUpdateData").modal("hide");
                        // var searchKey = "";
                        var searchKey=$(".input-group input").val();
                        clickPageButton(subjectCode, tableName, currentPage, searchKey);
                    } else if(data.data === "-1"){
                        toastr.error("主键重复！");
                    }else{
                        if (data.data === "-2") {
                            toastr.error("更新数据失败，" + data.col + " 列不能为空！");
                        }
                    }
                },
                error: function () {
                    alert("error!!!!!");
                }
            })
        }

        //删除数据
        function deleteDate(delPORTALID, tableName, subjectCode,j) {
            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>", function (r) {
                if (r) {
                    var currentPage = $("#currentPageNo"+tableName +"").html();
                    if(j==="1" && currentPage!=="1"){
                        currentPage=currentPage-1;
                    }
                    //获得当前页码
                    //获得当前页码

                    $.ajax({
                        url: "${ctx}/deleteData",
                        type: "POST",
                        dataType: "json",
                        data: {
                            "subjectCode": subjectCode,
                            "tableName": tableName,
                            "delPORTALID": delPORTALID
                        },
                        success: function (data) {
                            if (data.data === "1") {
                                toastr.success("删除成功!");
                                // var searchKey = "";
                                var searchKey=$(".input-group input").val();
                                clickPageButton(subjectCode, tableName, currentPage, searchKey);

                            }
                            if (data.data === "0") {
                                toastr.error("删除失败！");
                            }
                        }
                    })
                }
            })
        }

        //查看详情
        function checkDada(delPORTALID,tableName,subjectCode) {
            $("#checkTable tbody").html(" ");
            $.ajax({
                type: "post",
                url: "${ctx}/toCheckTableData",
                data: {"subjectCode": subjectCode, "tableName": tableName, "PORTALID": delPORTALID},
                dataType: "json",
                success: function (data) {
                    template.helper("dateFormat", formatDate);
                    var html=template("checkdataTmpl",data);
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



    </script>
</div>
</html>
