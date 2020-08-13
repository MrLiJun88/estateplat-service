<@com.html title="土地使用权查询" import="ace,public">
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

    //zwq 回车键搜索
    $('input').focus(function(){
        $('#search_xmmc').keydown(function (event) {
            if (event.keyCode == 13) {
                $("#search").click();
            }
        });

    });

    //项目表搜索事件
    $("#search").click(function () {
        var xmmc = $("#search_xmmc").val();
        $("#gjSearchForm")[0].reset();
        var Url = "${bdcdjUrl}/queryTdsyq/getTdsyqPagesJson?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:xmmc});
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/queryTdsyq/getTdsyqPagesJson?" + $("#gjSearchForm").serialize();
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
        url:"${bdcdjUrl}/queryTdsyq/getTdsyqPagesJson",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'QLID'},
        colNames:[ '证书状态','登记类型','权利人', '地籍号', '土地证号', '坐落','宗地面积', '规划用途','查看','附件查看','QLID','TDID','PROID','PROJECTID'],
        colModel:[
            {name:'ISZX', index:'ISZX', width:'10%', sortable:false},
            {name:'DJLX', index:'DJLX', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue && cellvalue != "0") {
                    return "";
                }
                var value = cellvalue;
                $.each(${gdDjlxList!}, function (i, item) {
                    if (item.dm == cellvalue) {
                        value = item.mc;
                    }
                })
                return value;
            }},
            {name:'QLR', index:'QLR', width:'8%', sortable:false},
            {name:'DJH', index:'DJH', width:'10%', sortable:false},
            {name:'TDZH', index:'TDZH', width:'15%', sortable:false},
            {name:'ZL', index:'ZL', width:'25%', sortable:false},
            {name:'ZDMJ', index:'ZDMJ', width:'10%', sortable:false},
            {name:'YT', index:'YT', width:'15%', sortable:false},
			{name:'CK', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
				return '<div style="margin-left:8px;">' +
						' <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="queryGdtd(\'' + rowObject.QLID + '\',\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>'
				'</div>'
			}
			},
            {
                name: 'mydy',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;">' +
                            '<div title="附件查看" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="showFjPic(\'' + rowObject.PROID + '\',\'' + rowObject.PROJECTID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' +
                            '</div>'
                }
            },
            {name:'QLID', index:'QLID', width:'0%', sortable:false, hidden:true},
            {name:'TDID', index:'TDID', width:'0%', sortable:false, hidden:true},
			{name:'PROID', index:'PROID', width:'0%', sortable:false, hidden:true},
            {name: 'PROJECTID', index: 'PROJECTID', width: '6%', sortable: false,hidden:true}
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
                getQlrByDyid(data.QLID, $(grid_selector), data.QLID);
                getDyYgCfStatus(data.QLID,$(grid_selector),data.TDID);
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

function queryGdtd(qlid,proid){
	openWin("${reportUrl!}/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=\\edit\\query_gdtd&op=write&cptName=query_gdtd&qlid=" + qlid + "&proid="+proid +"&ywType=server");

}

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
        openWin(url);
//        window.open(url);
    }
}

//zwq 查看附记
function showFjPic(proid, projectid) {
    debugger;
    if (projectid && projectid != 'undefined') {
        var url = "${bdcdjUrl!}/showTdFjPic/openImg?projectid=" + projectid;

    } else {
        //不动产文件管理
        var url = "${platformUrl}//fc.action?proid=" + proid;
    }
    openWin(url, "");
}
//获取 抵押 查封 预告 状态
function getDyYgCfStatus(qlid,table, tdid) {
    if (tdid == null || tdid == 'undefined') {
        tdid = "";
    }
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdTdxxByQlid?proid=" + tdid+"&bdclx=TD&qlid="+ qlid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            if (result.qlzts == null || result.qlzts == "") {
                cellVal = '<span class="label label-success">正常</span>';
            } else {
                var qlzts = result.qlzts.split(",");
                for (var i = 0; i < qlzts.length; i++) {
                    if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span><span> </span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span><span> </span>';
                    } else if (qlzts[i] == "ZX") {
                        cellVal += '<span class="label label-danger">注销</span><span> </span>';
                    } else if (qlzts[i] == "DI") {
                        cellVal += '<span class="label label-danger">地役</span><span> </span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }
//                if (result.zls != null && result.zls != "" && result.qlrs != "null")
//                    table.setCell(tdid, "ZL", result.zls);
            table.setCell(qlid, "ISZX", cellVal);
        }
    });
}
//异步请求查询每条数据的权利人
function getQlrByDyid(qlid,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/queryTdsyq/getQlrByQlid?qlid=" + qlid,
        dataType:"json",
        success:function (result) {
            table.setCell(rowid, "QLR", result.qlr_str);
        }
    });
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
                               data-watermark="请输入坐落/土地证号">
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
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                    <div class="row">

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                        </div>
                        <div class="col-xs-4">
                        </div>
                    </div>

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
