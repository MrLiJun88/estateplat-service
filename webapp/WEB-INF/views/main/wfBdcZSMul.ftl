<@com.html title="不动产权证信息列表" import="fr,ace,jQueryVersion">
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

        /*
         zwq 解决帆软的样式和页面上的jqgrid的下标的冲突
        */
    .ui-state-disabled {
        width: 10%;
        opacity:1;
        color: #BBB;
    }

    .print {
        border: 0px !important;
        padding-left: 20px;
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

    pre, input[type="text"] {
        padding: 0px !important;
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

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }
</style>

<script type="text/javascript">

_$(function () {

    //初始化权利信息列表
    logTableInit();
    /*   文字水印  */
    _$(".watermarkText").watermark();
    //查询按钮点击事件
    _$("#search").click(function () {
        var searchInfo = _$("#searchInfo").val();
        var workflowProid = "${workflowProid!}";
        var logUrl = "${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&zl=" + encodeURI(searchInfo);
        tableReload("log-grid-table", logUrl, {username:searchInfo});
    })

    var width = _$(window).width() / 2;
    if (width < 1000) {
        width = 1000;
    }
    var height = _$(window).height() - 20;
    _$("#ace-settings-box").css("width", width).css("height", height);
    _$(window).resize(function () {
        var width = _$(window).width() / 2;
        if (width < 1000) {
            width = 1000;
        }
        var height = _$(window).height() - 20;
        _$("#ace-settings-box").css("width", width).css("height", height);
    })

    //定位
    _$("#ace-settings-btn").click(function () {
        if (_$("#ace-settings-box").hasClass("open")) {
            setTimeout(function () {
                _$("#iframe").attr("src", _$("#iframeSrcUrl").val())
            }, 500);
        }
    })
    //日志表高级查询的搜索按钮事件
<#--$("#logSearchBtn").click(function () {-->
<#--var Url = "${bdcdjUrl}/bdcSjgl/getBdcXtLogListByPage?" + $("#logSearchForm").serialize();-->
<#--tableReload("log-grid-table", Url, {username:""});-->
<#--})-->
    //日志高级搜索关闭事件
//    $("#proHide").click(function () {
//        $("#logSearchPop").hide();
//        $("#logSearchForm")[0].reset();
//    });
    //日志高级查询按钮点击事件
//    $("#logShow").click(function () {
//        $("#logSearchPop").show();
//    });
    //时间控件
//    $('.date-picker').datepicker({
//        autoclose:true,
//        todayHighlight:true,
//        language:'zh-CN'
//    }).next().on(ace.click_event, function () {
//        $(this).prev().focus();
//    });

    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
//    var ua = navigator.userAgent.toLowerCase();
//    if (window.ActiveXObject){
//        if(ua.match(/msie ([\d.]+)/)[1]=='8.0'){
//            $(window).resize(function(){
//                $.each($(".moveModel > .modal-dialog"),function(){
//                    $(this).css("left",($(window).width()-$(this).width())/2);
//                    $(this).css("top","40px");
//                })
//            })
//        }
//    }

    //拖拽功能
//    $(".modal-header").mouseover(function () {
//        $(this).css("cursor", "move");//改变鼠标指针的形状
//    })
//    $(".modal-header").mouseout(function () {
//        $(".show").css("cursor", "default");
//    })
//    $(".logSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

    //resize to fit page size
    _$(window).on('resize.jqGrid', function () {
        var contentWidth;
        if (_$("#mainContent").width() > 0) {
            contentWidth = _$("#mainContent").width();
        }
        _$("#log-grid-table").jqGrid('setGridWidth', contentWidth);
    });

    _$("#plprint").click(function () {

        var grid_selector = "#log-grid-table";

        var p = [];
        var proid = "${workflowProid!}";
        var printurl = "${reportUrl}/ReportServer";
        //打印所有页
        p.push("{reportlet: '/edit/mul/bdc_bdcqz.cpt', proid : '" + proid + "',bdcdyid=''}");
        // 打印jqgrid展示当前页
//        var obj = _$(grid_selector).jqGrid("getRowData");
//        for (var i = 0; i < obj.length; i++) {
//            p.push("{reportlet: '/edit/mul/bdc_fwcq_zmd.cpt', proid : '" + proid + "', bdcdyid : '" + obj[i].BDCDYID + "'}");
//        }
        //将参数值组成的数组转化为字符串
        var rp = p.join(",");
        //使用FineReport自带的方法cjkEncode进行转码
        var reportlets = FR.cjkEncode("[" + rp + "]");

        var config = {
            url:printurl,
            isPopUp:true,
            data:{
                reportlets:reportlets
            }
        };
        FR.doURLFlashPrint(config);
    });
})
//选择数据的事件
function sel(proid, bdcdyid) {
    if (proid != '' && proid != undefined && bdcdyid != '' && bdcdyid != undefined) {
        var rid = "${rid!}";
        var taskid = "${taskid!}";
        var from = "${from!}";
        var wiid = "${wiid!}";
        var url = "${bdcdjUrl}/bdcZsResource?from=" + from + "&taskid=" + taskid + "&proid=" + proid + "&wiid=" + wiid + "&rid=" + rid + "&getSimpleCpt=zs&bdcdyid=" + bdcdyid;
        _$("#iframeSrcUrl").val(url);
        if (_$("#ace-settings-box").hasClass("open")) {
            _$("#iframe").attr("src", url);
        } else {
            _$("#ace-settings-box").click();
        }
    }
}

function tableReload(table, Url, data) {
    var jqgrid = _$("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}


function logTableInit() {
    var grid_selector = "#log-grid-table";
    var pager_selector = "#log-grid-pager";
    var workflowProid = "${workflowProid!}";
    //resize to fit page size
    /* $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
    });*/
    //resize on sidebar collapse/expand
    var parent_column = _$(grid_selector).closest('[class*="col-"]');
    _$(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            _$(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    _$(grid_selector).jqGrid({
        url:"${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid,
        datatype:"json",
        height:'auto',
        jsonReader:{id:'BDCDYID'},
        colNames:['序列', '不动产单元号', '坐落', "权利人", "定着物面积", '规划用途', 'ID'],
        colModel:[
            {name:'XL', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="xl" onclick="sel(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYID + '\')"/>'
            }
            },
            {name:'BDCDYH', index:'BDCDYH', width:'20%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                return value;
            }},
            {name:'ZL', index:'ZL', width:'25%', sortable:false},
            {name:'QLR', index:'QLR', width:'20%', sortable:false},
            {name:'DJZWMJ', index:'DJZWMJ', width:'10%', sortable:false},
            {name:'GHYT', index:'GHYT', width:'15%', sortable:false},
            {name:'BDCDYID', index:'BDCDYID', width:'0%', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        rownumbers:false,
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

function enableTooltips(table) {
    _$('.navtable .ui-pg-button').tooltip({container:'body'});
    _$(table).find('.ui-pg-div').tooltip({container:'body'});
}

function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
    };
    _$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = _$(this);
        var $class = _$.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
    <#--图形-->
        <div class="ace-settings-container" id="ace-settings-container">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
                <i class="ace-icon fa fa-globe blue bigger-200"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
            </div>
            <!-- /.ace-settings-box -->
        </div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="searchInfo" data-watermark="请输入坐落">
                    </td>
                    <td class="Search">
                        <a href="#" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>

                    <td class="print">
                        <button id="plprint" class="btn01 AdvancedButton" value="批量打印">批量打印</button>
                    </td>

                </tr>

            </table>
        </div>
        <table id="log-grid-table"></table>
        <div id="log-grid-pager"></div>
    </div>
</div>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
