<@com.html title="不予受理通知书查询" import="ace">
<style>
    .modal-dialog {
        width: 1000px;
    }

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
<script>
    $(function () {

        /*//resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth;
            if ($("#accessContent").width() > 0) {
                contentWidth = $("#accessContent").width();
            }
            $("#access-grid-table").jqGrid('setGridWidth', contentWidth);
        });*/
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

        /*   文字水印  */
        $(".watermarkText").watermark();

        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(this).css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

        //项目表搜索事件
        $("#searchBtn").click(function () {
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var Url = "${bdcdjUrl}/bdcByslTzs/queryBdcByslList";
            tableReload("access-grid-table", Url, {startTime:startTime,endTime:endTime});
        });

        //生成表格
        accessTableGrid();
    });
    
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    //auth表格初始化
    function accessTableGrid() {
        var grid_selector = "#access-grid-table";
        var pager_selector = "#access-grid-pager";

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
            url:"${bdcdjUrl}/bdcByslTzs/queryBdcByslList" ,
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'TZSID'},
            colNames: ['编号', '通知对象','通知时间','发通知时间','收件人','申请人', '坐落',  '通知原因','备注','TZSID'],
            colModel: [
                {name: 'BH', index: 'BH', width: '15%', sortable: false},
                {name: 'TZDX', index: 'TZDX', width: '15%', sortable: false},
                {name: 'TZSJ', index: 'TZSJ', width: '10%', sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (!cellvalue) {
                            return "";
                        }
                        var value = cellvalue;
                        var data = new Date(value).Format("yyyy-MM-dd");
                        return data;
                    }
                },
                {name: 'FTZSJ', index: 'FTZSJ', width: '10%', sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (!cellvalue) {
                            return "";
                        }
                        var value = cellvalue;
                        var data = new Date(value).Format("yyyy-MM-dd");
                        return data;
                    }
                },
                {name: 'SJR', index: 'SJR', width: '10%', sortable: false},
                {name: 'SQR', index: 'SQR', width: '10%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '25%', sortable: false},
                {name: 'TZYY', index: 'TZYY', width: '25%', sortable: false},
                {name: 'BZ', index: 'BZ', width: '25%', sortable: false},
                {name: 'TZSID', index: 'TZSID', width: '0%', sortable: false, hidden:true}
            ],
            viewrecords: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            pagerpos: "left",
            pager: pager_selector,
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
            },
            caption: "",
            autowidth: true
        });
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
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
    <div class="space-10"></div>
    <div class="page-content" id="accessContent">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td style="border: 0px">
                        <label>通知时间(起)：</label>
                        <span class="input-icon">
                            <input type="text" class="date-picker form-control" name="startTime" id="startTime"
                                   data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td style="border: 0px">
                        <label>通知时间(至)：</label>
                        <span class="input-icon">
                            <input type="text" class="date-picker form-control" name="endTime" id="endTime"
                                   data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td class="Search">
                        <a href="#" id="searchBtn">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                </tr>
            </table>
        </div>
        <table id="access-grid-table"></table>

        <div id="access-grid-pager"></div>

    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<div class="modal-backdrop fade in Pop" style="display:none;" id="modal-backdrop-pop"></div>
</@com.html>