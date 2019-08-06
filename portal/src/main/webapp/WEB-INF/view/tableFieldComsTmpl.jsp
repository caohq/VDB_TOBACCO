<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/24 0024
  Time: 下午 4:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/html" id="editTableFieldComsTmpl">

    <div class="skin skin-minimal">
        <table class="table table-hover table-bordered">
            <thead>
            <tr>
                <th>字段名称</th>
                <th><label style="color:red;">*</label>注释</th>
                <th>显示类型</th>
            </tr>
            </thead>
            <tbody>
            {{each tableInfosList as tableInfos i }}
            {{each tableInfos.tableInfos as tableInfo i }}
            <tr>
                <td class="fieldComsKey" tableName="{{tableInfos.tableName}}" fieldName="{{tableInfo.columnName}}"
                    columnNameLabel="{{tableInfo.columnNameLabel}}" dataType="{{tableInfo.dataType}}">
                    {{if tableInfo.columnNameLabel}}
                    {{tableInfo.columnNameLabel}}{{else}}{{tableInfo.columnName}}{{/if}}
                </td>
                <td>
                    <input type="text" class="form-control  input-sm fieldComsValue" style="width: 60%"
                           name="order_customer_name" value="{{tableInfo.columnComment}}" clumnCommet="{{tableInfo.columnComment}}">
                </td>
                <td class="td_showType">
                    {{if tableInfo.dataType=='int'||tableInfo.dataType=='integer'||tableInfo.dataType=='float'||tableInfo.dataType=='double'}}
                    <select class="sel form-control" style="width: 150px;display: inline">
                        <option class="sel_type" value="1" on="1">文本型</option>
                        <option class="sel_type" value="3" on="3">字典枚举型</option>
                        <option class="sel_type" value="4" on="4">关联数据表</option>
                    </select>
                    {{else if tableInfo.dataType=='date'||tableInfo.dataType=='time'||tableInfo.dataType=='datetime'}}
                    <select class="sel form-control" style="width: 150px;display: inline">
                        <option class="sel_type" value="1" on="1">文本型</option>
                        <option class="sel_type" value="3" on="3">字典枚举型</option>
                        <option class="sel_type" value="4" on="4">关联数据表</option>
                    </select>
                    {{else}}
                    <select class="sel form-control" style="width: 150px;display: inline">
                        <option class="sel_type" value="1" on="1">文本型</option>
                        <option class="sel_type" value="2" on="2">URL</option>
                        <option class="sel_type" value="3" on="3">字典枚举型</option>
                        <option class="sel_type" value="4" on="4">关联数据表</option>
                        <option class="sel_type" value="5" on="5">文件型</option>
                    </select>
                    {{/if}}
                    <span class="checkTypeDetail" id="{{tableInfo.columnName}}"
                          style="display: none;">
                    </span>

                </td>
            </tr>
            {{/each}}
            {{/each}}
            </tbody>
        </table>
    </div>
</script>

<script type="text/html" id="previewTableDataAndComsTmpl">
    <div class="skin skin-minimal">
        <table class="table table-hover table-bordered" border="0" style="table-layout: auto;">
            {{each datas as itemList i}}
            {{if i == 0}}
            <thead>
            <tr style="word-break: keep-all">
                <th>#</th>
                {{each itemList as item j}}
                <th>{{item.columnName}}
                    {{if item.columnComment}}
                    <br/>({{item.columnComment}})
                    {{/if}}
                </th>
                {{/each}}
            </tr>
            </thead>
            {{/if}}

            {{if i != 0}}
            <tbody border="0">
            <tr>
                <td>{{i}}</td>
                {{each itemList as item j}}
                <td title="{{item}}" style="white-space: nowrap;text-overflow: ellipsis;overflow: hidden;max-width: 120px;">{{item}}</td>
                {{/each}}
            </tr>
            </tbody>
            {{/if}}
            {{/each}}

        </table>
    </div>
</script>


<script type="text/html" id="tableNameLi">
    {{each data as value i}}
    {{each value as v key}}
    {{if i == 0}}
    <li class="active">
        <a href={{"#"+key}} data-toggle="tab" aria-expanded="true"> {{key}} </a>
    </li>
    {{else}}
    <li>
        <a href={{"#"+key}} data-toggle="tab"> {{key}} </a>
    </li>
    {{/if}}
    {{/each}}
    {{/each}}
</script>

<script type="text/html" id="tableNameDiv">
    {{each data as value i}}
    {{each value as v key}}
    {{if i== 0}}
    <div class="tab-pane active">
        <form id={{key}}></form>
    </div>
    {{else}}
    <div class="tab-pane">
        <form id={{key}}></form>
    </div>
    {{/if}}
    {{/each}}
    {{/each}}
</script>

<%--网盘功能  目录列表--%>
<script type="text/html" id="fileBarTemplate">
    {{each data as v i}}
    {{if i == 0}}
    &nbsp;|&nbsp;<span path="{{v.path}}" class="modules">{{v.name}}</span>
    {{else}}
    &nbsp;>&nbsp;<a path="{{v.path}}" class="modules">{{v.name}}</a>
    {{/if}}
    {{/each}}
</script>

<%--网盘功能 列表--%>
<script type="text/html" id="fileNetList">
    {{each data as v i}}
    <tr>
        <%-- <td> 暂时注释选中框
             <input type="checkbox" path="{{v.filePath}}" class="fileNetCheck"/>
         </td>--%>
        <td path="{{v.filePath}}" class="{{v.fileType}} fileName"><a href="javaScript:void(0)">{{v.fileName}}</a></td>
        <td>{{v.fileLength}}</td>
        <td>{{v.fileLastModified}}</td>
    </tr>
    {{/each}}
</script>
