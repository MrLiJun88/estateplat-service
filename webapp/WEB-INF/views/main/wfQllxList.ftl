<@com.html title="权利信息列表" import="ace,public">
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
        width;100% !important;
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
    .form .row .col-xs-4,.col-xs-10 {
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
$(function () {
    var windowWidth = window.screen.width;
    if(windowWidth<=1280){
        $("#ace-settings-container").hide();
    }
    //初始化权利信息列表
    logTableInit();
    /*   文字水印  */
    $(".watermarkText").watermark();
    //查询按钮点击事件
    $("#search").click(function () {
        var searchInfo = $("#searchInfo").val();
        var wiid = "${wiid!}";
        var yqlList = "${yqlList!}";
        var logUrl = "${bdcdjUrl}/qllxResource/getQllxListByPage?djlx=${djlx!}&wiid=" + wiid+"&qlr="+encodeURI(searchInfo)+"&yqlList="+yqlList+"&sqlx=${sqlx!}";
        tableReload("log-grid-table", logUrl, {username:searchInfo});
    })

    $("#plxg").click(function(){
        var url = "${reportUrl!}/ReportServer?reportlet=/edit/mul/bdc_modify_fdcq.cpt&op=write&proid="+"${workflowProid!}";
        window.location.href=url;
    })

    var width = $(window).width() / 2;
    if(width<1000){
        width=1000;
    }
    var height = $(window).height()-20;
    $("#ace-settings-box").css("width", width).css("height", height);
    $(window).resize(function () {
        var width = $(window).width() / 2;
        if(width<1000){
            width=1000;
        }
        var height = $(window).height()-20;
        $("#ace-settings-box").css("width", width).css("height", height);
    })

    //定位
    $("#ace-settings-btn").click(function(){
        if($("#ace-settings-box").hasClass("open")){
            setTimeout(function(){$("#iframe").attr("src",$("#iframeSrcUrl").val())},300);
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
    $(window).on('resize.jqGrid', function () {
        var  contentWidth;
        if($("#mainContent").width()>0){
            contentWidth=$("#mainContent").width();
        }
        $("#log-grid-table").jqGrid('setGridWidth',contentWidth);
    });
})

//选择数据的事件
function sel(proid,bdcdyh,bdcdyid){
    //saveReport();
    var windowWidth = window.screen.width;
    if(proid != '' && proid != undefined) {
        var rid = "${rid!}";
        var taskid = "${taskid!}";
        var from = "${from!}";
        var wiid = "${wiid!}";

        var url = "${bdcdjUrl}/qllxResource?from=" + from + "&taskid=" + taskid + "&proid=" + proid + "&wiid=" + wiid + "&rid=" + rid + "&getSimpleCpt=true&bdcdyh=" + bdcdyh + "&bdcdyid=" + bdcdyid;
        $("#iframeSrcUrl").val(url);
        setTimeout(function(){
            if(windowWidth<1280){
                showIndexModel(url,"权利信息","650","650",false);
                return;
            }
            if($("#ace-settings-box").hasClass("open")){
                $("#iframe").attr("src",url);
            }else{
                $("#ace-settings-box").click();
            }
        },100);

    }
}
function saveReport() {
    try {
        var contentFrame = document.getElementById("iframe");
        if (contentFrame != null) {
            var contentPane = contentFrame.contentWindow.contentPane;
            if (contentPane != null)
                contentPane.writeReport();
        }
    } catch (error) {
    }
}
function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

function logTableInit() {
    var grid_selector = "#log-grid-table";
    var pager_selector = "#log-grid-pager";
    var wiid = "${wiid!}";
    var yqlList = "${yqlList!}";
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
        url:"${bdcdjUrl}/qllxResource/getQllxListByPage?djlx=${djlx!}&wiid="+wiid+"&yqlList="+yqlList+"&sqlx=${sqlx!}",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'PROID'},
        colNames:['序列','PROID','坐落','不动产单元号',"权利人",
                <#if "${djlx!}" ?? && "${djlx!}"=="800">
                    "查封文号","查封机关",'查封文件'
                <#elseif "${djlx!}" ?? && "${djlx!}"=="1000">
                    "被担保主债权数额","抵押方式",'担保范围'
                <#else>
                    "定着物面积","宗地/宗海面积",'业务号'
                </#if>
        ],
        colModel:[
            {name: 'XL', index: '', width: '5%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="xl" onclick="sel(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\')"/>'
            }
            },
            {name:'PROID', index:'PROID', width:'0%', sortable:false, hidden:true},
            {name:'ZL', index:'ZL', width:'25%', sortable:false},
            {name:'BDCDYH', index:'BDCDYH', width:'25%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                var value ="";
                if(cellvalue!=null && cellvalue!="" && cellvalue!=undefined){
                   value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                }
                return value;
            }},
            {name:'QLR', index:'', width:'15%', sortable:false},
            <#if "${djlx!}" ?? && "${djlx!}"=="800">
                {name:'CFWH', index:'CFWH', width:'10%', sortable:false},
                {name:'CFJG', index:'CFJG', width:'10%', sortable:false},
                {name:'CFWJ', index:'CFWJ', width:'20%', sortable:false}
            <#elseif "${djlx!}" ?? && "${djlx!}"=="1000">
                {name:'BDBZZQSE', index:'BDBZZQSE', width:'10%', sortable:false},
                {name:'DYFS', index:'DYFS', width:'10%', sortable:false},
                {name:'DBFW', index:'DBFW', width:'20%', sortable:false}
            <#else>
                {name:'DJZWMJ', index:'DJZWMJ', width:'10%', sortable:false},
                {name:'ZDZHMJ', index:'ZDZHMJ', width:'10%', sortable:false},
                {name:'YWH', index:'YWH', width:'20%', sortable:false}
            </#if>

        ],
        viewrecords:true,
        rowNum:300,
        rowList:[10, 20, 50,100,200,300],
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
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getQlrmc(data.PROID,$(grid_selector));
            });
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
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
// 获取权利人名称
function getQlrmc(proid,table) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl!}/qllxResource/getQlrmcByProid?proid=" + proid,
        success: function (result) {
            if(result!=null && result!=""){
                if(result.qlrmc!="" && result.qlrmc!=null && result.qlrmc!='undefined') {
                    table.setCell(proid, "QLR", result.qlrmc);
                }
            }
        }
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

                    <#--<td class="Search">
                        <a href="#" id="plxg">
                            批量修改
                            <i class="ace-icon fa fa-file bigger-130"></i>
                        </a>
                    </td>-->

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
