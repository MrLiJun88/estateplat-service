<script type="text/javascript">
    function zydjTdTableInit() {
        var grid_selector = "#zydj-td-grid-table";
        var pager_selector = "#zydj-td-grid-pager";
        //resize to fit page size
      /*  $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });*/
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var gridRowNum=7;
        jQuery(grid_selector).jqGrid({
            //url: "${bdcdjUrl}/bdcSjgl/getGdTdJson",
            datatype: "local",
            height: '275px',
            jsonReader: {id: 'TDID'},
            colNames: ['序列', '权利人', "坐落", '土地证号'],
            colModel: [
                {name: 'XL', index: '', width: '9%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" onclick="zydjTdSel(\'' + rowObject.TDZH + '\',\'' + rowObject.TDID + '\')"/>'
                }
                },
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '30%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '30%', sortable: false},
                {name: 'TDZH', index: 'TDZH', width: '30%', sortable: false}
            ],
            viewrecords: true,
            rowNum: gridRowNum, /*
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
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if($(grid_selector).jqGrid("getRowData").length==gridRowNum){
                    $(grid_selector).jqGrid("setGridHeight","auto");
                }else{
                    $(grid_selector).jqGrid("setGridHeight","275px");
                }
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
</script>
<table id="zydj-td-grid-table"></table>
<div id="zydj-td-grid-pager"></div>
