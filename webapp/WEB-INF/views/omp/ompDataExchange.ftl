<@com.html title="一张图数据交互" import="ace,public">
<style>
    a {
        color: #428bca;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

    /*移动modal样式*/
    #djsjSearchPop .modal-dialog {
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

    /*移动modal样式*/
    #tipPop .modal-dialog {
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

    .alert {
        font-size: 12px;
        border-radius: 4px;
        padding: 5px;
        margin-bottom: 5px;
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

    .simpleSearch .simpleInput {
        border: 0px;
        width: 300px;
        height: 34px;
        font-size: 12px;
        text-indent: 10px;
    }

    .simpleSearch {
        width: 1200px;
        height: 36px;
        margin: 0px 0px 10px 0px;
        position: relative;
    }

</style>
<script type="text/javascript">
    var contractInfo = '';
    if (isNotBlank('${contractInfo!}')) {
        contractInfo = JSON.parse('${contractInfo!}');
    }
    $(function () {
        /*   文字水印  */
        $(".watermarkText").watermark();

        //搜索事件
        $("#search_btn").click(function () {
            if (isBlank($("#hthSearchCondition").val())) {
                tipInfo("请输入合同号！");
                return;
            }
            window.location.href = "${bdcdjUrl!}/ompData/ompDataExchange?proid=${proid!}&hthSearchCondition=" + $("#hthSearchCondition").val();
        });


        $("#hthSearchCondition").keydown(function (event) {
            if (event.keyCode == 13) { //绑定回车
                $("#search_btn").click();
            }
        });

        $("#imp_btn").click(function () {
            if (isBlank('${contractInfo!}') || isNotBlank(contractInfo.msg)) {
                tipInfo("无合同信息，无法导入！");
            } else {
                impContractInfo('${contractInfo!}');
            }
        });

        //默认初始化表格
        initTable();
    });

    function initTable() {
        var grid_selector = "#grid-table";

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
            datatype: "local",
            height: 'auto',
            jsonReader: 'hth',
            colNames: ["合同号", '项目名称'],
            colModel: [
                {name: 'hth', index: 'hth', width: '5%', sortable: false},
                {name: 'xmmc', index: 'xmmc', width: '11%', sortable: false}
            ],
            subGrid: true,
            subGridOptions: {
                plusicon: "ace-icon fa fa-plus  bigger-110 blue",
                minusicon: "ace-icon fa fa-minus  bigger-110 blue",
                openicon: "ace-icon fa fa-chevron-right  orange",
                expandOnLoad: true
            },
            subGridRowExpanded: function (subgrid_id, row_id) {
                var subgrid_table_id = subgrid_id + "_t";
                $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' style='width:400px' ></table>");
                $("#" + subgrid_table_id).jqGrid({
                    datatype: "local",
                    jsonReader: 'dkid',
                    colNames: ['地块名称'],
                    colModel: [
                        {name: "dkmc", index: "dkmc", sortable: false}
                    ],
                    height: 'auto',
                    loadComplete: function () {

                        if (isNotBlank(contractInfo) && isNotBlank(contractInfo.dkinfo)) {
                            var dkInfo = contractInfo.dkinfo;
                            var rowData = [{dkid: dkInfo.dkid, dkmc: dkInfo.dkmc}];
                            $.each(rowData, function (index, data) {
                                $("#" + subgrid_table_id).jqGrid('addRowData', data.dkid, data);

                            });
                            $("#" + subgrid_table_id).jqGrid('setGridWidth', 400);
                            $(".ui-jqgrid-bdiv").css('overflow-x', 'hidden');
                        }


                    }
                });
            },
            loadComplete: function () {
                if (isNotBlank(contractInfo)) {
                    if (isNotBlank(contractInfo.msg)) {
                        tipInfo(contractInfo.msg);
                    } else {
                        var rowData = [{hth: contractInfo.hth, xmmc: contractInfo.xmmc}];
                        $.each(rowData, function (index, data) {
                            $(grid_selector).jqGrid('addRowData', data.hth, rowData[index]);
                        });
                    }
                }
            },
            autowidth: true
        });
    }

    var impContractInfo = function (contractInfoStr) {
        $.ajax({
            url: '${bdcdjUrl!}/ompData/impContractInfo',
            data: {contractInfo: contractInfoStr, bdcdyh: '${bdcdyh!}', proid: '${proid!}'},
            success: function (result) {
                if (result == 'FAIL') {
                    tipInfo("导入失败！");
                } else if (result == 'SUCCESS') {
                    extractAttachment(contractInfoStr);
                    showConfirmDialog("附件管理", "导入成功，是否进行附件管理？", "fileOperate", "'" + '${proid!}' + "','" + contractInfo.proid + "'", "closeModel", "");
                }
            }
        });
    }

    function extractAttachment(contractInfoStr) {
        $.ajax({
            url: '${bdcdjUrl!}/ompData/extractAttachment',
            data: {contractInfo: contractInfoStr, proid: '${proid!}'},
            success: function (result) {

            }
        });
    }

    var fileOperate = function (bdcProid, ompProid) {
        //showIndexModel("${unifiedPlatformHost!}/portal/fileCenter/selectOther?RELOAD_IFRAME_URL=true&fromProid=" + ompProid + "&toProid=" + bdcProid + "&handleType = task", "附件管理", null, null, true);
        showWindow("${fileCenterUrl!}", "附件管理", 900, 900);
    }

    var closeModel = function () {
        window.parent.hideModel();
        window.parent.resourceRefresh();
    }

</script>
<div class="main-container">
    <div class="space-6"></div>
    <div class="page-content">
        <div class="row">
            <div class="simpleSearch">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="text" class="simpleInput watermarkText" id="hthSearchCondition"
                                   data-watermark="请输入合同号" value="${hthSearchCondition!}">
                        </td>
                        <td style="border: 0px">&nbsp;</td>
                        <td class="Search">
                            <a href="#" id="search_btn">
                                搜索
                                <i class="ace-icon fa fa-search bigger-130"></i>
                            </a>
                        </td>
                        <td style="border: 0px">&nbsp;</td>
                        <td class="Search">
                            <a href="#" id="imp_btn">
                                导入
                                <i class="ace-icon fa fa-share bigger-130"></i>
                            </a>
                        </td>
                    </tr>
                </table>
            </div>
            <table id="grid-table"></table>
        </div>
    </div>
</div>
<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<form>
    <input id="hth" hidden>
    <input id="bdcdyh" value="${bdcdyh!}" hidden>
    <input id="proid" value="${proid!}" hidden>
</form>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>