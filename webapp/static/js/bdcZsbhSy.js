$(function () {
    doBefore();
    var zsbhSyUrl = bdcdjUrl + "/bdcZsbhSy/getBdcZsbhSyPagesJson";
    sdInitTable();
    tableReload("sd-grid-table", zsbhSyUrl, {
        dcxc: $("#sd_search").val(),
        zsid: zsid,
        zsbh: zsbh
    });
    //搜索事件
    $("#sd_search_btn").click(function () {
        tableReload("sd-grid-table", zsbhSyUrl, {
            dcxc: $("#sd_search").val()
        });
    })
    //单元号高级查询的搜索按钮事件
    $("#sdGjSearchBtn").click(function () {
        var bdcdyh = $('#searchBdcdyh').val();
        var fzr = $('#searchFzr').val();
        var bdcqzh = $('#searchBdcqzh').val();
        var zsbh = $('#searchZsbh').val();
        var fzrq = $('#searchFzrq').val();
        tableReload("sd-grid-table", zsbhSyUrl, {
            dcxc: "",
            bdcdyh: bdcdyh,
            fzr: fzr,
            bdcqzh: bdcqzh,
            zsbh: zsbh,
            fzrq: fzrq
        });
    })
    $(".sdSearchPop-modal,.newPro-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //单元号高级查询的搜索按钮事件
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
});
function doBefore() {
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
    $(window).on('resize.chosen', function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
    }).trigger('resize.chosen');
    $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "", width: "100%"});
}
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
        jsonReader: {id: 'ZSID'},
        colNames: ['序列', '证书编号', '不动产单元', '不动产权证号', '打印人', '打印日期', 'ZSID'],
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
            {name: 'ZSBH', index: 'ZSBH', width: '8%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '10%', sortable: false},
            {name: 'BDCQZH', index: 'BDCQZH', width: '15%', sortable: false},
            {name: 'FZR', index: 'FZR', width: '10%', sortable: false},
            {
                name: 'FZRQ',
                index: 'FZRQ',
                width: '10%',
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
            {name: 'ZSID', index: 'ZSID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        //rowList: [7, 15, 20],
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
                //resize
                $(grid_selector).jqGrid('setGridWidth', $(".tableHeader").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            setHeight(grid_selector);
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
function showModal() {
    $('#myModal').show();
    relocateDialog('myModal');
}
function closeModal() {
    $('#myModal').hide();
    relocateDialog('addSel');
}
function reload() {
    var url = bdcdjUrl + "/bdcJsxx/getBdcJsxxPagesJson";
    tableReload("sd-grid-table", url, '', '');
}
function clearSearchDialog() {
    $('#searchBdcdyh').val('');
    $('#searchFzr').val('');
    $('#searchBdcqzh').val('');
    $('#searchZsbh').val('');
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