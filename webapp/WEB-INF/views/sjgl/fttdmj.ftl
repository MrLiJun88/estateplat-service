<@com.html title="" import="ace,public">
<style>
    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

        /*移动modal样式*/
    #lqSearchPop .modal-dialog, #cqSearchPop .modal-dialog, #fcSearchPop .modal-dialog, #tdSearchPop  .modal-dialog {
        width: 600px;
        position: fixed;
        top: 20px;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

        /*高级搜索的样式修改*/
    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

    .btn01 {
        display: inline-block;
        padding: 4px 12px;
        margin-bottom: 0;
        font-size: 14px;
        color: #333333;
        text-align: center;
        vertical-align: middle;
        cursor: pointer;
        background-color: #f2f2f2;
        border: 1px solid #aaa;
        webkit-border-radius: 0px !important;
        -moz-border-radius: 0px !important;
        border-radius: 0px !important;
    }

        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
    }

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }

    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
    }
</style>
<script type="text/javascript">
//table每页行数
$rownum = 10;
//table 每页高度
$pageHight = '370px';
//全局的不动产类型
$bdclx = 'TDFW';

$(function () {
    fwTableInit();
    var hhSearch = $("#fw_search_qlr").val();
    var fwUrl = "${bdcdjUrl}/getFttdmj/getFdcqZdJsonByPage?" + $("#fcSearchForm").serialize();
    tableReload("fw-grid-table", fwUrl, {hhSearch:hhSearch}, '', '');


    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        var contentWidth = $(".tab-content").width();
        $("#fw-grid-table").jqGrid('setGridWidth', contentWidth);
    });

    /*   文字水印  */
    $(".watermarkText").watermark();

    $('#fw_search_qlr').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#fw_search").click();
        }
    });
    //查询按钮点击事件
    $("#fw_search").click(function () {
        var hhSearch = $("#fw_search_qlr").val();
        var fwUrl = "${bdcdjUrl}/getFttdmj/getFdcqZdJsonByPage?" + $("#fcSearchForm").serialize();
        tableReload("fw-grid-table", fwUrl, {hhSearch:hhSearch}, '', '');
    })



})

function tableReload(table, Url, data, colModel, loadComplete) {
    var index = 0;
    var jqgrid = $("#" + table);
    if (colModel == '' && loadComplete == '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    } else if (loadComplete == '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel});
    } else if (loadComplete != '' && colModel != '') {
        jqgrid.setGridParam({
            url:Url,
            datatype:'json',
            page:1,
            postData:data,
            colModel:colModel,
            loadComplete:loadComplete
        });
    }
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

function jsFtmj(djh){
    //alert(djh);
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/getFttdmj/jsFttdmj?djh=" + djh,
        dataType:"json",
        success:function (result) {
            if(result=="成功"){
                jQuery("#fw-grid-table").collapseSubGridRow(djh);
            }else if(result=="失败"){
                alert("计算分摊面积失败，请检查宗地面积或者房产总面积是否为空");
            }

        },
        error:function (data) {
        }
    });
    setTimeout(function(){jQuery("#fw-grid-table").expandSubGridRow(djh);},200);

}

function fwTableInit() {
    var grid_selector = "#fw-grid-table";
    var pager_selector = "#fw-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'DJH'},
        colNames:['地籍号','坐落', '宗地宗海面积', '宗地宗海用途','计算分摊面积'],
        colModel:[
            {name:'DJH', index:'DJH', width:'10%', sortable:false},
            {name:'TDZL', index:'ZL', width:'15%', sortable:false},
            {name:'FZMJ', index:'ZDZHMJ', width:'5%', sortable:false},
            {name:'MC', index:'ZDZHYT', width:'10%', sortable:false},
            {name:'myac', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<div style="margin-left:8px;"> <div title="计算分摊面积" style="float:left;margin-left:40px;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="jsFtmj(\'' + rowObject.DJH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-columns  bigger-120 blue"></span></div>' +
                        '</div>'
            }
            }

        ],
        viewrecords:true,
        rowNum:$rownum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:true,
        multiselect:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");

        },
        editurl:"",
        caption:"",
        autowidth:true,
        subGrid:true,
        subGridOptions:{
            plusicon:"ace-icon fa fa-plus center bigger-110 blue",
            minusicon:"ace-icon fa fa-minus center bigger-110 blue",
            openicon:"ace-icon fa fa-chevron-right center orange"
        },
        subGridRowExpanded:function (subgridDivId, rowId) {
            var DJH = rowId;
            var subgridTableId = subgridDivId + "_t";
            $("#" + subgridDivId).html("<table id='" + subgridTableId + "'></table>");
            $("#" + subgridTableId).jqGrid({
                url:"${bdcdjUrl}/getFttdmj/getFdcqJsonByPage?djh=" + DJH,
                datatype:"json",
                height:250,
                colNames:['不动产单元号','房屋坐落', '规划用途', '建筑面积', '所在层', '总层数','分摊土地面积'],
                colModel:[
                    {name:'BDCDYH', index:'BDCDYH', width:'30%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                        var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                        return value;
                    }},
                    {name:'ZL', index:'FWZL', width:'30%', sortable:false},
                    {name:'MC', index:'GHYT', width:'16%', sortable:false},
                    {name:'JZMJ', index:'JZMJ', width:'12%', sortable:false},
                    {name:'SZC', index:'SZC', width:'12%', sortable:false},
                    {name:'ZCS', index:'ZCS', width:'12%', sortable:false},
                    {name:'FTTDMJ', index:'FTMJ', width:'12%', sortable:false}
                ],
                autowidth:true
            });
        }
    });
}


function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
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

</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="mainContent">

        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                               data-watermark="请输入地籍号/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" id="fw_search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                </tr>
            </table>
        </div>
        <table id="fw-grid-table"></table>
        <div id="fw-grid-pager"></div>
    </div>
</div>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
