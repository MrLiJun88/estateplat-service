$(function () {
    InitTable();
    $("#search_btn").click(function () {
        var sjh = $("#sjh").val();
        var fwbm = $("#fwbm").val();
        var zl = $("#zl").val();
        var exactQuery = "false";
        if ($("#exactQuery").get(0).checked) {
            exactQuery = "true";
        }
        var Url = bdcdjUrl + "/bdcsd/getSdxxByPage";
        tableReload("grid-table", Url, {
            sjh: sjh,
            fwbm: fwbm,
            zl: zl,
            exactQuery: exactQuery
        });
    });
    $("#tsxxHide").click(function () {
        $("#tsxxPop").hide();
    });
    $("#yesBtn").click(function () {
        gdByProid("true");
        $("#tsxxPop").hide();
    });
    $("#noBtn").click(function () {
        gdByProid("false");
        $("#tsxxPop").hide();
    });
})


function InitTable() {
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";
    //绑定回车键
    $('#sjh,#fwbm,#zl').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#search_btn").click();
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
        colNames: ['受理收件号', '登记类型', '申请类型', '房屋代码', '房屋所有权人', '坐落','归档状态', '是否收档',  '操作', 'PROID'],
        colModel: [
            {name: 'BH', index: 'BH', width: '7%', sortable: false},
            {name: 'DJLX', index: 'DJLX', width: '10%', sortable: false},
            {name: 'SQLX', index: 'SQLX', width: '10%', sortable: false},
            {name: 'FWDM', index: 'FWDM', width: '10%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
            {name: 'GDZT', index: 'GDZT', width: '5%', sortable: false},
            {name: 'SFSD', index: '', width: '5%', sortable: false},
            {name: 'CZ', index: '', width: '5%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '6%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 15,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        rownumbers: true,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "500px");
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.PROID,$(grid_selector),data.PROID);
                getSdzt(data.SFSD,$(grid_selector),data.PROID);
            });
            if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                tipInfo("未搜索到该数据，请核实！");
            }

        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.PROID == e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        //混合查封中数据来源的标志
                        cm["TYPE"] = "bdcqz";
                        $mulRowid.push(e);
                    } else if (!status && index >= 0) {
                        $mulRowid.remove(index);
                    }
                }
            })
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (cm.PROID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulRowid.remove(index);
                }
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

function getQlrmcAndZl(proid, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcsd/getDataByproid?proid=" + proid,
        success: function (result) {
            table.setCell(rowid, "QLR", result.qlr);
        },
        error: function () {
            tipInfo("error");
        }
    });
}

function getSdzt(sfsd, table, rowid) {
    var sfsdDom = "";
    var czDom = "";
    if (sfsd == null || sfsd == "" || sfsd == "0") {
        sfsdDom = '<span class="label label-warning" style="height: 17px; padding: auto">否</span>';
        czDom = '<a href="javascript:sdByProid(\'' + rowid + '\',\'0\')"><span class="label label-important" style="height: 17px; padding: auto">收档</span></a>';
    }
    if (sfsd == "1") {
        sfsdDom = '<span class="label label-success">是</span>';
        czDom = '<a href="javascript:sdByProid(\'' + rowid + '\',\'1 \')"><span class="label label-important" style="height: 17px; padding: auto">修改收档</span></a>';
    }
    table.setCell(rowid, "SFSD", sfsdDom);
    table.setCell(rowid, "CZ", czDom);
}

function sdByProid(proid, sfsd) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcsd/sdByProid?proid=" + proid,
        success: function (result) {
            if (result.msg == "success") {
                var grid_selector = "#grid-table";
                var table=$(grid_selector);
                $.Prompt("归档成功", 500);
                if (sfsd == "0") {
                    getSdzt("1", table, proid)
                } else {
                    getSdzt("0", table, proid)
                }
            } else {
                $.Prompt(result.msg, 500);
            }
        },
        error: function () {
            tipInfo("error");
        }
    });
}
function checkGdxxByProid() {
    if($mulRowid.length>0){
        var proids="";
        for (var i = 0; i < $mulRowid.length; i++) {
            if(proids==""){
                proids = $mulRowid[i];
            }else{
                proids+=","+$mulRowid[i];
            }
        }
        $.ajax({
            type: "POST",
            url: bdcdjUrl + "/bdcsd/checkGdxxByProid",
            data: {proids:proids},
            success: function (result) {
                if (result.msg ==null||result.msg=="") {
                    gdByProid(proids,"true");
                } else {
                    $('h3').html(result.msg);
                    $("#tsxxPop").show();
                }
            },
            error: function () {
                tipInfo("error");
            }
        });
    }else{
        tipInfo("请选择数据！")
    }
}
function gdByProid(cxgd) {
    var proids="";
    for (var i = 0; i < $mulRowid.length; i++) {
        if(proids==""){
            proids = $mulRowid[i];
        }else{
            proids+=","+$mulRowid[i];
        }
    }
    $.ajax({
        type: "POST",
        url: bdcdjUrl + "/bdcsd/gdByProid",
        data: {proids:proids,cxgd:cxgd},
        success: function (result) {
            if (result.msg == "success") {
                var grid_selector = "#grid-table";
                var table=$(grid_selector);
                $.Prompt("归档成功", 500);
                $("#search_btn").click();
            } else {
                $.Prompt(result.msg, 500);
            }
        },
        error: function () {
            tipInfo("error");
        }
    });
}
