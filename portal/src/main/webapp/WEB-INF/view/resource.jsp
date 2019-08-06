<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2018/10/24
  Time: 10:28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>

<html>

<head>
    <title>数据发布管理系统</title>
    <link href="${ctx}/resources/bundles/rateit/src/rateit.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css">
</head>

<body>

<div class="page-content" style="min-height: 650px;">
    <h3><b>资源列表页面</b></h3>

    <div class="row">
    <div class="col-xs-12 col-md-12 col-lg-12">
        <div class="alert alert-info" role="alert">
            <!--查询条件 -->
            <div class="row">
                <div class="col-md-12">

                    <label class="control-label">资源名称:</label>
                    <input type="text" id="title" class="input-small search-text">
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <label class="control-label">资源类型:</label>
                    <%--<input type="text" id="publicType" class="input-small search-text">--%>
                    <select name="publicType" id="publicType" class="input-small search-text">
                        <option value="" selected="selected">全部</option>
                        <option value="关系数据库">关系数据库</option>
                        <option value="文件">文件</option>
                    </select>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <label class="control-label">状态:</label>
                    <input type="text" id="status" class="input-small search-text">
                    &nbsp;&nbsp;
                    <button id="btnSearch" name="btnSearch" onclick="search();" class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查询</button>
                    &nbsp;&nbsp;
                    <button id="btnAdd" name="btnAdd" onclick="" class="btn info green btn-sm"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新建发布</button>
                </div>
            </div>

        </div>
        <div class="table-scrollable">
            <table class="table table-striped table-bordered table-advance table-hover">
                <thead>
                <tr>
                    <th style="width: 5%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">编号</th>
                    <th style="width: 30%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">
                        资源名称
                    </th>
                    <th style="width: 15%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">资源类型</th>
                    <th style="width: 20%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">发布时间</th>
                    <th style="width: 10%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">状态</th>
                    <th style="width: 25%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">操作</th>
                </tr>
                </thead>
                <tbody id="resourceList"></tbody>
            </table>
        </div>
        <div class="row margin-top-20">
            <div class="col-md-6 margin-top-10">
                当前第&nbsp;<span style="color:blue;" id="currentPageNo"></span>&nbsp;页,&nbsp;共&nbsp;<span
                    style="color:blue;"
                    id="totalPages"></span>页，<span
                    style="color:blue;" id="totalCount"></span>&nbsp;条数据
            </div>
            <div class="col-md-6">
                <div id="pagination" style="float: right"></div>
            </div>
        </div>
    </div>
</div>
</div>

<script type="text/html" id="resourceTmpl">
    {{each resourceList}}
    <tr>
        <td style="text-align: center">{{(currentPage-1)*pageSize+$index+1}}</td>
        <td><a href="javascript:editData('{{$value.id}}');">{{$value.title}}</a>
        </td>
        <td style="text-align: center">{{$value.publicType}}</td>
        <td style="text-align: center">{{dateFormat($value.creationDate)}}</td>
        <td style="text-align: center">{{$value.status}}</td>
        <td id="{{$value.id}}" style="text-align: center">
            <%--<button class="btn default btn-xs green-stripe" onclick="viewData()">查看</button>&nbsp;&nbsp;--%>
            <button class="btn default btn-xs purple updateButton" onclick="editData('{{$value.id}}')"><i class="fa fa-edit"></i>&nbsp;&nbsp;修改</button>&nbsp;&nbsp;
            <button class="btn default btn-xs red" onclick="deleteData('{{$value.id}}')"><i class="fa fa-trash"></i>&nbsp;&nbsp;删除</button>
        </td>
    </tr>
    {{/each}}
</script>

</body>

<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/bundles/rateit/src/jquery.rateit.js" type="text/javascript"></script>
    <script src="${ctx}/resources/js/subStrLength.js"></script>
    <script src="${ctx}/resources/js/regex.js"></script>
    <script src="${ctx}/resources/bundles/jquery/jquery.min.js"></script>
    <script src="${ctx}/resources/bundles/jquery-bootpag/jquery.bootpag.min.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.js"></script>
    <script src="${ctx}/resources/bundles/artTemplate/template.js"></script>

    <script type="text/javascript">
        var ctx = '${ctx}';
        var currentPageNo = 1;


        $(function () {
            template.helper("dateFormat", formatDate);
            getData(1);
            $(".search-text").keydown(function (event) {
                if (event.keyCode == 13){
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
        });

        function search() {
            getData(1);
        }

        function getData(pageNo) {
            $.ajax({
                url: "${ctx}/resource/getPageData",
                type: "get",
                dataType: "json",
                data: {
                    "title":$.trim($("#title").val()),
                    "publicType":$("#publicType").val(),
                    "status":$("#status").val(),
                    "pageNo": pageNo,
                    "pageSize": 10
                },
                success: function (data) {
                    console.log(data);
                    var html = template("resourceTmpl", data);
                    console.log(html);
                    $("#resourceList").empty();
                    $("#resourceList").append(html);
                    $("#currentPageNo").html(data.currentPage);
                    currentPageNo = data.currentPage;
                    $("#totalPages").html(data.totalPages);
                    $("#totalCount").html(data.totalCount);
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
                        currentPageNo = num;
                    });
                }
            });
        }


        function deleteData(id) {
            bootbox.confirm("确定要删除此条记录吗？", function (r) {
                if (r) {
                    $.ajax({
                        url: ctx + "/resource/delete/" + id,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.result == 'ok') {
                                toastr["success"]("删除成功！", "数据删除");
                                getData(currentPageNo);
                            }
                            else {
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
    </script>
</div>

</html>
