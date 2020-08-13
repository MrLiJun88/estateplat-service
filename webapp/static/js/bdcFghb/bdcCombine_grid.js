$(function() {
    djsjInitTable();
    $("#bdcdyTab").click(function () {
        removewAllActive();
        $("#bdcdyModel").addClass("active");
        djsjInitTable();
    })
    $("#djsjbdcdy_searchBtn").click(function () {
        var qlr = $("#djsjbdcdy_qlr").val();
        var zl = $("#djsjbdcdy_zl").val();
        var bdcdyh = $("#djsjbdcdy_bdcdyh").val();
        var Url = bdcdjUrl+"/bdcpic/getDjsjBdcdyByPage";
        tableReload("djsj-grid-table", Url, {qlr: qlr, zl: zl, bdcdyh: bdcdyh,bdclx:ml_bdclx});
    });
})

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

function djsjInitTable() {
    var grid_selector = "#djsj-grid-table";
    var pager_selector = "#djsj-grid-pager";
    //绑定回车键
    $('#djsjbdcdy_qlr,#djsjbdcdy_zl').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsjbdcdy_searchBtn").click();
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

    jQuery(grid_selector).jqGrid({
        datatype: "json",
        height: 'auto',
        jsonReader: {id: 'ID'},
        colNames: ['地籍号', '不动产单元号', '坐落', '权利人', '不动产类型', 'ID'],
        colModel: [
            {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '25%', sortable: false},
            {name: 'TDZL', index: 'TDZL', width: '20%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
            {name: 'BDCLX', index: 'BDCLX', width: '5%', sortable: false},
            {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        beforeRequest :function(){
            $(pager_selector+"_right").hide();
            var gridId=grid_selector.substring(1);
            $("#cb_"+gridId).hide();//隐藏全选按钮
        },
        loadComplete: function () {
            if (djsjbdcdy_bdcdyh != "" && djsjbdcdy_id != "") {
                $(grid_selector).jqGrid('setSelection', djsjbdcdy_id, true);
            }
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                var replacement =
                    {
                        'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
                    };
                $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                    var icon = $(this);
                    var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                    if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                })
            }, 0);
        },
        onSelectRow: function (rowid, status) {
            djsjbdcdy_bdcdyh = "";
            djsjbdcdy_id = "";
            $(grid_selector).jqGrid('resetSelection');
            var ids = $(grid_selector).getDataIDs();
            for (var i = 0; i < ids.length; i++) {
                $('#' + ids[i]).find("td").removeClass("SelectBG");
            }
            if (status) {
                var rowDatas = $(grid_selector).jqGrid('getRowData', rowid);
                djsjbdcdy_bdcdyh = rowDatas.BDCDYH;
                djsjbdcdy_id = rowid;
                $(grid_selector).jqGrid('setSelection', rowid, false);
                $('#' + rowid).find("td").addClass("SelectBG");
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}