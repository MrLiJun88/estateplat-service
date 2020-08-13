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
            var logUrl = "${bdcdjUrl}/bdcDjbQuery/djbQueryList?wiid=" + wiid;
            tableReload("log-grid-table", logUrl, {dcxc:searchInfo});
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
        $(window).on('resize.jqGrid', function () {
            var  contentWidth;
            if($("#mainContent").width()>0){
                contentWidth=$("#mainContent").width();
            }
            $("#log-grid-table").jqGrid('setGridWidth',contentWidth);
        });
    })
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function logTableInit() {
        var grid_selector = "#log-grid-table";
        var pager_selector = "#log-grid-pager";
        var wiid = "${wiid!}";
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
            url:"${bdcdjUrl}/bdcDjbQuery/djbQueryList?wiid="+wiid,
            datatype:"json",
            height:'auto',
            jsonReader:{id:'BDCDYH'},
            colNames:['不动产单元号','坐落','面积','用途','查看','DJBID','DJID','WIID','QLLX'],
            colModel:[
                {name:'BDCDYH', index:'BDCDYH', width:'25%', sortable:false},
                {name:'ZL', index:'ZL', width:'25%', sortable:false},
                {name:'MJ', index:'MJ', width:'15%', sortable:false},
                {name:'YT', index:'YT', width:'10%', sortable:false},
                {name:'CK', index:'ZDZHMJ', width:'10%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;">' +
                            '<div><a class="detail" href="javascript:openDetails(\'' + rowObject.BDCDYH + '\',\'' + rowObject.DJBID + '\',\'' + rowObject.DJID + '\')">查看</a></div>' +
                            '</div>'
                }},
                {name:'DJBID', index:'DJBID', width:'10%', sortable:false,hidden:true},
                {name:'DJID', index:'DJID', width:'10%', sortable:false,hidden:true},
                {name:'WIID', index:'WIID', width:'10%', sortable:false,hidden:true},
                {name:'QLLX', index:'QLLX', width:'20%', sortable:false,hidden:true}
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
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }

    function openDetails(bdcdyh,djbid,djid){
        var url ="${bdcdjUrl}/bdcDjb/showQL?bdcdyh="+bdcdyh+"&djbid="+djbid+"&djid="+djid;
        open(encodeURI(url));
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
</script>
<div class="main-container">
    <div class="space-8"></div>
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
