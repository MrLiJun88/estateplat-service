<script type="text/javascript">
    function zydjTableInit() {
        var grid_selector = "#zydj-grid-table";
        var pager_selector = "#zydj-grid-pager";
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
        jQuery(grid_selector).jqGrid({
            //url: "${bdcdjUrl}/bdcSjgl/getBdcBjxmListByPage",
            datatype: "local",
            height: 'auto',
            colNames: ['序列', '不动产权证号', "权利人", '坐落'],
            jsonReader:{id:'PROID'},
            colModel: [
                {name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="zydjXl" onclick="zydjSel(\'' + rowObject.BDCQZH + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.QLLX + '\',\'' + rowObject.XMMC + '\',\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYID + '\')"/>'
                }
                },
                {name: 'BDCQZH', index: 'BDCQZH', width: '30%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '30%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '30%', sortable: false}
            ],
            viewrecords: true,
            rowNum: 7, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if($(grid_selector).jqGrid("getRowData").length==7){
                    $(grid_selector).jqGrid("setGridHeight","100%");
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
<table id="zydj-grid-table"></table>
<div id="zydj-grid-pager"></div>
