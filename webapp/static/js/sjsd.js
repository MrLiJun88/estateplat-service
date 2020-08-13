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
    $("#sd").addClass("active");
    var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcdySdPagesJson";
    sdInitTable();
    tableReload("sd-grid-table", sdUrl, {
        dcxc: $("#sd_search").val(),
        bdcdyh: $("#bdcdyh").val(),
        fwdcbindex: $("#fwdcbindex").val()
    });

    //搜索事件
    $("#sd_search_btn").click(function () {
        var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcdySdPagesJson";
        tableReload("sd-grid-table", sdUrl, {dcxc: $("#sd_search").val()});
    })

    //单元号高级查询的搜索按钮事件
    $("#sdGjSearchBtn").click(function () {
        var bdcdyh = $('#searchBdcdyh').val();
        var qlr = $('#searchQlr').val();
        var zl = $('#searchZl').val();
        var xzzt = $('#searchXzzt').val();
        var bdclx = $('#searchBdclx').val();
        var sdr = $('#searchSdr').val();
        var jsr = $('#searchJsr').val();
        var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcdySdPagesJson";
        tableReload("sd-grid-table", sdUrl, {
            dcxc: "",
            bdcdyh: bdcdyh,
            qlr: qlr,
            zl: zl,
            xzzt: xzzt,
            bdclx: bdclx,
            jsr: jsr,
            sdr: sdr
        });
    })

    $(".sdSearchPop-modal,.newPro-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //默认初始化表格
    sdInitTable();
    //添加数据
    $("#add").click(function () {
        add();
    });

    //修改数据
    $("#save").click(function () {
        save();
    });

    //单元号高级查询的搜索按钮事件
    $("#djsjGjSearchBtn").click(function () {
        var djsjUrl = bdcdjUrl + "/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc: ""});
        $("#djsjSearchPop").hide();
        $("#djsjSearchForm")[0].reset();
    })

    $("#djsjHide,#ywsjHide,#tipHide,#tipCloseBtn").click(function () {
        if (this.id == "djsjHide") {
            $("#djsjSearchPop").hide();
            $("#djsjSearchForm")[0].reset();
        } else if (this.id == "ywsjHide") {
            $("#ywsjSearchPop").hide();
            $("#ywsjSearchForm")[0].reset();
        } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
        }
    });
    $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //默认初始化表格
    djsjInitTable();
    //搜索事件
    $("#djsj_search_btn").click(function () {
        var djsjUrl = bdcdjUrl + "/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm;
        tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
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

function turnToBdcdy() {
    var url = bdcdjUrl + "/selectBdcdy/";
    window.open(url);
}
function sdInitTable() {
    var grid_selector = "#sd-grid-table";
    var pager_selector = "#sd-grid-pager";

    //绑定回车键
    $('#sd_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#sd_search_btn").click();
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
        jsonReader: {id: 'BDCDYH'},
        colNames: ['序列', '权利人', '不动产单元', '坐落', '限制原因', '限制状态', '锁定人', '解锁人', 'PROID','修改'],
        colModel: [
            {
                name: 'XL',
                index: '',
                width: '5%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '</span>'
                }
            },
            {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '10%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '18%', sortable: false},
            {name: 'XZYY', index: 'XZYY', width: '20%', sortable: false},
            {name: 'XZZT', index: 'XZZT', width: '5%', sortable: false},
            {name: 'SDR', index: 'SDR', width: '10%', sortable: false},
            {name: 'JSR', index: 'JSR', width: '10%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
            {name: 'XG', index: 'XG', width: '10%', sortable: false,formatter: function (cellvalue, options, rowObject) {
                cellvalue='修改'
                cell = '<a href="javascript:updateBdcdySd(\'' + rowObject.BDCDYH + '\',\'' + rowObject.QLR + '\',\'' + rowObject.ZL + '\',\'' + rowObject.XZYY + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                return cell;
            }
            }
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
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
                var cellVal = "";
                if (data.XZZT == '0') {
                    cellVal = '<span class="label label-success">正常</span>';
                } else if (data.XZZT == '1') {
                    cellVal = '<span class="label label-danger">锁定</span>';
                } else {
                    cellVal = '<span class="label label-success">正常</span>';
                }
                $(grid_selector).setCell(data.BDCDYH, "XZZT", cellVal);
            })

        }/*,
         ondblClickRow:function (rowid) {
         var rowData = $(grid_selector).getRowData(rowid);
         if(rowData!=null)
         sdEditXm(rowid,rowData.BDCLX,rowData.BDCDYH);
         }*/,
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


function updateBdcdySd(bdcdyh, qlr,zl,xzyy) {
    $('#xgbdcdyh').val(bdcdyh);
    $('#xgqlr').val(qlr);
    $('#xgzl').val(zl);
    $('#xgxzyy').val(xzyy);
    $("#updateSdData").show();
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
        var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
        for (var i = 0; i < chk_value.length; i++) {
            var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
            $('#bdcdyh').val(rowData.BDCDYH);
            $('#qlr').val(rowData.QLR);
            $('#zl').val(rowData.ZL);
            $('#xzyy').val(rowData.XZYY);
        }
        $("#addSdData").show();
    } else if (id == 'sdShow') {
        $("#sdSearchPop").show();
    }
}

function add() {
    $("#addSdData").hide();
    var remoteUrl = bdcdjUrl + "/bdcSjSd/addBdcdySd";
    var sdData = $("#sdDataForm").serialize();
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: sdData,
        dataType: "json",
        success: function (data) {
            //alert(data);
            $.Prompt(data.message,1500);
            reload();
        },
        error: function (data) {
            $.Prompt('该不动产单元已锁定',1500);
        }
    });
}

function save() {
    $("#updateSdData").hide();
    var remoteUrl = bdcdjUrl + "/bdcSjSd/updateBdcdySd";
    var sdData = $("#updatesdDataForm").serialize();
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: sdData,
        dataType: "json",
        success: function (data) {
            //alert(data);
            $.Prompt(data.message,1500);
            reload();
        },
        error: function (data) {
            $.Prompt('该不动产单元未锁定',1500);
        }
    });
}

function showModal() {
    $('#myModal').show();
}

function closeModal() {
    $('#myModal').hide();
}
function chooseBdcdy(bdcdyh, bdclx, qlr, zl) {
    $('#bdcdyh').val(bdcdyh);
    $('#qlr').val(qlr);
    $('#zl').val(zl);
    $('#bdclx').val(bdclx);
    closeModal();
}

function unlock() {
    var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
    for (var i = 0; i < chk_value.length; i++) {
        var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
        unlockData(rowData.BDCDYH);
    }
}

function unlockData(bdcdyh) {
    var remoteUrl = bdcdjUrl + "/bdcSjSd/unlockBdcdySd";
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: {bdcdyh: bdcdyh},
        dataType: "json",
        success: function (data) {
            $.Prompt(data.message,1500);
            reload();
        },
        error: function (data) {
            //alert('');
        }
    });
}

function reload() {
    var url = bdcdjUrl + "/bdcSjSd/getBdcdySdPagesJson";
    tableReload("sd-grid-table", url, '', '');
    clearAddDialog();
}

function clearAddDialog() {
    $('#bdcdyh').val('');
    $('#qlr').val('');
    $('#zl').val('');
    $('#xzyy').val('');
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

function djsjInitTable() {
    var grid_selector = "#djsj-grid-table";
    var pager_selector = "#djsj-grid-pager";
    //绑定回车键
    $('#djsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
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
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'ID'},
        colNames: ['不动产单元号', '坐落', '权利人', '不动产类型', "ID"],
        colModel: [
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:chooseBdcdy(\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.TDZL + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    }
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'TDZL', index: 'TDZL', width: '10%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
            {
                name: 'BDCLX',
                index: 'BDCLX',
                width: '5%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var value = "";
                    if (cellvalue != null && cellvalue != "") {
                        if (cellvalue.indexOf('TD') > -1) {
                            if (cellvalue.indexOf('FW') > -1) {
                                value = "土地、房屋等建筑物";
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
                                value = "海域、房屋等建筑物";
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
                    return value;
                }
            },
            {name: 'ID', index: 'ID', width: '10%', sortable: false, hidden: true}
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
            qlrForTable(grid_selector, bdclxdm, zdtzm);
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}
