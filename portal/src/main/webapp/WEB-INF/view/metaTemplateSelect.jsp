<%--
  Created by IntelliJ IDEA.
  User: xiajl
  Date: 2019/4/25
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- xiajl20171109 元数据模块基础信息相关 -->
<div class="row" style="padding-top:20px;padding-bottom:20px;padding-left: 20px;padding-right: 20px;">
    <div>
        <div class="col-md-12">
            <p><span style="color: red;">*</span> 为必填项</p>
            <input type="checkbox" id="isSelectTemplate" name="isSelectTemplate">选择从模板中填写数据
        </div>
    </div>
    <div id="divSelectTemplate" style="display:none;">

        <div class="table-scrollable">
            <table class="table table-striped table-bordered table-advance table-hover">
                <thead>
                <tr>
                    <th style="width: 5%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">编号</th>
                    <th style="width: 30%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">
                        元数据模板名称
                    </th>
                    <th style="width: 10%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">创建人</th>
                    <th style="width: 15%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">创建时间</th>
                    <th style="width:20%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">备注</th>
                    <th style="width: 8%;text-align: center;background: #64aed9;color: #FFF;font-weight: bold">操作</th>
                </tr>
                </thead>
                <tbody id="metaTemplateList"></tbody>
            </table>
        </div>
        <div class="row margin-top-20">
            <div class="col-md-6 margin-top-20">
                当前第<span style="color:blue;" id="currentPageNo"></span>页,共<span style="color:blue;" id="totalPages"></span>页,<span style="color:blue;" id="totalCount"></span>条数据
            </div>
            <div class="col-md-6">
                <div id="pagination" style="float: right"></div>
            </div>
        </div>

    </div>
</div>


<script type="text/html" id="systemTmpl">
    {{each list}}
    <tr>
        <td style="text-align: center">{{(currentPage-1)*pageSize+$index+1}}</td>
        <td><a href="javascript:viewData('{{$value.id}}');">{{$value.metaTemplateName}}</a>
        </td>
        <td style="text-align: center">{{$value.metaTemplateCreator}}</td>
        <td style="text-align: center">{{dateFormat($value.metaTemplateCreateDate)}}</td>
        <td style="text-align: center">{{$value.memo}}</td>
        <td id="{{$value.templateId}}" style="text-align: center">
            <button type="button" class="btn default btn-xs purple updateButton"
                    onclick="selectData('{{$value.id}}')"><i class="fa fa-edit"></i>&nbsp;&nbsp;选择
            </button>
            &nbsp;&nbsp;
        </td>
    </tr>
    {{/each}}
</script>