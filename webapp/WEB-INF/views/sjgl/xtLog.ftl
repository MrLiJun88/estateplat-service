<@com.html title="系统日志" import="ace">
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
    $(function () {
        //初始化日志表格
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
            var logUrl = "${bdcdjUrl}/bdcSjgl/getBdcXtLogListByPage?" + $("#logSearchForm").serialize();
            tableReload("log-grid-table", logUrl, {username:searchInfo});
        })
        //日志表高级查询的搜索按钮事件
        $("#logSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcSjgl/getBdcXtLogListByPage?" + $("#logSearchForm").serialize();
            tableReload("log-grid-table", Url, {username:""});
        })
        //日志高级搜索关闭事件
        $("#proHide").click(function () {
            $("#logSearchPop").hide();
            $("#logSearchForm")[0].reset();
        });
        //日志高级查询按钮点击事件
        $("#logShow").click(function () {
            $("#logSearchPop").show();
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

    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
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
            url:"${bdcdjUrl}/bdcSjgl/getBdcXtLogListByPage",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'LOGID'},
            colNames:['操作人', '操作日期', "操作信息"],
            colModel:[
                {name:'USERNAME', index:'USERNAME', width:'30%', sortable:false},
                {name:'CZRQ', index:'CZRQ', width:'30%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd hh:mm:ss");
                    return data;
                }},
                {name:'PARMJSON', index:'PARMJSON', width:'40%', sortable:false}

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
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="searchInfo" data-watermark="请输入操作人">
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
        <table id="log-grid-table"></table>
        <div id="log-grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="logSearchPop">
    <div class="modal-dialog logSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
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
                            <label>操作信息：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" name="controllerMsg" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="logSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
