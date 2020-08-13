<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    .page-header {
        margin: 0px;
        padding: 0px;
        text-align: center;
        border-bottom: 0px;
    }

    .page-content {
        padding: 0px 12px 24px;
    }

    body {
        background-color: #FFFFFF;
    }

    .headTab {
        position: fixed;
        top: 0px;
    }

    li.ui-menu-item.active  a, .headTab li.active a {
        background-color: #408fc6 !important;
        color: #fff !important;
    }

    li.ui-menu-item.active  a {
        background-color: #408fc6 !important;
        color: #fff !important;
    }

    .imgClass {
        max-width: 1000px;
        myimg: expression(onload=function(){
            this.style.width=(this.offsetWidth > 500)?"500px":"auto"}
        );
    }
</style>
<script type="text/javascript">
$rownum = 8;
//table 每页高度
$pageHight = '320px';
//全局的不动产类型
$bdclx = 'fw';
//定义公用的基础colModel
fwColModel = [
    {name:'FWZL', index:'FWZL', width:'30%', sortable:false},
    {name:'JZMJ', index:'JZMJ', width:'14%', sortable:false},
    {name:'GHYT', index:'GHYT', width:'16%', sortable:false},
    {name:'SZC', index:'SZC', width:'9%', sortable:false},
    {name:'ZCS', index:'ZCS', width:'9%', sortable:false},
    {
        name: 'operate', index: '', width: '5%', sortable: false,
        formatter: function (cellvalue, options, rowObject) {
            return '<div style="margin-left:7px;">' +
                    '<div  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" ' +
                    'onclick="viewQlFwTdInfo(\'' + rowObject.FWID + '\',\'' + "FW" + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" ' +
                    'onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                    '<span class="fa fa-search fa-lg blue"></span>' +
                    '</div></div>'
        }
    }

];
tdColModel = [
    {name:'ZL', index:'ZL', width:'30%', sortable:false},
    {name:'ZDMJ', index:'ZDMJ', width:'8%', sortable:false},
    {name:'YT', index:'YT', width:'20%', sortable:false},
    {name:'QSXZ', index:'QSXZ', width:'15%', sortable:false},
    {name:'SYQLX', index:'SYQLX', width:'10%', sortable:false},
    {name:'DW', index:'DW', width:'10%', sortable:false},
    {
        name: 'operate', index: '', width: '5%', sortable: false,
        formatter: function (cellvalue, options, rowObject) {
            return '<div style="margin-left:7px;">' +
                    '<div  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" ' +
                    'onclick="viewQlFwTdInfo(\'' + rowObject.TDID + '\',\'' + "TD" + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" ' +
                    'onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                    '<span class="fa fa-search fa-lg blue"></span>' +
                    '</div></div>'
        }
    }
];
dyhLoadComplete = function () {
    var table = this;
    setTimeout(function () {
    }, 0);
    //清空
    $("#bdcdyh").val("");
    //如果10条设置宽度为auto,如果少于7条就设置固定高度
    if ($(table).jqGrid("getRowData").length == $rownum) {
        $(table).jqGrid("setGridHeight", "auto");
    } else {
        $(table).jqGrid("setGridHeight", $pageHight);
    }
    //去掉遮罩
    setTimeout($.unblockUI, 10);
};
$(function () {
    //下拉框  含搜索的
    $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据"});
    var width = $(window).width() / 2;
    if (width < 795) {
        width = 795;
    }
    var height = $(window).height() - 20;
    $("#ace-settings-box").css("width", width).css("height", height);
    $(window).resize(function () {
        var width = $(window).width() / 2;
        if (width < 795) {
            width = 795;
        }
        var height = $(window).height() - 20;
        $("#ace-settings-box").css("width", width).css("height", height);
    })
    fwTableInit();
    tdTableInit();
    fwtdTableInit();
    qlTableInit();
    fwtdqlTableInit();
    var fwUrl = "${bdcdjUrl}/gdXxLr/getGdFwAllJson";
    tableReload("fw-grid-table", fwUrl, {proid:'${proid!}',wiid:'${wiid!}'}, fwColModel, '');
    var tdUrl = "${bdcdjUrl}/gdXxLr/getGdTdJson";
    tableReload("td-grid-table", tdUrl, {proid:'${proid!}',wiid:'${wiid!}'}, tdColModel, '');
    var qlUrl = "${bdcdjUrl}/gdXxLr/getGdQlJson";
    tableReload("ql-grid-table", qlUrl, {proid:'${proid!}'}, '', '');
});
function fwTableInit() {
    var grid_selector = "#fw-grid-table";
    var pager_selector = "#fw-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', 800);
        }
    });
    jQuery(grid_selector).jqGrid({
        url:"",
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'FWID'},
        colNames:[ '坐落', '建筑面积', '规划用途', '所在层', '总层数',"查看"],
        colModel:fwColModel,
        viewrecords:true,
        rowNum:$rownum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
                $(grid_selector).jqGrid('setGridWidth', 800);
            } else {
                $(grid_selector).jqGrid('setGridWidth', 800);
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            var jqDataIDs = $(grid_selector).jqGrid("getDataIDs");
            var fwids = jqDataIDs.join(",");
            var fwTdUrl = "${bdcdjUrl}/gdXxLr/getGdFwTdJson";
            tableReload("fwtd-grid-table", fwTdUrl, {fwids: fwids}, '', '');
            var fwTdQlUrl = "${bdcdjUrl}/gdXxLr/getGdFwTdQlJson";
            tableReload("fwtdql-grid-table", fwTdQlUrl, {fwids: fwids}, '', '');
        },
        ondblClickRow:function (rowid, index) {
            $("#fwid").val(rowid);
            var radionTemp = document.getElementsByName("fwXl")[index - 1];
            radionTemp.checked = true;
            fwSel(rowid);
        },

        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}

function tdTableInit() {
    var grid_selector = "#td-grid-table";
    var pager_selector = "#td-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', 800);
        }
    });
    jQuery(grid_selector).jqGrid({
        url:"",
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'TDID'},
        colNames:[ '坐落', '宗地面积', '用途', '权属性质', '使用权类型','单位',"查看"],
        colModel:tdColModel,
        viewrecords:true,
        rowNum:$rownum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
                $(grid_selector).jqGrid('setGridWidth', 800);
            } else {
                $(grid_selector).jqGrid('setGridWidth', 800);
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
        },
        /*ondblClickRow:function (rowid, index) {
            $("#fwid").val(rowid);
            var radionTemp = document.getElementsByName("fwXl")[index - 1];
            radionTemp.checked = true;
            fwSel(rowid);
        },*/
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}

function getGdQlr(qlr, table, rowid) {
    $.ajax({
        url: "${bdcdjUrl}/gdXxLr/getGdQlr",
        type: "GET",
        data: {qlid: rowid},
        success: function (result) {
            qlr = '<span>' + result + '</span>';
            table.setCell(rowid, "QLR", qlr);
        },
        error: function () {
        }
    });
}

function viewQlFwTdInfo(infoId, cpt) {
    var url = "${bdcdjUrl!}/gdXxLr/gdQlFwTdInfo?infoId=" + infoId;
    var msg = "信息";
    if (cpt == "QL") {
        url += "&info=QL";
        msg = "权利" + msg;
    } else if (cpt == "FW") {
        url += "&info=FW";
        msg = "房屋" + msg;
    } else if (cpt == "TD") {
        url += "&info=TD";
        msg = "土地" + msg;
    }
    showIndexModel(url, msg, "1000", "650", false);
}

function qlTableInit() {
    var grid_selector = "#ql-grid-table";
    var pager_selector = "#ql-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', 800);
        }
    });
    var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        url:"${bdcdjUrl}/gdXxLr/getGdQlJsonAll?proid=${proid!}"+"&wiid=${wiid!}",
        datatype:"json",
        height:$pageHight,
        jsonReader:{id:'QLID'},
        colNames: ['权利类型', '业务类型', "权利人", "查看", "QLID"],
        colModel:[
            {name:'QLLX', index:'QLLX', width:'15%', sortable:false},
            {name:'DJLX', index:'DJLX', width:'25%', sortable:false},
            {name:'QLR', index:'QLR', width:'25%', sortable:false},
            {name: 'operate', index: '', width: '5%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:7px;">' +
                            '<div  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" ' +
                            'onclick="viewQlFwTdInfo(\'' + rowObject.QLID + '\',\''+"QL"+'\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" ' +
                            'onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                            '<span class="fa fa-search fa-lg blue"></span>' +
                            '</div></div>'
                }
            },
            {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords:true,
        rowNum:gridRowNum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        ondblClickRow:function (rowid, index) {
            $.ajax({
                type:"GET",
                url:"${bdcdjUrl}/gdXxLr/getGdqllx?qlid=" + rowid,
                dataType:"json",
                success:function (result) {
                    if (result != null && result != '')
                        tdSel(rowid, result);
                }
            });

            var radionTemp = document.getElementsByName("tdXl")[index - 1];
            radionTemp.checked = true;
        },
        loadComplete:function () {
            var table = this;
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length < gridRowNum) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
                $(grid_selector).jqGrid('setGridWidth', 800);
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
                $(grid_selector).jqGrid('setGridWidth', 800);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getGdQlr(data.QLR, $(grid_selector), data.QLID);
            });
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}

function fwtdqlTableInit() {
    var grid_selector = "#fwtdql-grid-table";
    var pager_selector = "#fwtdql-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', 800);
        }
    });
    var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        url:"",
        datatype:"json",
        height:$pageHight,
        jsonReader:{id:'QLID'},
        colNames: ['权利类型', '业务类型', "权利人", "查看", "QLID"],
        colModel:[
            {name:'QLLX', index:'QLLX', width:'15%', sortable:false},
            {name:'DJLX', index:'DJLX', width:'25%', sortable:false},
            {name:'QLR', index:'QLR', width:'25%', sortable:false},
            {name: 'operate', index: '', width: '5%', sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:7px;">' +
                            '<div  style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" ' +
                            'onclick="viewQlFwTdInfo(\'' + rowObject.QLID + '\',\''+"QL"+'\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" ' +
                            'onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">' +
                            '<span class="fa fa-search fa-lg blue"></span>' +
                            '</div></div>'
                }
            },
            {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true}
        ],
        viewrecords:true,
        rowNum:gridRowNum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        ondblClickRow:function (rowid, index) {
            $.ajax({
                type:"GET",
                url:"${bdcdjUrl}/gdXxLr/getGdqllx?qlid=" + rowid,
                dataType:"json",
                success:function (result) {
                    if (result != null && result != '')
                        tdSel(rowid, result);
                }
            });

            var radionTemp = document.getElementsByName("tdXl")[index - 1];
            radionTemp.checked = true;
        },
        loadComplete:function () {
            var table = this;
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length < gridRowNum) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
                $(grid_selector).jqGrid('setGridWidth', 800);
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
                $(grid_selector).jqGrid('setGridWidth', 800);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getGdQlr(data.QLR, $(grid_selector), data.QLID);
            });
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}

function fwtdTableInit() {
    var grid_selector = "#fwtd-grid-table";
    var pager_selector = "#fwtd-grid-pager";
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', 800);
        }
    });
    jQuery(grid_selector).jqGrid({
        url:"",
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'TDID'},
        colNames:[ '坐落', '宗地面积', '用途', '权属性质', '使用权类型','单位',"查看"],
        colModel:tdColModel,
        viewrecords:true,
        rowNum:$rownum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:false,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
                $(grid_selector).jqGrid('setGridWidth', 800);
            } else {
                $(grid_selector).jqGrid('setGridWidth', 800);
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
        },
        /*ondblClickRow:function (rowid, index) {
            $("#fwid").val(rowid);
            var radionTemp = document.getElementsByName("fwXl")[index - 1];
            radionTemp.checked = true;
            fwSel(rowid);
        },*/
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}

$(window).on('resize.jqGrid', function () {
    var contentWidth = $(".tab-content").width();
    $("#fw-grid-table,#td-grid-table,#ql-grid-table").jqGrid('setGridWidth', 800);
});
$(".watermarkText").watermark();
function tableReload(table, Url, data, colModel, loadComplete) {
    var index = 0;
    var jqgrid = $("#" + table);
    if (colModel == '' && loadComplete == '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    } else if (loadComplete == '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel});
    } else if (loadComplete != '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel, loadComplete:loadComplete});
    }
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
<!--   自适应高度    -->
$(document).ready(function () {
    loadLeftMenu();
    $("body").attr("data-spy", "scroll").attr("data-target", ".bs-docs-sidebar");
});

$(window).resize(function () {
    encodeURI()
    loadLeftMenu();
});

function loadLeftMenu() {
    var winHeight = $(window).height();
    var leftMenuHieght = 0;
    if (winHeight > 0) {
        leftMenuHieght = winHeight - 40;
    }
    $(".content").css({height:leftMenuHieght + "px"});
}
<!--end   自适应高度    -->
</script>
<div class="main-container">
    <div class="page-content" id="mainContent" align="center">
        <div class="row">


            <div id="zd">
                <#--<div class="bs-docs-sidebar" style="position: fixed;top: 15px;right:20px">
                    <ul id="menu"
                        class=" nav  bs-docs-sidenav affix-top ui-menu ui-widget ui-widget-content ui-corner-all">

                        &lt;#&ndash;<li class="ui-menu-item">&ndash;&gt;
                            &lt;#&ndash;<a href="#xmxx" class="ui-corner-all" tabindex="-1" role="menuitem">&ndash;&gt;
                                &lt;#&ndash;<#if "${bdclx!}"=="fw">项目信息</#if>&ndash;&gt;
                                &lt;#&ndash;<#if "${bdclx!}"=="td">土地信息</#if></a>&ndash;&gt;
                        &lt;#&ndash;</li>&ndash;&gt;
                        <li class="ui-menu-item">
                            <a href="#qlxx" class="ui-corner-all" tabindex="-1" role="menuitem"> 权利信息</a>
                        </li>
                        <#if "${bdclx!}"=="fw">
                            <li class="ui-menu-item">
                                <a href="#fwxx" class="ui-corner-all" tabindex="-1" role="menuitem">房屋信息</a>
                            </li>
                        </#if>
                        <#if "${bdclx!}"=="td">
                            <li class="ui-menu-item">
                                <a href="#tdxx" class="ui-corner-all" tabindex="-1" role="menuitem">土地信息</a>
                            </li>
                        </#if>
                    </ul>
                </div>-->
                <div align="center" style="width: 800px">

                    <#--<section id="xmxx">-->
                        <#--<#if "${bdclx!}"=="td">-->
                            <#--<iframe id='xmiframe'-->
                                    <#--src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&editFlag=${editFlag!}&tdid=${proid!}"-->
                                    <#--frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"-->
                                    <#--allowtransparency="yes" style="width: 680px;height:470px"></iframe></#if>-->
                        <#--<#if "${bdclx!}"=="fw">-->
                            <#--<iframe id='xmiframe'-->
                                    <#--src="${reportUrl!}/ReportServer?reportlet=edit%2Fgd_xm.cpt&op=write&editFlag=${editFlag!}&proid=${proid!}"-->
                                    <#--frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no"-->
                                    <#--allowtransparency="yes" style="width: 685px;height:250px"></iframe></#if>-->
                    <#--</section>-->
                    <section id="qlxx">
                        <div class="page-header">
                            <h1>权利信息</h1>
                        </div>
                        <table id="ql-grid-table" width="800px"></table>
                        <div id="ql-grid-pager"></div>
                    </section>
                    <#if "${bdclx!}"=="fw">
                        <section id="fwxx">
                            <div class="page-header">
                                <h1>房屋信息 </h1>
                            </div>
                            <table id="fw-grid-table" align="center"></table>
                            <div id="fw-grid-pager" align="center"></div>
                        </section>
                        <section id="fwtdqlxx">
                            <div class="page-header">
                                <h1>土地权利信息</h1>
                            </div>
                            <table id="fwtdql-grid-table" width="800px"></table>
                            <div id="fwtdql-grid-pager"></div>
                        </section>
                        <section id="fwtdxx">
                            <div class="page-header">
                                <h1>土地信息 </h1>
                            </div>
                            <table id="fwtd-grid-table" align="center"></table>
                            <div id="fwtd-grid-pager" align="center"></div>
                        </section>
                    </#if>
                    <#if "${bdclx!}"=="td">
                        <section id="tdxx">
                            <div class="page-header">
                                <h1>土地信息 </h1>
                            </div>
                            <table id="td-grid-table" align="center"></table>
                            <div id="td-grid-pager" align="center"></div>
                        </section>
                        </#if>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
</div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--end  面板操作  -->
</@com.html>
