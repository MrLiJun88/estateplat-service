<@com.html title="不动产登记过度抵押权查询系统" import="ace">
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
        var Url = "${bdcdjUrl}/gdDyqcx/getgdDyqPagesJsonace?" + $("#gjSearchForm").serialize();
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
        url:"${bdcdjUrl}/gdDyqcx/getgdDyqPagesJsonace",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'FWID'},
        colNames:["DYID",'fwid',"状态", '登记类型', '抵押人', '所有权证号','抵押权人', '抵押权证号','坐落','建筑面积','规划用途'],
        colModel:[
            {name:'DYID', index:'DYID', width:'0%', sortable:false,hidden:true},
            {name:'FWID', index:'FWID', width:'0%', sortable:false,hidden:true},
            {name:'ISJY', index:'ISJY', width:'10%',sortable:false,formatter:function (cellvalue, options, rowObject) {
                var value=cellvalue;
                if (value == '0') {
                    return '<span class="label label-success" style="margin-left: 20px;">' + "正常" + '</span>&nbsp;';
                }
                else if (value == '1') {
                    return '<span class="label label-gray" style="margin-left: 20px;">' + "解押" + '</span>&nbsp;';
                }
            }},
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
            {name:'DYR', index:'DYR', width:'10%', sortable:false},
            {name:'FCZH', index:'FCZH', width:'10%', sortable:false},
            {name:'DYQR', index:'DYQR', width:'10%', sortable:false},
            {name:'DYDJZMH', index:'DYDJZMH', width:'10%', sortable:false},
            {name:'FWZL', index:'FWZL', width:'20%', sortable:false},
            {name:'JZMJ', index:'JZMJ', width:'10%', sortable:false},
            {name:'GHYT', index:'GHYT', width:'10%', sortable:false}
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
                getQLR(data.DYID, $(grid_selector), data.FWID);
            })
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
});
//获取权利人信息
function getQLR(QLID,table,rowid) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/gdDyqcx/getGdDyQlr?QLID=" + QLID,
        dataType: "json",
        success: function (result) {
            table.setCell(rowid, "DYR", result.dyr);
            table.setCell(rowid, "DYQR", result.dyqr);
        }
    });
}
function serch() {
    var dcxc = $("#search_xmmc").val();
    var Url = "${bdcdjUrl}/gdDyqcx/getgdDyqPagesJsonace";
    tableReload("grid-table", Url, {dcxc:dcxc});
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
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
                            <label>抵押权人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="dyqr" class="form-control">
                        </div>

                        <div class="col-xs-2">
                            <label>抵押人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="dyr" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>所有权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fczh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fwzl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>抵押权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="dydjzmh" class="form-control">
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

<object id="Capture" style="width: 100%;height: 100%;border: 5 gray solid;display: none"
        classid="clsid:9A73DB73-2CA3-478D-9A3F-7E9D6A8D327C" codebase="CaptureVideo.cab#version=1,1,1,9">
    <embed></embed>
</object>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>