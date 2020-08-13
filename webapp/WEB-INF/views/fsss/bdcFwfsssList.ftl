<@com.html title="不动产登记业务管理系统" import="ace,public">
<script type="text/javascript">
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
            url: "${bdcdjUrl}/bdcFwfsss/getBdcFwFsssJson?wiid=${wiid!}"+"&bdcdyid=${bdcdyid!}"+"&proid=${proid!}" ,
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'FWFSSSID'},
            colNames: [ '房屋坐落', '不动产单元号','建筑面积','操作'],
            colModel: [
                {name: 'FWZL', index: 'FWZL', width: '200', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '180', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '80', sortable: false},
                {name:'mydy', index:'', width:'40', sortable:false, formatter:function (cellvalue, options, rowObject) {
                //zdd 不允许打印证书 return '<div style="margin-left:8px;"> <div title="打印" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-print fa-lg blue"></span></div>&nbsp;&nbsp;<div title="查看收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div></div>'
                return '<div style="margin-left:8px;">' +
                    '<div title="查看详情" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="ckxj(\'' + rowObject.FWFSSSID + '\',\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-file-text fa-lg red"></span></div>'
                    '</div>'}
        }
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
    function ckxj(fwfsssid,bdcdyh){
        var url = "${reportUrl}/ReportServer?reportlet=edit%2Fbdc_fwfsss.cpt&op=write&fwfsssid=" + fwfsssid + "&bdcdyh=" + bdcdyh ;
        openWin(url)
    }
</script>
<style type="text/css">
    .ui-jqgrid-htable .ui-jqgrid-sortable{
        color:#000!important;

    }
</style>
<div class="main-container">
    <input type="hidden" id="wiid" value="${wiid!}">
    <input type="hidden" id="bdcdyid" value="${bdcdyid!}">
<div class="space-10"></div>
<table id="td-grid-table"></table>
<div id="td-grid-pager"></div>
</div>
</@com.html>