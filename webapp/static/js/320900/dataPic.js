//table每页行数
$rownum = 8;
//table 每页高度
$pageHight = '300px';
//全局的不动产类型
$bdclx = 'TDFW';
var djIds = [];
var wiid="";
//定义公用的基础colModel
fwColModel = [
    {
        name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.PROID + '\',\'' + rowObject.SLBH + '\',\'' + rowObject.PPZT + '\',\'' + rowObject.YWLX + '\',\'' + rowObject.DJLX + '\')"/>'
    }
    },
    {name: 'SLBH', index: 'SLBH', width: '15%', sortable: false},
    {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
    {name: 'YWLX', index: 'YWLX', width: '15%', sortable: false},
    {name: 'FWZL', index: 'FWZL', width: '28%', sortable: false},
    {name: 'PPZT', index: 'PPZT', width: '15%', sortable: false},
    {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true}
];

dyhColModel = [
    {
        name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>'
    }
    },
    {name: 'YDJH', index: 'YDJH', width: '15%', sortable: false},
    {name: 'TDZL', index: 'TDZL', width: '25%', sortable: false},
    {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
    {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
    {
        name: 'BDCDYH',
        index: 'BDCDYH',
        width: '20%',
        sortable: false,
        formatter: function (cellvalue, options, rowObject) {
            if (!cellvalue) {
                return "";
            }
            var value = cellvalue.substr(19);
            return value;
        }
    },
    {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
    {name:'dw', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
        return '<div style="margin-left:8px;"> <div title="定位" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="dwbdcdy(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>' +
            '</div>'
    }
    },
    {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
];
dyhLoadComplete = function () {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
        enableTooltips(table);
    }, 0);

    oldDjhForTable("#dyh-grid-table");
    qlrForTable("#dyh-grid-table");

    //清空
    $("#bdcdyh").val("");
    //如果10条设置宽度为auto,如果少于7条就设置固定高度
    if ($(table).jqGrid("getRowData").length == $rownum) {
        $(table).jqGrid("setGridHeight", "auto");
    } else {
        $(table).jqGrid("setGridHeight", $pageHight);
    }

    //去掉遮罩
    setTimeout($.unblockUI, 10);

};

tdLoadComplete = function () {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
        enableTooltips(table);
    }, 0);

    var grid_selector = "#td-grid-table";
    //如果7条设置宽度为auto,如果少于7条就设置固定高度
    if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
        $(grid_selector).jqGrid("setGridHeight", "auto");
    } else {
        $(grid_selector).jqGrid("setGridHeight", $pageHight);
    }
    var jqData = $(grid_selector).jqGrid("getRowData");
    $.each(jqData, function (index, data) {
        asyncGetGdTdxx($(grid_selector), data.QLID, data.PROID);
        getSdStatus(data.QLID, data.TDZH, $(grid_selector), data.PROID, "TD");
    })
}

$(function () {
    //下拉框  含搜索的
    $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据"});
    var width = $(window).width() / 2;
    if (width < 795) {
        width = 795;
    }
    var height = $(window).height() - 20;
    $("#ace-settings-box").css("width", width).css("height", height);
    $(window).resize(function () {
        var width = $(window).width() / 2;
        if (width < 795) {
            width = 795;
        }
        var height = $(window).height() - 20;
        $("#ace-settings-box").css("width", width).css("height", height);
    })
    //默认初始化表格
    dyhTableInit();
    fwTdTableInit();

    if (msgInfo != "null") {
        alert(msgInfo);
    }
    //左边房屋林权草权土地
    $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
        var url;
        //清空查询内容
        $("#dyh_search_qlr").val("");
        $("#dyh_search_qlr").next().show();
        if (this.id == "dyhTab") {
            $("#file").addClass("active");
            var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {
                hhSearch: '',
                bdcdyh: '',
                bdclx: $bdclx
            }, dyhColModel, dyhLoadComplete);
        } else {
            //清空隐藏表单数据
            $("#lqid").val("");
            $("#cqid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#dah").val("");
            $("#xmmc").val("");
            //清空刷新不动产单元表
            $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
            $("#dyh-grid-table").trigger("reloadGrid");
            if (this.id == "fwTab") {
                $bdclx = 'TDFW';
                $("#fw").addClass("active");

                var fwUrl = bdcdjUrl+"/bdcSjgl/getGdXmFwJsonByPage";
                fwTableInit();
                if (isLoadGrid("fw"))
                    tableReload("fw-grid-table", fwUrl, {
                        hhSearch: '',
                        fczh: '',
                        dah: '',
                        filterFwPpzt: filterFwPpzt
                    }, fwColModel, '');
                $("#dyhTab").click();
            } else if (this.id == "lqTab") {
                $bdclx = 'TDSL';
                $("#lq").addClass("active");
                $("#dyhTab").click();
                var lqUrl = bdcdjUrl+"/bdcSjgl/getGdLqJson";
                lqTableInit();
                if (isLoadGrid("lq"))
                    tableReload("lq-grid-table", lqUrl, {hhSearch: '', lqzh: ''}, '', '');
            } else if (this.id == "cqTab") {
                $bdclx = 'TDQT';
                $("#cq").addClass("active");
                $("#dyhTab").click();
                var cqUrl = bdcdjUrl+"/bdcSjgl/getGdCqJson";
                cqTableInit();
                if (isLoadGrid("cq"))
                    tableReload("cq-grid-table", cqUrl, {hhSearch: '', cqzh: ''}, '', '');
            } else if (this.id == "tdTab") {
                $bdclx = 'TD';
                $("#td").addClass("active");
                $("#dyhTab").click();
                var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdTdJson";
                tdTableInit();
                if (isLoadGrid("td"))
                    tableReload("td-grid-table", tdUrl, {hhSearch: ''}, '', '');
            }
            getSqlxByDjlxAndBdclx('', '');
        }
        $("#fwTdTab").hide();

        $.ajax({
            type: "GET",
            url: bdcdjUrl+"/bdcSjgl/getDjlxByBdclx",
            data: {bdclx: $bdclx},
            dataType: "json",
            success: function (result) {
                //清空
                $("#djlxSelect").html("");
                if (result != null && result != '') {
                    $.each(result, function (index, data) {
                        $("#djlxSelect").append('<option value="' + data.DM + '" >' + data.MC + '</option>');
                    })
                }
                djlx = $("#djlxSelect  option:selected").text();
                getSqlxByDjlxAndBdclx(djlx, "");
                $("#djlxSelect").trigger("chosen:updated");

            },
            error: function (data) {
            }
        });
    })


    //右边不动产单元，房屋土地
    $("#dyhTab,#fwTdTab").click(function () {
        if (this.id == "fwTdTab") {
            $bdclx = 'TDFW';
            $("#fwTd").addClass("active");
            var grid_selector = "#fwTd-grid-table";
            var parent_column = $(grid_selector).closest('[class*="col-"]');
            $(grid_selector).jqGrid("setGridWidth", parent_column.width());
        } else {
            $("#file").addClass("active");
            var grid_selector = "#dyh-grid-table";
            var parent_column = $(grid_selector).closest('[class*="col-"]');
            $(grid_selector).jqGrid("setGridWidth", parent_column.width());

        }
    })
    var gdTabOrderArray = new Array();
    gdTabOrderArray = gdTabOrder.split(",");
    if (gdTabOrderArray != null && gdTabOrderArray.length > 0) {
        if (gdTabOrderArray[0] == 'td')
            $("#tdTab").click();
        else if (gdTabOrderArray[0] == 'lq')
            $("#lqTab").click();
        else if (gdTabOrderArray[0] == 'cq')
            $("#cqTab").click();
        else
            $("#fwTab").click();
    }
    /*   文字水印  */
    $(".watermarkText").watermark();

    //为各个输入框提供回车键查询功能
    $('input').focus(
        function () {
            var id = $(this).attr('id');
            if (id == 'fw_search_qlr') {
                $("#fw_search_qlr").keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#fw_search").click();
                    }
                });
            } else if (id == 'td_search_qlr') {
                $("#td_search_qlr").keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#td_search").click();
                    }
                });
            } else if (id == 'cq_search_qlr') {
                $("#cq_search_qlr").keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#cq_search").click();
                    }
                });
            } else if (id == 'lq_search_qlr') {
                $("#lq_search_qlr").keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#lq_search").click();
                    }
                });
            } else if (id == 'dyh_search_qlr') {
                $("#dyh_search_qlr").keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#dyh_search").click();
                    }
                });
            } else if (id == 'fwTd_search_qlr') {
                $("#fwTd_search_qlr").keydown(function (event) {
                    if (event.keyCode == 13) {
                        $("#fwTd_search").click();
                    }
                });
            }
        }
    );

    //查询按钮点击事件
    $("#fw_search").click(function () {
        var hhSearch = $("#fw_search_qlr").val();
        $("#gdproid").val("");
        var fwUrl = bdcdjUrl+"/bdcSjgl/getGdXmFwJsonByPage";
        tableReload("fw-grid-table", fwUrl, {
            hhSearch: hhSearch,
            gdproid: '',
            dah: '',
            filterFwPpzt: filterFwPpzt
        }, fwColModel, '');
    })

    $("#dyh_search").click(function () {
        resetBdcdyhs();
        var hhSearch = $("#dyh_search_qlr").val();
        var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
        tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch, bdcdyh: '', bdclx: $bdclx}, dyhColModel, '');
    })
    $("#lq_search").click(function () {
        var hhSearch = $("#lq_search_qlr").val();
        var lqUrl = bdcdjUrl+"/bdcSjgl/getGdLqJson";
        tableReload("lq-grid-table", lqUrl, {hhSearch: hhSearch, lqzh: ''}, '', '');
    })
    $("#cq_search").click(function () {
        var hhSearch = $("#cq_search_qlr").val();
        var cqUrl = bdcdjUrl+"/bdcSjgl/getGdCqJson";
        tableReload("cq-grid-table", cqUrl, {hhSearch: hhSearch, cqzh: ''}, '', '');
    })
    $("#td_search").click(function () {
        var hhSearch = $("#td_search_qlr").val();
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdTdJson";
        tableReload("td-grid-table", tdUrl, {
            hhSearch: hhSearch,
            tdzh: '',
            filterFwPpzt: filterFwPpzt
        }, '', tdLoadComplete);
    })
    $("#fwTd_search").click(function () {
        resetFwtdids();
        var hhSearch = $("#fwTd_search_qlr").val();
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
        $bdclx = "TDFW";
        tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
    })


    //登记类型变换事件
    $("#djlxSelect").change(function () {
        getSqlxByDjlxAndBdclx('', '');
    })


    //保存事件
    $("#save").click(function () {
        $("#workFlowDefId").val($("#sqlxSelect  option:selected").val());
        $("#sqlx").val($("#sqlxSelect  option:selected").text());
        $("#djlx").val($("#djlxSelect  option:selected").val());
        var sqlx_dm ="";
        if($("#sqlxSelect option:selected")!=null)
            sqlx_dm =$("#sqlxSelect option:selected")[0].attributes[1].nodeValue;
        var fwid = $("#fwid").val();
        var gdid;
        var tdid = $("#tdid").val();
        var lqid = $("#lqid").val();
        var cqid = $("#cqid").val();
        var bdcdyh = $("#bdcdyh").val();
        var ppzt = $("#ppzt").val();
        var dyid = $("#dyid").val();
        var ygid = $("#ygid").val();
        var cfid = $("#cfid").val();
        var yyid = $("#yyid").val();
        var gdproid = $("#gdproid").val();

        var mulGdfw = $("#mulGdfw").val();
        if (dyid == null || dyid == "undefined")
            dyid = "";
        if (ygid == null || ygid == "undefined")
            ygid = "";
        if (cfid == null || cfid == "undefined")
            cfid = "";
        if (yyid == null || yyid == "undefined")
            yyid = "";
        if (gdproid == null || gdproid == "undefined")
            gdproid = "";
        if (mulGdfw == null || mulGdfw == "undefined")
            mulGdfw = "";


        var bdcdyhs = getBdcdyhs();
        var fwtdids = getFwtdids();
        var bdcdyDjIds = getBdcdyDjIds();
        var djlx = $("#djlx").val();
        if (bdcdyhs == null || bdcdyhs == '' && (djlx!=null && djlx!="" && djlx!=800 && djlx!=1000 && sqlx_dm!=407&&sqlx_dm!=410)) {
            tipInfo("请选择不动产单元");
            return false;
        } else if (gdproid == '' && $("#fw").hasClass("active")) {
            tipInfo("请选择房产项目");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active")) {
            tipInfo("请选择土地证");
            return false;
        } else if (lqid == '' && $("#lq").hasClass("active")) {
            tipInfo("请选择林权证");
            return false;
        } else if (cqid == '' && $("#cq").hasClass("active")) {
            tipInfo("请选择草权证");
            return false;
        }
        if ($("#fw").hasClass("active")) {
            $("#tdid").val("");
            $("#cqid").val("");
            $("#lqid").val("");
        } else if ($("#td").hasClass("active")) {
            if (bdcdyhs.length > 1) {
                mulGdfw = "true";
            }
            // $("#gdproid").val("");
            $("#fwid").val("");
            $("#lqid").val("");
            $("#cqid").val("");
        } else if ($("#lq").hasClass("active")) {
            $("#gdproid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        } else if ($("#cq").hasClass("active")) {
            $("#gdproid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        }
        if (mulGdfw != "true") {
            //存在单个房屋进行登记
            if (fwid == '' && $("#fw").hasClass("active")) {
                tipInfo("该项目没有房屋！");
                return false;
            }
            if (bdcdyhs.length > 1) {
                tipInfo("请选择一个不动产单元号！");
                return false;
            }
            bdcdyh = bdcdyhs[0];
            $("#bdcdyh").val(bdcdyh);
            if (bdcdyDjIds != null && bdcdyDjIds.length > 0) {
                $("#djId").val(bdcdyDjIds[0]);
            }else
                $("#djId").val("");

            if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
                if (fwtdids.length > 1) {
                    tipInfo("请选择一个房屋土地证！");
                    return false;
                }
                tdid = fwtdids[0];
                $("#tdid").val(tdid);
            }


            var sqlxdm = $("#sqlxSelect  option:selected").val();
            var djId = $("#djId").val();
            if (djId == null || djId == "undefined")
                djId = "";
            if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                if (bdcdyhs == null || bdcdyhs == '') {
                    var msg = "没有匹配不动产单元，是否创建项目!";
                    showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + djId + "'", "", "");
                } else if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && matchTdzh == "true") {
                    var msg = "没有匹配房屋土地证，是否创建项目！";
                    //zwq 当匹配的时候未勾选房屋土地证，tdid赋空
                    tdid = '';
                    showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + djId + "'", "", "");
                } else {
                    checkXm(gdproid, bdcdyh, '', fwid, tdid, dyid, ygid, cfid, yyid, ppzt, djId);
                }
            } else {
                checkXm(gdproid, bdcdyh, '', fwid, tdid, dyid, ygid, cfid, yyid, ppzt, djId);
            }
        } else {
            //存在多个房屋进行登记
            if (bdcdyDjIds != null && bdcdyDjIds != "")
                $("#djIds").val(bdcdyDjIds.join("$"));
            if (bdcdyhs != null && bdcdyhs != "")
                $("#bdcdyhs").val(bdcdyhs.join("$"));
            var grid_selector;
            var recordId;
            if ($("#fw").hasClass("active")) {
                gdid = $("#gdproid").val();
                recordId = $("#gdproid").val();
                grid_selector = "#fw-grid-table";
            } else if ($("#td").hasClass("active")) {
                gdid = tdid;
                recordId = tdid;
                grid_selector = "#td-grid-table";
            } else if ($("#lq").hasClass("active")) {
                gdid = lqid;
                recordId = lqid;
                grid_selector = "#lq-grid-table";
            } else if ($("#cq").hasClass("active")) {
                gdid = cqid;
                recordId = cqid;
                grid_selector = "#cq-grid-table";
            }
            createMulXm(gdproid, bdcdyh, '', fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector);
        }

    })

    //匹配
    $("#match").click(function () {
        var fwid = $("#fwid").val();
        var gdid;
        var tdid = $("#tdid").val();
        var lqid = $("#lqid").val();
        var cqid = $("#cqid").val();
        var bdcdyh = $("#bdcdyh").val();
        var tdzh = $("#tdzh").val();
        var ppzt = $("#ppzt").val();
        var dyid = $("#dyid").val();
        var mulGdfw = $("#mulGdfw").val();
        var gdproid = $("#gdproid").val();
        var qlid = $("#qlid").val();
        if (dyid == null || dyid == "undefined")
            dyid = "";
        var ygid = $("#ygid").val();
        if (ygid == null || ygid == "undefined")
            ygid = "";
        var cfid = $("#cfid").val();
        if (cfid == null || cfid == "undefined")
            cfid = "";
        var yyid = $("#yyid").val();
        if (yyid == null || yyid == "undefined")
            yyid = "";
        if (gdproid == null || gdproid == "undefined")
            gdproid = "";
        if (mulGdfw == null || mulGdfw == "undefined")
            mulGdfw = "";
        var bdcdyhs = getBdcdyhs();
        var fwtdids = getFwtdids();
        var bdcdyDjIds = getBdcdyDjIds();
        if (bdcdyhs == null || bdcdyhs == '') {
            tipInfo("请选择不动产单元");
            return false;
        } else if (gdproid == '' && $("#fw").hasClass("active")) {
            tipInfo("请选择房产项目");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active")) {
            tipInfo("请选择土地证");
            return false;
        } else if (lqid == '' && $("#lq").hasClass("active")) {
            tipInfo("请选择林权证");
            return false;
        } else if (cqid == '' && $("#cq").hasClass("active")) {
            tipInfo("请选择草原证");
            return false;
        }
        if ($("#fw").hasClass("active")) {
            $("#tdid").val("");
            $("#cqid").val("");
            $("#lqid").val("");
        } else if ($("#td").hasClass("active")) {
            //$("#gdproid").val("");
            $("#fwid").val("");
            $("#lqid").val("");
            $("#cqid").val("");
        } else if ($("#lq").hasClass("active")) {
            $("#gdproid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        } else if ($("#cq").hasClass("active")) {
            $("#gdproid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        }
        if (mulGdfw != "true") {
            if (fwid == '' && $("#fw").hasClass("active")) {
                tipInfo("该项目没有房屋！");
                return false;
            }
            if (bdcdyhs.length > 1) {
                tipInfo("请选择一个不动产单元号！");
                return false;
            }
            bdcdyh = bdcdyhs[0];
            $("#bdcdyh").val(bdcdyh);
            if (bdcdyDjIds != null && bdcdyDjIds.length > 0) {
                $("#djId").val(bdcdyDjIds[0]);
            }else
                $("#djId").val("");
            if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
                if (fwtdids.length > 1) {
                    tipInfo("请选择一个房屋土地证！");
                    return false;
                }
                tdid = fwtdids[0];
                $("#tdid").val(tdid);
            }

            var djId = $("#djId").val();
            if (djId == null || djId == "undefined")
                djId = "";
            var sqlxdm = $("#sqlxSelect  option:selected").val();
            if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                //当不等于商品房转移登记的需要匹配土地证
                if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && matchTdzh == "true") {
                    var msg = "没有匹配房屋土地证，是否匹配！";
                    showConfirmDialog("提示信息", msg, "dyhPic", "'" + gdproid + "','" + bdcdyh + "','" + tdzh + "','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + djId + "'", "", "");
                } else {
                    dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, djId, qlid);
                }
            } else {
                dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, djId, qlid);
            }
        } else {
            tipInfo("该项目存在多个房屋，请到多个房屋匹配页面进行匹配！");
        }

    })

    $("#ppxm").click(function () {


    })


    //取消匹配
    $("#dismatch").click(function () {
        var qlid = $("#qlid").val();
        var fwid = $("#fwid").val();
        var tdid = $("#tdid").val();
        var lqid = $("#lqid").val();
        var cqid = $("#cqid").val();
        var bdcdyh = $("#bdcdyh").val();
        var ppzt = $("#ppzt").val();
        var gdproid = $("#gdproid").val();
        if (gdproid == null || gdproid == "undefined")
            gdproid = "";
        var bdcdyhs = getBdcdyhs();
        var fwtdids = getFwtdids();
        //未选择项目
        if (gdproid == '' && $("#fw").hasClass("active")) {
            tipInfo("请选择房产项目");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active")) {
            tipInfo("请选择土地证");
            return false;
        } else if (lqid == '' && $("#lq").hasClass("active")) {
            tipInfo("请选择林权证");
            return false;
        } else if (cqid == '' && $("#cq").hasClass("active")) {
            tipInfo("请选择草权证");
            return false;
        } else if (ppzt == '' || ppzt == '0') {
            //多个房屋匹配页面获得ppzt和数据库中不一致
            if (gdproid != '' && $("#fw").hasClass("active")) {
                $.ajax({
                    url: bdcdjUrl+'/bdcSjgl/getPpzt?gdproid=' + gdproid,
                    datatype: 'GET',
                    success: function (data) {
                        if (data == '0' || data == '') {
                            tipInfo("该项目未匹配");
                            return false;
                        } else if (data == '1' || data == '2') {
                            showConfirmDialog("提示信息", "是否解除匹配", "disPic", "'" + gdproid + "','" + tdid + "','" + lqid + "','" + cqid + "','" + qlid + "'", "", "");
                        } else {
                            tipInfo("已匹配项目未删除！");
                        }
                    }
                });

            } else {
                tipInfo("该项目未匹配");
                return false;
            }
        }
        else if (ppzt == '1' || ppzt == '2') {
            showConfirmDialog("提示信息", "是否解除匹配", "disPic", "'" + gdproid + "','" + tdid + "','" + lqid + "','" + cqid + "','" + qlid + "'", "", "");
        } else {

            tipInfo("已匹配项目未删除！");
        }


    });


//定位
    $("#ace-settings-btn").click(function () {
        if ($("#ace-settings-box").hasClass("open")) {
            $("#iframe").attr("src", $("#iframeSrcUrl").val())
        }
    })

//resize to fit page size
    $(window).on('resize.jqGrid', function () {
        var contentWidth = $(".tab-content").width();
        $("#fw-grid-table,#lq-grid-table,#dyh-grid-table,#cq-grid-table,#td-grid-table,#fwTd-grid-table").jqGrid('setGridWidth', contentWidth);
    });

//新增按钮点击事件
    $("#gdFwAdd,#gdLqAdd,#gdTdAdd,#gdCqAdd").click(function () {
        var bdclxId = this.id;
        $.ajax({
            type: "GET",
            url: bdcdjUrl+"/gdXxLr/getUUid",
            success: function (result) {
                if (result != null && result != "") {
                    if (bdclxId == "gdFwAdd") {
                        addOrUpdate(formUrl+"/gdQlxx/gdQlxx?editFlag=true&bdclx=fw&proid=" + result + "&iscp=true");
                    } else if (bdclxId == "gdLqAdd") {
                        addOrUpdate(formUrl+"/gdQlxx/gdLqxx?editFlag=true");
                    } else if (bdclxId == "gdTdAdd") {
                        addOrUpdate(formUrl+"/gdQlxx/gdQlxx?editFlag=true&bdclx=td&proid=" + result + "&iscp=true" + "&tdid=" + result);
                    } else if (bdclxId == "gdCqAdd") {
                        addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write");
                    }
                }
            }
        });
    })

    $("#bdcxm_search").click(function () {
        var bdcxmUrl = bdcdjUrl+"/bdcSjgl/getBdcXmJson";
        var hhSearch = $("#bdcxm_search_qlr").val();
        bdcxmTableInit();
        tableReload("bdcxm-grid-table", bdcxmUrl, {hhSearch: hhSearch}, '', '');
    })

//批量导入过渡房屋
    $("#gdFwsAdd").click(function () {
        $("#fileInput").show();
        $("#sjlx").val('gd_fw');
        $("#fileDownLoad").attr('href', bdcdjUrl+"/static/Tool/房产证模板.xls");
    });

//批量导入过渡房屋
    $("#fcadd").click(function () {
        var url = etlUrl+"/fw"
        window.open(url);
    });
    $("#ppxm").click(function () {
        var ppzt = $("#ppzt").val();

        if (ppzt == 0 || ppzt == '') {
            tipInfo("请先匹配，再关联项目");
            return false;
        }
        if ($("#fw").hasClass("active") && ppzt == 3) {
            tipInfo("该证书正在发证，不能再次发证！");
            return false;
        } else if ($("#fw").hasClass("active") && ppzt == 4) {
            tipInfo("该证书已经发证，不能再次发证！");
            return false;
        }
        var gdproid = $("#gdproid").val();
        if (gdproid == '' && $("#fw").hasClass("active")) {
            tipInfo("请选择房产项目");
            return false;
        }
        var bdcxmUrl = bdcdjUrl+"/bdcSjgl/getBdcXmJson";
        var hhSearch = '$$$';
        bdcxmTableInit();
        tableReload("bdcxm-grid-table", bdcxmUrl, {hhSearch: hhSearch}, '', '');
        $("#fileInput").show();

    });

    $("#fileHide").click(function () {
        $("#fileInput").hide();
    });

    $("#fileSub").click(function () {
        //遮罩
        $.blockUI({message: "请稍等……"});
    });

    $("#deletexmgl").click(function () {
        var gdproid = $("#gdproid").val();
        var tdid = $("#tdid").val();
        var lqid = $("#lqid").val();
        var cqid = $("#cqid").val();
        var ppzt = $("#ppzt").val();
        if (gdproid == '' && $("#fw").hasClass("active")) {
            tipInfo("请选择房产项目");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active")) {
            tipInfo("请选择土地证");
            return false;
        } else if (lqid == '' && $("#lq").hasClass("active")) {
            tipInfo("请选择林权证");
            return false;
        } else if (cqid == '' && $("#cq").hasClass("active")) {
            tipInfo("请选择草权证");
            return false;
        } else if (ppzt == '' || ppzt == '0') {
            tipInfo("该项目没匹配不动产单元");
            return false;
        }
        var gdproid = $("#gdproid").val();
        if (gdproid == "") {
            tipInfo("请选择一条要删除的数据!");
            return false;
        }
        var msg = "确认删除";
        showConfirmDialog("提示信息", msg, "deletexmgl", "'" + gdproid + "'", "", "");
    })
    $("#deletefwxm").click(function () {
        var gdproid = $("#gdproid").val();
        if (gdproid == "") {
            tipInfo("请选择一条要删除的数据!");
            return false;
        }
        var msg = "确认删除";
        showConfirmDialog("提示信息", msg, "deletefwxm", "'" + gdproid + "'", "", "");

    })

//修改按钮点击事件
    $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdFwTdUpdate,#gdCqUpdate").click(function () {
        if (this.id == "gdFwUpdate") {
            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }

            addOrUpdate(formUrl+"/gdQlxx/gdQlxx?editFlag=true&bdclx=fw&proid=" + gdproid + "&iscp=false");
        } else if (this.id == "gdLqUpdate") {
            var lqid = $("#lqid").val();
            if (lqid == null || lqid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate(formUrl+"/gdQlxx/gdLqxx?editFlag=true&lqid=" + lqid);
        } else if (this.id == "gdTdUpdate") {
            var tdid = "";
            if ($("#fwTd").hasClass("active")) {
                var tdids = getFwtdids();
                if (tdids == null && tdids == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                else if (tdids.length > 1) {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                } else if (tdids.length == 0) {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                else
                    tdid = getFwtdids();
            } else {
                tdid = $("#tdid").val();
                if (tdid == null || tdid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
            }

            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }

            addOrUpdate(formUrl+"/gdQlxx/gdQlxx?editFlag=true&bdclx=td&proid=" + gdproid + "&iscp=true&tdid=" + tdid);
        }  else if (this.id == "gdFwTdUpdate") {
            var tdid = "";
            if ($("#fwTd").hasClass("active")) {
                var tdids = getFwtdids();
                if (tdids == null && tdids == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                else if (tdids.length > 1) {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                } else if (tdids.length == 0) {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                else
                    tdid = getFwtdids();
            } else {
                tdid = $("#tdid").val();
                if (tdid == null || tdid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
            }

            var gdproid = getFwtdProids();
            if (gdproid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }

            addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=true&bdclx=td&proid=" + gdproid + "&iscp=true&tdid=" + tdid);
        }else if (this.id == "gdCqUpdate") {
            var cqid = $("#cqid").val();
            if (cqid == null || cqid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=" + cqid);
        }
    })

//查看按钮点击事件
    $("#gdFwView,#gdLqView,#gdTdView,#gdFwTdView,#gdCqView").click(function () {
        if (this.id == "gdFwView") {
            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要查看的数据!");
                return false;
            }

            addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=&bdclx=fw&proid=" + gdproid + "&iscp=false");
        } else if (this.id == "gdLqView") {
            var lqid = $("#lqid").val();
            if (lqid == null || lqid == "") {
                tipInfo("请选择一条要查看的数据!");
                return false;
            }
            addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=" + lqid);
        } else if (this.id == "gdTdView") {
            var tdid = "";
            if ($("#fwTd").hasClass("active")) {
                var tdids = getFwtdids();
                if (tdids == null && tdids == "") {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                }
                else if (tdids.length > 1) {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                } else if (tdids.length == 0) {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                }
                else
                    tdid = getFwtdids();
            } else {
                tdid = $("#tdid").val();
                if (tdid == null || tdid == "") {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                }
            }

            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要查看的数据!");
                return false;
            }

            addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=&bdclx=td&proid=" + gdproid + "&iscp=true&tdid=" + tdid);
        }  else if (this.id == "gdFwTdView") {
            var tdid = "";
            if ($("#fwTd").hasClass("active")) {
                var tdids = getFwtdids();
                if (tdids == null && tdids == "") {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                }
                else if (tdids.length > 1) {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                } else if (tdids.length == 0) {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                }
                else
                    tdid = getFwtdids();
            } else {
                tdid = $("#tdid").val();
                if (tdid == null || tdid == "") {
                    tipInfo("请选择一条要查看的数据!");
                    return false;
                }
            }

            var gdproid = getFwtdProids();
            if (gdproid == "") {
                tipInfo("请选择一条要查看的数据!");
                return false;
            }

            addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=&bdclx=td&proid=" + gdproid + "&iscp=true&tdid=" + tdid);
        }else if (this.id == "gdCqView") {
            var cqid = $("#cqid").val();
            if (cqid == null || cqid == "") {
                tipInfo("请选择一条要查看的数据!");
                return false;
            }
            addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=" + cqid);
        }
    })
})
var deletexmgl = function (gdproid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/deletexmgl?gdproid=" + gdproid,
        dataType: "json",
        success: function (jsonData) {
            alert(jsonData.result);
            $("#fw_search").click();
        },
        error: function (data) {
        }
    })
}

var deletefwxm = function (gdproid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/deleteGdXm?proid=" + gdproid,
        dataType: "json",
        success: function (result) {
            alert(result);
            $("#gdproid").val("");
            var fwUrl = bdcdjUrl+"/bdcSjgl/getGdXmFwJsonByPage";
            tableReload("fw-grid-table", fwUrl, {
                gdproid: '',
                dah: '',
                filterFwPpzt: filterFwPpzt
            }, fwColModel, '');
        },
        error: function (data) {
        }
    })
}

function getSqlxByDjlxAndBdclx(djlx, wfid) {
    if (djlx == null || djlx == "")
        djlx = $("#djlxSelect  option:selected").text();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getSqlxByDjlx",
        data: {djlx: djlx, bdclx: $bdclx},
        dataType: "json",
        success: function (result) {
            //清空
            $("#sqlxSelect").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    if (data.DM == "24D768DE8B8F4CD59F70E621C2CAB2E2" || data.DM == "19D3CDE088174478943341B32EF3238C" || data.DM == wfid)
                        $("#sqlxSelect").append('<option value="' + data.DM + '" selected="selected" name="'+data.SQLXDM+'>' + data.MC + '</option>');
                    else
                        $("#sqlxSelect").append('<option value="' + data.DM + '" name="'+data.SQLXDM+'">' + data.MC + '</option>');
                })
            }
            $("#sqlxSelect").trigger("chosen:updated");
            var sqlx = $("#sqlxSelect").val();
            if (wfids != null && wfids != "" && wfids.indexOf(sqlx) > -1)
                hideFwtdGrid();
        },
        error: function (data) {
        }
    });
}
function hideModal(proid) {
    if (proid && proid != undefined && proid != "undefined") {
        openWin(bdcdjUrl+'/bdcSjgl/formTab?proid=' + proid);
    }
}
function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    var openner = window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    if (window.opener && !window.opener.closed) {
        alert(openner);
    }
}


//选择草原证
function cqSel(cqzh, cqid, qlrmc, ppzt) {
    //赋值
    if (cqid && cqid != 'undefined') {
        $("#cqid").val(cqid);
    } else {
        $("#cqid").val("");
    }
    if (qlrmc && qlrmc != 'undefined') {
        $("#xmmc").val(qlrmc);
    } else
        qlrmc = "";
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else
        $("#ppzt").val("");
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/queryBdcdyhByGdid?gdid=" + cqid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {

                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                //清空查询内容
                $("#dyh_search_qlr").val("");
                $("#dyh_search_qlr").next().show();
                $.each(result, function (index, data) {
                    if (index == 0) {
                        if (data.hasOwnProperty("bdcdyh")) {
                            if (data.bdcdyh && data.bdcdyh != 'undefined') {
                                selDyhByFw(data.bdcdyh, "");
                                //定位
//                                bdzDyMap(data.bdcdyh);
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdcdyh: '',
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }

                        } else {
                            if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                selDyhByFw(data.BDCDYH, "");
                                //定位
//                                bdzDyMap(data.BDCDYH);
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdcdyh: '',
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }

                        }
                    }
                })
            }
        },
        error: function (data) {
        }
    });
}
//选择林权证
function lqSel(lqzh, lqid, qlrmc, ppzt) {

    //赋值
    if (lqid && lqid != 'undefined') {
        $("#lqid").val(lqid);
    } else {
        $("#lqid").val("");
    }
    if (qlrmc && qlrmc != 'undefined') {
        $("#xmmc").val(qlrmc);
    } else
        qlrmc = "";
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else
        $("#ppzt").val("");
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/queryBdcdyhByGdid?gdid=" + lqid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                //清空查询内容
                $("#dyh_search_qlr").val("");
                $("#dyh_search_qlr").next().show();
                $.each(result, function (index, data) {
                    if (index == 0) {
                        if (data.hasOwnProperty("bdcdyh")) {
                            if (data.bdcdyh && data.bdcdyh != 'undefined') {
                                selDyhByFw(data.bdcdyh, "");
                                //定位
//                                bdzDyMap(data.bdcdyh);
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdcdyh: '',
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }
                        } else {
                            if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                selDyhByFw(data.BDCDYH, "");
                                //定位
//                                bdzDyMap(data.BDCDYH);
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdcdyh: '',
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }

                        }
                    }
                })
            }
        },
        error: function (data) {
        }
    });

}
//选择土地证号

function tdSel(proid, qlid) {
    //zwq 因为djh和qlrmc都是在jqgrid加载完后再加载，所有传值无用
    var rowData = $("#td-grid-table").jqGrid("getRowData", qlid);
    var qlrmc = rowData.ZL;
    var ppzt = $(rowData.PPZT).attr("value");
    var djh = rowData.DJH;
    if (qlid && qlid != 'undefined') {
        $("#qlid").val(qlid);
    } else {
        $("#qlid").val("");
    }
    if (proid && proid != 'undefined') {
        $("#gdproid").val(proid);
    } else {
        $("#gdproid").val("");
    }
    if (qlrmc && qlrmc != 'undefined') {
        $("#xmmc").val(qlrmc);
    } else
        $("#xmmc").val("");
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else
        $("#ppzt").val("");

    //zwq 这个dyid没用到
    $("#dyid").val("");
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByTdidDjh?qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            /*if (result == '' || result == null) {
             $("#dyh_search_qlr").next().hide();
             $("#dyh_search_qlr").val(qlrmc);
             //无匹配数据 不刷新
             $("#file").addClass("active");
             var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
             tableReload("dyh-grid-table", dyhUrl, {
             hhSearch: qlrmc,
             bdcdyh: '',
             bdclx: $bdclx
             }, dyhColModel, dyhLoadComplete);
             } else {
             //清空查询内容
             $("#dyh_search_qlr").val("");
             $("#dyh_search_qlr").next().show();
             $.each(result, function (index, data) {
             if (index == 0) {
             if (data.hasOwnProperty("bdcdyh")) {
             if (data.bdcdyh && data.bdcdyh != 'undefined') {
             selDyhByFw(data.bdcdyh, "");
             //定位
             //                                bdzDyMap(data.bdcdyh);
             } else {
             $("#dyh_search_qlr").next().hide();
             $("#dyh_search_qlr").val(qlrmc);
             //无匹配数据 不刷新
             $("#file").addClass("active");
             var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
             tableReload("dyh-grid-table", dyhUrl, {
             hhSearch: qlrmc,
             bdcdyh: '',
             bdclx: $bdclx
             }, dyhColModel, dyhLoadComplete);
             }
             } else {
             if (data.BDCDYH && data.BDCDYH != 'undefined') {
             selDyhByFw(data.BDCDYH, "");
             //定位
             //                                bdzDyMap(data.BDCDYH);
             } else {
             $("#dyh_search_qlr").next().hide();
             $("#dyh_search_qlr").val(qlrmc);
             //无匹配数据 不刷新
             $("#file").addClass("active");
             var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
             tableReload("dyh-grid-table", dyhUrl, {
             hhSearch: qlrmc,
             bdcdyh: '',
             bdclx: $bdclx
             }, dyhColModel, dyhLoadComplete);
             }
             }
             }
             })
             }*/
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                //清空查询内容
                $("#dyh_search_qlr").val("");
                $("#dyh_search_qlr").next().show();
                $.each(result, function (index, data) {
                    if (index == 0) {
                        if (data.tdid && data.tdid != 'undefined')
                            $("#tdid").val(data.tdid);
                        if (data.gdid && data.gdid != 'undefined')
                            $("#tdid").val(data.gdid);
                        if (data.hasOwnProperty("bdcdyh")) {
                            if (data.bdcdyh && data.bdcdyh != 'undefined') {
                                selDyhByFw(data.bdcdyh, "");
                                //定位
//                                bdzDyMap(data.bdcdyh);
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdcdyh: '',
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }
                        } else {

                            if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                selDyhByFw(data.BDCDYH, "");
                                //定位
//                                bdzDyMap(data.BDCDYH);
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdcdyh: '',
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }
                        }
                    }
                })
            }
        },
        error: function (data) {
        }
    });

}

//锁定按钮点击事件
$("#lockFw,#gdTdLocked").click(function () {
    var qlid = $("#qlid").val();
    if (qlid == "") {
        tipInfo("请选择一条要锁定的数据!");
        return false;
    }

    //先验证该条数据是否已被锁定
    var cqzh = "";
    //组织页面上所选记录的相关字段值
    if ($bdclx == "TDFW") {
        cqzh = $("#fw-grid-table").getCell($("#qlid").val(), "FCZH");
    } else if ($bdclx == "TD") {
        cqzh = $("#td-grid-table").getCell($("#qlid").val(), "TDZH");
    } else {
        cqzh = "";
    }
    //验证界面上选择的数据，其产权证号是否为空，如是，则返回
    if (cqzh == "" || null == cqzh) {
        alert("要锁定的过渡数据产权证号不能为空！");
        return false;
    }

    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/checkGdSjSd",
        dataType: "json",
        data: {cqzh: cqzh},
        success: function (jsonData) {
            if(jsonData == false){
                alert("该过渡数据已被锁定，不能重复锁定！");
                return false;
            } else {
                $("#gdXzyyPop").show();
                $(window).trigger('resize.chosen');
                $(".modal-dialog").css({"_margin-left":"25%"});
            }
        },
        error: function (data) {
            alert("验证过渡数据是否被锁定，操作失败！");
        }
    });

});

//解锁按钮点击事件
$("#unlockFw,#gdTdUnLocked").click(function () {
    var qlid = $("#qlid").val();
    if (qlid == "") {
        tipInfo("请选择一条要解锁的数据!");
        return false;
    }

    var cqzh = "";
    //组织页面上所选记录的相关字段值
    if ($bdclx == "TDFW") {
        cqzh = $("#fw-grid-table").getCell($("#qlid").val(), "FCZH");
    } else if ($bdclx == "TD") {
        cqzh = $("#td-grid-table").getCell($("#qlid").val(), "TDZH");
    } else {
    }

    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/UnlockGdSj",
        dataType: "json",
        data: {cqzh: cqzh},
        success: function (jsonData) {
            alert(jsonData.msg);
            if (jsonData.flag = "true") {
                if ($bdclx == "TD") {
                    table.setCell(qlid, "SDF", " ");
                }
            }
        },
        error: function (data) {
            alert("解锁失败！");
        }
    });

    $(window).trigger('resize.chosen');
    $(".modal-dialog").css({"_margin-left":"25%"});
});

//限制原因确定按钮点击事件
$("#gdLockSureBtn").click(function () {
    var xzyy_str = $("#xzyy").val();
    if(xzyy_str==null || xzyy_str==""){
        tipInfo("限制原因不能为空，请输入！");
        return false;
    }
    var data_gd = null;
    var table = null;
    var qlid = $("#qlid").val();
    //组织页面上所选记录的相关字段值
    if ($bdclx == "TDFW") {
        var fczh = $("#fw-grid-table").getCell(qlid, "FCZH");
        var proid = $("#fw-grid-table").getCell(qlid, "PROID");
        data_gd = {qlid: qlid, cqzh: fczh, bdclx:$bdclx, proid: proid,xzyy: xzyy_str};
    }
    if ($bdclx == "TD") {
        var tdzh = $("#td-grid-table").getCell(qlid, "TDZH");
        var proid_td = $("#td-grid-table").getCell(qlid, "PROID");
        data_gd = {qlid: qlid,cqzh:tdzh,bdclx:$bdclx, proid: proid_td, xzyy:xzyy_str};
        table = $("#td-grid-table");
    }

    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/lockGdSj",
        dataType: "json",
        data: data_gd,
        success: function (jsonData) {
            alert(jsonData.msg);
            var cellVal = "";
            if (jsonData.flag == "true") {
                if ($bdclx == "TD") {
                    cellVal =
                        '<div title="'+xzyy_str+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                        '<span><img src="../static/img/locked.png" width="20px" height="20px" /></span>' +
                        '</div>';
                    table.setCell(qlid, "SDF", cellVal);
                }
            }
        },
        error: function (jsonData) {
            alert("锁定失败！");
        }
    });

    $("#gdXzyyPop").hide();
    $("#gdXzyyForm")[0].reset();
    $(".chosen-select").trigger('chosen:updated');
});

//限制原因取消按钮点击事件
$("#gdLockCancelBtn").click(function () {
    $("#gdXzyyPop").hide();
    $("#gdXzyyForm")[0].reset();
    $(".chosen-select").trigger('chosen:updated');
});

//限制原因对话框关闭事件
$("#proHide").click(function () {
    $("#gdXzyyPop").hide();
    $("#gdXzyyForm")[0].reset();
    $(".chosen-select").trigger('chosen:updated');
});

function fwSel(bdcid, slbh, ppzt, ywlx, djlx) {
    //遮罩
    $.blockUI({message: "请稍等……"});
    //zwq 因fwzl和qlrmc是后赋值上去的，所以需要通过获取jqgrid数据获取
    var rowData = $("#fw-grid-table").jqGrid("getRowData", bdcid);
    var fwzl = rowData.FWZL;
    var qlrmc = rowData.RF1DWMC;

    //赋值
    if (fwzl && fwzl != 'undefined')
        $("#xmmc").val(fwzl);
    else
        fwzl = "";
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else {
        $("#ppzt").val("");
        ppzt = "";
    }

    if (slbh && slbh != 'undefined')
        $("#gdslbh").val(slbh);

    if (bdcid && bdcid != 'undefined')
        $("#gdproid").val(bdcid);
    else
        $("#gdproid").val("");
    if (djlx && djlx == 'undefined')
        djlx = "";

    if (qlrmc && qlrmc != 'undefined')
        $("#qlr").val(qlrmc);
    else
        $("#qlr").val("");


    bdcid = $("#gdproid").val();
    $("#dyhTab").click();
    $("#tdid").val('');
    resetBdcdyhs();
    resetFwtdids();
    var isck = "";
    if (fwzl != null && fwzl != "" && (fwzl.indexOf("车库") > -1 || fwzl.indexOf("储藏室") > -1 || fwzl.indexOf("车位") > -1 || fwzl.indexOf("阁楼") > -1 ) && ywlx != null && ywlx != "" && ywlx == "车库余房登记") {
        isck = "true";
    }
//    判断是否是多个房屋
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/checkGdfwNum?gdproid=" + bdcid + "&bdclx=" + $bdclx + "&isck=" + isck,
        dataType: "json",
        success: function (result) {
            $("#mulGdfw").val(result.mulGdfw);
            $("#fwid").val(result.fwid);
            $.ajax({
                type: "GET",
                url: bdcdjUrl+"/bdcSjgl/getGdFcDjlxToSqlxWfid?djlx=" + djlx,
                dataType: "json",
                success: function (sqlxSesult) {
                    //获取过渡房产数据对应的不动产登记类型
                    if (sqlxSesult != null && sqlxSesult != "" && sqlxSesult.busiName != "") {
                        $("#djlxSelect option").each(function () {
                            if ($(this).text() == sqlxSesult.busiName) {
                                $(this).attr('selected', 'selected');
                            }
                            $("#djlxSelect").trigger("chosen:updated");
                        });
                        getSqlxByDjlxAndBdclx(sqlxSesult.busiName, sqlxSesult.wfid);
                    } else {
                        var busiName = "首次登记";
                        $("#djlxSelect option").each(function () {
                            if ($(this).text() == busiName) {
                                $(this).attr('selected', 'selected');
                            }
                            $("#djlxSelect").trigger("chosen:updated");
                        });

                        getSqlxByDjlxAndBdclx(busiName, "");
                    }
                    if (result != null && result != "" && result.mulGdfw != "false") {
                        var msg = "";
                        var readOnly = "false";
                        if (ppzt == "4") {
                            msg = "该项目存在多个房屋并且已经进行过匹配，是否继续查看匹配情况！";
                            readOnly = "true";
                        } else if (ppzt == "3") {
                            msg = "该项目存在多个房屋并且已经正在发证，是否继续查看匹配情况！";
                            readOnly = "true";
                        }
                        else if (ppzt == "1" || ppzt == "2")
                            msg = "该项目存在多个房屋并且已经进行过匹配，是否继续匹配！";
                        else
                            msg = "该项目存在多个房屋，是否继续匹配！";
                        //                showConfirmDialog("提示信息",msg,"showMulGdFwPic","'"+bdcid+"','" +qlrmc+"','"+ppzt+"','"+djlx+"'");
//                        disableDyhTabDiv("file");
                        clearTabDiv();
                        showConfirmDialog("提示信息", msg, "showMulGdFwPic", "'" + bdcid + "','" + readOnly + "'", "updateGdFwAndDyhSel", "'" + djlx + "'");
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                    } else {
                        visableDyhTabDiv("file");
                        $("#fwid").val(result.fwid);
                        picDyh(bdcid, result.fwid, fwzl, ppzt, djlx, isck);
                        setTimeout($.unblockUI, 10);
                    }
                },
                error: function (data) {
                }
            });
        }
    });

}

//匹配不动产单元
var picDyh = function (bdcid, fwid, fwzl, ppzt, djlx, isck) {
    //通过fczh获取hs_index
    var bdcdyhs = getBdcdyhs();
    var fwtdids = getFwtdids();
    var qlrmc = $("#xmmc").val();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/queryBdcdyhByGdProid?gdproid=" + bdcid + "&bdclx=" + $bdclx + "&isck=" + isck,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(fwzl);
                //无匹配数据 不刷新
//                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: fwzl,
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                //清空查询内容
                $("#dyh_search_qlr").val("");
                $("#dyh_search_qlr").next().show();
                if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length > 1) {
                    selDyhByFw("", bdcdyhs);
                    $("#bdcdyhs").val(bdcdyhs);
                } else if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length == 1) {
                    selDyhByFw(bdcdyhs.join(""), "");
                    $("#bdcdyhs").val("");
                    $("#bdcdyh").val(bdcdyhs.join(""));
                } else if (result[0].bdcdyh != null && result[0].bdcdyh != "" && result[0].bdcdyh != "undefined") {
                    selDyhByFw(result[0].bdcdyh, "");
                    $("#bdcdyhs").val("");
                    $("#bdcdyh").val(result[0].bdcdyh);
                } else if (result[0].BDCDYH != null && result[0].BDCDYH != "" && result[0].BDCDYH != "undefined") {
                    selDyhByFw(result[0].BDCDYH, "");
                    $("#bdcdyhs").val("");
                    $("#bdcdyh").val(result[0].BDCDYH);
                } else {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(fwzl);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: fwzl,
                        bdcdyh: '',
                        bdclx: $bdclx
                    }, dyhColModel, dyhLoadComplete);
                }
            }
        },
        error: function (data) {
        }
    });

    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getGdFcDjlxToSqlxWfid?djlx=" + djlx,
        dataType: "json",
        success: function (result) {
            //获取过渡房产数据对应的不动产登记类型
            if (result != null && result != "" && result.busiName != "") {
                $("#djlxSelect option").each(function () {
                    if ($(this).text() == result.busiName) {
                        $(this).attr('selected', 'selected');
                    }
                    $("#djlxSelect").trigger("chosen:updated");
                });
                getSqlxByDjlxAndBdclx(result.busiName, result.wfid);
            } else {
                var busiName = "首次登记";
                $("#djlxSelect option").each(function () {
                    if ($(this).text() == busiName) {
                        $(this).attr('selected', 'selected');
                    }
                    $("#djlxSelect").trigger("chosen:updated");
                });

                getSqlxByDjlxAndBdclx(busiName, "");
            }
            if (matchTdzh == "true" && result != null && (result.wfid == null || result.wfid == "" || wfids != null && wfids != "" && wfids.indexOf(result.wfid) < 0)) {
                $.ajax({
                    type: "GET",
                    url: bdcdjUrl+"/bdcSjgl/queryTdByGdproid?gdproid=" + bdcid,
                    dataType: "json",
                    success: function (result) {
                        if (result == '' || result == null) {
                            $("#fwTd_search_qlr").next().hide();
                            $("#fwTd_search_qlr").val(qlrmc);
                            $("#tdzh").val('');

                            $("#tdid").val('');
                            var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                            tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                        } else {
                            //清空查询内容
                            $("#fwTd_search_qlr").val("");
                            $("#fwTd_search_qlr").next().show();
                            if (fwtdids != null && fwtdids != "" && fwtdids.length > 1) {
                                selFwTdByFw("", fwtdids,"");
                                $("#tdids").val(fwtdids);
                            } else if (fwtdids != null && fwtdids != "" && fwtdids.length == 1) {
                                selFwTdByFw(fwtdids.join(""), "","");
                                $("#tdids").val("");
                                $("#tdid").val(fwtdids.join(""));
                            } else if (result[0].tdid != null && result[0].tdid != "" && result[0].tdid != "undefined") {
                                selFwTdByFw(result[0].tdid, "","");
                                $("#tdids").val("");
                                $("#tdid").val(result[0].tdid);
                            } else if (result[0].TDID != null && result[0].TDID != "" && result[0].TDID != "undefined") {
                                selFwTdByFw(result[0].TDID, "","");
                                $("#tdids").val("");
                                $("#tdid").val(result[0].TDID);
                            } else if (result[0].tdqlid != null && result[0].tdqlid != "" && result[0].tdid != "undefined") {
                                selFwTdByFw("", "",result[0].tdqlid);
                                $("#tdids").val("");
                                $("#tdid").val(result[0].tdqlid);
                            } else {
                                $("#fwTd_search_qlr").next().hide();
                                $("#fwTd_search_qlr").val(qlrmc);
                                var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                                tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                            }
                        }

                    },
                    error: function (data) {
                    }
                });
                $("#fwTdTab").show();

            }
        },
        error: function (data) {
        }
    });
}

var checkXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, djId) {
    if ($("#fw").hasClass("active") && ppzt == 3) {
        tipInfo("该证书正在发证，不能再次发证！");
        return false;
    } else if ($("#fw").hasClass("active") && ppzt == 4) {
        tipInfo("该证书已经发证，不能再次发证！");
        return false;
    }
    var grid_selector;
    var recordId;
    if ($("#fw").hasClass("active")) {
        gdid = $("#gdproid").val();
        recordId = $("#gdproid").val();
        grid_selector = "#fw-grid-table";
    } else if ($("#td").hasClass("active")) {
        gdid = tdid;
        recordId = tdid;
        grid_selector = "#td-grid-table";
    } else if ($("#lq").hasClass("active")) {
        gdid = lqid;
        recordId = lqid;
        grid_selector = "#lq-grid-table";
    } else if ($("#cq").hasClass("active")) {
        gdid = cqid;
        recordId = cqid;
        grid_selector = "#cq-grid-table";
    }
    if ($bdclx == "TD")
        tdid = '';
    //遮罩
    $.blockUI({message: "请稍等……"});
    var ppDyh=true;
    if($bdclx=="TDFW") {
        //判断一证多房是不是全部匹配
        $.ajax({
            type: "GET",
            url: bdcdjUrl + "/bdcSjgl/checkGdfwNum?gdproid=" + gdproid + "&bdclx=" + $bdclx + "&isck=" ,
            dataType: "json",
            async: false,
            success: function (result) {
                if (result != null && result != "" && result.mulGdfw != "false") {
                    if (result.hasPic == "false") {
                        tipInfo("存在房屋没有匹配不动产单元，请先匹配不动产单元！");
                        setTimeout($.unblockUI, 10);
                        return;
                    }
                    ppDyh = false;
                }
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
            }
        });
    }
    $.ajax({
        url: bdcdjUrl+'/bdcSjgl/isCancel?lx=' + $bdclx+"&wiid="+wiid,
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data.hasOwnProperty("result")) {
                if (data.result) {
                    if(ppDyh)
                        matchData(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId);
                    createXm(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId);
                } else if (!data.result && data.msg != null && data.msg != "") {
                    setTimeout($.unblockUI, 10);
                    if (data.checkModel == "ALERT")
                        if (data.hasOwnProperty("xzwh")) {
                            var xzwh = data.xzwh;
                            var examineInfo = data.examineInfo;
                            if("${sflw}" == "true"){
                                showConfirmDialog("提示信息","" + data.msg + "," + "是否创建例外", "createLwGd", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + gdid + "','" + grid_selector + "','" + djId + "','" + xzwh + "','"+examineInfo+"'", "", "");
                            }else{
                                tipInfo(data.msg);
                            }
                        }else{
                            tipInfo("创建项目失败，失败原因：" + data.msg);
                        }
                    else if (data.checkModel == "CONFIRM") {
                        showConfirmDialog("提示信息", data.msg, "createXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + gdid + "','" + grid_selector + "','" + djId + "','" + ppDyh + "'", "", "");
                    }

                } else {
                    setTimeout($.unblockUI, 10);
                    alert("创建项目失败！")
                }
            } else {
                setTimeout($.unblockUI, 10);
                var str = "";
                $.each(data.resultList, function (index, obj) {
                    str += obj + "\n";
                })
                window.open(reportUrl+"/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&fwid=" + $("#fwid").val());
            }

        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            alert("创建项目失败！")
        }
    });
}

var createLwGd = function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId, xzwh,examineInfo) {
    $.ajax({
        url: '${bdcdjUrl}/examine/creatWiid',
        type: 'post',
        dataType: 'json',
        data: {examineInfo:examineInfo},
        success: function (data) {
            if (data.hasOwnProperty("wiid")) {
                wiid = data.wiid;
            }
            if (data.hasOwnProperty("lwsqUrl")) {
                var lwsqUrl = data.lwsqUrl;
                window.parent.parent.showDynamicModel('exceptionModel',lwsqUrl,"增加例外");
            }
        },
        error: function (data) {

        }
    });
}

var confirmCreateXm=  function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId,ppDyh) {
    if(ppDyh)
        matchData(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId);
    createXm(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId);

}
var dyhPic = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, djId, qlid) {
    if (ppzt == 3) {
        tipInfo("该证书已进行过匹配、并且正在发证，不能重新匹配！");
        return false;
    } else if (ppzt == 4) {
        tipInfo("该证书已进行过匹配、并且已经发证，不能重新匹配！");
        return false;
    }
    var grid_selector;
    var recordId;
    if ($("#fw").hasClass("active")) {
        gdid = $("#gdproid").val();
        recordId = $("#gdproid").val();
        grid_selector = "#fw-grid-table";
    } else if ($("#td").hasClass("active")) {
        gdid = tdid;
        recordId = qlid;
        grid_selector = "#td-grid-table";
    } else if ($("#lq").hasClass("active")) {
        gdid = lqid;
        recordId = lqid;
        grid_selector = "#lq-grid-table";
    } else if ($("#cq").hasClass("active")) {
        gdid = cqid;
        recordId = cqid;
        grid_selector = "#cq-grid-table";
    }

    if ($bdclx == "TD")
        tdid = '';
    var options = {
        url: bdcdjUrl+'/bdcSjgl/matchData',
        type: 'post',
        dataType: 'json',
        data: {
            gdid: gdid,
            bdcdyh: bdcdyh,
            tdzh: tdzh,
            fwid: fwid,
            bdclx: $bdclx,
            tdid: tdid,
            ppzt: '2',
            dyid: dyid,
            ygid: ygid,
            cfid: cfid,
            yyid: yyid,
            gdproid: gdproid,
            djId: djId
        },
        success: function (data) {
            tipInfo(data.result);
            if ($("#fw").hasClass("active"))
                changePpzt("2", $(grid_selector), recordId);
            else
                changeQtPpzt("2", $(grid_selector), recordId);
        },
        error: function (data) {
        }
    };
    $.ajax(options);
}

var disPic = function (gdproid, tdid, lqid, cqid,qlid) {
    var recordId = "";
    var grid_selector = "";
    var fwid = $("#fwid").val();
    var gdid = '';
    var gdtab = '';
    if ($("#fw").hasClass("active")) {
        recordId = gdproid;
        grid_selector = "#fw-grid-table";
        gdid = gdproid;
        gdtab = "fw";
    } else if ($("#td").hasClass("active")) {
        gdid = tdid;
        gdtab = "td";
        recordId = qlid;
        grid_selector = "#td-grid-table";
    } else if ($("#lq").hasClass("active")) {
        gdid = lqid;
        gdtab = "lq";
        recordId = lqid;
        grid_selector = "#lq-grid-table";
    } else if ($("#cq").hasClass("active")) {
        gdid = cqid;
        gdtab = "cq";
        recordId = cqid;
        grid_selector = "#cq-grid-table";
    }
    $.ajax({
        url: bdcdjUrl+'/bdcSjgl/dismatch',
        type: 'post',
        datatype: 'json',
        data: {gdid: gdid, gdproid: gdproid, tdid: tdid, lqid: lqid, cqid: cqid, gdtab: gdtab, fwid: fwid},
        success: function (data) {
            if (data == "fail") {
                alert("删除失败！");
            } else if (data == "error") {
                alert("项目id不能为空！");
            } else {
                alert("删除成功！");
                $("#dyh_search").click()
                if ($("#fw").hasClass("active")) {
                    changePpzt("0", $(grid_selector), recordId);
                    $("#fw_search").click();
                    $("#fwtd_search").click();
                } else if ($("#td").hasClass("active")) {
                    changeQtPpzt("0", $(grid_selector), recordId);
                    $("td_search").click();
                } else if ($("#lq").hasClass("active")) {
                    $("#lq_search").click();
                } else if ($("#cq").hasClass("active")) {
                    $("#cq_search").click();
                }

            }
        },
        error: function (data) {
            alert("删除失败！");
        }
    });

}

var createXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId) {
    var tdidforTdid = "";
    $('input[name="tdXl"]:checked').each(function () {
        tdidforTdid += $(this).parent().find('input[name="tdidTemp"]').val() + ","
    })
    $.ajax({
        url: bdcdjUrl+'/bdcSjgl/creatCsdj?lx=' + $bdclx + "&tdids=" + tdidforTdid + "&step=2",
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                setTimeout($.unblockUI, 10);
                openWin(portalUrl+'/taskHandle?taskid=' + data.taskid, '_task');
            } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                setTimeout($.unblockUI, 10);
                alert("创建项目成功!");

            } else {
                setTimeout($.unblockUI, 10);
                alert("创建项目失败，失败原因：" + data.msg);
            }


        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
        }
    });


}
var matchData= function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector, djId) {
    var options = {
        url: bdcdjUrl+'/bdcSjgl/matchData',
        type: 'post',
        dataType: 'json',
        async: false,
        data: {
            gdid: gdid,
            bdcdyh: bdcdyh,
            fwid: fwid,
            bdclx: $bdclx,
            tdid: tdid,
            ppzt: '3',
            dyid: dyid,
            ygid: ygid,
            cfid: cfid,
            yyid: yyid,
            gdproid: gdproid,
            djId: djId
        },
        success: function (matchData) {
            if ($("#fw").hasClass("active"))
                changePpzt("3", $(grid_selector), gdid);
            else if(bdcdyh==''|| bdcdyh==null&&bdcdyh == 'undefined')
                changeQtPpzt("0", $(grid_selector), gdid);
            else
                changeQtPpzt("3", $(grid_selector), gdid);

        },
        error: function (data) {
            $.ajax({
                url: bdcdjUrl+'/bdcSjgl/matchData',
                type: 'post',
                dataType: 'json',
                async: false,
                data: {
                    gdid: gdid,
                    bdcdyh: bdcdyh,
                    fwid: fwid,
                    bdclx: $bdclx,
                    tdid: tdid,
                    ppzt: '2',
                    dyid: dyid,
                    ygid: ygid,
                    cfid: cfid,
                    yyid: yyid,
                    gdproid: gdproid
                },
                success: function (data) {
                    if ($("#fw").hasClass("active"))
                        changePpzt("2", $(grid_selector), gdid);
                    else
                        changeQtPpzt("2", $(grid_selector), gdid);
                    setTimeout($.unblockUI, 10);
                    alert("创建项目失败！");
                },
                error: function (data) {
                    setTimeout($.unblockUI, 10);
                }
            });
        }
    };
    $.ajax(options);
}
var createMulXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector) {
    var tdidforTdid = "";
    var djlx = $("#djlxSelect").find("option:selected").val();
    $('input[name="tdXl"]:checked').each(function () {
        tdidforTdid += $(this).parent().find('input[name="tdidTemp"]').val() + ","
    })
    if ($("#fw").hasClass("active") && ppzt == 3) {
        tipInfo("该证书正在发证，不能再次发证！");
        return false;
    } else if ($("#fw").hasClass("active") && ppzt == 4) {
        tipInfo("该证书已经发证，不能再次发证！");
        return false;
    }
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl+'/bdcSjgl/updateGdPpzt',
        type: 'post',
        dataType: 'json',
        data: {
            bdclx: $bdclx,
            ppzt: '3',
            gdproid: gdproid,
            djlx: djlx
        },
        success: function (matchData) {
            var fwtdids = getFwtdids();
            if (fwtdids.length == 1) {
                $("#tdid").val(fwtdids);
            } else {
                tdidforTdid = fwtdids;
            }
            //                                        refreshStore();
            if ($("#fw").hasClass("active"))
                changePpzt("3", $(grid_selector), gdid);
            else
                changeQtPpzt("3", $(grid_selector), gdid);
            $.ajax({
                url: bdcdjUrl+'/bdcSjgl/creatCsdj?lx=' + $bdclx + "&tdids=" + tdidforTdid + "&step=2",
                type: 'post',
                dataType: 'json',
                data: $("#form").serialize(),
                success: function (data) {
                    if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                        setTimeout($.unblockUI, 10);
                        openWin(portalUrl+'/taskHandle?taskid=' + data.taskid, '_task');
                    } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                        setTimeout($.unblockUI, 10);
                        alert("创建项目成功!");

                    } else {
                        setTimeout($.unblockUI, 10);
                        alert("创建项目失败，失败原因：" + data.msg);
                    }


                },
                error: function (data) {
                    $.ajax({
                        url: bdcdjUrl+'/bdcSjgl/updateGdPpzt',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            gdid: gdid,
                            bdcdyh: bdcdyh,
                            fwid: fwid,
                            bdclx: $bdclx,
                            tdid: tdid,
                            ppzt: '2',
                            dyid: dyid,
                            ygid: ygid,
                            cfid: cfid,
                            yyid: yyid,
                            gdproid: gdproid
                        },
                        success: function (data) {
                            if ($("#fw").hasClass("active"))
                                changePpzt("2", $(grid_selector), gdid);
                            else
                                changeQtPpzt("2", $(grid_selector), gdid);
                            setTimeout($.unblockUI, 10);
                            alert("创建项目失败！");
                        },
                        error: function (data) {
                            setTimeout($.unblockUI, 10);
                        }
                    });
                }
            });
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
        }
    };
    $.ajax(options);

}
var showMulGdFwPic = function (gdproid, readOnly) {

    var sqlx = $("#sqlxSelect").val();
    var url = bdcdjUrl+"/bdcSjgl/openMulGdFwPic?gdproid=" + gdproid + "&sqlxdm=" + sqlx + "&readOnly=" + readOnly;
    openWin(url, "房屋匹配");
}

function serchfc(gdproid, slbh) {
    $("#fw_search_qlr").val("");
    var index = 0;
    var data = {hhSearch: '', gdproid: gdproid};
    var Url = bdcdjUrl+"/bdcSjgl/getGdXmFwJsonByPage";
    var jqgrid = $("#fw-grid-table");
    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    index++;
                    if (index == 1) {
                        $("#xmmc").val(rowObject.RF1DWMC);
                        $("#fwid").val(rowObject.FWID);
                        $("#dah").val(rowObject.DAH);
                        fwSel(rowObject.PROID, rowObject.FWZL, rowObject.RF1DWMC, rowObject.SLBH, rowObject.PPZT, rowObject.DJLX)
//                    fwSel('', rowObject.RF1DWMC, rowObject.SLBH, rowObject.PPZT, rowObject.DJLX);
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" checked="true" onclick="fwSel(\'' + rowObject.PROID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.RF1DWMC + '\',\'' + rowObject.SLBH + '\',\'' + rowObject.PPZT + '\',\'' + rowObject.YWLX + '\',\'' + rowObject.DJLX + '\')"/>';
                    } else {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.PROID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.RF1DWMC + '\',\'' + rowObject.SLBH + '\',\'' + rowObject.PPZT + '\',\'' + rowObject.YWLX + '\',\'' + rowObject.DJLX + '\')"/>';
                    }
                }
            },
            {name: 'SLBH', index: 'SLBH', width: '15%', sortable: false},
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
            {name: 'YWLX', index: 'YWLX', width: '15%', sortable: false},
            {name: 'FWZL', index: 'FWZL', width: '28%', sortable: false},
            {name: 'PPZT', index: 'PPZT', width: '15%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true}
        ]
    });
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
    $("#fw_search_qlr").next().hide();
    if (slbh != '' && slbh != 'undefined')
        $("#fw_search_qlr").val(slbh);

}

//通过房产证号级联不动产单元
function selDyhByFw(bdcdyh, bdcdyhs) {
    var index = 0;
    var Url = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
    if (bdcdyhs != null && bdcdyhs != "") {
        bdcdyhs = bdcdyhs.join(",");
    }
    var data = {hhSearch: '', bdcdyh: bdcdyh, bdcdyhs: bdcdyhs, bdclx: $bdclx};
    var jqgrid = $("#dyh-grid-table");
    var grid_selector = "#dyh-grid-table";

    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {
                name: 'XL',
                index: 'XL',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    index++;
                    if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.indexOf(rowObject.BDCDYH) > -1) {
                        $("#bdcdyh").val("");
                        $("#djId").val("");
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" checked="true"  value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>';
                    } else if (index == 1) {
                        $("#bdcdyh").val(rowObject.BDCDYH);
                        $("#djId").val(rowObject.ID);
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" checked="true" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>';
                    } else {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>';
                    }
                }
            },
            {name: 'YDJH', index: 'YDJH', width: '15%', sortable: false},
            {name: 'TDZL', index: 'TDZL', width: '25%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
            {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '22%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return "";
                    }
                    var value = cellvalue.substr(19);
                    return value;
                }
            },
            {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
            {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
        ],
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);

//                    addYdjhForTable(grid_selector);
                oldDjhForTable(grid_selector);
                qlrForTable("#dyh-grid-table");
            }, 0);
            //如果10条设置宽度为auto,如果少于7条就设置固定高度
            if ($(table).jqGrid("getRowData").length == $rownum) {
                $(table).jqGrid("setGridHeight", "auto");
            } else {
                $(table).jqGrid("setGridHeight", $pageHight);
            }
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        }
    });
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
    if (djIds != null && djIds != "") {
        $("#djIds").val(djIds);
    }

}

function dwbdcdy(bdcdyh) {
    var bdclxdm="";
    if( $("#fw").hasClass("active")){
        bdclxdm="TDFW"
    }else if($("#td").hasClass("active")){
        bdclxdm="TD"
    }
    openWin(bdcdjUrl+'/bdzDyMap?bdcdyh=' + bdcdyh + "&bdclxdm=" + bdclxdm);
}
//选择不动产单元号的赋值操作
function dyhSel(dyh, id, bdclx, xmmc, dah) {
    var zdzhh;
    if (dyh && dyh != 'undefined') {
        zdzhh = dyh.substr(0, 19);
        $("#bdcdyh").val(dyh);
        //定位
//        bdzDyMap(dyh);
        //$("#zdzhh").val(zdzhh);
    } else {
        $("#bdcdyh").val("");
        //$("#zdzhh").val("");
    }
    if (id && id != 'undefined') {
        $("#djId").val(id);
    } else {
        $("#djId").val("");
    }

}
//通过不动产单元关联房产证号
function selFczhByBdcdyh(dah) {
    var index = 0;
    var Url = bdcdjUrl+"/bdcSjgl/getGdXmFwJsonByPage";
    var data = {dah: dah};
    var jqgrid = $("#fw-grid-table");
    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    index++;
                    if (index == 1) {
                        $("#xmmc").val(rowObject.RF1DWMC);
                        $("#fwid").val(rowObject.FWID);
                        $("#dah").val(rowObject.DAH);
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" checked="true" onclick="fwSel(\'' + rowObject.FCZH + '\',\'' + rowObject.FWID + '\',\'' + rowObject.SLBH + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.FWID + '\',\'' + rowObject.DYID + '\',\'' + rowObject.YGID + '\',\'' + rowObject.CFID + '\',\'' + rowObject.YYID + '\',\'' + rowObject.BDCID + '\')"/>';
                    } else {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.FCZH + '\',\'' + rowObject.FWID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.FWID + '\',\'' + rowObject.DYID + '\',\'' + rowObject.YGID + '\',\'' + rowObject.CFID + '\',\'' + rowObject.YYID + '\',\'' + rowObject.BDCID + '\')"/>';
                    }
                }
            },
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
            {name: 'FCZH', index: 'FCZH', width: '20%', sortable: false},
            {name: 'FWZL', index: 'FWZL', width: '55%', sortable: false}
        ]
    });
    jqgrid.trigger("reloadGrid");//重新加载JqGrid

}

//通过不动产单元获取项目id，定位
function bdzDyMap(bdcdyh) {
    if (bdcdyh != '' && bdcdyh != undefined && bdcdyh != "undefined") {
        $("#iframeSrcUrl").val(bdcdjUrl+"/bdzDyMap?bdcdyh=" + bdcdyh + "&bdclxdm=" + $bdclx);
        if ($("#ace-settings-box").hasClass("open")) {
            $("#iframe").attr("src", bdcdjUrl+"/bdzDyMap?bdcdyh=" + bdcdyh + "&bdclxdm=" + $bdclx);
        }
    }
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
    } else if (loadComplete != '' && colModel == '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, loadComplete:loadComplete});
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
        url: "",
        datatype: "local",
        height: $pageHight,
        jsonReader: {id: 'PROID'},
        colNames: ['序列', '受理编号', '权利人', "业务类型", '坐落', '匹配状态', 'PROID'],
        colModel: fwColModel,
        viewrecords: true,
        rowNum: $rownum, /*
         rowList:[10, 20, 30],*/
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            $("#fwid").val("");
            $("#dah").val("");
            $("#xmmc").val("");
            $("#dyid").val("");
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getPpzt(data.PPZT, $(grid_selector), data.PROID);
                asyncGetGdFwxx(data.PROID, $(grid_selector));
            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//zx 获取房产证抵押 查封 预告 异议 状态
function asyncGetGdFwxx(proid, table) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getGdFwxx?proid=" + proid,
        dataType: "json",
        success: function (result) {
            if (result != null && result != '') {
                if (result.qlr != '' && result.qlr != 'undefind')
                    table.setCell(proid, "RF1DWMC", result.qlr);
                if (result.zl != '' && result.zl != 'undefind')
                    table.setCell(proid, "FWZL", result.zl);
            }
        }
    });
}
//为表格添加地籍号列数据
function addDjhForTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getDjhByYdjh(data.DJH, $(grid_selector), rowIds[index]);
    })
}

/*
 //为表格添加原地籍号列数据
 function addYdjhForTable(grid_selector) {
 var jqData = $(grid_selector).jqGrid("getRowData");
 var rowIds = $(grid_selector).jqGrid('getDataIDs');
 $.each(jqData, function (index, data) {
 getYdjhBydjh(data.DJH, $(grid_selector), rowIds[index]);
 })
 }
 */

function oldDjhForTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getOldDjhByDjh(data.DJH, $(grid_selector), rowIds[index]);
    })
}
//为表格添加权利人列数据
function qlrForTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getQlrByDjid(data.ID, $(grid_selector), rowIds[index]);
    })
}

//获取老地籍号
function getOldDjhByDjh(djh, table, rowid) {
    if (djh == null || djh == "undefined")
        djh = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getOldDjhByDjh?djh=" + djh,
        success: function (result) {
            var oldDjh = result.oldDjh;
            if (oldDjh == null || oldDjh == "undefined")
                oldDjh = "";
            var cellVal = "";
            cellVal += '<span>' + oldDjh + '</span>';
            table.setCell(rowid, "YDJH", cellVal);
        }
    });
}
//获取过程房产项目的匹配状态
function getPpzt(ppzt, table, rowid) {
    if (ppzt == "1")
        ppzt = '<span class="label label-warning" value="1">已部分匹配未发证</span>';
    else if (ppzt == "2")
        ppzt = '<span class="label label-success" value="2">已匹配未发证</span>';
    else if (ppzt == "3")
        ppzt = '<span class="label label-warning" value="3">已匹配正在发证</span>';
    else if (ppzt == "4")
        ppzt = '<span class="label label-info" value="4">已匹配已发证</span>';
    else
        ppzt = '<span class="label label-danger" value="0">待匹配未发证</span>';
    table.setCell(rowid, "PPZT", ppzt);
}
//获取老地籍号
function getOldDjhByDjid(djid, table, rowid) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getOldDjhByDjid?djid=" + djid + "&bdclxdm=" + $bdclx,
        success: function (result) {
            var oldDjh = result.oldDjh;
            if (oldDjh == null || oldDjh == "undefined")
                oldDjh = "";
            var cellVal = "";
            cellVal += '<span>' + oldDjh + '</span>';
            table.setCell(rowid, "YDJH", cellVal);
        }
    });
}
//获取权利人
function getQlrByDjid(djid, table, rowid) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + $bdclx,
        success: function (result) {
            var qlr = result.qlr;
            if (qlr == null || qlr == "undefined")
                qlr = "";
            var cellVal = "";
            cellVal += '<span>' + qlr + '</span>';
            table.setCell(rowid, "QLR", cellVal);
        }
    });
}
//获取地籍号
function getDjhByYdjh(ydjh, table, rowid) {
    if (ydjh == null || ydjh == "undefined")
        ydjh = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getDjhByYdjh?ydjh=" + ydjh,
        dataType: "json",
        success: function (result) {
            var djh = result.djh;
            if (djh == null || djh == "undefined")
                djh = "";
            var cellVal = "";
            cellVal += '<span>' + djh + '</span>';
            table.setCell(rowid, "XDJH", cellVal);
        }
    });
}
//获取 抵押 查封 预告 状态
function getDyYgCfStatusTd(table, tdid) {
    if (tdid == null || tdid == 'undefined') {
        tdid = "";
    }
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/asyncGetGdQlztByBdcid?tdid=" + tdid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            if (result.qlzts == null || result.qlzts == "") {
                cellVal = '<span class="label label-success">正常</span>';
            } else {
                var qlzts = result.qlzts.split(",");
                for (var i = 0; i < qlzts.length; i++) {
                    if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span><span> </span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span><span> </span>';
                    }else if (qlzts[i] == "DI") {
                        cellVal += '<span class="label label-info">地役</span><span> </span>';
                    }else if (qlzts[i] == "ZX") {
                        cellVal += '<span class="label label-danger">注销</span><span> </span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }
            if (result.zls != null && result.zls != "" && result.qlrs != "null")
                table.setCell(tdid, "ZL", result.zls);
            table.setCell(tdid, "STATUS", cellVal);
        }
    });
}
//获取 抵押 查封 预告 状态
function getDyYgCfStatus(bdcid, table, rowid, dyid) {
    if (dyid == null || dyid == "undefined")
        dyid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getDyYgCfStatus?bdcid=" + bdcid + "&dyid=" + dyid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            if (dyid != null && dyid != "") {
                if (result.dyZx)
                    cellVal += '<span class="label label-danger">注销</span><span> </span>';
                else
                    cellVal += '<span class="label label-success">正常</span><span> </span>';
            } else {
                //正常
                if (result.cf && result.dy && result.yg) {
                    cellVal = '<span class="label label-success">正常</span>';
                } else {//有查封 预告 或 抵押
                    if (!result.cf) {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                    }
                    if (!result.dy) {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                    }
                    if (!result.yg) {
                        cellVal += '<span class="label label-info">预告</span>';
                    }
                }
            }

            table.setCell(rowid, "STATUS", cellVal);
        }
    });
}
//获取土地林权匹配状态
function getQtPpzt(ppzt, table, rowid) {
    if (ppzt == "1" || ppzt == "2" || ppzt == "3" || ppzt == "4")
        ppzt = '<span class="label label-success" value=' + ppzt + '>已匹配</span>';
    else
        ppzt = '<span class="label label-danger" value=' + ppzt + '>待匹配</span>';
    table.setCell(rowid, "PPZT", ppzt);
}
//土地证初始化
//土地证初始化
function tdTableInit() {
    var grid_selector = "#td-grid-table";
    var pager_selector = "#td-grid-pager";

    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        url: bdcdjUrl+"/bdcJgSjgl/getGdTdJson?filterFwPpzt=${filterFwPpzt!}",
        datatype: "json",
        height: $pageHight,
        jsonReader: {id: 'QLID'},
        colNames: ['序列','锁定否', '地籍号', '坐落', "土地证号", '匹配状态', '权利状态', '权利人', 'QLID', 'PROID'],
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl"  onclick="tdSel(\'' + rowObject.PROID + '\',\'' + rowObject.QLID + '\')"/>'
                }
            },
            {name: 'SDF', index: 'SDF', width: '10%', sortable: false},
            {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '25%', sortable: false},
            {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
            {name: 'PPZT', index: '', width: '13%', sortable: false},
            {name: 'STATUS', index: '', width: '13%', sortable: false},
            {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '0%', sortable: false, hidden: true},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: gridRowNum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);

//                addDjhForTable(grid_selector);

            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == gridRowNum) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                asyncGetGdTdxx($(grid_selector), data.QLID, data.PROID);
                getSdStatus(data.QLID, data.TDZH, $(grid_selector), data.PROID, "TD");
            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//zx 获取锁定状态
function getSdStatus(qlid, cqzh, table, proid, lx) {
    var cellVal = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getXzyy",
        dataType: "json",
        data: {cqzh: cqzh},
        async: false,
        success: function (jsonData) {
            if(jsonData.msg == "false"){
                var xzyy = jsonData.xzyy;
                if (lx == "TD") {
                    cellVal = '<div title="'+xzyy+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                        '<span><img src="../static/img/locked.png" width="20px" height="20px" /></span>' +
                        '</div>';
                    table.setCell(qlid, "SDF", cellVal);
                }
            }
        },
        error: function (data) {
        }
    });
}

//zx 获取土地证抵押 查封 预告 异议 状态
function asyncGetGdTdxx(table, qlid, proid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/asyncGetGdTdxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            //正常
            if (result.qlzts == null || result.qlzts == "") {
                cellVal = '<span class="label label-success">正常</span>';
            } else {
                var qlzts = result.qlzts.split(",");
                for (var i = 0; i < qlzts.length; i++) {
                    if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span>';
                    } else if (qlzts[i] == "DI") {
                        cellVal += '<span class="label label-info">地役</span>';
                    } else if (qlzts[i] == "ZX") {
                        cellVal += '<span class="label label-danger">注销</span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }

            table.setCell(qlid, "STATUS", cellVal);
//                if (result.qlrs != null && result.qlrs != "" && result.qlrs != "null")
//                    table.setCell(qlid, "RF1DWMC", result.qlrs);
//                if (result.cqzhs != null && result.cqzhs != "" && result.cqzhs != "null")
//                    table.setCell(qlid, "TDZH", result.cqzhs);
//                if (result.zls != null && result.zls != "" && result.zls != "null")
//                    table.setCell(qlid, "ZL", result.zls);
//                if (result.djhs != null && result.djhs != "" && result.djhs != "null")
//                    table.setCell(qlid, "LDJH", result.djhs);
//                if (result.xdjhs != null && result.xdjhs != "" && result.xdjhs != "null")
//                    table.setCell(qlid, "DJH", result.xdjhs);
            getQtPpzt(result.ppzt, table, qlid);
        }
    });
}
//草原表初始化
function cqTableInit() {
    var grid_selector = "#cq-grid-table";
    var pager_selector = "#cq-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: $pageHight,
        jsonReader: {id: 'CQID'},
        colNames: ['序列', '权利人', "草原证号", '坐落', '匹配状态', 'ID'],
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="cqXl" onclick="cqSel(\'' + rowObject.CQZH + '\',\'' + rowObject.CQID + '\',\'' + rowObject.ZL + '\',\'' + rowObject.PPZT + '\')"/>'
                }
            },
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '30%', sortable: false},
            {name: 'CQZH', index: 'CQZH', width: '25%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '30%', sortable: false},
            {name: 'PPZT', index: '', width: '15%', sortable: false},
            {name: 'CQID', index: 'CQID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: $rownum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //清空
            $("#cqid").val("");
            $("#xmmc").val("");
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getDyYgCfStatus(data.CQID, $(grid_selector), data.CQID, '');
                getQtPpzt(data.PPZT, $(grid_selector), data.CQID);
            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container: 'body'});
    $(table).find('.ui-pg-div').tooltip({container: 'body'});
}

//林权表初始化
function lqTableInit() {
    var grid_selector = "#lq-grid-table";
    var pager_selector = "#lq-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: $pageHight,
        jsonReader: {id: 'LQID'},
        colNames: ['序列', '权利人', "林权证号", '坐落', '匹配状态', 'ID'],
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="lqXl" onclick="lqSel(\'' + rowObject.LQZH + '\',\'' + rowObject.LQID + '\',\'' + rowObject.LQZL + '\',\'' + rowObject.PPZT + '\')"/>'
                }
            },
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '30%', sortable: false},
            {name: 'LQZH', index: 'LQZH', width: '25%', sortable: false},
            {name: 'LQZL', index: 'LQZL', width: '30%', sortable: false},
            {name: 'PPZT', index: '', width: '15%', sortable: false},
            {name: 'LQID', index: 'LQID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: $rownum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //清空
            $("#lqid").val("");
            $("#xmmc").val("");
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getDyYgCfStatus(data.LQID, $(grid_selector), data.LQID, '');
                getQtPpzt(data.PPZT, $(grid_selector), data.LQID);
            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}
function dyhTableInit() {
    var grid_selector = "#dyh-grid-table";
    var pager_selector = "#dyh-grid-pager";


    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        url: "",
        datatype: "local",
        height: $pageHight,
        jsonReader: {id: 'ID'},
        colNames: ['序列', '原地籍号', "坐落", '权利人', '地籍号', '不动产单元号', '不动产类型','定位', 'ID'],
        colModel: dyhColModel,
        viewrecords: true,
        rowNum: $rownum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false, /*
         multiboxonly:true,
         multiselect:true,*/
        loadComplete: dyhLoadComplete,
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true

    });
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

//根据key获取是否加载数据

function isLoadGrid(key) {
    var gdTabOrderArray = gdTabOrder.split(",");
    var gdTabLoadDataArray  = gdTabLoadData.split(",");
    if (gdTabOrderArray.length == gdTabLoadDataArray.length) {
        for (var i = 0; i < gdTabOrderArray.length; i++) {
            if (gdTabOrderArray[i] == key) {
                var loadGrid = gdTabLoadDataArray[i];
                if (loadGrid == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    } else
        return false;
}

//房屋土地证初始化
function fwTdTableInit() {

    var grid_selector = "#fwTd-grid-table";
    var pager_selector = "#fwTd-grid-pager";

    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        url: "",
        datatype: "json",
        height: $pageHight,
        jsonReader: {id: 'TDID'},
        colNames: ['序列', '土地证号', "坐落", '状态', 'ID'],
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '"  title="'+ rowObject.PROID+'"/>';
                }
            },
            {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '40%', sortable: false},

            {name: 'STATUS', index: '', width: '23%', sortable: false},
            {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: gridRowNum,
        pager: pager_selector,
        pagerpos: "left",
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
            if ($(grid_selector).jqGrid("getRowData").length == gridRowNum) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getDyYgCfStatusTd($(grid_selector), data.TDID)
            })

            $(table).jqGrid("setGridWidth", parent_column.width());
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//通过房产证号级联不动产单元
//通过房产证号级联不动产单元
function selFwTdByFw(tdid, tdids,qlid) {
    var index = 0;
    var Url = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
    if (tdids != null && tdids != "")
        tdids = tdids.join(",");
    var data = {hhSearch: '', tdid: tdid, tdids: tdids,qlid:qlid,fwtdz: 'true'};
    var jqgrid = $("#fwTd-grid-table");
    var pager_selector = "#fwTd-grid-pager";
    var parent_column = $(jqgrid).closest('[class*="col-"]');

    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    index++;
                    if (tdids != null && tdids != "" && tdids.indexOf(rowObject.TDID) > -1) {
                        $("#tdid").val("");
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '" title="'+ rowObject.PROID+'"/>';
                    } else if (index == 1) {
                        $("#tdid").val(rowObject.TDID);
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '" title="'+ rowObject.PROID+'"/>';
                    } else {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '" title="'+ rowObject.PROID+'"/>';
                    }
                }
            },
            {name: 'TDZH', index: 'TDZH', width: '30%', sortable: false},
//                {name: 'DJH', index: 'DJH', width: '22%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '38%', sortable: false},
//                {name: 'QLR', index: 'QLR', width: '20%', sortable: false},
            {name: 'STATUS', index: '', width: '15%', sortable: false},
            {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
        ],
        loadComplete: function () {
            var table = this;
            var ybdcqzh = $("#ybdcqzh").val();
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果10条设置宽度为auto,如果少于7条就设置固定高度
            if ($(table).jqGrid("getRowData").length == $rownum) {
                $(table).jqGrid("setGridHeight", "auto");
            } else {
                $(table).jqGrid("setGridHeight", $pageHight);
            }
            $(table).jqGrid("setGridWidth", parent_column.width());
            var jqData = $(jqgrid).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
//                    getDyYgCfStatus($(jqgrid), data.TDID);
                getDyYgCfStatusTd($(jqgrid), data.TDID);
                ybdcqzh = ybdcqzh + "," + data.TDZH + ";";
            });
            $("#ybdcqzh").val(ybdcqzh);
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        }
    });
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
function fwTdSel(tdzh, tdid) {
    $("#tdzh").val(tdzh);
    $("#tdid").val(tdid);
}

//open新窗口
function addOrUpdate(url) {
    openWin(url);
}
function refreshStore() {
    if ($bdclx == "TDFW") {
        $("#fw_search").click();
        $("#fwtd_search").click();
    } else if ($bdclx == "TD")
        $("#td_search").click();
    else if ($bdclx == "TDQT")
        $("#cq_search").click();
    else if ($bdclx == "TDSL")
        $("#lq_search").click();

}
//改变匹配状态
function changePpzt(ppzt, table, rowid) {
    $("#ppzt").val(ppzt);
    if (ppzt == "1")
        ppzt = '<span class="label label-warning">已部分匹配未发证</span>';
    else if (ppzt == "4")
        ppzt = '<span class="label label-info">已匹配已发证</span>';
    else if (ppzt == "3")
        ppzt = '<span class="label label-warning">已匹配正在发证</span>';
    else if (ppzt == "2")
        ppzt = '<span class="label label-success">已匹配未发证</span>';
    else
        ppzt = '<span class="label label-danger">待匹配未发证</span>';
    table.setCell(rowid, "PPZT", ppzt);
}
//改变匹配状态
function changeQtPpzt(ppzt, table, rowid) {
    $("#ppzt").val(ppzt);
    if (ppzt == "1" || ppzt == "2" || ppzt == "3" || ppzt == "4")
        ppzt = '<span class="label label-success" value=' + ppzt + '>已匹配</span>';
    else
        ppzt = '<span class="label label-danger" value=' + ppzt + '>待匹配</span>';
    table.setCell(rowid, "PPZT", ppzt);
}
function hideFwtdGrid() {
    $("#fwTdTab").hide();
    dyhTableInit();
    $("#dyhTab").click();
}
function showFwtdGrid() {
    $("#fwTdTab").show();
    dyhTableInit();
    fwTdTableInit();
    $("#dyhTab").click();
}
function changeSqlx() {
    var sqlx = $("#sqlxSelect").val();
    if (wfids != null && wfids != "" && wfids.indexOf(sqlx) > -1) {
        hideFwtdGrid();
        $("#tdid").val();
    } else if ($("#fw").hasClass("active")) {
        showFwtdGrid();
    }

}
function clearTabDiv() {
    $("#dyh_search_qlr").val('');
    $("#fwTd_search_qlr").val('');
    var dyhgrid_selector = "#dyh-grid-table";
    var dyhparent_column = $(dyhgrid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(dyhgrid_selector).jqGrid('setGridWidth', dyhparent_column.width());
        }
    });
    var fwTdgrid_selector = "#fwTd-grid-table";
    var fwTdparent_column = $(fwTdgrid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(fwTdgrid_selector).jqGrid('setGridWidth', fwTdparent_column.width());
        }
    });
//    $("#dyh-grid-table").jqGrid("setGridWidth",800);
//    $("#fwTd-grid-table").jqGrid("setGridWidth",800);
    jQuery("#dyh-grid-table").jqGrid("clearGridData");
    jQuery("#fwTd-grid-table").jqGrid("clearGridData");

}
function disableDyhTabDiv(id) {
    $("#dyh_search").unbind('click');
    $("#dyhTab").unbind('click');
    $("#dyh_search_qlr").attr("disabled", true);
    $("#fwTd_search").unbind('click');
    $("#fwTdTab").unbind('click');
    $("#fwTd_search_qlr").attr("disabled", true);
}
function visableDyhTabDiv(id) {


    $("#dyh_search").click(function () {
        resetBdcdyhs();
        var hhSearch = $("#dyh_search_qlr").val();
        var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
        tableReload("dyh-grid-table", dyhUrl, {
            hhSearch: hhSearch,
            bdcdyh: '',
            bdcdyhs: '',
            bdclx: $bdclx
        }, dyhColModel, '');
    })
    $("#fwTd_search").click(function () {
        resetFwtdids();
        var hhSearch = $("#fwTd_search_qlr").val();
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
        $bdclx = "TDFW";
        tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: '', tdids: ''}, '', '');
    })

    $("#dyh_search_qlr").attr("disabled", false);
    $("#fwTd_search_qlr").attr("disabled", false);
}
function getBdcdyhs() {
    var chk_value = [];
    $('input[name="dyhXl"]:checked').each(function () {
        chk_value.push($(this).val());
    });
    return chk_value;
}
function getFwtdids() {
    var chk_value = [];
    $('input[name="fwtdXl"]:checked').each(function () {
        chk_value.push($(this).val());
    });
    return chk_value;
}
function getFwtdProids() {
    var chk_value = [];
    $('input[name="fwtdXl"]:checked').each(function () {
        chk_value.push($(this)[0].title);
    });
    return chk_value;
}
function resetBdcdyhs() {
    $("input[name='dyhXl']").attr("checked", false);
    $("#bdcdyhs").val('');
}
function resetFwtdids() {
    $("input[name='fwtdXl']").attr("checked", false);
    $("#tdids").val('');
}
function getBdcdyDjIds() {
    var chk_value = [];
    $('input[name="dyhXl"]:checked').each(function () {
        chk_value.push($(this)[0].attributes.djid.value);
    });
    return chk_value;
}
function glxm(proid) {
    var ppzt = $("#ppzt").val();

    if (ppzt == 0 || ppzt == '') {
        tipInfo("请先匹配，再关联项目");
        return false;
    }
    if ($("#fw").hasClass("active") && ppzt == 3) {
        tipInfo("该证书正在发证，不能再次发证！");
        return false;
    } else if ($("#fw").hasClass("active") && ppzt == 4) {
        tipInfo("该证书已经发证，不能再次发证！");
        return false;
    }

    var yz = yzGlxmxx(proid);
    if (yz) {
        createGlxm(proid);
    } else {
        var msg = "中间库的权利人或坐落与收件单的不相符，是否继续创建？";
        showConfirmDialog("提示信息", msg, "createGlxm", "'" + proid + "'", "", "");
    }
}

function yzGlxmxx(proid) {
    //目前只支持房产项目，土地项目以后考虑
    var qlr = $("#qlr").val();
    var zl = $("#xmmc").val();
    var pd = false;
    $.ajax({
        url: "/bdcdj/bdcSjgl/yzGlxmxx?proid=" + proid + "&qlr=" + qlr + "&zl=" + zl,
        type: "GET",
        async: false,
        success: function (data) {
            pd = data;
        }
    });

    return pd;

}

function createGlxm(proid) {
    $.ajax({
        type: "POST",
        url: bdcdjUrl+"/bdcSjgl/glxm?&proid=" + proid,
        dataType: "json",
        data: $("#form").serialize(),
        success: function (data) {
            if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                setTimeout($.unblockUI, 10);
                openWin(portalUrl+'/taskHandle?taskid=' + data.taskid, '_task');
                $("#fileInput").hide();
                $("#fw_search").click();
            } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                setTimeout($.unblockUI, 10);
                alert("创建项目成功!");

            } else {
                setTimeout($.unblockUI, 10);
                alert("创建项目失败，失败原因：" + data.msg);
            }

        },
        error: function (data) {
        }
    });
}


function bdcxmTableInit() {
    var grid_selector = "#bdcxm-grid-table";
    var pager_selector = "#bdcxm-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', 580);
        }
    });
    var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        url: "",
        datatype: "local",
        height: $pageHight,
        jsonReader: {id: 'PROID'},
        colNames: ['受理编号', '权利人', '流程名称', '关联项目'],
        colModel: [
            {name: 'BH', index: 'BH', width: '20%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '25%', sortable: false},
            {name: 'SQLX', index: 'SQLX', width: '25%', sortable: false},
            {
                name: 'mydy',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:20px;"> <div title="关联项目" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="glxm(\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div>'
                }
            }
        ],
        viewrecords: true,
        rowNum: $rownum,
        pager: pager_selector,
        pagerpos: "left",
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
            if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
                $(grid_selector).jqGrid('setGridWidth', 580);
            } else {
                $(grid_selector).jqGrid('setGridWidth', 580);
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getPpzt(data.PPZT, $(grid_selector), data.FWID);
            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//选择多个房屋后关闭窗口更新房屋和单元号选择
var updateGdFwAndDyhSel = function (djlx) {
    var gdproid = $("#gdproid").val();
    var qlrmc = $("#xmmc").val();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getGdfwPpzt?gdproid=" + gdproid,
        dataType: "json",
        success: function (result) {
            getPpzt(result, $("#fw-grid-table"), gdproid);
        },
        error: function (data) {
        }
    });
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/queryBdcdyhByGdProid?gdproid=" + gdproid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);

                var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                //清空查询内容
                $("#dyh_search_qlr").val("");
                $("#dyh_search_qlr").next().show();
                var bdcdyhs = [];
                $.each(result, function (index, data) {
                    if (data.bdcdyh != null && data.bdcdyh != "" && data.bdcdyh != "undefined")
                        bdcdyhs.push(data.bdcdyh);
                });
                if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length > 1) {

                    selDyhByFw("", bdcdyhs);
                    $("#bdcdyhs").val(bdcdyhs);
                    $("#bdcdyh").val("");
                    $("#djId").val("");
                } else if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length == 1) {
                    selDyhByFw(bdcdyhs.join(""), "");
                    $("#bdcdyhs").val("");
                    $("#bdcdyh").val(bdcdyhs.join(""));
                    $("#djIds").val("");
                } else {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    $("#bdcdyh").val("");
                    $("#djId").val("");
                    var dyhUrl = bdcdjUrl+"/bdcSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: qlrmc,
                        bdcdyh: '',
                        bdclx: $bdclx
                    }, dyhColModel, dyhLoadComplete);
                }

            }
        },
        error: function (data) {
        }
    });

    var sqlx = $("#sqlxSelect").val();
    if (matchTdzh == "true" && (sqlx == null || sqlx == "" || wfids != null && wfids != "" && wfids.indexOf(sqlx) < 0)) {
        $.ajax({
            type: "GET",
            url: bdcdjUrl+"/bdcSjgl/queryBdcdyhByGdProid?gdproid=" + gdproid + "&bdclx=" + $bdclx,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#fwTd_search_qlr").next().hide();
                    $("#fwTd_search_qlr").val(qlrmc);
                    $("#tdzh").val('');

                    $("#tdid").val('');
                    var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                    tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                } else {
                    //清空查询内容
                    $("#fwTd_search_qlr").val("");
                    $("#fwTd_search_qlr").next().show();
                    var fwtdis = [];
                    $.each(result, function (index, data) {
                        if (data.tdid != null && data.tdid != "" && data.tdid != "undefined")
                            fwtdis.push(data.tdid);
                    });
                    if (fwtdis != null && fwtdis != "" && fwtdis.length > 1) {

                        selFwTdByFw("", fwtdis,"");
                        $("#fwtdis").val(fwtdis);
                        $("#tdzh").val('');
                        $("#tdid").val('');
                    } else if (fwtdis != null && fwtdis != "" && fwtdis.length == 1) {
                        selFwTdByFw(fwtdis.join(""), "","");
                        $("#fwtdis").val("");
                        $("#tdid").val(fwtdis.join(""));
                    } else {
                        $("#fwTd_search_qlr").next().hide();
                        $("#fwTd_search_qlr").val(qlrmc);
                        $("#tdid").val("");
                        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                        tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                    }

                }
            },
            error: function (data) {
            }
        });
        $("#fwTdTab").show();
    }


}