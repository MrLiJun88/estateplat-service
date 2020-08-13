<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    .modal-dialog {
        width: 1000px;
    }

        /*高级搜索样式添加 begin*/
    .AdvancedSearchForm {
        position: absolute;
        top: 10px;
        left: 48px;
        z-index: 9999;
        display: none;
    }

    .Advanced .modal-backdrop {
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1;
        background-color: #000;
        opacity: 0.5;
        filter: alpha(opacity = 50);
        display: none;
    }

    .Advanced .AdvancedLab {
        display: block;
        margin: 0;
        background: #f5f5f5;
        font-size: 12px;
        border-top: 1px solid #ddd;
        border-left: 1px solid #ddd;
        border-right: 1px solid #ddd;
        padding: 0px 20px 10px 20px;
        position: absolute;
        top: -57px;
        left: 486px;
        z-index: 3;
        width: 90px;
        line-height: 25px;
    }

    .Advanced {
        position: relative;
        margin: 0px 0px 10px 0px;
    }

    .AdvancedSearchForm .form-base {
        padding: 20px 20px 20px 20px;
        border: 1px solid #ddd;
        background: #f5f5f5;
        width: 623px;
        position: absolute;
        top: -22px;
        left: -47px;
    }

    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }

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

        /*高级搜索样式添加 end*/

</style>
<script type="text/javascript">
    /*   文字水印  */
    $(function () {
        //搜索事件
        $(".search").click(function () {
alert(1);
            var dcxc = '';
            if ($("#search_xmmc").val() != '') {
                dcxc = $("#search_xmmc").val();
            }
            var zdzhh = '';
            if ($("#zdzhh").val() != '') {
                zdzhh = $("#zdzhh").val();
            }
            var zl = '';
            if ($("#zl").val() != '') {
                zl = $("#zl").val();
            }
            var dbr = '';
            if ($("#dbr").val() != '') {
                dbr = $("#dbr").val();
            }
            var qlr = '';
            if ($("#qlr").val() != '') {
                qlr = $("#qlr").val();
            }
            var Url = "${bdcdjUrl}/bdcDjb/getbdcDjbPagesJsonace"
            Url = Url + "?zdzhh=" + zdzhh + "&zl=" + zl + "&dbr=" + dbr +"&dcxc="+dcxc+"&qlr="+qlr;
            Url= encodeURI(Url);
            var jqgrid = $("#grid-table");
            jqgrid.setGridParam({url:Url, datatype:'json', page:1});
            jqgrid.trigger("reloadGrid");//重新加载JqGrid
        })


        $("#hide").click(function () {
            $(".SearchFloat").hide();
        });
        $("#show").click(function () {
            $(".SearchFloat").show();
        });
    });
    /* 调用子页面方法  */
    function showModal() {
        $('#myModal').show();
        $('#modal-backdrop').show();
    }
    function hideModal() {
        $('#myModal').hide();
        $('#modal-backdrop').hide();
        $("#myModalFrame").attr("src", "${bdcdjUrl!}/bdcSjgl/toAddBdcxm");
    }
    var onmessage = function (e) {
        showModal();
    };

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

    $(function () {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

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
            subGrid:true,
            subGridOptions:{
                plusicon:"ace-icon fa fa-plus center bigger-110 blue",
                minusicon:"ace-icon fa fa-minus center bigger-110 blue",
                openicon:"ace-icon fa fa-chevron-right center orange"
            },
            subGridRowExpanded:function (subgridDivId, rowId) {
                var djbid = rowId;
                var subgridTableId = subgridDivId + "_t";
                $("#" + subgridDivId).html("<table id='" + subgridTableId + "'></table>");
                $("#" + subgridTableId).jqGrid({
                    url:"${bdcdjUrl}/bdcDjb/getbdcdy?djbid=" + djbid,
                    datatype:"json",
                    jsonReader:{id:'bdcdyid'}, //zdd 指定行记录操作ID
                    height:250,
                    colNames:['不动产单元号', '不动产类型', '不动产权状态'],
                    colModel:[
                        {name:'bdcdyh', index:'bdcdyh', width:'20%',formatter:function (cellvalue, options, rowObject) {
                            var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                            return value;
                        }},
                        {name:'bdclx', index:'bdclx', width:'20%'},
                        {name:'bdczt', index:'bdczt', width:'20%'}
                    ],
                    gridComplete:function () {
                        var ids = jQuery("#" + subgridTableId).jqGrid('getDataIDs');//获取id数组
                        for (var i = 0; i < ids.length; i++) {
                            var xmNdjhSec = ids[i];
                            var edit = "<a href=\"javascript:editNdjh(\'" + xmNdjhSec + "\')\"><span class='label label-lg label-yellow arrowed-in'>修改</span></a>";
                            var del = "<a href='#' onclick=\"xmNdjhDel('" + xmNdjhSec + "')\"><span class='label label-lg label-purple arrowed'>删除</span></a>";
                            jQuery("#" + subgridTableId).jqGrid('setRowData', ids[i], {cz:edit + del});
                        }
                    },
                    autowidth:true
                });
            },
            url:"${bdcdjUrl}/bdcDjb/getbdcDjbPagesJsonace",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'djbid'}, //zdd 指定行记录操作ID
            colNames:[ '宗地/宗海', '登簿人', '坐落', '收件材料查看' ],
            colModel:[
                {name:'zdzhh', index:'zdzhh', width:'40%', sortable:false},
                {name:'dbr', index:'dbr', width:'30%', sortable:false},
                {name:'zl', index:'zl', width:'30%', sortable:false},
                {name:'myac', index:'', width:'5%', sortable:false,
                    formatter:function (cellvalue, options, rowObject) {
                        return '<a href="#" title="点击选择"><div style="margin-left:8px;"> <div title="点击选择" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.djbid  +'\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ui-icon ui-icon-pencil"></span></div></div></a>'
                    }}
            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:true,
            multiselect:false,
            /*rownumbers:true,*/
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
            },
            ondblClickRow:function (rowid) {
                EditXm(rowid);
            },
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
        $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+":this.getMonth() + 1, //月份
                "d+":this.getDate(), //日
                "h+":this.getHours(), //小时
                "m+":this.getMinutes(), //分
                "s+":this.getSeconds(), //秒
                "q+":Math.floor((this.getMonth() + 3) / 3), //季度
                "S":this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    });

    //修改项目信息的函数
    function EditXm(djbid) {
        window.open('${bdcdjUrl}/bdcDjb/djb?djbid='+djbid);
    }

</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput" id="search_xmmc">
                    </td>
                    <td class="Search">
                        <a href="#" class="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="Advanced">
            <div class="AdvancedSearchForm SearchFloat" style="display: none;">
                <h3 class="AdvancedLab">高级搜索</h3>

                <form class="form-base">
                    <div class="row-fluid">
                        <div class="col-xs-12">
                            <div class="HasOptional">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            坐落：
                                        </td>
                                        <td>
                                            <input type="text" class="SSinput" id="zl">
                                        </td>
                                        <td>
                                            权利人：
                                        </td>
                                        <td>
                                            <input type="text" class="SSinput" id="qlr">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            登簿人：
                                        </td>
                                        <td>
                                            <input type="text" class="SSinput" id="dbr">
                                        </td>
                                        <td>
                                            宗地宗海号：
                                        </td>
                                        <td >
                                            <input type="text" class="SSinput" id="zdzhh">
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid ">
                        <div class="span10 offset2">
                            <button type="submit" class="btn01 btn01-primary search"><i
                                    class="icon-search icon-white"></i>搜索
                            </button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-backdrop SearchFloat" id="hide" style="display: none;"></div>
        </div>

        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
</@com.html>
