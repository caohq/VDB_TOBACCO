/**
 * Created by Administrator on 2017/4/26 0026.
 */

var curEditIsChoiceTableOrSql = 0; //1:table;2:sql
var curSourceTableChoice = null;
/*
var curDataSourceId = null;
*/
var curDataSubjectCode = $("#subjectCode").val();
var curTableName = null;
var curSQL = null;
var curSQLStrIndex = 0;
var sqlNum = 1;
var curRefer = null;
var ctx = '${ctx}';
var S_flag="0";
var showColumnType = [];
/**
 *
 * @param obj
 * @param dataSourceId
 * @param tableName
 */
function staticSourceTableChoice(editIsChoiceTableOrSql, obj, subjectCode, tableNameOrSql, refer,flag) {
    S_flag=flag;
    if (refer == "dataService" || !obj || obj.checked) {
        $('#editTableFieldComsId').html("");
        $('#previewTableDataAndComsId').html("");

        $('#editTableDataAndComsButtonId').parent().removeClass("active");
        $('#previewTableDataAndComsButtonId').parent().removeClass("active");


        $('#editTableFieldComsId').removeClass("active");
        $('#previewTableDataAndComsId').removeClass("active");

        $('#editTableDataAndComsButtonId').parent().addClass("active");
        $('#editTableFieldComsId').addClass("active");
        var tableInfosList = null;
        if (editIsChoiceTableOrSql == 1) {
            var tableInfos = getTableFieldComs(subjectCode, tableNameOrSql);
            tableInfosList = [];
            tableInfosList[0] = {tableName: tableNameOrSql, tableInfos: tableInfos};
        }
        $("#staticSourceTableChoiceModal").modal("show");
        var html = template("editTableFieldComsTmpl", {"tableInfosList": tableInfosList});
        $('#editTableFieldComsId').html(html);
        curSourceTableChoice = obj;
        curEditIsChoiceTableOrSql = editIsChoiceTableOrSql;
        curRefer = refer;
        if (editIsChoiceTableOrSql == 1) {
            curTableName = tableNameOrSql;
        }
        preSaveEditTableFieldComs();// 页面与保存coms信息
    } else {
        $(obj).removeAttr("coms");
    }
    $("#form_wizard_1").find(".button-save").removeAttr("disabled");

}

function staticSourceTableChoice_edit(editIsChoiceTableOrSql, obj, subjectCode, tableNameOrSql, refer,flag) {
    $("#btn_check").removeClass("btn btn-primary");
    $("#btn_eidt").removeClass("btn btn-default");

    $("#btn_eidt").addClass("btn btn-primary");
    $("#btn_check").addClass("btn btn-default");
    $("#span_tableName").html("");
    S_flag=flag;
    if (refer == "dataService" || refer == "dataResource"/* || !obj || obj.checked*/) {
        $('#editTableFieldComsId').html("");
        $('#previewTableDataAndComsId').html("");

        $('#editTableDataAndComsButtonId').parent().removeClass("active");
        $('#previewTableDataAndComsButtonId').parent().removeClass("active");

        $('#editTableFieldComsId').removeClass("active");
        $('#previewTableDataAndComsId').removeClass("active");

        $('#editTableDataAndComsButtonId').parent().addClass("active");
        $('#editTableFieldComsId').addClass("active");
        var tableInfosList = null;
        if (editIsChoiceTableOrSql == 1) {
            var tableInfos = getTableFieldComs(subjectCode, tableNameOrSql);
            tableInfosList = [];
            tableInfosList[0] = {tableName: tableNameOrSql, tableInfos: tableInfos};
        }
        $("#undescribe").hide();
        $("#isdescribe").hide();
        $(".qiehuan_div").hide();
        $("#staticSourceTableChoiceModalNew").show();
        $("#span_tableName").html(tableNameOrSql);

        getshowTypeData(tableNameOrSql);
        var html = template("editTableFieldComsTmpl", {"tableInfosList": tableInfosList});
        $('#editTableFieldComsId').html(html);
        curSourceTableChoice = obj;
        curEditIsChoiceTableOrSql = editIsChoiceTableOrSql;
        curRefer = refer;
        if (editIsChoiceTableOrSql == 1) {
            curTableName = tableNameOrSql;
        }
        preSaveEditTableFieldComs();// 页面与保存coms信息
    } else {
        $(obj).removeAttr("coms");
    }
    $("#form_wizard_1").find(".button-save").removeAttr("disabled");

}

function getTableFieldComs(subjectCode, tableName) {
    var dataResult = null;
    $.ajax({
        type: "post",
        url: ctx + '/getTableFieldComs',
        data: {"subjectCode": subjectCode, "tableName": $.trim(tableName), "timestamp": Date.parse(new Date())},
        dataType: "json",
        async: false,
        success: function (data) {
            if (!data || !data.tableInfos) {
                return;
            }
            dataResult = data.tableInfos;
        }
    });
    return deletePORTALID(dataResult);
}

function deletePORTALID(data) {
    var list = [];
    $.each(data, function (index, json) {
        var isPORTALID = false;
        $.each(json, function (key, value) {
            if (value === "PORTALID") {
                isPORTALID = true;
            }
        });
        isPORTALID ? 0 : list.push(json);
    });
    return list;
}

/**
 * 页面与保存coms信息
 */
function preSaveEditTableFieldComs() {
    if (curTableName || curSQL) {
        var fieldComsList = getEditTableOrSqlFieldComs();
        var tableInfos = $.toJSON(fieldComsList);
        if (curEditIsChoiceTableOrSql == 1) {
            $(curSourceTableChoice).attr("coms", tableInfos.toString());
        }
    }
}

/**
 * 保存coms信息
 */
function saveEditTableFieldComs() {
    if (curTableName || curSQL) {
        var fieldComsList = getEditTableOrSqlFieldComs();
        var tableInfos = $.toJSON(fieldComsList);
        if (curEditIsChoiceTableOrSql == 1) {
            $(curSourceTableChoice).attr("coms", tableInfos.toString());
            saveTableFieldComs(curDataSubjectCode, fieldComsList);
        }
        curSourceTableChoice = null;
        curTableName = null;
        curEditIsChoiceTableOrSql = 0;
        curSQL = null;
        curRefer = null;
    }
}

$(function () {
    $("#previewTableDataAndComsButtonId").bind("click", function () {
        if (curEditIsChoiceTableOrSql == 1) {
            var tableInfos = getEditTableOrSqlFieldComs();
            previewTableDataAndComs(curDataSubjectCode, tableInfos);
        }
    });
    //点击保存按钮
    // $("#editTableFieldComsSaveId").bind("click", saveEditTableFieldComs);
    $("#editTableFieldComsCloseId").bind("click", function () {
            $("#staticSourceTableChoiceModal").hide();
            $("#staticSourceTableChoiceModalNew").hide();
        }
    );
});

//点击保存按钮
function fun_saveTbaleFields() {
    var tableComment = $("#tableComment").val();
    if (tableComment === "" || tableComment === null) {
        toastr["warning"]("表名注释不能为空！");
        return;
    } else {
        saveEditTableFieldComs();
    }

}

function getEditTableOrSqlFieldComs() {
    var tableInfosList = [];
    var tableNameVar = null;
    var tableInfos = null;
    var tableInfosListIndex = 0;
    var tableInfosIndex = 0;
    showColumnType.splice(0, showColumnType.length);

    if ($(".fieldComsKey")) {
        $(".fieldComsKey").each(function (index, value, array) {
            var tableName = $(value).attr("tablename");
            if (tableNameVar != tableName) {
                tableInfos = [];
                var tableInfosObj = {tableName: tableName, tableInfos: tableInfos};
                tableInfosList[tableInfosListIndex++] = tableInfosObj;
                tableInfosIndex = 0;
                tableNameVar = tableName;
            }
            var fieldName = $(value).attr("fieldName");
            var columnNameLabel = $(value).attr("columnNameLabel");
            var dataType = $(value).attr("dataType");
            var fieldComs = $($(".fieldComsValue")[index]).val();
            var selectType = $($(".sel")[index]).val();
            showColumnType.push(selectType);
            tableInfos[tableInfosIndex++] = {
                columnName: fieldName,
                columnNameLabel: columnNameLabel,
                columnComment: fieldComs,
                dataType: dataType
            };
        });
    }
    return tableInfosList;
}

function saveTableFieldComs(curDataSubjectCode, tableInfos) {
    var state = "1";
    for (var index in tableInfos) {
        for (var key in tableInfos[index].tableInfos)
            if (S_flag === "1" && (tableInfos[index].tableInfos[key].columnComment === "" || tableInfos[index].tableInfos[key].columnComment === null)) {
                toastr["warning"]("注释不能为空");
                return;
            }
    }

    for (var index in tableInfos) {
        for(var key in tableInfos[index].tableInfos)
            if(tableInfos[index].tableInfos[key].columnComment==""){
                state = "0";
            }
    }

    if (tableInfos && tableInfos.length == 1) {
        var tableComment=$("#tableComment").val();
        if (tableComment === "" || tableComment === null) {
            toastr["warning"]("表名注释不能为空！");
            return;
        }
        $.ajax({
            type: "POST",
            url: ctx + '/saveTableFieldComs',
            data: {
                "state": state,
                "curDataSubjectCode": curDataSubjectCode,
                "tableName": tableInfos[0].tableName,
                "tableInfos": $.toJSON(tableInfos[0].tableInfos),
                "tableComment": tableComment,
                "showColumnType": showColumnType.join(",")
            },
            dataType: "json",
            success: function () {
                toastr["success"]("保存成功！");
                if ($(".qiehuan_div li:eq(0) a").hasClass("active")) {
                    $("#isdescribe").hide();
                    $("#undescribe").show();
                    $(".qiehuan_div").show();
                    $("#staticSourceTableChoiceModalNew").hide();
                } else {
                    $("#isdescribe").show();
                    $("#undescribe").hide();
                    $(".qiehuan_div").show();
                    $("#staticSourceTableChoiceModalNew").hide();
                }
                chooseTable(curDataSubjectCode,0);
            }
        });
    }
}

/*function saveSQLFieldComs(curDataSubjectCode, sqlStr, tableInfos) {
    $.ajax({
        type: "POST",
        url: ctx + '/saveSQLFieldComs',
        data: {
            "curDataSubjectCode": curDataSubjectCode, "sqlStr": sqlStr,
            "tableInfos": $.toJSON(tableInfos)
        },
        dataType: "json",
        success: function () {
        }
    });
}*/

function previewTableDataAndComs(curDataSubjectCode, tableInfosList) {
    $.ajax({
        type: "POST",
        url: ctx + '/previewRelationalDatabaseByTableName',
        data: {
            "curDataSubjectCode": curDataSubjectCode, "tableInfosList": $.toJSON(tableInfosList)
        },
        dataType: "json",
        success: function (data) {
            if (!data || !data.datas) {
                return;
            }
            var columnTitleList = [];
            tableInfosList.forEach(function (tableInfos, index1, array1) {
                tableInfos.tableInfos.forEach(function (value, index2, array2) {
                    // var columnTitle = value.columnName + "(" + value.columnComment + ")";
                    columnTitleList.push({columnName:value.columnName,columnComment:value.columnComment});
                });
            });
            data.datas.unshift(columnTitleList);
            var html = template("previewTableDataAndComsTmpl", {"datas": data.datas});
            $('#previewTableDataAndComsId').html(html);
        }
    });
}

/*function previewSqlDataAndComs(dataSourceId, tableInfosList) {
    var sqlStr;
    if (curRefer == "dataService") {
        sqlStr = $("#publicSql").val();
    } else {
        var sqlStrId = "sqlStr";
        if (curSQLStrIndex < 0) {
            return;
        }
        if (curSQLStrIndex != 0) {
            sqlStrId += curSQLStrIndex;
        }
        sqlStr = $("#" + sqlStrId).val();
    }
    $.ajax({
        type: "POST",
        url: ctx + '/previewRelationalDatabaseBySQL',
        data: {
            "dataSourceId": dataSourceId,
            "sqlStr": sqlStr,
            "tableInfosList": $.toJSON(tableInfosList)
        },
        dataType: "json",
        success: function (data) {
            if (!data || !data.datas) {
                return;
            }
            var columnTitleList = [];
            tableInfosList.forEach(function (tableInfos, index1, array1) {
                tableInfos.tableInfos.forEach(function (value, index2, array2) {
                    // var columnTitle = value.columnNameLabel + "<br>(" + value.columnComment + ")";
                    // columnTitleList.push(columnTitle);
                    columnTitleList.push({columnName:value.columnName,columnComment:value.columnComment});
                });
            });
            data.datas.unshift(columnTitleList);
            var html = template("previewTableDataAndComsTmpl", {"datas": data.datas});
            $('#previewTableDataAndComsId').html(html);
        }
    });
}*/


function editSqlFieldComs(sqlNum) {
    var sqlStrId = "sqlStr";
    if (sqlNum < 0) {
        return;
    }
    if (sqlNum != 0) {
        sqlStrId += sqlNum;
    }
    curSQLStrIndex = sqlNum;
    var sqlStr = $("#" + sqlStrId).val();
    staticSourceTableChoice_edit(2, null, sub, sqlStr, "dataResource",S_flag);
}



