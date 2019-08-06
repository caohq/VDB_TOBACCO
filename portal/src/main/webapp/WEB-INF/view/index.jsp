<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2017/9/19
  Time: 15:28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<html>

<head>
    <style type="text/css">
        .task-title span {
            font-family: 'Arial Normal', 'Arial';
            font-weight: 400;
            font-style: normal;
            font-size: 28px;
            color: #000000;
        }

        table.list td, table.list th {
            padding: 14px !important;
            border-bottom: #edefee 1px solid;
            border-top: none !important;
        }
    </style>
    <link type="text/css" rel="stylesheet" href="${ctx}/resources/css/home.css">
</head>

<body>

<shiro:hasRole name="root">
    <h1 style="margin-top: 20%;margin-left: 35%;"><b>欢迎使用${applicationScope.menus['systemRole_root']}</b></h1>
</shiro:hasRole>
<shiro:hasRole name="admin">
    <h3><b>欢迎使用${applicationScope.menus['systemRole_admin']}</b></h3>
    <div class="form">
        <table class="table list">
            <tbody>
            <tr>
                <th style="width: 134px !important;">${applicationScope.menus['organization_title']}名称:</th>
                <td>${subject.subjectName}</td>
            </tr>
            <tr>
                <th style="width: 134px !important;">${applicationScope.menus['organization_title']}代码:</th>
                <td>${subject.subjectCode}</td>
            </tr>
            <tr>
                <th style="width: 134px !important;">管理员账号:</th>
                <td>${subject.admin}</td>
            </tr>
            <tr>
                <th style="width: 134px !important;">联系人:</th>
                <td>${subject.contact}</td>
            </tr>
            <tr>
                <th style="width: 134px !important;">电话:</th>
                <td>${subject.phone}</td>
            </tr>
            <tr>
                <th style="width: 134px !important;">邮箱:</th>
                <td>${subject.email}</td>
            </tr>
            <tr>
                <th style="width: 134px !important;">描述:</th>
                <td>${subject.brief}</td>
            </tr>
            <tr>
                <th  style="width: 134px !important;"></th>
                <td></td>
            </tr>
            </tbody>
        </table>
    </div>
</shiro:hasRole>

</body>

<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">

</div>

</html>
