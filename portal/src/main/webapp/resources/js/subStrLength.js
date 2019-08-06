/**
 * Created by zhijian on 2016/5/21.
 */
function getSubStr(val, maxLength) {
    if (maxLength <= 0) {
        return val;
    }
    var valLength = strLength(val);
    if (valLength > maxLength) {
        val = subStr(val, maxLength);
        val = val + "...";
    }
    return val;
}

function subStr(val, maxLength) {
    var len = 0;
    var index = 0;
    for (; len <= maxLength; index++) {
        if (val.charCodeAt(index) > 127 || val.charCodeAt(index) == 94) {
            len += 2;
        } else {
            len++;
        }
    }
    return val.substr(0, index - 1);

}
function strLength(val) {
    var len = 0;
    for (var i = 0; i < val.length; i++) {
        if (val.charCodeAt(i) > 127 || val.charCodeAt(i) == 94) {
            len += 2;
        } else {
            len++;
        }
    }
    return len;
}