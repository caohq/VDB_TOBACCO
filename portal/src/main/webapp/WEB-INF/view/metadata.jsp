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
<c:set value="${pageContext.request.contextPath}" var="ctx" />

<html>

<head>
    <title>元数据管理</title>
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
        #table_List1 th,#table_List2 th{
            background-color: #64aed9;
            text-align: center;
            font-family: 'Arial Negreta', 'Arial Normal', 'Arial';
            font-weight: 700;
            font-style: normal;
            font-size: 18px;
            color: #FFFFFF;
        }
    </style>
</head>

<body>


    <div class="alert alert-info" role="alert" >
        <!--查询条件 -->
        <div class="row">
            <div class="col-md-12 form-inline">

                <label class="control-label">元数据英文名:</label>
                <input type="text" id="queryExtField" name="extField" class="form-control search-text ">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <label class="control-label">元数据中文名称:</label>
                <input type="text" id="queryExtFieldName" name="extFieldName" class="form-control search-text ">
                &nbsp;&nbsp;&nbsp;&nbsp;
                <button id="btnSearch" name="btnSearch" onclick="search();" class="btn success blue btn-sm"><i class="fa fa-search"></i>&nbsp;&nbsp;查询</button>
                &nbsp;&nbsp;
                <button id="btnAdd" name="btnAdd" onclick="" class="btn info green btn-sm"><i
                        class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增
                </button>
            </div>
        </div>

    </div>
    <div class="col-md-12" style="padding-left: 0px; padding-right: 0px;">

        <div class="table-message-group">列表加载中......</div>
        <div class="table-scrollable" style="padding-top: 0px;">

            <table class="table table-striped table-bordered table-advance table-hover">
                <thead>
                <tr id="table_List2">
                    <th style="width: 8%;">编号</th>
                    <th style="width: 20%;">
                        元数据英文名
                    </th>
                    <th style="width: 8%;">
                        字段类型
                    </th>
                    <th style="width: 15%;">元数据中文名</th>
                    <th style="width: 8%;">排序</th>
                    <th style="width: 8%;">是否必填</th>
                    <th style="width: 15%;">枚举值</th>
                    <%--<th style="width: 20%;">备注</th>--%>
                    <th style="width: 18%;">操作</th>
                </tr>
                </thead>
                <tbody id="metadataList">

                </tbody>
            </table>
        </div>

    </div>


<!--增加元数据-->
<div class="modal fade" tabindex="-1" role="dialog" id="addModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">增加元数据</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="addMetadataForm" method="post" accept-charset="utf-8" role="form"  onfocusout="true">
                    <%--中文名称--%>
                    <div class="form-group">
                        <label for="extFieldNameAdd" class="col-sm-3 control-label">元数据中文名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="extFieldNameAdd" name="extFieldName"
                                   placeholder="请输入元数据中文名称" required="required">
                        </div>
                    </div>
                    <%--英文名称--%>
                    <div class="form-group">
                        <label for="extFieldAdd" class="col-sm-3 control-label">元数据英文名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="extFieldAdd" name="extField" placeholder="请输入元数据英文名(首字母小字)"  required="required" >
                        </div>
                    </div>
                    <%--数据类型--%>
                    <div class="form-group">
                        <label for="typeAdd" class="col-sm-3 control-label">字段类型<span class="required">*
													</span></label>
                        <div class="col-sm-8">
                            <select class="form-control" style="width: 368px;height: 34px;" id="typeAdd" name="type" placeholder="请输入元数据字段类型">
                                <option  value="String">字符串(String)</option>
                                <option  value="Integer">整型数值(Integer)</option>
                                <option  value="Double">精度型数值(Double)</option>
                                <option  value="DateTime">日期时间(DateTime)</option>
                                <option  value="List">枚举值</option>
                            </select>
                        </div>
                    </div>
                    <%--数据类型为枚举值显示--%>
                    <div id="divEnumdata" class="form-group" style="display:none;">
                        <label for="enumdataAdd" class="col-sm-3 control-label">枚举项值</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="enumdataAdd" name="enumdata" placeholder="请输入枚举值,各枚举项值以英文逗号(,)隔开">
                        </div>
                    </div>
                    <%--是否必填--%>
                    <div class="form-group">
                        <label for="isMustAdd" class="col-sm-3 control-label">是否必填项<span class="required">
													</span></label>
                        <div class="col-sm-8">
                            <select class="form-control" style="width: 368px;height: 34px;" id="isMustAdd" name="isMust"
                                    placeholder="请选择元数据字段值是否必填项">
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>
                        </div>
                    </div>
                    <%--排序--%>
                    <div class="form-group">
                        <label for="sortOrderAdd" class="col-sm-3 control-label">排序<span class="required">
													</span></label>
                        <div class="col-sm-8">
                            <input  type="text" class="form-control"  id="sortOrderAdd" name="sortOrder" placeholder="请输入元数据排列顺序" />
                        </div>
                    </div>
                    <%--备注--%>
                    <div class="form-group">
                        <label for="remarkAdd" class="col-sm-3 control-label">备注<span class="required">
													</span></label>
                        <div class="col-sm-8">
                            <input  type="text" class="form-control"  id="remarkAdd" name="remark" placeholder="请输入备注信息" />
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn green" onclick="submitAddData();" ><i
                        class="glyphicon glyphicon-ok"></i>保存
                </button>
                <button type="button" data-dismiss="modal" onclick="resetData();" class="btn  btn-danger">取消</button>
            </div>
        </div>
    </div>
</div>

<!--修改元数据-->
<div class="modal fade" tabindex="-1" role="dialog" id="editModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">修改元数据</h4>
            </div>
            <div class="modal-body" style="min-height: 150px">
                <form class="form-horizontal" id="editMetadataForm" method="post" accept-charset="utf-8" role="form"  onfocusout="true">

                    <input type="hidden" id="metadataid" name="id">
                    <div class="form-group">
                        <label for="extFieldEdit" class="col-sm-3 control-label">元数据英文名<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="extFieldEdit" name="extField" placeholder="请输入元数据英文名(首字母小字)"  readonly required="required" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="typeEdit" class="col-sm-3 control-label">字段类型<span class="required">*
													</span></label>
                        <div class="col-sm-8">
                            <select class="form-control" style="width: 368px;height: 34px;" id="typeEdit" name="type" placeholder="请选择元数据字段类型">
                                <option  value="String">字符串(String)</option>
                                <option  value="Integer">整型数值(Integer)</option>
                                <option  value="Double">精度型数值(Double)</option>
                                <option  value="DateTime">日期时间(DateTime)</option>
                                <option  value="List">枚举值</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="extFieldNameEdit" class="col-sm-3 control-label">元数据中文名称<span class="required">
													*</span></label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="extFieldNameEdit" name="extFieldName" placeholder="请输入元数据中文名称"  required="required" >
                        </div>
                    </div>

                    <div id="divEditEnumdata" class="form-group" style="display:none;">
                        <label for="enumdataEdit" class="col-sm-3 control-label">枚举项值</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="enumdataEdit" name="enumdata" placeholder="请输入枚举值,各枚举项值以英文逗号(,)隔开">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="sortOrderEdit" class="col-sm-3 control-label">排序<span class="required">
													</span></label>
                        <div class="col-sm-8">
                            <input  type="text" class="form-control"  id="sortOrderEdit" name="sortOrder" placeholder="请输入元数据排列顺序" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="isMustEdit" class="col-sm-3 control-label">是否必填项<span class="required">
													</span></label>
                        <div class="col-sm-8">
                            <select class="form-control" style="width: 368px;height: 34px;" id="isMustEdit" name="isMust" placeholder="请选择元数据字段值是否必填项">
                                <option  value="0">否</option>
                                <option  value="1">是</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="remarkEdit" class="col-sm-3 control-label">备注<span>
													</span></label>
                        <div class="col-sm-8">
                            <input  type="text" class="form-control"  id="remarkEdit" name="remark" placeholder="请输入备注信息" />
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn green" onclick="submitEditData();" ><i
                        class="glyphicon glyphicon-ok"></i>保存
                </button>
                <button type="button" data-dismiss="modal" class="btn  btn-danger">取消</button>
            </div>
        </div>
    </div>
</div>


<!-- 元数据 -->
<script type="text/html" id="metadataTmpl">
    {{each list}}
    <tr>
        <td style="display:table-cell; vertical-align:middle ; text-align: center;" >{{$index+1}}</td>
        <td style="display:table-cell; vertical-align:middle;text-align: center;"> {{$value.extField}}
        </td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{$value.type}}</td>

        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{$value.extFieldName}}</td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">{{$value.sortOrder}}</td>
        <td style="display:table-cell; vertical-align:middle ;text-align: center;">
            {{if $value.isMust == 1}}是
            {{else if $value.isMust === 0}}否
            {{/if}}
        </td>
        <td style="display:table-cell; vertical-align:middle ;text-align: left;">{{$value.enumdata}}</td>
        <%--<td style="display:table-cell; vertical-align:middle ;text-align: left;">{{$value.remark}}</td>
--%>
        <td style="display:table-cell; vertical-align:middle;text-align: center;" id="{{$value.id}}" >
            <button class="btn default btn-xs purple updateButton" onclick="editData('{{$value.id}}')"><i class="fa fa-edit"></i>修改</button>&nbsp;
            <button class="btn default btn-xs red" onclick="deleteData('{{$value.id}}')"><i class="fa fa-trash"></i>删除</button>&nbsp;
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
        var ctx = '${ctx}';
        //var groupUsersSelect2;
        var validAddData;
        var validEditData;

        $(function () {
            getData();
            $(".search-text").keydown(function (event) {
                if (event.keyCode == 13) {
                    getData();
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

            // 只能输入英文
            jQuery.validator.addMethod("english", function(value, element) {
                var chrnum = /^([a-zA-Z]+)$/;
                return this.optional(element) || (chrnum.test(value));
            }, "只能输入字母");

            validAddData = {
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "", // validate all fields including form hidden input
                rules: {
                    extField: {
                        required: true,
                        english:true,
                        remote:
                            {
                                url:  ctx + "/admin/metadata/isExist",
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
                    type: {
                        required: true
                    },
                    extFieldName: {
                        required: true
                    },
                    sortOrder:{
                        digits:true
                    }

                },
                messages: {
                    extField: {
                        required: "请输入元数据英文名称",
                        english:"请输入英文字母",
                        remote: "此元数据英文名称己存在"
                    },
                    type: {
                        required: "请选择元数据英文名字段类型"
                    },
                    extFieldName: {
                        required: "请输入元数据中文名称"
                    },
                    sortOrder:{
                        digits:"只能输入整数"
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
                    type: {
                        required: true
                    },
                    extFieldName: {
                        required: true
                    },
                    sortOrder:{
                        digits:true
                    }
                },
                messages: {
                    type: {
                        required: "请选择元数据英文名字段类型"
                    },
                    extFieldName: {
                        required: "请输入元数据中文名称"
                    },
                    sortOrder:{
                        digits:"只能输入整数"
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

            $("#addMetadataForm").validate(validAddData);
            $("#editMetadataForm").validate(validEditData);

            /*groupUsersSelect2 = $('#users').select2({
                placeholder: "请选择用户",
                allowClear: true
            });*/

        });


        function search() {
            getData();
        }



        function getData() {
            $.ajax({
                url: "${ctx}/admin/metadata/getList",
                type: "get",
                dataType: "json",
                data: {
                    "extField":$.trim($("#queryExtField").val()),
                    "extFieldName":$.trim($("#queryExtFieldName").val())
                },
                success: function (data) {
                    console.log(data);
                    var html = template("metadataTmpl", data);
                    $(".table-message-group").html("");
                    $("#metadataList").empty();
                    $("#metadataList").append(html);
                    if(data.list.length ==0){
                        $(".table-message-group").html("暂时没有数据");
                    }
                }
            });
        }

        function deleteData(id) {
            bootbox.confirm("<span style='font-size:16px;'>确定要删除此条记录吗？</span>", function (r) {
                if (r) {
                    $.ajax({
                        url: ctx + "/admin/metadata/delete/" + id,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.result == 'ok') {
                                toastr["success"]("删除成功！", "数据删除");
                                getData();
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

        $("#btnAdd").click(function () {
            $("#extFieldAdd").val("");
            $("#extFieldNameAdd").val("");
            $("#typeAdd").val("String");
            $("#typeAdd").change();
            $("#isMustAdd").val(0);

            $.ajax({
                type: "get",
                url: '${ctx}/admin/metadata/getMaxOrder',
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    $("#sortOrderAdd").val(data.data);
                }
            });
            resetData();
            $("#addModal").modal('show');
        });

        $("#typeAdd").change(function () {
            if ($("#typeAdd").val() == 'List'){
                $("#divEnumdata").show();
                $("#enumdataAdd").attr("required","required");
            }
            else{
                $("#divEnumdata").hide();
                $("#enumdataAdd").removeAttrs("required");
            }
        });

        $("#typeEdit").change(function () {
            if ($("#typeEdit").val() == 'List'){
                $("#divEditEnumdata").show();
                $("#enumdataEdit").attr("required","required");
            }
            else{
                $("#divEditEnumdata").hide();
                $("#enumdataEdit").removeAttrs("required");
            }
        });

        function resetData() {
            $("#addMetadataForm").validate().resetForm();
            $("#addMetadataForm").validate().clean();
            $('.form-group').removeClass('has-error');
        }

        function submitAddData() {
            if (!$("#addMetadataForm").valid()) {
                return;
            }
            $.ajax({
                type: "POST",
                url: '${ctx}/admin/metadata/add',
                data: $("#addMetadataForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("添加成功！", "添加元数据");
                        $("#addModal").modal("hide");
                        getData();
                    } else {
                        toastr["error"]("添加失败！", "添加元数据");
                    }
                }
            });
        }

        <!--编辑元数据-->
        function editData(id) {
            resetData();
            $.ajax({
                type: "GET",
                url: '${ctx}/admin/metadata/info',
                data: {"id": id},
                dataType: "json",
                success: function (data) {
                    $("#editModal").modal("show");
                    $("#metadataid").val(data.metadata.id);
                    $("#extFieldEdit").val(data.metadata.extField);
                    $("#extFieldNameEdit").val(data.metadata.extFieldName);
                    $("#extFieldNameEdit").blur();
                    $("#typeEdit").val(data.metadata.type);
                    $("#typeEdit").change();
                    $("#enumdataEdit").val(data.metadata.enumdata);
                    $("#enumdataEdit").blur();
                    $("#sortOrderEdit").val(data.metadata.sortOrder);
                    $("#remarkEdit").val(data.metadata.remark);
                }
            });
        }




        function submitEditData() {
            if (!$("#editMetadataForm").valid()) {
                return;
            }
            $.ajax({
                type: "POST",
                url: '${ctx}/admin/metadata/update',
                data: $("#editMetadataForm").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("编辑成功！", "元数据编辑");
                        $("#editModal").modal("hide");
                        getData();
                    } else {
                        toastr["error"]("编辑失败！", "元数据编辑");
                    }
                }
            });
        }

    </script>

</div>

</html>
