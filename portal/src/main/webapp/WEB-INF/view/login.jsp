<%--
  User: jinbao
  CreateTate: 2019/3/23  11:06
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>系统登录</title>
    <link rel="stylesheet" href="${ctx}/resources/bundles/font-awesome-4.7.0/css/font-awesome.min.css">
    <link href="${ctx}/resources/bundles/bootstrapv3.3/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/css/home.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <style>
        input[type='text'], input[type='password'] {
            width: 80%;
            height: 100%;
            float: right;
        }
    </style>
    <script src="${ctx}/resources/bundles/jquery/jquery.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
</head>
<body>
<div class="loginbg"></div>
<div class="loginbox">
    <h3>${applicationScope.menus['login']}</h3>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <table>
            <tbody>
            <tr>
                <td>
                    <span>用户名：</span>
                    <input type="text" id="loginId" name="loginId" class="login_txtbx"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span>密&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
                    <input type="password" id="password" name="password" class="login_txtbx"/>
                </td>
            </tr>
            <dd>
                <font color="red">${errorMsg }</font>
            </dd>
            </tbody>
        </table>

        <div class="row">
            <div class="col-xs-6">
                <input type="checkbox" id="rememberPassword" name="rememberPassword"> <label
                    for="rememberPassword">记住密码</label>
            </div>
            <div class="col-xs-6 text-right">
                <%--<a href="#">注册</a>--%>
                <input type="submit" id="loginButton" value="登录" class="submit_btn"
                       style="width:40%;font-size:16px;background:#296ebf;color:#f8f8f8;height: 30px;border: none;border-radius: 6px;"/>
            </div>
        </div>
    </form>
</div>
</body>
<script>

    $(function () {
        var $loginId = $("#loginId");
        var $password = $("#password");
        var $rememberPassword = $("#rememberPassword");

        $rememberPassword.click(function () {
            var isChecked = $(this).is(":checked");
            var loginId = $loginId.val();
            var password = $password.val();
            if (isChecked) {
                if (loginId === "") {
                    toastr["warning"]("用户名不能为空", "警告！");
                    return false;
                }
                if (password === "") {
                    toastr["warning"]("密码不能为空", "警告！");
                    return false;
                }
                setCookie(loginId, password);
            } else {
                delCookie(loginId);
            }
        });

        $password.focus(function () {
            var cookie = getCookie($loginId.val());
            if (cookie) {
                $password.val(cookie);
                $rememberPassword.prop("checked", true);
            }
        })
    });

    //写cookies
    function setCookie(name, value) {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }

    //读取cookies
    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) return unescape(arr[2]);
        else return null;
    }

    //删除cookies
    function delCookie(name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = getCookie(name);
        if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }

</script>
</html>
