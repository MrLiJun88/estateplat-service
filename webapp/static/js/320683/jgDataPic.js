Array.prototype.remove = function (index) {
    if (index > -1) {
        this.splice(index, 1);
    }
};
if (!Array.prototype.forEach) {
    Array.prototype.forEach = function(callback, thisArg) {
        var T, k;
        if (this == null) {
            throw new TypeError(" this is null or not defined");
        }
        var O = Object(this);
        var len = O.length >>> 0; // Hack to convert O.length to a UInt32
        if ({}.toString.call(callback) != "[object Function]") {
            throw new TypeError(callback + " is not a function");
        }
        if (thisArg) {
            T = thisArg;
        }
        k = 0;
        while (k < len) {
            var kValue;
            if (k in O) {
                kValue = O[k];
                callback.call(T, kValue, k, O);
            }
            k++;
        }
    };
}
//全局数组
$selectArray = new Array();

//table每页行数
$mulDataFw = new Array();
$mulDataTd = new Array();
$mulRowidFw = new Array();
$mulRowidTd = new Array();
$selectedInput=new Array();

//table每页行数
$rownum = 8;
//table 每页高度
$pageHight = '300px';
//全局的不动产类型
$bdclx = 'TDFW';
var djIds = [];
//定义公用的基础colModel
fwColModel = [
    {name: 'XL', index: 'XL', width: '10%', sortable: false},
    {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
    {name: 'ZSLX', index: 'ZSLX', width: '15%', sortable: false},
    {name: 'FCZH', index: 'FCZH', width: '18%', sortable: false},
    {name: 'FWZL', index: 'FWZL', width: '30%', sortable: false},
    {name: 'PPZT', index: '', width: '13%', sortable: false},
    {name: 'STATUS', index: '', width: '18%', sortable: false},
    {name: 'QLZT', index: 'QLZT', width: '0%', sortable: false, hidden: true},
    {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
    {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
    {name: 'ROWNUM', index: 'ROWNUM', width: '0%', sortable: false, hidden: true}
];

tdColModel = [
    {name: 'XL', index: 'XL', width: '10%', sortable: false},
    {name: 'SDF', index: 'SDF', width: '10%', sortable: false},
    {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
    {name: 'ZL', index: 'ZL', width: '25%', sortable: false},
    {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
    {name: 'PPZT', index: '', width: '13%', sortable: false},
    {name: 'STATUS', index: '', width: '13%', sortable: false},
    {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
    {name: 'RF1DWMC', index: 'RF1DWMC', width: '0%', sortable: false, hidden: true},
    {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
    {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true},
    {name: 'TDQLZT', index: 'TDQLZT', width: '0%', sortable: false, hidden: true}
];

dyhColModel = [
    {
        name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>'
    }
    },
    {name: 'YDJH', index: 'YDJH', width: '15%', sortable: false},
    {name: 'TDZL', index: 'TDZL', width: '30%', sortable: false},
    {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
    {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
    {
        name: 'BDCDYH',
        index: 'BDCDYH',
        width: '15%',
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
    changeLineColor("#fw-grid-table");
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
        getSdStatus(data.QLID, data.TDZH, $(grid_selector), data.PROID, "", "TD");
    });
    changeLineColor("#td-grid-table");
    //去掉遮罩
    setTimeout($.unblockUI, 10);
};

fwtdLoadComplete = function () {
    var table = this;
    setTimeout(function () {
        updatePagerIcons(table);
        enableTooltips(table);
    }, 0);
    var grid_selector = "#fwTd-grid-table";
    //如果7条设置宽度为auto,如果少于7条就设置固定高度
    if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
        $(grid_selector).jqGrid("setGridHeight", "auto");
    } else {
        $(grid_selector).jqGrid("setGridHeight", $pageHight);
    }
    var jqData = $(grid_selector).jqGrid("getRowData");
    $.each(jqData, function (index, data) {
        getDyYgCfStatus($(grid_selector), data.TDID,"TD");
    })
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(table).jqGrid("setGridWidth", parent_column.width());
};

$(function () {
    bootbox.setDefaults({
        locale: "zh_CN"
    });

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
    //左边房屋林权草权土地
    $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
        var url;
        //清空查询内容
        $("#dyh_search_qlr").val("");
        $("#dyh_search_qlr").next().show();
        if (this.id == "dyhTab") {
            $("#file").addClass("active");
            var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {
                hhSearch: '',
                //bdcdyh: '',
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
                $("#dyhTab").click();
                var fwUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwJson";
                fwTableInit();
                if (isLoadGrid("fw"))
                    tableReload("fw-grid-table", fwUrl, {
                        hhSearch: '',
                        fczh: '',
                        dah: '',
                        filterFwPpzt: filterFwPpzt,
                        sfsh:'1'
                    }, "", fwLoadComplete);

            } else if (this.id == "lqTab") {
                $bdclx = 'TDSL';
                $("#lq").addClass("active");
                $("#dyhTab").click();
                var lqUrl = bdcdjUrl+"/bdcJgSjgl/getGdLqJson";
                lqTableInit();
                if (isLoadGrid("lq"))
                    tableReload("lq-grid-table", lqUrl, {hhSearch: '', lqzh: ''}, '', '');
            } else if (this.id == "cqTab") {
                $bdclx = 'TDQT';
                $("#cq").addClass("active");
                $("#dyhTab").click();
                var cqUrl = bdcdjUrl+"/bdcJgSjgl/getGdCqJson";
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
                    tableReload("td-grid-table", tdUrl, {hhSearch: '',sfsh:'1'}, '', '');
            }
            getSqlxByDjlxAndBdclx('', '');
        }
        $("#fwTdTab").hide();
        $.ajax({
            type: "GET",
            url: bdcdjUrl+"/bdcJgSjgl/getDjlx",
            dataType: "json",
            success: function (result) {
                //清空
                $("#djlxSelect").html("");
                if (result != null && result != '') {
                    $.each(result, function (index, data) {
                        $("#djlxSelect").append('<option value="' + data.businessId + '" >' + data.businessName + '</option>');
                    })
                }
                var businessId = $("#djlxSelect  option:selected").val();
                getSqlxByDjlxAndBdclx(businessId, "");
                $("#djlxSelect").trigger("chosen:updated");
            },
            error: function (data) {
            }
        });
    })

    //通过受理编号或权利人搜索
    $("#bdcxm_search").click(function () {
        var bdcxmUrl = bdcdjUrl+"/bdcJgSjgl/getBdcXmJson";
        var hhSearch = $("#bdcxm_search_qlr").val();
        bdcxmTableInit();
        tableReload("bdcxm-grid-table1", bdcxmUrl, {hhSearch: hhSearch}, '', '');
    })
    function bdcxmTableInit() {
        var grid_selector = "#bdcxm-grid-table1";
        var pager_selector = "#bdcxm-grid-pager1";
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
                        return '<div style="margin-left:20px;"> <div title="关联项目" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="glxm(\'' + rowObject.PROID + '\',\'' + rowObject.SQLXDM + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div>'
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

    //zwq 回车事件
    $('input').focus(function () {
        var id = $(this).attr('id');
        if (id == 'fw_search_qlr') {
            $('#fw_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#fw_search').click();
                }
            });
        }
        else if (id == 'td_search_qlr') {
            $('#td_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#td_search').click();
                }
            });

        } else if (id == 'lq_search_qlr') {
            $('#lq_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#lq_search').click();
                }
            });
        } else if (id == 'cq_search_qlr') {
            $('#cq_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#cq_search').click();
                }
            });
        }else if(id=="dyh_search_qlr"){
            $("#dyh_search_qlr").keydown(function(event){
                if(event.keyCode==13){
                    $('#dyh_search').click();
                }
            });
        }
    });

    //查询按钮点击事件
    $("#fw_search").click(function () {
        var hhSearch = $("#fw_search_qlr").val();
        var fwUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwJson";
        tableReload("fw-grid-table", fwUrl, {
            hhSearch: hhSearch,
            //fczh: '',
            //dah: '',
            filterFwPpzt: filterFwPpzt,
            sfsh:'1'
        }, "", fwLoadComplete);
    })
    $("#dyh_search").click(function () {
        resetBdcdyhs();
        var hhSearch = $("#dyh_search_qlr").val();
        var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
        //tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch, bdcdyh: '', bdclx: $bdclx}, dyhColModel, '');
        tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch,bdcdyh:'', bdclx: $bdclx}, dyhColModel, '');
    })
    $("#lq_search").click(function () {
        var hhSearch = $("#lq_search_qlr").val();
        var lqUrl = bdcdjUrl+"/bdcJgSjgl/getGdLqJson";
        tableReload("lq-grid-table", lqUrl, {hhSearch: hhSearch, lqzh: ''}, '', '');
    })
    $("#cq_search").click(function () {
        var hhSearch = $("#cq_search_qlr").val();
        var cqUrl = bdcdjUrl+"/bdcJgSjgl/getGdCqJson";
        tableReload("cq-grid-table", cqUrl, {hhSearch: hhSearch, cqzh: ''}, '', '');
    })
    $("#td_search").click(function () {
        var hhSearch = $("#td_search_qlr").val();
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdTdJson";
        tableReload("td-grid-table", tdUrl, {
            hhSearch: hhSearch,
            tdzh: '',
            filterFwPpzt: filterFwPpzt,
            sfsh:'1'
        }, '', tdLoadComplete);
    })
    $("#fwTd_search").click(function () {
        resetFwtdids();
        var hhSearch = $("#fwTd_search_qlr").val();
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
        $bdclx = "TDFW";
        tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: '',qlid:''}, '', '');
    })


    //登记类型变换事件
    $("#djlxSelect").change(function () {
        getSqlxByDjlxAndBdclx('', '');
    })

    $("#addTdSel").click(function () {
        var ids=$('#td-grid-table').jqGrid('getGridParam','selarrrow');
        ids.forEach(function(e){
            var cm = $("#td-grid-table").jqGrid("getRowData",e);
            var index = $.inArray(e, $mulRowidTd);
            if (index < 0) {
                $mulDataTd.push(cm);
                $mulRowidTd.push(e);
            } else if (index >= 0) {
                $mulDataTd.remove(index);
                $mulRowidTd.remove(index);
            }
        })
        $("#gdTdMulXx").html("<span>已选择(" + $mulRowidTd.length + ")</span>");
    });

    //zll 复选框勾选
    $("#gdfwMulXx").click(function () {
        var msg = "";
        if($mulDataFw.length==0){
            msg = "请选择房产证";
            tipInfo(msg);
            return;
        }
        var table="";
        if (this.id == "gdfwMulXx") {
            var qlids='';
            var qlidArr=new Array();
            for(var i=0;i<$mulDataFw.length;i++){
                qlidArr.push($mulDataFw[i].QLID)
            }
            var qlids = qlidArr.join(",");
            var windowWidth = window.screen.width;
//                if(windowWidth<1280){
            url= bdcdjUrl+"/bdcJgSjgl/qurrySelectGdfw?qlids="+qlids;
            showIndexModel(url,"列表详情","1000","650",false);
            return;
//                }
        }
    });

    $("#gdTdMulXx").click(function () {
        var msg = "";
        if($mulDataTd.length==0){
            msg = "请选择土地证";
            tipInfo(msg);
            return;
        }
        if (this.id == "gdTdMulXx") {
            var qlids='';
            var qlidArr=new Array();
            for(var i=0;i<$mulDataTd.length;i++){
                qlidArr.push($mulDataTd[i].QLID)
            }
            var qlids = qlidArr.join(",");
            var windowWidth = window.screen.width;
            url= bdcdjUrl+"/bdcJgSjgl/qurrySelectGdTd?qlids="+qlids;
            showIndexModel(url,"列表详情","1000","650",false);
            return;
        }
    });

    //保存事件
    $("#save").click(function () {
        var bdcqzh = $selectArray.join("、");
        $("#ybdcqzh").val(bdcqzh);
        var qlzt = $("#qlzt").val();
        var tdqlzt = $("#tdqlzt").val();
        if (qlzt == '1') {
            tipInfo("该权利已经注销，无法创建项目！")
            return false;
        }
        if (tdqlzt == 'ZX') {
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
        var qlid = $("#qlid").val();
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
        var sqlxdm = $("#sqlxSelect  option:selected").val();
        // var bppwfids = bppwfids;
        //YC纯土地可以不选择不动产单元做抵押注销、解封、查封
        if (bdcdyhs == null || bdcdyhs == '' && ( bppwfids.indexOf(sqlxdm) < 0) ) {
            tipInfo("请选择不动产单元");
            return false;
        } else if (gdproid == '' && $("#fw").hasClass("active") && $mulDataFw.length==0) {
            tipInfo("请选择房产证");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active") && $mulDataTd.length==0) {
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
            //$("#gdproid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        } else if ($("#cq").hasClass("active")) {
            //$("#gdproid").val("");
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        }
        var sqlxMc =$("#sqlx").val();
        //选择单个房产证或者项目内多幢的情况
        if (($mulDataFw.length==1 && bdcdyhs.length==1) || ($mulDataTd.length==0 && $("#td") && $mulDataFw.length == 0 || ($mulDataFw.length==1 && bdcdyhs.length==0)) || ($mulDataTd.length == 1 && $("#td"))) {
            if(sqlxMc!=null && sqlxMc!="抵押权变更登记（批量）" && sqlxMc!="" && sqlxMc.indexOf("批量")>-1){
                tipInfo("含有批量的申请类型需要选择2个及以上房产登记记录！");
                return false;
            }
            if(mulGdfw=='true'){
                if (bdcdyDjIds != null && bdcdyDjIds != "")
                    $("#djIds").val(bdcdyDjIds.join("$"));
                if (bdcdyhs != null && bdcdyhs != "")
                    $("#bdcdyhs").val(bdcdyhs.join("$"));
                bdcdyh = bdcdyhs[0];
                $("#bdcdyh").val(bdcdyh);
                var djIds = $("#djIds").val();
                $("#djId").val(djIds);
            }
            //存在单个房屋进行登记
            if (fwid == '' && $("#fw").hasClass("active")) {
                tipInfo("该项目没有房屋！");
                return false;
            }
            if (sqlxMc!="抵押权变更登记（批量）" && bdcdyhs.length > 1) {
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
            if (djId == null || djId == "undefined"){
                djId = "";
            }
            if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                if (bdcdyhs == null || bdcdyhs == '') {
                    //纯土地抵押权注销登记和房屋抵押权注销登记去掉“没有匹配不动产单元，是否创建项目”的提示
                    if(sqlxdm == "823F5C662090499F8A7E7177BAC25906"||sqlxdm == "9FDFBDA265C046D885B97C71AB6FB447") {
                        checkXm(gdproids, bdcdyh, '', fwid, tdid, qlid, ppzt, djId, lqid, cqid);
                    }else{
                        var msg = "没有匹配不动产单元，是否创建项目!";
                        showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproids + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + djId + "','" + yyid + "','" + ppzt + "','" + lqid + "','" + cqid + "','" + cqid+ "'", "", "");
                    }
                } else{
                    if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && matchTdzh == "true") {
                        var msg = "没有匹配房屋土地证，是否创建项目！";
                        showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproids + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + djId + "','" + yyid + "','" + ppzt + "','" + lqid + "','" + cqid + "','" + cqid + "'", "", "");
                    } else {
                        checkXm(gdproids, bdcdyh, '', fwid, tdid, qlid, ppzt, djId, lqid, cqid);
                    }
                }
            } else if(bppwfids != null && bppwfids != "" && bppwfids.indexOf(sqlxdm) >= 0){
                if (bdcdyhs == null || bdcdyhs == '') {
                    var msg = "没有匹配不动产单元，是否创建项目!";
                    showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproids + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + djId + "','" + yyid + "','" + ppzt + "','" + lqid + "','" + cqid + "','" + cqid+ "'", "", "");
                } else {
                    checkXm(gdproid, bdcdyh, '', fwid, tdid, qlid, ppzt, djId, lqid, cqid);
                }
            }else {
                checkXm(gdproid, bdcdyh, '', fwid, tdid, qlid, ppzt, djId, lqid, cqid);
            }
        }else {
            //存在多个房屋进行登记
            //项目内多幢
            var gdproids="";
            var qlids = "";
            var gdproidArr = new Array();
            var qlidArr = new Array();
            for (var i = 0; i < $mulDataFw.length; i++) {
                gdproidArr.push($mulDataFw[i].PROID)
                qlidArr.push($mulDataFw[i].QLID);
            };
            for (var i = 0; i < $mulDataTd.length; i++) {
                gdproidArr.push($mulDataTd[i].PROID)
                qlidArr.push($mulDataTd[i].QLID);
            }
            gdproids = gdproidArr.join(",");
            $("#gdproids").val(gdproids);
            qlids = qlidArr.join(",");
            $("#qlids").val(qlids);

            if ($("#fw").hasClass("active")) {
                $("#tdid").val("");
                $("#cqid").val("");
                $("#lqid").val("");
                $("#bdcdyh").val("");
                $("#djId").val("");
                $("#gdproid").val("");
                $("#qlid").val("");
            }
            if ($("#fw").hasClass("active")) {
                //选一条权利，但是匹配不同的不动产单元，属于一证多房的情况,而不是项目内多幢
                if(djlx=='800'|| bppwfids.indexOf(sqlxdm) >= 0){
                    createMulXm(gdproids,qlids);
                }else{
                    checkMulXmFw(gdproids,qlids);
                }
            }else if($("#td").hasClass("active")) {
                createMulXm(gdproids,qlids);
            }
        }
    })

    var checkMulXmFw = function(gdproids,qlids){
        var options = {
            url: bdcdjUrl+'/bdcJgSjgl/checkMulXmFw',
            type: 'post',
            dataType: 'json',
            data: $("#form").serialize(),
            success: function (data) {
                if(data!=null && (data.msg=="" || data.msg==null)){
                    createMulXm(gdproids,qlids);
                }else{
                    tipInfo(data.msg);
                    return false;
                }
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
    }
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
            url: bdcdjUrl+'/bdcJgSjgl/zs',
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
        if (qlzt == '1') {
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
        var mulGdfw = $("#mulGdfw").val();
        var gdproid = $("#gdproid").val();
        var qlid = $("#qlid").val();
        if (gdproid == null || gdproid == "undefined")
            gdproid = "";
        if (mulGdfw == null || mulGdfw == "undefined")
            mulGdfw = "";
        var bdcdyhs = getBdcdyhs();
        var fwtdids = getFwtdids();
        var bdcdyDjIds = getBdcdyDjIds();
        var djlx = $("#djlx").val();
        if (bdcdyhs == null || bdcdyhs == '') {
            tipInfo("请选择不动产单元");
            return false;
        }else if (qlid == '' && $("#fw").hasClass("active") && $mulDataFw.length==0) {
            tipInfo("请选择房产证");
            return false;
        } else if (tdid == '' && $("#td").hasClass("active") && $mulDataTd.length==0) {
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
            $("#fwid").val("");
            $("#lqid").val("");
            $("#cqid").val("");
        } else if ($("#lq").hasClass("active")) {
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        } else if ($("#cq").hasClass("active")) {
            $("#fwid").val("");
            $("#tdid").val("");
            $("#cqid").val("");
        }
        if (mulGdfw != "true") {
            if (fwid == '' && $("#fw").hasClass("active")) {
                tipInfo("该项目没有房屋！");
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
                if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && $("#matchTdzh").val() == "true") {
                    var msg = "没有匹配房屋土地证，是否匹配！";
                    showConfirmDialog("提示信息", msg, "dyhPic", "'" + gdproid + "','" + bdcdyh + "','" + tdzh + "','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "','" + djId + "'", "", + "'" + qlid + "'");
                } else {
                    dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, "", "", "", "", ppzt, djId, qlid);
                }
            } else {
                dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, "", "", "", "", ppzt, djId, qlid);
            }
        } else {
            tipInfo("该项目存在多个房屋，请到多个房屋匹配页面进行匹配！");
        }

    });

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
            tipInfo("请选择房产证");
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

            }else if (gdproid != '' && $("#lq").hasClass("active")) {
                $.ajax({
                    url: bdcdjUrl+'/bdcJgSjgl/getLqPpztByLqid?lqid=' + lqid,
                    datatype: 'GET',
                    success: function (data) {
                        if (data) {
                            showConfirmDialog("提示信息", "是否解除匹配", "disPic", "'" + gdproid + "','" + tdid + "','" + lqid + "','" + cqid + "','" + qlid + "'", "", "");
                        }else{
                            tipInfo("该项目未匹配");
                            return false;
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

    //关联项目
    $("#ppxm").click(function () {
        var gdproid = $("#gdproid").val();
        if (gdproid == '' && $("#fw").hasClass("active") && $mulDataFw.length==0) {
            tipInfo("请选择房产项目");
            return false;
        }
        var bdcxmUrl = bdcdjUrl+"/bdcJgSjgl/getBdcXmJson";
        var hhSearch = '$$$';
        tableReload("bdcxm-grid-table1", bdcxmUrl, {hhSearch: hhSearch}, '', '');
        bdcxmTableInit();
        $("#fileInput1").show();
    });

    //删除关联项目
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

    $("#fileHide1").click(function () {
        $("#fileInput1").hide();
    });
    //定位
    $("#ace-settings-btn").click(function () {
        if ($("#ace-settings-box").hasClass("open")) {
            $("#iframe").attr("src", $("#iframeSrcUrl").val())
        }
    })

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
                        addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=true&bdclx=fw&proid=" + result + "&iscp=true");
                    } else if (bdclxId == "gdLqAdd") {
                        addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write");
                    } else if (bdclxId == "gdTdAdd") {
                        //新增土地权利的时候，不需要生成tdid,在关联土地时，将土地权利信息和土地信息关联起来。+ "&tdid=" + result
                        addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=true&bdclx=td&proid=" + result + "&iscp=true" );
                    } else if (bdclxId == "gdCqAdd") {
                        addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write");
                    }
                }
            }
        });
    })

    $("#fileHide").click(function () {
        $("#fileInput").hide();
    });

    $("#fileSub").click(function () {
        //遮罩
        $.blockUI({message: "请稍等……"});
    });

//修改按钮点击事件
    $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdCqUpdate").click(function () {
        if (this.id == "gdFwUpdate") {
            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=true&bdclx=fw&proid=" + gdproid + "&iscp=true");
        } else if (this.id == "gdLqUpdate") {
            var lqid = $("#lqid").val();
            if (lqid == null || lqid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=" + lqid + "&iscp=true");
        } else if (this.id == "gdTdUpdate") {
            var tdid = "";
            var qlid = "";
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
                qlid = $("#qlid").val();
                if (qlid == null || qlid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
            }
            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate(bdcdjUrl+"/gdXxLr?editFlag=true&bdclx=td&proid=" + gdproid + "&iscp=true&tdid=" + tdid+"&qlid=" + qlid);
        } else if (this.id == "gdCqUpdate") {
            var cqid = $("#cqid").val();
            if (cqid == null || cqid == "") {
                tipInfo("请选择一条要修改的数据!");
                return false;
            }
            addOrUpdate(reportUrl+"/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=" + cqid);
        }
    })

    //注销按钮点击事件
    $("#gdFwZx,#gdTdZx").click(function () {
        var qlid = $("#qlid").val();
        var qlzt = $("#qlzt").val();
        if (qlid == "") {
            tipInfo("请选择一条要注销的数据!");
            return false;
        }
        var url = "";
        var lx = "";
        if (qlzt == '0' || qlzt == "") {
            if (this.id == "gdFwZx") {
                lx = "gdFw";
                zxQl(qlid,lx,qlzt);
            } else if (this.id == "gdTdZx") {
                lx = "gdTd";
                zxQl(qlid,lx,qlzt);
            }
        } else {
            tipInfo("该权利已经注销");
        }
    })

    //高级查询按钮点击事件
    $("#fwgjss").click(function () {
        $("#fwgjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });

    $("#tdgjss").click(function () {
        $("#tdgjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });

    $("#lqgjss").click(function () {
        $("#lqgjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });

    $("#cqgjss").click(function () {
        $("#cqgjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });

    $("#dyhgjss").click(function () {
        $("#dyhgjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });

    $("#fwtdgjss").click(function () {
        $("#fwtdgjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });

    $("#fwgjssHide").click(function () {
        $("#fwgjSearchPop").hide();
        $("#fwgjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    $("#lqgjssHide").click(function () {
        $("#lqgjSearchPop").hide();
        $("#lqgjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    $("#cqgjssHide").click(function () {
        $("#cqgjSearchPop").hide();
        $("#cqgjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    $("#tdgjssHide").click(function () {
        $("#tdgjSearchPop").hide();
        $("#tdgjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    $("#dyhgjssHide").click(function () {
        $("#dyhgjSearchPop").hide();
        $("#dyhgjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    $("#fwtdgjssHide").click(function () {
        $("#fwtdgjSearchPop").hide();
        $("#fwtdgjSearchForm")[0].reset();
        $(".chosen-select").trigger('chosen:updated');
    });

    //项目表高级查询的搜索按钮事件
    $("#fwgjSearchBtn").click(function () {
        var fwUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwJson?" + $("#fwgjSearchForm").serialize();
        tableReload("fw-grid-table", fwUrl,{hhSearch: "",sfsh:"1"}, "", fwLoadComplete);
    });

    $("#tdgjSearchBtn").click(function () {
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdTdJson?" + $("#tdgjSearchForm").serialize();
        tableReload("td-grid-table", tdUrl, {
            hhSearch: "",
            filterFwPpzt: filterFwPpzt,
            sfsh:'1'
        }, "", tdLoadComplete);
    });

    $("#lqgjSearchBtn").click(function () {
        var lqUrl = bdcdjUrl+"/bdcJgSjgl/getGdLqJson?" + $("#lqgjSearchForm").serialize();
        tableReload("lq-grid-table", lqUrl, {
            hhSearch: ""
        }, "", fwLoadComplete);
    });

    $("#cqgjSearchBtn").click(function () {
        var cqUrl = bdcdjUrl+"/bdcJgSjgl/getGdCqJson?" + $("#cqgjSearchForm").serialize();
        tableReload("cq-grid-table", cqUrl, {
            hhSearch: ""
        }, "", fwLoadComplete);
    });

    $("#dyhgjSearchBtn").click(function () {
        var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson?" + $("#dyhgjSearchForm").serialize();
        tableReload("dyh-grid-table", dyhUrl, {
            hhSearch: ""
        }, "", fwLoadComplete);
    });

    $("#fwtdgjSearchBtn").click(function () {
        var fwtdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?" + $("#fwtdgjSearchForm").serialize();
        tableReload("fwtd-grid-table", fwtdUrl, {
            hhSearch: ""
        }, "", fwLoadComplete);
    });

    function zxQl(qlid,lx,qlzt){
        $.blockUI({message: "请稍等……"});
        $.ajax({
            type: "GET",
            url: bdcdjUrl+'/bdcJgSjgl/checkMulCf?qlid=' + qlid,
            dataType: "json",
            success: function (jsonData) {
                var msg = false;
                if (jsonData.result=="mulCf") {
                    var tips="房屋";
                    if(lx=="gdTd"){
                        tips="土地";
                    }
                    bootbox.confirm("该"+tips+"存在多条查封记录，本次解封只解当前查封记录，是否解封？",function(result){
                        msg = true;
                    })
                }else{
                    msg = true;
                }
                setTimeout(function () {
                    if(msg){
                        bootbox.confirm("确认注销该权利吗？",function(result){
                            if(result){
                                url = bdcdjUrl+"/bdcJgSjgl/zxQl?qlid=" + qlid + "&lx="+lx;
                                $.ajax({
                                    type: "GET",
                                    url: url,
                                    dataType: "json",
                                    success: function (jsonData) {
                                        if (jsonData.result == "success") {
                                            //刷新
                                            setTimeout($.unblockUI, 10);
                                            if(lx=="gdFw"){
                                                $("#fw_search").click();
                                            }else if(lx=="gdTd"){
                                                $("#td_search").click();
                                            }
                                            $("#qlzt").val(qlzt);
                                        }else{
                                            alert(jsonData.result);
                                        }
                                    },
                                    error: function (data) {
                                    }
                                })
                            }
                        });
                    }
                },2500);
            },
            error: function (data) {
            }
        })
    }
    //解除注销按钮点击事件
    $("#gdFwJcZx,#gdTdJcZx").click(function () {
        var qlid = $("#qlid").val();
        var qlzt = $("#qlzt").val();
        if (qlid == "") {
            tipInfo("请选择一条要解除注销的数据!");
            return false;
        }
        if (qlzt == '1' || qlzt == "") {
            if (this.id == "gdFwJcZx") {
                $.ajax({
                    type: "GET",
                    url: bdcdjUrl+"/bdcJgSjgl/jczxQl?qlid=" + qlid + "&lx=gdFw",
                    dataType: "json",
                    success: function (jsonData) {
                        if (jsonData.result == "success") {
                            //刷新
                            $("#fw_search").click();
                            $("#qlzt").val(qlzt);
                        }
                    },
                    error: function (data) {
                        alert("失败");
                    }
                })
            } else if (this.id == "gdTdJcZx") {
                $.ajax({
                    type: "GET",
                    url: bdcdjUrl+"/bdcJgSjgl/jczxQl?qlid=" + qlid + "&lx=gdTd",
                    dataType: "json",
                    success: function (jsonData) {
                        if (jsonData.result == "success") {
                            $("#td_search").click();
                            $("#qlzt").val(qlzt);
                        }
                    },
                    error: function (data) {

                    }
                })
            }

        } else {
            tipInfo("该权利已经是正常状态");
        }
    })

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
                    alert("该过渡数据已被锁定，请先解锁再进行锁定！");
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
        var table = null;
        var rowNum = "";
        //组织页面上所选记录的相关字段值
        if ($bdclx == "TDFW") {
            cqzh = $("#fw-grid-table").getCell($("#qlid").val(), "FCZH");
            rowNum = $("#fw-grid-table").getCell($("#qlid").val(), "ROWNUM");
            table = $("#fw-grid-table");
        } else if ($bdclx == "TD") {
            cqzh = $("#td-grid-table").getCell($("#qlid").val(), "TDZH");
            rowNum = $("#td-grid-table").getCell($("#qlid").val(), "ROWNUM");
            table = $("#td-grid-table");
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
                    if ($bdclx == "TDFW") {
                        table.setCell(qlid, "XL", rowNum);
                    }
                    if ($bdclx == "TD") {
                        table.setCell(qlid, "SDF", " ");
                    }
                }
            },
            error: function (data) {
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
        var rowNum = "";
        //组织页面上所选记录的相关字段值
        if ($bdclx == "TDFW") {
            var fczh = $("#fw-grid-table").getCell(qlid, "FCZH");
            var proid = $("#fw-grid-table").getCell(qlid, "PROID");
            data_gd = {qlid: qlid, cqzh: fczh, bdclx:$bdclx, proid: proid,xzyy: xzyy_str};
            table = $("#fw-grid-table");
            rowNum = $("#fw-grid-table").getCell(qlid, "ROWNUM");
        }
        if ($bdclx == "TD") {
            var tdzh = $("#td-grid-table").getCell(qlid, "TDZH");
            var proid_td = $("#td-grid-table").getCell(qlid, "PROID");
            data_gd = {qlid: qlid,cqzh:tdzh,bdclx:$bdclx, proid: proid_td, xzyy:xzyy_str};
            table = $("#td-grid-table");
            rowNum = $("#td-grid-table").getCell(qlid, "ROWNUM");
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
                    if ($bdclx == "TDFW") {
                        cellVal =
                            '<div title="'+xzyy_str+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                            '<span style="font-family: cursive;"> ' + rowNum + '</span>' +
                            '<span><img src="../static/img/locked.png" width="20px" height="20px" /></span>' +
                            '</div>';
                        table.setCell(qlid, "XL", cellVal);
                    }
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
            }
        });
        $("#gdXzyyPop").hide();
        $("#gdXzyyForm")[0].reset();
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


    $("#queryFw").click(function () {
        var qlids = $("#qlids").val();
        if (qlids = null || qlids == "" || qlids == "undefined") {
            tipInfo("请选择房屋！");
            return;
        }
        var url = bdcdjUrl+"/bdcJgSjgl/openMulGdFw";
        window.open(url);

    })
    $("#clean").click(function () {
        $("#gdproids").val("");
        $("#qlids").val("");
        $mulDataFw.splice(0,$mulDataFw.length);
        $mulRowidFw.splice(0,$mulRowidFw.length);
        $selectedInput.splice(0,$selectedInput.length);
        $selectArray.splice(0,$selectArray.length);
        $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
        $("#fw-grid-table").trigger("reloadGrid");
        tipInfo("清空成功");
    })

    $("#gdTdclean").click(function () {
        $("#gdproids").val("");
        $("#qlids").val("");
        $mulDataTd.splice(0,$mulDataTd.length);
        $mulRowidTd.splice(0,$mulRowidTd.length);
        $selectedInput.splice(0,$selectedInput.length);
        $selectArray.splice(0,$selectArray.length);
        $("#gdTdMulXx").html("<span>已选择("+$mulRowidTd.length+")</span>");
        $("#td-grid-table").trigger("reloadGrid");
        tipInfo("清空成功");
    })

    $("#splitData").click(function () {
        var qlid = $("#qlid").val();
        $.ajax({
            url: bdcdjUrl + '/splitGdData/checkSplit',
            type: 'post',
            datatype: 'json',
            data: {qlid: qlid},
            success: function (data) {
                if (data.isSplit == "no") {
                    tipInfo(data.msg);
                } else if (data.isSplit == "yes") {
                    var msg = data.msg;
                    showConfirmDialog("提示信息", msg, "splitGdData", "'" + qlid + "'", "", "");
                }
            },
            error: function (data) {

            }
        });
    })

    $("#test").click(function () {
        var qlid='201505280202g18gck48';
        var editFlag='true';
        var url = formUrl+'/qlxxInterfaceForAnalysisController/gdFwsyqxx?qlid=' + qlid+'&editFlag='+editFlag;
        showIndexModel(url,"收件单信息",1000,800,false);
    })

    $("#addFw").click(function () {
        var proids = $("#gdproids").val();
        var qlids = $("#qlids").val();
        if (proids == null || proids == "" || proids == "undefined")
            proids = "";
        if (qlids == null || qlids == "" || qlids == "undefined")
            qlids = "";
        var fwXl = $('input[name="fwXl"]:checked');
        if (fwXl.length == 0) {
            tipInfo("请选择房屋");
            return false;
        }
        $.each(fwXl, function (index, obj) {
            var proid = $(obj).parent().find('input[name="fwxlTemp"]').val();
            var qlid = $(obj).parent().find('input[name="fwQlid"]').val();
            if (proids.indexOf("proid") < 0) {
                if (index == 0 && proids == "") {
                    proids = proid;
                } else if ((proids + ",").indexOf(proid + ",") < 0) {
                    proids = proids + "," + proid;
                }
            }
            if (qlids.indexOf("qlid") < 0) {
                if (index == 0 && qlids == "") {
                    qlids = qlid;
                } else if ((qlids + ",").indexOf(qlid + ",") < 0) {
                    qlids = qlids + "," + qlid;
                }
            }
        })
        if (proids != "") {
            $("#gdproids").val(proids);
            $("#qlids").val(qlids);
            tipInfo("添加成功");
        }
    })
})

function splitGdData(qlid) {
    $.ajax({
        url:bdcdjUrl + '/splitGdData/splitGdData',
        type: 'post',
        datatype: 'json',
        data: {qlid: qlid, bdclx: "TDFW"},
        success: function (data) {
            tipInfo(data);
        },
        error: function (data) {

        }
    });
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
                    $("#fwTd_search").click();
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

var deletexmgl = function (gdproid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/deletexmgl?gdproid=" + gdproid,
        dataType: "json",
        success: function (jsonData) {
            alert(jsonData.result);
            $("#fw_search").click();
        },
        error: function (data) {
        }
    })
}

function glxm(proid,sqlx) {
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
        createGlxm(proid,sqlx);
    } else {
        var msg = "中间库的坐落与收件单的不相符，是否继续创建？";
        showConfirmDialog("提示信息", msg, "createGlxm", "'" + proid + "'", "", "");
    }
}

function yzGlxmxx(proid) {
    //目前只支持房产项目，土地项目以后考虑
    var zl = $("#xmmc").val();
    var pd = false;
    $.ajax({
        url: bdcdjUrl+"/bdcJgSjgl/yzGlxmxx?proid=" + proid + "&zl=" + encodeURI(encodeURI(zl)),
        type: "GET",
        async: false,
        success: function (data) {
            pd = data;
        }
    });
    return pd;
}

function createGlxm(proid,sqlx) {
    $.ajax({
        url: bdcdjUrl+'/bdcJgSjgl/isCancel?bdclx=' + $bdclx + "&sqlx=" + sqlx,
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data.hasOwnProperty("result")) {
                if (data.result) {
                    createGlxmFun(proid);
                } else if (!data.result && data.msg != null && data.msg != "") {
                    setTimeout($.unblockUI, 10);
                    if (data.checkModel == "ALERT")
                        alert(data.msg);
                    else if (data.checkModel == "CONFIRM") {
                        showConfirmDialog("提示信息", data.msg, "createGlxmFun", "'" + gdproid + "'", "", "");
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
var createGlxmFun = function (proid) {
    var bdcqzh = $selectArray.join("、");
    $("#ybdcqzh").val(bdcqzh);
    $.ajax({
        type: "POST",
        url: bdcdjUrl+"/bdcJgSjgl/glxm?proid=" + proid + "&bdclx=" + $bdclx,
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
                alert(data.msg);
            }
        },
        error: function (data) {
        }
    });
}

//设置权利状态
function setQlzt(table, qlzt, rowid) {
    var cellVal = "";
    if (qlzt == '0') {
        cellVal = '<span class="label label-success">正常</span>';
    } else if (qlzt == '1') {
        cellVal = '<span class="label label-gray">注销</span><span> </span>';
    }
    table.setCell(rowid, "STATUS", cellVal);
}

function getSqlxByDjlxAndBdclx(djlx, wfid) {
    if (djlx == null || djlx == "")
        djlx = $("#djlxSelect  option:selected").text();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjglManageBySqlx/getSqlxByDjlx",
        data: {djlx: djlx, bdclx: $bdclx},
        dataType: "json",
        success: function (result) {
            //清空
            $("#sqlxSelect").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    $("#sqlxSelect").append('<option value="' + data.wfId + '" name="' + data.wfId + '">' + data.wfName + '</option>');
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
function getSqlxByDjlxAndBdclxByDjzxdm(businessId, wdid, djzxdm) {
    if (businessId == null || businessId == "")
        businessId = $("#djlxSelect  option:selected").val();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+ "/bdcJgSjgl/getSqlxByBusinessId",
        data: {businessId: businessId},
        dataType: "json",
        success: function (result) {
            //清空
            $("#sqlxSelect").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {

                    if (wdid == data.wfId)
                        $("#sqlxSelect").append('<option value="' + data.wfId + '" name="' + data.wfId + '" selected="selected">' + data.wfName + '</option>');
                    else
                        $("#sqlxSelect").append('<option value="' + data.wfId + '" name="' + data.wfId + '">' + data.wfName + '</option>');
                })
            }
            $("#sqlxSelect").trigger("chosen:updated");
            var wfid = $("#sqlxSelect").val();
            getDjzxByWfidDjzxdm(wdid, djzxdm);

        },
        error: function (data) {
        }
    });
}
function getDjzxByWfidDjzxdm(wfid, djzxdm) {
    if (wfid == null || wfid == "")
        wfid = $("#sqlxSelect  option:selected").text();
    $.ajax({
        type: "GET",
        url: $("#bdcdjUrl").val() + "/bdcJgSjglManageBySqlx/getDjzxByWfid",
        data: {wfid: wfid},
        dataType: "json",
        success: function (result) {
            //清空
            $("#djzxSelect").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    if (djzxdm == data.DJZXDM)
                        $("#djzxSelect").append('<option value="' + data.DJZXDM + '" name="' + data.DJZXDM + '"  selected="selected">' + data.DJZXMC + '</option>');
                    else
                        $("#djzxSelect").append('<option value="' + data.DJZXDM + '" name="' + data.DJZXDM + '">' + data.DJZXMC + '</option>');
                })
            }
            $("#djzxSelect").trigger("chosen:updated");
            var djzx = $("#djzxSelect").val();
            $("#djzx").val(djzx);

        },
        error: function (data) {
        }
    });
}
function getDjzxByWfid(wfid) {
    if (wfid == null || wfid == "")
        wfid = $("#sqlxSelect  option:selected").text();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjglManageBySqlx/getDjzxByWfid",
        data: {wfid: wfid},
        dataType: "json",
        success: function (result) {
            //清空
            $("#djzxSelect").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    $("#djzxSelect").append('<option value="' + data.DJZXDM + '" name="' + data.DJZXDM + '">' + data.DJZXMC + '</option>');
                })
            }
            $("#djzxSelect").trigger("chosen:updated");
            var djzx = $("#djzxSelect").val();
            $("#djzx").val(djzx);

        },
        error: function (data) {
        }
    });
}
function changeDjlx() {
    var djzx = $("#djzxSelect").val();
    $("#djzx").val(djzx);
}
function hideModal(proid) {
    if (proid && proid != undefined && proid != "undefined") {
        openWin(bdcdjUrl+'/bdcJgSjgl/formTab?proid=' + proid);
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
        url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByGdid?gdid=" + cqid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
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
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }
                        } else {
                            if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                selDyhByFw(data.BDCDYH, "");
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
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
        $("#qlid").val(lqid);
        $("#lqid").val(lqid);
        $("#gdproid").val(lqid);
    } else {
        $("#qlid").val("");
        $("#lqid").val("");
        $("#gdproid").val("");
    }
    if (qlrmc && qlrmc != 'undefined') {
        $("#xmmc").val(qlrmc);
    } else{
        qlrmc = "";
    }
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else
        $("#ppzt").val("");
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByGdid?gdid=" + lqid + "&bdclx=" + $bdclx,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
                    bdcdyh:'',
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
//选择土地证号
function tdSel(proid, qlid, tdzh, qlzt,tdid,tdqlzt) {
    //删除的原证号
    if ($selectArray.length == 1) {
        $selectArray.remove(0);
    } else {
        for (var i = 0; i != $selectArray.length; i++) {
            if ($selectArray[0] == tdzh) {
                $selectArray.splice(i, 1);
            }
        }
    }
    $selectArray.push(tdzh);
    var rowData = $("#td-grid-table").jqGrid("getRowData", qlid);
    var ppzt = $(rowData.PPZT).attr("value");
    if (tdid && tdid != 'undefined') {
        $("#tdid").val(tdid);
    }
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
    if (ppzt && ppzt != 'undefined')
        $("#ppzt").val(ppzt);
    else
        $("#ppzt").val("");
    if (qlzt && qlzt != 'undefined')
        $("#qlzt").val(qlzt);
    else
        qlzt = "";
    if (tdqlzt && tdqlzt != 'undefined')
        $("#tdqlzt").val(tdqlzt);
    else
        $("#tdqlzt").val("");
    var qlrmc = $("#td-grid-table").getCell(qlid, "ZL");
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByTdidDjh?qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                //无匹配数据 不刷新
                $("#file").addClass("active");
                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
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
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
                                    bdclx: $bdclx
                                }, dyhColModel, dyhLoadComplete);
                            }
                        } else {

                            if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                selDyhByFw(data.BDCDYH, "");
                            } else {
                                $("#dyh_search_qlr").next().hide();
                                $("#dyh_search_qlr").val(qlrmc);
                                //无匹配数据 不刷新
                                $("#file").addClass("active");
                                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                                tableReload("dyh-grid-table", dyhUrl, {
                                    hhSearch: qlrmc,
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


function fwSel(proid, qlrmc, ppzt, djlx, qlid, qlzt, fczh) {
    //遮罩
    $.blockUI({message: "请稍等……"});
    //赋值
    if (qlrmc && qlrmc != 'undefined')
        $("#xmmc").val(qlrmc);
    else
        qlrmc = "";
    var ppzt = $("#fw-grid-table").getCell(qlid, "PPZT");
    ppzt = getPpztVal(ppzt);
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
    else{
        qlzt = "";
        $("#qlzt").val(qlzt);
    }
    if (proid && proid != 'undefined')
        $("#gdproid").val(proid);
    else
        $("#gdproid").val("");
    if (djlx && djlx == 'undefined')
        djlx = "";
    if (qlrmc && qlrmc != 'undefined')
        $("#qlr").val(qlrmc);
    else
        $("#qlr").val("");
    proid = $("#gdproid").val();
    $("#dyhTab").click();
    $("#tdid").val('');
    resetBdcdyhs();
    resetFwtdids();
    //    判断是否是多个房屋
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/checkGdfwNum?gdproid=" + proid + "&bdclx=" + $bdclx + "&qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            $("#mulGdfw").val(result.mulGdfw);
            var fwzl = $("#fw-grid-table").getCell(qlid, "FWZL");
            $("#fwid").val(result.fwid);
            $("#xmmc").val(fwzl);
            $("#qlr").val(fwzl);
            $.ajax({
                type: "GET",
                url: bdcdjUrl+"/bdcJgSjglManageBySqlx/getGdFcDjlxToSqlxWfid?djzx=" + djlx,
                dataType: "json",
                success: function (sqlxSesult) {
                    //获取过渡房产数据对应的不动产登记类型
                    if (sqlxSesult != null && sqlxSesult != "" && sqlxSesult.businessId != "" && sqlxSesult.businessId != null){
                        $("#djlxSelect").val(sqlxSesult.businessId);
                        $("#djlxSelect option").each(function () {
                            if ($(this).text() == sqlxSesult.businessId) {
                                $(this).attr('selected', 'selected');
                            }
                            $("#djlxSelect").trigger("chosen:updated");
                        });
                        if (sqlxSesult != null && sqlxSesult != "" && sqlxSesult.WFID != "") {
                            $("#workFlowDefId").val(sqlxSesult.WFID);
                            getSqlxByDjlxAndBdclxByDjzxdm(sqlxSesult.businessId, sqlxSesult.WFID, sqlxSesult.DJZXDM);
                        }
                    } else {
                        var busiName = "首次登记";
                        $("#djlxSelect option").each(function () {
                            if ($(this).text() == busiName) {
                                $(this).attr('selected', 'selected');
                            }
                            $("#djlxSelect").trigger("chosen:updated");
                        });
                        getSqlxByDjlxAndBdclx("", "");
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
                        disableDyhTabDiv("file");
                        clearTabDiv();
                        showConfirmDialog("提示信息", msg, "showMulGdFwPic", "'" + proid + "','" + qlid + "','" + readOnly + "'", "updateGdFwAndDyhSel", "'" + djlx + "'");
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                    } else {
                        visableDyhTabDiv("file");
                        $("#fwid").val(result.fwid);
                        qlrmc = fwzl;
                        picDyh(proid, result.fwid, qlrmc, ppzt, djlx, qlid);
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
var picDyh = function (proid, fwid, qlrmc, ppzt, djlx, qlid) {
    //通过fczh获取hs_index
    var bdcdyhs = getBdcdyhs();
    var fwtdids = getFwtdids();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByQlid?proid=" + proid + "&bdclx=" + $bdclx + "&qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);
                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
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
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: qlrmc,
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
        url: bdcdjUrl+"/bdcJgSjglManageBySqlx/getGdFcDjlxToSqlxWfid?djzx=" + djlx,
        dataType: "json",
        success: function (result) {
            //获取过渡房产数据对应的不动产登记类型
            //if (result != null && result != "" && result.busiName != "") {
            //    $("#djlxSelect option").each(function () {
            //        if ($(this).text() == result.busiName) {
            //            $(this).attr('selected', 'selected');
            //        }
            //        $("#djlxSelect").trigger("chosen:updated");
            //    });
            //    getSqlxByDjlxAndBdclx(result.busiName, result.wfid);
            //} else {
            //    var busiName = "首次登记";
            //    $("#djlxSelect option").each(function () {
            //        if ($(this).text() == busiName) {
            //            $(this).attr('selected', 'selected');
            //        }
            //        $("#djlxSelect").trigger("chosen:updated");
            //    });
            //    getSqlxByDjlxAndBdclx(busiName, "");
            //}
            if (matchTdzh == "true" && result != null && (result.wfid == null || result.wfid == "" || wfids != null && wfids != "" && wfids.indexOf(result.wfid) < 0)) {
                $.ajax({
                    type: "GET",
                    url: bdcdjUrl+"/bdcJgSjgl/queryTdByQlid?qlid=" + qlid,
                    dataType: "json",
                    success: function (result) {
                        if (result == '' || result == null) {
                            $("#fwTd_search_qlr").next().hide();
                            $("#fwTd_search_qlr").val(qlrmc);
                            $("#tdzh").val('');
                            $("#tdid").val('');
                            var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                            tableReload("fwTd-grid-table", tdUrl, {
                                hhSearch: qlrmc,
                                tdzh: ''
                            }, '', fwtdLoadComplete);
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
                                selFwTdByFw(result[0].tdid, "");
                                $("#tdids").val("");
                                $("#tdid").val(result[0].tdid);
                            } else if (result[0].TDID != null && result[0].TDID != "" && result[0].TDID != "undefined") {
                                selFwTdByFw(result[0].TDID, "","");
                                $("#tdids").val("");
                                $("#tdid").val(result[0].TDID);
                            }else if (result[0].tdqlid != null && result[0].tdqlid != "" && result[0].tdid != "undefined") {
                                selFwTdByFw("", "",result[0].tdqlid);
                                $("#tdids").val("");
                                $("#tdid").val(result[0].tdqlid);
                            } else {
                                $("#fwTd_search_qlr").next().hide();
                                $("#fwTd_search_qlr").val(qlrmc);
                                var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson";
                                tableReload("fwTd-grid-table", tdUrl, {
                                    hhSearch: qlrmc,
                                    tdzh: ''
                                }, '', fwtdLoadComplete);
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
var checkXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, djId, lqid, cqid) {
    var grid_selector;
    var recordId;
    if ($("#fw").hasClass("active")) {
        gdid = fwid;
        recordId = qlid;
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
            url: bdcdjUrl + "/bdcJgSjgl/checkGdfwNum?gdproid=" + gdproid + "&bdclx=" + $bdclx + "&qlid=" + qlid,
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
        url: bdcdjUrl+'/bdcJgSjgl/isCancel?bdclx=' + $bdclx,
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data.hasOwnProperty("result")) {
                if (data.result) {
                    if(ppDyh)
                        matchData(gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId);
                    createXm(gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId);

                } else if (!data.result && data.msg != null && data.msg != "") {
                    setTimeout($.unblockUI, 10);
                    if (data.checkModel == "ALERT")
                        alert(data.msg);
                    else if (data.checkModel == "CONFIRM") {
                        showConfirmDialog("提示信息", data.msg, "confirmCreateXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + gdid + "','" + grid_selector + "','" + djId + "','" + ppDyh + "'", "", "");
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
var confirmCreateXm=  function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId,ppDyh) {
    if(ppDyh)
        matchData(gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId);
    createXm(gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId);

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
        gdid = fwid;
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
    //if($bdclx=="TD" && bdcdyh!=null && bdcdyh.indexOf("F")>-1)
    //    $bdclx = "TDFW";

    var options = {
        url: bdcdjUrl+'/bdcJgSjgl/matchData',
        type: 'post',
        dataType: 'json',
        data: {
            gdid: gdid,
            bdcdyh: bdcdyh,
            tdzh: tdzh,
            fwid: fwid,
            bdclx: $bdclx,
            tdid: tdid,
            tdqlid: tdid,
            ppzt: '2',
            dyid: dyid,
            ygid: ygid,
            cfid: cfid,
            yyid: yyid,
            gdproid: gdproid,
            djId: djId,
            qlid:qlid
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

var matchDataPic = function(gdid, bdcdyh, tdid, djId, recordId, gdproid,grid_selector ){
    $.ajax({
        url: bdcdjUrl+'/bdcJgSjgl/matchData',
        type: 'POST',
        dataType: 'json',
        data: {gdid: gdid, bdcdyh: bdcdyh,tdqlid:tdid,djId: djId,bdclx: $bdclx,qlid:recordId,ppzt:"2",gdproid:gdproid},
        success: function (data) {
            tipInfo(data.result);
            changeQtPpzt("2", $(grid_selector), recordId);
        },
        error: function (data) {
        }
    });
}

var createXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId) {
    var tdidforTdid = "";
    $('input[name="tdXl"]:checked').each(function () {
        tdidforTdid += $(this).parent().find('input[name="tdidTemp"]').val() + ","
    })
    var cqgs = $("#cqgsSelect").find("option:selected").val();
    $("#cqgs").val(cqgs);
    var options = {
        url: bdcdjUrl+'/bdcJgSjgl/matchData',
        type: 'post',
        dataType: 'json',
        data: {
            gdid: gdid,
            bdcdyh: bdcdyh,
            fwid: fwid,
            bdclx: $bdclx,
            tdid: tdid,
            ppzt: '3',
            gdproid: gdproid,
            djId: djId,
            qlid:qlid
        },
        success: function (matchData) {
            //                                        refreshStore();

            if ($("#fw").hasClass("active"))
                changePpzt("3", $(grid_selector), gdid);
            else if (bdcdyh == '' || bdcdyh == null && bdcdyh == 'undefined')
                changeQtPpzt("0", $(grid_selector), gdid);
            else
                changeQtPpzt("3", $(grid_selector), gdid);
            $.ajax({
                url: bdcdjUrl+'/bdcJgSjglManageBySqlx/createCsdj?bdclx=' + $bdclx,
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
                        url: bdcdjUrl+'/bdcJgSjgl/matchData',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            gdid: gdid,
                            bdcdyh: bdcdyh,
                            fwid: fwid,
                            bdclx: $bdclx,
                            tdid: tdid,
                            ppzt: '2',
                            gdproid: gdproid,
                            qlid:qlid
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
var matchData= function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId) {
    if(bdcdyh==null || bdcdyh==""){
        ppzt="6";
    }else{
        ppzt="3";
    }
    var options = {
        url: bdcdjUrl+'/bdcJgSjgl/matchData',
        type: 'post',
        dataType: 'json',
        async: false,
        data: {
            gdid: gdid,
            bdcdyh: bdcdyh,
            fwid: fwid,
            bdclx: $bdclx,
            tdid: tdid,
            ppzt: ppzt,
            qlid: qlid,
            gdproid: gdproid,
            djId: djId
        },
        success: function (matchData) {
            changeQtPpzt(ppzt, $(grid_selector), qlid);
        },
        error: function (data) { $.ajax({
            url: bdcdjUrl+'/bdcJgSjgl/matchData',
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
                qlid: qlid,
                gdproid: gdproid
            },
            success: function (data) {
                changeQtPpzt("2", $(grid_selector), qlid);
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
var createMulXm = function (gdproids,qlids) {
    var djlx = $("#djlx").val();
    if ($("#fw").hasClass("active")) {
        for (var i = 0; i < $mulDataFw.length; i++) {
            if(djlx!="800"){
                debugger;
                if ($mulDataFw[i].PPZT.indexOf("已部分匹配未发证")>-1 || $mulDataFw[i].PPZT == 0 || $mulDataFw[i].PPZT == '') {
                    tipInfo("所选证书有未匹配，请先匹配！");
                    return false;
                    break;
                }
                if ($mulDataFw[i].PPZT.indexOf("已匹配正在发证")>-1) {
                    tipInfo("所选证书正在发证，不能再次发证！");
                    return false;
                    break;
                }
                if ($mulDataFw[i].PPZT.indexOf("已匹配已发证")>-1) {
                    tipInfo("所选证书已经发证，不能再次发证！");
                    return false;
                    break;
                }
            }
            if ($mulDataFw[i].PPZT.indexOf("已受理")>-1) {
                tipInfo("所选证书已经受理，不能再次受理！");
                return false;
                break;
            }
        }
    }
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl+'/bdcSjgl/updateGdPpzt',
        type: 'post',
        dataType: 'json',
        data: {gdproids:gdproids,ppzt:3,bdclx:$bdclx},
        success: function (mulDataFw) {
            for (var i = 0; i < $mulDataFw.length; i++) {
                if(djlx=800) {
                    changePpzt("6", $("#fw-grid-table"), $mulDataFw[i].QLID);
                }else{
                    changePpzt("3", $("#fw-grid-table"), $mulDataFw[i].QLID);
                }
            }
            $.ajax({
                url: bdcdjUrl+'/bdcJgSjglManageBySqlx/createCsdj?lx=' + $bdclx + "&gdFwWay=cg",
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
                        alert(data.msg);
                    }
                    //清空列表
                    $("#gdproids").val("");
                    $("#qlids").val("");
                    $mulDataFw.splice(0,$mulDataFw.length);
                    $mulDataTd.splice(0,$mulDataTd.length);
                    $mulRowidFw.splice(0,$mulRowidFw.length);
                    $mulRowidTd.splice(0,$mulRowidTd.length);
                    $selectedInput.splice(0,$selectedInput.length);
                    $selectArray.splice(0,$selectArray.length);
                    $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
                    $("#gdTdMulXx").html("<span>已选择("+$mulRowidTd.length+")</span>");
                    $("#fw-grid-table").trigger("reloadGrid");
                    $("#td-grid-table").trigger("relaadGrid");
                },
                error: function (data) {
                    $.ajax({
                        url: bdcdjUrl+'/bdcSjgl/updateGdPpzt',
                        type: 'post',
                        dataType: 'json',
                        data: {gdproids:gdproids,ppzt:2,bdclx:$bdclx},
                        success: function (data) {
                            for (var i = 0; i < $mulDataFw.length; i++) {
                                changePpzt("2", $("#fw-grid-table"), $mulDataFw[i].QLID);
                            }
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
var showMulGdFwPic = function (gdproid,qlid, readOnly) {

    var sqlx = $("#sqlxSelect").val();
    var url = bdcdjUrl+"/bdcSjgl/openMulGdFwPic?gdproid=" + gdproid + "&qlid="+qlid+"&sqlxdm=" + sqlx + "&readOnly=" + readOnly;
    openWin(url, "房屋匹配");
}
//通过房产证号级联不动产单元
function selDyhByFw(bdcdyh, bdcdyhs) {
    var index = 0;
    var Url = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
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
        $("#djIds").val();
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
    } else {
        $("#bdcdyh").val("");
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
    var Url = bdcdjUrl+"/bdcJgSjgl/getGdXmFwJsonByPage";
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
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    } else if (loadComplete == '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel});
    } else if (loadComplete != '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel, loadComplete:loadComplete});
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
        url: bdcdjUrl+"/bdcJgSjgl/getGdFwJson?filterFwPpzt=${filterFwPpzt!}&sfsh=1",
        datatype: "json",
        height: $pageHight,
        jsonReader: {id: 'QLID'},
        colNames: ['序列', '权利人', '证书类型', "房产证号", '坐落', '匹配状态', '权利状态', 'QLZT', 'PROID', 'QLID', 'ROWNUM'],
        colModel: fwColModel,
        viewrecords: true,
        rowNum: $rownum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
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
            for(var i=0;i<=$mulRowidFw.length;i++){
                $(grid_selector).jqGrid('setSelection',$mulRowidFw[i]);
            }
            //赋值数量
            $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
            $.each(jqData, function (index, data) {
                asyncGetGdFwxx(data.PROID, $(grid_selector), data.PROID, data.QLZT, data.QLID);
                getSdStatus(data.QLID, data.FCZH, $(grid_selector), data.PROID, data.ROWNUM, "FW");
            });
            setTimeout(function () {
                changeLineColor("#fw-grid-table");
            }, 0);
        },
        onSelectAll: function(aRowids,status){
            var $myGrid = $(this);
            aRowids.forEach(function(e){
                var cm = $myGrid.jqGrid('getRowData', e);
                var index = $.inArray(e, $mulRowidFw);
                if (status && index < 0) {
                    $mulDataFw.push(cm);
                    $mulRowidFw.push(e);
                } else if (!status && index >= 0) {
                    $mulDataFw.remove(index);
                    $mulRowidFw.remove(index);
                }
            })
            //赋值数量
            $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
        },
        onSelectRow:function(rowid,status){
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData',rowid);
            //判断是已选择界面还是原界面s
            var index=$.inArray(rowid,$mulRowidFw);
            if(status && index<0){
                $mulDataFw.push(cm);
                $mulRowidFw.push(rowid);
                $selectedInput.push(rowid);
            }else if(!status && index>=0){
                $mulDataFw.remove(index);
                $mulRowidFw.remove(index);
                $selectedInput.remove(rowid);
            }
            if (status) {
                fwSel(cm.PROID, cm.FWZL, cm.PPZT, cm.DJLX, cm.QLID, cm.QLZT,cm.FCZH)
            }
            //赋值数量
            $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
        },
        gridComplete:function(){
            $.each($mulDataFw,function(index){
                $selectedInput.push($mulDataFw[index].QLID);
            });
            $.each($selectedInput,function(index){
                $('#'+$selectedInput[index]+'').click();
            });
            $selectedInput.splice(0,$selectedInput.length);
        },
        beforeSelectRow: function (rowid, e) {
            var $myGrid = $(this);
            try {
                var i = $.jgrid.getCellIndex($(e.target).closest('td')[0]);
            }catch (error){
                return;
            }
            var cm = $myGrid.jqGrid('getGridParam', 'colModel');
            return (cm[i].name === 'cb');
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
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
//为表格添加原地籍号列数据
function addYdjhForTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getYdjhBydjh(data.DJH, $(grid_selector), rowIds[index]);
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
function oldDjhForTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getOldDjhByDjh(data.DJH, $(grid_selector), rowIds[index]);
    })
}
function qlrForFwTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        asyncGetGdFwxx(data.PROID, $(grid_selector), data.PROID, data.QLZT, data.QLID);
        getSdStatus(data.QLID, data.FCZH, $(grid_selector), data.PROID, data.ROWNUM, "FW");
    })
}
//获取原地籍号
function getYdjhBydjh(djh, table, rowid) {
    if (djh == null || djh == "undefined")
        djh = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getYdjhBydjh?djh=" + djh,
        success: function (result) {
            var ydjh = result.ydjh;
            if (ydjh == null || ydjh == "undefined")
                ydjh = "";
            var cellVal = "";
            cellVal += '<span>' + ydjh + '</span>';

            table.setCell(rowid, "YDJH", cellVal);
        }
    });
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
//获取过渡房屋权利人
function getfwQlrByQlid(djid, table, rowid) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getFwQlrByQlid?djid=" + djid,
        success: function (result) {
            var qlr = result.qlr;
            if (qlr == null || qlr == "undefined")
                qlr = "";
            var cellVal = "";
            cellVal += '<span>' + qlr + '</span>';
            table.setCell(rowid, "RF1DWMC", cellVal);
        }
    });
}
//获取过渡房屋坐落
function getfwZlByQlid(djid, table, rowid) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getFwZlByQlid?djid=" + djid,
        success: function (result) {
            var fwzl = result.fwzl;
            if (fwzl == null || fwzl == "undefined")
                fwzl = "";
            var cellVal = "";
            cellVal += '<span>' + fwzl + '</span>';
            table.setCell(rowid, "FWZL", cellVal);
        }
    });
}
//获取地籍号
function getDjhByYdjh(ydjh, table, rowid) {
    if (ydjh == null || ydjh == "undefined")
        ydjh = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/getDjhByYdjh?ydjh=" + ydjh,
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
function getDyYgCfStatus(table, tdid,bdclx) {
    if (tdid == null || tdid == 'undefined') {
        tdid = "";
    }
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/asyncGetGdQlztByBdcid?tdid=" + tdid + "&bdclx="+bdclx ,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            if (result.qlzts == null || result.qlzts == "") {
                cellVal = '<span class="label label-success">正常</span>';
            } else {
                var qlzts = result.qlzts.split(",");
                for (var i = 0; i < qlzts.length; i++) {
                    //$("#tdqlzt").val(qlzts[0]);
                    table.setCell(tdid, "TDQLZT", qlzts[0]);
                    if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span><span> </span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span><span> </span>';
                    } else if (qlzts[i] == "ZX") {
                        cellVal += '<span class="label label-danger">注销</span><span> </span>';
                    } else if (qlzts[i] == "DI") {
                        cellVal += '<span class="label label-danger">地役</span><span> </span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }
            table.setCell(tdid, "STATUS", cellVal);
        }
    });
}
//zx 获取房产证抵押 查封 预告 异议 状态
function asyncGetGdFwxx(bdcid, table, rowid, qlzt, qlid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/asyncGetGdFwxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid+"&bdcid="+bdcid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            //正常
            var zls = result.zls;
            if (result.qlzts == null || result.qlzts == "") {
                cellVal += '<span class="label label-success">正常</span>';
                table.setCell(qlid, "QLZT", 0);
                $.ajax({
                    type: "GET",
                    url: bdcdjUrl+"/queryBdcdy/getBdcdyPagesJson?bdclx=" + $bdclx + "&zl=" + zls,
                    dataType: "json",
                    async: false,
                    success: function (result) {
                        if (result.rows != '') {
                            $.ajax({
                                type: "GET",
                                url: bdcdjUrl+"/queryBdcdy/getBdcdyhQlxx?bdcdyh=" + result.rows[0].BDCDYH,
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
                        table.setCell(qlid, "QLZT", 1);
                        break;
                    } else if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        $("#" +qlid).css("background-color", "plum");
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        $("#" + qlid).css("background-color", "red");
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span><span> </span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span><span> </span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }
            table.setCell(qlid, "STATUS", cellVal);
            getQtPpzt(result.ppzt, table, qlid);
        }
    });
}
//zx 获取锁定状态
function getSdStatus(qlid, cqzh, table, proid, index, lx) {
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
                if (lx == "FW") {
                    cellVal = '<div title="'+xzyy+'" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id=""  onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                        '<span style="font-family: cursive;"> ' + index + '</span>' +
                        '<span><img src="../static/img/locked.png" width="20px" height="20px" /></span>' +
                        '</div>';
                    table.setCell(qlid, "XL", cellVal);
                }
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
        type: "POST",
        url: bdcdjUrl+"/bdcJgSjgl/asyncGetGdTdxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid+"&proid="+proid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            //正常
            if (result.qlzts == null || result.qlzts == "") {
                cellVal = '<span class="label label-success">正常</span>';
            } else {
                var qlzts = result.qlzts.split(",");
                for (var i = 0; i < qlzts.length; i++) {
                    table.setCell(qlid, "TDQLZT", qlzts[0]);
                    if (qlzts[i] == "ZX") {
                        cellVal = '<span class="label label-danger">注销</span><span> </span>';
                        break;
                    } else if (qlzts[i] == "DY") {
                        cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        $("#" +qlid).css("background-color", "plum");
                    } else if (qlzts[i] == "CF") {
                        cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        $("#" + qlid).css("background-color", "red");
                    } else if (qlzts[i] == "YG") {
                        cellVal += '<span class="label label-info">预告</span><span> </span>';
                    } else if (qlzts[i] == "YY") {
                        cellVal += '<span class="label label-info">异议</span><span> </span>';
                    } else if (qlzts[i] == "ZX") {
                        cellVal += '<span class="label label-danger">注销</span><span> </span>';
                    } else if (qlzts[i] == "DI") {
                        cellVal += '<span class="label label-danger">地役</span>';
                    } else if (qlzts[i] == "DGQLZT") {
                        cellVal += '<span class="label label-info">多个权利状态</span>';
                    }
                }
            }
            table.setCell(qlid, "STATUS", cellVal);
            getQtPpzt(result.ppzt, table, qlid);
        }
    });
}
//jyl 改变查封抵押数据背景色
function changeLineColor(grid) {
    var ids = $(grid).jqGrid("getDataIDs");
    var rowDatas = $(grid).jqGrid("getRowData");
    for (var i = 0; i < ids.length; i++) {
        if (rowDatas[i].STATUS.indexOf("抵押") > -1) {
            $("#" + ids[i]).css("background-color", "plum");
        }
        if (rowDatas[i].STATUS.indexOf("查封") > -1) {
            $("#" + ids[i]).css("background-color", "red");
        } else {
            $.ajax({
                type: "GET",
                url: bdcdjUrl+"/bdcJgSjgl/checkGdCfByQlid?qlid=" + rowDatas[i].QLID,
                dataType: "json",
                async: false,
                success: function (result) {
                    if (result.cf == 1) {
                        var id = "#" + ids[i];
                        $(id).css("background-color", "red");
                    }
                }
            });
        }
    }
    ;
}


//获取匹配状态
function getPpzt(ppzt, table, rowid) {
    if (ppzt == "1")
        ppzt = '<span class="label label-warning" value='+ppzt+'>部分匹配</span>';
    else if (ppzt == "4" || ppzt == "3" || ppzt == "2")
        ppzt = '<span class="label label-success" value=' + ppzt + '>已匹配</span>';
    else
        ppzt = '<span class="label label-danger" value=' + ppzt + '>待匹配</span>';
    table.setCell(rowid, "PPZT", ppzt);
}

//获取匹配状态
function getQtPpzt(ppzt, table, rowid) {
    if(ppzt=="lq"){
        $.ajax({
            type: "GET",
            url: bdcdjUrl+"/bdcJgSjgl/getLqPpztByLqid?lqid=" + rowid,
            dataType: "json",
            success: function (result) {
                if(result){
                    ppzt = '<span class="label label-success" value="2">已匹配</span>';
                }else{
                    ppzt = '<span class="label label-danger" value="0">待匹配</span>';
                }
                table.setCell(rowid, "PPZT", ppzt);
            },
            error: function (data) {
            }
        });
    }else{
        if (ppzt == "2")
            ppzt = '<span class="label label-success" value="2">已匹配</span>';
        else if (ppzt == "1")
            ppzt = '<span class="label label-warning" value="1">部分匹配</span>';
        else if(ppzt == "6")
            ppzt = '<span class="label label-success" value="6">已受理</span>';
        else
            ppzt = '<span class="label label-danger" value="0">待匹配</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }
}

//获取权利人
function getQlr(table, rowid) {
    $.ajax({
        type: "POST",
        url: bdcdjUrl+"/bdcJgSjgl/asyncGetGdqlrByLqid?lqid=" + rowid,
        dataType: "json",
        success: function (result) {
            var qlr = "";
            if(result.qlr != null || result.qlr != ""){
                qlr=result.qlr;
            }
            table.setCell(rowid, "RF1DWMC", qlr);
        }
    });
}
//获取匹配状态实际值
function getPpztVal(ppzt) {
    if (ppzt != null && ppzt.indexOf("已匹配") > -1)
        ppzt = '2';
    else if (ppzt != null && ppzt.indexOf("部分匹配") > -1)
        ppzt = '1';
    else if(ppzt !=null && ppzt.indexOf("已受理")>-1)
        ppzt = "6";
    else
        ppzt = '0';
    return ppzt;
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
        url: bdcdjUrl+"/bdcJgSjgl/getGdTdJson?filterFwPpzt=${filterFwPpzt!}&sfsh=1",
        datatype: "json",
        height: $pageHight,
        jsonReader: {id: 'QLID'},
        colNames: ['序列', '锁定否', '地籍号', '坐落', "土地证号", '匹配状态', '权利状态', 'QLID', '权利人', 'PROID','TDID','TDQLZT'],
        colModel: tdColModel,
        viewrecords: true,
        rowNum: gridRowNum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
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
                asyncGetGdTdxx($(grid_selector), data.QLID, data.PROID);
                getSdStatus(data.QLID, data.TDZH, $(grid_selector), data.PROID, "", "TD");
            })
            changeLineColor("#td-grid-table");
        },
        onSelectAll:function(aRowids,status) {
            var $myGrid = $(this);
            aRowids.forEach(function(e){
                var cm = $myGrid.jqGrid('getRowData', e);
                var index = $.inArray(e, $mulRowidTd);
                if (status && index < 0) {
                    $mulDataTd.push(cm);
                    $mulRowidTd.push(e);
                } else if (!status && index >= 0) {
                    $mulDataTd.remove(index);
                    $mulRowidTd.remove(index);
                }
            })
            //赋值数量
            $("#gdTdMulXx").html("<span>已选择("+$mulRowidTd.length+")</span>");
        },
        onSelectRow:function(rowid,status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData',rowid);
            var index=$.inArray(rowid,$mulRowidTd);
            if(status && index<0){
                $mulDataTd.push(cm);
                $mulRowidTd.push(rowid);
                $selectedInput.push(rowid);
            }else if(!status && index>=0){
                $mulDataTd.remove(index);
                $mulRowidTd.remove(index);
                $selectedInput.remove(rowid);
            }
            if (status) {
                tdSel(cm.PROID, cm.QLID, cm.TDZH, cm.QLZT, cm.TDID,cm.TDQLZT);
            }
            //赋值数量
            $("#gdTdMulXx").html("<span>已选择("+$mulRowidTd.length+")</span>");
        },

        editurl: "",
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
        colNames: ['序列', '权利人', "草原证号", '坐落', '匹配状态', '权利状态', 'ID'],
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
            {name: 'STATUS', index: '', width: '15%', sortable: false},
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
                getDyYgCfStatus($(grid_selector), data.CQID,$bdclx);
                getQtPpzt(data.PPZT, $(grid_selector), data.CQID);
                getQlr($(grid_selector), data.CQID);
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
        colNames: ['序列', '权利人', "林权证号", '坐落', '匹配状态', '权利状态', 'ID'],
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
            {name: 'STATUS', index: '', width: '15%', sortable: false},
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
                getDyYgCfStatus($(grid_selector), data.LQID,$bdclx);
                var ppzt="lq";
                getQtPpzt(ppzt, $(grid_selector), data.LQID);
                getQlr($(grid_selector), data.LQID);
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
        colNames: ['序列', '原地籍号', '坐落', '权利人', '地籍号', "不动产单元号", '不动产类型','定位', 'ID'],
        colModel: dyhColModel,
        viewrecords: true,
        rowNum: $rownum,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        loadComplete: dyhLoadComplete,
        editurl: "",
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
    var gdTabOrderArray = new Array();
    gdTabOrderArray = gdTabOrder.split(",");
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
            {
                name: 'XL',
                index: '',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
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
                getDyYgCfStatus($(grid_selector), data.TDID,"TD")
            })
            $(table).jqGrid("setGridWidth", parent_column.width());
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

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
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '"/>';
                    } else if (index == 1) {
                        $("#tdid").val(rowObject.TDID);
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '"/>';
                    } else {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '"/>';
                    }
                }
            },
            {name: 'TDZH', index: 'TDZH', width: '30%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '40%', sortable: false},
            {name: 'STATUS', index: '', width: '20%', sortable: false},
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
                getDyYgCfStatus($(jqgrid), data.TDID,"TD");
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
        $("#fwTd_search").click();
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
    else if (ppzt == "6")
        ppzt = '<span class="label label-success">已受理</span>';
    else
        ppzt = '<span class="label label-danger">待匹配未发证</span>';
    table.setCell(rowid, "PPZT", ppzt);
}
//改变匹配状态
function changeQtPpzt(ppzt, table, rowid) {
    $("#ppzt").val(ppzt);
    if (ppzt == "1" || ppzt == "2" || ppzt == "3" || ppzt == "4")
        ppzt = '<span class="label label-success" value=' + ppzt + '>已匹配</span>';
    else if (ppzt == "6")
        ppzt = '<span class="label label-success" value=' + ppzt + '>已受理</span>';
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
    var wfids = "${wfids!}";
    if (wfids != null && wfids != "" && wfids.indexOf(sqlx) > -1) {
        hideFwtdGrid();
        $("#tdid").val();
    } else if ($("#fw").hasClass("active")) {
        showFwtdGrid();
    }
    var wfid = $("#sqlxSelect").val();
    $("#workFlowDefId").val(wfid);
    getDjzxByWfid(wfid);
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
        var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
        tableReload("dyh-grid-table", dyhUrl, {
            hhSearch: hhSearch,
            bdcdyh: '',
            bdcdyhs: '',
            bdclx: $bdclx
        }, dyhColModel, '') ;
    });
    $("#fwTd_search").click(function () {
        resetFwtdids();
        var hhSearch = $("#fwTd_search_qlr").val();
        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
        $bdclx = "TDFW";
        tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: '',qlid:'', tdids: ''}, '', '');
    });
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
    var qlid = $("#qlid").val();
    var qlrmc = $("#xmmc").val();
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcSjgl/getGdfwPpzt?gdproid=" + gdproid,
        dataType: "json",
        success: function (result) {
            getPpzt(result, $("#fw-grid-table"), qlid);
        },
        error: function (data) {
        }
    });
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByQlid?qlid=" + qlid,
        dataType: "json",
        success: function (result) {
            if (result == '' || result == null) {
                $("#dyh_search_qlr").next().hide();
                $("#dyh_search_qlr").val(qlrmc);

                var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: qlrmc,
                    //bdcdyh: '',
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
                    var dyhUrl = bdcdjUrl+"/bdcJgSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: qlrmc,
                        //bdcdyh: '',
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
            url: bdcdjUrl+"/bdcJgSjgl/queryBdcdyhByQlid?qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#fwTd_search_qlr").next().hide();
                    $("#fwTd_search_qlr").val(qlrmc);
                    $("#tdzh").val('');
                    $("#tdid").val('');
                    var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson";
                    tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: '',qlid:''}, '', '');
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
                        var tdUrl = bdcdjUrl+"/bdcJgSjgl/getGdFwTdJson";
                        tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: '',qlid:''}, '', '');
                    }

                }
            },
            error: function (data) {
            }
        });
        $("#fwTdTab").show();
    }


}

function uniqueArray(data) {
    data = data || [];
    var a = {};
    for (var i = 0; i < data.length; i++) {
        var v = data[i];
        if (typeof(a[v]) == 'undefined') {
            a[v] = 1;
        }
    };
    data.length = 0;
    for (var i in a) {
        data[data.length] = i;
    }
    return data;
}