var proidArray_yxz = [];
var proidArray_wxz = [];
var colNames = [];
var colModel = [];
var dataUrl = "";
var markSureId = "";
var activeTableId = "";

function yzdfyz(yxmids) {
    var sfyzdf = false;
    $.ajax({
        type: "POST",
        url: bdcdjUrl + "/yzdf/yzdfYz",
        async: false,
        data: {proids: yxmids.join(",")},
        success: function (result) {
            if (result != null && result.wdrProids != null) {
                sfyzdf = true;
                $("#yzdfSelectModal").show();
                proidArray_yxz = yxmids;
                proidArray_wxz = result.wdrProids
                $("#yzdfSum").html("共计(" + proidArray_yxz.length + ")");
                initYzdf();
            }
        }
    });
    return sfyzdf;
}

function initYzdf() {
    var activeTable = $('.active')[1];
    activeTableId = $(activeTable).attr("id");
    if (activeTableId == "" || activeTableId == null) {
        alert("代码缺少请联系管理员！");
        return;
    }
    if (activeTableId == "ywsj") {
        dataUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage";
        markSureId = "ywsjSure";
        colNames = ["proid", "不动产单元号", '不动产权证号', '房屋代码', '坐落', '权利人', '操作'];
        colModel = [
            {name: 'PROID', index: 'PROID', width: '0%', hidden: true},
            {name: 'BDCDYH', index: '', width: '18%', sortable: false},
            {name: 'BDCQZH', index: 'BDCQZH', width: '15%', sortable: false},
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'ZL', index: '', width: '15%', sortable: false},
            {name: 'QLR', index: '', width: '6%', sortable: false, hidden: true},
            {name: 'CZ', index: 'CZ', width: '5%', sortable: false}
        ];
    }
    if (activeTableId == "qlxx") {
        dataUrl = bdcdjUrl + "/selectBdcdyQlShow/getQlxxListByPage";
        markSureId = "qlxxSure";
        colNames = ["产权证号", "proid", "BDCDYID", "不动产单元号", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间', '操作'];
        colModel = [
            {name: 'CQZH', index: 'CQZH', width: '0%', hidden: true},
            {name: 'PROID', index: 'PROID', width: '0%', hidden: true},
            {name: 'BDCDYID', index: 'BDCDYID', width: '10%', hidden: true},
            {name: 'BDCDYH', index: 'BDCDYH', width: '20%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
            {name: 'CFLX', index: 'CFLX', width: '0%', hidden: true},
            {name: 'CFJG', index: 'CFJG', width: '20%', sortable: false},
            {name: 'CFWH', index: 'CFWH', width: '20%', sortable: false},
            {name: 'CFSQR', index: 'CFSQR', width: '0%', hidden: true},
            {name: 'BCFQLR', index: 'BCFQLR', width: '0%', hidden: true},
            {name: 'CFKSQX', index: 'CFKSQX', width: '0%', hidden: true, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'CFJSQX', index: 'CFJSQX', width: '0%', hidden: true, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'CZ', index: 'CZ', width: '5%', sortable: false}
        ];
    }
    yzdfInitTable("yzdfyxz", dataUrl, colNames, colModel, 100);
    yzdfInitTable("yzdfwxz", dataUrl, colNames, colModel, 3);
    tableReload("yzdfyxz-grid-table", dataUrl, {proids: proidArray_yxz.join(",")});
    tableReload("yzdfwxz-grid-table", dataUrl, {proids: proidArray_wxz.join(",")});
}


function yzdfInitTable(tableKey, dataUrl, colNames, colModel, rowNum) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";
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
        colNames: colNames,
        colModel: colModel,
        viewrecords: true,
        rowNum: rowNum,
        pager: pager_selector,
        pagerpos: "left",
        loadTotal: false,
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                if (activeTableId == "ywsj") {
                    getQlrmcAndZl(data.PROID, $(grid_selector), data.PROID);
                }
                if (activeTableId == "qlxx") {
                    getGdCfCqzh(data.PROID, data.BDCDYID, "", $(grid_selector));
                }
                getTjSc(data.PROID, $(grid_selector), tableKey);
            });
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

function getTjSc(rowid, table, tableKey) {
    var czDom = "";
    if (tableKey == "yzdfyxz") {
        czDom += '<span style="cursor:pointer;margin-left: 10px;background-color: #FA8072 !important;" class="label"  onclick="shanchu(\'' + rowid + '\')" >删除</span>'
    }
    if (tableKey == "yzdfwxz") {
        czDom += '<span style="cursor:pointer;margin-left: 10px;background-color: #66CDAA !important;" class="label"  onclick="tianjia(\'' + rowid + '\')" >添加</span>'
    }
    table.setCell(rowid, "CZ", czDom);
}

$("#rbjr").click(function () {
    proidArray_yxz = proidArray_yxz.concat(proidArray_wxz);
    proidArray_wxz = [];
    $("#yzdfSum").html("共计(" + proidArray_yxz.length + ")");
    tableReload("yzdfyxz-grid-table", dataUrl, {proids: proidArray_yxz.join(",")});
    $("#yzdfwxz-grid-table").jqGrid('clearGridData');
});


function tianjia(rowid) {
    proidArray_yxz.push(rowid);
    if (proidArray_wxz.length > 0) {
        var index = $.inArray(rowid, proidArray_wxz);
        proidArray_wxz.remove(index);
    }
    addDate("yzdfwxz-grid-table", rowid);
    tableReload("yzdfyxz-grid-table", dataUrl, {proids: proidArray_yxz.join(",")});
    if (proidArray_wxz.length == 0) {
        $("#yzdfwxz-grid-table").jqGrid('clearGridData');
    } else {
        tableReload("yzdfwxz-grid-table", dataUrl, {proids: proidArray_wxz.join(",")});
    }
}

function shanchu(rowid) {
    proidArray_wxz.push(rowid);
    if (proidArray_yxz.length > 0) {
        var index = $.inArray(rowid, proidArray_yxz);
        proidArray_yxz.remove(index);
    }
    addDate("yzdfyxz-grid-table", rowid);
    tableReload("yzdfwxz-grid-table", dataUrl, {proids: proidArray_wxz.join(",")});
    if (proidArray_yxz.length == 0) {
        $("#yzdfyxz-grid-table").jqGrid('clearGridData');
    } else {
        tableReload("yzdfyxz-grid-table", dataUrl, {proids: proidArray_yxz.join(",")});
    }
}

function addDate(tableKey, rowid) {
    var grid_selector = "#" + tableKey;
    var cm = $(grid_selector).jqGrid('getRowData', rowid);
    var index = $.inArray(rowid, $mulRowid);
    if (index < 0) {
        $mulData.push(cm);
        $mulRowid.push(rowid);
    } else if (index >= 0) {
        $mulData.remove(index);
        $mulRowid.remove(index);
    }
    updateSx();
}

function addAllDate() {
    var grid_selector = "#yzdfyxz-grid-table";
    var cmArray = $(grid_selector).jqGrid('getRowData');
    $.each(cmArray, function (i, cm) {
        var index = $.inArray(cm.PROID, $mulRowid);
        if (index < 0) {
            $mulData.push(cm);
            $mulRowid.push(cm.PROID);
        }
    })
    updateSx();
}

function updateSx() {
    $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
    $("#qlxxMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
}


function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

$("#yzdfCloseBtn").click(function () {
    addAllDate();
    $("#yzdfSelectModal").hide();
    $("#modal-backdrop-mul").hide();
});

$("#yzdfMulSureBtn").click(function () {
    addAllDate();
    $("#yzdfSelectModal").hide();
    $("#modal-backdrop-mul").hide();
    $("#" + markSureId).click();
});