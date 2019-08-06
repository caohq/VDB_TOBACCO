/**
 * Created by xiajl on 20171108
 */

$(function () {
    //xiajl20190425
    template.helper("dateFormat", formatDate);
    getData(1);

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


$("#isTemplate").change(function(){
    if ($(this).prop("checked") ){
        $("#divTemplate").css("display","");
        $("#metaTemplateName").val($("#Task_dataName").val() +"元数据模板");
        $(this).val("true");
        //选择模板失效
        $("#isSelectTemplate").prop("checked",false);
        $("#isSelectTemplate").attr("checked",false);
        $("#isSelectTemplate").attr("disabled","disabled");
    }
    else{
        $("#divTemplate").css("display","none");
        $("#isSelectTemplate").removeAttr("disabled");
        $(this).val("false");
    }
});

$("#isSelectTemplate").change(function(){
    if ($(this).prop("checked") ){
        $("#divSelectTemplate").css("display","");

        $("#isTemplate").prop("checked","");
        $("#isTemplate").attr("checked",false);
        $("#isTemplate").attr("disabled","disabled");
    }
    else{
        $("#divSelectTemplate").css("display","none");
        $("#isTemplate").removeAttr("disabled");
    }
});



//xiajl20190425
//获取元数据模板信息数据
function getData(pageNo) {
    $.ajax({
        url: ctx +"/metaTemplate/list",
        type: "get",
        dataType: "json",
        data: {
            "subjectCode":sub,
            "pageNo": pageNo,
            "pageSize": 10
        },
        success: function (data) {
            var html = template("systemTmpl", data);
            $("#metaTemplateList").empty();
            $("#metaTemplateList").append(html);
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

function viewData(id) {
    window.open(ctx + "/metaTemplate/templateDetailView/" + id, "_blank");
}

//选择模板数据
function selectData(id) {
    $.ajax({
        url: ctx + "/metaTemplate/templateDetail/" + id ,
        type: "get",
        dataType: "json",
        success: function (data) {
            if (data.metaTemplate) {
                $("#Task_dataName").val(data.metaTemplate.title);
                $("#dataDescribeID").val(data.metaTemplate.introduction);
                /*$("#cutDiv").append('<img src="" id="cutimg" style="height:100%; width: 100%;display: block"/>');
                var path = data.metaTemplate.imagePath;
                $('#cutimg').attr('src', path);
                $('#imgPath').val(data.metaTemplate.imagePath);*/
                $("#cutDiv#cutimg").attr("src", "${ctx}/resources/img/imgtemp.jpg");

                $("#select2_tags").val(data.metaTemplate.keyword);
                $("#select2_tags").select2({
                    tags: true,
                    multiple: true,
                    tags: [""]
                });
                $("#centerCatalogId").val(data.metaTemplate.catalogId);
                initCenterResourceCatalog("#jstree-demo", data.metaTemplate.catalogId);
                $("#startTime").val(convertMilsToDateString(data.metaTemplate.startTime));
                $("#endTime").val(convertMilsToDateString(data.metaTemplate.endTime));
                $("#dataSourceDesID").val(data.metaTemplate.createdByOrganization);
                $("#create_Organization").val(data.metaTemplate.createOrgnization);
                $("#create_person").val(data.metaTemplate.createPerson);

                $("#createTime").val(convertMilsToDateString(data.metaTemplate.creatorCreateTime));
                $("#publish_Organization").val(data.metaTemplate.publishOrgnization);
                $("#Task_email").val(data.metaTemplate.email);
                $("#Task_phone").val(data.metaTemplate.phoneNum);

                //xiajl20190310增加，显示扩展元数据信息
                if (data.metaTemplate.extMetadata){
                    $("#divExtMetadata input,select").each(function () {
                        var str = this.name;
                        var valueStr = "";
                        for (var i = 0; i < data.metaTemplate.extMetadata.length; i++) {
                            $.each(data.metaTemplate.extMetadata[i], function (key, value) {
                                if (key == str) {
                                    valueStr = value;
                                }
                            })
                        }
                        $(this).val(valueStr);
                    });
                }
                toastr["success"]("读取模板数据成功！", "读取模板数据成功！");
            } else {
                toastr["error"]("读取模板数据错误！", "读取模板数据错误！");
            }
        },
        error: function () {
            toastr["error"]("读取模板数据错误！", "读取模板数据错误！");
        }
    });

}
