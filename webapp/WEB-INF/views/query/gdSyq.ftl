<@com.html title="不动产登记过度所有权查询系统" import="ace">
<style type="text/css">
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

        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width: 100% !important;
        margin: 0px 5px 0px 0px
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

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;

    }
</style>

<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
$(function () {
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
    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});


    //绑定回车键
    $('#search_xmmc').keydown(function (event) {
        if (event.keyCode == 13) {
            serch();
        }
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/gdSyqcx/getgdSyqPagesJsonace?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:""});
        $("#gjSearchPop").hide();
        $("#gjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    })
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
        url:"${bdcdjUrl}/gdSyqcx/getgdSyqPagesJsonace",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'FWID'},
        colNames:["qlid",'fwid','proid','oinsid',"证书状态", '登记类型', '所有权人', '所有权证号', '坐落', '建筑面积',  '规划用途', '原权证号','附件查看', '房产信息'],
        colModel:[
            {name:'QLID', index:'QLID', width:'0%', sortable:false,hidden:true},
            {name:'FWID', index:'FWID', width:'0%', sortable:false,hidden:true},
            {name:'PROID', index:'PROID', width:'0%', sortable:false,hidden:true},
            {name:'OINSID', index:'OINSID', width:'0%', sortable:false,hidden:true},
            {name:'STATUS', index:'', width:'10%', sortable:false},
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
            {name:'QLR', index:'QLR', width:'20%', sortable:false},
            {name:'FCZH', index:'FCZH', width:'10%', sortable:false},
            {name:'FWZL', index:'FWZL', width:'20%', sortable:false},
            {name:'JZMJ', index:'JZMJ', width:'10%', sortable:false},
            {name:'GHYT', index:'GHYT', width:'10%', sortable:false},
            {name:'YQZH', index:'YQZH', width:'10%', sortable:false},
            {
                name: 'mydy',
                index: '',
                width: '7%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;">' +
                            '<div title="附件查看" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="showFjPic(\'' + rowObject.QLID + '\',\'' + rowObject.OINSID + '\',\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' +
                            '</div>'
                }
            },
            {
                name: 'fcxx',
                index: '',
                width: '7%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;">' +
                            '<div title="房产信息" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="showFcxx(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' +
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
        rownumbers:true,
        rownumWidth:50,
        loadComplete:function () {
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                asyncGetGdFwxx($(grid_selector),data.QLID,data.FWID);
                getQLR(data.QLID, $(grid_selector), data.FWID);
            })
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
});

//zx 获取房产证抵押 查封 预告 异议 状态
function asyncGetGdFwxx(table,qlid,fwid) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdFwxxByQlid?bdclx=TDFW&qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            //正常
            var zls = result.zls;
            if (result.qlzts == null || result.qlzts == "") {
                cellVal += '<span class="label label-success">正常</span>';
            } else {
                var qlzts = result.qlzts.split(",");
                for (var i = 0; i < qlzts.length; i++) {
                    //zhouwanqing 防止其他权利与注销同在
                    if (qlzts[i] == "ZX") {
                        cellVal = '<span class="label label-danger">注销</span><span> </span>';
                        break;
                    } else if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span><span> </span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span><span> </span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }
            table.setCell(fwid, "STATUS", cellVal);
        }
    });
}
//获取权利人信息
function getQLR(qlid,table,rowid) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/gdSyqcx/getGdQlr?qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            table.setCell(rowid, "QLR", result.QLR);
        }
    });
}
function serch() {
    var dcxc = $("#search_xmmc").val();
    var Url = "${bdcdjUrl}/gdSyqcx/getgdSyqPagesJsonace";
    tableReload("grid-table", Url, {dcxc:dcxc});
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
function showFjPic(qlid, oinsid, proid) {
    if (oinsid && oinsid != 'undefined') {
        var url = "${bdcdjUrl!}/showFcFjPic/openImg?qlid=" + qlid;
    } else {
        var url = "${platformUrl!}//fc.action?proid=" + proid;
    }
    openWin(url, "");
}

function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

function showFcxx(qlid) {
    debugger;
    var url = "${bdcdjUrl!}/showFcFjPic/openFcxx?qlid=" + qlid;
    $.ajax({
        type: "GET",
        url: url,
        dataType: "json",
        success: function (result) {
            if (result != null && result != "") {
                var fcurl = "${fcBaseUrl!}" + "/estate_wj/workflow_query_select_action.do?method=initTreeList&params=" + result.params;
                window.open(fcurl);
            } else {
                alert("系统错误，请联系管理员！");
            }

        }
    });
}
</script>

<div class="space-10"></div>
<div class="main-container">
    <div class="page-content">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入所有权证号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="serch()">
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
        <!--end  高级搜索 -->
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>所有权人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control" onclick="">
                        </div>

                        <div class="col-xs-2">
                            <label>所有权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fczh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fwzl" class="form-control">
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

<object id="Capture" style="width: 100%;height: 100%;border: 5 gray solid;display: none"
        classid="clsid:9A73DB73-2CA3-478D-9A3F-7E9D6A8D327C" codebase="CaptureVideo.cab#version=1,1,1,9">
    <embed></embed>
</object>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>