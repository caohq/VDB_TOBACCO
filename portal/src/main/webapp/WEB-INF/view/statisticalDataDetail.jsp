<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2019/3/6
  Time: 14:14
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
    <title>统计管理detail</title>
    <link href="${ctx}/resources/css/dataUpload.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/resources/bundles/bootstrap-toastr/toastr.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${ctx}/resources/bundles/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" href="${ctx}/resources/bundles/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="${ctx}/resources/bundles/bootstrapv3.3/css/bootstrap.css">
    <link href="${ctx}/resources/css/home.css" type="text/css"/>
    <style type="text/css">
        .css_chartsleft {
            width: 38%;
            height: 100%;
            float: left;
            margin-left: 1%;
        }

        .css_charts {
            width: 38%;
            height: 100%;
            float: left;
            margin-left: 13%;
            margin-right: 10%;
        }

        #datashowvisitTotal {
            width: 80%;
            height: 50%;
            margin-left: 4%;
            /*margin-right: 10%;*/
        }

        #datashowdownTotal {
            width: 80%;
            height: 50%;
            margin-left: 4%;
            /*margin-right: 10%;*/
        }

        .fa {
            font-size: 14px !important;
        }
    </style>
</head>
<body>
<div class="tap_div" style="margin-top:15px;">
    <div id="div1" style="width: 100%;height:400px;">
        <div class="report-title">
            <h4><i class="fa fa-caret-right"></i>${applicationScope.menus['organization_title']}访问量下载量TOP10统计</h4>
            <div></div>
            <a href="#" class="more" onclick="func_moreDetail()">查看更多</a>
        </div>
        <div style="width: 100%;height:100%;margin-top: 1%" class="report-content row">
            <div id="datashowvisit" class="css_chartsleft"></div>
            <div id="datashowdown" class="css_charts"></div>
        </div>
    </div>

    <%-- 专题详情--%>
    <div id="div2" style="width: 100%;height:700px; display: none;">
        <div class="report-title">
            <h4><i class="fa fa-caret-right"></i>${applicationScope.menus['organization_title']}访问量下载量统计</h4>
            <div></div>
            <a href="#" class="more" onclick="func_toTopTen()">返回</a>
        </div>
        <div id="datashowvisitTotal"></div>
        <div id="datashowdownTotal" style="margin-top: 30px;margin-bottom: 20px;"></div>
    </div>

    <%--数据集Top10--%>
    <div style="width: 100%;height:400px;margin-top: 50px;">
        <div class="report-title">
            <h4><i class="fa fa-caret-right"></i>数据集访问量下载量TOP10统计</h4>
            <a href="#" class="more"></a>
        </div>
        <div style="width: 100%;height:100%;margin-top: 2%;" class="report-content row">
            <div id="datacollectionvisit" class="css_chartsleft"></div>
            <div id="datacollectiondown" class="css_charts"></div>
        </div>
    </div>


    <%--显示所有主题库节点--%>
    <div id="themeList" style="width: 100%;margin-top: 50px;">
        <div class="report-title">
            <h4><i class="fa fa-caret-right"></i>按${applicationScope.menus['organization_title']}查看访问量下载量</h4>
            <div></div>
            <a href="#" class="more"></a>
        </div>
        <div class="clearfix"></div>
        <div class="report-content row" style="height-min:300px;width:100%;">
            <div id="showAllThemeSpan" class="pic-items">

            </div>
        </div>
    </div>
    <%--根据主题库节点查看该主题库节点内的访问量下载量--%>
    <div id="themeDetail" style="width: 100%;min-height: 400px;margin-top: 50px;display: none;margin-bottom: 3%;">
        <div class="report-title">
            <h4><i class="fa fa-caret-right"></i>按${applicationScope.menus['organization_title']}查看访问量下载量</h4>
            <div></div>
            <a id="reback_id" href="#" class="more">返回</a>
        </div>
        <div id="themeDetail111"
             style="width: 80%;margin-top:20px;margin-left: 10%;min-height:200px;">

        </div>
        <div id="detialCharts" style="width: 100%; float: none;margin-top:20px;">
            <table id="singleThemeTable" class="table table-hover biaoge" spellcheck="0" border="0">
                <thead>
                <tr class="table_tr">
                    <td width="60%">数据集名</td>
                    <td width="20%">访问量&nbsp;&nbsp;<i id="vcount_desc" class="fa fa-sort-desc" aria-hidden="true"
                                                      onclick="func_asc()"></i>
                        <i id="vcount_asc" class="fa fa-sort-asc" aria-hidden="true" style="display: none;"
                           onclick="func_desc()"></i></td>
                    <td width="20%">下载量&nbsp;&nbsp;<i id="dcount_desc" class="fa fa-sort-desc" aria-hidden="true"
                                                      onclick="func_ascDown()"></i>
                        <i id="dcount_asc" class="fa fa-sort-asc" aria-hidden="true" style="display: none;"
                           onclick="func_descDown()"></i>
                    </td>
                </tr>
                </thead>
                <tbody id="single_tbody"></tbody>
            </table>

            <div class="review-item clearfix">

                <div id="page_div" style="padding-top: 25px; float: left; display: none;">
                    当前第&nbsp;<span style="color:blue;" id="currentPageNo"></span>&nbsp;页,&nbsp;共&nbsp;<span
                        style="color:blue;"
                        id="totalPages"></span>页，<span
                        style="color:blue;" id="totalCount"></span>&nbsp;条数据
                </div>
                <div style="float: right;">
                    <div id="pagination"></div>
                </div>

            </div>
        </div>
            <div style="display: none;margin-top:30px;" id="pointOut_div" align="center"><h2>暂时没有数据集</h2></div>
    </div>
</div>


<script type="text/html" id="showAllTheme">
    {{each list as value i}}
    <div class="pic-item" style="width:210px;float: left; height: 200px;margin: 15px;">
        <a href="javaScript:void(0)" id="{{value.subjectCode}}" onclick="findBySubjectCode(this)">
            <img src="{{value.imagePath}}" width="200px" height="150px"/>
            <span style="text-align: center;font-weight: bolder;font-size: smaller;display: block;">{{value.subjectName}}</span>
        </a>
    </div>
    {{/each}}
</script>

<script type="text/html" id="showThemeDetial11">
    {{each subject as value i}}
    <div class="pic-item" style=" width: 17%; float: left;height: 150px;margin-bottom: 50px;">
        <img src="{{value.imagePath}}" width="200px" height="150px"/>
        <h5 style="text-align: center;">{{value.subjectName}}</h5>
    </div>
    <div class="pic-item" style=" width: 60%; float: left; padding: 20px;height:150px;margin-left: 30px;margin-top: 10px;">
        <label>简介：</label>{{value.brief}}
    </div>

    {{/each}}
</script>

<script type="text/html" id="single_tbodyTemplent">
    {{each vCount as value i}}
    <tr>
        <td><a href="${applicationScope.systemPro['dataexplore.url']}/{{value.id}}">{{value.title}}</a></td>
        <td>{{value.vCount}}</td>
        <td>{{value.dCount}}</td>
    </tr>
    {{/each}}
</script>

</body>
<!--为了加快页面加载速度，请把js文件放到这个div里-->
<div id="siteMeshJavaScript">
    <script src="${ctx}/resources/js/jquery.json.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/bundles/select2/select2.min.js"></script>
    <script src="${ctx}/resources/bundles/jstree/dist/jstree.js"></script>
    <script src="${ctx}/resources/bundles/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
    <script src="${ctx}/resources/bundles/layerJs/layer.js"></script>
    <script src="${ctx}/resources/bundles/echarts/echarts.min.js"></script>
    <%--<script src="${ctx}/resources/bundles/jquery.svg3dtagcloud/jquery.svg3dtagcloud.min.js"></script>--%>

    <script type="text/javascript">
        var s_subjectCode = "";
        var axisLabel = {     //横坐标字体倾斜展示
            interval: 0,
            // rotate: 45,//倾斜度 -90 至 90 默认为0
            // margin: 2,
            formatter: function (value, index) {
                var s = value.substring(0, 10);
                var ret = "";//拼接加\n返回的类目项
                var maxLength = 2;//每项显示文字个数
                var valLength = s.length;//X轴类目项的文字个数
                var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                if (rowN > 1)//如果类目项的文字大于3,
                {
                    for (var i = 0; i < rowN; i++) {
                        var temp = "";//每次截取的字符串
                        var start = i * maxLength;//开始截取的位置
                        var end = start + maxLength;//结束截取的位置
                        //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                        temp = s.substring(start, end) + "\n";
                        ret += temp; //凭借最终的字符串
                    }
                    return ret;
                } else {
                    return s;
                }
            }
        };
        var grid = {bottom: "30%"};
        var colors = ['#4a8cd6', '#da6663', '#7e64a1', '#a987d6',
            '#58bae5', '#54d7b3', '#51c061', '#89c154', '#e2e061',
            '#e2b65f'];
        $(function () {

            //统计主题库内访问量前十
            $.ajax({
                url: "${ctx}/ThemeStatisticVisit",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datashowvisit'));
                    var option = {
                        title: {
                            text: '${applicationScope.menus['organization_title']}TOP10访问量',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        legend: {
                            data: data.name,
                            x: 'center',
                            top: 'bottom'
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                            }
                        },
                        xAxis: [{
                            type: 'category',
                            data: data.name,
                            axisLabel: {
                                interval: 0,
                                rotate: 45,//倾斜度 -90 至 90 默认为0
                                margin: 2
                            },
                            splitLine: {
                                show: false
                            }
                        }],
                        yAxis: [
                            {
                                type: 'value'
                            }],
                        series: [{
                            name: '访问量',
                            type: 'bar',
                            barWidth: 25,
                            data: data.visitCount,
                            //配置样式
                            itemStyle: {
                                //通常情况下：
                                normal: {
                                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                    color: function (params) {
                                        var colorList = colors;
                                        return colorList[params.dataIndex];
                                    }
                                }
                            }
                        }]
                    };
                    option["xAxis"][0]["axisLabel"] = axisLabel;
                    option["grid"] = grid;
                    chart.setOption(option);
                }
            });
            //统计主题库内下载量前十
            $.ajax({
                url: "${ctx}/ThemeStatisticDown",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datashowdown'));
                    var option = {
                        // color: ['#5182bb', '#be4f4f', '#9bbb5e', '#8064a1', '#4facc5', '#2d4d74'],
                        title: {
                            text: '${applicationScope.menus['organization_title']}TOP10下载量',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        legend: {
                            data: data.name,
                            x: 'center',
                            top: 'bottom'
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                            }
                        },
                        xAxis: [{
                            type: 'category',
                            data: data.name,
                            axisLabel: {
                                interval: 0,
                                rotate: 45,//倾斜度 -90 至 90 默认为0
                                margin: 2
                            },
                            splitLine: {
                                show: false
                            }
                        }],
                        yAxis: [
                            {
                                type: 'value'
                            }],
                        series: [{
                            name: '下载量',
                            type: 'bar',
                            //设置柱子的宽度
                            barWidth: 25,
                            data: data.downCount,
                            //配置样式
                            itemStyle: {
                                //通常情况下：
                                normal: {
                                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                    color: function (params) {
                                        var colorList = colors;
                                        return colorList[params.dataIndex];
                                    }
                                }
                            }
                        }]
                    };
                    option["xAxis"][0]["axisLabel"] = axisLabel;
                    option["grid"] = grid;
                    chart.setOption(option);
                }
            });
            //统计数据集访问量前十
            $.ajax({
                url: "${ctx}/dataCollentionVisit",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datacollectionvisit'));
                    var option = {
                        // color: ['#5182bb', '#be4f4f', '#9bbb5e', '#8064a1', '#4facc5', '#2d4d74'],
                        title: {
                            text: '数据集TOP10访问量',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        legend: {
                            data: data.name,
                            x: 'center',
                            top: 'bottom'
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                            }
                        },
                        xAxis: [{
                            type: 'category',
                            data: data.name,
                            axisLabel: {
                                interval: 0,
                                rotate: 45,//倾斜度 -90 至 90 默认为0
                                margin: 2,
                                textStyle: {
                                    // fontWeight: "bolder",
                                    // color: "#000000"
                                }
                            },
                            splitLine: {
                                show: false
                            }
                        }],
                        yAxis: [
                            {
                                type: 'value'
                            }],
                        series: [{
                            name: '访问量',
                            type: 'bar',
                            barWidth: 25,
                            data: data.visitCount,
                            //配置样式
                            itemStyle: {
                                //通常情况下：
                                normal: {
                                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                    color: function (params) {
                                        var colorList = colors;
                                        return colorList[params.dataIndex];
                                    }
                                }
                            }
                        }]
                    };
                    option["xAxis"][0]["axisLabel"] = axisLabel;
                    option["grid"] = grid;
                    chart.setOption(option);
                }
            });
            //统计数据集下载量前十
            $.ajax({
                url: "${ctx}/dataCollentionDown",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datacollectiondown'));
                    var option = {
                        // color: ['#5182bb', '#be4f4f', '#9bbb5e', '#8064a1', '#4facc5', '#2d4d74'],
                        title: {
                            text: '数据集TOP10下载量',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        legend: {
                            data: data.name,
                            x: 'center',
                            top: 'bottom'
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                            }
                        },

                        xAxis: [{
                            type: 'category',
                            data: data.name,
                            splitLine: {     //去掉网格线
                                show: false
                            }
                        }],
                        yAxis: [
                            {
                                type: 'value'
                            }],
                        series: [{
                            name: '下载量',
                            type: 'bar',
                            //设置柱子的宽度
                            barWidth: 25,
                            data: data.downCount,
                            //配置样式
                            itemStyle: {
                                //通常情况下：
                                normal: {
                                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                    color: function (params) {
                                        var colorList = colors;
                                        return colorList[params.dataIndex];
                                    }
                                }
                            }
                        }]
                    };
                    option["xAxis"][0]["axisLabel"] = axisLabel;
                    option["grid"] = grid;
                    chart.setOption(option);
                }
            });
            //查询出所有主题库
            $.ajax({
                url: "${ctx}/showAllTheme",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var html = template("showAllTheme", data);
                    $("#showAllThemeSpan").append(html);
                }
            })
        });


        function findBySubjectCode(i) {
            var subjectCode = $(i).attr("id");
            $.ajax({
                url: "${ctx}/findBySubjectCode",
                type: "post",
                dataType: "json",
                data: {"subjectCode": subjectCode},
                success: function (data) {
                    $("#themeDetail111").html(" ");
                    $("#themeList").hide();
                    $("#themeDetail").show();
                    var html = template("showThemeDetial11", data);
                    var subject = data.subject;
                    $("#themeDetail111").append(html);
                    var page = 1;
                    singcharts(subjectCode, page);
                }
            });
        }

        $("#reback_id").click(function () {
            $("#themeList").show();
            $("#themeDetail").hide();
        });

        function fun_limit(subjectCode, data) {
            if (data !== null) {
                $("#page_div").show();
            }

            $("#currentPageNo").html(data.currentPage);
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
                singcharts(subjectCode, num);
            });
        }


        function singcharts(subjectCode, page) {
            $("#single_tbody").html(" ");
            s_subjectCode = subjectCode;
            $.ajax({
                url: "${ctx}/SingleSortTable",
                type: "post",
                dataType: "json",
                data: {"subjectCode": subjectCode, "pageNo": page},
                success: function (data) {
                    if(data.vCount.length===0){
                        $("#detialCharts").hide();
                        $("#pointOut_div").show();
                    }else{
                        $("#pointOut_div").hide();
                        $("#detialCharts").show();
                        var html = template("single_tbodyTemplent", data);
                        $("#single_tbody").append(html);
                        fun_limit(subjectCode, data);
                    }

                }
            });
        }

        function func_asc() {
            var subjectCode = subjectCode;
            var pageNo = 1;
            var sortMethod = "asc";
            $("#vcount_desc").hide();
            $("#vcount_asc").show();
            $("#single_tbody").html(" ");
            sortByvisit(subjectCode, pageNo, sortMethod);
        }

        function func_desc() {
            var subjectCode = subjectCode;
            var pageNo = 1;
            var sortMethod = "desc";
            $("#vcount_asc").hide();
            $("#vcount_desc").show();
            $("#single_tbody").html(" ");
            sortByvisit(subjectCode, pageNo, sortMethod);
        }

        function sortByvisit(subjectCode, pageNo, sortMethod) {
            $.ajax({
                url: "${ctx}/SortByVcount",
                type: "post",
                dataType: "json",
                data: {"subjectCode": s_subjectCode, "pageNo": pageNo, "sortMethod": sortMethod},
                success: function (data) {
                    $("#single_tbody").html(" ");
                    var html = template("single_tbodyTemplent", data);
                    $("#single_tbody").append(html);
                    if (data !== null) {
                        $("#page_div").show();
                    }
                    $("#currentPageNo").html(data.currentPage);
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
                        sortByvisit(subjectCode, num, sortMethod);
                    });
                }
            });
        }

        function func_ascDown() {
            var subjectCode = subjectCode;
            var pageNo = 1;
            var sortMethod = "asc";
            $("#dcount_desc").hide();
            $("#dcount_asc").show();
            $("#single_tbody").html(" ");
            sortByDown(subjectCode, pageNo, sortMethod);
        }

        function func_descDown() {
            var subjectCode = subjectCode;
            var pageNo = 1;
            var sortMethod = "desc";
            $("#dcount_asc").hide();
            $("#dcount_desc").show();
            $("#single_tbody").html(" ");
            sortByDown(subjectCode, pageNo, sortMethod);
        }

        function sortByDown(subjectCode, pageNo, sortMethod) {
            $.ajax({
                url: "${ctx}/SortByDcount",
                type: "post",
                dataType: "json",
                data: {"subjectCode": s_subjectCode, "pageNo": pageNo, "sortMethod": sortMethod},
                success: function (data) {
                    $("#single_tbody").html(" ");
                    var html = template("single_tbodyTemplent", data);
                    $("#single_tbody").append(html);
                    if (data !== null) {
                        $("#page_div").show();
                    }
                    $("#currentPageNo").html(data.currentPage);
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
                        sortByDown(subjectCode, num, sortMethod);
                    });
                }
            });
        }

        function func_moreDetail() {
            $("#div1").hide();
            $.ajax({
                url: "${ctx}/ThemeStatisticVisitTotal",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datashowvisitTotal'));
                    var option = {
                        title: {
                            text: '${applicationScope.menus['organization_title']}访问量统计',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        legend: {
                            data: data.name,
                            x: 'center',
                            top: 'bottom'
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                            }
                        },
                        xAxis: [{
                            type: 'category',
                            data: data.name,
                            axisLabel: {
                                interval: 0,
                                rotate: 45,//倾斜度 -90 至 90 默认为0
                                margin: 2
                            },
                            splitLine: {
                                show: false
                            }
                        }],
                        yAxis: [
                            {
                                type: 'value'
                            }],
                        series: [{
                            name: '访问量',
                            type: 'bar',
                            barWidth: 22,
                            data: data.visitCount,
                            //配置样式
                            itemStyle: {
                                //通常情况下：
                                normal: {
                                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                    color: function (params) {
                                        var colorList = colors;
                                        return colorList[params.dataIndex];
                                    }
                                }
                            }
                        }]
                    };
                    option["xAxis"][0]["axisLabel"] = axisLabel;
                    option["grid"] = grid;
                    chart.setOption(option);
                }
            });

            $.ajax({
                url: "${ctx}/ThemeStatisticDownTotal",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var chart = echarts.init(document.getElementById('datashowdownTotal'));
                    var option = {
                        title: {
                            text: '${applicationScope.menus['organization_title']}下载量统计',
                            left: 'center'
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow',
                                label: {
                                    show: true
                                }
                            }
                        },
                        legend: {
                            data: data.name,
                            x: 'center',
                            top: 'bottom'
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                            }
                        },
                        xAxis: [{
                            type: 'category',
                            data: data.name,
                            axisLabel: {
                                interval: 0,
                                rotate: 45,//倾斜度 -90 至 90 默认为0
                                margin: 2
                            },
                            splitLine: {
                                show: false
                            }
                        }],
                        yAxis: [
                            {
                                type: 'value'
                            }],
                        series: [{
                            name: '下载量',
                            type: 'bar',
                            barWidth: 22,
                            data: data.downCount,
                            //配置样式
                            itemStyle: {
                                //通常情况下：
                                normal: {
                                    //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                                    color: function (params) {
                                        var colorList = colors;
                                        return colorList[params.dataIndex];
                                    }
                                }
                            }
                        }]
                    };
                    option["xAxis"][0]["axisLabel"] = axisLabel;
                    option["grid"] = grid;
                    chart.setOption(option);
                }
            });
            $("#div2").show();
        }

        function func_toTopTen() {
            $("#div1").show();
            $("#div2").hide();
        }
    </script>
</div>
</html>
