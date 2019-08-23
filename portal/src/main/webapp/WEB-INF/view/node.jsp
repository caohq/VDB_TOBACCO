<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>

<html>

<head>
    <style type="text/css">
        td {
            text-align: center;
        }


        .error-message {
            color: red;
        }
        .table-bordered>tbody>tr>td {
            white-space: nowrap;
        }
    </style>
</head>

<body>

<!--专业库筛选条件-->
<div class="alert alert-info" role="alert">
    <div class="row">
        <div class="col-md-12 form-inline">
            <label class="control-label"
                   style="color: black">节点名称:</label>
            <input type="text" id="nodeNameFilter" name="nodeNameFilter"
                   placeholder="请输入节点名称"
                   class="form-control search-text" style="width: 300px;"/>

            &nbsp;&nbsp;&nbsp;&nbsp;

            <button id="searchnodeBtn" name="searchnodeBtn" onclick="searchNode();"
                    class="btn success blue btn-sm">
                <i class="fa fa-search"></i>&nbsp;&nbsp;查&nbsp;&nbsp;询
            </button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button id="addnodeBtn" name="addnodeBtn" style="margin-left: -10px;" class="btn info green btn-sm"
                    onclick="addNode()">
                <i class="glyphicon glyphicon-plus"></i>&nbsp;&nbsp;新增节点
            </button>
        </div>
    </div>
</div>

<!--专业库列表页面-->
<div class="table-message">列表加载中......</div>
<div class="table-scrollable">
    <table class="table table-striped table-bordered table-advance table-hover">
        <thead>
        <tr id="dataList">
            <th style="display:none;">${applicationScope.menus['organization_title']}ID</th>
            <th style="width: 3%;">编号</th>
            <th style="width: 5%;">节点名称</th>
            <th style="width: 5%;">节点代码</th>
            <th style="width: 5%;">主题库代码</th>
            <th style="width: 5%;">URL地址</th>
            <th style="width: 5%;">序号</th>
            <th style="width: 10%;">操作</th>
        </tr>
        </thead>
        <tbody id="nodeList">
        </tbody>
    </table>
</div>

<!--专业库分页插件-->
<div class="row margin-top-20">
    <div class="page-message col-md-6 margin-top-10">
        当前第&nbsp;<span style="color:blue;" id="pageNum"></span>&nbsp;页,&nbsp;共&nbsp;<span style="color:blue;"
                                                                                          id="totalPages"></span>页，
        共<span style="color:blue;" id="total"></span>&nbsp;条数据
    </div>
    <div class="page-list col-md-6">
        <div id="pagination" style="float: right"></div>
    </div>
</div>
<%--节点显示列表--%>
<script type="text/html" id="nodeListTable">
    {{each list}}
    <tr>
        <td style="display:none;">{{$value.id}}</td>
        <td style="display:none;">{{$value.serialNo}}</td>
        <td style="text-align: center;">{{ $index + 1}}</td>
        <td style="text-align: center">{{$value.nodeName}}</td>
        <td style="text-align: center">{{$value.nodeCode}}</td>
        <td style="text-align: center">{{$value.subjectCode}}</td>
        <td style="text-align: center">{{$value.nodeURL}}</td>
        <td style="text-align: center">{{$value.serialNo}}</td>
        <td id="{{$value.id}}">
            <table class="0" cellspacing="0" border="0" align="center">
                <tr>
                    <td class="bianji" name="{{$value.id}}"><a href="javascript:;" onclick="updateNode(this);"><i
                            class="fa fa-pencil-square-o" aria-hidden="true"></i>修改</a></td>
                    <td width="1"></td>
                    <td class="shanchu" name="{{$value.id}}"><a href="javascript:;" onclick="deleteNode(this);"><i
                            class="fa fa-trash-o fa-fw" aria-hidden="true"></i>删除</a></td>
                </tr>
            </table>
        </td>
    </tr>
    {{/each}}
</script>

<div id="addNodeDialog" class="modal fade" tabindex="-1" aria-hidden="true" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
                <h4 id="titleForAddNodeDialog" class="modal-title">
                    新增节点</h4>
            </div>

            <!--node info input form-->
            <div class="modal-body">
                <form id="addNodeForm" class="form-horizontal" role="form" method="post"
                      enctype="multipart/form-data" accept-charset="utf-8" onfocusout="true">

                    <div class="form-group">
                        <label class="col-md-3 control-label" for="nodeNames">
                            节点名称<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入节点名称"
                                   id="nodeNames"
                                   name="nodeNames" required="required"/>
                        </div>
                    </div>

                    <!--nodeCode需要保证唯一性，为了保证唯一，需要通过后端数据库交互验证是否已经存在-->
                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            节点代码<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入节点代码"
                                   id="nodeCode"
                                   name="nodeCode" required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            节点URL<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入远程端URL" id="nodeURL"
                                   name="nodeURL" required="required"/>
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
                </form>
            </div>

            <!--buttons to submit or cancel-->
            <div class="modal-footer">
                <button id="savenodeAddBtn" class="btn btn-success" onclick="agreeAddNode();">
                    保存
                </button>
                <button id="cancelnodeAddBtn" class="btn default" data-dismiss="modal">
                    取消
                </button>
            </div>
        </div>
    </div>
</div>

<div id="updateNodeDialog" class="modal fade" tabindex="-1" aria-hidden="true" data-width="400">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <button class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
                <h4 id="titleForUpdateNodeDialog" class="modal-title">
                    修改${applicationScope.menus['organization_title']}</h4>
            </div>
            <div class="modal-body">
                <form id="updateNodeForm" class="form-horizontal" role="form" method="post">
                    <div class="form-group">
                        <label class="col-md-3 control-label" for="nodeNames" style="display:none;">
                            ${applicationScope.menus['organization_title']}id（不显示）
                        </label>
                        <div style="display:none;">
                            <input type="text" class="form-control" id="idM" name="id"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label" for="nodeNames">
                            ${applicationScope.menus['organization_title']}名称<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入${applicationScope.menus['organization_title']}名称"
                                   id="nodeNameM"
                                   name="nodeNameM" required="required"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            ${applicationScope.menus['organization_title']}代码<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   placeholder="请输入${applicationScope.menus['organization_title']}代码"
                                   id="nodeCodeM"
                                   name="nodeCode" required="required" readonly="readonly"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">
                            节点URL<span style="color: red;">*</span>
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" placeholder="请输入远程端URL" id="nodeURLM"
                                   name="nodeURL" required="required"/>
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

                </form>
            </div>
            <div class="modal-footer">
                <button id="agreeUpdatenodeBtn" class="btn btn-success" onclick="agreeUpdateNode();">
                    保存
                </button>
                <button id="cancelUpdatenodeBtn" class="btn default" data-dismiss="modal">
                    取消
                </button>
            </div>
        </div>
    </div>
</div>
</body>

<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script type="text/javascript"
            src="${ctx}/resources/bundles/bootstrap-wizard/jquery.bootstrap.wizard.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/form-validation/form-validation.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/bootstrapv3.3/js/bootstrap.js"></script>
    <script type="text/javascript">
        var nextSerialNo = 1;
        var currentPage = 1;
        var subjectCode = '${sessionScope.SubjectCode}';
        //初始化
        $(function () {

            console.log("${applicationScope.menus['organization_title']}页面初始化");
            getNode(1);

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

            jQuery.validator.addMethod("isNodeURL",function (value,element) {
                var mobile=/^(((\d{1,2})|(1\d{1,2})|(2[0-4]\d)|(25[0-5]))\.){3}((\d{1,2})|(1\d{1,2})|(2[0-4]\d)|(25[0-5]))$/;
                return this.optional(element) || (mobile.test(value));
            });

            //新建专业库对话框的验证
            var addNodeValid = {
                errorElement: "span",
                errorClass: "error-message",
                focusInvalid: false,
                rules: {
                    nodeNames: "required",
                    nodeCode: {
                        required: true,
                        remote:
                            {
                                url: "${ctx}/node/queryNodeCode",
                                type: "get",
                                data:
                                    {
                                        'nodeCode': function () {
                                            return $("#nodeCode").val();
                                        }
                                    },
                                dataType: "json"
                            }
                    },
                    nodeURL:{
                        required: true,
                        isNodeURL:true
                    },
                    serialNo: "required"
                },
                messages: {
                    nodeNames: "请输入节点名称",
                    nodeCode: {
                        required: "请输入节点代码",
                        remote: "此节点代码已经存在！"
                    },
                    nodeURL:{
                        required: "请输入节点URL",
                        isNodeURL:"请填写正确的URL地址"
                    },
                    serialNo: "请输入节点的序号"
                }
            };
            
            // 修改专业库对话框的验证
            var updateNodeValid = {
                errorElement: 'span',
                errorClass: 'error-message',
                focusInvalid: false,
                rules: {
                    nodeNameM: "required",
                    nodeCode: "required",
                    nodeURL:{
                        required: true,
                        isNodeURL:true
                    },
                    serialNo: "required"
                },
                messages: {
                    nodeNameM: "请输入节点名称",
                    nodeCode: "请输入节点代码",
                    nodeURL:{
                        required: "请输入节点URL",
                        isNodeURL:"请填写正确的URL地址"
                    },
                    serialNo: "请输入节点的序号"
                }
            };

            $("#addNodeForm").validate(addNodeValid);
            $("#updateNodeForm").validate(updateNodeValid);

            $("#imageM,#image").on("change", function (item) {
                showUploadFileAsDataURL(item);
            })
        });

        //专业库名称的模糊搜索
        function searchNode() {
            getNode(1);
        }

        //查询专业库
        function getNode(pageNum) {
            console.log("getnode请求已经发送了");
            $.ajax({
                url: "${ctx}/node/getNodeList",
                type: "get",
                data: {
                    "nodeName": $("#nodeNameFilter").val().trim(),
                    "subjectCode":subjectCode,
                    "pageNum": pageNum
                },
                dataType: "json",
                success: function (data) {
                    console.log("获得node成功！");
                    console.log("success - data = " + data);

                    var totalnode = data.totalCount;
                    if (totalnode == 0) {
                        $("#pagination").off();
                        $(".table-message").show();
                        $(".table-message").html("暂时没有数据");
                        $(".page-message").hide();
                        $(".page-list").hide();
                        $("#nodeList").hide();
                    } else {
                        $(".table-message").hide();
                        $(".page-message").show();
                        $(".page-list").show();
                        $("#nodeList").show();

                        var html = template("nodeListTable", data);
                        $("#nodeList").empty();
                        $("#nodeList").append(html);

                        $("#pageNum").html(data.currentPage);
                        currentPage = pageNum;
                        $("#totalPages").html(data.totalPages);
                        $("#total").html(data.totalCount);


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
                            getNode(num);
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

        function addNode() {
            clearAllInput();
            $.ajax({
                url: "${ctx}/node/getNextSerialNo",
                type: "get",
                data: {"subjectCode":subjectCode},
                dataType: "json",
                success: function (data) {
                    $("#serialNo").val(data);
                    $("#addNodeDialog").modal("show");
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
                }
            });
        }

        //添加节点
        function agreeAddNode() {
            if (!$("#addNodeForm").valid()) {
                return;
            }
            var formData = new FormData();
            formData.append("nodeName", $("#nodeNames").val());
            formData.append("nodeCode", $("#nodeCode").val());
            formData.append('nodeURL', $('#nodeURL').val());
            formData.append("serialNo", $("#serialNo").val());

            $.ajax({
                url: "${ctx}/node/addNode",
                type: "post",
                contentType: false,
                processData: false,
                data: formData,
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("添加成功！");
                        $("#addNodeDialog").modal("hide");
                        getNode(1); //没有搜索条件的情况下，显示第一页
                    } else if(data.result == 'exist') {
                        toastr["error"]("添加失败，主题库代码已存在！");
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
                }
            });

        }

        //更新专业库
        function updateNode(updateBtn) {
            $("#resetPassword").prop("checked", false);
            $("#adminPasswdM").prop("checked", false);
            clearAllInput();
            $.ajax({
                type: "GET",
                async: false,
                url: '${ctx}/node/queryNodeById',
                data: {id: $(updateBtn).parent().attr("name")},
                dataType: "json",
                success: function (data) {
                    $("#idM").val(data.node.id);
                    $("#nodeNameM").val(data.node.nodeName);
                    $("#nodeCodeM").val(data.node.nodeCode);
                    $("#nodeURLM").val(data.node.nodeURL);
                    $("#serialNoM").val(data.node.serialNo);

                    $("#updateNodeDialog").modal("show");
                },
                error: function (data) {
                    console.log(data);
                }
            });
        }

        function agreeUpdateNode() {
            if (!$("#updateNodeForm").valid()) {
                return;
            }

            var formData = new FormData();
            formData.append("id", $("#idM").val());
            formData.append("nodeName", $("#nodeNameM").val());
            formData.append("nodeCode", $("#nodeCodeM").val());
            formData.append("nodeURL", $("#nodeURLM").val());
            formData.append("serialNo", $("#serialNoM").val());

            console.log("agreeUpdatenode - formData = " + formData);

            $.ajax({
                url: "${ctx}/node/updateNode",
                type: "post",
                contentType: false,
                processData: false,
                data: formData,
                dataType: "json",
                success: function (data) {
                    if (data.result == 'ok') {
                        toastr["success"]("修改成功！");
                        $("#updateNodeDialog").modal("hide");
                    }
                    console.log(data);
                    getNode(1); //没有搜索条件的情况下，显示第一页
                    // location.reload();
                },
                error: function (data) {

                }
            });
        }

        //删除专业库
        function deleteNode(deleteBtn) {
            var id = $(deleteBtn).parent().attr("name");

            console.log("idOfnodeToBeDeleted = " + id);

            bootbox.confirm("<span style='font-size: 16px'>确认要删除此条记录吗?</span>",
                function (result) {
                    if (result) {
                        bootbox.confirm("<span style='font-size: 16px'>${applicationScope.menus['organization_title']}相关信息很重要，请再次确认要删除吗?</span>", function (result) {
                                if (result) {
                                    var deleteUrl = "${ctx}/node/deleteNode?id="+id ;
                                    var currentPageList = $("#nodeList>tr:last>td:eq(2)").text();
                                    if (currentPageList === "1") {
                                        currentPage = --currentPage === 0 ? 1 : currentPage;
                                    }
                                    $.ajax({
                                        url: deleteUrl,
                                        type: "get",
                                        dataType: "json",
                                        success: function (data) {
                                            console.log(data);
                                            console.log("typeof data = " + (typeof data));
                                            if (data.result.trim() == "ok") {
                                                toastr["success"]("删除成功！");
                                                getNode(currentPage);
                                            } else {
                                                toastr["error"]("删除失败！");
                                            }
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

        // 清理弹窗中所有的输入框
        function clearAllInput() {
            $("input").val("");
            $(".showImgIfExist").hide();
            $("textarea").val("");
        }
    </script>
</div>
</html>
