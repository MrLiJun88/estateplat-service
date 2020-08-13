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
    var jsxxUrl = bdcdjUrl + "/bdcJsxx/getBdcJsxxPagesJson";
    sdInitTable();
    tableReload("sd-grid-table", jsxxUrl, {
        dcxc: $("#sd_search").val(),
        bdcdyh: $("#bdcdyh").val(),
        fwdcbindex: $("#fwdcbindex").val()
    });
    //搜索事件
    $("#sd_search_btn").click(function () {
        var jsxxUrl = bdcdjUrl + "/bdcJsxx/getBdcJsxxPagesJson";
        tableReload("sd-grid-table", jsxxUrl, {
            dcxc: $("#sd_search").val(),
            bdcdyh: '',
            qlr: '',
            zl: '',
            jszt: '',
            bdclx: '',
            jbr: '',
            qxr: ''
        });
    })
    //单元号高级查询的搜索按钮事件
    $("#sdGjSearchBtn").click(function () {
        var bdcdyh = $('#searchBdcdyh').val();
        var qlr = $('#searchQlr').val();
        var zl = $('#searchZl').val();
        var xzzt = $('#searchXzzt').val();
        var bdclx = $('#searchBdclx').val();
        var jbr = $('#searchJbr').val();
        var qxr = $('#searchQxr').val();
        var jsxxUrl = bdcdjUrl + "/bdcJsxx/getBdcJsxxPagesJson";
        tableReload("sd-grid-table", jsxxUrl, {
            dcxc: "",
            bdcdyh: bdcdyh,
            qlr: qlr,
            zl: zl,
            xzzt: xzzt,
            bdclx: bdclx,
            jbr: jbr,
            qxr: qxr
        });
    })
    $(".sdSearchPop-modal,.newPro-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
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
    $("#djsjTab,#ywsjTab").click(function () {
        if (this.id == "djsjTab") {
            $("#djsj").addClass("active");
            var djsjUrl = bdcdjUrl + "/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";
            djsjInitTable();
            tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
        } else if (this.id == "ywsjTab") {
            $("#ywsj").addClass("active");
            var ywsjUrl = bdcdjUrl + "/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}${proid}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
            ywsjInitTable();
            tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
        }
    });
    $("#ywsj_search_btn").click(function () {
        var ywsjUrl = bdcdjUrl + "/selectBdcdy/getBdczsListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm;
        tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
    })
});
function readyBeforeInit(grid_selector, pager_selector, id) {
    //绑定回车键
    enterButton(id);
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
}
function sdInitTable() {
    var grid_selector = "#sd-grid-table";
    var pager_selector = "#sd-grid-pager";
    readyBeforeInit(grid_selector, pager_selector, 'sd_search');
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'JSID'},
        colNames: ['序列', '权利人', '不动产单元', '不动产权证号', '坐落', '警示原因', '状态', '经办人', '取消人', '修改', 'PROID'],
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
            {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '20%', sortable: false},
            {name: 'CQZH', index: 'CQZH', width: '20%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
            {name: 'JSYY', index: 'JSYY', width: '15%', sortable: false},
            {name: 'JSZT', index: 'JSZT', width: '5%', sortable: false},
            {name: 'JBR', index: 'JBR', width: '5%', sortable: false},
            {name: 'QXR', index: 'QXR', width: '5%', sortable: false},
            {
                name: 'XG', index: 'XG', width: '3%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:3px;"><div title="修改"  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="updateBdcJsxx(\'' + rowObject.BDCDYH + '\',\'' + rowObject.QLR + '\',\'' + rowObject.ZL + '\',\'' + rowObject.JSYY + '\',\'' + rowObject.CQZH + '\',\'' + rowObject.JSZT + '\',\'' + rowObject.QXYY + '\',\'' + rowObject.JSID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div></div>';
                }
            },
            {name: 'JSID', index: 'JSID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 5,
        //rowList: [7, 15, 20],
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
                $(grid_selector).jqGrid('setGridWidth', $(".tableHeader").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            setHeight(grid_selector);
            defineJszt(grid_selector);
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
function updateBdcJsxx(bdcdyh, qlr, zl, jsyy, cqzh, jszt, qxyy, jsid) {
    $('#xgjsid').val(jsid == 'undefined' ? '' : jsid);
    $('#xgbdcdyh').val(bdcdyh == 'undefined' ? '' : bdcdyh);
    $('#xgqlr').val(qlr == 'undefined' ? '' : qlr);
    $('#xgzl').val(zl == 'undefined' ? '' : zl);
    $('#xgxzyy').val(jsyy == 'undefined' ? '' : jsyy);
    $('#xgqxyy').val(qxyy == 'undefined' ? '' : qxyy);
    $('#xgcqzh').val(cqzh == 'undefined' ? '' : cqzh);
    if (jszt == '1' || jszt == '' || jszt == 'undefined') {
        $('#qxyyDiv').addClass('hide');
        $('#xzyyDiv').removeClass('hide');
    } else if ('0') {
        $('#xzyyDiv').addClass('hide');
        $('#qxyyDiv').removeClass('hide');
    }
    $("#updateJsxxData").show();
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
    clearUpdateDialog();
    clearCancelDialog();
    clearSearchDialog();
}
function showDialog(id) {
    if (id == 'addSel') {
        var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
        for (var i = 0; i < chk_value.length; i++) {
            var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
            $('#bdcdyh').val(rowData.BDCDYH);
            $('#qlr').val(rowData.QLR);
            $('#zl').val(rowData.ZL);
            $('#xzyy').val(rowData.JSYY);
            $('#cqzh').val(rowData.CQZH);
        }
        $("#addSdData").show();

    } else if (id == 'sdShow') {
        $("#sdSearchPop").show();
    }
    relocateDialog(id);

}
function add() {
    var bdcdyh = $('#bdcdyh').val();
    var cqzh = $('#cqzh').val();
    if ((bdcdyh == '' || bdcdyh == 'undefined') && (cqzh == '' || cqzh == 'undefined')) {
        alert('请选择不动产单元或产权证！')
        return false;
    }
    $("#addSdData").hide();
    var remoteUrl = bdcdjUrl + "/bdcJsxx/addBdcJsxx";
    var sdData = $("#sdDataForm").serialize();
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: sdData,
        dataType: "json",
        success: function (data) {
            alert(data.message);
            reload();
        },
        error: function (data) {
            alert('该不动产单元已锁定');
        }
    });
}
function save() {
    $("#updateJsxxData").hide();
    var remoteUrl = bdcdjUrl + "/bdcJsxx/updateBdcJsxx";
    var sdData = $("#updateJsxxDataForm").serialize();
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: sdData,
        dataType: "json",
        success: function (data) {
            alert(data.message);
            reload();
        },
        error: function (data) {
            alert('该不动产单元未锁定');
        }
    });
}
function showModal() {
    $('#myModal').show();
    relocateDialog('myModal');
}
function closeModal() {
    $('#myModal').hide();
    relocateDialog('addSel');
}
function chooseBdcdy(bdcdyh, bdclx, zl, cqzh, qlr, zsid) {
    $('#bdcdyh').val(bdcdyh);
    $('#cqzh').val(cqzh);
    $('#zl').val(zl);
    $('#bdclx').val(bdclx);
    $('#zsid').val(zsid);
    $('#qlr').val(qlr);
    closeModal();
}
function unlock() {
    var chk_value = $('#sd-grid-table').jqGrid('getGridParam', 'selarrrow');
    if (chk_value == '' || chk_value == 'undefined') {
        alert('请选择数据');
    }
    for (var i = 0; i < chk_value.length; i++) {
        var rowData = $('#sd-grid-table').jqGrid('getRowData', chk_value[i]);
        cancelJx(rowData.CQZH, rowData.BDCDYH, rowData.ZSID, rowData.JSID);
    }
}
function cancelJx(cqzh, bdcdyh, zsid, jsid) {
    $('#cancelJsid').val(jsid == 'undefined' ? '' : jsid);
    $('#cancelCqzh').val(cqzh == 'undefined' ? '' : cqzh);
    $('#cancelBdcdyh').val(bdcdyh == 'undefined' ? '' : bdcdyh);
    $('#cancelZsid').val(zsid == 'undefined' ? '' : zsid);
    $('#cancelJs').show();
}
function cancel() {
    $('#cancelJs').hide();
    var canceldata = $("#cancelJxForm").serialize();
    var remoteUrl = bdcdjUrl + "/bdcJsxx/cancelBdcJsxx";
    $.ajax({
        type: "POST",
        url: remoteUrl,
        data: canceldata,
        dataType: "json",
        success: function (data) {
            alert(data.message);
            reload();
        },
        error: function (data) {
        }
    });
}
function reload() {
    var url = bdcdjUrl + "/bdcJsxx/getBdcJsxxPagesJson";
    tableReload("sd-grid-table", url, '', '');
    clearAddDialog();
    clearUpdateDialog();
}
function clearAddDialog() {
    $('#bdcdyh').val('');
    $('#qlr').val('');
    $('#zl').val('');
    $('#xzyy').val('');
    $('#bdclx').val('');
    $('#cqzh').val('');
    $('#zsid').val('');
}
function clearUpdateDialog() {
    $('#xgjsid').val('');
    $('#xgbdcdyh').val('');
    $('#xgqlr').val('');
    $('#xgzl').val('');
    $('#xgxzyy').val('');
    $('#xgcqzh').val('');
}
function clearCancelDialog() {
    $('#cancelCqzh').val('');
    $('#cancelBdcdyh').val('');
    $('#cancelZsid').val('');
    $('#cancelReason').val('');
}
function clearSearchDialog() {
    $('#searchBdcdyh').val('');
    $('#searchQlr').val('');
    $('#searchZl').val('');
    $('#searchJbr').val('');
    $('#searchQxr').val('');
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
    if (djid == null || djid == " ")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + bdclxdm + "&zdtzm=" + zdtzm,
        success: function (result) {
            var qlr = result.qlr;
            if (qlr == null || qlr == " ")
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
    readyBeforeInit(grid_selector, pager_selector, 'djsj_search');
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
                        cell = '<a href="javascript:chooseBdcdy(\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.TDZL + '\',\'' + "" + '\',\'' + rowObject.QLR + '\',\'' + "" + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    }
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'TDZL', index: 'TDZL', width: '5%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '5%', sortable: false},
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
        rowNum: 5,
        rowList: [5, 15, 20],
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
                $(grid_selector).jqGrid('setGridWidth', "860");
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            setHeight(grid_selector);
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}
function ywsjInitTable() {
    var grid_selector = "#ywsj-grid-table";
    var pager_selector = "#ywsj-grid-pager";
    //resize to fit page size
    readyBeforeInit(grid_selector, pager_selector, 'ywsj_search');
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'ZSID'},
        colNames: ['不动产单元号', '坐落', '不动产权证号', '不动产类型','PROID' ,'ZSID'],
        colModel: [
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '10%',
                sortable: false
            },
            {name: 'ZL', index: 'ZL', width: '5%', sortable: false},
            {name: 'BDCQZH', index: 'BDCQZH', width: '5%', sortable: false},
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
            {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
            {name: 'ZSID', index: 'ZSID', width: '10%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 5,
        rowList: [5, 15, 20],
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
                $(grid_selector).jqGrid('setGridWidth', "850");
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            setHeight(grid_selector);
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.PROID,data.BDCQZH, $(grid_selector),data.ZSID);
            })
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

function getQlrmcAndZl(proid,bdcqzh, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/selectBdcdy/getdateByProid?proid=" + proid,
        success: function (result) {
            table.setCell(rowid, "ZL", result.zl);
            table.setCell(rowid, "QLR", result.qlr);
            table.setCell(rowid, "BDCDYH", result.bdcdyh);
            table.setCell(rowid, "BDCLX", result.bdclx);
            var bdcdyCellVal = "";
            bdcdyCellVal = '<a href="javascript:chooseBdcdy(\'' + result.bdcdyh + '\',\'' + result.bdclx+ '\',\'' + result.zl + '\',\'' + bdcqzh + '\',\'' + result.qlr + '\',\'' + rowid + '\')" title="' + result.bdcdyh + '" >' + result.bdcdyh+ "</a>";
            table.setCell(rowid, "BDCDYH", bdcdyCellVal);
            var bdcqzhCellVal = "";
            bdcqzhCellVal = '<a href="javascript:chooseBdcdy(\'' + result.bdcdyh + '\',\'' + result.bdclx+ '\',\'' + result.zl + '\',\'' + bdcqzh + '\',\'' + result.qlr + '\',\'' + rowid + '\')" title="' + bdcqzh + '" >' + bdcqzh+ "</a>";
            table.setCell(rowid, "BDCQZH", bdcqzhCellVal);
        }
    });
}
function defineJszt(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    $.each(jqData, function (index, data) {
        var cellVal = "";
        if (data.JSZT == '0') {
            cellVal = '<span class="label label-success">正常</span>';
        } else if (data.JSZT == '1') {
            cellVal = '<span class="label label-danger">锁定</span>';
        } else {
            cellVal = '<span class="label label-success">正常</span>';
        }
        $(grid_selector).setCell(data.JSID, "JSZT", cellVal);
    })

}
function enterButton(id) {
    $('#' + id).keydown(function (event) {
        if (event.keyCode == 13) {
            $('#' + id + '_btn').click();
        }
    });
}
function setHeight(grid_selector) {
    $(grid_selector).jqGrid("setGridHeight", "200px");
    $(window).trigger('resize.chosen');
    var browser = navigator.appName;
    if (browser != 'Microsoft Internet Explorer') {
        $("#myModal .modal-content").css({"height": "480px", "margin-top": "-30px"});
        $(" #myModal .modal-content .tab-content").css({"height": "430px"});
    }
}
function relocateDialog(id) {
    $(window).trigger('resize.chosen');
    var browser = navigator.appName;
    if (browser == 'Microsoft Internet Explorer') {
        if (id == 'addSel') {
            $(".modal-dialog").css({"margin-left": "2%"});
        } else if (id == 'myModal') {
            $(".modal-dialog").css({"margin-left": "8%"});
        }
    } else {
        if (id == 'addSel') {
            $(".modal-dialog").css({"margin-left": "25%"});
        } else if (id == 'myModal') {
            $(".modal-dialog").css({"margin-left": "8%"});
        }

    }
}
