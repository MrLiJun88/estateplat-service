/**
 * Created by jhj on 2017/1/16.
 */
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
    var xymxUrl = bdcdjUrl + "/bdcXymx/getBdcXymxPagesJson";
    xymxInitTable();
    tableReload("sd-grid-table", xymxUrl, {
        dcxc: $("#sd_search").val(),
        xyglid: $('#xyglid').val()
    });
    //搜索事件
    $("#sd_search_btn").click(function () {
        var xymxUrl = bdcdjUrl + "/bdcXymx/getBdcXymxPagesJson";
        tableReload("sd-grid-table", xymxUrl, {
            dcxc: $("#sd_search").val()
        });
    })
    $("#xymxGjSearchBtn").click(function () {
        var shr = $('#shr').val();
        var shsjStart = $('#shsjStart').val();
        var shsjEnd = $('#shsjEnd').val();
        var sfsx = $('#sfsx').val();
        var xymxUrl = bdcdjUrl + "/bdcXymx/getBdcXymxPagesJson";
        tableReload("sd-grid-table", xymxUrl, {
            dcxc: "",
            shr: shr,
            shsjStart: shsjStart,
            shsjEnd: shsjEnd,
            sfsx: sfsx
        });
    })
    $(window).on('resize.chosen', function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
    }).trigger('resize.chosen');
    $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "", width: "100%"});
    //时间控件
    timeButton();
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
function xymxInitTable() {
    var grid_selector = "#sd-grid-table";
    var pager_selector = "#sd-grid-pager";
    readyBeforeInit(grid_selector, pager_selector, 'sd_search');
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'XYMXID'},
        colNames: ['序列', '审核人', '审核时间', '内容', '是否生效', '', 'XYMXID'],
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
            {name: 'SHR', index: 'SHR', width: '10%', sortable: false},
            {
                name: 'SHSJ',
                index: 'SHSJ',
                width: '20%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return "";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy年MM月dd日");
                    return data;
                }
            },
            {name: 'NR', index: 'NR', width: '20%', sortable: false},
            {name: 'SFSX', index: 'SFSX', width: '15%', sortable: false},

            {
                name: 'XG', index: 'XG', width: '3%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:3px;"><div title="修改"  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="updateBdcXymx(\'' + rowObject.XYMXID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div></div>';
                }
            },
            {name: 'XYMXID', index: 'XYMXID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        rownumbers: false,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                $(grid_selector).jqGrid('setGridWidth', $(".tableHeader").width());
            }, 0);
            setHeight(grid_selector);
            defineSfsx(grid_selector);
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
function updateBdcXymx(xymxid) {
    $('#xymxid').val(xymxid == 'undefined' ? '' : xymxid);
    var msg = "是否确认生效?";
    showConfirmDialog("提示信息", msg, "confirmEffect", "'" + xymxid + "'", "", "");
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
    clearSearchDialog();
    clearAddDialog();
}
function showDialog(id) {
    if (id == 'addXymx') {
        $("#addXymx").show();
    } else if (id == 'sdShow') {
        $("#sdSearchPop").show();
    }
    relocateDialog(id);
}
function confirmEffect(xymxid) {
    var remoteUrl = bdcdjUrl + "/bdcXymx/updateBdcXymx";
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
            alert('该数据不能立即生效');
        }
    });
}
function showModal() {
    $('#addData').show();
    relocateDialog('addData');
}
function closeModal() {
    $('#myModal').hide();
    relocateDialog('addSel');
}
function reload() {
    var url = bdcdjUrl + "/bdcXymx/getBdcXymxPagesJson";
    tableReload("sd-grid-table", url, {xyglid: $('#xyglid').val()});
    clearAddDialog();
    clearUpdateDialog();
}
function clearSearchDialog() {
    $('#shr').val('');
    $('#shsj').val('');
    $('#sfsx').val('');
}
function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}
function defineSfsx(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    $.each(jqData, function (index, data) {
        var cellVal = "";
        if (data.SFSX == '0') {
            cellVal = '<span class="label label-success">未生效</span>';
        } else if (data.SFSX == '1') {
            cellVal = '<span class="label label-danger">生效</span>';
        } else {
            cellVal = '<span class="label label-success">未生效</span>';
        }
        $(grid_selector).setCell(data.XYMXID, "SFSX", cellVal);
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
    if ($(grid_selector).jqGrid("getRowData").length == 7) {
        $(grid_selector).jqGrid("setGridHeight", "auto");
    } else {
        $(grid_selector).jqGrid("setGridHeight", "275px");
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
function clearAddDialog() {
    $("#addDataForm")[0].reset();
}
function timeButton() {
    $('.date-picker').datepicker({
        autoclose: true,
        todayHighlight: true,
        language: 'zh-CN'
    }).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
}
function addXymx() {
    var xyglid = $('#xyglid').val();
    var nr = $('#nr').val();
    if((xyglid==''||xyglid=='undefined')||(nr==''||nr=='undefined')){
        alert('请输入内容');
        return false;
    }
    var remoteUrl = bdcdjUrl + "/bdcXymx/addBdcXymx";
    var sdData = $("#addDataForm").serialize();
    closeDialog('addData');
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
            alert('添加失败');
        }
    });
}
