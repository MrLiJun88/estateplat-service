<@com.html title="不动产登记业务管理系统" import="ace">
<script type="text/javascript">
    //选择土地证号
    function tdSel(tdzh,id){
        if(tdzh && tdzh!='undefined'){
            $("#tdzh").val(tdzh);
        }
        if(id && id!='undefined'){
            $("#tdid").val(id);
        }
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }

    $(function () {
        var grid_selector = "#td-grid-table";
        var pager_selector = "#td-grid-pager";
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
        var index = 0;
        var proid = $("#proid").val();
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/fwqlr/getFwqlrPagesJson?proid="+proid+"&bdcdyh=${bdcdyh!}",
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'proid'},
            colNames: [ '权利人', '权利人证件类型','权利人证件号'],
            colModel: [
                {name: 'QLR', index: 'QLR', width: '200', sortable: false},

                {name: 'QLRSFZJZL', index: 'QLRSFZJZL', width: '100', sortable: false},
                {name: 'QLRZJH', index: 'QLRZJH', width: '200', sortable: false}
            ],
            viewrecords: true,
//            rowNum: 10,
//            rowList:[10, 20, 30],
//            pager: pager_selector,
//            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                var page = $(grid_selector).getGridParam('page');
                index = (page-1)*7;
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if($(grid_selector).jqGrid("getRowData").length==10){
                    $(grid_selector).jqGrid("setGridHeight","auto");
                }else{
                    $(grid_selector).jqGrid("setGridHeight","300px");
                }
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    });
    //修改项目信息的函数
    function EditXm(id) {
        var proid='';
        if($("#proid").val()!=''){
            proid=$("#proid").val();
        }
    }
</script>
<style type="text/css">
    .ui-jqgrid-htable .ui-jqgrid-sortable{
        color:#000!important;

    }
</style>
<div class="main-container">
<input type="hidden" id="proid" value="${proid!}">
<div class="space-10"></div>
<table id="td-grid-table"></table>
<div id="td-grid-pager"></div>
</div>
</@com.html>