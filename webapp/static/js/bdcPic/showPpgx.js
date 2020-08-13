$(function () {

    InitTable();
    $("#search_btn").click(function () {
        search();
    });


    Array.prototype.remove = function (index) {
        if (index > -1) {
            this.splice(index, 1);
        }
    };

})

function search() {
    var bdcqzh = $("#bdcqzh").val();
    var cqzhjc = $("#cqzhjc").val();
    var zl = $("#zl").val();
    var bdclx = $("#bdclxSelect").val();
    var fwbm = $("#fwbm").val();
    var bdcdyh = $("#bdcdyh").val();
    var qlr = $("#qlr").val();
    if (bdcqzh == "" && cqzhjc == "" && zl == "" && fwbm == "" && bdcdyh == "" && qlr == "") {
        tipInfo("请至少输入一个查询条件！");
    } else {
        var exactQuery = "false";
        var Url = bdcdjUrl + "/bdcpic/getBdcZsListByPage";
        tableReload("grid-table", Url, {
            bdcqzh: bdcqzh,
            fwbm: fwbm,
            qlr: qlr,
            zl: zl,
            bdcdyh: bdcdyh,
            bdclx: bdclx,
            cqzhjc: cqzhjc,
            exactQuery: exactQuery
        });
    }
}

function InitTable() {
    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";
    //绑定回车键
    $('#bdcqzh,#cqzhjc,#zl,#qlr,#fwbm,#bdcdyh').keydown(function (event) {
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

    $(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'PROID'},
        colNames: ['不动产单元号', '不动产权证号', '房屋代码', '坐落', '权利人', '不动产类型', '匹配状态', '操作', 'PROID'],
        colModel: [
            {name: 'BDCDYH', index: '', width: '12%', sortable: false},
            {name: 'BDCQZH', index: '', width: '11%', sortable: false},
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'ZL', index: '', width: '15%', sortable: false},
            {name: 'QLR', index: '', width: '5%', sortable: false},
            {
                name: 'BDCLX', index: '', width: '4%', sortable: false,
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
            {name: 'PPZT', index: 'PPZT', width: '4%', sortable: false},
            {name: 'CZ', index: 'CZ', width: '15%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '6%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        rownumbers: true,
        beforeRequest: function () {
            $(pager_selector + "_right").hide();
            var gridId = grid_selector.substring(1);
            $("#cb_" + gridId).hide();//隐藏全选按钮
        },
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
                $(grid_selector).jqGrid("setGridHeight", "300px");
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            for (var i = 0; i <= $mulRowid.length; i++) {
                $(grid_selector).jqGrid('setSelection', $mulRowid[i]);
                $('#' + $mulRowid[i]).find("td").addClass("SelectBG");
            }
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.PROID, $(grid_selector), data.PROID);
                getPpzt(data.PROID, $(grid_selector), data.PROID, data.BDCDYH);
            });
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }

        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.PROID == e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        $mulRowid.push(e);
                        $('#' + e).find("td").addClass("SelectBG");
                    } else if (!status && index >= 0) {
                        $mulRowid.remove(index);
                        $('#' + e).find("td").removeClass("SelectBG");
                    }
                }
            })
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            //判断是已选择界面还是原界面
            if (cm.PROID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    $mulRowid.push(rowid);
                    $('#' + rowid).find("td").addClass("SelectBG");
                } else if (!status && index >= 0) {
                    $mulRowid.remove(index);
                    $('#' + rowid).find("td").removeClass("SelectBG");
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
        async: false,
        url: bdcdjUrl + "/selectBdcdyQlShow/getdateByProid?proid=" + proid,
        success: function (result) {
            table.setCell(rowid, "FWBM", result.fwbm);
            table.setCell(rowid, "ZL", result.zl);
            table.setCell(rowid, "QLR", result.qlr);
            table.setCell(rowid, "BDCDYH", result.bdcdyh);
            table.setCell(rowid, "BDCLX", result.bdclx);
            table.setCell(rowid, "BDCQZH", result.bdcqzh);
        },
        error: function () {
            tipInfo("error");
        }
    });
}

// 撤回分割
function withdrawFg(fgid) {
    $.blockUI({message: "请稍等……"});
    // 验证是否能够撤回
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcFghb/validateWithdrawFg?fgid=" + fgid,
        success: function (result) {
            if (result != null && result.msg == "success") {
                // 开始撤回
                $.ajax({
                    type: "GET",
                    url: bdcdjUrl + "/bdcFghb/withdrawFg?fgid=" + fgid,
                    success: function (result) {
                        setTimeout($.unblockUI, 10);
                        if (result == "success") {
                            search();
                            tipInfo("撤回成功！");
                        } else {
                            tipInfo("撤回失败！");
                        }
                    },
                    error: function () {
                        setTimeout($.unblockUI, 10);
                        tipInfo("撤回失败！");
                    }
                });
            } else {
                setTimeout($.unblockUI, 10);
                tipInfo("该撤回数据中存子分割，无法撤回！");
            }
        },
        error: function () {
            setTimeout($.unblockUI, 10);
            tipInfo("撤回失败！");
        }
    });
}

//获取匹配状态
function getPpzt(proid, table, rowid, bdcdyh) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcpic/getPpzt?proid=" + proid,
        success: function (result) {
            var ppzt = '';
            var cz = '';
            if (result == "已匹配" || result == "未匹配土地证") {
                ppzt = '<span class="label label-success" value=' + ppzt + '>'+result+'</span>';
                cz = '<a href="javascript:bdcPic(\'' + proid + '\')">重新匹配</a>' + ' ' + '<a href="javascript:cxBdcPic(\'' + proid + '\')">撤销匹配</a> ' + ' ' + '<a href="javascript:showPpjg(\'' + proid + '\')">匹配信息</a>' + ' ' + '<a href="javascript:printPpjg(\'' + proid + '\')">打印匹配单</a>';
            } else {
                ppzt = '<span class="label label-warning" value=' + ppzt + '>未匹配</span>';
                cz = '<a href="javascript:bdcPic(\'' + proid + '\')">匹配</a>';
            }
            table.setCell(rowid, "PPZT", ppzt);
            var selectBdclx = $("#bdclxSelect").val();
            // 分割按钮
            if (selectBdclx == "TDFW") {
                //验证是否是多幢产权 非多幢产权不允许分割
                $.ajax({
                    url: bdcdjUrl + "/bdcFghb/checkExistFdcqDz",
                    type: 'POST',
                    dataType: 'json',
                    async: false,
                    data: {proids: proid},
                    success: function (data) {
                        if (data.msg == "true") {
                            // cz = cz + '  <a href="javascript:bdcSplit(\'' + proid + '\')">分割</a>';
                            cz = cz + '  <a href="javascript:showConfirmDialogWithClose(\'' + "提示信息" + '\',\'' + "确认进行分割" + '\',\'' + "bdcSplit" + '\',\'' + proid + '\',\'' + "" + '\',\'' + "" + '\')">分割</a>';
                        }
                        cz = cz + '  <a href="javascript:showFghbCkxx(\'' + proid + '\')">查看</a>';
                    },
                    error: function (data) {
                        tipInfo("验证项目内多幢产权失败!");
                    }
                });
            } else {
                // cz = cz + '  <a href="javascript:bdcSplit(\'' + proid + '\')">分割</a>';
                cz = cz + '  <a href="javascript:showConfirmDialogWithClose(\'' + "提示信息" + '\',\'' + "确认进行分割" + '\',\'' + "bdcSplit" + '\',\'' + proid + '\',\'' + "" + '\',\'' + "" + '\')">分割</a>';
                cz = cz + '  <a href="javascript:showFghbCkxx(\'' + proid + '\')">查看</a>';
            }
            // 撤回按钮
            $.ajax({
                type: "GET",
                async: false,
                url: bdcdjUrl + "/bdcFghb/getFgidByproid?proid=" + proid,
                success: function (data) {
                    if (data != null && data != "" && data.fgid != "" && data.fgid != null) {
                        // cz = cz + '  <a href="javascript:withdrawFg(\'' + data.fgid + '\')">撤回</a>';
                        cz = cz + '  <a href="javascript:showConfirmDialogWithClose(\'' + "提示信息" + '\',\'' + "确认进行撤回" + '\',\'' + "withdrawFg" + '\',\'' + data.fgid + '\')">撤回</a>';
                    }
                },
                error: function () {
                    tipInfo("error");
                }
            });
            table.setCell(rowid, "CZ", cz);
        },
        error: function () {
            tipInfo("error");
        }
    });
}

function showPpjg(proid) {
    window.open(bdcdjUrl + "/bdcpic/showPpjg?proid=" + proid, "匹配结果查看");
}

function printPpjg(proid) {
    var printUrl = bdcdjUrl + "/bdcPrint/printPpd?proid=" + proid + "&hiddeMode=true";
    window.location.href = "eprt://" + printUrl;
}


function bdcSplit(proid) {
    //产权分割
    if (proid == null || proid == '') {
        tipInfo("请至少选择一条数据！");
    } else {
        var selectBdclx = $("#bdclxSelect").val();
        if (selectBdclx == "TDFW") {
            $.blockUI({message: "请稍等……"});
            $.ajax({
                url: bdcdjUrl + "/bdcFghb/bdcSplit",
                type: 'POST',
                dataType: 'json',
                data: {proid: proid},
                success: function (data) {
                    setTimeout($.unblockUI, 10);
                    if (data != null && data != "" && data.msg == "success") {
                        search();
                        showFghbCkxx(data.proid);
                    } else {
                        tipInfo("分割失败!");
                    }
                },
                error: function (data) {
                    setTimeout($.unblockUI, 10);
                    tipInfo("分割失败!");
                }
            });
        } else {
            var url = bdcdjUrl + "/bdcFghb/tdSplit?proid=" + proid;
            window.open(url, "土地分割", "left=1,top=0,height=500, width=1000");
        }
    }
}

// 查看按钮
function showFghbCkxx(proid) {
    $.ajax({
        url: bdcdjUrl + "/BdcGdxxMul/getCqWiidByProid?proid=" + proid,
        type: 'POST',
        dataType: 'json',
        async: 'false',
        data: {proids: proid},
        success: function (data) {
            if (data != null && data != "") {
                window.open(bdcdjUrl + "/bdcFghb/ckxx?wiid=" + data.wiid);
            }
        },
        error: function (data) {
            tipInfo("error!");
        }
    });
}

function bdcCombine() {
    //产权合并
    if ($mulRowid.length < 2) {
        tipInfo("请至少选择两条数据！");
    } else {
        var proids = null;
        for (var i = 0; i < $mulRowid.length; i++) {
            if (proids == null) {
                proids = $mulRowid[i];
            } else {
                proids = proids + "," + $mulRowid[i];
            }
        }
        $.ajax({
            url: bdcdjUrl + "/bdcFghb/checkExistFdcqDz",
            type: 'POST',
            dataType: 'json',
            data: {proids: proids},
            success: function (data) {
                if (data.msg == "false") {
                    openFghbPage(proids);
                } else {
                    tipInfo("项目内多幢产权无法进行合并！");
                }
            },
            error: function (data) {
                tipInfo("验证失败!");
            }
        });
    }

}

function openFghbPage(proids) {
    //打开页面前清空所选数据
    $mulRowid = new Array();
    search();
    window.open(bdcdjUrl + "/bdcFghb?proids=" + proids);
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

//显示确认对话框
function showConfirmDialogWithClose(title, msg, okMethod, okParm, cancelMethod, cancelParm) {
    var comfirmDia = bootbox.dialog({
        message: "<h3><b>" + msg + "</b></h3>",
        title: title,
        buttons: {
            OK: {
                label: "是",
                className: "btn-primary",
                callback: function () {
                    if (okMethod != null && okMethod != "")
                        eval(okMethod + "('" + okParm + "')");
                }
            },
            Cancel: {
                label: "否",
                className: "btn-default",
                callback: function () {
                    comfirmDia.hide();
                    if (cancelMethod != null && cancelMethod != "")
                        eval(cancelMethod + "(" + cancelParm + ")");
                }
            }
        }
    });
}

function bdcPicMul() {
    if ($mulRowid.length == 0) {
        tipInfo("请勾选需要匹配的数据！");
    } else {
        var proids = $mulRowid.join(",");
        bdcPic(proids);
    }
}

function cxBdcPic(proid) {
    $.ajax({
        url: bdcdjUrl + "/bdcpic/checkCxPic",
        type: "POST",
        data: {proid: proid},
        success: function (data) {
            if (data != null) {
                if (data.checkModel == "alert") {
                    infoMsg(data.checkMsg, 3000);
                } else {
                    cxPp(proid)
                }
            }
        },
        error: function () {
            infoMsg("验证失败", 3000);
        }
    });

}

function cxPp(proid) {
    $("#loadingModal").modal('show');
    $("#loadingModal").modal('show');
    $.ajax({
        url: bdcdjUrl + "/bdcpic/cxBdcPic",
        type: 'POST',
        dataType: 'json',
        data: {proid: proid},
        success: function (data) {
            $('#loadingModal').modal('hide');
            if (data.msg == "success") {
                infoMsg("撤销匹配成功", 3000);
            } else {
                infoMsg("撤销匹配失败", 3000);
            }
        },
        error: function () {
            infoMsg("撤销匹配失败", 3000);
        }
    });
}