<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    a {
        color: #428bca;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

        /*移动modal样式*/
    #djsjSearchPop .modal-dialog {
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

        /*移动modal样式*/
    #tipPop .modal-dialog {
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

        /*移动modal样式*/
    #ywsjSearchPop .modal-dialog {
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

        /*去掉表格横向滚动条*/
        /*.ui-jqgrid-bdiv{
            overflow-x: hidden!important;
        }*/

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

    $(document).keydown(function (event) {
        if (event.keyCode == 13) { //绑定回车
        <#--if ($("#djsjTab").html()!=null&&!($("#djsjTab").html().indexOf("undefined") > -1)) {-->
        <#--var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";-->
        <#--tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});-->
        <#--}-->
        <#--if ($("#ywsjTab").html()!=null&&!($("#ywsjTab").html().indexOf("undefined") > -1)) {-->
        <#--var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}";-->
        <#--tableReload("ywsj-grid-table", ywsjUrl, {dcxc:$("#ywsj_search").val()});-->
        <#--}-->
        }
    });

    $("#djsjTab,#ywsjTab,#qlxxTab").click(function () {
        if (this.id == "djsjTab") {
            $("#djsj").addClass("active");
            $("#ywsjHide").click();
            //var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";
            djsjInitTable();
            //tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});
        } else if (this.id == "ywsjTab") {
            $("#ywsj").addClass("active");
            $("#djsjHide").click();
            //var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}${proid}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
            ywsjInitTable();
            tableReload("ywsj-grid-table", ywsjUrl, {dcxc:$("#ywsj_search").val()});
        } else if (this.id == "qlxxTab") {
            $("#qlxx").addClass("active");
            $("#qlxxHide").click();
            //var qlxxUrl = "${bdcdjUrl}/selectBdcdy/getQlxxListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}${proid}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
            qlxxInitTable();
            //tableReload("qlxx-grid-table", ywsjUrl, {dcxc:$("#qlxx_search").val()});
        }
    })



    //搜索事件
    $("#djsj_search_btn").click(function () {
        var dcxc = $("#djsj_search").val();
        if(dcxc == "" || dcxc == null ||dcxc == undefined) {
            tipInfo("请输入权利人/坐落/不动产单元号");
        }else{
            var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";
            tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});
        }

    })
    $("#ywsj_search_btn").click(function () {
        var dcxc = $("#ywsj_search").val();
        if(dcxc == "" || dcxc == null ||dcxc == undefined) {
            tipInfo("请输入不动产权证号/权利人/坐落/不动产单元号");
        }else{
            var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
            tableReload("ywsj-grid-table", ywsjUrl, {dcxc:$("#ywsj_search").val()});
        }

    })
    $("#qlxx_search_btn").click(function () {
        var dcxc = $("#qlxx_search").val();
        if(dcxc == "" || dcxc == null ||dcxc == undefined) {
            tipInfo("请输入被查封权利人/坐落/不动产单元号/查封文号");
        }else{
            var qlxxUrl = "${bdcdjUrl}/selectBdcdy/getQlxxListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
            tableReload("qlxx-grid-table", qlxxUrl, {dcxc:$("#qlxx_search").val()});
        }

    })

    /*高级按钮点击事件 begin*/
    $("#djsjShow,#ywsjShow").click(function () {
        if (this.id == "ywsjShow") {
            $("#ywsjSearchPop").show();

        } else if (this.id == "djsjShow") {
            $("#djsjSearchPop").show();

        }
    });

    //单元号高级查询的搜索按钮事件
    $("#djsjGjSearchBtn").click(function () {
        var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc:""});
        $("#djsjSearchPop").hide();
        $("#djsjSearchForm")[0].reset();
    })

    //土地高级查询的搜索按钮事件
    $("#ywsjGjSearchBtn").click(function () {
        var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&qlxzdm=${qlxzdm!}&bdclxdm=${bdclxdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}&" + $("#ywsjSearchForm").serialize();
        tableReload("ywsj-grid-table", ywsjUrl, {dcxc:""});
        $("#ywsjSearchPop").hide();
        $("#ywsjSearchPop")[0].reset();

    })

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
    $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
    //默认初始化表格
    if ("${bdcdyly!}" == '2' || "${bdcdyly!}" == '0') {
        djsjInitTable();
    } else if ("${bdcdyly!}" == '1') {
        ywsjInitTable();
    } else if ("${bdcdyly!}" == '3') {
        qlxxInitTable();
    }
});
//地籍数据
function djsjInitTable() {
    var grid_selector = "#djsj-grid-table";
    var pager_selector = "#djsj-grid-pager";

    //绑定回车键
    $('#djsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });

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
        datatype:"local",
        height:'auto',
        jsonReader:{id:'ID'},
        colNames:["地籍号", '不动产单元号', '坐落', '权利人', '不动产类型', "ID"],
        colModel:[
            {name:'DJH', index:'DJH', width:'18%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '')
                    cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                else
                    cell = '';
                return cell;
            }},
            {name:'BDCDYH', index:'BDCDYH', width:'25%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '') {
                    cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                    cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                }
                else
                    cell = '';
                return cell;
            }},
            {name:'TDZL', index:'TDZL', width:'20%', sortable:false},
            {name:'QLR', index:'QLR', width:'10%', sortable:false},
            {name:'BDCLX', index:'BDCLX', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                var value = "";
                if (cellvalue != null && cellvalue != "") {
                    if (cellvalue.indexOf('TD') > -1) {
                        if (cellvalue.indexOf('FW') > -1) {
                            value = "土地、房屋等建筑物";
                        } else if (cellvalue.indexOf('GZW') > -1)
                            value = "土地、构筑物";
                        else if (cellvalue.indexOf('SL') > -1)
                            value = "土地、森林、林木";
                        else if (cellvalue.indexOf('QT') > -1)
                            value = "土地、其他";
                        else
                            value = "土地";
                    } else if (cellvalue.indexOf('HY') > -1) {

                        if (cellvalue.indexOf('FW') > -1) {
                            value = "海域、房屋等建筑物";
                        } else if (cellvalue.indexOf('GZW') > -1)
                            value = "海域、构筑物";
                        else if (cellvalue.indexOf('WJM') > -1)
                            value = "海域、无居民海岛";
                        else if (cellvalue.indexOf('SL') > -1)
                            value = "海域、森林、林木";
                        else if (cellvalue.indexOf('QT') > -1)
                            value = "海域、其他";
                        else
                            value = "海域";
                    } else if (cellvalue.indexOf('QT') > -1) {
                        value = "其他";
                    }
                }
                return value;
            }},
            {name:'ID', index:'ID', width:'10%', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:7,
        rowList:[7, 15, 20],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        rownumbers:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            qlrForTable(grid_selector, "${bdclxdm!}");
        }/*,
            ondblClickRow:function (rowid) {
                var rowData = $(grid_selector).getRowData(rowid);
                if(rowData!=null)
                    djsjEditXm(rowid,rowData.BDCLX,rowData.BDCDYH);
            }*/,
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}
//权利信息
function qlxxInitTable() {
    var grid_selector = "#qlxx-grid-table";
    var pager_selector = "#qlxx-grid-pager";
    $('#qlxx_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
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
        datatype:"local",
        height:'auto',
        jsonReader:{id:'PROID'},
        //colNames:["不动产单元号", '坐落', '类型', '查封机关','查封申请人','被查封申请人'],
        colNames:["不动产单元号", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间'],
        colModel:[
            {name:'BDCDYH', index:'BDCDYH', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '') {
                    cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                    cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                } else
                    cell = '';
                return cell;
            }},
            {name:'ZL', index:'ZL', width:'11%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '')
                    cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                else
                    cell = '';
                return cell;
            }},
            {name:'CFLX', index:'CFLX', width:'6%', sortable:false},
            {name:'CFJG', index:'CFJG', width:'6%', sortable:false},
            {name:'CFWH', index:'CFWG', width:'6%', sortable:false},
            {name:'CFSQR', index:'CFSQR', width:'6%', sortable:false},
            {name:'BCFQLR', index:'BCFQLR', width:'6%', sortable:false},
            {name:'CFKSQX', index:'CFKSQX', width:'6%', sortable:false, formatoptions:{newformat:'Y-m-d'}},
            {name:'CFJSQX', index:'CFJSQX', width:'6%', sortable:false, formatoptions:{newformat:'Y-m-d'}}
        ],
        viewrecords:true,
        rowNum:7,
        rowList:[7, 15, 20],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        rownumbers:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
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
//业务数据
function ywsjInitTable() {
    var grid_selector = "#ywsj-grid-table";
    var pager_selector = "#ywsj-grid-pager";
    $('#ywsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
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
        datatype:"local",
        height:'auto',
        jsonReader:{id:'PROID'},
        colNames:["不动产单元号", '不动产权证号', '坐落', '权利人'],
        colModel:[
            {name:'BDCDYH', index:'BDCDYH', width:'12%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '') {
                    cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                    cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                } else
                    cell = '';
                return cell;
            }},
            {name:'BDCQZH', index:'BDCQZH', width:'11%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (cellvalue != null && cellvalue != '')
                    cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                else
                    cell = '';
                return cell;
            }},
            {name:'ZL', index:'ZL', width:'10%', sortable:false},
            {name:'QLR', index:'QLR', width:'6%', sortable:false}
        ],
        viewrecords:true,
        rowNum:7,
        rowList:[7, 15, 20],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        rownumbers:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
        }/*,
            ondblClickRow:function (rowid) {
                var rowData = $(grid_selector).getRowData(rowid);
                if(rowData!=null)
                    ywsjEditXm(rowid,rowData.BDCDYH);
            }*/,
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

//修改项目信息的函数
function djsjEditXm(id, bdclx, bdcdyh) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    $.blockUI({ message:"请稍等……" });
    var options = {
        url:'${bdcdjUrl}/wfProject/checkBdcXm',
        type:'post',
        dataType:'json',
        data:{proid:proid, bdcdyh:bdcdyh, djId:id},
        success:function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            if (data.length > 0) {
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                $.each(data, function (i, item) {
                    var islw = false;
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                        }else {
                            $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                        }
                    }
                })
                if (!islw) {
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
            }
            if (alertSize == 0 && confirmSize == 0) {
                djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
            } else if (alertSize == 0 && confirmSize > 0) {
                $("span[name='hlBtn']").click(function () {
                    $(this).parent().remove();
                    if ($("#csdjConfirmInfo > div").size() == 0) {
                        djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                    }
                })
            }

        },
        error:function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        }
    };
    $.ajax(options);
}
function djsjInitVoFromOldData(proid, id, bdclx, bdcdyh) {
    $.ajax({
        type:'get',
        url:'${bdcdjUrl}/wfProjectMul/initVoFromOldData?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh,
        success:function (data) {

            if (data == '成功') {
                $.ajax({
                    type:'get',
                    async:true,
                    url:'${bdcdjUrl}/wfProject/updateWorkFlow?proid=' + proid,
                    success:function (data) {

                    }
                });
                window.parent.hideModel();
                window.parent.resourceRefresh();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            } else {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                alert(data);
            }
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.readyState == 4) {
                alert("保存失败!");
            }
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        }
    });
}
//修改项目信息的函数
function ywsjEditXm(id, bdcdyh, bdcdyid) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    var options = {
        url:'${bdcdjUrl}/wfProject/checkBdcXm',
        type:'post',
        dataType:'json',
        data:{proid:proid, yxmid:id, bdcdyh:bdcdyh},
        success:function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            if (data.length > 0) {
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                var alertCount = 0;
                $.each(data, function (i, item) {
                    if (item.checkModel == "alert") {
                        alertCount++;
                    }
                })
                var islw = false;
                $.each(data, function (i, item) {
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "confirmAndCreate") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                        }else {
                            $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                        }
                    }
                })
                if (!islw) {
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
            }
            if (alertSize == 0 && confirmSize == 0) {
                ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid);
            } else if (alertSize == 0 && confirmSize > 0) {
                $("span[name='hlBtn']").click(function () {
                    $(this).parent().remove();
                    if ($("#csdjConfirmInfo > div").size() == 0) {
                        ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid);
                    }
                })
            }
        },
        error:function (data) {

        }
    };
    $.ajax(options);
}
function ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid) {
    $.ajax({
        type:'get',
        url:'${bdcdjUrl}/wfProjectMul/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid,
        success:function (data) {

            if (data == '成功') {
                $.ajax({
                    type:'get',
                    async:true,
                    url:'${bdcdjUrl!}/wfProject/updateWorkFlow?proid=' + proid,
                    success:function (data) {

                    }
                });
                window.parent.hideModel();
                window.parent.resourceRefresh();

            } else {
                alert(data);
            }
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.readyState == 4) {
                alert("保存失败!");
            }
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
<#--function openProjectInfo(proid) {-->
    <#--if (proid && proid != undefined) {-->
        <#--window.open('${bdcdjUrl}/bdcSjgl/formTab?proid=' + proid + "&readOnly=" + true);-->
    <#--}-->
<#--}-->
function openProjectInfo(proid) {
    if (proid && proid != undefined) {
        $.ajax({
            url: "${bdcdjUrl}/qllxResource/getViewUrl?proid=" + proid,
            type: 'post',
            success: function (data) {
                if(data&& data != undefined){
                    openWin(data);
                }
            },
            error: function (data) {
                tipInfo("查看失败！");
            }
        });
    }
}
function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}
function createProjectInfo(yproid, sqlxdm, bdcdyh, bdcdyid, alertCount) {
    if (alertCount > 0) {
        alert("验证不通过，不能创建项目！");
        return false;
    }
    if (confirm("确定要创建项目吗？")) {
        $("#tipPop").hide();
        $("#modal-backdrop").hide();
        $.blockUI({ message:"请稍等……" });
        if (yproid && yproid != undefined) {
            var proid = $("#proid").val();
            var taskid;
            var url = window.parent.parent.window.location.href;

            if (url != null && url.indexOf("?taskid=") > -1)
                taskid = url.substr(url.indexOf("?taskid=") + 8, url.length);
            $.ajax({
                type:"GET",
                url:"${bdcdjUrl!}/wfProject/createLhcfByProid?proid=" + proid + "&yproid=" + yproid + "&createSqlxdm=" + sqlxdm + "&bdcdyh=" + bdcdyh + "&bdcdyid=" + bdcdyid,
                success:function (result) {
                    if (result != null && result != "" && taskid != null && taskid != "") {
                        $.ajax({
                            type:"post",
                            url:"${platformUrl!}/task!del.action?taskid=" + taskid,
                            success:function (data) {
                                if (data.indexOf("true") > -1 || data.indexOf("1") > -1) {
                                    window.parent.parent.window.location.href = "${portalUrl!}/taskHandle?taskid=" + result;
                                }
                            }
                        });
                    }

                }
            });

        }
    }

}
//zdd 此处用意是在前台页面加载权利人   提高后台SQL相应相率   但是经过测试未必有慢的情况，并且导致权利人查询出现问题  所以暂时去掉
//为表格添加权利人列数据
function qlrForTable(grid_selector, bdclxdm) {
//    var jqData = $(grid_selector).jqGrid("getRowData");
//    var rowIds = $(grid_selector).jqGrid('getDataIDs');
//    $.each(jqData, function (index, data) {
//        getQlrByDjid(data.ID, $(grid_selector), rowIds[index], bdclxdm);
//    })
}
//获取权利人
function getQlrByDjid(djid, table, rowid, bdclxdm) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + bdclxdm,
        success:function (result) {
            var qlr = result.qlr;
            if (qlr == null || qlr == "undefined")
                qlr = "";
            var cellVal = "";
            cellVal += '<span>' + qlr + '</span>';
            table.setCell(rowid, "QLR", cellVal);
        }
    });
}
</script>
<div class="main-container">
<input type="hidden" id="proid" value="${proid!}">
<div class="page-content">
<div class="row">
<div class="tabbable">
<ul class="nav nav-tabs">
    <#if bdcdyly==2>
        <li class="active">
            <a data-toggle="tab" id="djsjTab" href="#djsj">
                不动产单元
            </a>
        </li>
        <li>
            <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                产权证
            </a>
        </li>
    <#else>
        <#if bdcdyly==0>
            <li class="active">
                <a data-toggle="tab" id="djsjTab" href="#djsj">
                    不动产单元
                </a>
            </li>
        </#if>
        <#if bdcdyly==1>
            <li class="active">
                <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                    产权证
                </a>
            </li>
        </#if>
        <#if bdcdyly==3>
            <li class="active">
                <a data-toggle="tab" id="qlxxTab" href="#qlxx">
                    查封信息
                </a>
            </li>
        </#if>
    </#if>
</ul>
<div class="tab-content">
    <#if bdcdyly==2>
    <div id="djsj" class="tab-pane in active">
    <#else>
        <#if bdcdyly==0>
        <div id="djsj" class="tab-pane in active">
        </#if>
        <#if bdcdyly==1>
        <div id="djsj" class="tab-pane">
        </#if>
        <#if bdcdyly==3>
        <div id="djsj" class="tab-pane">
        </#if>
    </#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="djsj_search" data-watermark="请输入权利人/坐落/不动产单元号">
                </td>
                <td class="Search">
                    <a href="#" id="djsj_search_btn">
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
    <table id="djsj-grid-table"></table>
    <div id="djsj-grid-pager"></div>
</div>
    <#if bdcdyly==2>
    <div id="ywsj" class="tab-pane">
    <#else>
        <#if bdcdyly==0>
        <div id="ywsj" class="tab-pane">
        </#if>
        <#if bdcdyly==1>
        <div id="ywsj" class="tab-pane in active">
        </#if>
        <#if bdcdyly==3>
        <div id="ywsj" class="tab-pane">
        </#if>
    </#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="ywsj_search"
                           data-watermark="请输入不动产权证号/权利人/坐落/不动产单元号">
                </td>
                <td class="Search">
                    <a href="#" id="ywsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="ywsjShow">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <table id="ywsj-grid-table"></table>
    <div id="ywsj-grid-pager"></div>
</div>
    <#if bdcdyly==2>
    <div id="qlxx" class="tab-pane">
    <#else>
        <#if bdcdyly==0>
        <div id="qlxx" class="tab-pane">
        </#if>
        <#if bdcdyly==1>
        <div id="qlxx" class="tab-pane">
        </#if>
        <#if bdcdyly==3>
        <div id="qlxx" class="tab-pane in active">
        </#if>
    </#if>
    <div id="qlxx" class="tab-pane">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                    <#--<input type="text" class="SSinput watermarkText" id="qlxx_search" data-watermark="权利人/坐落/不动产单元号">-->
                        <input type="text" class="SSinput watermarkText" id="qlxx_search"
                               data-watermark="请输入被查封权利人/坐落/不动产单元号/查封文号">
                    </td>
                    <td class="Search">
                        <a href="#" id="qlxx_search_btn">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                </tr>
            </table>
        </div>
        <table id="qlxx-grid-table"></table>
        <div id="qlxx-grid-pager"></div>
    </div>
</div>
</div>
</div>
</div>
</div>
<!--地籍高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="djsjSearchPop">
    <div class="modal-dialog djsjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="djsjHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="djsjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="bdclx" class="form-control">
                                <#list bdclxList! as bdclx>
                                    <option value="${bdclx.DM!}">${bdclx.MC}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>土地坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="djsjGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--业务高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="ywsjSearchPop">
    <div class="modal-dialog ywsjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>业务高级查询</h4>
                <button type="button" id="ywsjHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="ywsjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label> 土地坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>不动产权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcqzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="ywsjGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo"></div>
                <div id="csdjConfirmInfo"></div>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
