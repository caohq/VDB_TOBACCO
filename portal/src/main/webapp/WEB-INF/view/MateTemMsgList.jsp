<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2019/03/14
  Time: 10:42
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>

<html>

<head>
    <title>元数据管理</title>
    <link href="${ctx}/resources/bundles/rateit/src/rateit.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/bundles/select2/select2.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">

        #table_List1 th, #table_List2 th {
            background-color: #64aed9;
            text-align: center;
            font-family: 'Arial Negreta', 'Arial Normal', 'Arial';
            font-weight: 700;
            font-style: normal;
            font-size: 18px;
            color: #FFFFFF;
        }

        .arrListSty {
            display: inline-block;
            margin-right: 5px;
            background-color: #aaaaaa;
            padding: 3px 6px;
            margin-bottom: 5px;
        }

        .modediv {
            padding-top: 7px;
            margin-top: 1px;
        }
    </style>
</head>

<body>


<%--<div class="alert alert-info" role="alert" >--%>
<%--<!--查询条件 -->--%>
<%--<div class="row">--%>
<%--<div class="col-md-12 form-inline">--%>

<%--<label class="control-label">元数据英文名:</label>--%>
<%--<input type="text" id="queryExtField" name="extField" class="form-control search-text ">--%>
<%--&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--<label class="control-label">元数据中文名称:</label>--%>
<%--<input type="text" id="queryExtFieldName" name="extFieldName" class="form-control search-text ">--%>
<%--&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--<button id="btnSearch" name="btnSearch" onclick="search();" class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查询</button>--%>
<%--&nbsp;&nbsp;--%>
<%--<button id="btnAdd" name="btnAdd" onclick="" class="btn info green btn-sm"><i--%>
<%--class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增--%>
<%--</button>--%>
<%--</div>--%>
<%--</div>--%>

<%--</div>--%>
<div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">

    <div class="table-message-group">列表加载中......</div>
    <div class="table-scrollable" style="padding-top: 0px;">

        <table class="table table-striped table-bordered table-advance table-hover">
            <thead>
            <tr id="table_List2">
                <th style="width: 8%;">编号</th>
                <th style="width: 20%;">
                    模板名称
                </th>
                <th style="width: 18%;">
                    模板创建时间
                </th>
                <th style="width: 8%;">模板创建者</th>
                <th style="width: 8%;">模板备注</th>
                <th style="width: 8%;">数据集名称</th>
                <%--<th style="width: 15%;">简介</th>--%>
                <th style="width: 18%;">操作</th>
            </tr>
            </thead>
            <tbody id="metadataList">

            </tbody>
        </table>
    </div>

</div>


<!--查看元数据-->
<div class="modal fade" tabindex="-1" role="dialog" id="checkModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">元数据模板详情</h4>
            </div>
            <div class="modal-body" style="max-height: 500px;overflow: auto">
                <%--<form class="form-horizontal" id="editMetadataForm" method="post" accept-charset="utf-8" role="form"  onfocusout="true">--%>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">模板名称:</label>
                        <div class="col-sm-8 modediv" id="rel-metaName"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">模板创建者:</label>
                        <div class="col-sm-8 modediv" id="rel-createdMeta"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">模板创建日期:</label>
                        <div class="col-sm-8 modediv" id="rel-creationDate"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">模板备注:</label>
                        <div class="col-sm-8 modediv" id="rel-MetaRemarks"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">数据集名称:</label>
                        <div class="col-sm-8 modediv" id="rel-title"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">简介:</label>
                        <div class="col-sm-8 modediv" id="rel-introduction" style="work-break:break-all"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">图片地址:</label>
                        <div class="col-sm-8 modediv" id="rel-imagePath">
                            <img src="" alt="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">资源关键词:</label>
                        <div class="col-sm-8 modediv" id="rel-keyword"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">分类目录ID:</label>
                        <div class="col-sm-8 modediv" id="rel-catalogId"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">开始时间:</label>
                        <div class="col-sm-8 modediv" id="rel-startTime"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">结束时间:</label>
                        <div class="col-sm-8 modediv" id="rel-endTime"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">版权声明:</label>
                        <div class="col-sm-8 modediv" id="rel-createdByOrganization"></div>
                    </div>
                    <hr style="border-bottom:1px dashed #111;">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">创建者机构:</label>
                        <div class="col-sm-8 modediv" id="rel-createOrgnization"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">创建人员:</label>
                        <div class="col-sm-8 modediv" id="rel-createdPerson"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">元数据中的创建日期:</label>
                        <div class="col-sm-8 modediv" id="rel-MetaCreateDate"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布者机构:</label>
                        <div class="col-sm-8 modediv" id="rel-publishOrgnization"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布者邮件:</label>
                        <div class="col-sm-8 modediv" id="rel-email"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布者电话:</label>
                        <div class="col-sm-8 modediv" id="rel-phoneNum"></div>
                    </div>
                    <div id="extDate">
                        <hr style="border-bottom:1px dashed #111;">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">投影方式:</label>
                            <div class="col-sm-8 modediv" id="rel-projection"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">产品编号:</label>
                            <div class="col-sm-8 modediv" id="rel-productCode"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">数据加工方法:</label>
                            <div class="col-sm-8 modediv" id="rel-dataMethod"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">共享方式 :</label>
                            <div class="col-sm-8 modediv" id="rel-enjoyMethod"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">开火方式 :</label>
                            <div class="col-sm-8 modediv" id="rel-fireMode"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">增速比 :</label>
                            <div class="col-sm-8 modediv" id="rel-addspeed"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">年龄 :</label>
                            <div class="col-sm-8 modediv" id="rel-age"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">测试方法 :</label>
                            <div class="col-sm-8 modediv" id="rel-testMethod"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">制造日期 :</label>
                            <div class="col-sm-8 modediv" id="rel-manufactureDate"></div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">速度 :</label>
                            <div class="col-sm-8 modediv" id="rel-speed"></div>
                        </div>
                    </div>
                </form>


            </div>
            <div class="modal-footer">
                <%--<button type="button" class="btn green" onclick="submitEditData();" ><i--%>
                <%--class="glyphicon glyphicon-ok"></i>保存--%>
                <%--</button>--%>
                <button type="button" data-dismiss="modal" class="btn  btn-danger">退出</button>
            </div>
        </div>
    </div>
</div>


<!-- 元数据 -->
<script type="text/html" id="metadataTmpl">
    {{each list as value index}}
    <tr>
        <td style="display:table-cell; vertical-align:middle ; text-align: center;">{{index+1}}</td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;"> {{value.metaTemplateName}}
        </td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{dateFormat(value.metaTemplateCreateDate)}}</td>

        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{value.metaTemplateCreator}}</td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{value.memo}}</td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{value.title}}</td>
        <%--<td style="display:table-cell; vertical-align:middle ;text-align: left;">{{$value.introduction}}</td>--%>
        <td style="display:table-cell; vertical-align:middle;text-align: center;" id="{{value.id}}">
            <button class="btn default btn-xs purple updateButton" onclick="checkData('{{value.id}}')"><i
                    class="fa fa-eye"></i>查看
            </button>&nbsp;
            <button class="btn default btn-xs red" onclick="deleteData('{{value.id}}')"><i class="fa fa-trash"></i>删除
            </button>&nbsp;
        </td>
    </tr>
    {{/each}}
</script>


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
        <%--var subjectCode = '${SubjectCode}';--%>
        $(function () {
            template.helper("dateFormat", formatDate);
        });
        $().ready(function () {
            getMetaTemList();
        });


        function getMetaTemList() {
            $.ajax({
                url: "${ctx}/metaTemplate/getAllList",
                type: "post",
                dataType: "json",
                success: function (data) {
                    $("#metadataList").html("");
                    var html = template("metadataTmpl", data);
                    $(".table-message-group").html("");
                    $("#metadataList").append(html);
                    if (data.list.length == 0) {
                        $(".table-message-group").html("暂时没有数据");
                    }
                }
            })
        }

        function checkData(id) {
            $.ajax({
                url: "${ctx}/metaTemplate/getDetail",
                type: "post",
                dataType: "json",
                data: {"id": id},
                success: function (data) {
                    var list = data.list;
                    var extList = data.extList;

                    $("#rel-metaName").html(list.metaTemplateName);

                    $("#rel-createdMeta").html(list.metaTemplateCreator);
                    $("#rel-creationDate").html(convertMilsToDateString(list.metaTemplateCreateDate));
                    $("#rel-MetaRemarks").html(list.memo);
                    $("#rel-title").html(list.title);
                    $("#rel-introduction").html(list.introduction);

                    $("#rel-imagePath img").attr("src",list.imagePath);
                    $("#rel-imagePath img").attr("style","max-width:160px;");
                    var $keyword = $("#rel-keyword");
                    listSpan(list.keyword, ",", $keyword);

                    $("#rel-email").html(list.email);
                    $("#rel-catalogId").html(list.catalogId);
                    $("#rel-startTime").html(convertMilsToDateString(list.startTime));
                    $("#rel-endTime").html(convertMilsToDateString(list.endTime));

                    $("#rel-createdByOrganization").html(list.createdByOrganization);
                    $("#rel-createOrgnization").html(list.createOrgnization);
                    $("#rel-createdPerson").html(list.createPerson);
                    $("#rel-MetaCreateDate").html(list.creatorCreateTime);

                    $("#rel-publishOrgnization").html(list.publishOrgnization);
                    $("#rel-phoneNum").html(list.phoneNum);

                    //扩展数据
                    if (extList.length === 0) {
                        $("#extDate").hide();
                    } else {
                        $("#rel-projection").html(extList[0]);
                        $("#rel-productCode").html(extList[1]);
                        $("#rel-dataMethod").html(extList[2]);
                        $("#rel-enjoyMethod").html(extList[3]);
                        $("#rel-fireMode").html(extList[4]);
                        $("#rel-addspeed").html(extList[5]);
                        $("#rel-age").html(extList[6]);
                        $("#rel-testMethod").html(extList[7]);
                        $("#rel-manufactureDate").html(extList[8]);
                        $("#rel-speed").html(extList[9]);
                        $("#extDate").show();
                    }

                    $("#checkModal").modal("show")
                }
            });
        }

        function deleteData(id) {
            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>", function (r) {
                if (r) {
                    $.ajax({
                        url: "${ctx}/metaTemplate/deleteMetaTem",
                        type: "post",
                        dataType: "json",
                        data: {"id": id},
                        success: function (data) {
                             if(data.success==="success"){
                                 getMetaTemList();
                             }
                        }
                    });
                }
            });
        }
            function listSpan(arrStr, spl, ele) {
                var arrList = arrStr.split(spl);
                if (arrList.length == 1 && arrList[0] == "") {
                    ele.parent().hide();
                    ele.empty();
                    return
                } else {
                    ele.parent().show()
                }
                var arrListStr = "";
                for (var i = 0; i < arrList.length; i++) {
                    arrListStr += "<span class='arrListSty'>" + arrList[i] + "</span>"
                }
                ele.empty();
                ele.append(arrListStr)
            }

    </script>

</div>

</html>
