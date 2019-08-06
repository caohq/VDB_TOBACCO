<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/24
  Time: 16:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row" style="padding-top:20px;">
    <div>
        <div class="col-md-12">
            <input type="checkbox" id="isTemplate" name="isTemplate" >&nbsp;创建模板
        </div>
    </div>
    <div id="divTemplate" style="display:none;">
        <div class="form-group">
            <label class="control-label col-md-3" for="metaTemplateName">模板名称 <span
                    style="margin-left: 13px"></span>
            </label>
            <div class="col-md-5" style="padding-top:13px">
                <input type="text" class="form-control" name="metaTemplateName" placeholder="请输入元数据模板名称"
                       id="metaTemplateName" >
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-md-3" for="memo">模板备注<span
                    style="margin-left: 13px"></span>
            </label>
            <div class="col-md-5" style="padding-top:13px">
                <input type="text" class="form-control" name="memo" placeholder="请输入元数据模板备注"
                       id="memo" >
            </div>
        </div>

    </div>
</div>