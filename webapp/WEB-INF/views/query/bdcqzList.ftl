<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    .modal-dialog {
        width: 1000px;
    }

        /*高级搜索样式添加 begin*/
        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .btn01 {
        display: inline-block;
        padding: 4px 12px;
        margin-bottom: 0;
        font-size: 14px;
        color: #333333;
        text-align: center;
        vertical-align: middle;
        cursor: pointer;
        background-color: #f2f2f2;
        border: 1px solid #aaa;
        webkit-border-radius: 0px !important;
        -moz-border-radius: 0px !important;
        border-radius: 0px !important;
    }

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
        margin: 0px 5px 0px 0px;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

        /*重写下拉列表高度*/
    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
$(function () {
<#--//搜索事件-->
<#--$("#gjSearchBtn").click(function () {-->

<#--var dcxc = '';-->
<#--if ($("#search_xmmc").val() != '') {-->
<#--dcxc = $("#search_xmmc").val();-->
<#--}-->
<#--var qlr = '';-->
<#--if ($("#qlr").val() != '') {-->
<#--qlr = $("#qlr").val();-->
<#--}-->
<#--var bdcdyh = '';-->
<#--if ($("#bdcdyh").val() != '') {-->
<#--bdcdyh = $("#bdcdyh").val();-->
<#--}-->
<#--var djlx = '';-->
<#--if ($("#djlx").val() != '') {-->
<#--djlx = $("#djlx").val();-->
<#--}-->
<#--var bdcqzh = '';-->
<#--if ($("#bdcqzh").val() != '') {-->
<#--bdcqzh = $("#bdcqzh").val();-->
<#--}-->
<#--var ghyt = '';-->
<#--if ($("#ghyt").val() != '') {-->
<#--ghyt = $("#ghyt").val();-->
<#--}-->
<#--var qszt = '';-->
<#--if ($("#qszt").val() != '') {-->
<#--qszt = $("#qszt").val();-->
<#--}-->
<#--var Url = "${bdcdjUrl}/bdcqz/getBdcqzPagesJson"-->
<#--Url = Url + "?qlr=" + qlr + "&bdcdyh=" + bdcdyh + "&djlx=" + djlx + "&bdcqzh=" + bdcqzh+"&ghyt="+ghyt+"&qszt="+qszt+"&dcxc="+dcxc;-->
<#--Url= encodeURI(Url);-->
<#--var jqgrid = $("#grid-table");-->
<#--jqgrid.setGridParam({url:Url, datatype:'json', page:1});-->
<#--jqgrid.trigger("reloadGrid");//重新加载JqGrid-->
<#--})-->



    //下拉框
    $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据", width:"100%"});
    $(window).on('resize.chosen',function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
    }).trigger('resize.chosen');

    try {
        Capture = document.getElementById("Capture");//根据js的脚本内容，必须先获取object对象
        content = $("#search_xmmc");
    } catch (err) {
        alert("请安装Active X控件CaptureVideo.cab");
    }
    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
    var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject) {
        if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
            $(window).resize(function () {
                $.each($(".moveModel > .modal-dialog"), function () {
                    $(this).css("left", ($(window).width() - $(this).width()) / 2);
                    $(this).css("top", "40px");
                })
            })
        }
    }

    //zwq 回车键搜索
    $('input').focus(function(){
        $('#search_xmmc').keydown(function (event) {
            if (event.keyCode == 13) {
                $("#search").click();
            }
        });

    });

    //时间控件
    $('.date-picker').datepicker({
        autoclose:true,
        todayHighlight:true,
        language:'zh-CN'
    }).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });

    //项目表搜索事件
    $("#search").click(function () {
        var xmmc = $("#search_xmmc").val();
        $("#gjSearchForm")[0].reset();
        var Url = "${bdcdjUrl}/bdcqz/getBdcqzPagesJson?zstype=zs&" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:xmmc});
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/bdcqz/getBdcqzPagesJson?zstype=zs&" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:""});
    });
    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

    //项目高级搜索关闭事件
    $("#proHide").click(function () {
        $("#gjSearchPop").hide();
        $("#gjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });
    //项目高级查询按钮点击事件
    $("#show").click(function () {
        $("#gjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });
});
/* 调用子页面方法  */
function showModal() {
    $('#myModal').show();
    $('#modal-backdrop').show();
}
function hideModal() {
    $('#myModal').hide();
    $('#modal-backdrop').hide();
    $("#myModalFrame").attr("src", "${bdcdjUrl!}/bdcSjgl/toAddBdcxm");
}
var onmessage = function (e) {
    showModal();
};

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
}
function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}

$(function () {
//        var grid_selector = "#grid-table";
//        var pager_selector = "#grid-pager";
//
//        //resize to fit page size
//        $(window).on('resize.jqGrid', function () {
//            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
//        });
//        //resize on sidebar collapse/expand
//        var parent_column = $(grid_selector).closest('[class*="col-"]');
//        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
//            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
//                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
//            }
//        });
    /*   文字水印  */
    $(".watermarkText").watermark();
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });
    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });

    jQuery(grid_selector).jqGrid({
        <#--url:"${bdcdjUrl}/bdcqz/getBdcqzPagesJson?zstype=zs",-->
        url:"",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'ZSID'},
        colNames:['权利人','证件号', '不动产单元号', '申请类型', '不动产权证号', '用途', '权利状态', '查封/抵押/异议状态', '查看', 'ID', 'PROID', 'BDCLX' ,'ZSTYPE'],
        colModel:[
            {name:'QLR', index:'QLR', width:'5%', sortable:false},
            {name:'QLRZJH', index:'QLR', width:'10%', sortable:false},
            {name:'BDCDYH', index:'BDCDYH', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                return value;
            }},
            {name:'SQLX', index:'SQLX', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                $.each(${sqlxListJson!}, function (i, item) {
                    if (item.dm == cellvalue) {
                        value = item.mc;
                    }
                })
                return value;
            }},
            {name:'BDCQZH', index:'BDCQZH', width:'15%', sortable:false},
            {name:'YT', index:'YT', width:'12%', sortable:false},
            {name:'QSZT', index:'QSZT', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue && cellvalue != "0") {
                    return"";
                }
                var value = cellvalue;
                $.each(${qsztListJson!}, function (i, item) {
                    if (item.dm == cellvalue) {
                        value = item.mc;
                    }
                })
                /*var xmzt = rowObject.XMZT;
                if (xmzt == '2') {
                    value = '临时'
                }*/
                if (value == '现势')
                    return '<span class="label label-success">' + value + '</span>&nbsp;';
                else if (value == '历史')
                    return '<span class="label label-gray">' + value + '</span>&nbsp;';
                else if (value == '临时')
                    return '<span class="label label-warning">' + value + '</span>&nbsp;';
            }},
            {name:'CFDYZT', index:'CFDYZT', width:'6%', sortable:false},
            {name:'mydy', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                //zdd 不允许打印证书 return '<div style="margin-left:8px;"> <div title="打印" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-print fa-lg blue"></span></div>&nbsp;&nbsp;<div title="查看收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div></div>'
                return '<div style="margin-left:8px;">' +
                        '<div title="证书" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="dianjizs(\'' + rowObject.ID + '\',\'' + rowObject.ZSID + '\',\'' + rowObject.ZSTYPE + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-file-text fa-lg red"></span></div>' +
                        '<div title="收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' +
                        '<div title="定位" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="locate(\'' + rowObject.ID + '\',\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa  fa-crosshairs fa-lg green"></span></div>' +
                        '<div title="证书关系" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="zsrel(\'' + rowObject.ID + '\',\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-sitemap"></span></div>' +
                        '</div>'
            }
            },
            {name:'ZSID', index:'ZSID', width:'0%', sortable:false, hidden:true},
            {name:'ID', index:'ID', width:'0%', sortable:false, hidden:true},
            {name:'BDCLX', index:'BDCLX', width:'0%', sortable:false, hidden:true},
			{name:'ZSTYPE', index:'ZSTYPE', width:'0%', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly: false,
        multiselect: true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            }, 0);
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getBdcqzQlrxx(data.ID,$(grid_selector), data.ZSID);
                getSdStatus(data.ZSID,data.BDCQZH,$(grid_selector));
                if (data.QSZT != null && data.QSZT.indexOf("历史") < 0)
                    getBdcqzDyCfStatus(data.ID, Trim(data.BDCDYH, 'g'), $(grid_selector), data.ZSID);
            })
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (status) {
                $("#zsid").val(rowid);
            }
        },
        ondblClickRow:function (rowid, index) {
            dianji(rowid);
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+":this.getMonth() + 1, //月份
            "d+":this.getDate(), //日
            "h+":this.getHours(), //小时
            "m+":this.getMinutes(), //分
            "s+":this.getSeconds(), //秒
            "q+":Math.floor((this.getMonth() + 3) / 3), //季度
            "S":this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    //锁定按钮点击事件
    $("#lockZs").click(function () {
        var zsid = $("#zsid").val();
        if (zsid == "") {
            tipInfo("请选择一条要锁定的数据!");
            return false;
        }

        //先验证该条数据是否已被锁定
        var cqzh = $("#grid-table").getCell($("#zsid").val(), "BDCQZH");
        //验证界面上选择的数据，其产权证号是否为空，如是，则返回
        if (cqzh == "" || null == cqzh) {
            alert("要锁定的数据产权证号不能为空！");
            return false;
        }
//        alert(cqzh);

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcqz/checkBdcSjSd",
            data: {zsid: zsid},
            success: function (jsonData) {
                if(jsonData == "false"){
                    alert("该数据已被锁定，请先解锁再进行锁定！");
                    return false;
                } else {
                    $("#gdXzyyPop").show();
                    $(window).trigger('resize.chosen');
                    $(".modal-dialog").css({"_margin-left":"25%"});
                }
            },
            error: function (data) {
                alert("验证数据是否被锁定，操作失败！");
            }
        });

    });

    //限制原因确定按钮点击事件
    $("#gdLockSureBtn").click(function () {
        var xzyy_str = $("#xzyy").val();
        if(xzyy_str==null || xzyy_str==""){
            tipInfo("限制原因不能为空，请输入！");
            return false;
        }
        var data_gd = null;
        var table = null;
        var zsid = $("#zsid").val();
        var cqzh = bdcqzh = $("#grid-table").getCell(zsid, "BDCQZH");
        //组织页面上所选记录的相关字段值
        var bdclx = $("#grid-table").getCell(zsid, "BDCLX");
        var proid = $("#grid-table").getCell(zsid, "ID");
        data_gd = {zsid: zsid, cqzh: cqzh, bdclx: bdclx, proid: proid,xzyy: xzyy_str};
        table = $("#grid-table");

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcqz/lockBdcZsSj",
            dataType: "json",
            data: data_gd,
            success: function (jsonData) {
                alert(jsonData.msg);
                var cellVal = "";
                if (jsonData.flag == "true") {
                    cellVal =
                            '<div title="'+xzyy_str+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                            '<span><img src="${bdcdjUrl!}/static/img/locked.png" width="20px" height="20px" /></span>' +
                            '<span style="font-family: cursive;"> ' + cqzh + '</span>' +
                            '</div>';
                    table.setCell(zsid, "BDCQZH", cellVal);
                }
            },
            error: function (jsonData) {
            }
        });

        $("#gdXzyyPop").hide();
        $("#gdXzyyForm")[0].reset();
//            $(".chosen-select").trigger('chosen:updated');
    });

    //限制原因取消按钮点击事件
    $("#gdLockCancelBtn").click(function () {
        $("#gdXzyyPop").hide();
        $("#gdXzyyForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    //限制原因对话框关闭事件
    $("#proHide1").click(function () {
        $("#gdXzyyPop").hide();
        $("#gdXzyyForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    //解锁按钮点击事件
    $("#unlockZs").click(function () {
        var zsid = $("#zsid").val();
        if (zsid == "") {
            tipInfo("请选择一条要解锁的数据!");
            return false;
        }

        var table = null;
        table = $("#grid-table");

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcqz/UnlockBdcZsSj",
            dataType: "json",
            data: {zsid: zsid},
            success: function (jsonData) {
                alert(jsonData.msg);
                if (jsonData.flag == "true") {
                    table.setCell(zsid, "BDCQZH", jsonData.cqzh);
                }
            },
            error: function (data) {
            }
        });

        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });
});

//lj 获取锁定状态
function getSdStatus(zsid, cqzh, table) {
    var cellVal = "";
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcqz/getXzyy",
        dataType: "json",
        data: {cqzh: cqzh},
        async: false,
        success: function (jsonData) {
            if(jsonData.msg == "false"){
                var xzyy = jsonData.xzyy;
                cellVal = '<div title="'+xzyy+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                        '<span><img src="${bdcdjUrl!}/static/img/locked.png" width="20px" height="20px" /></span>' +
                        '<span style="font-family: cursive;"> ' + cqzh + '</span>' +
                        '</div>';
                table.setCell(zsid, "BDCQZH", cellVal);
            }
        },
        error: function (data) {
        }
    });
}

function Trim(str, is_global) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (is_global.toLowerCase() == "g") {
        result = result.replace(/\s/g, "");
    }
    return result;
}
//lcl 二次查询权利人信息
function getBdcqzQlrxx(proid,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcqz/getBdcqzQlrxx?proid="+proid+"&zsid=" + rowid,
        dataType:"json",
        success:function (result) {
            table.setCell(rowid, "QLR", result.QLR);
            table.setCell(rowid, "QLRZJH", result.QLRZJH);
        }
    });
}

function getBdcqzDyCfStatus(proid, bdcdyh, table, rowid) {
    $.ajax({
        type:"POST",
        url:"${bdcdjUrl}/bdcqz/getBdcqzDyCfStatus?proid=" + proid + "&zsid=" + rowid + "&bdcdyh=" + bdcdyh,
        dataType:"json",
        success:function (result) {
            var cellVal = "";
            if (!result.cf) {
                cellVal += '<span class="label label-danger">查封</span>';
            }
            if (!result.dy) {
                cellVal += '<span class="label label-warning">抵押</span>';
            }
            if (!result.yy) {
                cellVal += '<span class="label label-warning">异议</span>';
            }
            if (result.dy && result.cf && result.yy) {
                cellVal += '<span class="label label-success">正常</span>';
            }
            table.setCell(rowid, "CFDYZT", cellVal);
        }
    });
}
function dianji(id) {
    if (id != "") {
        var proid = encodeURI(encodeURI(id));
        var url = "${bdcdjUrl}/bdcZsResource/printZs?zsid=" + id;
        openWin(url);
    }
}

//点击证书按钮，展示证书
function dianjizs(id, zsid , zstype) {
    if (zsid != "" && id != "") {
        var data = $("#grid-table").getRowData(zsid);
        var qszt = data.QSZT;
        var cfdyzt = data.CFDYZT;
        var iszs = data.BDCQZH;
        if (qszt != '') {
            if (qszt.indexOf('历史') > 0) {
                qszt = '1';
            } else {
                qszt = '0';
            }

        }
        if (cfdyzt != '') {
            if (cfdyzt.indexOf('查封') > 0) {
                cfdyzt = '2';
            } else if (cfdyzt.indexOf('抵押') > 0) {
                cfdyzt = '3';
            } else {
                cfdyzt = '0';
            }


        }
        if (iszs != '') {
            if (iszs.indexOf('证明') > 0) {
                iszs = '0';
            } else {
                iszs = '1';
            }
        }

        var url = "${bdcdjUrl}/bdcZsResource/displayZsZm?proid=" + id + "&qszt=" + qszt + "&cfdyzt=" + cfdyzt + "&iszs=" + iszs + "&zsid=" + zsid + "&zstype="+ zstype;
        openWin(url);
    }
}

//点击定位按钮，展示定位图
function locate(id, bdcdyh) {
    if (id != "") {
        var url = "${bdcdjUrl}/bdzDyMap?proid=" + id + "&bdcdyh=" + bdcdyh;
        openWin(url)
//        window.open(url);
    }
}
//点击证书关系
function zsrel(id, bdcdyh) {
    if (id != "") {
//        var url = "/server/bdcZsRel?proid=" + id + "&bdcdyh=" + bdcdyh;
        var url = "/workflowPlugin/index.html?url=" + "${bdcdjUrl!}" + "/bdcZsRel/getAllXmRelXml?proid=" + id + "&bdcdyh=" + bdcdyh;
        openWin(url);
    }
}


//修改项目信息的函数
function EditXm(id) {
    if (id != "") {
        var proid = encodeURI(encodeURI(id));
        var url = "${bdcdjUrl}/bdcZsResource/printZs?zsid=" + id;
        openWin(url);
    }
}
//查看收件材料
function LookFile(id) {
    if (id != "") {
        var proid = encodeURI(encodeURI(id));
        var url = "${path_platform!}/fc.action?proid=" + id + "&readOnly=true";
        openWin(url);
    }
}

function openWin(url, name) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}
function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">

    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入身份证号/权利人/不动产单元号/不动产权证号/用途/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" onclick="openDevices_onclick(0)">扫描</button>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>

        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="lockZs">
                        <i class="ace-icon fa fa-search"></i>
                        <span>锁定</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="unlockZs">
                        <i class="ace-icon fa fa-search"></i>
                        <span>解锁</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
    <!--限制原因-->
    <div class="Pop-upBox moveModel" style="display: none;" id="gdXzyyPop">
        <div class="modal-dialog gjSearchPop-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">
                        <i class="ace-icon fa fa-search bigger-110"></i>
                        请输入限制原因
                    </h4>
                    <button type="button" id="proHide1" class="proHide">
                        <i class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="bootbox-body" style="background: #fafafa;">
                    <form class="form advancedSearchTable" id="gdXzyyForm">
                        <div class="row">
                            <div class="col-xs-2">
                                <label>限制原因：</label>
                            </div>
                            <div class="col-xs-10">
                                <input type="text" name="xzyy" id="xzyy" class="form-control">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modelFooter">
                    <button type="button" class="btn btn-sm btn-primary" id="gdLockSureBtn">确定</button>
                    <button type="button" class="btn btn-sm btn-primary" id="gdLockCancelBtn">取消</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <#list bdcqzGjssOrderList as bdcqzGjss>
                        <#if bdcqzGjss == 'qlr'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权利人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="qlr" class="form-control">
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='bdcdyh'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产单元号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcdyh" class="form-control">
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='sqlx'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>申请类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="sqlx" class="form-control chosen-select" data-placeholder=" ">
                                    <option></option>
                                    <#list sqlxList as sqlx>
                                        <option value="${sqlx.dm}">${sqlx.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='bdcqzh'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产权证号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcqzh" class="form-control">
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='ghyt'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>规划用途：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="ghyt" class="form-control chosen-select" data-placeholder=" ">
                                    <option></option>
                                    <#list fwytList as fwyt>
                                        <option value="${fwyt.dm}">${fwyt.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='qszt'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权属状态：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="qszt" class="form-control">
                                    <option></option>
                                    <#list qsztList as qszt>
                                        <option value="${qszt.dm}">${qszt.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='fzrqBegin'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>发证日期(起)：</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="fzqssj"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='fzrqEnd'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>发证日期(至)：</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="fzjssj"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcqzGjss=='szr'>
                            <#if (bdcqzGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>缮证人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="szr" class="form-control">
                            </div>
                            <#if (bdcqzGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        </#if>
                    </#list>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
    </div>
</div>
<input type="hidden" id="zsid" name="zsid"/>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
