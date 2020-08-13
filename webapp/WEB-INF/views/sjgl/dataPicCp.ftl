<@com.html title="" import="ace,public">
<style>
    .filesub{
        display: block;
        width: 90px;
        line-height: 34px;
        height: 34px;
        text-align: center;
        background: #155e96;
        color: #fff;
        word-spacing: 10px;
    }

    a:visited{
        color: #FFF;
    }

    a:hover {
        color: #FFF;
    }

    .col-xs-11 {
        width: 100%
    }

    .tab-content {
        overflow: hidden;
        height: auto;
        width: auto;
    }

    .tableHeader {
        width: 800px;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
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

    .SSinput {
        min-width: 100px !important;
    }

        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 1175px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput .modal-dialog {
        width: 500px;
        position: fixed;
        top: 0;
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

    .title {
        font-size: 18pt;
        text-align: center;
        padding-left: 2px;
        position: absolute;
        margin-left: 40px;
        margin-top: 5px;
    }

    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
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

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
    }
</style>
<script type="text/javascript">
//table每页行数
$rownum = 8;
//table 每页高度
$pageHight = '300px';
//全局的不动产类型
$bdclx = 'TDFW';
var djIds = [];
//定义公用的基础colModel
fwColModel=[
    {name: 'XL', index: '', width: '15%', sortable: false, formatter: function (cellvalue, options, rowObject) {
        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.PROID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.PPZT + '\',\'' + rowObject.DJLX + '\',\'' + rowObject.QLID + '\',\'' + rowObject.QLZT + '\')"/>'
    }
    },
    {name:'RF1DWMC',index:'RF1DWMC',width:'15%',sortable:false},
    {name:'ZSLX',index:'ZSLX',width:'15%',sortable:false},
    {name:'FCZH',index:'FCZH', width:'20%',sortable:false},
    {name:'FWZL',index:'FWZL', width:'27%',sortable:false},
    {name:'PPZT', index:'', width:'15%', sortable:false},
    {name:'STATUS', index:'', width:'13%', sortable:false},
    {name:'QLZT', index:'QLZT', width:'0%', sortable:false,hidden:true},
    {name:'PROID', index:'PROID', width:'0%', sortable:false,hidden:true},
    {name:'QLID', index:'QLID', width:'0%', sortable:false,hidden:true}
];
dyhColModel = [
    {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>'
    }
    },
    {name: 'YDJH', index: 'YDJH', width: '15%', sortable: false},
    {name: 'TDZL', index: 'TDZL', width: '30%', sortable: false},
    {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
    {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
    {name: 'BDCDYH', index: 'BDCDYH', width: '15%', sortable: false, formatter: function (cellvalue, options, rowObject) {
        if (!cellvalue) {
            return"";
        }
        var value = cellvalue.substr(19);
        return value;
    }
    },
    {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
    {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
];
dyhLoadComplete = function () {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
        enableTooltips(table);
    }, 0);

    addYdjhForTable("#dyh-grid-table");
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
fwLoadComplete = function () {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
        enableTooltips(table);
    }, 0);
    qlrForFwTable("#fw-grid-table");
    //如果10条设置宽度为auto,如果少于7条就设置固定高度
    if ($(table).jqGrid("getRowData").length == $rownum) {
        $(table).jqGrid("setGridHeight", "auto");
    } else {
        $(table).jqGrid("setGridHeight", $pageHight);
    }
    //去掉遮罩
    setTimeout($.unblockUI, 10);
};
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

    if('${msgInfo}'!="null"){
        alert('${msgInfo}');
    }

    //左边房屋林权草权土地
    $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
        var url;
        //清空查询内容
        $("#dyh_search_qlr").val("");
        $("#dyh_search_qlr").next().show();
        if (this.id == "dyhTab") {
            $("#file").addClass("active");
            var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {hhSearch: '', bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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

                var fwUrl = "${bdcdjUrl}/bdcSjgl/getGdFwCpJson";
                fwTableInit();
                if (isLoadGrid("fw"))
                    tableReload("fw-grid-table", fwUrl, {hhSearch: '', fczh: '', dah: '', filterFwPpzt: "${filterFwPpzt!}"}, fwColModel,fwLoadComplete);
                $("#dyhTab").click();
            } else if (this.id == "lqTab") {
                $bdclx = 'TDSL';
                $("#lq").addClass("active");
                $("#dyhTab").click();
                var lqUrl = "${bdcdjUrl}/bdcSjgl/getGdLqJson";
                lqTableInit();
                if (isLoadGrid("lq"))
                    tableReload("lq-grid-table", lqUrl, {hhSearch: '', lqzh: ''}, '', '');
            } else if (this.id == "cqTab") {
                $bdclx = 'TDQT';
                $("#cq").addClass("active");
                $("#dyhTab").click();
                var cqUrl = "${bdcdjUrl}/bdcSjgl/getGdCqJson";
                cqTableInit();
                if (isLoadGrid("cq"))
                    tableReload("cq-grid-table", cqUrl, {hhSearch: '', cqzh: ''}, '', '');
            } else if (this.id == "tdTab") {
                $bdclx = 'TD';
                $("#td").addClass("active");
                $("#dyhTab").click();
                var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
                tdTableInit();
                if (isLoadGrid("td"))
                    tableReload("td-grid-table", tdUrl, {hhSearch: ''}, '', '');
            }
            getSqlxByDjlxAndBdclx('', '');
        }
        $("#fwTdTab").hide();

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/getDjlxByBdclx",
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
    var gdTabOrder = "${gdTabOrder!}";
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

    //查询按钮点击事件
    $("#fw_search").click(function () {
        var hhSearch = $("#fw_search_qlr").val();
        var fwUrl = "${bdcdjUrl}/bdcSjgl/getGdFwCpJson";
        tableReload("fw-grid-table", fwUrl, {hhSearch: hhSearch, fczh: '', dah: '', filterFwPpzt: '${filterFwPpzt!}'}, fwColModel,fwLoadComplete);
    })
    $("#dyh_search").click(function () {
        resetBdcdyhs();
        var hhSearch = $("#dyh_search_qlr").val();
        var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
        tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch, bdcdyh: '', bdclx: $bdclx}, dyhColModel, '');
    })
    $("#lq_search").click(function () {
        var hhSearch = $("#lq_search_qlr").val();
        var lqUrl = "${bdcdjUrl}/bdcSjgl/getGdLqJson";
        tableReload("lq-grid-table", lqUrl, {hhSearch: hhSearch, lqzh: ''}, '', '');
    })
    $("#cq_search").click(function () {
        var hhSearch = $("#cq_search_qlr").val();
        var cqUrl = "${bdcdjUrl}/bdcSjgl/getGdCqJson";
        tableReload("cq-grid-table", cqUrl, {hhSearch: hhSearch, cqzh: ''}, '', '');
    })
    $("#td_search").click(function () {
        var hhSearch = $("#td_search_qlr").val();
        var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
        tableReload("td-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', filterFwPpzt: '${filterFwPpzt!}'}, '', '');
    })
    $("#fwTd_search").click(function () {
        resetFwtdids();
        var hhSearch = $("#fwTd_search_qlr").val();
        var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson?fwtdz=true";
        $bdclx = "TDFW";
        tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
    })


    //登记类型变换事件
    $("#djlxSelect").change(function () {
        getSqlxByDjlxAndBdclx('', '');
    })


    //保存事件
    $("#save").click(function () {
        var qlzt = $("#qlzt").val();
        if (qlzt == '1'){
            tipInfo("该权利已经注销，无法创建项目！")
            return false;
        }
        $("#workFlowDefId").val($("#sqlxSelect  option:selected").val());
        $("#sqlx").val($("#sqlxSelect  option:selected").text());
        $("#djlx").val($("#djlxSelect  option:selected").val());
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
        if (bdcdyhs == null || bdcdyhs == '') {
            tipInfo("请选择不动产单元");
            return false;
        } else if (gdproid == '' && $("#fw").hasClass("active")) {
            tipInfo("请选择房产证");
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
            $("#gdproid").val("");
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
            }

            if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
                if (fwtdids.length > 1) {
                    tipInfo("请选择一个房屋土地证！");
                    return false;
                }
                tdid = fwtdids[0];
                $("#tdid").val(tdid);
            }


            var sqlxdm = $("#sqlxSelect  option:selected").val();
            var wfids = "${wfids!}";
            var djId=$("#djId").val();
            if(djId==null || djId=="undefined")
                djId="";
            if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                //当不等于商品房转移登记的需要匹配土地证
                if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && "${matchTdzh!}" == "true") {
                    var msg = "没有匹配房屋土地证，是否创建项目！";
                    showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + djId + "'", "", "");
                } else {
                    checkXm(gdproid, bdcdyh, '', fwid, tdid, dyid, ygid, cfid, yyid, ppzt,djId);
                }
            } else {
                checkXm(gdproid, bdcdyh, '', fwid, tdid, dyid, ygid, cfid, yyid, ppzt,djId);
            }
        } else{
            //存在多个房屋进行登记
            if(bdcdyDjIds!=null && bdcdyDjIds!="")
                $("#djIds").val(bdcdyDjIds.join("$"));
            if(bdcdyhs!=null && bdcdyhs!="")
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
            createMulXm(gdproid, bdcdyh, '', fwid, tdid, dyid, ygid, cfid, yyid, ppzt,gdid,grid_selector);
        }

    })
    $("#fz").click(function () {
        var fwid = $("#fwid").val();
        var djlx = $("#djlxSelect  option:selected").text();
        var gdid;
        var tdid = $("#tdid").val();
        var lqid = $("#lqid").val();
        var cqid = $("#cqid").val();
        var bdcdyh = $("#bdcdyh").val();
        var bdclx = "";
        var sqlxSelect = $("#sqlxSelect").val();
        if (bdcdyh == '') {
            tipInfo("请先匹配");
            return false;
        } else if (fwid == '' && $("#fw").hasClass("active")) {
            tipInfo("请先匹配");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active")) {
            tipInfo("请先匹配");
            return false;
        } else if (lqid == '' && $("#lq").hasClass("active")) {
            tipInfo("请先匹配");
            return false;
        } else if (cqid == '' && $("#cq").hasClass("active")) {
            tipInfo("请先匹配");
            return false;
        }
        if ($("#fw").hasClass("active")) {
            gdid = $("#dah").val();
            bdclx = "TDFW";
        } else if ($("#td").hasClass("active")) {
            gdid = tdid;
            bdclx = "TD";
        } else if ($("#lq").hasClass("active")) {
            gdid = lqid;
            bdclx = "TDSL";
        } else if ($("#cq").hasClass("active")) {
            gdid = cqid;
            bdclx = "TDQT";
        }
        var options = {
            url: '${bdcdjUrl}/bdcSjgl/zs',
            type: 'post',
            dataType: 'json',
            data: {gdid: gdid, bdcdyh: bdcdyh, sqlxSelect: sqlxSelect, djlx: djlx, bdclx: bdclx},
            success: function (data) {
                hideModal(data);
            },
            error: function (data) {
            }
        };
        $.ajax(options);
    });
    //匹配
    $("#match").click(function () {
        var qlzt = $("#qlzt").val();
        if(qlzt=='1'){
            tipInfo("该权利已经注销，无法匹配！");
            return false;
        }
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
            tipInfo("请选择房产证");
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
            $("#gdproid").val("");
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
            }
            if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
                if (fwtdids.length > 1) {
                    tipInfo("请选择一个房屋土地证！");
                    return false;
                }
                tdid = fwtdids[0];
                $("#tdid").val(tdid);
            }

            var djId=$("#djId").val();
            if(djId==null || djId=="undefined")
                djId="";
            var sqlxdm = $("#sqlxSelect  option:selected").val();
            var wfids = "${wfids!}";
            if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                //当不等于商品房转移登记的需要匹配土地证
                if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && "${matchTdzh!}" == "true") {
                    var msg = "没有匹配房屋土地证，是否匹配！";
                    showConfirmDialog("提示信息", msg, "dyhPic", "'" + gdproid + "','" + bdcdyh + "','" + tdzh + "','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + djId + "'", "", "");
                } else {
                    dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt,djId);
                }
            } else {
                dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt,djId);
            }
        }else{
            tipInfo("该项目存在多个房屋，请到多个房屋匹配页面进行匹配！");
        }

    })


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
        var bdclxId=this.id;
        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/gdXxLr/getUUid",
            dataType:"json",
            success:function (result) {
                if (result != null && result != "") {
                    if (bdclxId == "gdFwAdd") {
                        addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=fw&proid=" + result);
                    } else if (bdclxId == "gdLqAdd") {
                        addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write");
                    } else if (bdclxId == "gdTdAdd") {
                        addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=td&proid=" + result);
                    } else if (bdclxId== "gdCqAdd") {
                        addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write");
                    }
                }
            }
        });
    })

    //批量导入过渡房屋
    $("#gdFwsAdd").click(function(){
        $("#fileInput").show();
        $("#sjlx").val('gd_fw');
        $("#fileDownLoad").attr('href',"${bdcdjUrl}/static/Tool/房产证模板.xls");
    });

    $("#gdTdsAdd").click(function(){
        $("#fileInput").show();
        $("#sjlx").val('gd_td');
        $("#fileDownLoad").attr('href',"${bdcdjUrl}/static/Tool/土地证模版.xls");
    });

    $("#fileHide").click(function () {
        $("#fileInput").hide();
    });

    $("#fileSub").click(function () {
        //遮罩
        $.blockUI({ message: "请稍等……" });
    });

//修改按钮点击事件
    $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdCqUpdate").click(function () {
        if (this.id == "gdFwUpdate") {
            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }

            addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=fw&proid="+gdproid);
        }else if(this.id=="gdLqUpdate"){
            var lqid=$("#lqid").val();
            if(lqid==null || lqid==""){
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=" + lqid);
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
                }else if (tdids.length ==0) {
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

            addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=td&proid="+tdid);
        }else if(this.id=="gdCqUpdate"){
            var cqid=$("#cqid").val();
            if(cqid==null || cqid==""){
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=" + cqid);
        }
    })

    //注销按钮点击事件
    $("#gdFwZx").click(function(){
        var qlid = $("#qlid").val();
        var qlzt = $("#qlzt").val();
        if(qlzt == '0'){
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSjgl/zxQl?qlid="+qlid,
                dataType:"json",
                success:function(result){
                    if (result == "success"){
                        var qlzt = '1';
                        var rowid = $("#gdproid").val();
                        var table = "#fw-grid-table";
                        setQlzt($(table),qlzt,rowid);
                        $("#qlzt").val(qlzt);
                    }
                },
                error:function(data){

                }

            })
        } else {
            tipInfo("该权利已经注销");
        }
    })

    //解除注销按钮点击事件
    $("#gdFwJcZx").click(function(){
        var qlid = $("#qlid").val();
        var qlzt = $("#qlzt").val();
        if(qlzt == '1'){
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSjgl/jczxQl?qlid="+qlid,
                dataType:"json",
                success:function(result){
                    if (result == "success"){
                        var qlzt = '0';
                        var rowid = $("#gdproid").val();
                        var table = "#fw-grid-table";
                        setQlzt($(table),qlzt,rowid);
                        $("#qlzt").val(qlzt);
                    }
                },
                error:function(data){

                }

            })
        } else {
            tipInfo("该权利已经是正常状态");
        }
    })
})

//设置权利状态
function setQlzt(table,qlzt,rowid){
    var cellVal = "";
    if(qlzt == '0'){
        cellVal = '<span class="label label-success">正常</span>';
    } else if (qlzt == '1'){
        cellVal = '<span class="label label-gray">注销</span>';
    }
    table.setCell(rowid,"STATUS",cellVal);
}

function getSqlxByDjlxAndBdclx(djlx, wfid) {
    if (djlx == null || djlx == "")
        djlx = $("#djlxSelect  option:selected").text();
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getSqlxByDjlx",
        data: {djlx: djlx, bdclx: $bdclx},
        dataType: "json",
        success: function (result) {
            //清空
            $("#sqlxSelect").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    if (data.DM == "24D768DE8B8F4CD59F70E621C2CAB2E2" || data.DM == "19D3CDE088174478943341B32EF3238C" || data.DM == wfid)
                        $("#sqlxSelect").append('<option value="' + data.DM + '" selected="selected">' + data.MC + '</option>');
                    else
                        $("#sqlxSelect").append('<option value="' + data.DM + '" >' + data.MC + '</option>');
                })
            }
            $("#sqlxSelect").trigger("chosen:updated");
            var sqlx = $("#sqlxSelect").val();
            var wfids = "${wfids!}";
            if (wfids != null && wfids != "" && wfids.indexOf(sqlx) > -1)
                hideFwtdGrid();
        },
        error: function (data) {
        }
    });
}
function hideModal(proid) {
    if (proid && proid != undefined && proid != "undefined") {
        openWin('${bdcdjUrl}/bdcSjgl/formTab?proid=' + proid);
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
        url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByGdid?gdid=" + cqid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {

                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
        url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByGdid?gdid=" + lqid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
function tdSel(djh, id, qlrmc, ppzt, dyid) {
    if (id && id != 'undefined') {
        $("#tdid").val(id);
    } else {
        $("#tdid").val("");
    }
    if (qlrmc && qlrmc != 'undefined') {
        $("#xmmc").val(qlrmc);
    } else
        qlrmc = "";
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else
        $("#ppzt").val("");
    if (dyid && dyid != 'undefined')
        $("#dyid").val(dyid);
    else
        $("#dyid").val("");
    dyid = $("#dyid").val();
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByTdidDjh?tdid=" + id + "&djh=" + djh + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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

function fwSel(bdcid, qlrmc, ppzt, djlx,qlid,qlzt) {
    //遮罩
    $.blockUI({ message: "请稍等……" });
    //赋值
    if (qlrmc && qlrmc != 'undefined')
        $("#xmmc").val(qlrmc);
    else
        qlrmc = "";
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else {
        $("#ppzt").val("");
        ppzt = "";
    }
    if (qlid && qlid != 'undefined')
        $("#qlid").val(qlid);
    else
        qlid = "";
    if (qlzt && qlzt != 'undefined')
        $("#qlzt").val(qlzt);
    else
        qlzt = "";
    if (bdcid && bdcid != 'undefined')
        $("#gdproid").val(bdcid);
    else
        $("#gdproid").val("");
    if (djlx && djlx == 'undefined')
        djlx = "";

    bdcid = $("#gdproid").val();
    $("#dyhTab").click();
    $("#tdid").val('');
    resetBdcdyhs();
    resetFwtdids();

//    判断是否是多个房屋
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/checkGdfwNum?gdproid=" + bdcid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            $("#mulGdfw").val(result.mulGdfw);
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcSjgl/getGdFcDjlxToSqlxWfid?djlx=" + djlx,
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
                        disableDyhTabDiv("file");
                        clearTabDiv();
                        showConfirmDialog("提示信息", msg, "showMulGdFwPic", "'" + bdcid + "','" + readOnly + "'", "updateGdFwAndDyhSel", "'" + djlx + "'");
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                    } else {
                        visableDyhTabDiv("file");
                        $("#fwid").val(result.fwid);
                        picDyh(bdcid, result.fwid, qlrmc, ppzt, djlx);
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
var picDyh = function (bdcid, fwid, qlrmc, ppzt, djlx) {
    //通过fczh获取hs_index
    var bdcdyhs = getBdcdyhs();
    var fwtdids = getFwtdids();

    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByGdProid?gdproid=" + bdcid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
//                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
                }
            }
        },
        error: function (data) {
        }
    });

    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getGdFcDjlxToSqlxWfid?djlx=" + djlx,
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
            var wfids = "${wfids!}";
            if ("${matchTdzh!}" == "true" && result != null && (result.wfid == null || result.wfid == "" || wfids != null && wfids != "" && wfids.indexOf(result.wfid) < 0)) {
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/bdcSjgl/queryTdByGdproid?gdproid=" + bdcid,
                    dataType: "json",
                    success: function (result) {
                        if (result == '' || result == null) {
                            $("#fwTd_search_qlr").next().hide();
                            $("#fwTd_search_qlr").val(qlrmc);
                            $("#tdzh").val('');

                            $("#tdid").val('');
                            var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson?fwtdz=true";
                            tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                        } else {
                            //清空查询内容
                            $("#fwTd_search_qlr").val("");
                            $("#fwTd_search_qlr").next().show();
                            if (fwtdids != null && fwtdids != "" && fwtdids.length > 1) {
                                selFwTdByFw("", fwtdids);
                                $("#tdids").val(fwtdids);
                            } else if (fwtdids != null && fwtdids != "" && fwtdids.length == 1) {
                                selFwTdByFw(fwtdids.join(""), "");
                                $("#tdids").val("");
                                $("#tdid").val(fwtdids.join(""));
                            } else if (result[0].tdid != null && result[0].tdid != "" && result[0].tdid != "undefined") {
                                selFwTdByFw(result[0].tdid, "");
                                $("#tdids").val("");
                                $("#tdid").val(result[0].tdid);
                            } else if (result[0].TDID != null && result[0].TDID != "" && result[0].TDID != "undefined") {
                                selFwTdByFw(result[0].TDID, "");
                                $("#tdids").val("");
                                $("#tdid").val(result[0].TDID);
                            } else {
                                $("#fwTd_search_qlr").next().hide();
                                $("#fwTd_search_qlr").val(qlrmc);
                                var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
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
var checkXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt,djId) {
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
    $.blockUI({ message: "请稍等……" });
    $.ajax({
        url: '${bdcdjUrl}/bdcSjgl/isCancel?lx=' + $bdclx+"&gdFwWay=cg",
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data.hasOwnProperty("result")) {
                if (data.result) {
                    createXm(gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector,djId);
                } else if (!data.result && data.msg != null && data.msg != "") {
                    setTimeout($.unblockUI, 10);
                    if (data.checkModel == "ALERT")
                        alert("创建项目失败，失败原因：" + data.msg);
                    else if (data.checkModel == "CONFIRM") {
                        showConfirmDialog("提示信息", data.msg, "createXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "'", "", ",'" + gdid + "','" + grid_selector + "','" + djId + "'", "", "");
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
                window.open("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&fwid=" + $("#fwid").val());
            }

        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            alert("创建项目失败！")
        }
    });
}
var dyhPic = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt,djId) {
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

    var options = {
        url: '${bdcdjUrl}/bdcSjgl/matchData',
        type: 'post',
        dataType: 'json',
        data: {gdid: gdid, bdcdyh: bdcdyh, tdzh: tdzh, fwid: fwid, bdclx: $bdclx, tdid: tdid, ppzt: '2', dyid: dyid, ygid: ygid, cfid: cfid, yyid: yyid, gdproid: gdproid, djId: djId},
        success: function (data) {
            tipInfo(data.result);
//            if ($("#fw").hasClass("active"))
//                changePpzt("2", $(grid_selector), recordId);
//            else
                changeQtPpzt("2", $(grid_selector), recordId);
        },
        error: function (data) {
        }
    };
    $.ajax(options);
}
var createXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector,djId) {
    var options = {
        url: '${bdcdjUrl}/bdcSjgl/matchData',
        type: 'post',
        dataType: 'json',
        data: {gdid: gdid, bdcdyh: bdcdyh, fwid: fwid, bdclx: $bdclx, tdid: tdid, ppzt: '3', dyid: dyid, ygid: ygid, cfid: cfid, yyid: yyid, gdproid: gdproid,djId:djId},
        success: function (matchData) {
            //                                        refreshStore();
//            if ($("#fw").hasClass("active"))
//                changePpzt("3", $(grid_selector), gdid);
//            else
                changeQtPpzt("3", $(grid_selector), gdid);
            $.ajax({
                url: '${bdcdjUrl}/bdcSjgl/creatCsdj?lx=' + $bdclx+"&gdFwWay=cg",
                type: 'post',
                dataType: 'json',
                data: $("#form").serialize(),
                success: function (data) {
                    if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                        setTimeout($.unblockUI, 10);
                        openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid, '_task');
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
                        url: '${bdcdjUrl}/bdcSjgl/matchData',
                        type: 'post',
                        dataType: 'json',
                        data: {gdid: gdid, bdcdyh: bdcdyh, fwid: fwid, bdclx: $bdclx, tdid: tdid, ppzt: '2', dyid: dyid, ygid: ygid, cfid: cfid, yyid: yyid, gdproid: gdproid},
                        success: function (data) {
//                            if ($("#fw").hasClass("active"))
//                                changePpzt("2", $(grid_selector), gdid);
//                            else
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
var createMulXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, dyid, ygid, cfid, yyid, ppzt, gdid, grid_selector) {
    if ($("#fw").hasClass("active") && ppzt == 3) {
        tipInfo("该证书正在发证，不能再次发证！");
        return false;
    } else if ($("#fw").hasClass("active") && ppzt == 4) {
        tipInfo("该证书已经发证，不能再次发证！");
        return false;
    }
    $.blockUI({ message: "请稍等……" });
    var options = {
        url: '${bdcdjUrl}/bdcSjgl/updateGdPpzt',
        type: 'post',
        dataType: 'json',
        data: {gdid: gdid, bdcdyh: bdcdyh, fwid: fwid, bdclx: $bdclx, tdid: tdid, ppzt: '3', dyid: dyid, ygid: ygid, cfid: cfid, yyid: yyid, gdproid: gdproid},
        success: function (matchData) {
            //                                        refreshStore();
//            if ($("#fw").hasClass("active"))
//                changePpzt("3", $(grid_selector), gdid);
//            else
                changeQtPpzt("3", $(grid_selector), gdid);
            $.ajax({
                url: '${bdcdjUrl}/bdcSjgl/creatCsdj?lx=' + $bdclx+"&gdFwWay=cg",
                type: 'post',
                dataType: 'json',
                data: $("#form").serialize(),
                success: function (data) {
                    if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                        setTimeout($.unblockUI, 10);
                        openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid, '_task');
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
                        url: '${bdcdjUrl}/bdcSjgl/updateGdPpzt',
                        type: 'post',
                        dataType: 'json',
                        data: {gdid: gdid, bdcdyh: bdcdyh, fwid: fwid, bdclx: $bdclx, tdid: tdid, ppzt: '2', dyid: dyid, ygid: ygid, cfid: cfid, yyid: yyid, gdproid: gdproid},
                        success: function (data) {
//                            if ($("#fw").hasClass("active"))
//                                changePpzt("2", $(grid_selector), gdid);
//                            else
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
    var url = "${bdcdjUrl!}/bdcSjgl/openMulGdFwPic?gdproid=" + gdproid + "&sqlxdm=" + sqlx + "&readOnly=" + readOnly;
    openWin(url, "房屋匹配");
}
//通过房产证号级联不动产单元
function selDyhByFw(bdcdyh, bdcdyhs) {
    var index = 0;
    var Url = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
    if (bdcdyhs != null && bdcdyhs != "")
        bdcdyhs = bdcdyhs.join(",");
    var data = {hhSearch: '', bdcdyh: bdcdyh, bdcdyhs: bdcdyhs, bdclx: $bdclx};
    var jqgrid = $("#dyh-grid-table");
    var grid_selector = "#dyh-grid-table";

    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {name: 'XL', index: 'XL', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
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
            {name: 'BDCDYH', index: 'BDCDYH', width: '22%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
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

                addYdjhForTable(grid_selector);

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
    var Url = "${bdcdjUrl}/bdcSjgl/getGdXmFwJsonByPage";
    var data = {dah: dah};
    var jqgrid = $("#fw-grid-table");
    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                index++;
                if (index == 1) {
                    $("#xmmc").val(rowObject.RF1DWMC);
                    $("#fwid").val(rowObject.FWID);
                    $("#dah").val(rowObject.DAH);
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" checked="true" onclick="fwSel(\'' + rowObject.FCZH + '\',\'' + rowObject.FWID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.FWID + '\',\'' + rowObject.DYID + '\',\'' + rowObject.YGID + '\',\'' + rowObject.CFID + '\',\'' + rowObject.YYID + '\',\'' + rowObject.BDCID + '\')"/>';
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
        $("#iframeSrcUrl").val("${bdcdjUrl}/bdzDyMap?bdcdyh=" + bdcdyh + "&bdclxdm=" + $bdclx);
        if ($("#ace-settings-box").hasClass("open")) {
            $("#iframe").attr("src", "${bdcdjUrl}/bdzDyMap?bdcdyh=" + bdcdyh + "&bdclxdm=" + $bdclx);
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
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, colModel: colModel, loadComplete: loadComplete});
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
        colNames:['序列', '权利人','证书类型', "房产证号", '坐落','匹配状态','权利状态','QLZT','PROID','QLID'],
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
//            var jqData = $(grid_selector).jqGrid("getRowData");
//            $.each(jqData, function (index, data) {
//                getQtPpzt(data.PPZT, $(grid_selector), data.PROID);
//            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//为表格添加地籍号列数据
function addDjhForTable(grid_selector){
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getDjhByYdjh(data.DJH, $(grid_selector), rowIds[index]);
    })
}

//为表格添加原地籍号列数据
function addYdjhForTable(grid_selector){
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getYdjhBydjh(data.DJH, $(grid_selector), rowIds[index]);
    })
}
//为表格添加权利人列数据
function qlrForTable(grid_selector){
//    var jqData = $(grid_selector).jqGrid("getRowData");
//    var rowIds = $(grid_selector).jqGrid('getDataIDs');
//    $.each(jqData, function (index, data) {
//        getQlrByDjid(data.ID, $(grid_selector), rowIds[index]);
//    })
}

function qlrForFwTable(grid_selector){
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getQtPpzt(data.PPZT, $(grid_selector), data.PROID);
        getfwQlrByQlid(data.QLID, $(grid_selector), rowIds[index]);
        setQlzt($(grid_selector),data.QLZT,data.PROID);
    })
}


//获取原地籍号
function getYdjhBydjh(djh, table, rowid) {
    if (djh == null || djh == "undefined")
        djh = "";
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getYdjhBydjh?djh=" + djh,
        success: function (result) {
            var ydjh = result.ydjh;
            if (ydjh == null || ydjh == "undefined")
                ydjh = "";
            var cellVal = "";
            cellVal += '<span>'+ydjh+'</span>';
            table.setCell(rowid, "YDJH", cellVal);
        }
    });
}
//获取权利人
function getQlrByDjid(djid, table, rowid) {
    <#--if (djid == null || djid == "undefined")-->
        <#--djid = "";-->
    <#--$.ajax({-->
        <#--type: "GET",-->
        <#--url: "${bdcdjUrl}/bdcSjgl/getQlrByDjid?djid=" + djid,-->
        <#--success: function (result) {-->
            <#--var qlr = result.qlr;-->
            <#--if (qlr == null || qlr == "undefined")-->
                <#--qlr = "";-->
            <#--var cellVal = "";-->
            <#--cellVal += '<span>'+qlr+'</span>';-->
            <#--table.setCell(rowid, "QLR", cellVal);-->
        <#--}-->
    <#--});-->
}
//获取过渡房屋权利人
function getfwQlrByQlid(djid, table, rowid) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getFwQlrByQlid?djid=" + djid,
        success: function (result) {
            var qlr = result.qlr;
            if (qlr == null || qlr == "undefined")
                qlr = "";
            var cellVal = "";
            cellVal += '<span>'+qlr+'</span>';
            table.setCell(rowid, "RF1DWMC", cellVal);
        }
    });
}
//获取地籍号
function getDjhByYdjh(ydjh, table, rowid) {
    if (ydjh == null || ydjh == "undefined")
        ydjh = "";
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getDjhByYdjh?ydjh=" + ydjh,
        dataType: "json",
        success: function (result) {
            var djh =   result.djh;
            if (djh == null || djh == "undefined")
                djh = "";
            var cellVal = "";
            cellVal += '<span>'+djh+'</span>';
            table.setCell(rowid, "XDJH", cellVal);
        }
    });
}

//获取 抵押 查封 预告 状态
function getDyYgCfStatus(bdcid, table, rowid, dyid) {
    if (dyid == null || dyid == "undefined")
        dyid = "";
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getDyYgCfStatus?bdcid=" + bdcid + "&dyid=" + dyid,
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
//获取匹配状态
function getPpzt(ppzt, table, rowid) {
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
//获取土地林权匹配状态
function getQtPpzt(ppzt, table, rowid) {
    if (ppzt == "1" || ppzt == "2" || ppzt == "3" || ppzt == "4"){
        ppzt = '<span class="label label-success">已匹配</span>'; }
    else{
        ppzt = '<span class="label label-danger">待匹配</span>';  }
    table.setCell(rowid, "PPZT", ppzt);
}
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
        url: "${bdcdjUrl}/bdcSjgl/getGdTdJson?filterFwPpzt=${filterFwPpzt!}",
        datatype: "json",
        height: $pageHight,
        jsonReader: {id: 'TDID'},
        colNames: ['序列', '原地籍号','地籍号', '坐落', "土地证号", '匹配状态', 'ID', 'DYID'],
        colModel: [
            {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl"  onclick="tdSel(\'' + rowObject.DJH + '\',\'' + rowObject.TDID + '\',\'' + rowObject.ZL + '\',\'' + rowObject.PPZT + '\',\'' + rowObject.DYID + '\')"/>'
            }
            },
            {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
            {name: 'XDJH', index: 'XDJH', width: '15%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '35%', sortable: false},
            {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
            {name: 'PPZT', index: '', width: '15%', sortable: false},
            {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true} ,
            {name: 'DYID', index: 'DYID', width: '0%', sortable: false, hidden: true}
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

            addDjhForTable(grid_selector);

            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == gridRowNum) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
//                getDyYgCfStatus(data.TDID,$(grid_selector),data.TDID,'') ;
                getQtPpzt(data.PPZT, $(grid_selector), data.TDID);
            })
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
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
            {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
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
            {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
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
        colNames: ['序列', '原地籍号', '坐落', '权利人','地籍号', "不动产单元号", '不动产类型',  'ID'],
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

//根据key获取是否加载数据

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
            {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '"/>';
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
                getDyYgCfStatus(data.TDID, $(grid_selector), data.TDID, '')
            })

            $(table).jqGrid("setGridWidth", parent_column.width());
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//通过房产证号级联不动产单元
function selFwTdByFw(tdid, tdids) {
    var index = 0;
    var Url = "${bdcdjUrl}/bdcSjgl/getGdTdJson?fwtdz=true";
    if (tdids != null && tdids != "")
        tdids = tdids.join(",");
    var data = {hhSearch: '', tdid: tdid, tdids: tdids,fwtdz:'true'};
    var jqgrid = $("#fwTd-grid-table");
    var pager_selector = "#fwTd-grid-pager";
    var parent_column = $(jqgrid).closest('[class*="col-"]');

    jqgrid.setGridParam({
        url: Url,
        datatype: 'json',
        page: 1,
        postData: data,
        colModel: [
            {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                index++;
                if (tdids != null && tdids != "" && tdids.indexOf(rowObject.TDID) > -1) {
                    $("#tdid").val("");
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '"/>';
                } else if (index == 1) {
                    $("#tdid").val(rowObject.TDID);
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '"/>';
                } else {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '"/>';
                }
            }
            },
            {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '35%', sortable: false},

            {name: 'STATUS', index: '', width: '25%', sortable: false},
            {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
        ],
        loadComplete: function () {
            var table = this;
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
                getDyYgCfStatus(data.TDID, $(jqgrid), data.TDID, '')
            })
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
        ppzt = '<span class="label label-success">已匹配</span>';
    else
        ppzt = '<span class="label label-danger">待匹配</span>';
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
    var wfids = "${wfids!}";
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
        var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
        tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch, bdcdyh: '', bdcdyhs: '', bdclx: $bdclx}, dyhColModel, '');
    })
    $("#fwTd_search").click(function () {
        resetFwtdids();
        var hhSearch = $("#fwTd_search_qlr").val();
        var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson?fwtdz=true";
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
function resetBdcdyhs() {
    $("input[name='dyhXl']").attr("checked", false);
    $("#bdcdyhs").val('');
}
function getFwtdids() {
    var chk_value = [];
    $('input[name="fwtdXl"]:checked').each(function () {
        chk_value.push($(this).val());
    });
    return chk_value;
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
//选择多个房屋后关闭窗口更新房屋和单元号选择
var updateGdFwAndDyhSel = function (djlx) {
    var gdproid = $("#gdproid").val();
    var qlrmc = $("#xmmc").val();
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/getGdfwPpzt?gdproid=" + gdproid ,
        dataType: "json",
        success: function (result) {
            getPpzt(result, $("#fw-grid-table"), gdproid);
        },
        error: function (data) {
        }
    });
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByGdProid?gdproid=" + gdproid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);

                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
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
                    var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {hhSearch: qlrmc, bdcdyh: '', bdclx: $bdclx}, dyhColModel, dyhLoadComplete);
                }

            }
        },
        error: function (data) {
        }
    });

    var sqlx = $("#sqlxSelect").val();
    var wfids = "${wfids!}";
    if ("${matchTdzh!}" == "true" && (sqlx == null || sqlx == "" || wfids != null && wfids != "" && wfids.indexOf(sqlx) < 0)) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByGdProid?gdproid=" + gdproid + "&bdclx=" + $bdclx,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#fwTd_search_qlr").next().hide();
                    $("#fwTd_search_qlr").val(qlrmc);
                    $("#tdzh").val('');

                    $("#tdid").val('');
                    var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
                    tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                } else {
                    //清空查询内容
                    $("#fwTd_search_qlr").val("");
                    $("#fwTd_search_qlr").next().show();
                    var fwtdis = [];
                    $.each(result, function (index, data) {
                        if(data.tdid!=null && data.tdid!="" && data.tdid!="undefined")
                        fwtdis.push(data.tdid);
                    });
                    if (fwtdis != null && fwtdis != "" && fwtdis.length > 1) {

                        selFwTdByFw("", fwtdis);
                        $("#fwtdis").val(fwtdis);
                        $("#tdzh").val('');
                        $("#tdid").val('');
                    } else if (fwtdis != null && fwtdis != "" && fwtdis.length == 1) {
                        selFwTdByFw(fwtdis.join(""), "");
                        $("#fwtdis").val("");
                        $("#tdid").val(fwtdis.join(""));
                    } else {
                        $("#fwTd_search_qlr").next().hide();
                        $("#fwTd_search_qlr").val(qlrmc);
                        $("#tdid").val("");
                        var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
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
</script>
<div class="main-container">
<div class="space-8"></div>
<div class="page-content" id="mainContent">
<#--图形-->
<div class="ace-settings-container" id="ace-settings-container" style="display: none;">
    <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
        <i class="ace-icon fa fa-globe blue bigger-200"></i>
    </div>

    <div class="ace-settings-box clearfix " id="ace-settings-box">
        <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
    </div>
    <!-- /.ace-settings-box -->
</div>
<div class="row">
    <div class="col-xs-2" style="min-width: 230px">
        <div class="profile-user-info profile-user-info-striped">
            <div class="profile-info-name"> 登记类型</div>

            <div class="profile-info-value">
                <select id="djlxSelect" class="chosen-select" style="width:100px">
                    <#list djlxList as djlx>
                        <option value="${djlx.DM!}">${djlx.MC!}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="col-xs-3" style="min-width:370px">
        <div class="profile-user-info profile-user-info-striped">
            <div class="profile-info-name"> 申请类型</div>

            <div class="profile-info-value">
                <select id="sqlxSelect" class="chosen-select" style="width:250px" onchange="changeSqlx()">
                    <#list sqlxList as sqlx>
                        <option value="${sqlx.DM!}">${sqlx.MC!}</option>
                    </#list>
                </select>
            </div>
        </div>
    </div>
    <div class="col-xs-3">
        <button type="button" class="btn btn-sm btn-primary" id="save">创建</button>
        <button type="button" class="btn btn-sm btn-primary" id="ylzs" style="display: none;">预览证书</button>
        <#if "${showZjfzBtn!}"=='true'>
            <button type="button" class="btn btn-sm btn-primary" id="fz">缮证</button>
        </#if>
        <button type="button" class="btn btn-sm btn-primary" id="match">匹配</button>
    </div>
</div>
<div class="space-4"></div>
<div class="row">
<div class="col-xs-6">
    <div class="tabbable">
        <ul class="nav nav-tabs">
            <#list gdTabOrderList as gdTabOrder>
                <#if gdTabOrder=='td'>
                    <li>
                        <a data-toggle="tab" id="tdTab" href="#td">
                            纯土地证
                        </a>
                    </li>
                <#elseif   gdTabOrder=='lq'>
                    <li>
                        <a data-toggle="tab" id="lqTab" href="#lq">
                            林权证
                        </a>
                    </li>
                <#elseif   gdTabOrder=='cq'>
                    <li>
                        <a data-toggle="tab" id="cqTab" href="#cq">
                            草原证
                        </a>
                    </li>
                <#else>
                    <li>
                        <a data-toggle="tab" id="fwTab" href="#fw">
                            房产证
                        </a>
                    </li>

                </#if>

            </#list>

        </ul>
        <div class="tab-content">
            <div id="fw" class="tab-pane ">
                <div class="simpleSearch">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                       data-watermark="请输入权利人/坐落">
                            </td>
                            <td class="Search">
                                <a href="#" id="fw_search">
                                    搜索
                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
                <#if "${editFlag!}"=="true">
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="gdFwAdd">
                                    <i class="ace-icon fa fa-file-o"></i>
                                    <span>新增</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdFwUpdate">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>修改</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdFwsAdd">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>批量导入</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdFwZx">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>注销</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdFwJcZx">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>解除注销</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                </#if>
                <table id="fw-grid-table"></table>
                <div id="fw-grid-pager"></div>
            </div>
        <#--林权-->
            <div id="lq" class="tab-pane">
                <div class="simpleSearch">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <input type="text" class="SSinput watermarkText" id="lq_search_qlr"
                                       data-watermark="请输入权利人/坐落/林权证号">
                            </td>
                            <td class="Search">
                                <a href="#" id="lq_search">
                                    搜索
                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
                <#if "${editFlag!}"=="true">
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="gdLqAdd">
                                    <i class="ace-icon fa fa-file-o"></i>
                                    <span>新增</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdLqUpdate">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>修改</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                </#if>
                <table id="lq-grid-table"></table>
                <div id="lq-grid-pager"></div>
            </div>
        <#--草原-->
            <div id="cq" class="tab-pane">
                <div class="simpleSearch">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <input type="text" class="SSinput watermarkText" id="cq_search_qlr"
                                       data-watermark="请输入权利人/坐落/草原证号">
                            </td>
                            <td class="Search">
                                <a href="#" id="cq_search">
                                    搜索
                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
                <#if "${editFlag!}"=="true">
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="gdCqAdd">
                                    <i class="ace-icon fa fa-file-o"></i>
                                    <span>新增</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdCqUpdate">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>修改</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                </#if>
                <table id="cq-grid-table"></table>
                <div id="cq-grid-pager"></div>
            </div>
        <#--土地-->
            <div id="td" class="tab-pane">
                <div class="simpleSearch">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <input type="text" class="SSinput watermarkText" id="td_search_qlr"
                                       data-watermark="请输入坐落/土地证号/地籍号">
                            </td>
                            <td class="Search">
                                <a href="#" id="td_search">
                                    搜索
                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
                <#if "${editFlag!}"=="true">
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="gdTdAdd">
                                    <i class="ace-icon fa fa-file-o"></i>
                                    <span>新增</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdTdUpdate">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>修改</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdTdsAdd">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>批量导入</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                </#if>
                <table id="td-grid-table"></table>
                <div id="td-grid-pager"></div>
            </div>
        </div>
    </div>
</div>
<div class="col-xs-6">
    <div class="tabbable">
        <ul class="nav nav-tabs" id="myTab">
            <li class="active">
                <a data-toggle="tab" id="dyhTab" href="#file">
                    不动产单元
                </a>
            </li>
            <li>
                <a data-toggle="tab" id="fwTdTab" href="#fwTd" style="display:none;">
                    房屋土地证
                </a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="file" class="tab-pane in active ">
                <div class="simpleSearch">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <input type="text" class="SSinput watermarkText" id="dyh_search_qlr"
                                       data-watermark="请输入权利人/坐落/不动产单元号">
                            </td>
                            <td class="Search">
                                <a href="#" id="dyh_search">
                                    搜索
                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
                <#if "${editFlag!}"=="true">
                    <div class="tableHeader">
                        <ul>

                        </ul>
                    </div>
                </#if>
                <table id="dyh-grid-table"></table>
                <div id="dyh-grid-pager"></div>
            </div>

            <div id="fwTd" class="tab-pane">
                <div class="simpleSearch">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <input type="text" class="SSinput watermarkText" id="fwTd_search_qlr"
                                       data-watermark="请输入坐落/土地证号">
                            </td>
                            <td class="Search">
                                <a href="#" id="fwTd_search">
                                    搜索
                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                </a>
                            </td>
                        </tr>
                    </table>
                </div>
                <#--<#if "${editFlag!}"=="true">
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="fwTdAdd">
                                    <i class="ace-icon fa fa-file-o"></i>
                                    <span>新增</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="fwTdUpdate">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                    <span>修改</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                </#if>-->
                <table id="fwTd-grid-table" style="width: 800px"></table>
                <div id="fwTd-grid-pager" style="width: 800px"></div>
            </div>
        </div>
    </div>
</div>
</div>
</div>
</div>
<#--文件选择框-->
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle">选择文件（请确认文件为.xls格式）</h4>
                <button type="button" id="fileHide" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <td><input type="file" id="fileWindow" name="file" style="width: 280px;height:34px"/></td>
                                        <input type="hidden" name="sjlx" id="sjlx">
                                        <td><input type="submit" id="fileSub" class="filesub" value="导入"/></td>
                                        <td><a href="" class="filesub" id="fileDownLoad">模板下载</a></td>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        <#--</div>-->
        </div>
    </div>
</div>
<form id="form" hidden="hidden">
    <input type="hidden" id="djlx" name="djlx">
    <input type="hidden" id="fwid" name="fwid">
    <input type="hidden" id="tdid" name="tdid">
    <input type="hidden" id="dah" name="dah">
    <input type="hidden" id="lqid" name="lqid">
    <input type="hidden" id="cqid" name="cqid">
    <input type="hidden" id="djId" name="djId">
    <input type="hidden" id="bdcdyh" name="bdcdyh">
    <input type="hidden" id="workFlowDefId" name="workFlowDefId">
    <input type="hidden" id="sqlx" name="sqlxMc">
    <input type="hidden" id="xmmc" name="xmmc">
    <input type="hidden" id="tdzh" name="tdzh"/>
    <input type="hidden" id="ppzt" name="ppzt"/>
    <input type="hidden" id="dyid" name="dyid"/>
    <input type="hidden" id="ygid" name="ygid"/>
    <input type="hidden" id="cfid" name="cfid"/>
    <input type="hidden" id="yyid" name="yyid"/>
    <input type="hidden" id="gdproid" name="gdproid"/>
    <input type="hidden" id="mulGdfw" name="mulGdfw"/>
    <input type="hidden" id="djIds" name="djIds"/>
    <input type="hidden" id="bdcdyhs" name="bdcdyhs"/>
    <input type="hidden" id="tdids" name="tdids"/>
    <input type="hidden" id="qlid" name="qlid"/>
    <input type="hidden" id="qlzt" name="qlzt"/>
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
