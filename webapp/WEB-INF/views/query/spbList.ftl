<@com.html title="不动产登记业务管理系统" import="ace">
<style xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
    .modal-dialog {
        width: 1000px;
    }

        /*高级搜索样式添加 begin*/
    .AdvancedSearchForm {
        position: absolute;
        top: 10px;
        left: 48px;
        z-index: 9999;
        display: none;
    }

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

    .Advanced .modal-backdrop {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1;
        background-color: #000;
        opacity: 0.5;
        filter: alpha(opacity = 50);
        display: none;
    }

    .Advanced .AdvancedLab {
        display: block;
        margin: 0;
        background: #f5f5f5;
        font-size: 12px;
        border-top: 1px solid #ddd;
        border-left: 1px solid #ddd;
        border-right: 1px solid #ddd;
        padding: 0px 20px 10px 20px;
        position: absolute;
        top: -57px;
        left: 486px;
        z-index: 3;
        width: 90px;
        line-height: 25px;
    }

    .Advanced {
        position: relative;
        margin: 0px 0px 10px 0px;
    }

    .AdvancedSearchForm .form-base {
        padding: 20px 20px 20px 20px;
        border: 1px solid #ddd;
        background: #f5f5f5;
        width: 623px;
        position: absolute;
        top: -22px;
        left: -47px;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
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

        /*高级搜索样式添加 end*/
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

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

</style>
<script type="text/javascript">
/*   文字水印  */
$(function () {
    //搜索事件
<#--$(".search").click(function () {-->

<#--var dcxc = '';-->
<#--if ($("#search_xmmc").val() != '') {-->
<#--dcxc = $("#search_xmmc").val();-->
<#--}-->
<#--var qlr = '';-->
<#--if ($("#qlr").val() != '') {-->
<#--qlr = $("#qlr").val();-->
<#--}-->
<#--var ywr = '';-->
<#--if ($("#ywr").val() != '') {-->
<#--ywr = $("#ywr").val();-->
<#--}-->
<#--var bdcdyh = '';-->
<#--if ($("#bdcdyh").val() != '') {-->
<#--bdcdyh = $("#bdcdyh").val();-->
<#--}-->
<#--var djlx = '';-->
<#--if ($("#djlx").val() != '') {-->
<#--djlx = $("#djlx").val();-->
<#--}-->
<#--var qllx = '';-->
<#--if ($("#qllx").val() != '') {-->
<#--qllx = $("#qllx").val();-->
<#--}-->
<#--var sqzsbs = '';-->
<#--if ($("#sqzsbs").val() != '') {-->
<#--sqzsbs = $("#sqzsbs").val();-->
<#--}-->
<#--var sqfbcz = '';-->
<#--if ($("#sqfbcz").val() != '') {-->
<#--sqfbcz = $("#sqfbcz").val();-->
<#--}-->
<#--var zl = '';-->
<#--if ($("#zl").val() != '') {-->
<#--zl = $("#zl").val();-->
<#--}-->
<#--var Url = "${bdcdjUrl}/spb/getSpbPagesJson"-->
<#--Url = Url + "?qlr=" + qlr +"&ywr="+ywr+ "&bdcdyh=" + bdcdyh + "&djlx=" + djlx + "&qllx=" + qllx+"&sqzsbs="+sqzsbs+"&sqfbcz="+sqfbcz+"&zl="+zl+"&dcxc="+dcxc;-->
<#--Url= encodeURI(Url);-->
<#--var jqgrid = $("#grid-table");-->
<#--jqgrid.setGridParam({url:Url, datatype:'json', page:1});-->
<#--jqgrid.trigger("reloadGrid");//重新加载JqGrid-->
<#--})-->
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

    $(".watermarkText").watermark();

    //绑定回车键
    $('#search_xmmc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#search").click();
        }
    });

    //项目表搜索事件
    $("#search").click(function () {
        var xmmc = $("#search_xmmc").val();
        var Url = "${bdcdjUrl}/spb/getSpbPagesJson?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:xmmc});
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/spb/getSpbPagesJson?" + $("#gjSearchForm").serialize();
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

//        $("#hide").click(function () {
//            $(".SearchFloat").hide();
//        });
//        $("#show").click(function () {
//            $(".SearchFloat").show();
//        });
    //项目高级搜索关闭事件
    $("#proHide").click(function () {
        $("#gjSearchPop").hide();
        $("#gjSearchForm")[0].reset();
    });
    //项目高级查询按钮点击事件
    $("#show").click(function () {
        $("#gjSearchPop").show();
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
        url:"${bdcdjUrl}/spb/getSpbPagesJson",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'ID'},
        colNames:["权利人", '义务人', '不动产单元号', '坐落', '申请类型', '权利类型', '申请证书版式', '申请分别持证', '查看'],
        colModel:[
            {name:'QLR', index:'QLR', width:'6%', sortable:false},
            {name:'YWR', index:'YWR', width:'6%', sortable:false},
            {name:'BDCDYH', index:'BDCDYH', width:'20%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                var value = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                return value;
            }},
            {name:'ZL', index:'ZL', width:'12%', sortable:false},
            {name:'SQLX', index:'SQLX', width:'17%', sortable:false, formatter:function (cellvalue, options, rowObject) {
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
            {name:'QLLX', index:'QLLX', width:'13%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                $.each(${qlListJson!}, function (i, item) {
                    if (item.dm == cellvalue) {
                        value = item.mc;
                    }
                })
                return value;
            }},
            {name:'SQZSBS', index:'SQZSBS', width:'6%', sortable:false},
            {name:'SQFBCZ', index:'SQFBCZ', width:'6%', sortable:false},
            {name:'myac', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<div style="margin-left:8px;"> <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="printCk(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>' +
                        '</div>'
            }
            }
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:true,
        multiselect:false,
        /*rownumbers:true,*/
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
        },
        ondblClickRow:function (rowid) {
            printCk(rowid);
        },
        onCellSelect:function (rowid) {

        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
    $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
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
});


//打印项目信息的函数
function printCk(id) {
    if (id != "") {
        var proid = encodeURI(encodeURI(id));
        openWin("${reportUrl}/ReportServer?reportlet=print%2Fbdc_spb.cpt&op=write&proid=" + proid);

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
                               data-watermark="请输入权利人/义务人/不动产权单元号/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
    <#--<div class="Advanced">-->
    <#--<div class="AdvancedSearchForm SearchFloat" style="display: none;">-->
    <#--<h3 class="AdvancedLab">高级搜索</h3>-->

    <#--<form class="form-base">-->
    <#--<div class="row-fluid">-->
    <#--<div class="col-xs-12">-->
    <#--<div class="HasOptional">-->
    <#--<table cellpadding="0" cellspacing="0" border="0">-->
    <#--<tr>-->
    <#--<td>-->
    <#--权利人：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="qlr">-->
    <#--</td>-->
    <#--<td>-->
    <#--义务人：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="ywr">-->
    <#--</td>-->
    <#--</tr>-->
    <#--<tr>-->
    <#--<td>-->
    <#--不动产单元：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="bdcdyh">-->
    <#--</td>-->
    <#--<td>-->
    <#--登记类型：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="djlx">-->
    <#--</td>-->
    <#--</tr>-->
    <#--<tr>-->
    <#--<td>-->
    <#--权利类型：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="qllx">-->
    <#--</td>-->
    <#--<td>-->
    <#--申请证书版式：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="sqzsbs">-->
    <#--</td>-->
    <#--</tr>-->
    <#--<tr>-->
    <#--<td>-->
    <#--申请分别特征：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="sqfbcz">-->
    <#--</td>-->
    <#--<td>-->
    <#--坐落：-->
    <#--</td>-->
    <#--<td>-->
    <#--<input type="text" class="SSinput" id="zl">-->
    <#--</td>-->
    <#--</tr>-->
    <#--</table>-->
    <#--</div>-->
    <#--</div>-->
    <#--</div>-->
    <#--<div class="row-fluid ">-->
    <#--<div class="span10 offset2">-->
    <#--<button type="submit" class="btn01 btn01-primary search"><i-->
    <#--class="icon-search icon-white"></i>搜索-->
    <#--</button>-->
    <#--</div>-->
    <#--</div>-->
    <#--</form>-->
    <#--</div>-->
    <#--<div class="modal-backdrop SearchFloat" id="hide" style="display: none;"></div>-->
    <#--</div>-->

        <table id="grid-table"></table>
        <div id="grid-pager"></div>
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
                    <#list sqspGjssOrderList as sqspGjss>
                        <#if sqspGjss == 'qlr'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权利人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="qlr" class="form-control">
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='ywr'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>义务人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="ywr" class="form-control">
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='bdcdy'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产单元：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcdyh" class="form-control">
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='sqlx'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>申请类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="sqlx" class="form-control">
                                    <option></option>
                                    <#list sqList as sqlx>
                                        <option value="${sqlx.dm}">${sqlx.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='qllx'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权利类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="qllx" class="form-control">
                                    <option></option>
                                    <#list qlList as qllx>
                                        <option value="${qllx.dm}">${qllx.mc}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='zl'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zl" class="form-control">
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='sqzsbs'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>申请证书版式：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="sqzsbs" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="单一版">单一版</option>
                                    <option value="集成版">集成版</option>
                                </select>
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   sqspGjss=='sqfbcz'>
                            <#if (sqspGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>申请分别持证：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="sqfbcz" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="否">否</option>
                                    <option value="是">是</option>
                                </select>
                            </div>
                            <#if (sqspGjss_index + 1) % 2 == 0>
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
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
