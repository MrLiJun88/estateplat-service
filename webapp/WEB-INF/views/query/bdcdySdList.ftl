<@com.html title="不动产单元冻结清单" import="ace,public">
<style>
    .modal-dialog {
        width: 1000px;
    }

    /*高级搜索样式添加 begin*/
    /*移动modal样式*/
    #highLevelSearch .modal-dialog {
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
    .chosen-container > .chosen-single, [class*="chosen-container"] > .chosen-single {
        height: 34px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">

    $(function () {
        //全局数组
        $selectArray = new Array();
        $mulData = new Array();
        $mulRowid = new Array();
        $selectedInput = new Array();
        $rownum = 10;
        Array.prototype.remove = function (index) {
            if (index > -1) {
                this.splice(index, 1);
            }
        };
        if (!Array.prototype.forEach) {
            Array.prototype.forEach = function (callback, thisArg) {
                var T, k;
                if (this == null) {
                    throw new TypeError(" this is null or not defined");
                }
                var O = Object(this);
                var len = O.length >>> 0; // Hack to convert O.length to a UInt32
                if ({}.toString.call(callback) != "[object Function]") {
                    throw new TypeError(callback + " is not a function");
                }
                if (thisArg) {
                    T = thisArg;
                }
                k = 0;
                while (k < len) {
                    var kValue;
                    if (k in O) {
                        kValue = O[k];
                        callback.call(T, kValue, k, O);
                    }
                    k++;
                }
            };
        }

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


        //下拉框
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据", width: "100%"});
        $(window).on('resize.chosen', function () {
            $.each($('.chosen-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var w = $(obj).parent().width();
                $(obj).next().css("width", w);
            })
        }).trigger('resize.chosen');

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

        //zwq 回车键搜索
        $('input').focus(function () {
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
            var Url = "${bdcdjUrl}/bdcSjSd/getBdcdySdPagesJson?proid=" + $("#proid").val() + "&" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc: xmmc});
        });

        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcSjSd/getBdcdySdPagesJson?proid=" + $("#proid").val() + "&" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc: ""});
        });
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

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
            $(".modal-dialog").css({"_margin-left": "25%"});
        });

        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/bdcSjSd/getBdcdySdPagesJson?proid=${proid!}",
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'BDCDYID'},
            colNames: ['权利人', '不动产单元号', '坐落', '锁定人', '解锁人', '冻结状态', 'PROID', 'BDCLX', 'WIID', 'BDCDYID'],
            colModel: [
                {name: 'QLR', index: 'QLR', width: '5%', sortable: false},
                {
                    name: 'BDCDYH',
                    index: 'BDCDYH',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        var value = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        return value;
                    }
                },
                {name: 'ZL', index: 'ZL', width: '10%', sortable: false, hidden: false},
                {name: 'SDR', index: 'SDR', width: '5%', sortable: false, hidden: false},
                {name: 'JSR', index: 'JSR', width: '5%', sortable: false, hidden: false},
                {
                    name: 'XZZT', index: 'XZZT', width: '10%', sortable: false, hidden: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue == "0") {
                            cellvalue = '<span class="label label-success">未冻结</span>';
                        } else if (cellvalue == "1") {
                            cellvalue = '<span class="label label-danger">已冻结</span>';
                        } else {
                            cellvalue = '<span class="label label-success">未冻结</span>';
                        }
                        return cellvalue;
                    }
                },
                {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
                {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
                {name: 'WIID', index: 'WIID', width: '0%', sortable: false, hidden: true},
                {name: 'BDCDYID', index: 'BDCDYID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            rowList: [10, 20, 30],
            pager: pager_selector,
            pagerpos: "left",
            multiselect: true,
            altRows: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
                //var jqData = $(grid_selector).jqGrid("getRowData");

            },
            ondblClickRow: function (rowid, index) {
                //dianji(rowid);
            },
            editurl: "", //nothing is saved
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
    });


    function unlockBdcdy() {
        var chk_value = $('#grid-table').jqGrid('getGridParam', 'selarrrow');
        for (var i = 0; i < chk_value.length; i++) {
            var rowData = $('#grid-table').jqGrid('getRowData', chk_value[i]);
            unlockBdcdyXzzt(rowData.BDCDYID);
        }
    }

    function lockBdcdy() {
        var chk_value = $('#grid-table').jqGrid('getGridParam', 'selarrrow');
        for (var i = 0; i < chk_value.length; i++) {
            var rowData = $('#grid-table').jqGrid('getRowData', chk_value[i]);
            lockBdcdyXzzt(rowData.BDCDYID);
        }
    }


    function unlockBdcdyXzzt(bdcdyid) {
        var updateUrl = "${bdcdjUrl}/bdcSjSd/unlockBdcdySd";
        $.ajax({
            type: "POST",
            url: updateUrl,
            data: {bdcdyid: bdcdyid},
            //data:$('#form').serialize(),
            dataType: "json",
            success: function (data) {
                alert(data.message);
                var url = "${bdcdjUrl}/bdcSjSd/getBdcdySdPagesJson?proid=${proid!}";
                tableReload("grid-table", url, {hhSearch: ''}, '', '');
            },
            error: function (data) {
                //alert('');
            }
        });
    }

    function lockBdcdyXzzt(bdcdyid) {
        var updateUrl = "${bdcdjUrl}/bdcSjSd/lockBdcdySd";
        $.ajax({
            type: "POST",
            url: updateUrl,
            data: {bdcdyid: bdcdyid},
            //data:$('#form').serialize(),
            dataType: "json",
            success: function (data) {
                alert(data.message);
                var url = "${bdcdjUrl}/bdcSjSd/getBdcdySdPagesJson?proid=${proid!}";
                tableReload("grid-table", url, {hhSearch: ''}, '', '');
            },
            error: function (data) {
                //alert('');
            }
        });
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }


    function Trim(str, is_global) {
        var result;
        result = str.replace(/(^\s+)|(\s+$)/g, "");
        if (is_global.toLowerCase() == "g") {
            result = result.replace(/\s/g, "");
        }
        return result;
    }


    function openWin(url, name) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function highSearch() {
        $('#highLevelSearch').show();
    }
    function ensureSearch() {
        var jsr = $('#jsrSearch').val();
        var sdr = $('#sdrSearch').val();
        var xzzt = $('#xzztSearch').val();
        var Url = "${bdcdjUrl}/bdcSjSd/getBdcdySdPagesJson?proid=${proid!}&" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url);
    }

    function closeDialog() {
        $("#highLevelSearch").hide();
        $("#gjSearchForm")[0].reset();
        //$(".chosen-select").trigger('chosen:updated');
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
                               data-watermark="权利人/不动产单元号/锁定人/解锁人">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td class="Search">
                        <button type="button" class="btn01 AdvancedButton" id="highlevel_search_bdcdy">高级搜索</button>
                    </td>

                </tr>

            </table>
        </div>
        <div class="tableHeader">
            <#if "${visible!}"!="false">
                <ul>
                    <li>
                        <button type="button" id="lockBdcdy" onclick="lockBdcdy()">
                            <i class="fa fa-lock"></i>
                            <span>冻结</span>
                        </button>
                    </li>
                    <li>
                        <button type="button" id="unlockBdcdy" onclick="unlockBdcdy()">
                            <i class="fa fa-unlock-alt"></i>
                            <span>解冻</span>
                        </button>
                    </li>
                </ul>
            </#if>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="highLevelSearch">
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
                        <div class="row">
                            <div class="col-xs-2">
                                <label>锁定状态:</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="xzzt" class="form-control" id="xzztSearch">
                                    <option value="">请选择</option>
                                    <option value="0">锁定</option>
                                    <option value="1">解锁</option>
                                </select>
                            </div>
                            <div class="col-xs-2">
                                <label>锁定人:</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="sdr" id="sdrSearch" class="form-control">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-2">
                                <label>解锁人:</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="jsr" id="jsrSearch" class="form-control">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="enSureBtn"
                        onclick="ensureSearch()">确定
                </button>
                <button type="button" class="btn btn-sm btn-primary" id="cancelBtn"
                        onclick="closeDialog()">取消
                </button>
            </div>
        </div>
    </div>
</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
    </div>
</div>

<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>

</@com.html>
