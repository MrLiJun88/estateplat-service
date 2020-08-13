$(function () {
    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
    var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject) {
        if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
            $(window).resize(function () {
                $.each($(".moveModel > .modal-dialog"), function () {
                    $(this).css("left", ($(window).width() - $(this).width()) / 2);
                    $(this).css("top", "40px");
                })
            })
        }
    }
    /*   文字水印  */
    $(".watermarkText").watermark();
    $(document).keydown(function (event) {
        if (event.keyCode == 13) { //绑定回车

        }
    });
    //全局数组
    $selectArray = new Array();
    //table每页行数
    $mulData = new Array();
    $mulRowid = new Array();
    $selectedInput = new Array();
    if (bdcqzh != null) {
        $('#cd_search').val(bdcqzh);
    }
    cdInitTable();

    //搜索事件
    $("#cd_search_btn").click(function () {
        var cdUrl = bdcdjUrl + "/bdcZsCd/getBdcZsCdByPages";
        var cqzh = $('#cqzh').val();
        var bdcdyh = $('#cqbdcdyh').val();
        var exactQuery = "false";
        if ($("#exactQueryBdcqzCd").get(0).checked) {
            exactQuery = "true";
        }
        tableReload("cd-grid-table", cdUrl, {cqzh: cqzh, bdcdyh:bdcdyh, exactQuery:exactQuery});
    });

    $(".cdSearchPop-modal,.newPro-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //默认初始化表格

    //添加数据
    $("#add").click(function () {
        var type = "";
        var bdcqzh = $('#bdcqzh').val();
        if(bdcqzh && bdcqzh!=''){
            type = "bdcqzh";
        }
        add(type);
    });

    //修改数据
    $("#save").click(function () {
        save();
    });


    $("#ywsjTab").click(function () {
        if (this.id == "ywsjTab") {
            $("#ywsj").addClass("active");
            ywsjInitTable();
        }
    });

    $("#ywsjGjSearchBtn").click(function () {
        var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=&dyfs=&qllx=&qlxzdm=&bdclxdm=&proid=&ysqlxdm=" + "&" + $("#ywsjSearchForm").serialize();
        tableReload("ywsj-grid-table", ywsjUrl, {dcxc: ""});
        $("#ywsjSearchPop").hide();
        $("#ywsjSearchForm")[0].reset();
    })

    $("#ywsjHide,#tipHide,#tipCloseBtn").click(function () {
        if (this.id == "ywsjHide") {
            $("#ywsjSearchPop").hide();
            $("#ywsjSearchForm")[0].reset();
        } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
        }
    });
    $(".ywsjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //默认初始化表格，限制总表格
    ywsjInitTable();
    //搜索事件
    //不动产权
    $("#ywsj_search_btn").click(function () {
        var zslx = $("#zslxSelect").val();
        var fzqssj = $("#fzqssj").val();
        var fzjssj = $("#fzjssj").val();
        var dcxc = $("#ywsj_search").val();
        var bdcqzh = $("#bdcqzhgrid").val();
        var qlr = $("#qlrywsj").val();
        var zl = $("#zlywsj").val();
        var bdcdyh = $("#bdcdyhywsj").val();
        var cqzhjc = $("#cqzhjcywsj").val();
        var fwbm = $("#fwbmywsj").val();
        var dyr = $("#dyrywsj").val();
        var exactQuery = "false";
        if ((bdcqzh == "" || bdcqzh == null || bdcqzh == undefined)
            && (qlr == "" || qlr == null || qlr == undefined)
            && (zl == "" || zl == null || zl == undefined)
            && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
            && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)
            && (fwbm == "" || fwbm == null || fwbm == undefined)
            && (dyr == "" || dyr == null || dyr == undefined)) {
            $.Prompt("请输入不动产权证号/产权证号简称/坐落/权利人/不动产单元号/房屋代码", 1500);
        } else {
            var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=&dyfs=&qllx=&bdclxdm=&qlxzdm=&proid=&ysqlxdm=&zstype=" + zslx + "&fzqssj=" + fzqssj + "&fzjssj=" + fzjssj;
            tableReload("ywsj-grid-table", ywsjUrl, {
                bdcqzh: bdcqzh,
                fwbm: fwbm,
                qlr: qlr,
                zl: zl,
                bdcdyh: bdcdyh,
                cqzhjc: cqzhjc,
                dyr: dyr,
                exactQuery: exactQuery
            });
        }
    })


    $(window).on('resize.chosen', function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
    }).trigger('resize.chosen');
    $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "", width: "100%"});
});


function cdInitTable() {
    var grid_selector = "#cd-grid-table";
    var pager_selector = "#cd-grid-pager";

    //绑定回车键
    $('#cd_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#cd_search_btn").click();
        }
    });

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
        jsonReader: {id: 'CDID'},
        colNames: ['序列', '产权证号',  '不动产单元号', '限制文号', '裁定状态', '裁定经办人', '解除裁定经办人', '裁定时间','解除裁定时间','CDID'],
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '3%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '</span>'
                }
            },
            {name: 'CQZH', index: 'CQZH', width: '12%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '17%', sortable: false},
            {name: 'CDYY', index: 'CDYY', width: '15%', sortable: false},
            {name: 'CDZT', index: 'CDZT', width: '5%', sortable: false},
            {name: 'CDJBR', index: 'CDJBR', width: '7%', sortable: false},
            {name: 'JCCDJBR', index: 'JCCDJBR', width: '5%', sortable: false},
            {name: 'CDSJ', index: 'CDSJ', width: '8%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return "";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
            {name: 'JCCDSJ', index: 'JCCDSJ', width: '8%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return "";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
            {name: 'CDID', index: 'PROID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        // multiselect: true,
        rownumbers: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }

            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                debugger;
                var cellVal = "";
                if (data.CDZT ==0) {
                    cellVal = '<span class="label label-success">正常</span>';
                } else if (data.CDZT == 1) {
                    cellVal = '<span class="label label-danger">裁定</span>';
                } else {
                    cellVal = '<span class="label label-success">正常</span>';
                }
                $(grid_selector).setCell(data.CDID, "CDZT", cellVal);
            })

        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}


function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
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

function closeDialog(id) {
    var x = '#' + id;
    $(x).hide();
    clearAddDialog();
}
function showDialog(id) {
    if (id == 'addSel') {
        var chk_value = $('#cd-grid-table').jqGrid('getGridParam', 'selarrrow');
        for (var i = 0; i < chk_value.length; i++) {
            var rowData = $('#cd-grid-table').jqGrid('getRowData', chk_value[i]);
            $('#bdcdyh').val(rowData.BDCDYH);
            $('#bdcqzh').val(rowData.CQZH);
            $('#zl').val(rowData.ZL);
            $('#cdyy').val(rowData.CDYY);
        }
        $("#addCdData").show();
    } else if (id == 'cdShow') {
        $("#cdSearchPop").show();
    }
}


function add(type) {
    $("#addCdData").hide();
    var remoteUrl = bdcdjUrl + "/bdcZsCd/addBdcZsCd";
    var cqzh = "";
    if(type == "bdcqzh"){
        cqzh = $('#bdcqzh').val();
    }
    var bdcdyh = $('#bdcdyh').val();
    var proid = $('#proid').val();
    var cdyy = $('#cdyy').val();
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: {cqzh:cqzh,bdcdyh:bdcdyh,proid:proid,cdyy:cdyy},
        dataType: "json",
        success: function (data) {
            //alert(data);
            $.Prompt(data.message,1500);
            // $('#cdDataForm').reset();
            reload();
        },
        error: function (data) {
            $('#cdDataForm').reset();
            $.Prompt('该产权证号已裁定',1500);
        }
    });
}

function showModal() {
    $('#myModal').show();
}

function closeModal() {
    $('#myModal').hide();
}
function chooseBdcdy(bdcdyh, bdclx, bdcqzh, proid, gdbdcdyh,type) {
    if(type && type != '') {
        if (type == 'bdcqzh') {
            $('#bdcdyh').val(bdcdyh);
            $('#bdcqzh').val(bdcqzh);
            $('#bdclx').val(bdclx);
            $('#proid').val(proid);
        }
    }
    closeModal();
}


function reload() {
    var url = bdcdjUrl + "/bdcZsCd/getBdcZsCdByPages";
    tableReload("cd-grid-table", url, '', '');
    clearAddDialog();
}

function clearAddDialog() {
    $('#bdcdyh').val('');
    $('#bdcqzh').val('');
    $('#zl').val('');
    $('#cdyy').val('');
    $('#bdclx').val('');
}

function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}
//zdd 此处用意是在前台页面加载权利人   提高后台SQL相应相率   但是经过测试未必有慢的情况，并且导致权利人查询出现问题  所以暂时去掉
//为表格添加权利人列数据
function qlrForTable(grid_selector, bdclxdm, zdtzm) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getQlrByDjid(data.ID, $(grid_selector), rowIds[index], bdclxdm, zdtzm);
    })
}
//获取权利人
function getQlrByDjid(djid, table, rowid, bdclxdm, zdtzm) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + bdclxdm + "&zdtzm=" + zdtzm,
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

//业务数据
function ywsjInitTable() {
    var grid_selector = "#ywsj-grid-table";
    var pager_selector = "#ywsj-grid-pager";
    $('#ywsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#bdcqzhgrid').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#cqzhjcywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#zlywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#qlrywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#bdcdyhywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#fwbmywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    //resize to fit page size
    var contentWidth = $(".bootbox").width();
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', contentWidth);
    });
    //resize on sidebar collapse/expand
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column);
        }
    });
    // if ((qllx == "18" && sqlxdm != "1001" && sqlxdm != "1019" && sqlxdm != "9999905" && sqlxdm != "9979902" && sqlxdm != "9999910") || sqlxdm == "9940002")
    //
    // if () {}
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'PROID'},
        colNames: ['不动产单元号', '不动产权证号', '房屋代码', '坐落', '权利人', '不动产类型', '不动产单元状态', '操作', 'PROID', '权利状态', '交易状态', "匹配状态"],
        colModel: [
            {
                name: 'BDCDYH',
                index: '',
                width: '12%',
                sortable: false
            },
            {
                name: 'BDCQZH',
                index: '',
                width: '11%',
                sortable: false
            },
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'ZL', index: '', width: '10%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
            {name: 'BDCLX', index: '', width: '6%', sortable: false, hidden: true},
            {name: 'BDCDYZT', index: 'BDCDYZT', width: '10%', sortable: false, hidden: true},
            {name: 'CZ', index: 'CZ', width: '5%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '6%', sortable: false, hidden: true},
            {name: 'QLZT', index: 'QLZT', width: '5%', sortable: false},
            {name: 'JYZT', index: 'JYZT', width: '10%', sortable: false},
            {name: 'PPZT', index: 'PPZT', width: '5%', sortable: false}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        rownumbers: true,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', contentWidth);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.PROID, $(grid_selector), data.PROID);
                getSdStatus(data.PROID, data.BDCQZH);
                getbdcdyZt(data.PROID, null, data.BDCLX, $(grid_selector), data.PROID);
                getbdcdyJyZt(data.FWBM, $(grid_selector), data.PROID);
                getbdcdyPpZt(data.PROID, $(grid_selector), data.PROID);
            });

            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }

        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}


function getQlrmcAndZl(proid, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getdateByProid?proid=" + proid,
        success: function (result) {
            getCzxx(rowid, table, result.bdcdyh, proid);
            table.setCell(rowid, "FWBM", result.fwbm);
            table.setCell(rowid, "ZL", result.zl);
            table.setCell(rowid, "QLR", result.qlr);
            table.setCell(rowid, "BDCDYH", result.bdcdyh);
            table.setCell(rowid, "BDCLX", result.bdclx);
            var type = "bdcqzh";
            var bdcdyCellVal =  '<a href="javascript:chooseBdcdy(\'' + result.bdcdyh + '\',\'' + result.bdclx + '\',\'' + result.bdcqzh + '\',\'' + proid + '\',\'' + result.zl + '\',\'' + type + '\')" title="' + result.bdcdyh + '" >' + result.bdcdyh + "</a>";
            table.setCell(rowid, "BDCDYH", bdcdyCellVal);
            var bdcqzhCellVal =  '<a href="javascript:chooseBdcdy(\'' + result.bdcdyh + '\',\'' + result.bdclx + '\',\'' + result.bdcqzh + '\',\'' + proid + '\',\'' + result.zl + '\',\'' + type + '\')" title="' + result.bdcqzh + '" >' + result.bdcqzh + "</a>";
            table.setCell(rowid, "BDCQZH", bdcqzhCellVal);
        }
    });
}


function getSdStatus(qlid, cqzh) {
    cqzh = encodeURI(cqzh);
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getXzyy?cqzh=" + cqzh + "&qlid=" + qlid,
        dataType: "json",
        async: false,
        success: function (jsonData) {
            if (jsonData.msg == "false") {
                var xzyy = jsonData.xzyy;
                $("#" + qlid).css("background-color", "#F9F900");
                $("#" + qlid).attr("tooltips", "限制原因：" + jsonData.xzyy);
                $("#" + qlid).hoverTips();
            }
        },
        error: function (data) {
        }
    });
}

function getbdcdyZt(proid, djid, bdclx, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcdyZt?proid=" + proid + "&djid=" + djid + "&bdclx=" + bdclx,
        success: function (result) {
            var cellVal = ""
            if (result.bdcCxBdcdyZt != null) {
                if (result.bdcCxBdcdyZt.xszjgcdycs >= 1) {
                    cellVal += '<span class="label label-default">在建工程抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsygcs >= 1) {
                    cellVal += '<span class="label label-pink">预告</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsydyacs >= 1) {
                    cellVal += '<span class="label label-success">预抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsdyacs >= 1) {
                    cellVal += '<span class="label label-success">抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsycfcs >= 1) {
                    cellVal += '<span class="label label-danger">预查封</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xscfcs >= 1) {
                    cellVal += '<span class="label label-danger">查封</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsyycs >= 1) {
                    cellVal += '<span class="label label-warning">异议</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsdyics >= 1) {
                    cellVal += '<span class="label label-purple">地役</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xssdcs >= 1) {
                    cellVal += '<span class="label label-yellow">锁定</span><span> </span>';
                }
            }
            if (cellVal == "" && djid == null && proid == null) {
                cellVal += '<span class="label label-success">未匹配不动产单元</span><span> </span>';
            } else if (cellVal == "") {
                cellVal += '<span class="label label-success">正常</span><span> </span>';
            }
            table.setCell(rowid, "BDCDYZT", cellVal);
        }
    });

    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcdyQlZt?proid=" + proid + "&djid=" + djid + "&bdclx=" + bdclx,
        success: function (result) {
            var cellVal = ""
            if (result.sfyg == true) {
                cellVal += '<span class="label label-pink">预告</span><span> </span>';
            }
            if (result.sfdy == true) {
                cellVal += '<span class="label label-success">抵押</span><span> </span>';
            }
            if (result.sfcf == true || result.sfyy == true || result.sfsd == true) {
                cellVal += '<span class="label label-danger">限制</span><span> </span>';
            }
            if (result.existXscq == true) {
                cellVal += '<span class="label label-success">确权</span><span> </span>';
            }
            if (cellVal == "") {
                cellVal += '<span class="label label-success">未登记</span><span> </span>';
            }
            table.setCell(rowid, "QLZT", cellVal);
        }
    });
}

function getbdcdyJyZt(fwbm, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcdyJyZt?fwbm=" + fwbm + "&proid=" + rowid,
        success: function (result) {
            var cellVal = "";
            if (result.jyzt == "0") {
                cellVal += '<span class="label label-danger">交易数据缺失</span>';
            }
            if (result.jyzt == "1") {
                cellVal += '<span class="label label-gray">待售</span>';
            }
            if (result.jyzt == "2") {
                cellVal += '<span class="label label-info">已售</span>';
            }
            if (result.jyzt == "3") {
                cellVal += '<span class="label label-success">备案</span>';
            }
            if (result.jyzt == "9") {
                cellVal += '<span class="label label-success">确权</span>';
            }
            table.setCell(rowid, "JYZT", cellVal);
        }
    });
}

function getbdcdyPpZt(proid, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcpic/getPpzt?proid=" + proid,
        success: function (result) {
            var ppzt = '';
                ppzt = '<a href="javascript:showPpjg(\'' + proid + '\')"><span class="label label-success" value=' + ppzt + '>'+result+'</span></a>';

            table.setCell(rowid, "PPZT", ppzt);
        },
        error: function () {
            tipInfo("error");
        }
    });
}