<%--
  User: jinbao
  CreateTate: 2019/3/21  9:54
  通过excel创建表以及导入数据
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"/>
<html>
<head>
    <title>Title</title>

    <style>

        .inputfile {
            width: 0.1px;
            height: 0.1px;
            opacity: 0;
            overflow: hidden;
            position: absolute;
            z-index: -1;
        }

        .qiehuan_div li.active a {
            background: #2a6ebf !important;
            margin-right: 1px !important;
            color: #fff !important;
        }

        .preview {
            height: auto;
            width: auto;
            border: none;
            margin-bottom: 0px;
        }

        #selectTable select {
            width: 60%;
        }
    </style>
</head>
<body>

<%--弹窗页定义 开始--%>
<div id="staticSourceTableChoiceModal" class="modal fade" tabindex="-1" data-width="200">
    <div class="modal-dialog" style="min-width:600px;width:auto;max-width: 55%">
        <div class="modal-content">
            <div class="modal-header bg-primary">
                <h4 class="modal-title" id="relationalDatabaseModalTitle"
                    style="color: white;font-size: 18px;font-weight: 500;">预览数据</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="portlet box" style="border:0;">
                            <div class="portlet-title" style="display: none">
                            </div>
                            <div class="tab-content"
                            >
                                <div id="previewTableDataAndComsId" style="max-height: 400px;">
                                    <div class="skin skin-minimal">
                                        <table class="table table-hover table-bordered">

                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="editTableFieldComsSaveId" data-dismiss="modal" class="btn red">关闭
                </button>
            </div>
        </div>
    </div>
</div>

<div id="synchronizeConfigurationAndLog" class="modal fade" tabindex="-1" data-width="200">
    <div class="modal-dialog" style="width:600px;width:auto;max-width: 50%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        id="editTableFieldComsCloseId11"></button>
                <h4 class="modal-title" id="title_id1">同步设置与记录</h4>

            </div>

            <div class="modal-body" style="overflow:auto;max-height: 500px;">

                <div class="tab-content"
                     style="background-color: white;max-height:50%;padding-top: -10px ;">
                    <div style="margin-left: 1%;margin-right: 1%;width:98%;">
                        <table class="table table-bordered data-table" border="0">
                            <thead>
                            <tr class='table_tr'>
                                <th>表名</th>
                                <th>频率</th>
                                <th>日志</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>

                    </div>
                </div>
            </div>

            <div id="update_div" class="modal-footer">
                <%--<button id="updatebtn"  style="width: 80px;height: 30px; border: 1px solid #cad9ea;">保存</button>--%>
            </div>
        </div>
    </div>
</div>
<%--弹窗页定义 结束--%>


<%--正文开始--%>
<div class="qiehuan_div">
    <ul class="nav nav-tabs activeTabs" role="tablist">
    </ul>
</div>


<div class="tab-content" style="background-color: white;">
    <%--导入式建表--%>
    <div class="tab-pane active" id="uploadExcel" style="height: 50%">
        <form name="form" id="fileForm" method="post" style="text-align: center;">
            <div style="padding-top: 5%;"></div>
            <a href="${ctx}/fileImport/getExcelTemplate" style="font-size: 19px;">点击下载Excel模板</a><br/>
            <div style="margin: 25px;"></div>
            <input type="file" name="file" id="excelFile" class="inputfile"/>
            <label id="fileLabel" for="excelFile" class="btn btn-default">请选择上传文件</label>
            <input id="excelFileUpload" type="button" class="btn btn-default" onclick="uploadExcel();" value="上传"/>
            <input id="resetFile" type="button" class="btn btn-default" onclick="initExcelCreateTable()" value="重置"/>
        </form>

    </div>

    <%--关联创建表--%>
    <div class="tab-pane" id="sql">
        <div style="width: 100%;padding: 20px 20px;">
            <p></p>
            <h4><input type="radio" name="radioType" id="tableRadio" checked><a
                    style="color: blue;font-weight: bolder;">方式1：通过联合表A和表B，形成新表</a></h4>
            <div style="margin-left: 3%;">
                <form id="selectTable">
                    <div class="row" id="tableA">
                        <div class="col-md-1 text-right"> 关联表A</div>
                        <div class="col-md-4">
                            <select id="selectTableA" class="selectTable">
                            </select>
                        </div>
                        <div class="col-md-1 text-right">表A关联字段</div>
                        <div class="col-md-4">
                            <select id="selectTableFieldA" class="">

                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-1 text-right">表A字段</div>
                    </div>
                    <div id="checkBoxTableFieldA" class="checkBoxTableField"></div>

                    <div class="row" style="height: 20px"></div>

                    <div class="row" id="tableB">
                        <div class="col-md-1 text-right"> 关联表B</div>
                        <div class="col-md-4">
                            <select id="selectTableB" class="selectTable">

                            </select>
                        </div>
                        <div class="col-md-1 text-right">表B关联字段</div>
                        <div class="col-md-4">
                            <select id="selectTableFieldB" class="">

                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-1 text-right">表B字段</div>
                    </div>
                    <div id="checkBoxTableFieldB" class="checkBoxTableField">
                    </div>
                </form>
            </div>
            <div style="width: 100%;height: 30px"></div>
            <h4><input type="radio" name="radioType" id="sqlRadio"><a style="color: blue;font-weight: bolder;">方式2：通过SQL语句，形成新表</a>
            </h4>
            <div style="margin-left: 3%;display:none">
                <form id="selectSQL">
                    <div id="totalList">
                        <div class="row">
                            <div class="col-md-1">SQL查询:</div>
                            <div class="col-md-7">
                                <input type="text" class="form-control sqlStatements inputVili" name="newSql">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div style="width: 100%;height: 30px"></div>
            <h4 style="font-weight: bold">新的表名称</h4>
            <div style="margin-left: 3%;">
                <div class="row">
                    <div class="col-md-1 text-right"><strong>新表名</strong></div>
                    <div class="col-md-3">
                        <input type="text" class="form-control inputVili" placeholder="新表名" name="newName">
                    </div>
                    <div class="col-md-1"></div>
                    <div class="col-md-2 text-right">
                        <input type="checkbox" id="synchronizeTable"/>
                        <label for="synchronizeTable">同步更新表</label>
                    </div>
                    <div class="col-md-2">
                        <select id="period" style="display: none;">
                            <option value="">请选择同步周期</option>
                            <option value="HALF_OF_THE_DAY">12小时</option>
                            <option value="DAY">天</option>
                            <option value="WEEK">周</option>
                            <option value="MONTH">月</option>
                            <option value="YEAR">年</option>
                        </select>
                    </div>
                    <div class="col-md-2 text-left">
                        <a onclick="showSynchronizeDialog()"><strong>同步设置与记录</strong></a>
                    </div>
                </div>
                <div style="height:20px"></div>
                <div class="row">
                    <div class="col-md-4 text-center">
                        <button type="button" class="btn blue preview" onclick="previewSqlDataAndComs()">预览
                        </button>
                    </div>
                    <div class="col-md-4 text-center">
                        <button type="button" class="btn green" onclick="createTableBySql()"><span
                                class="glyphicon glyphicon-plus"></span>创建表
                        </button>
                    </div>
                    <div class="col-md-2" style="text-align: left;width: 20px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--解析excel根据sheet页生成表格--%>
<div id="excelTableList">
    <div class="tap_div">
        <div class="tab-content activeTabs" style="width:100%;">
            <table spellcheck="0" class="table table-hover biaoge" cellspacing="0" border="0">
            </table>
        </div>

    </div>
</div>
<%--正文结束--%>
</div>
</body>

<%--js 开始--%>
<div id="siteMeshJavaScript">
    <script type="text/javascript"
            src="${ctx}/resources/bundles/bootstrap-closable-tab/bootstrap-closable-tab.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/layerJs/layer.js"></script>
    <script>

        var sub = '${sessionScope.SubjectCode}';
        var excelFileName;
        var tagParent = $("div.qiehuan_div ul");
        var li_excel = '<li class="active" value="0"><a id="createTableByExcel" href="#uploadExcel" data-toggle="tab">导入式建表</a></li>';
        var li_sql = '<li class="" value="1"><a id="createTableBySQL" href="#sql" data-toggle="tab">关联创建新表</a></li>';
        (function () {
            // 默认excel导入式建表
            initExcelCreateTable();
            closableTab.afterCloseTab = function (item) {
                if (!$(".nav.nav-tabs.activeTabs li")[0]) {
                    $("#uploadExcel").addClass("active");
                    initExcelCreateTable();
                }
            };

            // 上传文件按钮
            var $inputs = $("#excelFile");
            [].forEach.call($inputs, function (e) {
                var label = $inputs.next();
                $inputs.on("change", function (e) {
                    excelFileName = e.target.value.split("\\").pop();
                    label.text(excelFileName);
                    resetButton(true);
                })
            });

            // 清除建表过程中的选中主键标识
            $("body").on("click", "#clearPK", function () {
                $("[name='isPK']").prop("checked", false);
            });

            // 初始化通过已有表创建新表
            initSqlCreateTable();

            $("input[name='radioType']").click(function () {
                $(this).next().trigger("click");
            });

            $("#synchronizeTable").on("click", function () {
                var $_this = $(this);
                var $period = $("#period");
                var checked = $_this.is(":checked");
                if (checked) {
                    $period.show();
                } else {
                    $period.hide();
                }
            });

            // 表同步弹窗
            $(document).on("focus", ".synchronizeTable", function () {
                var $_this = $(event.target);
                $_this.data("last", $_this.val());
            });
            $(document).on("change", ".synchronizeTable", function () {
                var $_this = $(event.target);
                var val = $_this.val();
                bootbox.confirm("当前操作会变更表的同步频率确定要修改么？", function (r) {
                    if (r) {
                        var id = $_this.attr("id");
                        $.ajax({
                            type: "POST",
                            url: "${ctx}/fileImport/updateSynchronizeTable",
                            dataType: "JSON",
                            data: {
                                "synchronizeId": id,
                                "frequency": val
                            },
                            success: function (data) {
                            }
                        })
                    } else {
                        $_this.val($_this.data("last"));
                    }
                });
            })
        })();

        function initExcelCreateTable() {
            resetFile();
            tagParent.html("");
            tagParent.append(li_excel);
            tagParent.append(li_sql);
            $("#excelTableList").hide();
        }

        function resetFile() {
            resetButton(false);
            $("#excelFile").val("");
            $("#fileLabel").text("请选择上传文件");
        }

        function resetButton(isSelectFile) {
            if (isSelectFile) {
                $("#excelFileUpload").show();
                $("#resetFile").show();
            } else {
                $("#excelFileUpload").hide();
                $("#resetFile").hide();
            }
        }

        function showExcelTables() {
            $("#uploadExcel").removeClass("active");
            $("#excelTableList").show();
        }

        function uploadExcel() {
            var formData = new FormData($("#fileForm")[0]);
            var fileName = excelFileName;

            if (fileName === undefined) {
                toastr["warning"]("提示！", "请选择文件");
                return;
            }
            var allFileType = ".xlsx|";
            var s = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            if (allFileType.indexOf(s + "|") === -1) {
                toastr["warning"]("提示！", "请选择上传Excel2007及以上版本文件");
                return;
            }
            formData.append("subjectCode", sub);
            var filePath = $("#excelFile").val();
            formData.append("filePath", filePath);
            $.ajax({
                url: '${ctx}/fileImport/excel',
                type: 'post',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                beforeSend: function () {
                    index = layer.load(1, {
                        shade: [0.5, '#fff'] //0.1透明度的白色背景
                    });
                },
                success: function (result) {
                    var resultJson = JSON.parse(result);
                    if (resultJson["code"] === "error") {
                        toastr["error"]("错误！", resultJson["message"]);
                    } else {
                        var data = resultJson.data;
                        tagParent.html("");
                        $.each(data, function (index, value) {

                            $.each(value, function (sheetName, sheetData) {
                                var id = sheetName;
                                var name = sheetName;
                                var closable = "true";
                                var html = "";
                                var tableStatus = sheetData[0][0];
                                if (tableStatus === "isExist") {
                                    html = template("insertData", {"data": sheetData, "tableName": sheetName});
                                } else {

                                    html = template("createTable", {"data": sheetData, "tableName": sheetName});
                                }
                                var item = {'id': id, 'name': name, 'closable': closable, 'template': html};
                                // 执行创建页签
                                closableTab.addTab(item);
                            })


                        });
                        showExcelTables();
                    }
                },
                complete: function () {
                    $("#layui-layer-shade" + index + "").remove();
                    $("#layui-layer" + index + "").remove();
                },
                error: function (returndata) {
                    toastr["error"]("错误！", returndata);
                }
            });
        }

        /**
         * 创建表并保存数据 || 仅保存数据
         */
        function createTableAndInsertValue(_this) {
            var tableName = $(_this).attr("tableName");
            var table = $("div[tablename=" + tableName + "] table")[0];
            var tableData = parseTableData2Json(table);
            var size = tableData.data.length;
            if (size) {
                var data = new FormData($("#fileForm")[0]);
                var s = JSON.stringify(tableData.data);
                data.append("tableData", s);
                data.append("subjectCode", sub);
                data.append("tableName", tableName);

                var requestUrl = "";
                if (tableData.type === "insert") {
                    requestUrl = "${ctx}/fileImport/onlyInsertValue";
                } else {
                    requestUrl = "${ctx}/fileImport/createTableAndInsertValue";
                }
                $.ajax({
                    type: "POST",
                    url: requestUrl,
                    dataType: "JSON",
                    data: data,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    beforeSend: function () {
                        index = layer.load(1, {
                            shade: [0.5, '#fff'] //0.1透明度的白色背景
                        });
                    },
                    success: function (data) {
                        var jsonData = data;
                        if (jsonData.code === "error") {
                            toastr["error"]("错误！", jsonData.message);
                        } else {
                            toastr["success"]("提示！", jsonData.message);
                            var $saveExcelSuccess = $(_this);
                            $saveExcelSuccess.text("保存成功");
                            $saveExcelSuccess.attr('disabled', 'disabled');

                        }
                    },
                    complete: function () {
                        $("#layui-layer-shade" + index + "").remove();
                        $("#layui-layer" + index + "").remove();
                    }
                });
            }
        }

        function parseTableData2Json(table) {
            var result = {};
            var rows = table.rows;
            var rowLength = rows.length;
            var trl = [];
            for (var i = 1; i < rowLength; i++) {
                var cells = rows[i].cells;
                var cellLength = cells.length;
                /*
                * 根据cellLength 区分
                * 6:新增表 并新增数据
                * 5：比对数据库已存在表字段 与excel新导入字段 导入新增数据
                * */
                if (cellLength === 6) {
                    var b = serializeTableForCreate(cellLength, cells, trl, i);
                    if (b === false) {
                        return b;
                    }
                    result["type"] = "createAndInsert";
                }
                if (cellLength === 5) {
                    serializeTableForInsert(cellLength, cells, trl, i);
                    result["type"] = "insert";
                }
            }
            result["data"] = trl;
            return result;
        }

        function serializeTableForCreate(cellLength, cells, trl, i) {
            var tdl = {};
            for (var j = 0; j < cellLength; j++) {
                if (j === 1) {
                    var fieldName = $.trim($(cells[j]).find("input").val());
                    if (fieldName === "") {
                        toastr["error"]("错误！", "第" + i + "行 请输入字段名称！");
                        return false;
                    }
                    tdl["field"] = fieldName;
                } else if (j === 2) {
                    var fieldComment = $.trim($(cells[j]).find("input").val());
                    if (fieldComment === "") {
                        toastr["error"]("错误！", "第" + i + "行 请输入字段注释！");
                        return false;
                    }
                    tdl["comment"] = fieldComment;
                } else if (j === 3) {
                    var fieldType = $(cells[j]).find("select :selected").val();
                    if (fieldType === "0") {
                        toastr["error"]("错误！", "第" + i + "行 请选择字段类型！");
                        return false;
                    }
                    tdl["type"] = fieldType;
                } else if (j === 4) {
                    var fieldLength = $.trim($(cells[j]).find("input").val());
                    var reg = /^[1-9]\d*$/;
                    if (!reg.test(fieldLength)) {
                        toastr["error"]("错误！", "第" + i + "行 字段长度有误！");
                        return false;
                    }
                    tdl["length"] = fieldLength;
                } else if (j === 5) {
                    var length = $(cells[j]).find("input:checked").length;
                    tdl["pk"] = length;
                }
            }
            trl.push(tdl);
        }

        function serializeTableForInsert(cellLength, cells, trl, i) {
            var tdl = {};
            for (var j = 0; j < cellLength; j++) {
                if (j === 0) {
                    tdl["oldField"] = $.trim($(cells[j]).text());
                }
                if (j === 2) {
                    tdl["field"] = $.trim($(cells[j]).find("select :selected").val());
                    var num = $(cells[j]).find(":checked").val();
                    tdl["insertNum"] = num === -1 ? -1 : num - 1;
                }
            }
            trl.push(tdl);
        }

        function saveValueAfterDeleteTable() {
            var tableName = $.trim($("div.qiehuan_div ul li.active a").text());
            bootbox.confirm("<span style='font-size: 16px'>确认要删除 “" + tableName + "” 数据表么</span>",
                function (result) {
                    if (result) {
                        $.ajax({
                            url: "${ctx}/fileImport/deleteTable",
                            dataType: "JSON",
                            type: "POST",
                            data: {
                                "tableName": tableName,
                                "subjectCode": sub

                            },
                            success: function (data) {
                                var parse = data;
                                if (parse.code === "success") {
                                    $("div.qiehuan_div li.active i").trigger("onclick");
                                    toastr["info"]("提示！", parse.message);
                                }
                            }
                        })
                    }
                }
            );
        }

        function initSqlCreateTable() {

            $("#selectTable select.selectTable").on("change", function (event) {
                var _this = $(event.currentTarget);
                var tableName = _this.val();
                getTableField(_this, tableName);
            });

            $("#createTableBySQL").on("click", function () {
                resetSelectAndCheckBox();
                getTableName();
            });

            $("h4 a").click(function () {
                $(this).parent().find("input").prop("checked", true);
                var $div = $(this).parent().next("div");
                var otherDiv = $("h4 a").parent().next("div").not($div);
                otherDiv.hide();
                $div.show();
            })
        }

        /**
         * @return {string}
         */
        function selectToSql() {
            var sqlString = "";
            var tableAName = $.trim($("#selectTableA").val());
            if (tableAName === "选择表") {
                toastr["warning"]("请选择表A", "警告");
                return sqlString;
            }

            var relationA = $.trim($("#selectTableFieldA").val());
            if (relationA === "") {
                toastr["warning"]("请选择表A的关联字段", "警告");
                return sqlString;
            }
            relationA = "a." + relationA;

            var fieldsA = "";
            $.each($("#checkBoxTableFieldA").find("input:checked"), function (k, v) {
                var val = $.trim($(v).attr("id"));
                fieldsA += val.replace("A", "a.") + ",";
            });
            if (fieldsA === "") {
                toastr["warning"]("请选择表A的查询字段", "警告");
                return sqlString;
            }
            fieldsA = fieldsA.slice(0, fieldsA.length - 1);

            var tableBName = $.trim($("#selectTableB").val());
            if (tableBName === "选择表") {
                toastr["warning"]("请选择表B", "警告");
                return sqlString;
            }

            var relationB = $.trim($("#selectTableFieldB").val());
            if (relationB === "") {
                toastr["warning"]("请选择表B的关联字段", "警告");
                return sqlString;
            }
            relationB = "b." + relationB;

            var fieldsB = "";
            $.each($("#checkBoxTableFieldB").find("input:checked"), function (k, v) {
                var val = $.trim($(v).attr("id"));
                fieldsB += val.replace("B", "b.") + ",";
            });
            if (fieldsB === "") {
                toastr["warning"]("请选择表B的查询字段", "警告");
                return sqlString;
            }
            fieldsB = fieldsB.slice(0, fieldsB.length - 1);
            sqlString = "SELECT " + fieldsA + "," + fieldsB + " FROM " + tableAName + " AS a LEFT JOIN " + tableBName + " AS b ON " + relationA + " = " + relationB;
            return sqlString;
        }

        function inputToSql() {
            var inputSql = "";
            inputSql = $.trim($("input[name='newSql']").val());
            if (inputSql.length === 0) {
                toastr["warning"]("请输入SQL语句", "警告！");
            }
            return inputSql;
        }

        function previewSqlDataAndComs() {
            var $title = $("#relationalDatabaseModalTitle");
            var sqlString, isTable, isSQL;
            isTable = $("#selectTable").is(":visible");
            isSQL = $("#selectSQL").is(":visible");
            if (isTable) {
                sqlString = selectToSql();
                if (sqlString.length === 0) {
                    return;
                }
                $title.html("预览数据:<span style='color:red'>通过联合关联</span>");
            } else if (isSQL) {
                sqlString = inputToSql();
                if (sqlString.length === 0) {
                    return;
                }
                $title.html("预览数据:<span style='color:red'>通过sql查询</span>");
            } else {
                return;
            }

            var sql = validateSql(sqlString);

            if (sql.flag !== "true") {
                toastr["error"](sql.flag, "错误！");
                return;
            }
            previewSqlDataAndComs_real(sqlString);
        }

        function previewSqlDataAndComs_real(sqlString) {
            $.ajax({
                type: "POST",
                url: "${ctx}/fileImport/previewSqlData",
                dataType: "JSON",
                data: {
                    sqlString: sqlString
                },
                success: function (data) {
                    $("#staticSourceTableChoiceModal").modal("show");
                    var html = template("previewSqlData", {
                        "data": data.data,
                        "columnName": data.columnName
                    });
                    var $table = $("#previewTableDataAndComsId").find("table");
                    $table.html("");
                    $table.html(html);
                    $table.nextAll().remove();
                    if (data.range === "out") {
                        $table.after("<p style='color: red'>查询结果大于10条，仅展示前10条</p>")
                    }
                }
            })
        }

        function createTableBySql() {
            var sqlString, isTable, isSQL;
            isTable = $("#selectTable").is(":visible");
            isSQL = $("#selectSQL").is(":visible");
            if (isTable) {
                sqlString = selectToSql();
                if (sqlString.length === 0) {
                    return;
                }
            } else if (isSQL) {
                sqlString = inputToSql();
                if (sqlString.length === 0) {
                    return;
                }
            } else {
                return;
            }

            var sql = validateSql(sqlString);
            if (sql.flag !== "true") {
                toastr["error"](sql.flag, "错误！");
                return;
            }

            var newName = $.trim($($("input[name='newName']")).val());
            var tableName = validateTableName(newName);
            if (tableName.flag !== "true") {
                toastr["error"](tableName.flag, "错误！");
                return;
            }
            createTableBySql_real(sql.newSql, tableName.newName)
        }

        function createTableBySql_real(newSql, newName) {
            $.ajax({
                type: "POST",
                url: "${ctx}/fileImport/createTableBySql",
                dataType: "JSON",
                data: {
                    newSql: newSql,
                    newName: newName,
                    period: $.trim($("#period").val())
                },
                success: function (data) {
                    console.log(data);
                    toastr["info"](data.message, "成功！");
                }
            })
        }

        function validateSql(sqlString) {
            var result = {};

            result["newSql"] = sqlString;
            $.ajax({
                async: false,
                url: "${ctx}/fileImport/validateSqlString",
                type: "POST",
                dataType: "JSON",
                data: {
                    newSql: sqlString
                },
                success: function (data) {
                    result["flag"] = data;
                }
            });
            return result;
        }

        function validateTableName(newName) {
            var result = {};

            if (newName.length === 0) {
                result["flag"] = "请输入新的表名";
                return result;
            }
            result["newName"] = newName;
            $.ajax({
                async: false,
                type: "POST",
                url: "${ctx}/fileImport/validateTableName",
                dataType: "JSON",
                data: {
                    newName: newName
                },
                success: function (data) {
                    result["flag"] = data;
                }
            });
            return result;
        }

        function resetSelectAndCheckBox() {
            $("select:not('#period')").html("");
            $("#checkBoxTableFieldA").html("");
            $("#checkBoxTableFieldB").html("");
        }

        function getTableName() {
            var tableNameList = [];
            $.ajax({
                async: false,
                type: "POST",
                url: "${ctx}/relationalDatabaseTableList",
                dataType: "JSON",
                data: {
                    subjectCode: sub
                },
                success: function (data) {
                    tableNameList = tableNameList.concat(data.list)
                }
            });
            var sort = tableNameList.sort();
            sort.splice(0, 0, "选择表");
            var html = template("tableNameOption", {"data": sort});
            $(".selectTable").html("");
            $(".selectTable").html(html);
        }

        function getTableField(_this, tableName) {
            $.ajax({
                type: "POST",
                url: "${ctx}/getTableFieldComs",
                dataType: "JSON",
                data: {
                    subjectCode: sub,
                    tableName: tableName
                },
                success: function (data) {
                    var field = [];
                    $.each(data.tableInfos, function (k, v) {
                        if (v["columnName"] !== "PORTALID") {
                            field.push(v["columnName"]);
                        }
                    });
                    var fieldOption = template("tableFieldOption", {"data": field});
                    _this.parent().parent().find("select:eq(1)").html("");
                    _this.parent().parent().find("select:eq(1)").html(fieldOption);

                    var result = [];
                    for (var i = 0; i < field.length; i += 4) {
                        result.push(field.slice(i, i + 4))
                    }
                    var _thisID = _this.attr("id");
                    var owner = _thisID.slice(_thisID.length - 1, _thisID.length);
                    var fieldCheckBox = template("tableFieldCheckBox", {"data": result, "owner": owner});
                    _this.parent().parent().nextAll(".checkBoxTableField:eq(0)").html("");
                    _this.parent().parent().nextAll(".checkBoxTableField:eq(0)").html(fieldCheckBox);
                }
            })
        }

        // 同步设置与记录
        function showSynchronizeDialog() {
            $("#synchronizeConfigurationAndLog").modal("show");
            var $tbody = $("#synchronizeConfigurationAndLog tbody");
            $.ajax({
                type: "POST",
                url: "${ctx}/fileImport/selectSynchronizeInfo",
                dataType: "JSON",
                data: {},
                success: function (data) {
                    var html = template("synchronizeConfiguration", {"list": data.list});
                    debugger;
                    $tbody.html("");
                    $tbody.html(html);
                    $.each(data.select, function (key, value) {
                        $("#" + key).val(value);
                    })
                }
            })
        }

    </script>
</div>
<%--js 结束--%>

<%--模板定义 开始--%>
<div id="artTemplate">
    <script type="text/html" id="createTable">
        <div tableName="{{tableName}}" class="tab-pane">
            <table class="table table-hover table-bordered">
                <thead>
                <tr style="word-break: keep-all">
                    <th>序号</th>
                    <th>excel字段名称</th>
                    <th>excel字段注释</th>
                    <th>字段类型</th>
                    <th>字段长度</th>
                    <th class="text-center">
                        设置主键
                        <button class="btn btn-default" id="clearPK" type="button">清除设置</button>
                    </th>
                </tr>
                </thead>
                <tbody>
                {{each data as v i}}
                {{if i > 0}}
                <tr>
                    <td>{{i}}</td>

                    {{each v as vv ii}}
                    {{if ii > 2}}
                    <td>
                        <input name={{vv}} value={{vv}}>
                    </td>
                    {{/if}}
                    {{/each}}

                    <td>
                        <select fieldType={{v[0]}}>
                            <option selected value="0">请选择字段类型</option>
                            <option value="varchar">varchar</option>
                            <option value="int">int</option>
                            <option value="text">text</option>
                        </select>
                    </td>

                    <td>
                        <input fieldLength={{v[0]}} placeholder="请输入字段长度"/>
                    </td>
                    <td class="text-center"><input fieldPk="{{v[0]}}" name="isPK" type="radio"/></td>
                </tr>
                {{/if}}
                {{/each}}
                </tbody>
            </table>
        </div>
        <button type="button" onclick="createTableAndInsertValue(this)" data-dismiss="modal"
                class="saveExcelSuccess btn bule" tablename="{{tableName}}">创建表并上传数据
        </button>
    </script>

    <script type="text/html" id="insertData">
        <div class="tab-pane" tableName="{{tableName}}">
            <table class="table table-hover table-bordered">
                <thead>
                <tr style="word-break: keep-all">
                    <th>表中字段名称</th>
                    <th>表中字段注释</th>
                    <th>excel字段名称</th>
                    <th>excel字段注释</th>
                    <th class="text-center">是否主键</th>
                </tr>
                </thead>
                <tbody>
                {{each data as v i}}
                {{if i > 0 && v[0] !="PORTALID"}}
                <tr>
                    {{each v as vv ii}}
                    {{if ii == 0 || ii == 1}}
                    <td>
                        {{vv}}
                    </td>
                    {{/if}}
                    {{/each}}


                    <td>
                        <select>
                            <option value="-1">不匹配任何字段</option>
                            {{each data as vd id}}
                            {{if id > 0 && vd[3] != ""}}
                            {{if id == i}}
                            <option selected="selected" value={{vd[3]}}>{{vd[3]}}</option>
                            {{else}}
                            <option value={{vd[3]}}>{{vd[3]}}</option>
                            {{/if}}
                            {{/if}}
                            {{/each}}
                        </select>
                    </td>


                    {{each v as vv ii}}
                    {{if ii == 4}}
                    <td>{{vv}}</td>
                    {{/if}}
                    {{/each}}

                    {{if v[2] == "PRI"}}
                    <td class="text-center"><input type="radio" name="isPK" fieldPk="{{v[0]}}" checked disabled/></td>
                    {{else }}
                    <td class="text-center"><input type="radio" name="isPK" disabled/></td>
                    {{/if}}
                </tr>
                {{/if}}
                {{/each}}
                </tbody>
            </table>
        </div>
        <button type="button" onclick="createTableAndInsertValue(this)" data-dismiss="modal"
                class="saveExcelSuccess btn bule" tablename="{{tableName}}">增量保存
        </button>
        <button type="button" onclick="saveValueAfterDeleteTable()" data-dismiss="modal"
                class="saveExcelSuccess btn bule" tablename="{{tableName}}">重新上传
        </button>
    </script>

    <%--sql建表新增--%>
    <script type="text/html" id="tableNameOption">
        {{each data as v i}}
        <option value="{{v}}">{{v}}</option>
        {{/each}}
    </script>

    <script type="text/html" id="tableFieldOption">
        {{each data as v i}}
        <option value="{{v}}">{{v}}</option>
        {{/each}}
    </script>

    <%--
    [[第一行],[第二行],[第三行], ... ,[最后一行]]
    --%>
    <script type="text/html" id="tableFieldCheckBox">
        {{each data as v i}}
        <div class="row">
            <div class="col-md-1">
            </div>
            {{each v as rowv rowi}}
            <div class="col-md-2">
                <input type="checkbox" id="{{owner}}{{rowv}}"/>
                <label for="{{owner}}{{rowv}}">{{rowv}}</label>
            </div>
            {{/each}}
        </div>
        {{/each}}
    </script>

    <%--预览sql--%>
    <script type="text/html" id="previewSqlData">
        <thead>
        <tr>
            {{each columnName as item i}}
            <th class="text-center">
                {{item}}
            </th>
            {{/each}}
        </tr>
        </thead>
        <tbody id="pre-body">
        {{each data as item i}}
        <tr class="text-center">
            {{each item as vv ii}}
            {{if vv == "" || vv == null}}
            <td>-</td>
            {{else}}
            <td>{{vv}}</td>
            {{/if}}
            {{/each}}
        </tr>
        {{/each}}
        </tbody>
    </script>

    <%--同步设置与记录列表--%>
    <script type="text/html" id="synchronizeConfiguration">
        {{each list as value i}}
        <tr>
            <td>{{value.tableName}}</td>
            <td>
                <select class="synchronizeTable" id="{{value.id}}">
                    <option value="TERMINATION">
                        {{if value.frequency == 0}}
                        请选择同步周期
                        {{else}}
                        停止当前同步操作
                        {{/if}}
                    </option>
                    <option value="HALF_OF_THE_DAY">12小时</option>
                    <option value="DAY">天</option>
                    <option value="WEEK">周</option>
                    <option value="MONTH">月</option>
                    <option value="YEAR">年</option>
                </select>
            </td>
            <td>
                <a href="${ctx}/fileNet/downloadLog?fileName={{value.tableName}}.log" target="_Blank">点击下载日志</a>
            </td>
        </tr>
        {{/each}}
    </script>
</div>
<%--模板定义 结束--%>
</html>
