<@com.html title="证书编号管理" import="ace,puclic">
<style>
        /*移动modal样式*/
    #logSearchPop .modal-dialog {
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

        /*高级搜索的样式修改*/
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

        /*去掉表格横向滚动条*/
        /*.ui-jqgrid-bdiv{
            overflow-x: hidden!important;
        }*/

        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
    }

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }

    .form .row .col-xs-4, .col-xs-10 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

        /*日期表单样式*/
    .dropdown-menu {
        z-index: 10000 !important;
    }

    .input-icon {
        width: 100%;
    }
</style>
<script type="text/javascript">
dyhLoadComplete = function () {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
        enableTooltips(table);
    }, 0);
    //清空
    $("#bdcdyh").val("");
    //如果10条设置宽度为auto,如果少于7条就设置固定高度
    if ($(table).jqGrid("getRowData").length == $rownum) {
        $(table).jqGrid("setGridHeight", "auto");
    } else {
        $(table).jqGrid("setGridHeight", $pageHight);
    }
    //去掉遮罩
    setTimeout($.unblockUI, 10);
};


$(function () {
    //初始化日志表格
    $(".watermarkText").css("height", "35px");
    logTableInit();
    /*   文字水印  */
    $(".watermarkText").watermark();

    //绑定回车键
    $('#searchInfo').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#search").click();
        }
    });

    //查询按钮点击事件
    $("#search").click(function () {
        var searchInfo = $("#searchInfo").val();
        var logUrl = "${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage?" + $("#logSearchForm").serialize();
        tableReload("log-grid-table", logUrl, {hhSearch:searchInfo});
    })
    //日志表高级查询的搜索按钮事件
    $("#logSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage?" + $("#logSearchForm").serialize();
        tableReload("log-grid-table", Url, {hhSearch:""});
    })
    //日志高级搜索关闭事件
    $("#proHide").click(function () {
        $("#logSearchPop").hide();
        $("#logSearchForm")[0].reset();
    });
    //日志高级查询按钮点击事件
    $("#logShow").click(function () {
        $("#logSearchPop").show();
        $("#logAdd").hide();
        $("#logLq").hide();
        $("#logSearch").show();
        $("#logSearchBtn").show();
        $("#logAddBtn").hide();
        $("#logLqBtn").hide();
        $("#saveZsBh").hide();
        $("#xhZsBh").hide();
        $("#edit").hide();
        $("#xhBh").hide();
        $("#titlebz").html("高级搜索");
    });

    $("#saveZsBh").click(function () {
        $.ajax({
            type:"POST",
            url:"${bdcdjUrl}/zsBhGlNew/zfZsBh?" + $("#editForm").serialize(),
            dataType:"text",
            success:function (data) {
                if (data != null && data != '') {
                    alert(data);
                    $("#logSearchPop").hide();
                    var searchInfo = $("#searchInfo").val();
                    var logUrl = "${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage?" + $("#logSearchForm").serialize();
                    tableReload("log-grid-table", logUrl, {hhSearch:searchInfo}, '', dyhLoadComplete);
                }
            }
        });
    });

    $("#xhZsBh").click(function () {
        $.ajax({
            type:"POST",
            url:"${bdcdjUrl}/zsBhGlNew/xhZsBh?" + $("#xhForm").serialize(),
            dataType:"text",
            success:function (data) {
                if (data != null && data != '') {
                    alert(data);
                    $("#logSearchPop").hide();
                    var searchInfo = $("#searchInfo").val();
                    var logUrl = "${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage?" + $("#logSearchForm").serialize();
                    tableReload("log-grid-table", logUrl, {hhSearch:searchInfo}, '', dyhLoadComplete);
                }
            }
        });
    });

    $("#addBh").click(function () {
        $("#logSearchPop").show();
        $("#logLq").hide();
        $("#logAdd").show();
        $("#logSearch").hide();
        $("#logSearchBtn").hide();
        $("#logLqBtn").hide();
        $("#logAddBtn").show();
        $("#saveZsBh").hide();
        $("#xhZsBh").hide();
        $("#edit").hide();
        $("#xhBh").hide();
        $("#titlebz").html("新增编号");
    });
    $("#lqBh").click(function () {
        $("#logSearchPop").show();
        $("#logLq").show();
        $("#logAdd").hide();
        $("#logSearch").hide();
        $("#logSearchBtn").hide();
        $("#logLqBtn").show();
        $("#logAddBtn").hide();
        $("#saveZsBh").hide();
        $("#xhZsBh").hide();
        $("#edit").hide();
        $("#xhBh").hide();
        $("#titlebz").html("领用编号");
    });
    $("#logAddBtn").click(function () {
        $.blockUI({message: "编号增加中，请稍等…"});
        var qsbh = $("#qsbh").val();
        var jsbh = $("#jsbh").val();
        var xh =  $("#xh").val();
        if (qsbh == "" || jsbh == "" || xh == "") {
            alert("证书编号或箱号不能为空");
            setTimeout($.unblockUI, 10);
            return;
        }
        if (isNaN(qsbh) || isNaN(jsbh) || isNaN(xh)) {
            alert("证书编号或箱号请输入数字");
            setTimeout($.unblockUI, 10);
            return;
        } else {
            if (qsbh.length > 9 || jsbh.length > 9) {
                alert("输入的证书编号不能大于9位");
                setTimeout($.unblockUI, 10);
                return;
            }
        }
        $.ajax({
            type:"POST",
            url:"${bdcdjUrl}/zsBhGlNew/saveZsBh?" + $("#addForm").serialize(),
            dataType:"text",
            success:function (result) {
                if (result != null && result != '') {
                    alert(result);
                    $("#logSearchPop").hide();
                    setTimeout($.unblockUI, 10);
                    var searchInfo = $("#searchInfo").val();
                    var logUrl = "${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage?" + $("#logSearchForm").serialize();
                    tableReload("log-grid-table", logUrl, {hhSearch:searchInfo}, '', dyhLoadComplete);
                }
            }
        });
    });

    $("#logLqBtn").click(function () {
        $.blockUI({message: "编号领取中，请稍等…"});
        var qsxh = $("#qsxh").val();
        var jsxh = $("#jsxh").val();
        var lqdw = $("#lqdw").val();
        var dwdm=$("#dwdm").val();
        if(lqdw == "" || lqdw == ""){
            alert("请选择领取部门");
            setTimeout($.unblockUI, 10);
            return;
        }
        if (qsxh == "" || jsxh == "" ) {
            alert("箱号不能为空");
            setTimeout($.unblockUI, 10);
            return;
        }
        if (isNaN(qsxh) || isNaN(jsxh)) {
            alert("箱号请输入数字");
            setTimeout($.unblockUI, 10);
            return;
        }
        $.ajax({
            type:"POST",
            url:"${bdcdjUrl}/zsBhGlNew/lqZsBh?" + $("#lqForm").serialize(),
            dataType:"text",
            success:function (result) {
                if (result != null && result != '') {
                    if(result == "success"){
                        alert("成功");
                        $("#logSearchPop").hide();
                        var url = "${reportUrl!}/ReportServer?reportlet=/edit/bdc_zsbh_lymxb.cpt&op=write&qsxh="+qsxh+"&jsxh="+jsxh+"&dwdm="+dwdm;
                        openWin(url);
                        setTimeout($.unblockUI, 10);
                    }else {
                        alert(result);
                        $("#logSearchPop").hide();
                        setTimeout($.unblockUI, 10);
                        var searchInfo = $("#searchInfo").val();
                        var logUrl = "${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage?" + $("#logSearchForm").serialize();
                        tableReload("log-grid-table", logUrl, {hhSearch: searchInfo}, '', dyhLoadComplete);
                    }
                }
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

    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".logSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        var contentWidth;
        if ($("#mainContent").width() > 0) {
            contentWidth = $("#mainContent").width();
        }
        $("#log-grid-table").jqGrid('setGridWidth', contentWidth);
    });
})

function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

function tableReload(table, Url, data, colModel, loadComplete) {
    var index = 0;
    var jqgrid = $("#" + table);
    if (colModel == '' && loadComplete == '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    } else if (loadComplete == '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel});
    } else if (loadComplete != '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel, loadComplete:loadComplete});
    }
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}


function getLqrByLqdw(lqdw) {
    if(lqdw == null || lqdw =="")
        lqdw = $("#lqdw  option:selected").val();
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/zsBhGlNew/getLqrByLqdw",
        data: {lqdw:lqdw},
        dataType: "json",
        success: function (result) {
            if (result != null && result != '') {
                $("#lqr").html("");
                $.each(result, function (index, data) {
                    $("#lqr").append('<option value="' + data.userId + '" >' + data.userName + '</option>');
                });
            }
        },
        error: function (data) {
        }
    });




}

function logTableInit() {
    var grid_selector = "#log-grid-table";
    var pager_selector = "#log-grid-pager";
    //resize to fit page size
    /* $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
    });*/
    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        url:"${bdcdjUrl}/zsBhGlNew/getBdcZsBhListByPage",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'LOGID'},
        colNames:['证书类型', '证书编号', "使用情况", "创建人", "创建时间", "领取人",<#if '${isedit!}'='true'>"操作"</#if>],
        colModel:[
            {name:'zslx', index:'zslx', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                if (value == "zs")
                    return "不动产证书";
                else if (value == "zms")
                    return "不动产证明书";
            }},
            {name:'zsbh', index:'zsbh', width:'25%', sortable:false},
            {name:'syqk', index:'syqk', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return "";
                }
                var value = cellvalue;
                if (value == "6")
                    return "未领用";
                if (value == "0")
                    return "未使用";
                else if (value == "1")
                    return "已打证";
                else if (value == "2")
                    return "作废";
                else if (value == "3")
                    return "已使用";
                else if (value == "4")
                    return "遗失";
                else if (value == "5")
                    return "销毁";
                else if (value == "7")
                    return "其他";
            }},
            {name:'cjr', index:'cjr', width:'15%', sortable:false},
            {name:'cjsj', index:'cjsj', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                var data = new Date(value).Format("yyyy年MM月dd日");
                return data;
            }},
            {name:'lqr', index:'lqr', width:'15%', sortable:false}<#if '${isedit!}'='true'>,
                {name:'mydy', index:'', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<div title="作废遗失"  style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="zfZsbh(\'' + rowObject.zsbhid + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div>'+
                            '<div title="销毁"  style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="xhZsbh(\'' + rowObject.zsbhid + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div></div>'
                }
                }
            </#if>
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        rownumbers:true,
        rownumWidth:50,
        multiboxonly:false,
        multiselect:false,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}
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

function zfZsbh(zsbhid) {
    $.ajax({
        type:"get",
        url:"${bdcdjUrl}/zsBhGlNew/organizeZsbh?zsbhid=" + zsbhid,
        dataType:"json",
        success:function (data) {
            if (data != null && data != '') {
                $("#logSearchPop").show();
                $("#logAdd").hide();
                $("#logLq").hide();
                $("#logSearch").hide();
                $("#logSearchBtn").hide();
                $("#logAddBtn").hide();
                $("#logLqBtn").hide();
                $("#saveZsBh").show();
                $("#xhZsBh").hide();
                $("#edit").show();
                $("#xhBh").hide();
                $("#titlebz").html("作废遗失");
//                var bdczsbh = $.parseJSON(data);
                $("#zsbh").val(data.zsbh);
                $("#zszt").find("option[value='" + data.zszt + "']").attr("selected", true);
                $("#bdcqzh").val(data.bdcqzh);
                if (data.bfyy != "" && data.bfyy != "null")
                    $("#bfyy").val(data.bfyy);
                if (data.ysyy != "" && data.ysyy != "null")
                    $("#bfyy").val(data.ysyy);
                $("#zsbhid").val(data.zsbhid);
            }
        }
    });
}
function xhZsbh(zsbhid) {
    $.ajax({
        type:"get",
        url:"${bdcdjUrl}/zsBhGlNew/organizeZsbh?zsbhid=" + zsbhid,
        dataType:"json",
        success:function (data) {
            if (data != null && data != '') {
                $("#logSearchPop").show();
                $("#logAdd").hide();
                $("#logLq").hide();
                $("#logSearch").hide();
                $("#logSearchBtn").hide();
                $("#logAddBtn").hide();
                $("#logLqBtn").hide();
                $("#saveZsBh").hide();
                $("#xhZsBh").show();
                $("#edit").hide();
                $("#xhBh").show();
                $("#titlebz").html("销毁");
                debugger;
                $("#xh_zsbh").val(data.zsbh);
                $("#xh_zszt").find("option[value='" + data.zszt + "']").attr("selected", true);
                $("#xh_bdcqzh").val(data.bdcqzh);
                if (data.xhjzr != "" && data.xhjzr != "null")
                    $("#xhjzr").val(data.xhjzr);
                $("#xhzsbhid").val(data.zsbhid);
            }
        }
    });
}
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="searchInfo"
                               data-watermark="请输入年份/证书类型/证书编号/创建人/领取人">
                    </td>
                    <td class="Search">
                        <a href="#" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="logShow">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="addBh">
                        <i class="ace-icon fa fa-file-o bigger-130"></i>
                        <span>新增编号</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="lqBh">
                        <i class="ace-icon fa fa-file-o bigger-130"></i>
                        <span>领用</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="log-grid-table"></table>
        <div id="log-grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="logSearchPop">
    <div class="modal-dialog logSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                </i><h4 class="modal-title" id="titlebz">高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" id="logSearch" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="logSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>起始日期：</label>
                        </div>
                        <div class="col-xs-4">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="qsrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                        </div>
                        <div class="col-xs-2">
                            <label>结束日期：</label>
                        </div>
                        <div class="col-xs-4">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="jsrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>起始编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qsbh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>结束编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jsbh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>创建人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cjr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>领取人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lqr" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证书类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="zslx" class="form-control">
                                <option value="">全部</option>
                                <option value="zs">不动产证书</option>
                                <option value="zms">不动产证明书</option>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>使用情况：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="syqk" class="form-control">
                                <option value="">全部</option>
                                <option value="6">未领用</option>
                                <option value="0">未使用</option>
                                <option value="1">已打证</option>
                                <option value="3">已使用</option>
                                <option value="2">作废</option>
                                <option value="4">遗失</option>
                                <option value="5">销毁</option>
                                <option value="6">其他</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>行政区名称：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="dwdm" id="dwdm" class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" ">
                                <#list dwxxList as dwxx>
                                    <option value="${dwxx.dwdm!}">${dwxx.dwmc!}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>部门：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="organId" class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" ">
                                <option value="">全部</option>
                                <#list organVoList as pforgan>
                                    <option value="${pforgan.organId!}">${pforgan.organName!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="bootbox-body" id="logAdd" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="addForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>起始编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id='qsbh' name="qsbh" class="form-control watermarkText"
                                   data-watermark="如：100">
                        </div>
                        <div class="col-xs-2">
                            <label>结束编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id='jsbh' name="jsbh" class="form-control watermarkText"
                                   data-watermark="如：200">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>行政区名称：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="dwdm" class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" ">
                                <#list dwxxList as dwxx>
                                    <option value="${dwxx.dwdm!}">${dwxx.dwmc!}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>证书类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="zslx" class="form-control">
                                <option value="zs">不动产证书</option>
                                <option value="zms">不动产证明书</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>箱号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id='xh' name="xh" class="form-control watermarkText">
                        </div>
                        <div class="col-xs-2">
                            <label>生产厂商：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id='sccs' name="sccs" class="form-control watermarkText">
                        </div>
                    </div>
                </form>
            </div>
            <div class="bootbox-body" id="logLq" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="lqForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>起始箱号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id='qsxh' name="qsxh" class="form-control watermarkText"
                                   data-watermark="如：1">
                        </div>
                        <div class="col-xs-2">
                            <label>结束箱号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id='jsxh' name="jsxh" class="form-control watermarkText"
                                   data-watermark="如：2">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>行政区名称：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="dwdm" class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" ">
                                <#list dwxxList as dwxx>
                                    <option value="${dwxx.dwdm!}">${dwxx.dwmc!}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>证书类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="zslx" class="form-control">
                                <option value="zs">不动产证书</option>
                                <option value="zms">不动产证明书</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>领取部门：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="lqdw" id="lqdw" class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" " onchange="getLqrByLqdw()">
                                <option value="">全部</option>
                                <#list organVoList as pforgan>
                                    <option value="${pforgan.organName!}">${pforgan.organName!}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>领取人：</label>
                        </div>
                        <div class="col-xs-4">
                                <select id="lqr" name="lqrid" class="form-control"  required="true" class="chosen-select " >
                                    <#--<#list lqrList as lqr>-->
                                        <option value=""></option>
                                    <#--</#list>-->
                                </select>
                        </div>

                    </div>
                </form>
            </div>
            <div class="bootbox-body" id="edit" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="editForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证书编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="hidden" id='zsbhid' name="zsbhid">
                            <input type="text" id='zsbh' name="zsbh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产证号：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" id='bdcqzh' name="bdcqzh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>使用状态：</label>
                        </div>
                        <div class="col-xs-10">
                            <select id='zszt' name='zszt' class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" ">
                                <option value="2">报废</option>
                                <option value="4">遗失</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>原因：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" id='bfyy' name="bfyy" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="bootbox-body" id="xhBh" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="xhForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证书编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="hidden" id='xhzsbhid' name="zsbhid">
                            <input type="text" id='xh_zsbh' name="zsbh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产证号：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" id='xh_bdcqzh' name="bdcqzh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>使用状态：</label>
                        </div>
                        <div class="col-xs-10">
                            <select id='xh_zszt' name='zszt' class="form-control" required="true" class="chosen-select "
                                    data-placeholder=" ">
                                <option value="5">销毁</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>销毁见证人：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" id='xhjzr' name="xhjzr" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="logSearchBtn">搜索</button>
                <button type="button" class="btn btn-sm btn-primary" id="logAddBtn">新增</button>
                <button type="button" class="btn btn-sm btn-primary" id="logLqBtn">领取</button>
                <button type="button" class="btn btn-sm btn-primary" id="saveZsBh">保存</button>
                <button type="button" class="btn btn-sm btn-primary" id="xhZsBh">销毁</button>
            </div>
        </div>

    </div>
</div>
</div>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>