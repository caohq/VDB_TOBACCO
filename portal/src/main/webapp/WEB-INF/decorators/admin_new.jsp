<%--
  Created by IntelliJ IDEA.
  User: jinbao
  Date: 2019/3/18
  Time: 15:02
  To change this template use File | Settings | File Templates.

  replace admin.jsp
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>
        <shiro:hasRole name="admin">
            ${applicationScope.menus['systemRole_admin']}
        </shiro:hasRole>
        <shiro:hasRole name="root">
            ${applicationScope.menus['systemRole_root']}
        </shiro:hasRole>
    </title>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport"/>

    <link rel="stylesheet" href="${ctx}/resources/bundles/font-awesome-4.7.0/css/font-awesome.min.css">
    <link href="${ctx}/resources/bundles/bootstrapv3.3/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/metronic/global/css/components.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/metronic/global/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/metronic/css/layout.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/metronic/css/themes/light.css" rel="stylesheet" type="text/css"
          id="style_color"/>
    <link href="${ctx}/resources/bundles/metronic/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/css/customUserLogin.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-fileinput/css/bootstrap.min.css"/>

    <%--样式bule: 2019年3月18日引入--%>
    <link href="${ctx}/resources/css/home.css" rel="stylesheet" type="text/css"/>

    <!--BEGIN PAGE STYLES-->
    <sitemesh:write property="head"></sitemesh:write>
    <!--END PAGE STYLES-->

    <!-- BEGIN THEME STYLES -->
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/style.css">
    <!-- END THEME STYLES -->

    <link rel="shortcut icon" href="${ctx}/resources/img/favicon.ico"/>

    <style>
        .page-content-wrapper .page-content {
            padding: unset !important;
            background-color: #f1f1f1;
        }

        .right_div {
            /*padding-top:8px;*/
            padding-bottom: unset;
        }

        #content {
            overflow-y: auto !important;
            overflow-x: hidden !important;
        }

        .page-sidebar .page-sidebar-menu > li > a {
            padding: 8px 15px;
        }

        .navbar-nav > li > a {
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .btn {
            border-radius: 6px !important;
        }

        table td, table th {
            border-right: none !important;
            border-left: none !important;
        }

        .page-content table tbody tr:hover {
            background: #f5f5f5;
        }

        table.items tr:hover {
            background: none !important;
        }

        .alert.alert-info {
            background-color: #cbe2fc !important;
        }


        #upload-list th, #dataList th, #table_List1 th, #table_List2 th {
            background-color: #cbe2fc;
            text-align: center;
            font-size: 14px;
            font-weight: normal;
            color: #000;
        }

        button.btn.btn-danger {
            background-color: #e5e5e5;
            color: #333333
        }

        .right_div table tbody tr {
            background-color: #FFFFff !important;
        }

        .left_top {
            padding-left: 0px !important;
            padding-top: 17px;
            text-align: center;
        }

        .page-content-wrapper .page-content {
            /*margin-left: 0% !important;*/
            width: 82% !important;
            float: right !important;
        }
        .left_bottom{
            background-size: 100%;
        }

    </style>

</head>


<body>

<div class="con_div" style="height: 95% !important;min-height: 300px !important;">

    <div class="page-container" style="height: 100%;width: 100%;margin: 0 auto;min-height: 400px !important;">
        <!-- BEGIN SIDEBAR -->
        <div class="page-sidebar-wrapper">
            <%--NEW BAR ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！--%>
            <div class="page-sidebar" style="width: 18%">
                <div class="left_bottom">
                    <div class="left_top">
                        <%--<img src="${ctx}/resources/img/logo.png">--%>
                        <shiro:hasRole name="admin">
                            <span style="font-weight: bold;color: white;font-size: 20px;">${applicationScope.menus['systemRole_admin']}</span>
                        </shiro:hasRole>
                        <shiro:hasRole name="root">
                            <span style="font-weight: bold;color: white;font-size: 18px;">${applicationScope.menus['systemRole_root']}</span>
                        </shiro:hasRole>
                    </div>
                    <div class="user_div">
                        <table cellspacing="0" cellpadding="0" border="0" align="center">
                            <tbody>
                            <tr>
                                <td colspan="2"><img src="${ctx}/resources/img/user.png" width="65" height="65"></td>
                            </tr>
                            <tr>
                                <td>欢迎您！${sessionScope.userName}</td>
                                <td><a style="border-radius: 6px;margin-left:6px;color: #FFFFff"
                                       href="${ctx}/logout"><span class="glyphicon glyphicon-log-out"></span></a></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="left_div">
                        <shiro:hasRole name="root">
                            <ul>
                                <li><a  href="${ctx}/subjectMgmt/subjectIndex" style=" white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"><i class="fa fa-cog"
                                                                                 aria-hidden="true"></i>
                                        ${applicationScope.menus['organization_title']}注册管理</a>
                                </li>
                                <li><a class="" href="${ctx}/resCatalog"><i class="fa fa-book"
                                                                            aria-hidden="true"></i> 数据分类管理</a>
                                </li>
                                <li><a class="" href="${ctx}/group/list"><i class="fa fa-users"
                                                                            aria-hidden="true"></i> 用户与组管理</a>
                                </li>
                                <li><a class="" href="${ctx}/dataRelease"><i class="fa fa-upload"
                                                                             aria-hidden="true"></i> 发布审核管理</a>
                                </li>
                                <li><a href="${ctx}/admin/metadata"><i class="fa fa-cog" aria-hidden="true"></i>
                                    元数据管理</a>
                                        <%--<ul>--%>
                                        <%--<li class="l2-menu"><a href="${ctx}/metaTemplateManage"><i--%>
                                        <%--class="fa fa-bars"></i>元数据模板管理</a></li>--%>
                                        <%--</ul>--%>
                                </li>
                                <li><a class="" href="${ctx}/statisticalDataDetail"><i class="fa  fa-bar-chart"
                                                                                       aria-hidden="true"></i>
                                    数据统计管理</a>
                                </li>
                                <li><a class="" href="${ctx}/theme/"><i class="fa  fa-bar-chart"
                                                                                       aria-hidden="true"></i>
                                    主题库</a>
                                </li>
                            </ul>

                        </shiro:hasRole>
                        <shiro:hasRole name="admin">
                            <ul>
                                    <%--<li><a href="${ctx}/authorization"><i class="fa fa-cog" aria-hidden="true"></i>
                                            ${applicationScope.menus['organization_title']}用户授权</a></li>--%>
                                <li><a href="javaScript:void(0);"><i class="fa fa-database" aria-hidden="true"></i>
                                    关系数据管理</a>
                                    <ul>
                                        <li class="l2-menu"><a href="${ctx}/dataConfiguration"><i
                                                class="fa fa-bars"></i>数据字段配置</a></li>
                                        <li class="l2-menu"><a href="${ctx}/createTableAndImportData"><i
                                                class="fa fa-bars"></i>创建新表</a></li>
                                        <li class="l2-menu"><a href="${ctx}/recordManage"><i
                                                class="fa fa-bars"></i>数据记录管理</a>
                                            <div id="alltableName"
                                                 style="max-height:230px;overflow: auto;display:none;margin-top:1%;">

                                            </div>
                                        </li>
                                        </li>
                                    </ul>
                                </li>
                                <li><a href="${ctx}/fileMange"><i class="fa fa-file" aria-hidden="true"></i>
                                    文件数据管理</a></li>
                                <li><a href="${ctx}/dataRelease"><i class="fa fa-upload" aria-hidden="true"></i>
                                    数据发布管理</a></li>
                                <li class=""><a href="${ctx}/metaTemplate/toMateTemMsgList"><i class="fa fa-bars"></i>元数据模板管理</a>
                                </li>
                                        <li class=""><a href="${ctx}/ceshi"><i class="fa fa-bars"></i>测试</a>
                                        </li>
                            </ul>
                        </shiro:hasRole>

                    </div>
                </div>
            </div>
            <%--NEW BAR ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！--%>

        </div>
        <!-- END SIDEBAR -->

        <!-- BEGIN CONTENT -->
        <div class="page-content-wrapper">
            <div class="page-content" style="height:100%">
                <div class="right_div">
                    <div id="content-top">
                        <div class="fabu_div2"></div>
                    </div>
                    <div id="content">
                        <sitemesh:write property="body"/>
                    </div>
                </div>
            </div>
        </div>
        <!-- END CONTENT -->
    </div>
</div>

<div class="foot_div" style="height: 5% !important;position: relative;">
    <div style="position: absolute;top: 50%;left: 48%; margin-top: -14px;margin-left: -170px;">
        ${applicationScope.menus['copyright']}
    </div>
</div>


<script src="${ctx}/resources/bundles/jquery/jquery.min.js"></script>
<script src="${ctx}/resources/bundles/jquery/jquery-migrate.min.js"></script>
<script src="${ctx}/resources/bundles/bootstrapv3.3/js/bootstrap.min.js"></script>
<script src="${ctx}/resources/bundles/frontend/layout/scripts/back-to-top.js"></script>
<script src="${ctx}/resources/bundles/frontend/layout/scripts/layout.js"></script>
<script src="${ctx}/resources/bundles/bootbox/bootbox.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/bundles/metronic/global/scripts/metronic.js"></script>
<script type="text/javascript" src="${ctx}/resources/bundles/metronic/scripts/layout.js"></script>
<script type="text/javascript" src="${ctx}/resources/bundles/artTemplate/template.js"></script>
<script src="${ctx}/resources/js/regex.js"></script>
<script src="${ctx}/resources/bundles/jquery-validation/js/jquery.validate.min.js"></script>
<script src="${ctx}/resources/bundles/jquery-validation/js/additional-methods.min.js"></script>
<script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
<script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
<script src="${ctx}/resources/js/dataRegisterEditTableFieldComs.js"></script>
<script src="${ctx}/resources/bundles/bootstrap-closable-tab/bootstrap-closable-tab.js"></script>
<script src="${ctx}/resources/bundles/nicescroll/jquery.nicescroll.min.js"></script>


<script type="text/javascript">
    var userName = "${sessionScope.userName}";

    template.helper("dateFormat", convertMilsToDateString);
    template.helper("dateTimeFormat", convertMilsToDateTimeString);

    function convertMilsToDateTimeString(mil) {
        var date = new Date(mil);
        return date.Format("yyyy-MM-dd hh:mm:ss");
    }

    function convertMilsToDateString(mil) {
        var date = new Date(mil);
        return date.Format("yyyy-MM-dd");
    }

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
    jQuery(document).ready(function () {
        $("li ul").hide();
        $("div.left_div a[href='javaScript:void(0);']").click(function () {
            var sonUl = $(this).next();
            var sonUlIsHidden = sonUl.is(":hidden");
            if (sonUlIsHidden) {
                window.location.href = sonUl.find("li:eq(0) a").attr("href");
                sonUl.show();
            } else {
                sonUl.hide();
            }
        });
        scrolltotop.init('${ctx}');
        Layout.init();
        bootbox.setLocale("zh_CN");
        var path = window.location.pathname;
        if (path.indexOf('?') > -1) {
            path = path.substring(0, path.indexOf('?'));
        }

        $("div.left_div ul a").each(function () {
            var href = $(this).attr("href");
            if (href.indexOf('?') > -1) {
                href = href.substring(0, href.indexOf('?'));
            }
            if (href === path) {
                $(this).addClass("active");
                var level2 = $(this).parent().is("[class='l2-menu']");
                var level3 = $(this).parent().is("[class='l3-menu']");
                var oneName, twoName, threeName;
                if (level3) {
                    threeName = $(this).text();

                    $(this).parent().parent().show();
                    twoName = $(this).parent().parent().prev().text();
                    $(this).parent().parent().prev().addClass("active");

                    $(this).parent().parent().parent().show();
                    oneName = $(this).parent().parent().parent().prev().text();
                    $(this).parent().parent().parent().prev().addClass("active");
                } else if (level2) {
                    twoName = $(this).text();

                    $(this).parent().parent().show();
                    oneName = $(this).parent().parent().prev().text();
                    $(this).parent().parent().prev().addClass("active");
                } else {
                    oneName = $(this).text();
                }
                $("div.time_div a").append(oneName);
                if (typeof threeName !== "undefined") {
                    $("div.time_div").append("--&gt;" + "<a>" + twoName + "</a>");
                    $("div.time_div").append("--&gt;" + "<a>" + threeName + "</a>");
                    $(".fabu_div2").append(threeName);
                } else if (typeof twoName !== "undefined") {
                    $("div.time_div").append("--&gt;" + "<a>" + twoName + "</a>");
                    $(".fabu_div2").append(twoName);
                } else {
                    $(".fabu_div2").append(oneName);
                }
            }
        });

        var otherPath = path.split("/").pop();
        if (otherPath === "dataSourceDescribe") {
            $(".time_div").append("<a>数据发布管理</a>--&gt;<a>新增数据发布</a>");
        } else if (otherPath === "loginSuccess") {
            $(".time_div").append("<a>节点信息</a>");
            $(".fabu_div2").append("<a>节点信息</a>");
        } else if (otherPath === "editResource") {
            $(".time_div").append("<a>数据发布管理</a>--&gt;<a>数据集编辑</a>");
        }

        // 根据显示器重置 div.page-content 实际内容区域大小
        var $page_content = $(".page-content");
        $page_content.css("margin-left", $(".page-sidebar").width());
        // $page_content.css("min-height", $page_content.height() - $(".foot_div").height());
        $page_content.css("min-height", "200px");
        $page_content.css("height", "100%");


        $("#content").height("86%");

        // $("#alltableName").niceScroll({cursorborder: "", cursorcolor: "#155aab", boxzoom: true}); // First scrollable DIV
    });

</script>
<sitemesh:write property="div.siteMeshJavaScript"/>
<sitemesh:write property="div.artTemplate"/>

</body>
</html>
