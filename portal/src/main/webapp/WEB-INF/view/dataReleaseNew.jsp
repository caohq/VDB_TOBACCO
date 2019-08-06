<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/30
  Time: 13:28
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
    <title>数据发布管理系统</title>
    <link href="${ctx}/resources/css/dataUpload.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/select2/select2.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .arrListSty {
            display: inline-block;
            margin-right: 5px;
            background-color: #aaaaaa;
            padding: 3px 6px;
            margin-bottom: 5px;
        }

        #allModal .form-group div {
            word-break: break-all;
        }

        .commentsList {
            border: 1px solid darkgray;
            margin-bottom: 5px;
            background-color: #7ad588;
            border-radius: 7px !important;
        }

        .commentsList .form-group {
            margin-bottom: 0px !important;
        }

        .commentsList label {
            padding-top: 0px !important;
        }

        .shenhe a {
            border-radius: 6px !important;
        }
        .form-control{
            display: inline;
            width: 70%;
        }
    </style>
</head>

<body>
<input type="hidden" id="subjectCode" value="${sessionScope.SubjectCode}"/>

<div class="alert alert-info fabu_div" role="alert">
    <!--查询条件 -->
    <div class="row">
        <form class="form-inline" style="margin-bottom: 0px">

            <label style="padding-left: 10px;color:black">数据集名称:</label>
            <input type="text" class="form-control" id="resourceName" placeholder="请输入数据集名称">


            <label style="padding-left: 10px;color:black">数据类型:</label>
            <select id="resourcePublicType" class="form-control" style="width: 120px">
                <option value="">全部</option>
                <option value="RDB">RDB</option>
                <option value="FILE">FILE</option>
            </select>


            <label style="padding-left: 10px;color:black">状态:</label>
            <select id="resourceState" class="form-control" style="width: 120px">
                <option value="">全部</option>
                <option value="1">待审核</option>
                <option value="-1">未完成</option>
                <option value="2">审核通过</option>
                <option value="0">审核未通过</option>
            </select>

            <button type="button" class="btn blue btn-sm" style="margin-left: 20px" id="seachResource"><i
                    class="fa fa-search"></i>&nbsp;&nbsp;查&nbsp;&nbsp;询
            </button>
            <shiro:hasRole name="admin">
                <button type="button" class="btn green btn-sm" style="margin-right: 15px;margin-left: 10px;"
                        onclick="newRelease()"><i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增数据发布
                </button>
            </shiro:hasRole>
        </form>
    </div>
</div>

<div class="upload-table" style="background: #fff;">
    <div class="table-message">列表加载中......</div>
    <table class="table table-bordered data-table" id="upload-list">
        <thead>
        <tr>
            <th width="4%">编号</th>
            <th width="30%">数据集名称</th>
            <th width="5%">类型</th>
            <%-- <th width="10%">来源位置</th>--%>
            <th width="10%">发布时间</th>
            <th width="10%">状态</th>
            <th width="16%">操作</th>
        </tr>
        </thead>
        <tbody id="bd-data">
        </tbody>
    </table>

    <div class="row margin-top-20 ">
        <div class="page-message" style="float: left;padding-left: 20px; line-height: 56px"></div>
        <div class="page-list" style="float: right; padding-right: 15px;"></div>
    </div>
</div>

<div id="allModal" class="modal fade" tabindex="-1" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">数据集详情查看</h4>
            </div>
            <div class="modal-body" style="max-height: 500px;overflow: auto">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">审核人姓名&nbsp;&nbsp;:</label>
                        <div class="col-sm-9" id="CommentsName">aaa</div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">审核时间&nbsp;&nbsp;:</label>
                        <div class="col-sm-9" id="CommentsTime">bbb</div>
                    </div>
                    <div class="form-group">
                            <label class="col-sm-3 control-label">审核内容&nbsp;&nbsp;:</label>
                        <div class="col-sm-9" id="CommentsContent">vvvv</div>
                        </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">数据集标识:</label>
                        <div class="col-sm-8 modediv" id="pid"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">数据集名称:</label>
                        <div class="col-sm-8 modediv" id="title"></div>
                    </div>
                    <div class="form-group" style="word-break: break-all">
                        <label class="col-sm-3 control-label">发布类型:</label>
                        <div class="col-sm-8 modediv" id="publicType"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">简介:</label>
                        <div class="col-sm-8 modediv" id="introduction" style="work-break:break-all"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布图片:</label>
                        <div class="col-sm-8 modediv" id="imagePath">
                            <img src="" style="max-width:160px;" alt="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">资源关键词:</label>
                        <div class="col-sm-8 modediv" id="keyword"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">分类目录ID:</label>
                        <div class="col-sm-8 modediv" id="catalogId"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">分类目录名称:</label>
                        <div class="col-sm-8 modediv" id="catalogName"></div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">开始时间:</label>
                        <div class="col-sm-8 modediv" id="startTime"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">结束时间:</label>
                        <div class="col-sm-8 modediv" id="endTime"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">版权声明:</label>
                        <div class="col-sm-8 modediv" id="createdByOrganization"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">状态:</label>
                        <div class="col-sm-8 modediv" id="status"></div>
                    </div>
                    <hr style="border-bottom:1px dashed #111;">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">创建人员:</label>
                        <div class="col-sm-8 modediv" id="createdBy"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">创建者机构:</label>
                        <div class="col-sm-8 modediv" id="createOrgnization"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">创建日期:</label>
                        <div class="col-sm-8 modediv" id="creationDate"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布者机构:</label>
                        <div class="col-sm-8 modediv" id="publishOrgnization"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布者邮件:</label>
                        <div class="col-sm-8 modediv" id="email"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">发布者电话:</label>
                        <div class="col-sm-8 modediv" id="phoneNum"></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">最新发布日期:</label>
                        <div class="col-sm-8 modediv" id="updateDate"></div>
                    </div>
                    <hr style="border-bottom:1px dashed #111;">
                    <div class="form-group FILE">
                        <label class="col-sm-3 control-label">实体文件路径:</label>
                        <div class="col-sm-8 modediv" id="filePath"></div>
                    </div>
                    <div class="form-group FILE">
                        <label class="col-sm-3 control-label">总文件数:</label>
                        <div class="col-sm-8 modediv" id="toFilesNumber"></div>
                    </div>
                    <div class="form-group FILE">
                        <label class="col-sm-3 control-label">总文大小:</label>
                        <div class="col-sm-8 modediv" id="toFilesMemorySize"></div>
                    </div>

                    <div class="form-group RDB">
                        <label class="col-sm-3 control-label">实体表名:</label>
                        <div class="col-sm-8 modediv" id="publicContent"></div>
                    </div>
                    <div class="form-group RDB">
                        <label class="col-sm-3 control-label">总记录数:</label>
                        <div class="col-sm-8 modediv" id="toRecordNumber"></div>
                    </div>
                    <div class="form-group RDB">
                        <label class="col-sm-3 control-label">总记录大小:</label>
                        <div class="col-sm-8 modediv" id="toRdbMemorySize"></div>
                    </div>

                    <div class="form-group RDBFILE">
                        <label class="col-sm-3 control-label">总存储量:</label>
                        <div class="col-sm-8 modediv" id="toMemorySize"></div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">用户组:</label>
                        <div class="col-sm-8 modediv" id="userGroupId"></div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

                <button type="button" data-dismiss="modal" class="btn btn-success">关闭</button>
            </div>
        </div>
    </div>
</div>

<div id="auditModal" class="modal fade" tabindex="-1" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">数据审核</h4>
            </div>
            <div class="modal-body" style="overflow: auto">
                <div id="AuditMessageList">
                </div>
                <div id="AuditMessage">
                    <form class="form-horizontal" id="submit_form1" accept-charset="utf-8" role="form" onfocusout="true"
                          method="POST">
                        <div class="form-group">
                            <label class="control-label col-md-3">审核结果 <span class="required">
                                                    * </span>
                            </label>
                            <div class="col-md-7" style="padding-top:13px">
                                <span style="margin-right:10%">
                                    <input id="radio1" type="radio" name="audit_status" value="2" checked/>
                                    <label for="radio1">审核通过</label>
                                </span>
                                <span>
                                    <input id="radio2" type="radio" name="audit_status" value="0"/>
                                    <label for="radio2">审核未通过</label>
                                </span>

                            </div>

                        </div>
                        <div id="releas_remark" class="form-group " style="display: none;">
                            <label class="control-label col-md-3" for="audit_content">审核详情
                                <span class="required">* </span>
                            </label>
                            <div class="col-md-7" style="padding-top:13px">
                                                    <textarea type="text" class="form-control" cols="30" rows="4"
                                                              placeholder="请输入审核结果理由，不少于6个字符"
                                                              id="audit_content" name="audit_content"
                                                              required="required"></textarea>

                            </div>
                        </div>

                        <div id="selectGroup" class="form-group" style="margin-left: 64px;">
                            <div class="col-sm-2 text-right">
                                <label for="permissions" class="control-label">
                                    公开范围
                                    <span class="required">*</span>
                                </label>
                            </div>
                            <div class="col-sm-8" style="padding-top: 7px;">
                                <select class='form-control select2me' name='permissions' id='permissions' multiple>
                                </select>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn green" auditId="" id="auditId"><i
                        class="glyphicon glyphicon-ok"></i>确认
                </button>
                <button type="button" class="btn default" onclick="remValidate()">取消</button>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="resourceTmp1">
    {{each resourceList as value i}}
    <tr keyIdTr="{{value.id}}">
        <td>{{i + 1}}</td>
        <td><a href="javascript:void(0)" onclick="showData('{{value.id}}','{{value.publicType}}','{{value.status}}')">{{value.title}}</a>
        </td>
        <td>{{value.publicType}}</td>
        <%-- <td style="word-break: break-all">{{value.createdByOrganization}}</td>--%>
        <td>{{dateFormat(value.creationDate)}}</td>

        {{if value.status == '1'}}
        <td id="{{value.dataTaskId}}">待审核</td>
        {{else if value.status == '0'}}
        <td id="{{value.dataTaskId}}" style="color: #d9534f">审核未通过&nbsp;<span class="glyphicon glyphicon-remove"></span>
        </td>
        {{else if value.status == '2'}}
        <td id="{{value.dataTaskId}}" style="color: #5cb85c">审核通过&nbsp;<span class="glyphicon glyphicon-ok"></span></td>
        {{else if value.status == '-1'}}
        <td id="{{value.dataTaskId}}" <%--style="color:#ec971f"--%>>未完成</td>
        {{/if}}

        <td>
            <table style="margin-left:10%;">
                <tr>


                    <shiro:hasRole name="admin">
                        <td class="chakan"><a href="javascript:;"
                                              onclick="showData('{{value.id}}','{{value.publicType}}','{{value.status}}')">
                            <i class="fa fa-eye" aria-hidden="true"></i> 查看</a></td>
                        <td width="5"></td>
                        {{if value.status != '2'}}
                        <td class="bianji"><a href="javascript:;" onclick="editdatarelease(this)"
                                              keyIdTd="{{value.id}}">
                            <i class="fa fa-pencil-square-o" aria-hidden="true"></i>编辑</a>
                        </td>
                        <td width="5"></td>
                        {{/if}}
                        <td class="shanchu"><a href="javascript:;" onclick="removeData('{{value.id}}');"><i
                                class="fa fa-window-close-o" aria-hidden="true"></i> 删除</a>
                        </td>
                    </shiro:hasRole>

                    <shiro:hasRole name="root">

                        {{if value.status == '1'}}
                        <td class="shenhe"><a href="javascript:;" onclick="auditRelease('{{value.id}}')">
                            <i class="fa fa-user-circle-o" aria-hidden="true"></i> 审核</a></td>
                        <td width="5"></td>
                        {{/if}}
                        {{if value.status == '0'}}
                        <td class="shenhe"><a href="javascript:;" onclick="auditRelease('{{value.id}}')">
                            <i class="fa fa-user-circle-o" aria-hidden="true"></i> 审核</a></td>
                        <td width="5"></td>
                        {{/if}}

                        {{if value.status == '-1'}}
                        <td class="chakan"><a href="javascript:;"
                                              onclick="showData('{{value.id}}','{{value.publicType}}','{{value.status}}')">
                            <i class="fa fa-eye" aria-hidden="true"></i> 查看</a></td>
                        <td width="5"></td>

                        {{else if value.status != '-1'}}
                        <td class="chakan"><a href="javascript:;"
                                              onclick="showData('{{value.id}}','{{value.publicType}}','{{value.status}}')">
                            <i class="fa fa-eye" aria-hidden="true"></i> 查看</a></td>
                        <td width="5"></td>

                        {{/if}}

                        {{if value.status == '2'}}
                        <td class="shanchu"><a href="javascript:;" onclick="disableRelease('{{value.id}}')">
                            <i class="fa fa-window-close-o" aria-hidden="true"></i>停用</a></td>
                        {{/if}}
                    </shiro:hasRole>
                </tr>
            </table>
        </td>
    </tr>
    {{/each}}
</script>

<script type="text/html" id="resourceTmp2">
    {{each auditMessageList as value i}}
    <div class="commentsList">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label">审核人姓名&nbsp;&nbsp;:</label>
                <div class="col-sm-9">{{value.auditPerson}}</div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">审核时间&nbsp;&nbsp;:</label>
                <div class="col-sm-9">{{dateFormat(value.auditTime)}}</div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">审核内容&nbsp;&nbsp;:</label>
                <div class="col-sm-9">{{value.auditCom}}</div>
            </div>
        </form>
    </div>
    {{/each}}
</script>

<script type="text/html" id="groupOption">
    {{each data as value i}}
    <option value="{{value.groupName}}">{{value.groupName}}</option>
    {{/each}}
</script>
</body>

<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script type="text/javascript" src="${ctx}/resources/bundles/select2/select2.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/select2/select2_locale_zh-CN.js"></script>
    <script type="text/javascript">
        var publicType = "";
        var resourceState = "";
        var resourceName = "";
        var statusres = "2";
        $(function () {
            template.helper("dateFormat", formatDate);
            tableConfiguration2(1, "", "", "")
        });
        $("#resourcePublicType").on("change", function () {
            publicType = $("#resourcePublicType option:selected").val();
        });
        $("#resourceState").on("change", function () {
            resourceState = $("#resourceState option:selected").val();
        });
        $("#seachResource").click(function () {
            resourceName = $("#resourceName").val();
            tableConfiguration2(currentPageNumber(), publicType, resourceState, resourceName);
        });

        function editdatarelease(i) {
            var id = $(i).attr("keyIdTd");
            console.log(id);
            window.location.href = "${ctx}/resource/editResource?resourceId=" + id;
        }

        var validData = {
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "", // validate all fields including form hidden input
            rules: {
                audit_status: {
                    required: true
                },
                audit_content: {
                    required: true,
                    minWords: true
                }
            },
            messages: {
                audit_status: {
                    required: "请输入数据集名称"
                },
                audit_content: {
                    required: "请输入审核具体信息"
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
        jQuery.validator.addMethod("minWords", function (value, element) {
            var str = $("#audit_content").val();
            var bytesCount = 0;
            for (var i = 0; i < str.length; i++) {
                var c = str.charAt(i);
                if (/^[\u0000-\u00ff]$/.test(c)) {          //匹配双字节
                    bytesCount += 1;
                } else if (/^[\u4e00-\u9fa5]$/.test(c)) {
                    bytesCount += 2;
                } else {
                    bytesCount += 1;
                }
            }
            var workFlag = bytesCount < 6 ? false : true
            return this.optional(element) || ($("#dataDescribeID").val() == "" || workFlag);
        }, "最少输入6个字符");
        $("#submit_form1").validate(validData)

        function remValidate() {
            $("#submit_form1").validate().resetForm();
            $(".has-error").removeClass("has-error")
            $('#auditModal').modal('hide')

        }

        function auditRelease(id) {
            $("#auditId").attr("auditId", id);
            $("#audit_status option:eq(0)").prop("selected", true);
            $("#audit_content").val("");
            $("#AuditMessageList").empty();
            $.ajax({
                url: "${ctx}/resource/getAuditMessage",
                type: "GET",
                data: {
                    resourceId: id
                },
                success: function (data) {
                    var list = JSON.parse(data)
                    console.log(list)
                    var tabCon = template("resourceTmp2", list);
                    $("#AuditMessageList").append(tabCon);
                    initSelectGroup();
                    $("#audit_content").val("审核通过!");
                    $("#auditModal").modal("show")
                },
                error: function () {
                    console.log("请求失败")
                }
            })

        }

        $("#auditId").click(function () {
            var id, auditContent, userGroupId;
            if (statusres === "2") {
                if ($("#permissions").val() === null) {
                    toastr["info"]("请选择数据集的公开范围", "提示！");
                    return;
                } else {
                    userGroupId = $("#permissions").val().toString();
                }
            } else if (statusres === "0") {
                if (!$("#submit_form1").valid()) {
                    return
                }
            }

            id = $(this).attr("auditId");
            auditContent = $("#audit_content").val();
            $.ajax({
                url: "${ctx}/resource/audit",
                type: "POST",
                data: {
                    resourceId: id,
                    status: statusres,
                    auditContent: auditContent,
                    userGroupId: userGroupId
                },
                success: function (data) {
                    $('#auditModal').modal('hide');
                    tableConfiguration2(currentPageNumber(), "", "", "")
                },
                error: function () {
                    console.log("请求失败")
                }
            })

        });

        function disableRelease(id) {
            var currentTargetName = $.trim($(event.currentTarget).parent().parent().parent().parent().parent().parent().find("td:eq(1)").text());
            bootbox.confirm("<span style='font-size: 16px'>确认停用<span style='font-size: 16px;color: #5b9bd1'>" + currentTargetName + "</span>么？</span>" +
                "<div style='margin-top:2%;width: 80%;'><textarea  id='reason' class='form-control' placeholder='请输入停用理由' value=''/><span id='span_alert' style='color:red;margin-left: 1%;'>*(必填)</span></div>", function (r) {
                if (r) {
                    if($("#reason").val()!==null && $("#reason").val()!==""){
                        $.ajax({
                            url: "${ctx}/resource/stopResource",
                            type: "POST",
                            data: {
                                resourceId: id
                            },
                            success: function (data) {
                                tableConfiguration2(currentPageNumber(), "", "", "");
                            //    停用成功，发送邮件通知
                                $.ajax({
                                    url: "${ctx}/resource/sendStopEmail",
                                    type: "POST",
                                    data: {
                                        resourceId: id,
                                        reason:$("#reason").val()
                                    }
                            })
                            },
                            error: function () {
                                console.log("请求失败")
                            }
                        })
                    }else{
                        toastr["warning"]("停用理由不能为空！");
                    }
                }
            })

        }

        function showData(id, type, tabStatus) {
            tabStatus = tabStatus == 0 ? "审核未通过" : tabStatus == 1 ? "未审核" : "审核通过"
            if (tabStatus == "审核未通过") {
                $.ajax({
                    url: "${ctx}/resource/getAuditMessage",
                    type: "GET",
                    data: {
                        resourceId: id
                    },
                    success: function (data) {
                        var list = JSON.parse(data).auditMessageList[0]
                        console.log(list)
                        if (type == "mysql" || type == "") {
                            $("#mysqlCommentsName").html(list.auditPerson)
                            $("#mysqlCommentsTime").html(convertMilsToDateTimeString(list.auditTime))
                            $("#mysqlCommentsContent").html(list.auditCom)
                        } else {
                            $("#fileCommentsName").html(list.auditPerson)
                            $("#fileCommentsTime").html(convertMilsToDateTimeString(list.auditTime))
                            $("#fileCommentsContent").html(list.auditCom)
                        }
                        // 当前登录为系统管理员 不显示未通过审核原因
                        var currentUser = $("#subjectCode").val();
                        if (currentUser !== "") {
                            $("#mysqlComments").show();
                        } else {
                            $("#mysqlComments").hide();
                        }
                    },
                    error: function () {
                        console.log("请求失败")
                    }
                })
            }
            $.ajax({
                url: "${ctx}/resource/resourceDetail",
                type: "GET",
                dataType: "JSON",
                data: {
                    resourceId: id
                },
                success: function (data) {
                    var resource = data.resource;
                    var resCatalog = data.resCatalog;
                    $("#dataSourceId").html(id);
                    $("#email").html(resource.email);
                    $("#catalogId").html(resCatalog.rid);
                    $("#catalogName").html(resCatalog.name);
                    $("#imagePath img").attr("src", resource.imagePath);
                    $("#status").html(tabStatus);
                    $("#resState").html(resource.resState);
                    $("#publishOrgnization").html(resource.publishOrgnization);
                    $("#createOrgnization").html(resource.createOrgnization);
                    $("#publicType").html(resource.publicType);
                    var $publicContent = $("#publicContent");
                    listSpan(resource.publicContent, ";", $publicContent);
                    var $filePath = $("#filePath");
                    var str = resource.filePath.replace(/%_%/g, "/");
                    var filrStr = str.substr(0, str.length - 1);
                    listSpan(filrStr, ";", $filePath);
                    $("#fileName").html(resource.fileName);
                    $("#fieldComs").html(resource.fieldComs);
                    $("#subjectCode").html(resource.subjectCode);
                    $("#pid").html(id);
                    $("#title").html(resource.title);
                    $("#introduction").html(resource.introduction);
                    var $keyword = $("#keyword");
                    listSpan(resource.keyword, ",", $keyword);
                    $("#taxonomy").html(resource.taxonomy);
                    $("#dataFormat").html(resource.dataFormat);
                    if (resource.startTime == null) {
                        $("#startTime").parent().hide()
                    } else {
                        $("#startTime").parent().show()
                    }
                    if (resource.endTime == null) {
                        $("#endTime").parent().hide()
                    } else {
                        $("#endTime").parent().show()
                    }
                    if (resource.createdByOrganization === "") {
                        $("#createdByOrganization").parent().hide()
                    } else {
                        $("#createdByOrganization").parent().show()
                    }
                    if (resource.createPerson === "") {
                        $("#createdBy").parent().hide()
                    } else {
                        $("#createdBy").parent().show()
                    }
                    if (resource.creationDate == null) {
                        $("#creationDate").parent().hide()
                    } else {
                        $("#creationDate").parent().show()
                    }
                    if (resource.phoneNum === "") {
                        $("#phoneNum").parent().hide()
                    } else {
                        $("#phoneNum").parent().show()
                    }

                    if (resource.publicType === "RDB") {
                        $(".FILE").hide();
                        $(".RDB").show();
                        $(".RDBFILE").hide();
                    } else if (resource.publicType === "FILE") {
                        $(".FILE").show();
                        $(".RDB").hide();
                        $(".RDBFILE").hide();
                    } else {
                        $(".FILE").show();
                        $(".RDB").show();
                        $(".RDBFILE").show();
                    }

                    $("#startTime").html(convertMilsToDateString(resource.startTime));
                    $("#endTime").html(convertMilsToDateString(resource.endTime));
                    $("#createdByOrganization").html(resource.createdByOrganization);
                    $("#createdBy").html(resource.createPerson);
                    $("#creationDate").html(convertMilsToDateString(resource.creatorCreateTime));
                    $("#organizationName").html(resource.organizationName);
                    $("#mail").html(resource.mail);
                    $("#phoneNum").html(resource.phoneNum);
                    $("#updateDate").html(convertMilsToDateTimeString(resource.updateDate));
                    $("#citation").html(resource.citation);

                    $("#toFilesNumber").html(resource.toFilesNumber);
                    $("#toFilesMemorySize").html(getPrintSize(data.toFilesMemorySize));

                    $("#toRecordNumber").html(resource.toRecordNumber);
                    $("#toRdbMemorySize").html(getPrintSize(data.toRdbMemorySize));

                    $("#toMemorySize").html(getPrintSize(data.toMemorySize));
                    var $userGroupId = $("#userGroupId");
                    listSpan(resource.userGroupId, ",", $userGroupId);
                    $("#allModal").modal("show");
                },
                error: function () {
                    $(".table-message").html("请求失败");
                }
            })
        }

        function newRelease() {
            window.location.href = "${ctx}/dataSourceDescribe"
        }

        function tableConfiguration2(num, publicType, resourceState, resourceName) {
            $.ajax({
                url: "${ctx}/resource/getPageData",
                type: "GET",
                data: {
                    pageNo: num,
                    title: resourceName,
                    publicType: publicType,
                    pageSize: 10,
                    status: resourceState
                },
                success: function (data) {
                    $("#bd-data").html("");
                    var DataList = JSON.parse(data);
                    console.log(DataList)
                    var tabCon = template("resourceTmp1", DataList);
                    $("#bd-data").append(tabCon);

                    if (DataList.resourceList.length == 0) {
                        $(".table-message").html("暂时没有数据");
                        $(".page-message").html("");
                        $(".page-list").html("");
                        return
                    }
                    $(".table-message").html("");
                    /*
                    * 创建table
                    * */
                    if ($(".page-list .bootpag").length != 0) {
                        $(".page-list").off();
                        $('.page-list').empty();
                    }
                    $(".page-message").html("当前第&nbsp;<span style='color:blue'>" + DataList.currentPage + "</span>&nbsp;页,&nbsp;共&nbsp;<span style='color:blue'>" + DataList.totalPages + "</span>&nbsp;页,&nbsp;&nbsp;共&nbsp;<span style='color:blue'>" + DataList.totalCount + "</span>&nbsp;条数据");
                    $('.page-list').bootpag({
                        total: DataList.totalPages,
                        page: DataList.currentPage,
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
                        tableConfiguration2(num, publicType, resourceState, resourceName);
                    });
                },
                error: function () {
                    console.log("请求失败")
                }
            })


        }

        /**
         *
         * @returns 当前页码 如果当前数据操作完没有数据 则返回上一页码
         */
        function currentPageNumber() {
            var currentPage = $("ul.pagination.bootpag li.active").text();
            var currentPageListSize = $("tr[keyidtr]:last td:eq(0)").text();
            if (currentPageListSize === 1) {
                currentPage = --currentPage === 0 ? 1 : currentPage;
            }
            return currentPage;
        }

        function removeData(id) {
            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>", function (r) {
                if (r) {
                    $.ajax({
                        url: "${ctx}/resource/delete/" + id,
                        type: "POST",
                        /*data:{
                            datataskId:id
                        },*/
                        success: function (data) {
                            toastr["success"]("删除成功");
                            tableConfiguration2(currentPageNumber(), publicType, resourceState, resourceName);
                        },
                        error: function () {
                            console.log("请求失败");
                        }
                    })
                } else {

                }
            })


        }

        function listSpan(arrStr, spl, ele) {
            var arrList = arrStr.split(spl);
            if (arrList.length == 1 && arrList[0] == "") {
                ele.parent().hide()
                ele.empty()
                return
            } else {
                ele.parent().show()
            }
            var arrListStr = ""
            for (var i = 0; i < arrList.length; i++) {
                arrListStr += "<span class='arrListSty'>" + arrList[i] + "</span>"
            }
            ele.empty()
            ele.append(arrListStr)
        }

        function initSelectGroup() {
            var resourceId = $.trim($("#auditId").attr("auditid"));
            var $permissions = $("#permissions");
            $permissions.select2("destroy");
            $permissions.find("option").remove();
            var selectGroup;
            $.ajax({
                async: false,
                type: "GET",
                url: "${ctx}/resource/getUserGroups",
                dataType: "JSON",
                success: function (data) {
                    var tabCon = template("groupOption", {"data": data.groupList});
                    $permissions.append("<option value='public'>公开组</option>");
                    $permissions.append(tabCon);
                    selectGroup = $permissions.select2({
                        placeholder: "请选择用户",
                        allowClear: true
                    });
                }
            });

            $.ajax({
                type: "POST",
                url: "${ctx}/resource/getResourceById",
                dataType: "JSON",
                data: {
                    resourceId: resourceId
                },
                success: function (data) {
                    var userGroupId = data.resource.userGroupId;
                    if ($.trim(userGroupId).length !== 0) {
                        selectGroup.select2().val(userGroupId.split(",")).trigger("change");
                    }
                }
            });
        }

        //    审核提示框
        $(function () {
            $(":radio").click(function () {
                if (this.checked) {
                    if ($(this).attr("id") === "radio1") {
                        // 审核通过
                        statusres = "2";
                        $("#releas_remark").hide();
                        $("#selectGroup").show();
                        $("#audit_content").val("审核通过！");
                    }
                    if ($(this).attr("id") === "radio2") {
                        // 审核不通过
                        statusres = "0";
                        $("#selectGroup").hide();
                        $("#releas_remark").show();
                        $("#audit_content").val("审核未通过！");
                    }
                }
            });

            $("#permissions").on("select2-selecting", function (e) {
                var val = e.val;
                if (val === "public") {
                    toastr["info"]("选择公开组，当前数据集将会向所有注册用户开放", "提示！");
                    $("#permissions").select2().val(['public']).trigger("change")
                } else {
                    var selected = $("#permissions").val();
                    if (selected !== null && selected.indexOf("public") !== -1) {
                        toastr["info"]("当前数据集已经向所有注册用户开放", "提示！");
                        return false;
                    }
                }
            })
        })

        function getPrintSize(size){

            //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
            if (size < 1024) {
                return size + "B";
            } else {
                size = size / 1024;
            }
            //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
            //因为还没有到达要使用另一个单位的时候
            //接下去以此类推
            if (size < 1024) {
                return size.toFixed(2) + "KB";
            } else {
                size = size / 1024;
            }
            if (size < 1024) {
                //因为如果以MB为单位的话，要保留最后1位小数，
                //因此，把此数乘以100之后再取余
                // size = size * 100;
                return size.toFixed(2)+ "MB";
            } else {
                //否则如果要以GB为单位的，先除于1024再作同样的处理
                size = size / 1024;
                return size.toFixed(2)+ "GB";
            }
        }
    </script>
</div>

</html>

