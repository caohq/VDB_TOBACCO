/**
 * Created by liuang on 2017/11/2.
 */

$(function () {
    getLicenseList();
});
function getLicenseList() { //展示选取数据库的表，下拉框展示
    $.ajax({
        type: "GET",
        url: ${ctx} + '/license/list',
        data: {timestamp: new Date()},
        dataType: "json",
        success: function (data) {
            $('#dataLicenseID').empty();
            for (var i = 0; i < data.licenses.length; i++) {
                $('#dataLicenseID').append('<option value="' + data.licenses[i].id + '">' + data.licenses[i].name + '</option>');
            }
        }
    });
}