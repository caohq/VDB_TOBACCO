var closableTab = {

    //添加tab
    //tabItem = {id：页签ID,name：页签名称,closable：是否可关闭,template：显示内容HTML}
    addTab: function (tabItem) {
        var id = "tab_seed_" + tabItem.id;
        var container = "tab_container_" + tabItem.id;

        $("li[id^=tab_seed_]").removeClass("active");
        $("div[id^=tab_container_]").removeClass("active");

        if (!$('#' + id)[0]) {
            /*a:style="position: relative;padding:2px 20px 2px 15px"*/
            var li_tab = '<li role="presentation" class="" id="' + id + '"><a href="#' + container + '"  role="tab" data-toggle="tab" >' + tabItem.name;
            if (tabItem.closable) {
                li_tab = li_tab + '<i class="glyphicon glyphicon-remove small" tabclose="' + id + '" style="position: absolute;right:4px;top: 4px;"  onclick="closableTab.closeTab(this)"></i></a></li> ';
            } else {
                li_tab = li_tab + '</a></li>';
            }

            var tabpanel = '<div role="tabpanel" class="tab-pane" id="' + container + '">' +
                tabItem.template +
                '</div>';
            $('.nav-tabs.activeTabs').append(li_tab);
            $('.tab-content.activeTabs').append(tabpanel);
        }
        $("#" + id).addClass("active");
        $("#" + container).addClass("active");
    },

    //关闭tab
    closeTab: function (item) {
        var val = $(item).attr('tabclose');
        var containerId = "tab_container_" + val.substring(9);

        if ($('#' + containerId).hasClass('active')) {
            if ($('#' + val).prev()[0]) {
                $('#' + val).prev().addClass('active');
                $('#' + containerId).prev().addClass('active');
            } else {
                $('#' + val).next().addClass('active');
                $('#' + containerId).next().addClass('active');
            }
        }
        $("#" + val).remove();
        $("#" + containerId).remove();
        closableTab.afterCloseTab(item);
        /*bootbox.confirm("<span style='font-size: 16px'>确认要关闭此条记录吗?</span>", function (r) {
            if (r) {
            }
        })*/
    },
    addTabComment: function (tabItem) {
        var tableComment = tabItem.tableComment;
        var id = "tab_seed_" + tabItem.id;
        $('#' + id).attr("title", tableComment);
    },
    // 关闭之后的操作
    afterCloseTab: function (item) {

    },


    // 初始化动态标签失败操作
    error: function (item) {

    }
};