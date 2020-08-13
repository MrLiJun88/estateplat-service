<@com.html title="不动产单元查询" import="ace,public">
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
    //项目表搜索事件
    $("#search").click(function () {
        var xmmc = $("#search_xmmc").val();
        $("#gjSearchForm")[0].reset();
        var Url = "${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:xmmc});
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson?" + $("#gjSearchForm").serialize();
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
        url:"${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'BDCDYH'},
        colNames:[ '不动产类型','不动产单元号',"权利人", '坐落', '权利状态', '查封/抵押/异议状态','历史关系', 'BDCDYH'],
        colModel:[
            {name:'BDCLX', index:'BDCLX', width:'10%', sortable:false},
            {name:'BDCDYH', index:'BDCDYH', width:'25%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                return value;
            }},
            {name:'QLR', index:'QLR', width:'10%', sortable:false},
            {name:'ZL', index:'ZL', width:'30%', sortable:false},
            {name:'QSZT', index:'QSZT', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {



                if (cellvalue == '1')
                    return '<span class="label label-success">' + '现势' + '</span>&nbsp;';
                else if (cellvalue == '2')
                    return '<span class="label label-gray">' + '历史' + '</span>&nbsp;';
                else if (cellvalue == '0')
                    return '<span class="label label-warning">' + '临时' + '</span>&nbsp;';
            }},
            {name:'ZT', index:'ZT', width:'15%', sortable:false},
            {name:'mydy', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                //zdd 不允许打印证书 return '<div style="margin-left:8px;"> <div title="打印" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-print fa-lg blue"></span></div>&nbsp;&nbsp;<div title="查看收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div></div>'
                return '<div style="margin-left:8px;">' +
                        '<div title="历史关系" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="hisRel(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-sitemap"></span></div>' +
                        '</div>'
            }
            },
            {name:'BDCDYH', index:'BDCDYH', width:'0%', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
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
                getBdcdyhQlxx(data.BDCDYH, $(grid_selector), data.BDCDYH);
                getBdcdyhxx(data.BDCDYH, $(grid_selector), data.BDCDYH);
            })
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
});

function Trim(str, is_global) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (is_global.toLowerCase() == "g") {
        result = result.replace(/\s/g, "");
    }
    return result;
}
//点击定位按钮，展示定位图
function locate(id, bdcdyh) {
    if (bdcdyh != "") {
        var url = "${bdcdjUrl}/bdzDyMap?proid=&bdcdyh=" + bdcdyh;
        openWin(url)
//        window.open(url);
    }
}

function getBdcdyhQlxx(bdcdyh,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/queryBdcdy/getBdcdyhQlxx?bdcdyh=" + bdcdyh,
        dataType:"json",
        success:function (result) {
            var cellVal = "";
            var qlr = "";
            if (!result.cf) {
                cellVal += '<span class="label label-danger">查封</span>';
            }
            if (!result.dy) {
                cellVal += '<span class="label label-warning">抵押</span>';
            }
            if (!result.yy) {
                cellVal += '<span class="label lable-dissension">异议</span>';
            }
            if (result.dy && result.cf && result.yy) {
                cellVal += '<span class="label label-success">正常</span>';
            }
            //if (result.qlr!=null && result.qlr!="") {
            //    qlr = result.qlr;
            //}
            if(result.qszt!=2){
                table.setCell(rowid, "ZT", cellVal);
            }
            //table.setCell(rowid, "QLR", qlr);
        }
    });
}

//xiejianan 获取不动产单元的不动产类型，权利人，坐落，权利状态信息
function getBdcdyhxx(bdcdyh,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/queryBdcdy/getBdcdyhxx?bdcdyh=" + bdcdyh,
        dataType:"json",
        success:function (result) {
            table.setCell(rowid, "BDCLX", result.BDCLX);
            table.setCell(rowid, "QLR", result.QLR);
            table.setCell(rowid, "ZL", result.ZL);
            table.setCell(rowid, "QSZT", result.QSZT);
        }
    });
}

//点击证书关系
function hisRel( bdcdyh) {
    if (bdcdyh != "") {
//        var url = "/server/bdcZsRel?proid=" + id + "&bdcdyh=" + bdcdyh;
        var url = "/workflowPlugin/index.html?url=" + "${bdcdjUrl!}" + "/bdcZsRel/getDyAllXmRelXml?bdcdyh=" + bdcdyh;
        openWin(url);
    }
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
                               data-watermark="请输入不动产单元号/坐落">
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
                    <#list bdcdyGjssOrderList as bdcdyGjss>
                        <#if bdcdyGjss == 'bdclx'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="bdclx" class="form-control" id="bdclxSel">
                                    <option></option>
                                    <#list bdcList as bdclx>
                                        <option value="${bdclx.DM}">${bdclx.MC}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdyGjss=='bdcdyh'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产单元号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcdyh" class="form-control">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdyGjss=='zl'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zl" class="form-control">
                            </div>
                            <div class="col-xs-2">
                            </div>
                            <div class="col-xs-4">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdyGjss=='zdzdh'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>宗地宗海号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zdzhh" class="form-control">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdyGjss=='dbr'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登簿人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="dbr" class="form-control">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdyGjss=='zdh'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>宗地号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zdh" class="form-control">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif bdcdyGjss='xqmc'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>小区名称：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="fwmc" class="form-control">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif bdcdyGjss='qlr'>
                            <#if (bdcdyGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权利人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="qlr" class="form-control">
                            </div>
                            <#if (bdcdyGjss_index + 1) % 2 == 0>
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
