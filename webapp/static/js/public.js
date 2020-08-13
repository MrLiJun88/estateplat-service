function renderGridNumber(value, p, r) {
    return Ext.String.format('{0}', Ext.util.Format.number(value, '0.0000').replace(',', '.'));
}

function renderGridDate(value, p, r) {
    return Ext.String.format('{0}', Ext.Date.dateFormat(new Date(value), 'Y-m-d'));
}

function trim() {

}

function onloadFr(obj) {
    var contentPane = obj.contentWindow.contentPane;
    if (contentPane != null) {
        contentPane.on("afterload", function () {
            setframeHeight(obj);
        });
    }
}

function setframeHeight(obj) {
    var reportFrame = obj;
    // 获得页面中的所有行
    var tr = reportFrame.contentWindow.document.getElementsByTagName("tr");
    //为了避免报表加载结束后出现滚动条现象，这里将报表容器的overflow属性设置为hidden
    //由于在报表容器属性的设置只能在报表计算之后，所以用setTimeout来设置延迟执行时间，如果数据过多，请按照具体情况修改延迟时间
    setTimeout(function () {
        obj.contentWindow.document.getElementById("content-container").style.overflow = "hidden";
    }, 10)
    // 由于报表页面还存在页边距，因此框架高度是大于所有行累计的高度的，这里赋一个初始值以表示边距的大小
    var height = 100;
    for (var i = 0; i < tr.length; i++) {
        //由于报表页面加载完成之后，可能会将单元格也在加载成一个tr，会导致重复计算，这里通过条件判断来获取行的tr
        if (tr[i].id.substring(0, 1) == "r") {
            height = height + tr[i].offsetHeight;
        }
    }
    obj.style.height = height + "px";
}

function parseURL(url) {
    var a = document.createElement('a');
    a.href = url;
    return {
        source: url,
        protocol: a.protocol.replace(':', ''),
        host: a.hostname,
        port: a.port,
        query: a.search,
        params: (function () {
            var ret = {},
                seg = a.search.replace(/^\?/, '').split('&'),
                len = seg.length, i = 0, s;
            for (; i < len; i++) {
                if (!seg[i]) {
                    continue;
                }
                s = seg[i].split('=');
                ret[s[0]] = s[1];
            }
            return ret;

        })(),
        file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
        hash: a.hash.replace('#', ''),
        path: a.pathname.replace(/^([^\/])/, '/$1'),
        relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
        segments: a.pathname.replace(/^\//, '').split('/')
    };
}

//替换myUrl中的同名参数值
function replaceUrlParams(myUrl, newParams) {
    for (var x in newParams) {
        var hasInMyUrlParams = false;
        for (var y in myUrl.params) {
            if (x.toLowerCase() == y.toLowerCase()) {
                myUrl.params[y] = newParams[x];
                hasInMyUrlParams = true;
                break;
            }
        }
        //原来没有的参数则追加
        if (!hasInMyUrlParams) {
            myUrl.params[x] = newParams[x];
        }
    }
    var _result = myUrl.protocol + "://" + myUrl.host + ":" + myUrl.port + myUrl.path + "?";

    for (var p in myUrl.params) {
        _result += (p + "=" + myUrl.params[p] + "&");
    }

    if (_result.substr(_result.length - 1) == "&") {
        _result = _result.substr(0, _result.length - 1);
    }

    if (myUrl.hash != "") {
        _result += "#" + myUrl.hash;
    }
    return _result;
}

function reloadWindow() {
    window.location.reload();
}

$(function () {
    /**
     * serialize object
     * @returns {{}}
     */
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
});

/**
 *rmb小写转大写（小写的值,转大写的字段id）
 */
function changeDx(num, name) {
    var title = "人民币";
    if (f_check_number(num, title)) {
        var strOutput = "",
            strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
        num = (num.value * 100).toString();
        var intPos = num.indexOf('.');
        if (intPos >= 0) {
            num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
        }
        strUnit = strUnit.substr(strUnit.length - num.length);
        for (var i = 0; i < num.length; i++) {
            strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i, 1), 1) + strUnit.substr(i, 1);
        }
        var dx = strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");
        $("#" + name).val(dx);
    }
}

/**
 *rmb小写转大写（小写的值,转大写的字段id）
 */
function changeDxWy(num, name) {
    var title = "人民币";
    if (f_check_number(num, title)) {
        var strOutput = "",
            strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
        num = (num.value * 100 * 10000).toString();
        var intPos = num.indexOf('.');
        if (intPos >= 0) {
            num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
        }
        strUnit = strUnit.substr(strUnit.length - num.length);
        for (var i = 0; i < num.length; i++) {
            strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i, 1), 1) + strUnit.substr(i, 1);
        }
        var dx = strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");
        $("#" + name).val(dx);
    }
}

/* 
 * 判断是否为数字，是则返回true,否则返回false
 */
function f_check_number(obj, code) {
    if (obj && obj.value) {
        if (/^\-?[0-9]*\.?[0-9]*$/.test(obj.value)) {
            return true;
        } else {
            obj.value = "";
            f_alert(code, "请输入数字");
            //obj.focus();
            return false;
        }
    }
    return false;
}

function f_alert(code, str) {
    var msg = "";
    if (code && code != "") {
        msg = code + ":" + str;
    } else {
        msg = str;
    }
    msg = msg.replace(/\\n/g, "\n");
    alert(msg);
}

Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

function getClientHeight() {
    //可见高
    var clientHeight = document.body.clientHeight;//其它浏览器默认值
    if (navigator.userAgent.indexOf("MSIE 6.0") != -1) {
        clientHeight = document.body.clientHeight;
    } else if (navigator.userAgent.indexOf("MSIE") != -1) {
        //IE7 IE8
        clientHeight = document.documentElement.offsetHeight
    }

    if (navigator.userAgent.indexOf("Chrome") != -1) {
        clientHeight = document.body.scrollHeight;
    }

    if (navigator.userAgent.indexOf("Firefox") != -1) {
        clientHeight = document.documentElement.scrollHeight;
    }
    return clientHeight;
}

function openWin(url, name) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

//显示确认对话框
function showConfirmDialog(title, msg, okMethod, okParm, cancelMethod, cancelParm) {
    var comfirmDia = bootbox.dialog({
        message: "<h3><b>" + msg + "</b></h3>",
        title: title,
        buttons: {
            OK: {
                label: "是",
                className: "btn-primary",
                callback: function () {
                    if (okMethod != null && okMethod != "")
                        eval(okMethod + "(" + okParm + ")");
                }
            },
            Cancel: {
                label: "否",
                className: "btn-default",
                callback: function () {
                    comfirmDia.hide();
                    if (cancelMethod != null && cancelMethod != "")
                        eval(cancelMethod + "(" + cancelParm + ")");
                }
            }
        }
    });
}

function showConfirmSeeDialog(title, msg, proid, okMethod, okParm, cancelMethod, cancelParm) {
    var comfirmDia = bootbox.dialog({
        message: '<div class="alert alert-danger"><span style="cursor: pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + proid + '\')" >查看</span>' + msg + '</div>',
        title: '<h4 class="modal-title">' + title + '</h4>',
        buttons: {
            OK: {
                label: "是",
                className: "btn-primary",
                callback: function () {
                    if (okMethod != null && okMethod != "")
                        eval(okMethod + "(" + okParm + ")");
                }
            },
            Cancel: {
                label: "否",
                className: "btn-default",
                callback: function () {
                    comfirmDia.hide();
                    if (cancelMethod != null && cancelMethod != "")
                        eval(cancelMethod + "(" + cancelParm + ")");
                }
            }
        }
    });
}


//提示信息
function tipInfo(msg) {
    $.Prompt(msg, 1500);
    // bootbox.dialog({
    //     message: "<h3><b>" + msg + "</b></h3>",
    //     title: "",
    //     buttons: {
    //         main: {
    //             label: "关闭",
    //             className: "btn-primary"
    //         }
    //     }
    // });
    // return;
}

function showIndexModel(url, title, width, height, fullscreen) {
    window.parent.parent.showModel(url, title, width, height, fullscreen);
}

function hideIndexModel() {
    window.parent.parent.hideModel();
}

function showWindow(url, title, width, height) {
    var ua = navigator.userAgent;
    if (ua.lastIndexOf("MSIE 6.0") != -1) {
        if (ua.lastIndexOf("Windows NT 5.1") != -1) {
            height = (height * 1.0 + 102);
        } else if (ua.lastIndexOf("Windows NT 5.0") != -1) {
            height = (height * 1.0 + 49);
        }
    }
    if (width == null && height == null) {
        width = screen.availWidth - 10;
        height = screen.availHeight - 32;
    }
    try {
        return window.showModalDialog(url, title, "dialogHeight=" + height + "px;dialogWidth=" + width + "px");
    } catch (ex) {
        var winOption = "height=" + height + ",width=" + width + ",top=55,left=200,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes";
        return window.open(url, window, winOption);
    }
}

function showComWindow(url, title) {
    var height = 550;
    var width = 992;
    var ua = navigator.userAgent;
    if (ua.lastIndexOf("MSIE 6.0") != -1) {
        if (ua.lastIndexOf("Windows NT 5.1") != -1) {
            height = (height * 1.0 + 102);
        } else if (ua.lastIndexOf("Windows NT 5.0") != -1) {
            height = (height * 1.0 + 49);
        }
    }
    try {
        return window.showModalDialog(url, title, "dialogHeight=" + height + "px;dialogWidth=" + width + "px");
    } catch (ex) {
        var winOption = "height=" + height + ",width=" + width + ",top=55,left=200,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes";
        return window.open(url, window, winOption);
    }
}

//判断object是否是空对象,空数组,空字符串,null,undefined,如果是则返回false,不是则返回true
//空字符串不包括"(空格)",只特指""
function isNotBlank(object) {
    if (typeof object === "object" && !(object instanceof Array)) {
        var hasProp = false;
        for (var prop in object) {
            hasProp = true;
            break;
        }
        if (hasProp) {
            hasProp = [hasProp];
        } else {
            return false;
        }
        return hasProp;
    }
    return typeof object != "undefined" && object != "";
}

function isBlank(object) {
    return !isNotBlank(object);
}

if (!Array.prototype.forEach) {
    Array.prototype.forEach = function (callback, thisArg) {
        var T, k;
        if (this == null) {
            throw new TypeError(" this is null or not defined");
        }
        var O = Object(this);
        var len = O.length >>> 0; // Hack to convert O.length to a UInt32
        if ({}.toString.call(callback) != "[object Function]") {
            throw new TypeError(callback + " is not a function");
        }
        if (thisArg) {
            T = thisArg;
        }
        k = 0;
        while (k < len) {
            var kValue;
            if (k in O) {
                kValue = O[k];
                callback.call(T, kValue, k, O);
            }
            k++;
        }
    };
}


function addSelectJson(mulSelectJson, nameStr, value, type, isLjz, hsJson) {

    var ljzJson = {};
    ljzJson.name = nameStr;
    ljzJson.value = value;
    ljzJson.type = type;
    ljzJson.ljz = isLjz;
    ljzJson.property = hsJson;
    mulSelectJson[nameStr] = ljzJson;
    $.cookie('lpb_cookie', JSON.stringify(mulSelectJson));
}

function openPostPage(url, paramName, paramValue, type) {

    var html1 = '<form id="queryForm" name="queryForm" method="post" target="_self" action="' + url + '">' +
        '<input type="hidden" id="' + paramName + '" name="' + paramName + '" value="' + paramValue + '"/>' +      //pk_id是一个长度超过3000字符的值
        '</form>';

    if (type == "iframe") {

        document.getElementById('mainiframe').contentWindow.document.write(html1);  //将表单写入iframe中
        document.getElementById('mainiframe').contentWindow.document.getElementById('queryForm').submit();//执行iframe中表单的提交
    } else if (type == "winLocation") {
        window.document.body.innerHTML = html1;
        window.document.forms[0].submit();

    }

}

//ie8 indexof方法识别不了
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (obj) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == obj) {
                return i;
            }
        }
        return -1;
    }
}


function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

function updatePagerIcons(table) {
    var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140',
            'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
        };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container: 'body'});
    $(table).find('.ui-pg-div').tooltip({container: 'body'});
}

function infoMsg(message,hm) {
    if(hm==null){
        hm=1000;
    }
    var boxModel = bootbox.dialog({
        message: '<h5 style="text-align: center">' + message + '</h5>',
        size: 'large',
        onEscape: false,
        backdrop: true,
        closeButton: false
    });
    boxModel.modal('show');
    setTimeout(function () {
        boxModel.modal('hide');
    }, hm);
}