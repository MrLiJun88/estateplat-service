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
        width: 600px;
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

    $(function () {
        fwTableInit();
        var hhSearch = $("#fw_search_qlr").val();
        if ("${bdclx!}" == "TDFW")
            var fwUrl = "${bdcdjUrl}/bdcSjgl/getFwZsJsonByPage?fwid=${fwid!}";
        else
            var fwUrl = "${bdcdjUrl}/bdcSjgl/getFwZsJsonByPage?tdid=${tdid!}";
        tableReload("fw-grid-table", fwUrl, {hhSearch: hhSearch}, '', '');


        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth = $(".tab-content").width();
            $("#fw-grid-table").jqGrid('setGridWidth', contentWidth);
        });

        /*   文字水印  */
        $(".watermarkText").watermark();

        $('#fw_search_qlr').keydown(function (event) {
            if (event.keyCode == 13) {
                $("#fw_search").click();
            }
        });

    })

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

    function showZsQl(qlid) {
        var url = "${bdcdjUrl!}/bdcSjgl/showZsQl?showpage=showql&qlid=" + qlid;
        openWin(url);
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
            jsonReader: {id: 'QLID'},
            colNames: ['QLID', 'QLLX', 'ZSLX', '房产证号', '证书状态', '权利状态' , '查看'],
            colModel: [
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
                {name: 'QLLX', index: 'QLLX', width: '0%', sortable: false, hidden: true},
                {name: 'ZSLX', index: 'ZSLX', width: '0%', sortable: false, hidden: true},
                {name: 'FCZH', index: 'FCZH', width: '15%', sortable: false},
                {name: 'ZSZT', index: 'ZSZT', width: '10%', sortable: false},
                {name: 'QLZT', index: 'QLZT', width: '10%', sortable: false},
                {
                    name: 'mydy',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<div title="查看详细权利" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="showZsQl(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>'

                    }
                }
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: true,
            multiselect: true,
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
                    getZszt(data.QLID, $(grid_selector), data.ZSZT, data.ZSLX);
                })
            },
            editurl: "",
            caption: "",
            autowidth: true
        });
    }

    function getZszt(qlid, table, iszx, zslx) {
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcSjgl/getGdZsZt",
            data: {
                qlid: qlid,
                zslx: zslx
            },
            success: function (result) {
                if (isNotBlank(result.ZSZT)) {
                    if (result.ZSZT == '0') {
                        iszx = '<span class="label label-success">现势</span>';
                    } else {
                        iszx = '<span class="label label-gray">历史</span>';
                    }
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdXxByQlid?&qlid=" + qlid,
                        success: function (result) {
                            asyncGetGdXxByQlid(table, qlid, $bdclx);
                        }
                    });
                } else {
                    iszx = '<span class="label label-gray">历史</span>';
                }
                table.setCell(qlid, "ZSZT", iszx);
            }
        });
    }

    function asyncGetGdXxByQlid(table, qlid, bdcLx) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdXxByQlid?bdclx=" + bdcLx + "&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                //正常
                var zls = result.zls;
                if (isBlank(result.qlzts)) {
                    cellVal += '<span class="label label-success">正常</span>';
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson?bdclx=" + bdcLx + "&zl=" + zls,
                        dataType: "json",
                        async: false,
                        success: function (result) {
                            if (result.rows != '') {
                                $.ajax({
                                    type: "GET",
                                    url: "${bdcdjUrl}/queryBdcdy/getBdcdyhQlxx?bdcdyh=" + result.rows[0].BDCDYH,
                                    dataType: "json",
                                    async: false,
                                    success: function (result) {
                                        if (!result.dy) {
                                            cellVal = '';
                                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                                        }
                                        if (!result.cf) {
                                            cellVal = '';
                                            cellVal += '<span class="label label-danger">查封</span><span> </span>';
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    var qlzts = result.qlzts.split(",");
                    for (var i = 0; i < qlzts.length; i++) {
                        //zhouwanqing 防止其他权利与注销同在
                        if (qlzts[i] == "ZX") {
                            cellVal = '<span class="label label-danger">注销</span><span> </span>';
                            break;
                        } else if (qlzts[i] == "DY") {
                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        } else if (qlzts[i] == "CF") {
                            cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        } else if (qlzts[i] == "YG") {
                            cellVal += '<span class="label label-info">预告</span><span> </span>';
                        } else if (qlzts[i] == "YY") {
                            cellVal += '<span class="label label-info">异议</span><span> </span>';
                        } else if (qlzts[i] == "DGQLZT") {
                            cellVal += '<span class="label label-info">多个权利状态</span>';
                        }
                    }
                }
                table.setCell(qlid, "QLZT", cellVal);
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

</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="mainContent">
        <iframe id='showQl' name="showQl"
            <#if "${bdclx!}"=="TDFW">
                src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&fwid=${fwid!}&iscp=true&editFlag=false"
            </#if>
            <#if "${bdclx!}"=="TD">
                src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&tdid=${tdid!}&iscp=true&editFlag=false"
            </#if>
                frameborder="no"
                border="0" marginwidth="0" marginheight="0"
                scrolling="no" allowtransparency="yes" width="99.1%" height="410px"></iframe>

        <div class="space-4"></div>
        <table id="fw-grid-table"></table>
        <div id="fw-grid-pager"></div>
    </div>
</div>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
