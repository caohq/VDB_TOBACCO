<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2018/10/31
  Time: 10:42
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>

<html>

<head>
    <title>用户与组管理</title>
    <link href="${ctx}/resources/bundles/rateit/src/rateit.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/select2/select2.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .error-message {
            color: red;
        }

        .page-content {
            height: 830px;
            min-height: 830px;
        }

        .shenhe a {
            border-radius: 6px !important;
        }
    </style>
</head>

<body>


<div class="col-md-12">
    <div class="tabbable-custom ">
        <!-- tab header --->
        <ul class="nav nav-tabs ">
            <li class="active">
                <a href="#subjectContent" data-toggle="tab" id="showSubjectContent" style="white-space:nowrap;">
                    节点管理
                </a>
            </li>
            <li>
                <a href="#themeContent" data-toggle="tab">
                    主题库管理</a>
            </li>
        </ul>
        <!--tab content-->
        <div class="tab-content">

            <!--节点管理标签页-->
            <div class="tab-pane active" id="subjectContent" style="min-height: 400px">
                <div class="row">
                    <div class="col-xs-12 col-md-12 col-lg-12">

                        <!--用户管理标签页: 用户筛选条件-->
                        <div class="alert alert-info" role="alert">
                            <div class="row">
                                <div class="col-md-12 form-inline">
                                    <div style="float: left;width: 23%">
                                        <label class="control-label" style="color: black">节点名称:</label>
                                        <input type="text" id="subjectNameFilter" name="subjectNameFilter" style="width: 69%"
                                                placeholder="节点名称"
                                               class="form-control search-text"/>
                                    </div>
                                    <%--<div style="float: left;width: 23%">--%>
                                        <%--<label class="control-label" style="color: black">用户名:</label>--%>
                                        <%--<input type="text" id="userNameFilter" style="width: 69%"--%>
                                               <%--name="userNameFilter" placeholder="用户名"--%>
                                               <%--class="form-control search-text"/>--%>
                                    <%--</div>--%>
                                    <%--<div style="float: left;width: 23%">--%>
                                        <%--<label class="control-label" style="color: black">用户组:</label>--%>
                                        <%--<select name='groupsFilter' id='groupsFilter' style="width: 69%"--%>
                                                <%--multiple="multiple" class="form-control select2me"--%>
                                                <%--style="width: 200px;">--%>
                                            <%--<c:forEach var="group" items="${allGroupList}">--%>
                                                <%--<option value="${group.groupName}" id="${group.id}"--%>
                                                        <%--style="width: 150px; height: 30px;">${group.groupName}</option>--%>
                                            <%--</c:forEach>--%>
                                        <%--</select>--%>
                                    <%--</div>--%>
                                    <div style="float: left;width: 10%;text-align: center;margin-left:-25px;margin-top: 3px;">
                                        <button id="searchUserBtn" name="searchUserBtn" onclick="searchSubject();"
                                                class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查&nbsp;&nbsp;询
                                        </button>
                                    </div>
                                    <div style="float: left;width: 21%;margin-top: 3px;">
                                        <button id="addUserBtn" name="addUserBtn"
                                                class="btn info green btn-sm" onclick="addSubject()">
                                            <i  class="glyphicon glyphicon-plus"></i>
                                            &nbsp;&nbsp;新增${applicationScope.menus['organization_title']}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--用户管理标签页: 用户列表-->
                        <div class="table-message">列表加载中......</div>
                        <div class="table-scrollable">
                            <table class="table table-striped table-bordered table-advance table-hover">
                                <thead>
                                <tr id="table_List1">
                                    <th style="display:none;">${applicationScope.menus['organization_title']}ID</th>
                                    <th style="width: 3%;">编号</th>
                                    <th style="width: 10%;">${applicationScope.menus['organization_title']}名称</th>
                                    <th style="width: 5%;">${applicationScope.menus['organization_title']}代码</th>
                                    <th style="width: 5%;">管理员账号</th>
                                    <th style="width: 5%;">负责人</th>
                                    <th style="width: 5%;">电话</th>
                                    <th style="width: 10%;">操作</th>
                                </tr>
                                </thead>

                                <tbody id="subjectList">

                                </tbody>

                            </table>
                        </div>

                        <!--用户管理标签页: 分页-->
                        <div class="row margin-top-20">
                            <div class="page-message col-md-6 margin-top-10">
                                当前第&nbsp;<span style="color:blue;"
                                               id="pageNum"></span>&nbsp;页,&nbsp;共&nbsp;<span
                                    style="color:blue;"
                                    id="totalPages"></span>页，
                                共<span style="color:blue;" id="total"></span>&nbsp;条数据
                            </div>
                            <div class="page-list col-md-6">
                                <div id="paginationForUser" style="float: right"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!--主题库 tab-->
            <div class="tab-pane" id="themeContent">
                <div class="row">
                    <div class="col-xs-12 col-md-12 col-lg-12">
                        <div class="alert alert-info" role="alert">
                            <!--查询条件 -->
                            <div class="row">
                                <div class="col-md-12 form-inline">

                                    <label class="control-label" style="color: black;">用户组名:</label>
                                    <input type="text" id="themeName" name="themeName"
                                           class="form-control search-text ">
                                    &nbsp;&nbsp;&nbsp;&nbsp;

                                    <button id="btnSearch" name="btnSearch" onclick="search();"
                                            class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查询
                                    </button>
                                    &nbsp;&nbsp;
                                    <button id="btnAdd" name="btnAdd" onclick="" class="btn info green btn-sm"><i
                                            class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增主题库
                                    </button>
                                </div>
                            </div>

                        </div>
                        <div class="table-message-group">列表加载中......</div>
                        <div class="table-scrollable">

                            <table class="table table-striped table-bordered table-advance table-hover">
                                <thead>
                                <tr id="table_List2">
                                    <th style="width: 5%;">编号</th>
                                    <th style="width: 15%;">
                                        主题库名
                                    </th>
                                    <th style="width: 10%;">
                                        主题库代码
                                    </th>
                                    <th style="width: 30%;">描述</th>
                                    <th style="width: 17%;">创建时间</th>
                                    <th style="width: 25%;">操作</th>
                                </tr>
                                </thead>
                                <tbody id="groupList"></tbody>
                            </table>
                        </div>
                        <div class="row margin-top-20">
                            <div class="col-md-6 margin-top-10" id="message-group1">
                                当前第&nbsp;<span style="color:blue;"
                                               id="currentPageNo"></span>&nbsp;页,&nbsp;共&nbsp;<span
                                    style="color:blue;"
                                    id="totalPages"></span>页，<span
                                    style="color:blue;" id="totalCount"></span>&nbsp;条数据
                            </div>
                            <div class="col-md-6" id="message-group2">
                                <div id="pagination" style="float: right"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--新增主题库-->
<div class="modal fade" tabindex="-1" role="dialog" id="addModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">新增主题库</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="addThemeForm" method="post" accept-charset="utf-8" role="form"
                      onfocusout="true">
                    <div class="form-group">
                        <label for="themeNameAdd" class="col-sm-3 control-label">主题库名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="themeNameAdd" name="themeName"
                                   placeholder="请输入主题库名称" required="required">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="themeCodeAdd" class="col-sm-3 control-label">主题库代码<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="themeCodeAdd" name="themeCode"
                                   placeholder="请输入主题库代码" required="required">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="descAdd" class="col-sm-3 control-label">描述<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <textarea type="text" class="form-control" cols="30" rows="5" id="descAdd" name="describe"
                                      placeholder="请输入主题库描述信息" required="required"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" onclick="submitAddData();"><i
                        class="glyphicon glyphicon-ok"></i>保存
                </button>
                <button type="button" data-dismiss="modal" onclick="resetData();" class="btn  default">取消</button>
            </div>
        </div>
    </div>
</div>

<!--修改主题库-->
<div class="modal fade" tabindex="-1" role="dialog" id="editModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">修改主题库</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="editThemeForm" method="post" accept-charset="utf-8" role="form"
                      onfocusout="true">
                    <div class="form-group">

                        <input type="hidden" class="form-control"
                               id="themeId"
                               name="id" value=""/>

                        <input type="hidden" class="form-control"
                               id="groupUsers"
                               name="users"/>

                        <label for="themeNameEdit" class="col-sm-3 control-label">主题库名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="themeNameEdit" name="themeName"
                                   placeholder="请输入用户组名称"  required="required">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="themeCodeEdit" class="col-sm-3 control-label">主题库代码<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" cols="30" rows="5" id="themeCodeEdit" name="themeCode"
                                      placeholder="请输入主题库代码" required="required" readonly />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="describeEdit" class="col-sm-3 control-label">描述<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <textarea type="text" class="form-control" cols="30" rows="5" id="describeEdit" name="describe"
                                      placeholder="请输入用户组描述信息" required="required"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn green" onclick="submitEditData();"><i
                        class="glyphicon glyphicon-ok"></i>保存
                </button>
                <button type="button" data-dismiss="modal" class="btn  default">取消</button>
            </div>
        </div>
    </div>
</div>


<!-- 主题库列表-->
<script type="text/html" id="themeTmpl">
    {{each list}}
    <tr>
        <td style="display:table-cell; vertical-align:middle ; text-align: center;">
            {{$index+1}}
        </td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;"><a
                href="javascript:editData('{{$value.id}}');">{{$value.themeName}}</a>
        </td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;"><a
                href="javascript:editData('{{$value.id}}');">{{$value.themeCode}}</a>
        </td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{$value.describe}}</td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{dateFormat($value.createTime)}}
        </td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;" id="a{{$value.id}}">
            <table class="0" cellspacing="0" border="0" align="center">
                <tr>
                    <%--<td class="shenhe"><a href="#" onclick="addUserData('{{$value.id}}')"><i class="fa fa-user"--%>
                                                                                             <%--aria-hidden="true"></i>添加用户</a>--%>
                    <%--</td>--%>
                    <td width="1"></td>
                    <td class="bianji"><a href="#" onclick="editData('{{$value.id}}')"><i class="fa fa-pencil-square-o"
                                                                                          aria-hidden="true"></i>修改</a>
                    </td>
                    <td width="1"></td>
                    <td class="shanchu"><a href="#" onclick="deleteTheme('{{$value.id}}')"><i class="fa fa-trash-o fa-fw"
                                                                                             aria-hidden="true"></i>删除</a>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
    {{/each}}
</script>

<!--专业库列表-->
<script type="text/html" id="subjectListTable">
    {{each list}}
    <tr>
        <td style="display:none;">{{$value.id}}</td>
        <td style="display:none;">{{$value.serialNo}}</td>
        <td style="text-align: center;">{{ $index + 1}}</td>
        <td style="text-align: center">{{$value.subjectName}}</td>
        <td style="text-align: center">{{$value.subjectCode}}</td>
        <td style="text-align: center">{{$value.admin}}</td>
        <td style="text-align: center">{{$value.contact}}</td>
        <td style="text-align: center">{{$value.phone}}</td>
        <td id="{{$value.id}}">
            <table class="0" cellspacing="0" border="0" align="center">
                <tr>
                    <td class="bianji" name="{{$value.id}}"><a href="javascript:;" onclick="updateSubject(this);"><i
                            class="fa fa-pencil-square-o" aria-hidden="true"></i>修改</a></td>
                    <td width="1"></td>
                    <td class="shanchu" name="{{$value.id}}"><a href="javascript:;" onclick="deleteSubject(this);"><i
                            class="fa fa-trash-o fa-fw" aria-hidden="true"></i>删除</a></td>
                </tr>
            </table>
        </td>
    </tr>
    {{/each}}
</script>

<script type="text/html" id="subjectThemeGroup">
{{each list as value}}
<option  value="{{value.themeCode}}"> {{value.themeName}}</option>
    {{/each}}
    </script>
<%--新增数据节点--%>
<div id="addSubjectDialog" class="modal fade" tabindex="-1" aria-hidden="true" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
                <h4 id="titleForAddSubjectDialog" class="modal-title">
                    新增${applicationScope.menus['organization_title']}</h4>
            </div>

            <!--subject info input form-->
            <div class="modal-body">
                <form id="addSubjectForm" class="form-horizontal" role="form" method="post"
                      enctype="multipart/form-data" accept-charset="utf-8" onfocusout="true">

                    <div class="form-group">
                        <label class="col-md-3 control-label" for="subjectName">
                            ${applicationScope.menus['organization_title']}名称<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入${applicationScope.menus['organization_title']}名称"
                                   id="subjectName"
                                   name="subjectName" required="required"/>
                        </div>
                    </div>

                    <!--SubjectCode需要保证唯一性，为了保证唯一，需要通过后端数据库交互验证是否已经存在-->
                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            ${applicationScope.menus['organization_title']}代码<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入${applicationScope.menus['organization_title']}代码"
                                   id="subjectCode"
                                   name="subjectCode" required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="showImgIfExist" hidden>
                            <img style="width: 20%;margin-left: 27%;margin-bottom: 2%;"/>
                        </div>
                        <label class="col-md-3 control-label">
                            图片<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="file" id="image" name="image" class="form-control file" placeholder="请选择一个本地图片"
                                   accept="image/gif, image/jpeg, image/png, image/jpg" required="required">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            ${applicationScope.menus['organization_title']}简介
                        </label>
                        <div class="col-md-9">
                                <textarea class="form-control"
                                          placeholder="请输入${applicationScope.menus['organization_title']}简介" id="brief"
                                          name="brief"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            管理员账号<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入管理员账号" id="admin" name="admin"
                                   required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            管理员密码<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入管理员密码" id="adminPasswd"
                                   name="adminPasswd" required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            联系人<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入联系人姓名" id="contact" name="contact"
                                   required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            联系电话<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入联系人电话" id="phone" name="phone"
                                   required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            Email
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入联系人Email" id="email" name="email"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            序号<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入序号，只能输入数字" id="serialNo"
                                   name="serialNo" required="required" onkeyup="this.value=this.value.replace(/\D/g,'')"
                                   onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            主题库<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <%--选择主题库--%>
                            <select id="subjectThemeName" name="subjectThemeName" class="form-control" name="themeGroup">

                            </select>
                        </div>
                    </div>

                </form>
            </div>

            <!--buttons to submit or cancel-->
            <div class="modal-footer">
                <button id="saveSubjectAddBtn" class="btn btn-success" onclick="agreeAddSubject();">
                    保存
                </button>
                <button id="cancelSubjectAddBtn" class="btn default" data-dismiss="modal">
                    取消
                </button>
            </div>
        </div>
    </div>
</div>

<%--修改数据节点--%>
<div id="updateSubjectDialog" class="modal fade" tabindex="-1" aria-hidden="true" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
                <h4 id="titleForUpdateSubjectDialog" class="modal-title">
                    修改${applicationScope.menus['organization_title']}</h4>
            </div>
            <div class="modal-body">
                <form id="updateSubjectForm" class="form-horizontal" role="form" method="post">
                    <div class="form-group">
                        <label class="col-md-3 control-label" for="subjectName" style="display:none;">
                            ${applicationScope.menus['organization_title']}id（不显示）
                        </label>
                        <div style="display:none;">
                            <input type="text" class="form-control" id="idM" name="id"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label" for="subjectName">
                            ${applicationScope.menus['organization_title']}名称<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入${applicationScope.menus['organization_title']}名称"
                                   id="subjectNameM"
                                   name="subjectName" required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            ${applicationScope.menus['organization_title']}代码<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入${applicationScope.menus['organization_title']}代码"
                                   id="subjectCodeM"
                                   name="subjectCode" required="required" readonly="readonly"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="showImgIfExist" hidden>
                            <img style="width: 20%;margin-left: 27%;margin-bottom: 2%;"/>
                        </div>
                        <label class="col-md-3 control-label">
                            图片
                        </label>
                        <div class="col-md-9">
                            <input type="file" id="imageM" name="image" class="form-control file"
                                   placeholder="请选择一个本地图片" accept="image/gif, image/jpeg, image/png, image/jpg">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            ${applicationScope.menus['organization_title']}简介
                        </label>
                        <div class="col-md-9">
                                <textarea class="form-control"
                                          placeholder="请输入${applicationScope.menus['organization_title']}简介" id="briefM"
                                          name="brief"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            管理员账号<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入管理员账号" id="adminM" name="admin"
                                   required="required" readonly="readonly"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <%--<label class="col-md-3 control-label">
                            管理员密码<span style="color: red;">*</span>
                        </label>--%>
                        <label class="col-md-3 control-label" for="resetPassword">
                            <%--密&nbsp;&nbsp;&nbsp;&nbsp;码<span style="color: red;">*</span>--%>
                            <input type="checkbox" id="resetPassword">
                            重置密码<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" disabled="disabled" class="form-control" placeholder="请输入管理员密码"
                                   id="adminPasswdM" name="adminPasswd" required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            联系人<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入联系人姓名" id="contactM" name="contact"
                                   required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            联系电话<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入联系人电话" id="phoneM" name="phone"
                                   required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            Email
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入联系人Email" id="emailM" name="email"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            序号
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入序号，只能输入数字" id="serialNoM"
                                   name="serialNo" readonly="readonly" onkeyup="this.value=this.value.replace(/\D/g,'')"
                                   onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            主题库<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9" id="updateThemeDiv">
                            <%--选择主题库--%>
                            <%--<select id="updateSubjectThemeName" name="updateSubjectThemeName" class="form-control" name="themeGroup">--%>

                            <%--</select>--%>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button id="agreeUpdateSubjectBtn" class="btn btn-success" onclick="agreeUpdateSubject();">
                    保存
                </button>
                <button id="cancelUpdateSubjectBtn" class="btn default" data-dismiss="modal">
                    取消
                </button>
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
    <script src="${ctx}/resources/js/regex.js"></script>
    <script src="${ctx}/resources/bundles/jquery/jquery.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrapv3.3/js/bootstrap.min.js"></script>

    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-validation/js/jquery.validate.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-validation/js/additional-methods.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-validation/js/localization/messages_zh.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/select2/select2.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/form-validation/form-validation.js"></script>


    <script type="text/javascript">
        var ctx = '${ctx}';
        var currentGroupNo = 1;
        var validAddData;
        var validEditData;

        $(function () {
            getSubject(1);

            $("#resetPassword").on('click', function () {
                var currentTarget = event.currentTarget;
                var isChecked = $(currentTarget).is(":checked");
                if (isChecked) {
                    $("#adminPasswdM").removeAttrs("disabled");
                } else {
                    $("#adminPasswdM").attr("disabled", "disabled");
                }
            });
            template.helper("dateFormat", formatDate);
            getData(1);

            $(".search-text").keydown(function (event) {
                if (event.keyCode == 13) {
                    getData(1);
                }
            });

            toastr.options = {
                "closeButton": true,
                "debug": false,
                "positionClass": "toast-top-right",
                "onclick": null,
                "showDuration": "1000",
                "hideDuration": "1000",
                "timeOut": "5000",
                "extendedTimeOut": "1000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut"
            };

            validAddData = {
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "", // validate all fields including form hidden input
                rules: {
                    themeName: {
                        required: true,
                        remote:
                            {
                                url: "isExist",
                                type: "get",
                                dataType: "json",
                                data:
                                    {
                                        'themeName': function () {
                                            return $("#themeNameAdd").val();
                                        }
                                    }
                            }
                    },
                    desc: {
                        required: true
                    }
                },
                messages: {
                    themeName: {
                        required: "请输入主题库名称",
                        // remote: "此名称己存在"
                    },
                    desc: {
                        required: "请输入主题库描述信息"
                    }
                },
                errorPlacement: function (error, element) { // render error placement for each input type
                    if (element.parent(".input-group").size() > 0) {
                        error.insertAfter(element.parent(".input-group"));
                    } else {
                        error.insertAfter(element); // for other inputs, just perform default behavior
                    }
                },
                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                }
            };

            validEditData = {
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "", // validate all fields including form hidden input
                rules: {
                    themeName: {
                        required: true
                    },
                    desc: {
                        required: true
                    }
                },
                messages: {
                    themeName: {
                        required: "请输入主题库名称"
                    },
                    desc: {
                        required: "请输入主题库描述信息"
                    }
                },
                errorPlacement: function (error, element) { // render error placement for each input type
                    if (element.parent(".input-group").size() > 0) {
                        error.insertAfter(element.parent(".input-group"));
                    } else {
                        error.insertAfter(element); // for other inputs, just perform default behavior
                    }
                },
                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                }
            };

            $("#addThemeForm").validate(validAddData);
            $("#editGroupForm").validate(validEditData);

            //新建、修改专业库对话框的验证
            var addSubjectValid = {
                errorElement: "span",
                errorClass: "error-message",
                focusInvalid: false,
                rules: {
                    subjectName: "required",
                    subjectCode: {
                        required: true,
                        remote:
                            {
                                url: "${ctx}/subjectMgmt/querySubjectCode",
                                type: "get",
                                data:
                                    {
                                        'subjectCode': function () {
                                            return $("#subjectCode").val();
                                        }
                                    },
                                dataType: "json"
                            }
                    },
                    image: "required",
                    admin: {
                        required: true,
                        remote:
                            {
                                url: "${ctx}/subjectMgmt/queryAdmin",
                                type: "get",
                                data:
                                    {
                                        'admin': function () {
                                            return $("#admin").val();
                                        }
                                    },
                                dataType: "json"
                            }
                    },
                    adminPasswd: {
                        required: true,
                        minlength: 6
                    },
                    contact: "required",
                    phone:
                        {
                            required: true,
                            maxlength: 11,
                            maxlength: 11,
                            isphoneNum: true
                        },
                    email: {
                        required: false,
                        email: true
                    },
                    serialNo: "required"
                },
                messages: {
                    subjectName: "请输入${applicationScope.menus['organization_title']}名称",
                    subjectCode: {
                        required: "请输入${applicationScope.menus['organization_title']}代码",
                        remote: "此${applicationScope.menus['organization_title']}代码已经存在！"
                    },
                    image: "请选择一个图片",
                    admin: {
                        required: "请输入${applicationScope.menus['organization_title']}管理员账号",
                        remote: "此${applicationScope.menus['organization_title']}管理员账号已经存在！"
                    },
                    adminPasswd: {
                        required: "请输入${applicationScope.menus['organization_title']}管理密码",
                        minlength: "密码至少为6位"
                    },
                    contact: "请输入${applicationScope.menus['organization_title']}联系人",
                    phone: {
                        required: "请输入手机号",
                        maxlength: "请填写11位的手机号",
                        minlength: "请填写11位的手机号",
                        isphoneNum: "请填写正确的手机号码"
                    },
                    email: "请输入一个正确的email",
                    serialNo: "请输入${applicationScope.menus['organization_title']}的序号"
                }
            };

            jQuery.validator.addMethod("isphoneNum", function (value, element) {
                var length = value.length;
                var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
                return this.optional(element) || (length == 11 && mobile.test(value));
            }, "请填写正确的手机号码");

            var updateSubjectValid = {
                errorElement: 'span',
                errorClass: 'error-message',
                focusInvalid: false,
                rules: {
                    subjectName: "required",
                    subjectCode: "required",
                    admin: "required",
                    adminPasswd: {
                        required: true,
                        minlength: 6
                    },
                    contact: "required",
                    phone:
                        {
                            required: true,
                            maxlength: 11,
                            maxlength: 11,
                            isphoneNum: true
                        },
                    email: {
                        required: false,
                        email: true
                    },
                    serialNo: "required"
                },
                messages: {
                    subjectName: "请输入${applicationScope.menus['organization_title']}名称",
                    subjectCode: "请输入${applicationScope.menus['organization_title']}代码",
                    admin: "请输入${applicationScope.menus['organization_title']}管理员账号",
                    adminPasswd: {
                        required: "请输入${applicationScope.menus['organization_title']}管理密码",
                        minlength: "密码至少为6位"
                    },
                    contact: "请输入${applicationScope.menus['organization_title']}联系人",
                    phone: {
                        required: "请输入手机号",
                        maxlength: "请填写11位的手机号",
                        minlength: "请填写11位的手机号",
                        isphoneNum: "请填写正确的手机号码"
                    },
                    email: "请输入一个正确的email",
                    serialNo: "请输入${applicationScope.menus['organization_title']}的序号"
                }
            };

            $("#updateSubjectForm").validate(updateSubjectValid);
            $("#addSubjectForm").validate(addSubjectValid);

            $("#imageM,#image").on("change", function (item) {
                showUploadFileAsDataURL(item);
            })

            $("#showSubjectContent").click(function () {
                location.reload();
            });
        });

        //获得主题库列表
        function getData(pageNo) {
            $.ajax({
                url: "${ctx}/theme/getThemeList",
                type: "get",
                dataType: "json",
                data: {
                    "themeName": $.trim($("#themeName").val()),
                    "pageNo": pageNo,
                    "pageSize": 10
                },
                success: function (data) {
                    console.log("-----------------")
                    console.log(data)

                    var html = template("themeTmpl", data);
                    $("#groupList").empty();
                    $("#groupList").append(html);
                    if (data.list.length == 0) {
                        $(".table-message-group").html("暂时没有数据")
                        $("#message-group1").hide()
                        $("#message-group2").hide()
                        return
                    }
                    $(".table-message-group").html("")
                    $("#message-group1").show()
                    $("#message-group2").show()
                    $("#currentPageNo").html(data.currentPage);
                    currentGroupNo = data.currentPage;
                    $("#totalPages").html(data.totalPages);
                    $("#totalCount").html(data.totalCount);
                    if (data.totalCount == 0) {
                        $("#currentPageNo").html("0");
                    }

                    if ($("#pagination .bootpag").length != 0) {
                        $("#pagination").off();
                        $('#pagination').empty();
                    }

                    $('#pagination').bootpag({
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
                        getData(num);
                        currentGroupNo = num;
                    });
                }
            });
        }

        function search() {
            getData(1);
        }
        //删除主题库
        function deleteTheme(id) {

            bootbox.confirm("<span style='font-size:16px;'>确定要删除此条记录吗？</span>", function (r) {
                if (r) {
                    var currentPageListSize = $.trim($("div.active table:eq(0)>tbody>tr:last>td:eq(0)").text());
                    if (currentPageListSize === "1") {
                        currentGroupNo = --currentGroupNo === 0 ? 1 : currentGroupNo;
                    }
                    $.ajax({
                        url: ctx + "/theme/deleteTheme/" + id,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.result == 'ok') {
                                toastr["success"]("删除成功！");
                                getData(currentGroupNo);
                                //删除用户组的同时,刷新用户列表信息
                            } else if (data.result == 'no') {
                                toastr["error"]("删除失败，该主题库下存在节点库！" +
                                    "");
                            }
                        },
                        error: function () {
                            toastr["error"]("");
                        }
                    });
                }
            });
        }

        //专业库名称的模糊搜索
        function searchSubject() {
            getSubject(1);
        }

        $("#btnAdd").click(function () {
            $("#themeNameAdd").val("");
            $("#descAdd").val("");
            resetData();
            $("#addModal").modal('show');
        });

        //重置校验窗体xiajl20181117
        function resetData() {
            $("#addThemeForm").validate().resetForm();
            $("#addThemeForm").validate().clean();
            $('.form-group').removeClass('has-error');
        }

        // 选中图片回显图片
        function showUploadFileAsDataURL(input) {
            var reader = new FileReader();
            reader.readAsDataURL(input.currentTarget.files[0]);
            reader.onload = function (event) {
                $(input.currentTarget).parent().parent().find("div:eq(0)").show();
                $(input.currentTarget).parent().parent().find("img").attr("src", event.target.result);
            }
        }

        //新增主题库保存
        function submitAddData() {
            if (!$("#addThemeForm").valid()) {
                return;
            }
            $.ajax({
                type: "POST",
                url: '${ctx}/theme/addTheme',
                data: $("#addThemeForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("添加成功！");
                        $("#addModal").modal("hide");
                        $("#themeNameAdd").val("");
                        $("#descAdd").val("");
                        getData(currentGroupNo);
                    } else if(data.result == 'exist') {
                        toastr["error"]("添加失败，主题库代码已存在！");
                    }
                }
            });
        }

        <!--编辑、更新主题库-->
        function editData(id) {
            $.ajax({
                type: "GET",
                url: '${ctx}/theme/toUpdateTheme',
                data: {"id": id},
                dataType: "json",
                success: function (data) {
                    $("#editModal").modal("show");
                    $("#themeNameEdit").val(data.theme.themeName);
                    $("#describeEdit").val(data.theme.describe);
                    $("#themeCodeEdit").val(data.theme.themeCode);
                    $("#themeId").val(data.theme.id);
                }
            });
        }

        //修改主题库，点击保存按钮
        function submitEditData() {
            if (!$("#editThemeForm").valid()) {
                return;
            }
            $.ajax({
                type: "POST",
                url: '${ctx}/theme/updateTheme',
                data: $("#editThemeForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("修改成功！");
                        $("#editModal").modal("hide");
                        getData(currentGroupNo);
                    } else if (data.result == 'exist'){
                        toastr["error"]("主题库名称重复！");
                    }
                }
            });
        }

        //查询专业库
        function getSubject(pageNum) {
            console.log("getSubject请求已经发送了");
            $.ajax({
                url: "${ctx}/subjectMgmt/querySubject",
                type: "get",
                data: {
                    "subjectNameFilter": $("#subjectNameFilter").val().trim(),
                    "pageNum": pageNum
                },
                dataType: "json",
                success: function (data) {
                    console.log("获得subject成功！");
                    console.log("success - data = " + data);

                    var totalSubject = data.total;
                    if (totalSubject == 0) {
                        $("#pagination").off();
                        $(".table-message").show();
                        $(".table-message").html("暂时没有数据");
                        $(".page-message").hide();
                        $(".page-list").hide();
                        $("#subjectList").hide();
                    } else {
                        $(".table-message").hide();
                        $(".page-message").show();
                        $(".page-list").show();
                        $("#subjectList").show();

                        var html = template("subjectListTable", data);
                        $("#subjectList").empty();
                        $("#subjectList").append(html);

                        $("#pageNum").html(data.pageNum);
                        currentPage = pageNum;
                        $("#totalPages").html(data.totalPages);
                        $("#total").html(data.total);


                        //分页
                        if ($("#pagination .bootpag").length != 0) {
                            $("#pagination").off();
                            $('#pagination').empty();
                        }
                        $('#pagination').bootpag({
                            total: data.totalPages,
                            page: data.pageNum,
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
                            getSubject(num);
                            currentPage = num;
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
                }
            });
        }

        //删除专业库
        function deleteSubject(deleteBtn) {
            var id = $(deleteBtn).parent().attr("name");

            console.log("idOfSubjectToBeDeleted = " + id);

            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>",
                function (result) {
                    if (result) {
                        bootbox.confirm("<span style='font-size: 16px'>${applicationScope.menus['organization_title']}相关信息很重要，请再次确认要删除吗?</span>", function (result) {
                                if (result) {
                                    var deleteUrl = "${ctx}/subjectMgmt/deleteSubject?id=" + id + "&pageNum=" + 1;
                                    var currentPageList = $("#subjectList>tr:last>td:eq(2)").text();
                                    if (currentPageList === "1") {
                                        currentPage = --currentPage === 0 ? 1 : currentPage;
                                    }
                                    $.ajax({
                                        url: deleteUrl,
                                        type: "get",
                                        dataType: "text",
                                        success: function (data) {
                                            if (data.trim() == "1") {
                                                toastr["success"]("删除成功！");
                                                getSubject(currentPage);
                                            } else {
                                                toastr["error"]("删除失败！");
                                            }
                                            console.log(data);
                                            console.log("typeof data = " + (typeof data));
                                        },
                                        error: function (data) {
                                            console.log(data);
                                            toastr["error"]("删除失败！");
                                        }
                                    });
                                }
                            }
                        );
                    }
                }
            );
        }

        //更新专业库
        function updateSubject(updateBtn) {

            $("#updateSubjectThemeName").html(" ");
            $("#updateThemeDiv").html("");

            $("#resetPassword").prop("checked", false);
            $("#adminPasswdM").prop("checked", false);
            clearAllInput();
            $.ajax({
                type: "GET",
                async: false,
                url: '${ctx}/subjectMgmt/findSubjectAndThemeById',
                data: {id: $(updateBtn).parent().attr("name")},
                dataType: "json",
                success: function (data) {

                    $("#idM").val(data.subject.id);
                    $("#subjectNameM").val(data.subject.subjectName);
                    $("#subjectCodeM").val(data.subject.subjectCode);
                    if (data.imagePath !== "") {
                        $(".showImgIfExist img").attr("src", data.subject.imagePath);
                        $(".showImgIfExist").show();
                    }
                    $("#imageM").attr("src", data.subject.imagePath);
                    $("#briefM").val(data.subject.brief);
                    $("#adminM").val(data.subject.admin);
                    // $("#adminPasswdM").val(data.adminPasswd);
                    $("#contactM").val(data.subject.contact);
                    $("#phoneM").val(data.subject.phone);
                    $("#emailM").val(data.subject.email);
                    $("#serialNoM").val(data.subject.serialNo);
                      var s="";
                    for(var i=0;i<data.list.length;i++){
                        if(data.subject.themeCode===data.list[i].themeCode){
                             s+="<input id='updateSubjectThemeName' class='form-control' name='"+ data.list[i].themeCode +"' value='"+data.list[i].themeName +"' readonly />";

                        }
                        $("#updateThemeDiv").append(s);
                    }
                    $("#updateSubjectThemeName").append(s);

                    $("#updateSubjectDialog").modal("show");
                },
                error: function (data) {
                    console.log(data);
                }
            });
        }

        //增加专业库
        function addSubject() {
            $("#subjectThemeName").html(" ");
            clearAllInput();
            $.ajax({
                url: "${ctx}/subjectMgmt/getNextSerialNoAndTheme",
                type: "get",
                data: {},
                dataType: "json",
                success: function (data) {
                    $("#serialNo").val(data.nextSerialNo);
                    var html=template("subjectThemeGroup",data);
                    $("#subjectThemeName").append(html);
                    $("#addSubjectDialog").modal("show");
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
                }
            });
        }

        //添加专业库，保存新增信息
        function agreeAddSubject() {
            if (!$("#addSubjectForm").valid()) {
                return;
            }

            var formData = new FormData();

            formData.append("subjectName", $("#subjectName").val());
            formData.append("subjectCode", $("#subjectCode").val());
            formData.append('image', $('#image').get(0).files[0]);
            formData.append('brief', $("#brief").val());
            formData.append("admin", $("#admin").val());
            formData.append("adminPasswd", $("#adminPasswd").val());
            formData.append("contact", $("#contact").val());
            formData.append("phone", $("#phone").val());
            formData.append("email", $("#email").val());
            formData.append("serialNo", $("#serialNo").val());
            formData.append("themeCode",$("#subjectThemeName").val());
            $.ajax({
                url: "${ctx}/subjectMgmt/addSubject",
                type: "post",
                contentType: false,
                processData: false,
                data: formData,
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    if(data==="1"){
                        toastr["success"]("添加成功！");
                        $("#addSubjectDialog").modal("hide");
                    }else if(data==="0"){
                        toastr["error"]("添加失败！数据节点代码重复");
                    }
                    getSubject(1); //没有搜索条件的情况下，显示第一页
                    // location.reload();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
                }
            });

        }

        //修改专业库，点击保存按钮
        function agreeUpdateSubject() {
            if (!$("#updateSubjectForm").valid()) {
                return;
            }
            var formData = new FormData();
            formData.append("id", $("#idM").val());
            formData.append("subjectName", $("#subjectNameM").val());
            formData.append("subjectCode", $("#subjectCodeM").val());
            formData.append('image', $('#imageM').get(0).files[0]);
            formData.append('brief', $("#briefM").val());
            formData.append("admin", $("#adminM").val());
            formData.append("adminPasswd", $("#adminPasswdM").val());
            formData.append("contact", $("#contactM").val());
            formData.append("phone", $("#phoneM").val());
            formData.append("email", $("#emailM").val());
            formData.append("serialNo", $("#serialNoM").val());
            var txt= $("input[id='updateSubjectThemeName']").attr("name");
            formData.append("themeCode",txt);
            console.log("agreeUpdateSubject - formData = " + formData);

            $.ajax({
                url: "${ctx}/subjectMgmt/updateSubject",
                type: "post",
                contentType: false,
                processData: false,
                data: formData,
                dataType: "json",
                success: function (data) {
                    if(data==="1"){
                        toastr["success"]("修改成功！");
                        $("#updateSubjectDialog").modal("hide");
                    }else if(data==="0"){
                        toastr["error"]("修改失败！");
                    }
                    console.log(data);
                    getSubject(1); //没有搜索条件的情况下，显示第一页
                    // location.reload();

                },
                error: function (data) {

                }
            });
        }
        // 清理弹窗中所有的输入框
        function clearAllInput() {
            $("input").val("");
            $(".showImgIfExist").hide();
            $("textarea").val("");
        }

    </script>
</div>

</html>
