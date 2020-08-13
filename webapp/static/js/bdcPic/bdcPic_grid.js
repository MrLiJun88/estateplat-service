$(function () {
    djsjInitTable();
    $("#bdcdyTab").click(function () {
        removewAllActive();
        $("#bdcdyModel").addClass("active");
        djsjInitTable();
    })
    $("#djsjbdcdy_searchBtn").click(function () {
        var fwdm = $("#djsjbdcdy_fwbm").val();
        var zl = $("#djsjbdcdy_zl").val();
        var bdcdyh = $("#djsjbdcdy_bdcdyh").val();
        var Url = bdcdjUrl + "/bdcpic/getDjsjBdcdyByPage";
        tableReload("djsj-grid-table", Url, {fwdm: fwdm, zl: zl, bdcdyh: bdcdyh, bdclx: ml_bdclx});
    });

    $("#tdTab").click(function () {
        removewAllActive();
        $("#tdModel").addClass("active");
        tdInitTable();
        if (td_bdcqzh != "") {
            $("#td_bdcqzh").val(td_bdcqzh);
            setTimeout(function () {
                $("#td_searchBtn").click()
            }, 500)
        }
        if (wwslbh != "" && ml_bdcqzh != "") {
            $.ajax({
                type: "GET",
                url: bdcdjUrl + "/bdcpic/getGxWwSqxx?wwslbh=" + wwslbh+"&fczh="+ml_bdcqzh,
                dataType: "json",
                success: function (data) {
                    if (data.tdxmid != "") {
                        td_proid=data.tdxmid;
                        $("#td_searchBtn").click();
                    }
                },
                error: function () {
                    tipInfo("error");
                }
            });

        }
    })
    $("#td_searchBtn").click(function () {
        var bdcqzh = $("#td_bdcqzh").val();
        var zl = $("#td_zl").val();
        var qlr = $("#td_qlr").val();
        var Url = bdcdjUrl + "/bdcpic/getBdcZsByPage";
        tableReload("td-grid-table", Url, {qlr: qlr, zl: zl, bdcqzh: bdcqzh, tdProid: td_proid});
    });
})


function djsjInitTable() {
    var grid_selector = "#djsj-grid-table";
    var pager_selector = "#djsj-grid-pager";
    //绑定回车键
    $('#djsjbdcdy_qlr,#djsjbdcdy_zl').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsjbdcdy_searchBtn").click();
        }
    });
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });

    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });

    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'ID'},
        colNames: ['地籍号', '不动产单元号', '坐落', '权利人', '不动产类型', 'ID'],
        colModel: [
            {name: 'DJH', index: 'DJH', width: '14%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '20%', sortable: false},
            {name: 'TDZL', index: 'TDZL', width: '20%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
            {
                name: 'BDCLX', index: '', width: '7%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var value = "";
                    if (cellvalue != null && cellvalue != "") {
                        if (cellvalue.indexOf('TD') > -1) {
                            if (cellvalue.indexOf('FW') > -1) {
                                value = "土地、房屋";
                            } else if (cellvalue.indexOf('GZW') > -1)
                                value = "土地、构筑物";
                            else if (cellvalue.indexOf('SL') > -1)
                                value = "土地、森林、林木";
                            else if (cellvalue.indexOf('QT') > -1)
                                value = "土地、其他";
                            else
                                value = "土地";
                        } else if (cellvalue.indexOf('HY') > -1) {
                            if (cellvalue.indexOf('FW') > -1) {
                                value = "海域、房屋";
                            } else if (cellvalue.indexOf('GZW') > -1)
                                value = "海域、构筑物";
                            else if (cellvalue.indexOf('WJM') > -1)
                                value = "海域、无居民海岛";
                            else if (cellvalue.indexOf('SL') > -1)
                                value = "海域、森林、林木";
                            else if (cellvalue.indexOf('QT') > -1)
                                value = "海域、其他";
                            else
                                value = "海域";
                        } else if (cellvalue.indexOf('QT') > -1) {
                            value = "其他";
                        }
                    }
                    if (value == "") {
                        value = cellvalue;
                    }
                    return value;
                }
            },
            {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        beforeRequest: function () {
            $(pager_selector + "_right").hide();
            var gridId = grid_selector.substring(1);
            $("#cb_" + gridId).hide();//隐藏全选按钮
        },
        loadComplete: function () {
            // if (djsjbdcdy_bdcdyh != "" && djsjbdcdy_id != "") {
            //     $(grid_selector).jqGrid('setSelection', djsjbdcdy_id, true);
            // }
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                var replacement =
                    {
                        'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
                    };
                $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                    var icon = $(this);
                    var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                    if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                })
            }, 0);
        },
        onSelectRow: function (rowid, status) {
            djsjbdcdy_bdcdyh = "";
            djsjbdcdy_id = "";
            djsjbdcdy_qlr = "";
            djsjbdcdy_zl = "";
            $(grid_selector).jqGrid('resetSelection');
            var ids = $(grid_selector).getDataIDs();
            for (var i = 0; i < ids.length; i++) {
                $('#' + ids[i]).find("td").removeClass("SelectBG");
            }
            if (status) {
                var rowDatas = $(grid_selector).jqGrid('getRowData', rowid);
                djsjbdcdy_bdcdyh = rowDatas.BDCDYH;
                djsjbdcdy_qlr = rowDatas.QLR;
                djsjbdcdy_zl = rowDatas.TDZL;
                djsjbdcdy_id = rowid;
                var bdclx = rowDatas.BDCLX
                if (bdclx == "土地") {
                    bdclx = "TD";
                } else if (bdclx == "土地、房屋") {
                    bdclx = "TDFW";
                }
                getTdmj(djsjbdcdy_id, bdclx);
                $(grid_selector).jqGrid('setSelection', rowid, false);
                $('#' + rowid).find("td").addClass("SelectBG");
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}


function tdInitTable() {
    var grid_selector = "#td-grid-table";
    var pager_selector = "#td-grid-pager";
    //绑定回车键
    $('#td_bdcqzh,#td_zl,#td_qlr,#td_bdcdyh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#td_searchBtn").click();
        }
    });

    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });

    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'PROID'},
        colNames: ['土地证证号', '坐落', '权利人', '证书类型', "不动产单元号", 'PROID'],
        colModel: [
            {name: 'BDCQZH', index: 'BDCQZH', width: '20%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
            {name: 'ZSLX', index: 'ZSLX', width: '5%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '20%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        beforeRequest: function () {
            $(pager_selector + "_right").hide();
            var gridId = grid_selector.substring(1);
            $("#cb_" + gridId).hide();//隐藏全选按钮
        },
        loadComplete: function () {
            // if (td_bdcqzh != "" && td_id != "") {
            //     $(grid_selector).jqGrid('setSelection', td_id, true);
            // }
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                var replacement =
                    {
                        'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
                    };
                $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                    var icon = $(this);
                    var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                    if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                })
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    asyncGetywsj($(grid_selector), data.PROID);
                })
            }, 0);
        },
        onSelectRow: function (rowid, status) {
            td_bdcqzh = "";
            td_zl = "";
            td_qlr = "";
            td_id = "";
            $(grid_selector).jqGrid('resetSelection');
            var ids = $(grid_selector).getDataIDs();
            for (var i = 0; i < ids.length; i++) {
                $('#' + ids[i]).find("td").removeClass("SelectBG");
            }
            if (status) {
                var rowDatas = $(grid_selector).jqGrid('getRowData', rowid);
                td_bdcqzh = rowDatas.BDCQZH;
                td_zl = rowDatas.ZL;
                td_qlr = rowDatas.QLR;
                td_id = rowid;
                $(grid_selector).jqGrid('setSelection', rowid, false);
                $('#' + rowid).find("td").addClass("SelectBG");
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

function asyncGetywsj(table, proid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcpic/getYwsjxx?proid=" + proid,
        dataType: "json",
        success: function (data) {
            if (data != null) {
                if (data.qlrmc != null && data.qlrmc != '') {
                    table.setCell(proid, "QLR", data.qlrmc);
                }
                if (data.bdcqzh != null && data.bdcqzh != '') {
                    table.setCell(proid, "BDCQZH", data.bdcqzh);
                }
                if (data.zstype != null && data.zstype != '') {
                    if (data.zstype == '不动产权') {
                        table.setCell(proid, "ZSLX", "证书");
                    } else {
                        table.setCell(proid, "ZSLX", "证明");
                    }
                }
            }
        },
        error: function () {
            tipInfo("error");
        }
    });
}

function getTdmj(id, bdclx) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcpic/getTdmj?id=" + id + "&bdclx=" + bdclx,
        dataType: "json",
        success: function (data) {
            if (data != null) {
                djsjbdcdy_fttdmj = data.fttdmj;
                djsjbdcdy_dytdmj = data.dytdmj;
            }
        },
        error: function () {
            tipInfo("error");
        }
    });
}
