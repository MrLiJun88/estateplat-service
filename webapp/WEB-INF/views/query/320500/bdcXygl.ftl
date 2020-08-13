<@com.html title="不动产项目查询" import="ace,public">
<style>
    /*高级搜索样式添加 begin*/
    /*移动modal样式*/
    .modal-dialog {
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

    overflow-x: hidden

    ;
    }

    /*重写下拉列表高度*/
    .chosen-container > .chosen-single, [class*="chosen-container"] > .chosen-single {
        height: 34px;

    .icon_01 {
        display: inline-block;
        width: 16px;
        height: 16px;
        margin-top: 1px;
        line-height: 16px;
        vertical-align: text-top;
        background: url("../img/beenSelect.png") no-repeat;
    }

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

    .simpleSearch {
        width: 100% !important;
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
        initXygl();//初始化信用管理
        var Url = "${bdcdjUrl}/bdcXygl/getSzBdcXyglListJsonByPage?";
        tableReload("grid-table", Url, {dcxc: ''});
        $("#search").click(function () {
            var xmmc = $("#search_xmmc").val();
            tableReload("grid-table", Url, {dcxc: xmmc, zt: ""});
        });
        $("#add").click(function () {
            var xymxids = $("#grid-table").jqGrid('getGridParam', 'selarrrow');
            var url = "${bdcdjUrl}/bdcXygl/viewXymx?type=add&xymxid=";
            if (xymxids && xymxids != 'undefined' && xymxids.length > 0) {
                url += xymxids[0];
                showConfirmDialog("提示", "是否添加勾选信用管理的明细？", "openWin", "'" + url + "'", "", "");
            } else {
                openWin(url);
            }
        });

        $("#update").click(function () {
            var xymxids = $("#grid-table").jqGrid('getGridParam', 'selarrrow');
            if (xymxids == '' || xymxids == null || xymxids == 'undefined') {
                alert("请选择数据");
                return;
            } else if (xymxids.length > 1) {
                alert("请选择一条数据");
                return;
            }
            var url = "${bdcdjUrl}/bdcXygl/viewXymx?type=update&xymxid=" + xymxids;
            openWin(url);

        });
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal, .sdSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

        //initBdcRyxx();//初始化人员信息
        var ryxxUrl = "${bdcdjUrl}/bdcXygl/getBdcRyxxPagesJson";
        tableReload("ryxx-grid-table", ryxxUrl, {
            dcxc: $("#ryxx_search").val()
        });
        $("#ryxx_search_btn").click(function () {
            tableReload("ryxx-grid-table", ryxxUrl, {
                dcxc: $("#ryxx_search").val()
            });
        });

        $("#changeZtLs,#changeZtXs,#changeZtHs").click(function () {
            var zt = "0";
            if (this.id == "changeZtXs")
                zt = "1";
            else if (this.id == "changeZtHs")
                zt = "2";
            tableReload("grid-table", Url, {zt: zt});
        });

        $("#sure").click(function () {
            var ids = $('#grid-table').jqGrid('getGridParam', 'selarrrow');
            if (ids == null || ids == '' || ids == 'undefined') {
                alert("请选择数据");
                return;
            }
            $.ajax({
                url: "${bdcdjUrl}/bdcXygl/changeZtToXs?xymxid=" + ids,
                type: "get",
                success: function (data) {
                    alert(data);
                    tableReload("grid-table", Url, {
                        dcxc: $("#ryxx_search").val()
                    });
                }
            });
        });

        $("#clear").click(function () {
            var ids = $('#grid-table').jqGrid('getGridParam', 'selarrrow');
            if (ids == null || ids == '' || ids == 'undefined') {
                alert("请选择数据");
                return;
            }
            $("#xymxid").val(ids);
            $("#cleaZt").show();
        });

        $("#clearXymxZt").click(function () {
            var ids = $("#xymxid").val();
            var resaon = $("#reason").val();
            $.ajax({
                url: "${bdcdjUrl}/bdcXygl/changeZtToHs",
                type: "post",
                data: {xymxid: ids, reason: resaon},
                success: function (data) {
                    alert(data);
                    $("#cleaZt").reset();
                    $("#cleaZt").hide();
                    tableReload("grid-table", Url, {
                        dcxc: $("#ryxx_search").val()
                    });
                },
                error: function (data) {
                    alert(data);
                }
            })
        });
        $("select[name='zt']").change(function () {
            tableReload("grid-table", Url, {zt: $(this).val()});
        });
    });
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    $(function () {
        /*   文字水印  */
        $(".watermarkText").watermark();
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

        $("#show").click(function () {
            $("#gjSearchPop").show();
        })
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        $("#gjSearchBtn").click(function () {
            var url = "${bdcdjUrl}/bdcXygl/getSzBdcXyglListJsonByPage?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", url, {dcxc: ""});
        });
        $("#delXygl").click(function () {
            debugger;
            var url = "${bdcdjUrl}/bdcXygl/delXygl";
            var ids = $('#grid-table').jqGrid('getGridParam', 'selarrrow');
            delRule(ids, url, "grid-table");
        });
    });
    function resizePage(grid_selector) {
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
    }

    function Look(xymxid) {
        var url = "${bdcdjUrl}/bdcXygl/viewXymx?type=view&xymxid=" + xymxid;
        openWin(url);
    }
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function closeDialog(id) {
        var x = '#' + id;
        $(x).hide();
        clearAddData();
    }
    function clearAddData() {
        $("#addDataForm")[0].reset();
    }
    function showModal() {
        $('#myModal').show();
        relocateDialog('myModal');
    }
    function relocateDialog(id) {
        $(window).trigger('resize.chosen');
        var browser = navigator.appName;
        if (browser == 'Microsoft Internet Explorer') {
            if (id == 'addSel') {
                $(".modal-dialog").css({"margin-left": "2%"});
            } else if (id == 'myModal') {
                $(".modal-dialog").css({"margin-left": "8%"});
            }
        } else {
            if (id == 'addSel') {
                $(".modal-dialog").css({"margin-left": "25%"});
            } else if (id == 'myModal') {
                $(".modal-dialog").css({"margin-left": "8%"});
            }

        }
    }
    function readyBeforeInit(grid_selector, pager_selector, id) {
        //绑定回车键
        enterButton(id);
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
    }
    function initXygl() {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        resizePage(grid_selector);
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/bdcXygl/getSzBdcXyglListJsonByPage",
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'XYMXID'},
            colNames: ['权利人', '证件类型', '证件编号', '通讯地址', '明细内容', '状态', '修改', 'xyglid', 'xymxid'],
            colModel: [
                {name: 'QLRMC', index: 'QLRMC', width: '15%', sortable: false},
                {name: 'ZJLX', index: 'ZJLX', width: '10%', sortable: false},
                {name: 'QLRZJH', index: 'QLRZJH', width: '15%', sortable: false},
                {name: 'QLRTXDZ', index: 'QLRTXDZ', width: '20%', sortable: false},
                {name: 'NR', index: 'NR', width: '25%', sortable: false},
                {name: 'SFXS', index: 'SFXS', width: '5%', sortable: false},
                {
                    name: 'XG',
                    index: 'XG',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        cellvalue = '查看'
                        cell = '<a href="javascript:Look(\'' + rowObject.XYMXID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                        return cell;
                    }
                },
                {name: 'XYGLID', index: 'XYGLID', width: '1%', sortable: false, hidden: true},
                {name: 'XYMXID', index: 'XYMXID', width: '1%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: 50,
            rowList: [10, 20, 30],
            multiboxonly: false,
            multiselect: true,
            fviewrecords: true,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
            },

            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    function initBdcRyxx() {
        var grid_selector = "#ryxx-grid-table";
        var pager_selector = "#ryxx-grid-pager";
        resizePage(grid_selector);
        var cell = '';
        readyBeforeInit(grid_selector, pager_selector, 'sd_search');
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'RYXXID'},
            colNames: ['序列', '权利人', '证件类型', '证件编号', '地址', '联系电话', 'RYXXID'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '3%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM + '</span>'
                    }
                },
                {
                    name: 'RYMC', index: 'RYMC', width: '10%', sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue != null && cellvalue != '') {
                            cell = '<a href="javascript:chooseRyxx(\'' + rowObject.RYXXID + '\',\'' + rowObject.RYMC + '\',\'' + rowObject.ZJLX + '\',\'' + rowObject.ZJBH + '\',\'' + rowObject.TXDZ + '\',\'' + rowObject.ZJLXDM + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                        }
                        else {
                            cell = '';
                        }
                        return cell;
                    }
                },
                {name: 'ZJLX', index: 'ZJLX', width: '5%', sortable: false},
                {name: 'ZJBH', index: 'ZJBH', width: '8%', sortable: false},
                {name: 'TXDZ', index: 'TXDZ', width: '10%', sortable: false},
                {name: 'LXDH', index: 'LXDH', width: '8%', sortable: false},
                {name: 'RYXXID', index: 'RYXXID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: 5,
            rowList: [5, 10, 30],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            rownumbers: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    enableTooltips(table);
                    $(grid_selector).jqGrid('setGridWidth', '810');
                }, 0);
                setHeight(grid_selector);
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
    function enterButton(id) {
        $('#' + id).keydown(function (event) {
            if (event.keyCode == 13) {
                $('#' + id + '_btn').click();
            }
        });
    }
    function setHeight(grid_selector) {
        if ($(grid_selector).jqGrid("getRowData").length == 7) {
            $(grid_selector).jqGrid("setGridHeight", "auto");
        } else {
            $(grid_selector).jqGrid("setGridHeight", "200px");
        }
    }
    function chooseRyxx(ryxxid, rymc, zjlx, zjbh, txdz, zjlxdm) {
        $('#ryxxid').val(ryxxid)
        $('#qlr').val(rymc);
        $('#zjlx').val(zjlx);
        $('#zjbh').val(zjbh);
        $('#txdz').val(txdz);
        $('#qlrsfzjzl').val(zjlxdm);
        closeModal();
    }
    function closeModal() {
        $('#myModal').hide();
        relocateDialog('addSel');
    }
    function addXygl() {
        var qlr = $('#qlr').val();
        var qlrzjh = $('#qlrzjh').val();
        if ((qlr == '' || qlr == 'undefined') || (qlrzjh == '' || qlrzjh == 'undefined')) {
            alert('请选择权利人！')
            return false;
        }
        var remoteUrl = "${bdcdjUrl}/bdcXygl/addBdcXygl";
        var sdData = $("#addDataForm").serialize();
        $.ajax({
            type: "POST",
            url: remoteUrl,
            data: sdData,
            dataType: "json",
            success: function (data) {
                $("#addData").hide();
                alert(data.message);
                reload();
            },
            error: function (data) {
                alert('添加失败');
            }
        });
    }
    function reload() {
        $('#search').click();
        clearAddData();
    }
    //删除判断是否没有选择数据
    function delRule(ids, url, gridId) {
        if (ids.length == 0) {
            tipInfo("请选择一条数据!");
            return;
        }
        $.blockUI({message: "请稍等……"});
        bootbox.dialog({
            message: "是否删除？",
            title: "",
            closeButton: false,
            buttons: {
                success: {
                    label: "确定",
                    className: "btn-success",
                    callback: function () {
                        $.getJSON(url + "?ids=" + ids, {}, function (jsonData) {
                            setTimeout($.unblockUI, 10);
                            alert(jsonData.result);
                            $('#' + gridId).trigger("reloadGrid");
                        })
                    }
                },
                main: {
                    label: "取消",
                    className: "btn-primary",
                    callback: function () {
                        setTimeout($.unblockUI, 10);
                    }
                }
            }
        });
    }
    function openWin(url) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        var openner = window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
        if (window.opener && !window.opener.closed) {
            alert(openner);
        }
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
                               data-watermark="请输入权利人/证件编号">
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
                    <td style="padding-left: 10px;border-width: 0px"></td>
                    <td style="border: 0px">
                        <select name="zt" id="ztSelect" class="form-control" style="width: 150px;">
                            <option value="">请选择状态...</option>
                            <option value="0">临时</option>
                            <option value="1">现势</option>
                            <option value="2">历史</option>
                        </select>
                    </td>
                </tr>
            </table>
        </div>

        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="add">
                        新增
                    </button>
                </li>
            </ul>
            <ul>
                <li>
                    <button type="button" id="update">
                        修改
                    </button>
                </li>
            </ul>
            <ul>
                <li>
                    <button type="button" id="delXygl">
                        删除
                    </button>
                </li>
            </ul>
            <ul>
                <li>
                    <button type="button" id="sure">
                        修改状态为现势
                    </button>
                </li>
            </ul>
            <ul>
                <li>
                    <button type="button" id="clear">
                        修改状态为历史
                    </button>
                </li>
            </ul>
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
                            <label>证件编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zjh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>通讯地址：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="txdz" class="form-control">
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
<!--添加-->
<div class="Pop-upBox moveModel  ui-draggable" style="display: none;" id="addData">
    <div class="modal-dialog gjSearchPop-modal ui-draggable">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>新增</h4>
                <button type="button" id="sdHide" class="proHide" onclick="closeDialog('addData')"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="addDataForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlrmc" id='qlr' class="form-control" readonly="readonly">
                        </div>
                        <div class="col-xs-2">
                            <label>通讯地址：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlrtxdz" id='txdz' class="form-control" readonly="readonly">
                        </div>

                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证件类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zjlx" id='zjlx' class="form-control" readonly="readonly">
                        </div>
                        <div class="col-xs-2">
                            <label>证件编号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlrzjh" id='zjbh' class="form-control" readonly="readonly">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>备注：</label>
                        </div>
                        <div class="col-xs-10">
                            <input type="text" name="bz" id='bz' class="form-control">
                        </div>
                    </div>
                    <div style="display: none;">
                        <input type="hidden" name="ryxxid" id='ryxxid'>
                        <input type="hidden" name="qlrsfzjzl" id='qlrsfzjzl'>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" style='margin-right: 30px' id="selectBdcdy"
                        onclick="showModal()">选择权利人
                </button>
                <button type="button" class="btn btn-sm btn-primary" id="addXygl" onclick="addXygl()">添加</button>
            </div>
        </div>

    </div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="cleaZt">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>新增</h4>
                <button type="button" id="sdHide" class="proHide" onclick="closeDialog('cleaZt')"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="cleaZtForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>解除生效原因：</label>
                        </div>
                        <div class="col-xs-8">
                            <input type="text" name="reason" id='reason' class="form-control">
                        </div>
                        <div class="col-xs-2" style="display: none;">
                            <input type="hidden" name="xymxid" id='xymxid'>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="clearXymxZt">消除</button>
            </div>
        </div>

    </div>
</div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
