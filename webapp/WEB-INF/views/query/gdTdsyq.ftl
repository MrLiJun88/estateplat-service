<@com.html title="不动产登记过渡土地信息查询" import="ace,public">
<style>
    a {
        color: #428bca;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }


    .alert {
        font-size: 12px;
        border-radius: 4px;
        padding: 5px;
        margin-bottom: 5px;
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

</style>
<script type="text/javascript">
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

        /*高级按钮点击事件 begin*/
        $("#djsjShow").click(function(){
            $("#gjSearchPop").show();
        });

        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/gdSyqcx/getGdTdsyqPageJson?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:""});
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        })
        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(window).trigger('resize.chosen');
            $(".modal-dialog").css({"_margin-left":"25%"});
        });
//        $(".gjSearchPop-modal").draggable({opacity: 0.7,handle: 'div.modal-header'});
        gdtdInitTable();
    });

    function gdtdInitTable(){
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
        <#--url:"${bdcdjUrl}/gdSyqcx/getGdTdsyqPageJson",-->
            datatype:"local",
            height:'auto',
            jsonReader:{id:'FWID'},
            colNames:["qlid",'tdid',"地籍号", '土地证号', '所有权人', '土地坐落'],
            colModel:[
                {name:'QLID', index:'QLID', width:'0%', sortable:false,hidden:true},
                {name:'TDID', index:'TDID', width:'0%', sortable:false,hidden:true},
                {name:'DJH', index:'DJH', width:'20%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cell = '<a href="javascript:glGdtd(\'${proid!}\',\'' + rowObject.DJH + '\',\'' + rowObject.QLID + '\',\'' + rowObject.TDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }},
                {name:'TDZH', index:'TDZH', width:'20%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cell = '<a href="javascript:glGdtd(\'${proid!}\',\'' + rowObject.DJH + '\',\'' + rowObject.QLID + '\',\'' + rowObject.TDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }},
                {name:'QLR', index:'QLR', width:'10%', sortable:false},
                {name:'TDZL', index:'TDZL', width:'20%', sortable:false}

            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:true,
//            rownumWidth:50,
            multiboxonly:false,
            multiselect:false,
            loadComplete:function () {
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
//                    asyncGetGdFwxx($(grid_selector),data.QLID,data.FWID);
//                    getQLR(data.QLID, $(grid_selector), data.FWID);
                });
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
//                    $(grid_selector).jqGrid('setGridWidth',  $(".page-content").width());
                }, 0);
//                alert($(grid_selector).jqGrid("getRowData").length);
                if ($(grid_selector).jqGrid("getRowData").length == 10) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "275px");
                }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
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
    function glGdtd(proid,djh,qlid,tdzh){
//        alert(proid+"$$$"+qlid+"$$$");
        if(qlid!=''){
            $.blockUI({ message:"请稍等……" });
            $.ajax({
                type:"get",
                url:"${bdcdjUrl!}/wfProject/glGdtd?proid=" + proid + "&qlid="+qlid,
                success:function (data) {

                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    if (data == '成功') {
                        window.parent.hideModel();
                        window.parent.resourceRefresh();
                    }
                },
                error:function (XMLHttpRequest, textStatus, errorThrown) {
                    if(XMLHttpRequest.readyState==4){
                        setTimeout($.unblockUI, 10);
                        alert("选择失败!");
                    }
                }
            });
        }
    }
    function serch(){
        var dcxc = $("#gdtd_search").val();
        var Url = "${bdcdjUrl}/gdSyqcx/getGdTdsyqPageJson";
        tableReload("grid-table", Url, {dcxc:dcxc});
    }


</script>

<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="page-content">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="gdtd_search" data-watermark="请输入土地证号/地籍号/权利人/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" id="gdtd_search_btn"  onclick="serch()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="djsjShow">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>

<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>所有权人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control" onclick="">
                        </div>

                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control" onclick="">
                        </div>

                        <div class="col-xs-2">
                            <label>土地坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>