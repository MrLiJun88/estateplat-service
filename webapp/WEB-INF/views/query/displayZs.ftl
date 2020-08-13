<@com.html title="" import="ace,public">
<style>
    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    /*移动modal样式*/
    #lqSearchPop .modal-dialog, #cqSearchPop .modal-dialog, #fcSearchPop .modal-dialog, #tdSearchPop .modal-dialog {
        width: 800px;
        position: fixed;
        top: 20px;
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

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
    }
</style>
<script type="text/javascript">
    //table每页行数
    $rownum = 10;
    //table 每页高度
    $pageHight = '370px';
    //全局的不动产类型
    $bdclx = 'TDFW';
    fwColNames = ['档案号', '房屋坐落', '规划用途', '建筑面积', '所在层', '总层数'];
    fwColModel = [
        {name: 'DAH', index: 'DAH', width: '8%', sortable: false},
        {name: 'FWZL', index: 'FWZL', width: '30%', sortable: false},
        {name: 'GHYT', index: 'GHYT', width: '16%', sortable: false},
        {name: 'JZMJ', index: 'JZMJ', width: '12%', sortable: false},
        {name: 'SZC', index: 'SZC', width: '12%', sortable: false},
        {name: 'ZCS', index: 'ZCS', width: '12%', sortable: false}
    ];
    tdColNames = ['地籍号', '土地坐落', '规划用途', '宗地面积'];
    tdColModel = [
        {name: 'DJH', index: 'DAH', width: '12%', sortable: false},
        {name: 'ZL', index: 'ZL', width: '30%', sortable: false},
        {name: 'YT', index: 'YT', width: '16%', sortable: false},
        {name: 'ZDMJ', index: 'ZDMJ', width: '12%', sortable: false}
    ];
    $(function () {
        $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
            //关掉高级查询
            $("#lqHide,#cqHide,#fcHide,#tdHide").click();
            if (this.id == "fwTab") {
                $bdclx = 'TDFW';
                $("#fw").addClass("active");
                var fwUrl = "${bdcdjUrl}/bdcJgSjgl/getGdInfoJson?" + $("#fcSearchForm").serialize();
                fwTableInit();
                if (isLoadGrid("fw"))
                    tableReload("fw-grid-table", fwUrl, {hhSearch: '', rf1Dwmc: ''}, '', '');
            }
        });

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth = $(".tab-content").width();
            $("#fw-grid-table,#lq-grid-table,#cq-grid-table,#td-grid-table").jqGrid('setGridWidth', contentWidth);
        });

        /*初始化列表*/
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = gdTabOrder.split(",");
        if (gdTabOrderArray != null && gdTabOrderArray.length > 0) {
            if (gdTabOrderArray[0] == 'td')
                $("#tdTab").click();
            else if (gdTabOrderArray[0] == 'lq')
                $("#lqTab").click();
            else if (gdTabOrderArray[0] == 'cq')
                $("#cqTab").click();
            else
                $("#fwTab").click();

            $(window).trigger("resize.jqGrid");
        }
        /*   文字水印  */
        $(".watermarkText").watermark();

        //添加空选项
        var option = $("<option>").val('').text('');
        $("#djlx").prepend(option);

        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".tdSearchPop-modal,.fcSearchPop-modal,.lqSearchPop-modal,.cqSearchPop-modal").draggable({
            opacity: 0.7,
            handle: 'div.modal-header'
        });

        //房产高级查询的搜索按钮事件
        $("#fcGjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcSjgl/getGdXmFwJsonGaoByPage?" + $("#fcSearchForm").serialize();
            tableReload("fw-grid-table", Url, {hhSearch: "", rf1Dwmc: ''});
        })

        $("#exportExcel").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            hhSearch = encodeURI(encodeURI(hhSearch));
            var url = "${reportUrl!}/ReportServer?reportlet=edit%2Fbdc_zs_export.cpt&op=write&hhSearch=" + hhSearch;
            openWin(url);
        })

        /*高级按钮点击事件 begin*/
        $("#lqShow,#cqShow,#tdShow,#fcShow").click(function () {
            if (this.id == "lqShow") {
                $("#lqSearchPop").show();
                $(".modal-dialog").css({"_margin-left": "25%"});
            } else if (this.id == "tdShow") {
                $("#tdSearchPop").show();
                $(".modal-dialog").css({"_margin-left": "25%"});
            } else if (this.id == "fcShow") {

                $("#fcSearchPop").show();
                $(".fcSearchPop-modal").css({
                    opacity: 0.9,
                    handle: 'div.modal-header'
                });
                $(".modal-dialog").css({"_margin-left": "25%"});
            } else if (this.id == "cqShow") {
                $("#cqSearchPop").show();
                $(".modal-dialog").css({"_margin-left": "25%"});
            }
        });
        //高级查询弹出框隐藏
        $("#lqHide,#cqHide,#fcHide,#tdHide").click(function () {
            if (this.id == "cqHide") {
                $("#cqSearchPop").hide();
                $("#cqSearchForm")[0].reset();
            } else if (this.id == "fcHide") {
                $("#fcSearchPop").hide();
                $("#fcSearchForm")[0].reset();
            } else if (this.id == "tdHide") {
                $("#tdSearchPop").hide();
                $("#tdSearchForm")[0].reset();
            } else if (this.id == "lqHide") {
                $("#lqSearchPop").hide();
                $("#lqSearchForm")[0].reset();
            }
        });

        //高级查询弹出框关闭隐藏
        $("#fcGjCloseBtn,#tdGjCloseBtn,#lqGjCloseBtn,#cqGjCloseBtn").click(function () {
            if (this.id == "cqGjCloseBtn") {
                $("#cqSearchPop").hide();
                $("#cqSearchForm")[0].reset();
            } else if (this.id == "fcGjCloseBtn") {
                $("#fcSearchPop").hide();
                $("#fcSearchForm")[0].reset();
            } else if (this.id == "tdGjCloseBtn") {
                $("#tdSearchPop").hide();
                $("#tdSearchForm")[0].reset();
            } else if (this.id == "lqGjCloseBtn") {
                $("#lqSearchPop").hide();
                $("#lqSearchForm")[0].reset();
            }
        });
        /*绑定回车事件*/
        $('input').focus(function () {
            var id = $(this).attr('id');
            if (id == 'fw_search_qlr') {
                $('#fw_search_qlr').keydown(function (event) {
                    if (event.keyCode == 13) {
                        $('#fw_search').click();
                    }
                });
            }
        });

        //查询按钮点击事件
        $("#fw_search").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/bdcJgSjgl/getGdInfoJson?" + $("#fcSearchForm").serialize();
            tableReload("fw-grid-table", fwUrl, {hhSearch: hhSearch, rf1Dwmc: ''}, '', '');
        });

//新增按钮点击事件
        $("#gdFwAdd,#gdLqAdd,#gdTdAdd,#gdCqAdd").click(function () {
            var bdclxId = this.id;
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/gdXxLr/getUUid",
                dataType: "json",
                success: function (result) {
                    if (result != null && result != "") {
                        if (bdclxId == "gdFwAdd") {
                            addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=fw&proid=" + result);
                        } else if (bdclxId == "gdLqAdd") {
                            addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write");
                        } else if (bdclxId == "gdTdAdd") {
                            addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=td&proid=" + result);
                        } else if (bdclxId == "gdCqAdd") {
                            addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write");
                        }
                    }
                }
            });
        });
        //修改按钮点击事件
        $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdCqUpdate").click(function () {
            if (this.id == "gdFwUpdate") {
                updateGdsj($("#fw-grid-table"), "${bdcdjUrl!}/gdXxLr?bdclx=fw&editFlag=true&proid=");
            } else if (this.id == "gdLqUpdate") {
                updateGdsj($("#lq-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=");
            } else if (this.id == "gdTdUpdate") {
                updateGdsj($("#td-grid-table"), "${bdcdjUrl!}/gdXxLr?bdclx=td&editFlag=true&proid=");
            } else if (this.id == "gdCqUpdate") {
                updateGdsj($("#cq-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=");
            }
        });
    });

    function showFjGl(qlid, bdclx, bdcid) {
        if (bdclx == 'TD') {
            url = "${bdcdjUrl!}/gdXxLr?bdclx=td&gdTabOrder=fj&proid=" + qlid;
        } else {
            url = "${bdcdjUrl!}/bdcJgSjgl/displayZsRel?&showpage=fjgl&qlid=" + qlid + "&bdcid=" + bdcid;
        }
        openWin(url);
    }

    //修改过度数据
    function updateGdsj(table, url) {
        var ids = table.jqGrid('getGridParam', 'selarrrow');
        if (ids.length == 0) {
            tipInfo("请选择一条要修改的数据!");
        } else if (ids.length > 1) {
            tipInfo("注意:只能选择一条数据进行修改!");
        } else {
            addOrUpdate(url + ids[0]);
        }
    }

    function getBdcLxQlIdsCqZhQlr(rowId, table, bdcLx, qlId, cqZh, qlr) {
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcJgSjgl/getBdcLxAndQlIdsByGdId?gdId=" + rowId,
            dataType: "JSON",
            success: function (result) {
                bdcLx = result.BDCLX;
                qlId = result.QLID;
                table.setCell(rowId, "BDCLX", bdcLx);
                table.setCell(rowId, "QLID", qlId);
                $.ajax({
                    type: "POST",
                    url: "${bdcdjUrl}/bdcJgSjgl/getCqZhAndQlrByQlId?qlId=" + qlId,
                    success: function (result) {
                        cqZh = result.CQZH;
                        qlr = result.QLR;
                        table.setCell(rowId, "CQZH", cqZh);
                        table.setCell(rowId, "QLR", qlr);
                        getZsZt(result.ZSZT, table, rowId);
                    }
                });
            }
        });
    }

    function tableReload(table, Url, data, colModel, loadComplete) {
        var index = 0;
        var jqgrid = $("#" + table);
        if (colModel == '' && loadComplete == '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        } else if (loadComplete == '' && colModel != '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, colModel: colModel});
        } else if (loadComplete != '' && colModel != '') {
            jqgrid.setGridParam({
                url: Url,
                datatype: 'json',
                page: 1,
                postData: data,
                colModel: colModel,
                loadComplete: loadComplete
            });
        }
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function fwTableInit() {
        var grid_selector = "#fw-grid-table";
        var pager_selector = "#fw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'GDID'},
            colNames: ['产权证号', '坐落', '权利人', '证书状态', '操作', 'BDCLX', 'QLID', 'GDID'],
            colModel: [
                {name: 'CQZH', index: 'CQZH', width: '15%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '20%', sortable: false},
                {name: 'ZSZT', index: 'ZSZT', width: '8%', sortable: false},
                {
                    name: 'OPERATE', index: '', width: '8%', sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div title="查看证书" style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="showZsRel(\'' + rowObject.GDID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-columns  bigger-120 blue"></span></div>' +
                                '<div title="查看附件管理" style="float:left;margin-left:8px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="showFjGl(\'' + rowObject.PROID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.FWID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-file  bigger-120 blue"></span></div>'
                    }
                },
                {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
                {name: 'GDID', index: 'GDID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: true,
            multiselect: true,
            editurl: "",
            caption: "",
            autowidth: true,
            subGrid: true,
            subGridOptions: {
                plusicon: "ace-icon fa fa-plus center bigger-110 blue",
                minusicon: "ace-icon fa fa-minus center bigger-110 blue",
                openicon: "ace-icon fa fa-chevron-right center orange"
            },
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getBdcLxQlIdsCqZhQlr(data.GDID, $(grid_selector), data.BDCLX, data.QLID, data.CQZH, data.QLR);
                });
            },
            subGridRowExpanded: function (subGridId, rowId) {
                var subgridTableId = subGridId + "_t";
                var tableHtml = "<table id='" + subgridTableId + "'></table>";
                $("#" + subGridId).html(tableHtml);
                var subGridData = $(grid_selector).jqGrid("getRowData", rowId);
                var bdcLx = subGridData.BDCLX;
                var url = "${bdcdjUrl}/bdcJgSjgl/getGdEntityJson?gdId=" + rowId + "&bdcLx=" + bdcLx;
                var height = 250;
                if (bdcLx == 'TDFW') {
                    $("#" + subgridTableId).jqGrid({
                        url: url,
                        datatype: "json",
                        jsonReader: {id: 'GDID'},
                        height: height,
                        colNames: fwColNames,
                        colModel: fwColModel,
                        autowidth: true
                    });
                } else if (bdcLx == 'TD') {
                    $("#" + subgridTableId).jqGrid({
                        url: url,
                        datatype: "json",
                        jsonReader: {id: 'GDID'},
                        height: height,
                        colNames: tdColNames,
                        colModel: tdColModel,
                        autowidth: true
                    });
                }
            }
        });
    }

    //获取过渡证书状态
    function getZsZt(zszt, table, rowid) {
        var zsztCell = '';
        if (isNotBlank(zszt)) {
            zszt.split('$').forEach(function (data) {
                if (data == 0) {
                    zsztCell += '<span class="label label-success">现势</span>　';
                } else {
                    zsztCell += '<span class="label label-gray">历史</span>　';
                }
            });
            table.setCell(rowid, "ZSZT", zsztCell);
        }
    }

    //查看证书关系
    function showZsRel(gdId) {
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcJgSjgl/getBdcLxAndQlIdsByGdId?gdId=" + gdId,
            success: function (result) {
                var url = "${bdcdjUrl!}/bdcJgSjgl/displayZsRel?qlId=" + result.QLID + "&bdcLx=" + result.BDCLX;
                var windowHeight = $(window).height();
                var windowWidth = $(window).width();
//                showIndexModel(url, '证书详细信息', windowWidth, windowHeight * 1.2, false);
                open(url, '证书详细信息');
            }
        });

    }

    //open新窗口
    function addOrUpdate(url) {
        openWin(url);
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
    /**
     * 根据key获取是否加载数据
     * @param key
     * @returns {boolean}
     */
    function isLoadGrid(key) {
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        var gdTabLoadData = "${gdTabLoadData!}";
        var gdTabLoadDataArray = new Array();
        gdTabLoadDataArray = gdTabLoadData.split(",");
        if (gdTabOrderArray.length == gdTabLoadDataArray.length) {
            for (var i = 0; i < gdTabOrderArray.length; i++) {
                if (gdTabOrderArray[i] == key) {
                    var loadGrid = gdTabLoadDataArray[i];
                    if (loadGrid == 1) {
                        return true;
                        break;
                    } else {
                        return false;
                        break;
                    }
                }
            }
            return false;
        } else
            return false;
    }
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="row">
            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li>
                        <a data-toggle="tab" id="fwTab" href="#fw">
                            证书查询
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="fw" class="tab-pane in active">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                               data-watermark="请输入权利人/坐落/产权证号">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="fw_search">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                    <td style="border: 0px">&nbsp;</td>
                                    <td>
                                        <button type="button" class="btn01 AdvancedButton" id="fcShow">高级搜索</button>
                                    </td>
                                    <td>
                                        <button type="button" class="btn01 AdvancedButton" id="exportExcel">导出</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="tableHeader">
                            <ul>
                                <li>
                                    <button type="button" name="add" id="gdFwAdd">
                                        <i class="ace-icon fa fa-file-o"></i>
                                        <span>新增</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" name="update" id="gdFwUpdate">
                                        <i class="ace-icon fa fa-pencil-square-o"></i>
                                        <span>修改</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                        <table id="fw-grid-table"></table>
                        <div id="fw-grid-pager"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--房产证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="fcSearchPop">
    <div class="modal-dialog fcSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="fcHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fcSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>权利人证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlrzjh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>档案号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="dah" class="form-control">
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
                            <label>证书号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zsh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>证书类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select id="zslx" name="zslx" class="form-control" style="margin-left: 5px;">
                                <option value=""></option>
                                <option value="tdz">土地证</option>
                                <option value="txz">他项证</option>
                                <option value="fcz">房产证</option>
                                <option value="ftz">房他证</option>
                                <option value="fyz">房预证</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>规划用途：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="ghyt" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>房屋结构：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fwjg" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>登记类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select id="djlx" name="djlx" class="form-control" style="margin-left: 5px;">
                                <option selected="selected"></option>
                                <#list djlxList! as djlx>
                                    <option value="${djlx.DM!}">${djlx.MC!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="fcGjSearchBtn">搜索</button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-sm btn-primary" id="fcGjCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<!--土地证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="tdSearchPop">
    <div class="modal-dialog tdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="tdHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="tdSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
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
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>宗地面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zdmj" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>用途：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="yt" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tdGjSearchBtn">搜索</button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-sm btn-primary" id="tdGjCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<!--林权证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="lqSearchPop">
    <div class="modal-dialog lqSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="lqHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="lqSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>林权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lqzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lqzl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>承包面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="syqmj" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>林种：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lz" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="lqGjSearchBtn">搜索</button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-sm btn-primary" id="lqGjCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<!--草权高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="cqSearchPop">
    <div class="modal-dialog cqSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="cqHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="cqSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>草权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cqzh" class="form-control">
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
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>承包面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cbmj" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="cqGjSearchBtn">搜索</button>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-sm btn-primary" id="cqGjCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
</@com.html>
