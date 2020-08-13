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
    var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcqzSdPagesJson";
    if (bdcqzh != null) {
        $('#sd_search').val(bdcqzh);
    }
    sdInitTable();

    tableReload("sd-grid-table", sdUrl, {
        dcxc: $("#sd_search").val(),
        bdcdyh: $("#bdcdyh").val(),
        fwdcbindex: $("#fwdcbindex").val()
    });


    //搜索事件
    $("#sd_search_btn").click(function () {
        var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcqzSdPagesJson";
        tableReload("sd-grid-table", sdUrl, {dcxc: $("#sd_search").val()});
    });

    //单元号高级查询的搜索按钮事件
    $("#sdGjSearchBtn").click(function () {
        var xzzt = $('#searchXzzt').val();
        var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcqzSdPagesJson";
        tableReload("sd-grid-table", sdUrl, {
            dcxc: "",
            xzzt: xzzt
        });
    });

    $(".sdSearchPop-modal,.newPro-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //默认初始化表格

    //添加数据
    $("#add").click(function () {
        var type = "";
        var bdcqzh = $('#bdcqzh').val();
        var fczh = $('#fczh').val();
        var tdzh = $('#tdzh').val();
        if(bdcqzh && bdcqzh!=''){
            type = "bdcqzh";
        }else if(fczh && fczh!=''){
            type = "fczh";
        }else if(fczh && fczh!=''){
            type = "tdzh";
        }
        add(type);
    });

    //修改数据
    $("#save").click(function () {
        save();
    });


    $("#modifySdSave").click(function () {
        $.blockUI({message: "请稍等……"});
        bootbox.dialog({
            message: "<h3><b>是否确定修改？</b></h3>",
            title: "",
            closeButton: false,
            buttons: {
                success: {
                    label: "确定",
                    className: "btn-success",
                    callback: function () {
                        setTimeout($.unblockUI, 10);
                        modifySdSave();
                    }
                },
                main: {
                    label: "取消",
                    className: "btn-primary",
                    callback: function () {
                        setTimeout($.unblockUI, 10);
                    }
                }
            }
        });
    });

    $("#unlockSdSave").click(function () {
        unlock();
    });

    $("#djsjTab,#fcsjTab,#tdsjTab").click(function () {
        if (this.id == "djsjTab") {
            $("#djsj").addClass("active");
            djsjInitTable();
        } else if (this.id == "fcsjTab") {
            $("#fcsj").addClass("active");
            fwTableInit();
        } else if (this.id == "tdsjTab"){
            $("#tdsj").addClass("active");
            tdTableInit();
        }
    });

    //单元号高级查询的搜索按钮事件
    $("#djsjGjSearchBtn").click(function () {
        var djsjUrl = bdcdjUrl + "/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc: ""});
        $("#djsjSearchPop").hide();
        $("#djsjSearchForm")[0].reset();
    });
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
    //默认初始化表格，限制总表格
    djsjInitTable();
    //搜索事件
    //不动产权
    $("#ywsj_search_btn").click(function () {
        var djsjUrl = bdcdjUrl + "/bdcSjSd/getBdczsListByPage?";
        tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#ywsj_search").val()});
    });

    //房产证
    $("#fcsj_search_btn").click(function () {
        var fcsjUrl = bdcdjUrl + "/bdcSjSd/getGdFwJson?";
        tableReload("fcsj-grid-table",fcsjUrl, {hhSearch: $("#fcsj_search").val()});
    });

    //土地证
    $("#tdsj_search_btn").click(function () {
        var tdsjUrl = bdcdjUrl + "/bdcSjSd/getGdTdJson?";
        tableReload("tdsj-grid-table", tdsjUrl, {hhSearch: $("#tdsj_search").val()});
    });

    $(window).on('resize.chosen', function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
    }).trigger('resize.chosen');
    $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "", width: "100%"});
});


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
        jsonReader: {id: 'SDID'},
        colNames: ['序列', '权证号', /*'不动产单元',*/ '坐落', '限制原因', '限制状态', '锁定人', '解锁人', '锁定时间','解锁时间','PROID','SDID','LY','QLID'/*,'修改'*/],
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
            {name: 'CQZH', index: 'CQZH', width: '20%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
            {name: 'XZYY', index: 'XZYY', width: '15%', sortable: false},
            {name: 'XZZT', index: 'XZZT', width: '5%', sortable: false},
            {name: 'SDR', index: 'SDR', width: '5%', sortable: false},
            {name: 'JSR', index: 'JSR', width: '5%', sortable: false},
            {name: 'SDSJ', index: 'SDSJ', width: '5%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return "";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
            {name: 'JSSJ', index: 'JSSJ', width: '5%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return "";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
            {name: 'SDID', index: 'SDID', width: '0%', sortable: false, hidden: true},
            {name: 'LY', index: 'LY', width: '0%', sortable: false, hidden: true},
            {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true}
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
                debugger;
                var cellVal = "";
                if (data.XZZT ==0) {
                    cellVal = '<span class="label label-success">正常</span>';
                } else if (data.XZZT == 1) {
                    cellVal = '<span class="label label-danger">锁定</span>';
                } else {
                    cellVal = '<span class="label label-success">正常</span>';
                }
                $(grid_selector).setCell(data.SDID, "XZZT", cellVal);
                getZl(data.QLID,data.PROID ,$(grid_selector),data.SDID);
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

//获取数据
function getQlrmcAndZl(bdcqzh, table, rowid) {
    $.ajax({
        type: "GET",
        url:bdcdjUrl + "/selectBdcdy/getdateByBdcqzh?bdcqzh=" + bdcqzh,
        //url: "${bdcdjUrl}/selectBdcdy/getdateByBdcqzh?bdcqzh=" + bdcqzh,
        success: function (result) {
            table.setCell(rowid, "ZL", result.zl);
            //table.setCell(rowid, "QLR", result.qlr);
            //table.setCell(rowid, "BDCDYH", result.bdcdyh);
            table.setCell(rowid, "BDCLX", result.bdclx);
            //var bdcdyCellVal = "";
            //table.setCell(rowid, "BDCDYH", bdcdyCellVal);
        }
    });
}

//获取数据
function getZl(qlid,proid, table, rowid) {
    $.ajax({
        type: "GET",
        url:bdcdjUrl + "/bdcSjSd/getdateByQlidAndProid?proid=" + proid+"&qlid="+qlid,
        success: function (result) {
            table.setCell(rowid, "ZL", result.zl);
        }
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
        var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
        for (var i = 0; i < chk_value.length; i++) {
            var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
            $('#bdcdyh').val(rowData.BDCDYH);
            $('#bdcqzh').val(rowData.CQZH);
            $('#zl').val(rowData.ZL);
            $('#xzyy').val(rowData.XZYY);
        }
        $("#addSdData").show();
    } else if (id == 'sdShow') {
        $("#sdSearchPop").show();
    }
}

function modifylock(){
    var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
    for (var i = 0; i < chk_value.length; i++) {
        var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
        modifydata(rowData.CQZH,rowData.LY,rowData.QLID,rowData.SDID);
    }
}

function modifydata(cqzh,ly,qlid,sdid){
    var remoteUrl = "";
    if (ly && ly == 'BDC') {
        remoteUrl = bdcdjUrl + "/bdcSjSd/updateBdcqzSd";
    }else if (ly && ly=='GD'){
        remoteUrl = bdcdjUrl + "/bdcSjSd/updateGdSd";
    }
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: {cqzh: cqzh, qlid: qlid,sdid:sdid},
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

function updateBdcqzSd(bdcdyh, bdcqzh,zl,xzyy) {
    $('#xgbdcdyh').val(bdcdyh);
    $('#xgbdcqzh').val(bdcqzh);
    $('#xgzl').val(zl);
    $('#xgxzyy').val(xzyy);
    $("#updateSdData").show();
}

function add(type) {
    $("#addSdData").hide();
    var remoteUrl = "";
    if(type == 'bdcqzh'){
        remoteUrl = bdcdjUrl + "/bdcSjSd/addBdcqzSd";
    }else{
        remoteUrl = bdcdjUrl + "/bdcSjSd/addBdcqzToBdcGdSd";
    }
    var sdData = $("#sdDataForm").serialize();
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: sdData,
        dataType: "json",
        success: function (data) {
            //alert(data);
            $.Prompt(data.message,1500);
            $('#sdDataForm').reset();
            reload();
        },
        error: function (data) {
            $('#sdDataForm').reset();
            $.Prompt('该产权证号已锁定',1500);
        }
    });
}

function save() {
    $("#updateSdData").hide();
    var remoteUrl = bdcdjUrl + "/bdcSjSd/updateBdcqzSd";
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
    $('#myModal1').show();
}

function closeModal() {
    $('#myModal1').hide();
}
function chooseBdcdy(bdcdyh, bdclx, bdcqzh, proid, zl,type) {
    if(type && type != '') {
        if (type == 'bdcqzh') {
            $('#bdcdyh').val(bdcdyh);
            $('#bdcqzh').val(bdcqzh);
            $('#zl').val(zl);
            $('#bdclx').val(bdclx);
            $('#proid').val(proid);
            $('#fczh').val("");
            $('#qlid').val("");
            $('#tdfwid').val("");
            $('#tdzh').val("");
        } else if (type == 'fczh') {
            $('#fczh').val(bdcqzh);
            $('#qlid').val(bdcdyh);
            $('#tdfwid').val(bdclx);
            $('#proid').val(proid);
            $('#bdcdyh').val("");
            $('#bdcqzh').val("");
            $('#tdzh').val("");
        } else if (type == 'tdzh'){
            $('#tdzh').val(bdcqzh);
            $('#qlid').val(bdcdyh);
            $('#tdfwid').val(bdclx);
            $('#proid').val(proid);
            $('#bdcdyh').val("");
            $('#bdcqzh').val("");
            $('#fczh').val("");
        }
    }
    closeModal();
}

function unlock() {
    var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
    var jsyy = $("#jsyy").val();
    closeDialog('unlockSdData');
    $.blockUI({ message:"请稍等……" });
    bootbox.dialog({
        message: "<h3><b>确认根据限制原因解除坐落的锁定吗？</b></h3>",
        title:"",
        closeButton:false,
        buttons:{
            success:{
                label:"确定",
                className:"btn-success",
                callback:function () {
                    setTimeout($.unblockUI, 10);
                    for (var i = 0; i < chk_value.length; i++) {
                        var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
                        unlockData(rowData.CQZH,rowData.LY,rowData.QLID,jsyy,rowData.SDID);
                    }
                }
            },
            main:{
                label:"取消",
                className:"btn-primary",
                callback:function () {
                    setTimeout($.unblockUI, 10);
                }
            }
        }
    });
}

function unlockData(cqzh,ly,qlid,jsyy,sdid) {
    var remoteUrl = "";
    if (ly && ly == 'BDC') {
        remoteUrl = bdcdjUrl + "/bdcSjSd/unlockBdcqzSd";
    }else if (ly && ly=='GD'){
        remoteUrl = bdcdjUrl + "/bdcSjSd/unlockGdSd";
    }
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: {bdcqzh: cqzh, qlid: qlid, jsyy: jsyy,sdid:sdid},
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
    var url = bdcdjUrl + "/bdcSjSd/getBdcqzSdPagesJson";
    tableReload("sd-grid-table", url, '', '');
    clearAddDialog();
}

function clearAddDialog() {
    $('#bdcdyh').val('');
    $('#bdcqzh').val('');
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
    $('#ywsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    //resize to fit page size
    var contentWidth = $(".bootbox").width();
    contentWidth=850;
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
        jsonReader: {id: 'BDCQZH'},
        colNames: ['不动产单元号', '坐落', '不动产权证号', '不动产类型','PROID'],
        colModel: [
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '10%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var type = "bdcqzh";
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:chooseBdcdy(\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCQZH + '\',\'' + rowObject.PROID + '\',\'' + rowObject.ZL + '\',\'' + type + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    }
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
            {name: 'BDCQZH', index: 'BDCQZH', width: '10%', sortable: false},
            {
                name: 'BDCLX',
                index: 'BDCLX',
                width: '10%',
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
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false,hidden: true},
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        //rownumbers: true,
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
            //加载其他数据信息
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.BDCQZH, $(grid_selector),data.BDCQZH);
            });
            qlrForTable(grid_selector, bdclxdm, zdtzm);
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

//房产证初始化
function fwTableInit() {
    var grid_selector = "#fcsj-grid-table";
    var pager_selector = "#fcsj-grid-pager";
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
        //url: "${bdcdjUrl}/bdcJgSjgl/getGdFwJson?filterFwPpzt=${filterFwPpzt!}",
        url:"",
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'QLID'},
        colNames: ['序列', '权利人', '证书类型', "房产证号", '坐落', '权利状态','限制原因', '锁定状态'/*,'打印回执单'*/, '匹配状态', 'QLZT', 'PROID', 'QLID', 'FWID'],
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
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '20%', sortable: false},
            {name: 'ZSLX', index: 'ZSLX', width: '12%', sortable: false},
            {name: 'FCZH', index: 'FCZH', width: '18%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var type = "fczh";
                    if (cellvalue != null && cellvalue != '') {
                        cell = '<a href="javascript:chooseBdcdy(\'' + rowObject.QLID + '\',\'' + rowObject.FWID + '\',\'' + rowObject.FCZH + '\',\'' + rowObject.PROID + '\',\'' + rowObject.ZL + '\',\'' + type + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    }
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'FWZL', index: 'FWZL', width: '20%', sortable: false},
            {name: 'STATUS', index: '', width: '8%', sortable: false, hidden:true},
            {name: 'XZYY', index: 'XZYY', width: '10%', sortable: false, hidden:true},
            {name: 'XZZT', index: 'XZZT', width: '8%', sortable: false, hidden:true},
            //{name:'mydy', index:'', width:'6%', sortable:false, formatter:function (cellvalue, options, rowObject) {
            //    //zdd 不允许打印证书 return '<div style="margin-left:8px;"> <div title="打印" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-print fa-lg blue"></span></div>&nbsp;&nbsp;<div title="查看收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div></div>'
            //    return '<div style="margin-left:8px;">' +
            //        '<div title="打印" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="printhzd(\'' + rowObject.QLID + '\',\'TDFW\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-file-text fa-lg red"></span></div>' +
            //        '</div>'
            //}
            //},
            {name: 'PPZT', index: '', width: '13%', sortable: false, hidden: true},
            {name: 'QLZT', index: 'QLZT', width: '0%', sortable: false, hidden: true},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
            {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
            {name: 'FWID', index: 'FWID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
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
                //resize
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            $("#fwid").val("");
            $("#dah").val("");
            $("#xmmc").val("");
            $("#dyid").val("");
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            for (var i = 0; i <= $mulRowid.length; i++) {
                $(grid_selector).jqGrid('setSelection', $mulRowid[i]);
            }
            //赋值数量
            $("#gdfwMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            //$.each(jqData, function (index, data) {
            //    asyncGetGdFwxx(data.PROID, $(grid_selector), data.PROID, data.QLZT, data.QLID);
            //    getFwId(data.QLID, $(grid_selector), data.QLID)
            //    getGdSd(data.QLID, $(grid_selector), data.QLID);
            //    getXzyy(data.QLID, $(grid_selector),data.QLID);
            //})
        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);

            aRowids.forEach(function (e) {

                var cm = $myGrid.jqGrid('getRowData', e);
                var index = $.inArray(e, $mulRowid);
                if (status && index < 0) {
                    $mulData.push(cm);
                    $mulRowid.push(e);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);

                }
            })
            //赋值数量

            $("#gdfwMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        gridComplete: function () {
            $.each($mulData, function (index) {
                $selectedInput.push($mulData[index].PROID);
            });
            $.each($selectedInput, function (index) {
                $('#' + $selectedInput[index] + '').click();
            });
            $selectedInput.splice(0, $selectedInput.length);
        },
        beforeSelectRow: function (rowid, e) {
            var $myGrid = $(this),
                i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
                cm = $myGrid.jqGrid('getGridParam', 'colModel');
            return (cm[i].name === 'cb');
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//zx 获取房产证抵押 查封 预告 异议 状态
function asyncGetGdFwxx(bdcid, table, rowid, qlzt, qlid) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdFwxxByQlid?bdclx=TDFW" + "&qlid=" + qlid + "&bdcid=" + bdcid,
        dataType: "json",
        success: function (result) {
            var cellVal = "";
            //正常
            var zls = result.zls;
            if (result.qlzts == null || result.qlzts == "") {
                cellVal += '<span class="label label-success">正常</span>';
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson?bdclx=TDFW" + "&zl=" + zls,
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
            table.setCell(qlid, "STATUS", cellVal);
//                if (result.qlrs != null && result.qlrs != "" && result.qlrs != "null")
//                    table.setCell(qlid, "RF1DWMC", result.qlrs);
//                if (result.cqzhs != null && result.cqzhs != "" && result.cqzhs != "null")
//                    table.setCell(qlid, "FCZH", result.cqzhs);
//                if (result.zls != null && result.zls != "" && result.zls != "null")
//                    table.setCell(qlid, "FWZL", result.zls);

            //getQtPpzt(result.ppzt, table, qlid);
        }
    });
}


//土地证初始化
function tdTableInit() {
    var grid_selector = "#tdsj-grid-table";
    var pager_selector = "#tdsj-grid-pager";

    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    //var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        //url: "${bdcdjUrl}/bdcSjSd/getGdTdJson?filterFwPpzt=${filterFwPpzt!}",
        url:"",
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'QLID'},
        colNames: ['序列', '地籍号', '坐落', '权利人', "土地证号", '权利状态','限制原因', '锁定状态',/*'打印回执单',*/ '匹配状态', 'QLID', '权利人', 'PROID', 'TDID'],
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
            {name: 'DJH', index: 'DJH', width: '10%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '20%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
            {name: 'TDZH', index: 'TDZH', width: '15%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    var type = "tdzh";
                    if (cellvalue != null && cellvalue != '') {
                        cell = '<a href="javascript:chooseBdcdy(\'' + rowObject.QLID + '\',\'' + rowObject.TDID + '\',\'' + rowObject.TDZH + '\',\'' + rowObject.PROID + '\',\'' + rowObject.ZL + '\',\'' + type + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    }
                    else
                        cell = '';
                    return cell;
                }},
            {name: 'STATUS', index: '', width: '8%', sortable: false, hidden:true},
            {name: 'XZYY', index: 'XZYY', width: '10%', sortable: false, hidden:true},
            {name: 'XZZT', index: 'XZZT', width: '8%', sortable: false, hidden:true},
            //{name:'mydy', index:'', width:'6%', sortable:false, formatter:function (cellvalue, options, rowObject) {
            //    //zdd 不允许打印证书 return '<div style="margin-left:8px;"> <div title="打印" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-print fa-lg blue"></span></div>&nbsp;&nbsp;<div title="查看收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div></div>'
            //    return '<div style="margin-left:8px;">' +
            //        '<div title="打印" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="printhzd(\'' + rowObject.QLID + '\',\'TD\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-file-text fa-lg red"></span></div>' +
            //        '</div>'
            //}
            //},
            {name: 'PPZT', index: '', width: '13%', sortable: false, hidden: true},
            {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
            {name: 'RF1DWMC', index: 'RF1DWMC', width: '0%', sortable: false, hidden: true},
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
            {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        loadComplete: function () {
            //zwq 清空全局数组
//                $selectArray.length = 0;
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
            //$.each(jqData, function (index, data) {
            //    asyncGetGdTdxx($(grid_selector), data.QLID, data.PROID);
            //    getGdSd(data.TDID, $(grid_selector), data.QLID);
            //    getXzyy(data.QLID, $(grid_selector),data.QLID);
            //})
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//zx 获取土地证抵押 查封 预告 异议 状态
function asyncGetGdTdxx(table, qlid, proid) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdTdxxByQlid?bdclx=TD" + "&qlid=" + qlid,
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
            if(cellVal.indexOf("注销") > -1){
                cellVal = '<span class="label label-danger">注销</span>';
            }
            table.setCell(qlid, "STATUS", cellVal);
            //getQtPpzt(result.ppzt, table, qlid);
        }
    });
}

function modifySd(){
    var modifySdIds = $("#sdIds").val();
    var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
    for (var i = 0; i < chk_value.length; i++) {
        var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
        if(modifySdIds == "" || modifySdIds == null){
            modifySdIds = rowData.SDID;
        }else if(modifySdIds != "" && modifySdIds != null){
            modifySdIds = modifySdIds + "," + rowData.SDID;
        }
    }
    if(modifySdIds != "undefined" && modifySdIds != null && modifySdIds != ""){
        $("#sdIds").val(modifySdIds);
    }
    $("#modifySdData").show();
}

function modifySdSave(){
    var modifySdIds = $("#sdIds").val();
    var xzyy = $("#xgxzyy").val();
    $("#sdIds").val("");
    $.ajax({
        type: "POST",
        url: bdcdjUrl + "/bdcSjSd/modifySdSave",
        data: { sdids: modifySdIds,xzyy:xzyy},
        dataType: "json",
        success: function (data) {
            $.Prompt("请输入权利人/坐落/不动产单元号/房屋编号",1500);
            closeDialog('modifySdData');
            var sdUrl = bdcdjUrl + "/bdcSjSd/getBdcqzSdPagesJson";
            tableReload("sd-grid-table", sdUrl, {dcxc: $("#sd_search").val()});
        },
        error: function (data) {
            $.Prompt(data.message,1500);
        }
    });
}

function getBdcdySd(proid, table, rowid) {
    $.ajax({
        type: "POST",
        url: "${bdcdjUrl}/bdcSjSd/getBdcdySdXzzt",
        dataType: "json",
        data: {proid: proid},
        success: function (result) {
            var cellVal = "";
            if (result.XZZT == '0') {
                cellVal += '<span class="label label-danger">锁定</span>';
            } else if (result.XZZT == '1') {
                cellVal += '<span class="label label-warning">正常</span>';
            } else {
                cellVal += '<span class="label label-success">正常</span>';
            }
            table.setCell(rowid, "XZZT", cellVal);
            if (result.XZYY != null && result.XZYY != "" && result.XZYY != undefined) {
                table.setCell(rowid, "XZYY", result.XZYY);
            }
        }
    });
}


function insertDataToGdSd(proid, qlid, zh, sdid, type,xzyy) {
    var remoteUrl = "${bdcdjUrl}/bdcSjSd/addBdcqzToBdcGdSd";
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: {proid: proid, qlid: qlid, cqzh: zh, tdfwid: sdid, type: type,xzyy:xzyy},
        //data:$('#form').serialize(),
        dataType: "json",
        success: function (data) {
            $.Prompt(data.message,1500);
            refresh(data.type,data.qlid,data.proid);
        },
        error: function (data) {
            $.Prompt(data.message,1500);
        }
    });
}
function unlockbefore(){
    var modifySdIds = $("#sdIds").val();
    var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
    for (var i = 0; i < chk_value.length; i++) {
        var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
        if(modifySdIds == "" || modifySdIds == null){
            modifySdIds = rowData.SDID;
        }else if(modifySdIds != "" && modifySdIds != null){
            modifySdIds = modifySdIds + "," + rowData.SDID;
        }
    }
    if(modifySdIds != "undefined" && modifySdIds != null && modifySdIds != ""){
        $("#sdIds").val(modifySdIds);
    }
    $("#unlockSdData").show();
}
