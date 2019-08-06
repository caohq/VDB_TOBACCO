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
                <a href="#userContent" data-toggle="tab" id="showUserContent" style="white-space:nowrap;">
                    用户管理
                </a>
            </li>
            <li>
                <a href="#groupContent" data-toggle="tab">
                    用户组管理</a>
            </li>
        </ul>
        <!--tab content-->
        <div class="tab-content">

            <!--用户管理标签页-->
            <div class="tab-pane active" id="userContent" style="min-height: 400px">
                <div class="row">
                    <div class="col-xs-12 col-md-12 col-lg-12">

                        <!--用户管理标签页: 用户筛选条件-->
                        <div class="alert alert-info" role="alert">
                            <div class="row">
                                <div class="col-md-12 form-inline">
                                    <div style="float: left;width: 23%">
                                        <label class="control-label" style="color: black">用户账号:</label>
                                        <input type="text" id="loginIdFilter" style="width: 69%"
                                               name="loginIdFilter" placeholder="用户账号"
                                               class="form-control search-text"/>
                                    </div>
                                    <div style="float: left;width: 23%">
                                        <label class="control-label" style="color: black">用户名:</label>
                                        <input type="text" id="userNameFilter" style="width: 69%"
                                               name="userNameFilter" placeholder="用户名"
                                               class="form-control search-text"/>
                                    </div>
                                    <div style="float: left;width: 23%">
                                        <label class="control-label" style="color: black">用户组:</label>
                                        <select name='groupsFilter' id='groupsFilter' style="width: 69%"
                                                multiple="multiple" class="form-control select2me"
                                                style="width: 200px;">
                                            <c:forEach var="group" items="${allGroupList}">
                                                <option value="${group.groupName}" id="${group.id}"
                                                        style="width: 150px; height: 30px;">${group.groupName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div style="float: left;width: 10%;text-align: center;margin-left:-25px;margin-top: 3px;">
                                        <button id="searchUserBtn" name="searchUserBtn" onclick="searchUser();"
                                                class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查&nbsp;&nbsp;询
                                        </button>
                                    </div>
                                    <div style="float: left;width: 21%;margin-top: 3px;">
                                        <button id="addUserBtn" name="addUserBtn"
                                                class="btn info green btn-sm" onclick="addUser()"><i
                                                class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增用户
                                        </button>
                                    </div>
                                    <%--<label class="control-label">用户账号:</label>
                                    <input type="text" id="loginIdFilter" name="loginIdFilter" placeholder="用户账号" class="form-control search-text" />
                                    &nbsp;&nbsp;--%>
                                </div>
                            </div>
                        </div>

                        <!--用户管理标签页: 用户列表-->
                        <div class="table-message">列表加载中......</div>
                        <div class="table-scrollable">
                            <table class="table table-striped table-bordered table-advance table-hover">
                                <thead>
                                <tr id="table_List1">
                                    <th style="width: 5%;">编号</th>
                                    <th style="width: 15%;">用户名</th>
                                    <th style="width: 15%;">账号</th>
                                    <th style="width: 15%;">编辑时间</th>
                                    <th style="width: 25%;">用户组</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody id="userList">

                                </tbody>

                            </table>
                        </div>

                        <!--用户管理标签页: 分页-->
                        <div class="row margin-top-20">
                            <div class="page-message col-md-6 margin-top-10">
                                当前第&nbsp;<span style="color:blue;"
                                               id="curUserPageNum"></span>&nbsp;页,&nbsp;共&nbsp;<span
                                    style="color:blue;"
                                    id="totalUserPages"></span>页，
                                共<span style="color:blue;" id="totalUsers"></span>&nbsp;条数据
                            </div>
                            <div class="page-list col-md-6">
                                <div id="paginationForUser" style="float: right"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!--group tab-->
            <div class="tab-pane" id="groupContent">
                <div class="row">
                    <div class="col-xs-12 col-md-12 col-lg-12">
                        <div class="alert alert-info" role="alert">
                            <!--查询条件 -->
                            <div class="row">
                                <div class="col-md-12 form-inline">

                                    <label class="control-label" style="color: black;">用户组名:</label>
                                    <input type="text" id="groupName" name="groupName"
                                           class="form-control search-text ">
                                    &nbsp;&nbsp;&nbsp;&nbsp;

                                    <button id="btnSearch" name="btnSearch" onclick="search();"
                                            class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查询
                                    </button>
                                    &nbsp;&nbsp;
                                    <button id="btnAdd" name="btnAdd" onclick="" class="btn info green btn-sm"><i
                                            class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增用户组
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
                                        用户组名
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

<!--新增用户组Group-->
<div class="modal fade" tabindex="-1" role="dialog" id="addModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">新增用户组</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="addGroupForm" method="post" accept-charset="utf-8" role="form"
                      onfocusout="true">
                    <div class="form-group">
                        <label for="groupNameAdd" class="col-sm-3 control-label">用户组名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="groupNameAdd" name="groupName"
                                   placeholder="请输入用户组名称" required="required">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="descAdd" class="col-sm-3 control-label">描述<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <textarea type="text" class="form-control" cols="30" rows="5" id="descAdd" name="desc"
                                      placeholder="请输入用户组描述信息" required="required"></textarea>
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

<!--修改用户组Group-->
<div class="modal fade" tabindex="-1" role="dialog" id="editModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">修改用户组</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="editGroupForm" method="post" accept-charset="utf-8" role="form"
                      onfocusout="true">
                    <div class="form-group">

                        <input type="hidden" class="form-control"
                               id="groupId"
                               name="id" value=""/>

                        <input type="hidden" class="form-control"
                               id="groupUsers"
                               name="users"/>

                        <label for="groupNameEdit" class="col-sm-3 control-label">用户组名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="groupNameEdit" name="groupName"
                                   placeholder="请输入用户组名称" readonly required="required">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="descEdit" class="col-sm-3 control-label">描述<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <textarea type="text" class="form-control" cols="30" rows="5" id="descEdit" name="desc"
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

<!--用户组Group, 添加用户-->
<div class="modal fade" tabindex="-1" role="dialog" id="groupModalForAddUser">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">用户组添加用户</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="groupFormForAdduser" method="post" accept-charset="utf-8" role="form"
                      onfocusout="true">
                    <div class="form-group" style="margin-bottom:2px;">
                        <input type="hidden" id="spanGroupId">
                        <label class="col-sm-3 control-label">用户组名称:</label>
                        <div class="col-sm-8" style="padding-top: 7px;">
                            <span id="spanGroupName"></span>
                        </div>
                    </div>
                    <div class="form-group" style="margin-bottom:2px;">
                        <label class="col-sm-3 control-label">描述:</label>
                        <div class="col-sm-8" style="padding-top: 7px;">
                            <span id="spanDesc"></span>
                        </div>
                    </div>
                    <div class="form-group" style="margin-bottom:2px;">
                        <label class="col-sm-3 control-label">本组已有用户:</label>
                        <div class="col-sm-8" style="padding-top: 7px;">
                            <select class='form-control select2me' name='users' id='users' multiple>
                                <c:forEach var="item" items="${list}">
                                    <option value="${item.id}" id="${item.id}">${item.userName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn green" onclick="submitAddUser();"><i
                        class="glyphicon glyphicon-ok"></i>保存
                </button>
                <button type="button" data-dismiss="modal" class="btn  default">取消</button>
            </div>
        </div>
    </div>
</div>

<!-- 用户组 group -->
<script type="text/html" id="groupTmpl">
    {{each list}}
    <tr>
        <td style="display:table-cell; vertical-align:middle ; text-align: center;">
            {{$index+1}}
        </td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;"><a
                href="javascript:editData('{{$value.id}}');">{{$value.groupName}}</a>
        </td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{$value.desc}}</td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{dateFormat($value.createTime)}}
        </td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;" id="a{{$value.id}}">
            <%--<button class="btn default btn-xs green" onclick="addUserData('{{$value.id}}')"><i class="fa fa-user"></i>添加用户</button>--%>
            <%--<button class="btn default btn-xs purple updateButton" onclick="editData('{{$value.id}}')"><i class="fa fa-edit"></i>修改</button>&nbsp;--%>
            <%--<button class="btn default btn-xs red" onclick="deleteData('{{$value.id}}')"><i class="fa fa-trash"></i>删除</button>&nbsp;--%>
            <table class="0" cellspacing="0" border="0" align="center">
                <tr>
                    <td class="shenhe"><a href="#" onclick="addUserData('{{$value.id}}')"><i class="fa fa-user"
                                                                                             aria-hidden="true"></i>添加用户</a>
                    </td>
                    <td width="1"></td>
                    <td class="bianji"><a href="#" onclick="editData('{{$value.id}}')"><i class="fa fa-pencil-square-o"
                                                                                          aria-hidden="true"></i>修改</a>
                    </td>
                    <td width="1"></td>
                    <td class="shanchu"><a href="#" onclick="deleteData('{{$value.id}}')"><i class="fa fa-trash-o fa-fw"
                                                                                             aria-hidden="true"></i>删除</a>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
    {{/each}}
</script>

<!--用户管理标签页：用户列表表格-->
<script type="text/html" id="userListTable">
    {{each list}}
    <tr>
        <td style="text-align: center">{{1 + $index}}</td>
        <td style="text-align: center">{{$value.userName}}</td>
        <td style="text-align: center">{{$value.loginId}}</td>
        <td style="text-align: center">{{$value.createTime}}</td>
        <%--<td style="text-align: center">{{$value.stat}}</td>--%>
        <td style="text-align: center">{{$value.groups}}</td>
        <td style="text-align: center" id="{{$value.id}}">
            <%--<button class="btn default btn-xs purple updateUserButton" data-target="#updateUserDialog" data-toggle="modal" onclick="updateUser(this);"><i class="fa fa-edit"></i>&nbsp;修改</button>--%>
            <%--&nbsp;--%>
            <%--<button class="btn default btn-xs red updateUserGroupButton" onclick="deleteUser(this);"><i class="fa fa-trash"></i>&nbsp;删除</button>--%>
            <table class="0" cellspacing="0" border="0" align="center">
                <tr>
                    <td class="bianji"><a href="#" data-target="#updateUserDialog" data-toggle="modal"
                                          onclick="updateUser(this);"><i class="fa fa-pencil-square-o"
                                                                         aria-hidden="true"></i>修改</a></td>
                    <td width="1"></td>
                    <td class="shanchu"><a href="#" onclick="deleteUser(this);"><i class="fa fa-trash-o fa-fw"
                                                                                   aria-hidden="true"></i>删除</a></td>
                </tr>
            </table>

        </td>
    </tr>
    {{/each}}
</script>

<!--用户管理标签页：新建用户对话框-->
<div id="addUserDialog" class="modal fade" role="dialog" tabindex="-1" role="dialog" aria-hidden="true"
     data-width="400">
    <div class="modal-dialog" role="document">
        <div class="modal-content">

            <div class="modal-header  bg-primary">
                <button class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
                <h4 id="titleForAddUserDialog" class="modal-title">新增用户</h4>
            </div>

            <div class="modal-body">
                <form id="addUserForm" class="form-horizontal" role="form" method="post" accept-charset="utf-8"
                      onfocusout="true">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="userName">
                                用&nbsp;户&nbsp;名<span style="color: red;">*</span>
                            </label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" placeholder="请输入用户名称" id="userName"
                                       name="userName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="loginId">
                                用户账号<span style="color: red;">*</span>
                            </label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" placeholder="请输入用户账号,只可输入数字和字母的组合" id="loginId"
                                       name="loginId" required="required"
                                       onkeyup="this.value=this.value.replace(/[^\w_]/g,'');"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="password">
                                密&nbsp;&nbsp;&nbsp;&nbsp;码<span style="color: red;">*</span>
                            </label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" placeholder="请输入密码,至少为6位" id="password"
                                       name="password" required="required">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="userRealName">真实姓名:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="userRealName" class="form-control" type="text" name="userRealName"
                                       placeholder="请输入真实姓名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="idCard">证件号:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="idCard" class="form-control" type="text" name="idCard" placeholder="请输入证件号">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="sex" class=" col-sm-3 control-label">性别:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-8">
                                <select id="sex" type="text" name="sex">
                                    <option value="">请选择性别</option>
                                    <option value="男">男</option>
                                    <option value="女">女</option>
                                </select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="phoneNo">联系电话:<span style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="phoneNo" class="form-control" type="text" name="phoneNo"
                                       placeholder="请输入联系电话">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="email">电子邮件:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="email" class="form-control" type="text" name="email" placeholder="请输入电子邮件">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="workName">单位名称:</label>
                            <div class="col-sm-9">
                                <input id="workName" class="form-control" type="text" name="workName"
                                       placeholder="请输入单位名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="workAddress">单位地址:</label>
                            <div class="col-sm-9">
                                <input id="workAddress" class="form-control" type="text" name="workAddress"
                                       placeholder="请输入单位地址">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="laboratoryDirec">研究方向:</label>
                            <div class="col-sm-9">
                                <input id="laboratoryDirec" class="form-control" type="text" name="laboratoryDirec"
                                       placeholder="请输入研究方向">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="laboratory">研究室:</label>
                            <div class="col-sm-9">
                                <input id="laboratory" class="form-control" type="text" name="laboratory"
                                       placeholder="请输入研究室">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-md-3 control-label" for="subjectCodeForAddUserDialog">
                                专业库
                            </label>
                            <div class="col-md-9">
                                <select class='form-control' name='subjectCodeForAddUserDialog'
                                        id='subjectCodeForAddUserDialog' placeholder="请选择专业库">
                                    <option value="" disabled selected>请选择专业库</option>
                                    <c:forEach var="subject" items="${subjectList}">
                                        <option value="${subject.subjectCode}"
                                                id="${subject.subjectCode}">${subject.subjectName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label" for="groupsForAddUserDialog">
                                用户组
                            </label>
                            <div class="col-md-9">
                                <select class='form-control select2me' name='groupsForAddUserDialog'
                                        id='groupsForAddUserDialog' multiple="multiple" placeholder="请选择用户组">
                                    <c:forEach var="group" items="${allGroupList}">
                                        <option value="${group.groupName}" id="${group.id}">${group.groupName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button id="saveUserAddBtn" class="btn btn-success" onclick="agreeAddUser();">
                    保存
                </button>
                <button id="cancelUserAddBtn" class="btn default" data-dismiss="modal">
                    取消
                </button>
            </div>
        </div>
    </div>
</div>

<!--用户管理标签页：修改用户对话框-->
<div id="updateUserDialog" class="modal fade" role="dialog" tabindex="-1" role="dialog" aria-hidden="true"
     data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <h4 id="titleForUpdateUserDialog" class="modal-title">修改用户</h4>
            </div>

            <div class="modal-body">
                <form id="updateUserForm" class="form-horizontal" role="form" method="post" accept-charset="utf-8"
                      onfocusout="true">
                    <div class="form-body">
                        <span id="idForUpdate" hidden="hidden"></span>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="userNameForUpdate">
                                用&nbsp;户&nbsp;名<span style="color: red;">*</span>
                            </label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" placeholder="请输入用户名称" id="userNameForUpdate"
                                       name="userNameForUpdate" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="loginIdForUpdate">
                                用户账号
                            </label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" placeholder="请输入用户账号" id="loginIdForUpdate"
                                       name="loginIdForUpdate" readonly="readonly"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="resetPassword">
                                <%--密&nbsp;&nbsp;&nbsp;&nbsp;码<span style="color: red;">*</span>--%>
                                <input type="checkbox" id="resetPassword">
                                重置密码<span style="color: red;">*</span>
                            </label>
                            <div class="col-md-9">
                                <input disabled="disabled" type="text" class="form-control" placeholder="请输入密码"
                                       id="passwordForUpdate" name="passwordForUpdate" required="required">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="userRealNameForUpdate">真实姓名:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="userRealNameForUpdate" class="form-control" type="text"
                                       name="userRealNameForUpdate"
                                       placeholder="请输入真实姓名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="idCardForUpdate">证件号:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="idCardForUpdate" class="form-control" type="text" name="idCardForUpdate"
                                       placeholder="请输入真实姓名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="sexForUpdate" class=" col-sm-3 control-label">性别:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-8">
                                <select id="sexForUpdate" type="text" name="sexForUpdate">
                                    <option value="">请选择性别</option>
                                    <option value="男">男</option>
                                    <option value="女">女</option>
                                </select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="phoneNoForUpdate">联系电话:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="phoneNoForUpdate" class="form-control" type="text" name="phoneNoForUpdate"
                                       placeholder="请输入联系电话">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="emailForUpdate">电子邮件:<span
                                    style="color: red;">*</span></label>
                            <div class="col-sm-9">
                                <input id="emailForUpdate" class="form-control" type="text" name="emailForUpdate"
                                       placeholder="请输入电子邮件">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="workNameForUpdate">单位名称:</label>
                            <div class="col-sm-9">
                                <input id="workNameForUpdate" class="form-control" type="text" name="workNameForUpdate"
                                       placeholder="请输入单位名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="workAddressForUpdate">单位地址:</label>
                            <div class="col-sm-9">
                                <input id="workAddressForUpdate" class="form-control" type="text"
                                       name="workAddressForUpdate"
                                       placeholder="请输入单位地址">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="laboratoryDirecForUpdate">研究方向:</label>
                            <div class="col-sm-9">
                                <input id="laboratoryDirecForUpdate" class="form-control" type="text"
                                       name="laboratoryDirecForUpdate"
                                       placeholder="请输入研究方向">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class=" col-sm-3 control-label" for="laboratoryForUpdate">研究室:</label>
                            <div class="col-sm-9">
                                <input id="laboratoryForUpdate" class="form-control" type="text"
                                       name="laboratoryForUpdate"
                                       placeholder="请输入研究室">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-md-3 control-label" for="subjectCodeForUpdateUserDialog">
                                专业库
                            </label>
                            <div class="col-md-9">
                                <select class='form-control' name='subjectCodeForUpdateUserDialog'
                                        id='subjectCodeForUpdateUserDialog'>
                                    <c:forEach var="subject" items="${subjectList}">
                                        <option value="${subject.subjectCode}"
                                                id="${subject.subjectCode}">${subject.subjectName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="groupsForUpdateUserDialog">
                                用户组
                            </label>
                            <div class="col-md-9">
                                <select class='form-control select2me' name='groupsForUpdateUserDialog'
                                        id='groupsForUpdateUserDialog' multiple="multiple">
                                    <c:forEach var="group" items="${allGroupList}">
                                        <option value="${group.groupName}" id="${group.id}">${group.groupName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button id="saveUserUpdateBtn" class='btn btn-success' onclick="agreeUpdateUser();">
                    保存
                </button>
                <button id="cancelUserUpdateBtn" class="btn default" data-dismiss="modal">
                    取消
                </button>
            </div>
        </div>
    </div>
</div>

<!--用户管理标签页：删除用户对话框-->
<div id="deleteUserDialog" class="modal fade" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">
                    删除用户
                </h5>
            </div>
            <div class="modal-body">
                <h5>确认删除该用户？</h5>
            </div>

            <div class="modal-footer">
                <span id="idOfUserToBeDeleted" hidden="hidden"></span>
                <button id="agreeDeleteUserBtn" class="btn green" onclick="agreeDeleteUser();">确认</button>
                <button id="cancelDeleteUserBtn" class="btn default" data-dismiss="modal">取消</button>
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


    <script type="text/javascript">
        var ctx = '${ctx}';
        var currentGroupNo = 1;
        var validatorAdd;
        var groupUsersSelect2;
        var validAddData;
        var validEditData;

        $(function () {
            $("#resetPassword").on('click', function () {
                var currentTarget = event.currentTarget;
                var isChecked = $(currentTarget).is(":checked");
                if (isChecked) {
                    $("#passwordForUpdate").removeAttrs("disabled");
                } else {
                    $("#passwordForUpdate").attr("disabled", "disabled");
                }
            })
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
                    groupName: {
                        required: true,
                        remote:
                            {
                                url: "isExist",
                                type: "get",
                                dataType: "json",
                                data:
                                    {
                                        'groupName': function () {
                                            return $("#groupNameAdd").val();
                                        }
                                    }
                            }
                    },
                    desc: {
                        required: true
                    }
                },
                messages: {
                    groupName: {
                        required: "请输入用户组名称",
                        remote: "此用户组名称己存在"
                    },
                    desc: {
                        required: "请输入用户组描述信息"
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
                    groupName: {
                        required: true
                    },
                    desc: {
                        required: true
                    }
                },
                messages: {
                    groupName: {
                        required: "请输入用户组名称"
                    },
                    desc: {
                        required: "请输入用户组描述信息"
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

            $("#addGroupForm").validate(validAddData);
            $("#editGroupForm").validate(validEditData);

            //getAllUserList();
            groupUsersSelect2 = $('#users').select2({
                placeholder: "请选择用户",
                allowClear: true
            });


            //////////////////////////////////////////////////////////////////////////////////////////////////
            //
            // 用户管理标签页：jquery初始化代码
            //
            //////////////////////////////////////////////////////////////////////////////////////////////////
            $("#showUserContent").click(function () {
                location.reload();
            });


            queryUser(null, null, null, 1); //用户管理标签页：获得用户列表
            //选择用户组筛选用户
            var groupsFilterSelect2 = $("#groupsFilter").select2(
                {
                    placeholder: "请选择用户组",
                    allowClear: true
                }
            );
            //添加用户对话框中，选择用户组
            var userGroupForAddUserDialogSelect2 = $("#groupsForAddUserDialog").select2({
                placeholder: "请选择用户组",
                allowClear: true
            });
            //添加、更新用户对话框中，选择用户组
            var groupsForUpdateUserDialogSelect2 = $('#groupsForUpdateUserDialog').select2({
                placeholder: "请选择用户组",
                allowClear: true
            });

            var addUserValid = {
                errorElement: 'span',
                errorPlacement: function (error, element) {
                    if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
                        var eid = element.attr('name'); //获取元素的name属性
                        error.appendTo(element.parent()); //将错误信息添加当前元素的父结点后面
                    } else {
                        error.insertAfter(element);
                    }
                },
                errorClass: 'error-message',
                focusInvalid: false,
                rules: {
                    userName: "required",
                    loginId: {
                        required: true,
                        remote:
                            {
                                url: "${ctx}/user/queryLoginId",
                                type: "get",
                                data:
                                    {
                                        'loginId': function () {
                                            return $("#loginId").val();
                                        }
                                    },
                                dataType: "json"
                            }
                    },
                    password: {
                        required: true,
                        minlength: 6
                    },
                    userRealName: {
                        required: true
                    },
                    idCard: {
                        required: true,
                        isIdCard: true
                    },
                    sex: {
                        required: true
                    },
                    phoneNo: {
                        required: true,
                        isPhoneNum: true
                    },
                    email: {
                        required: true,
                        email: true
                    }
                },
                messages: {
                    userName: "请输入用户名",
                    loginId: {
                        required: "请输入用户账号",
                        remote: "此用户账号已经存在！"
                    },
                    password: {
                        required: "请输入密码",
                        minlength: "密码至少为6位"
                    },
                    userRealName: {
                        required: "请输入真实姓名"
                    },
                    idCard: {
                        required: "请输入证件号"
                    },
                    sex: {
                        required: "请选择性别"
                    }
                }
            };
            var updateUserValid = {
                errorElement: 'span',
                errorClass: 'error-message',
                focusInvalid: false,
                rules: {
                    userNameForUpdate: "required",

                    loginIdForUpdate: {
                        required: true
                    },
                    passwordForUpdate: {
                        required: true,
                        minlength: 6
                    },
                    userRealNameForUpdate: {
                        required: true
                    },
                    idCardForUpdate: {
                        required: true,
                        isIdCard: true
                    },
                    sexForUpdate: {
                        required: true
                    },
                    phoneNoForUpdate: {
                        required: true,
                        isPhoneNum: true
                    },
                    emailForUpdate: {
                        required: true,
                        email: true
                    }
                    /*groupsForUpdateUserDialog: "required",*/
                },
                messages: {
                    userNameForUpdate: "请输入用户名",
                    loginIdForUpdate: {
                        required: "请输入用户账号",
                        /*remote: "此用户账号已经存在！"*/
                    },
                    passwordForUpdate: {
                        required: "请输入密码",
                        minlength: "密码至少为6位"
                    },
                    /*groupsForUpdateUserDialog: "请输入用户组"*/
                    userRealNameForUpdate: {
                        required: "请输入真实姓名"
                    },
                    idCardForUpdate: {
                        required: "请输入证件号"
                    },
                    sexForUpdate: {
                        required: "请选择性别"
                    }
                }
            };
            $("#addUserForm").validate(addUserValid);
            $("#updateUserForm").validate(updateUserValid);
        });
        //自定义手机号验证
        jQuery.validator.addMethod("isPhoneNum", function (value, element) {
            var length = value.length;
            var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
            return this.optional(element) || (length == 11 && mobile.test(value));
        }, "请正确填写您的手机号码");
        //自定义身份证号验证
        jQuery.validator.addMethod("isIdCard", function (value, element) {
            var length = value.length;
            var idCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
            return this.optional(element) || (idCard.test(value));
        }, "请输入正确的身份证号");

        function search() {
            getData(1);
        }

        //获取所有的用户信息
        function getAllUserList() {
            $.ajax({
                url: "${ctx}/group/getUserList",
                type: "get",
                dataType: "json",
                success: function (data) {

                }
            });
        }

        function getData(pageNo) {
            $.ajax({
                url: "${ctx}/group/getPageData",
                type: "get",
                dataType: "json",
                data: {
                    "groupName": $.trim($("#groupName").val()),
                    "pageNo": pageNo,
                    "pageSize": 10
                },
                success: function (data) {
                    console.log("-----------------")
                    console.log(data)

                    var html = template("groupTmpl", data);
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

        function deleteData(id) {

            bootbox.confirm("<span style='font-size:16px;'>确定要删除此条记录吗？</span>", function (r) {
                if (r) {
                    var currentPageListSize = $.trim($("div.active table:eq(0)>tbody>tr:last>td:eq(0)").text());
                    if (currentPageListSize === "1") {
                        currentGroupNo = --currentGroupNo === 0 ? 1 : currentGroupNo;
                    }
                    $.ajax({
                        url: ctx + "/group/delete/" + id,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.result == 'ok') {
                                toastr["success"]("删除成功！", "数据删除");
                                getData(currentGroupNo);
                                //删除用户组的同时,刷新用户列表信息
                                searchUser();
                            } else {
                                toastr["error"]("删除失败！", "数据删除");
                            }
                        },
                        error: function () {
                            toastr["error"]("删除失败！", "数据删除");
                        }
                    });
                }
            });
        }

        $("#btnAdd").click(function () {
            $("#groupNameAdd").val("");
            $("#descAdd").val("");
            resetData();
            $("#addModal").modal('show');
        });

        //重置校验窗体xiajl20181117
        function resetData() {
            $("#addGroupForm").validate().resetForm();
            $("#addGroupForm").validate().clean();
            $('.form-group').removeClass('has-error');
        }

        function submitAddData() {
            if (!$("#addGroupForm").valid()) {
                return;
            }
            $.ajax({
                type: "POST",
                url: '${ctx}/group/add',
                data: $("#addGroupForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("添加成功！", "添加用户组");
                        $("#addModal").modal("hide");
                        $("#groupNameAdd").val("");
                        $("#descAdd").val("");
                        getData(currentGroupNo);
                    } else {
                        toastr["error"]("添加失败！", "添加用户组");
                    }
                }
            });
        }

        <!--编辑用户组-->
        function editData(id) {
            $.ajax({
                type: "GET",
                url: '${ctx}/group/info',
                data: {"id": id},
                dataType: "json",
                success: function (data) {
                    $("#editModal").modal("show");
                    $("#groupNameEdit").val(data.group.groupName);
                    $("#descEdit").val(data.group.desc);
                    $("#groupId").val(data.group.id);
                }
            });
        }


        <!--用户组中增加用户界面-->
        function addUserData(id) {
            $.ajax({
                type: "GET",
                url: '${ctx}/group/info',
                data: {"id": id},
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    $("#groupModalForAddUser").modal("show");
                    $("#spanGroupName").html(data.group.groupName);
                    $("#spanDesc").html(data.group.desc);
                    $("#spanGroupId").val(data.group.id);
                    //编辑显示己增加的用户
                    //编辑显示己增加的用户
                    console.log(data.group.users);
                    console.log(JSON.stringify(data.group.users));
                    $("#users").select2().val(data.group.users).trigger("change");

                }
            });
        }

        function submitEditData() {
            if (!$("#editGroupForm").valid()) {
                return;
            }
            $.ajax({
                type: "POST",
                url: '${ctx}/group/update',
                data: $("#editGroupForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("编辑成功！", "用户组编辑");
                        $("#editModal").modal("hide");
                        getData(currentGroupNo);
                    } else {
                        toastr["error"]("编辑失败！", "用户组编辑");
                    }
                }
            });
        }

        <!--用户组中增加用户信息确认 -->
        function submitAddUser() {
            console.log("id=" + $("#spanGroupId").val());
            $.ajax({
                type: "POST",
                url: '${ctx}/group/updateUsers',
                traditional: true,
                data: {
                    "id": $("#spanGroupId").val(),
                    // "users":JSON.stringify($("#users").val())
                    "users": $("#users").val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("用户组增加用户成功！", "用户组编辑");
                        $("#groupModalForAddUser").modal("hide");
                        //同时更新用户信息
                        queryUser(null, null, null, 1);
                    } else {
                        toastr["error"]("用户组增加用户失败！", "用户组编辑");
                    }
                }
            });
        }
    </script>


    <%--//////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // 用户管理标签页：javascript代码
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////--%>
    <script type="text/javascript">
        var currentUserPage = 1;

        //添加用户按钮
        function addUser() {
            $("#userName").val("");
            $("#loginId").val("");
            $("#password").val("");
            $("#subjectCodeForAddUserDialog").val("");
            $("#groupsForAddUserDialog").select2("val", "");

            resetAddUserDialog();

            $("#addUserDialog").modal("show");
        }

        function resetAddUserDialog() {
            $("#addUserForm").validate().resetForm();
            $("#addUserForm").validate().clean();
            $(".form-group").removeClass("has-error");
        }

        //添加用户对话框的保存
        function agreeAddUser() {
            if (!$("#addUserForm").valid()) {
                return;
            }

            console.log("adduser\n" + (typeof $("#groupsForAddUserDialog").val()) + "\nadduser");

            $.ajax({
                url: "${ctx}/user/addUser",
                type: "get",
                data: {
                    "userName": $("#userName").val(),
                    "loginId": $("#loginId").val(),
                    "password": $("#password").val(),

                    "userRealName": $("#userRealName").val(),
                    "idCard": $("#idCard").val(),
                    "sex": $("#sex").val(),
                    "phoneNo": $("#phoneNo").val(),
                    "email": $("#email").val(),
                    "workName": $("#workName").val(),
                    "workAddress": $("#workAddress").val(),
                    "laboratoryDirec": $("#laboratoryDirec").val(),
                    "laboratory": $("#laboratory").val(),

                    "subjectCode": $("#subjectCodeForAddUserDialog").val(),
                    "groups": ($("#groupsForAddUserDialog").val() == null) ? "" : ($("#groupsForAddUserDialog").val().toString())
                },
                dataType: "text",
                success: function (data) {
                    console.log(data);
                    $("#addUserDialog").modal("hide");
                    setTimeout(function () {
                    }, 100);
                    queryUser(null, null, null, 1); //没有搜索条件的情况下，显示第一页
                    location.reload();
                },
                error: function (data) {

                }
            });
        }

        //查询 按钮
        function searchUser() {
            var loginId = $("#loginIdFilter").val().trim();
            var userName = $("#userNameFilter").val().trim();
            var groups = $("#groupsFilter").val();
            if (groups == null) {
                groups = "";
            } else {
                groups = groups.toString();
            }

            console.log("searchUser - parameters - loginId = " + loginId);
            console.log("searchUser - parameters - userName = " + userName);
            console.log("searchUser - parameters - groups = " + groups);

            queryUser(loginId, userName, groups, 1);
        }

        function queryUser(loginId, userName, groups, curUserPageNum) {
            $.ajax({
                url: "${ctx}/user/queryUser",
                type: "get",
                data: {
                    "loginId": loginId,
                    "userName": userName,
                    "groups": groups,
                    "curUserPageNum": curUserPageNum,
                    "pageSize": 10
                },
                dataType: "json",
                success: function (data) {
                    console.log(data);

                    var userSize = data.totalUsers;
                    if (userSize == 0) {
                        $("#paginationForUser").off();
                        $(".table-message").show();
                        $(".table-message").html("暂时没有数据");
                        $(".page-message").hide();
                        $(".page-list").hide();
                        $("#userList").hide();
                    } else {
                        $(".table-message").hide();
                        $(".page-message").show();
                        $(".page-list").show();
                        $("#userList").show();

                        var html = template("userListTable", data);
                        $("#userList").empty();
                        $("#userList").append(html);

                        $(".table-message").html("列表加载中......")
                        $("#curUserPageNum").html(data.curUserPageNum);
                        currentUserPage = data.curUserPageNum;
                        $("#totalUserPages").html(data.totalUserPages);
                        $("#totalUsers").html(data.totalUsers);


                        if ($("#paginationForUser .bootpag").length != 0) {
                            $("#paginationForUser").off();
                            $('#paginationForUser').empty();
                        }

                        $('#paginationForUser').bootpag({
                            total: data.totalUserPages,
                            page: data.curUserPageNum,
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
                        }).on('page', function (event, toNum) {
                            queryUser(loginId, userName, groups, toNum);
                            currentUserPage = toNum;
                        });
                    }
                }
            });
        }

        //删除按钮
        function deleteUser(deleteBtn) {
            // var idOfUser = $(deleteBtn).parent().attr("id");
            var idOfUser = $(deleteBtn).parent().parent().parent().parent().parent().attr("id");

            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>",
                function (result) {
                    if (result) {
                        var deleteUrl = "${ctx}/user/deleteUser";
                        var currentPageListSize = $.trim($("div.active table:eq(0)>tbody>tr:last>td:eq(0)").text());
                        if (currentPageListSize === "1") {
                            currentUserPage = --currentUserPage === 0 ? 1 : currentUserPage;
                        }
                        $.ajax({
                            url: deleteUrl,
                            type: "get",
                            data: {
                                id: idOfUser
                            },
                            dataType: "text",
                            success: function (data) {
                                console.log(data);
                                console.log("typeof data = " + (typeof data));
                                if (data.trim() == "1") {
                                    queryUser(null, null, null, currentUserPage);
                                    toastr["success"]("删除成功！", "数据删除");
                                    //location.reload();
                                } else {
                                    toastr["error"]("删除失败！", "数据删除");
                                }
                            },
                            error: function (data) {
                                console.log(data);
                                toastr["error"]("删除失败！", "数据删除");
                            }
                        });
                    }
                }
            );
        }

        function agreeDeleteUser(agreeDeleteBtn) {
            $("#deleteUserDialog").modal("hide");
            var idOfUser = $("#idOfUserToBeDeleted").html();
            $.ajax({
                url: "${ctx}/user/deleteUser",
                type: "get",
                data:
                    {"id": idOfUser},
                dataType: "json",
                success: function (data) {
                    queryUser(null, null, null, 1);
                },
                error: function (data) {

                }
            });
        }

        function updateUser(updateBtn) {
            $("#resetPassword").prop("checked", false);
            $("#passwordForUpdate").prop("checked", false);
            var idOfUser = $(updateBtn).parent().parent().parent().parent().parent().attr("id");

            $.ajax(
                {
                    url: "${ctx}/user/getUserById",
                    type: "get",
                    data: {
                        "id": idOfUser
                    },
                    dataType: "json",
                    success: function (data) {
                        $("#idForUpdate").html(data.id);
                        $("#userNameForUpdate").val(data.userName);
                        $("#loginIdForUpdate").val(data.loginId);


                        $("#userRealNameForUpdate").val(data.userRealName);
                        $("#idCardForUpdate").val(data.idCard);
                        $("#sexForUpdate").val(data.sex);
                        $("#phoneNoForUpdate").val(data.phoneNo);
                        $("#emailForUpdate").val(data.email);
                        $("#workNameForUpdate").val(data.workName);
                        $("#workAddressForUpdate").val(data.workAddress);
                        $("#laboratoryDirecForUpdate").val(data.laboratoryDirec);
                        $("#laboratoryForUpdate").val(data.laboratory);


                        var subjectCodeArr = data.subjectCode.split(",");
                        $("#subjectCodeForUpdateUserDialog").val(subjectCodeArr);
                        var groupArr = data.groups.split(",");
                        $("#groupsForUpdateUserDialog").select2().val(groupArr).trigger("change");
                        $("#updateUserDialog").modal("show");

                    },
                    error: function (data) {

                    }
                }
            );
        }

        function agreeUpdateUser() {
            if (!$("#updateUserForm").valid()) {
                return;
            }

            $.ajax(
                {
                    url: "${ctx}/user/updateUser",
                    type: "get",
                    data: {
                        "id": $("#idForUpdate").html(),
                        "userName": $("#userNameForUpdate").val(),
                        "loginId": $("#loginIdForUpdate").val(),
                        "password": $("#passwordForUpdate").val(),


                        "userRealName": $("#userRealNameForUpdate").val(),
                        "idCard": $("#idCardForUpdate").val(),
                        "sex": $("#sexForUpdate").val(),
                        "phoneNo": $("#phoneNoForUpdate").val(),
                        "email": $("#emailForUpdate").val(),
                        "workName": $("#workNameForUpdate").val(),
                        "workAddress": $("#workAddressForUpdate").val(),
                        "laboratoryDirec": $("#laboratoryDirecForUpdate").val(),
                        "laboratory": $("#laboratoryForUpdate").val(),


                        "subjectCode": $("#subjectCodeForUpdateUserDialog").val(),
                        "groups": ($("#groupsForUpdateUserDialog").val() == null) ? "" : ($("#groupsForUpdateUserDialog").val().toString())
                    },
                    dataType: "text",
                    success: function (data) {
                        console.log(data);
                        $("#updateUserDialog").modal("hide");
                        queryUser(null, null, null, 1); //没有搜索条件的情况下，显示第一页
                        location.reload();
                    },
                    error: function (data) {
                        console.log(data);
                    }
                }
            );
        }

        /*//subjectCode唯一性
        $("#loginId").change(
            function()
            {
                var loginId = $(this).val();

            }
        );*/

        <%--$("#loginId").blur(--%>
        <%--function()--%>
        <%--{--%>
        <%--$.ajax({--%>
        <%--type: "GET",--%>
        <%--async: false,--%>
        <%--url: '${ctx}/user/queryLoginId',--%>
        <%--data: {loginId: $(this).val()},--%>
        <%--dataType: "text",--%>
        <%--success: function (data){--%>
        <%--var cntOfLoginId = parseInt(data);--%>
        <%--if (cntOfLoginId > 0)--%>
        <%--{--%>
        <%--alert("loginId已经存在，请另外选择一个！")--%>
        <%--}--%>
        <%--},--%>
        <%--error: function(data) {--%>
        <%--console.log(data);--%>
        <%--}--%>
        <%--});--%>
        <%--}--%>

    </script>
</div>

</html>
