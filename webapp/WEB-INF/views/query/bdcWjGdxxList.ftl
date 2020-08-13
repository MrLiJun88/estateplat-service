<@com.html title="归档信息" import="ace">
<style>
    .new-modal {
        width: 600px;
    }

    .modal-dialog {
        width: 650px;
        margin: 30px auto;
    }

    .bootbox {
        overflow: auto;
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
<script>
    var formUrl = "${formUrl!}";
    var reportUrl = "${reportUrl!}";
    var serverUrl = "${serverUrl!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl = "${path_platform!}";
    var showUpFileButton = "${showUpFileButton!}";
    $(function () {
        //生成表格
        accessTableGrid();
        $("#getResult").click(function () {
            var ids = $('#access-grid-table').jqGrid('getGridParam', 'selarrrow');
            if (ids.length == 0) {
                bootbox.dialog({
                    message: "<h3><b>请选择至少一条数据!</b></h3>",
                    title: "",
                    buttons: {
                        main: {
                            label: "关闭",
                            className: "btn-primary",
                        }
                    }
                });
                return;
            }else if(ids.length == 1){
                var count = 0;
                for (var k = 0; k < ids.length; k++) {
                    var rowData = $('#access-grid-table').jqGrid("getRowData", ids[k]);//根据上面的id获得本行的所有数据
                    var val = rowData.DAID;
                    if (val && val != 'undefined' && val != 'undefined') {
                        count++;
                    }
                }
                if(count==1){
                    checkIsGd(ids);
                    return;
                }else if(count==0){
                    $.blockUI({ message:"请稍等……" });
                    bootbox.dialog({
                        message: "<h3><b>您所选归档信息包含归档内容，是否继续归档？</b></h3>",
                        title:"",
                        closeButton:false,
                        buttons:{
                            success:{
                                label:"确定",
                                className:"btn-success",
                                callback:function () {
                                    setTimeout($.unblockUI, 10);
                                    //单个归档
                                    gdOne(ids);
                                }
                            },
                            main:{
                                label:"取消",
                                className:"btn-primary",
                                callback:function () {
                                    setTimeout($.unblockUI, 10);
                                }
                            }
                        }
                    });
                    return;

                }
            };

            var counts = 0;
            for (var k = 0; k < ids.length; k++) {
                var rowData = $('#access-grid-table').jqGrid("getRowData", ids[k]);//根据上面的id获得本行的所有数据
                var val = rowData.DAID;
                if (val && val != 'undefined' && val != 'undefined') {
                    counts++;
                }
            }

            if (counts!=0 && ids.length > 1) {
                $.blockUI({ message:"请稍等……" });
                bootbox.dialog({
                    message: "<h3><b>您所选归档信息用包含归档内容，是否继续归档？</b></h3>",
                    title:"",
                    closeButton:false,
                    buttons:{
                        success:{
                            label:"确定",
                            className:"btn-success",
                            callback:function () {
                                setTimeout($.unblockUI, 10);
                                //归档
                                gd(ids);
                            }
                        },
                        main:{
                            label:"取消",
                            className:"btn-primary",
                            callback:function () {
                                setTimeout($.unblockUI, 10);
                            }
                        }
                    }
                });
            }else{
                //归档
                gd(ids);
            }
        });

        //打印档案封皮
        $("#printDafp").click(function () {
            var ids = $('#access-grid-table').jqGrid('getGridParam', 'selarrrow');
            if (ids.length == 0) {
                bootbox.dialog({
                    message: "<h3><b>请选择至少一条数据!</b></h3>",
                    title: "",
                    buttons: {
                        main: {
                            label: "关闭",
                            className: "btn-primary",
                        }
                    }
                });
                return;
            }
            //打印封皮
//            for (var k = 0; k < ids.length; k++) {
//                var id = ids[k];//根据上面的id获得本行的所有数据
//                printAllDafp(id);
//            }
            //打印单个封皮
            if(ids.length==1){
                printDafp(ids[0]);
            }
            if(ids.length>1){
                printAllDafp(ids);
            }
        });

        function printAllDafp(proids){
            var printUrl = "${serverUrl}/bdcPrint/printAllDafp?proids=" + proids+"&hiddeMode=false";
            window.location.href = "eprt:"+printUrl;
        }

        function printDafp(proid){
            var printUrl = "${serverUrl}/bdcPrint/printDafp?proid=" + proid+"&hiddeMode=false";
            window.location.href = "eprt:"+printUrl;
        }

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth;
            if ($("#accessContent").width() > 0) {
                contentWidth = $("#accessContent").width();
            }
            $("#access-grid-table").jqGrid('setGridWidth', contentWidth);
        });
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

        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        });
        $(".modal-header").mouseout(function () {
            $(this).css("cursor", "default");
        });
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

        //项目表搜索事件
        $("#searchBtn").click(function () {
            var search = $("#search").val();
            $("#gjSearchForm")[0].reset();
            var Url = "${bdcdjUrl}/BdcWjGdxx/getWjGdxxPagesJson?" + $("#gjSearchForm").serialize();
            tableReload("access-grid-table", Url, {searchText: search});
        });
        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/BdcWjGdxx/getWjGdxxPagesJson?" + $("#gjSearchForm").serialize();
            tableReload("access-grid-table", Url, {searchText: ""});
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
        });
        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
    })

    //归档功能
    function gd(ids){
        $.blockUI({message: "归档中，请稍等…"});
        var options = {
            url: '${bdcdjUrl}/BdcWjGdxx/bdcXmGd?proids=' + ids,
            type: 'get',
            dataType: 'json',
            success: function (result) {
                if (result != '') {
                    $.Prompt(result,1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                } else {
                    $.Prompt("归档失败，请检查档案系统并重试",1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                }
            },
            error: function (result) {
                if(result.status==200){
                    if (result.responseText != '') {
                        $.Prompt(result.responseText,1500);
                        $('#access-grid-table').trigger("reloadGrid");
                        setTimeout($.unblockUI, 10);
                    } else {
                        $.Prompt("归档失败，请检查档案系统并重试",1500);
                        $('#access-grid-table').trigger("reloadGrid");
                        setTimeout($.unblockUI, 10);
                    }
                }else{
                    $.Prompt("归档失败，请重试",1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                }
            }
        };
        $.ajax(options);
    }

    var isGd = true;
    //判断是否已经归档
    function checkIsGd(ids){
        var options = {
            url: '${bdcdjUrl}/BdcWjGdxx/checkBdcXmIsGd?proid=' + ids,
            type: 'get',
            dataType: 'json',
            success: function (result) {
                if (result != '') {
                    $.Prompt(result,1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                } else {
                    isGd=false;
                }
            },
            error: function (result) {
                if(result.status==200){
                    if (result.responseText != '') {
                        $.Prompt(result.responseText,1500);
                        $('#access-grid-table').trigger("reloadGrid");
                        setTimeout($.unblockUI, 10);
                    } else {
                        $.Prompt("网络出现问题",1500);
                        $('#access-grid-table').trigger("reloadGrid");
                        setTimeout($.unblockUI, 10);
                    }
                }else{
                    $.Prompt("网络出现问题",1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                }
            }
        };
        $.ajax(options);
    }


    //归档功能(单个归档)
    function gdOne(ids){
        $.blockUI({message: "归档中，请稍等…"});
        var options = {
            url: '${bdcdjUrl}/BdcWjGdxx/bdcXmGdOne?proid=' + ids,
            type: 'get',
            dataType: 'json',
            success: function (result) {
                if (result != '') {
                    $.Prompt(result,1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                } else {
                    $.Prompt("归档失败，请检查档案系统并重试",1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                }
            },
            error: function (result) {
                if(result.status==200){
                    if (result.responseText != '') {
                        $.Prompt(result.responseText,1500);
                        $('#access-grid-table').trigger("reloadGrid");
                        setTimeout($.unblockUI, 10);
                    } else {
                        $.Prompt("归档失败，请检查档案系统并重试",1500);
                        $('#access-grid-table').trigger("reloadGrid");
                        setTimeout($.unblockUI, 10);
                    }
                }else{
                    $.Prompt("归档失败，请重试",1500);
                    $('#access-grid-table').trigger("reloadGrid");
                    setTimeout($.unblockUI, 10);
                }
            }
        };
        $.ajax(options);
    }

    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function tipInfo(msg) {
        // bootbox.dialog({
        //     message: "<h3><b>" + msg + "</b></h3>",
        //     title: "",
        //     buttons: {
        //         main: {
        //             label: "关闭",
        //             className: "btn-primary"
        //         }
        //     }
        // });
        // return;
        $.Prompt(msg,1500);
    }
    //auth表格初始化
    function accessTableGrid() {
        var grid_selector = "#access-grid-table";
        var pager_selector = "#access-grid-pager";
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            //url: "${bdcdjUrl}/BdcWjGdxx/getGdxxPagesJson",
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'PROID'},
            colNames: ['编号', '不动产权证号', '不动产单元号', '坐落','案卷号','目录号', '归档人', '归档日期', '归档状态',<#if "${showUpFileButton!}"=="true">'上传附件',</#if> '档案ID','PROID','统编号'],
            colModel: [
                {name: 'BH', index: 'BH', width: '15%', sortable: false},
                {name: 'BDCQZH', index: 'BDCQZH', width: '20%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '20%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
                {name: 'AJH', index: 'AJH', width: '5%', sortable: false},
                {name: 'MLH', index: 'MLH', width: '5%', sortable: false},
                {name: 'GDR', index: 'GDR', width: '6%', sortable: false},
                {
                    name: 'GDRQ',
                    index: 'GDRQ',
                    width: '6%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (!cellvalue) {
                            return "";
                        }
                        var value = cellvalue;
                        var data = new Date(value).Format("yyyy-MM-dd");
                        return data;
                    }
                },
                {
                    name: '',
                    index: '',
                    width: '6%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (rowObject.DAID && rowObject.DAID != "") {
                            return '<span class="label label-success">已归档</span>';
                        } else {
                            return '<span class="label label-warning">归档失败</span>';
                        }
                    }
                },
                <#if "${showUpFileButton!}"="true">
                    {
                        name: 'upFile',
                        index: '',
                        width: '7%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            return '<div style="margin-left:20px;"><div title="上传附件"  style="float:left;cursor:pointer; margin-left: 10px;" class="ui-pg-div ui-inline-edit" id="" onclick="upFileForDA(\'' + rowObject.WIID + '\',\'' + rowObject.BH + '\',\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg black"></span></div></div>'
                        }
                    },
                </#if>
                {name: 'DAID', index: 'DAID', width: '0%', sortable: false},
                {name: 'PROID', index: 'PROID', width: '0%', sortable: false},
                {name: 'JJDBH', index: 'JJDBH', width: '0%', sortable: false}
            ],
            viewrecords: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            pagerpos: "left",
            pager: pager_selector,
            altRows: false,
            multiboxonly: true,
            multiselect: true,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth', $("#accessContent").width());
                }, 0);
                //判断是否为统编号查询结果，若是通过统编号查询出来的结果，默认全选
                var is_tbh_search = "true";
                var search = $("#search").val();
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    // 异步加载表单所需产权证
                    asycAccessBdcXxByProid($(grid_selector),data.PROID);
                    if(data.JJDBH != search){
                        is_tbh_search = "false";
                        return false;
                    }
                });
                if(is_tbh_search=="true" && (search!=""&&search!=undefined&&search!=null)){
                    $("#cb_access-grid-table").click();
                }

            },
            ondblClickRow: function (rowid) {
            },
            caption: "",
            autowidth: true
        });
        Date.prototype.Format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds()             //毫秒
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    }
    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }
    //var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');


    function upFileForDA(wiid,bh,proid){
        upFile("",proid,wiid);
    }

    function asycAccessBdcXxByProid(table,proid){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl!}/BdcWjGdxx/asycAccessBdcXxByProid?proid=" +proid,
            success: function (result) {
                table.setCell(proid, "BDCQZH", result.bdcqzh);
                table.setCell(proid, "ZL", result.zl);
                table.setCell(proid, "BDCDYH", result.bdcdyh);
            }
        });
    }

    // 批量上传
    function upFile(clmc,proid,wiid) {
        $.ajax({
            type: 'post',
            url: formUrl + '/bdcdjSlxx/creatSjcl',
            data: {proid: proid, wiid: wiid},
            success: function (data) {
                var w_width = screen.availWidth - 21;
                var w_height = screen.availHeight - 47;
                var url = formUrl + "/createSjdcl/createAllFileFolder?proid=" + proid + "&clmc=";

                var title = "收件单材料";

                var windowDia = window.showModalDialog(url, title, "dialogHeight=" + w_height + "px;dialogWidth= " + w_width + "px");

                if (windowDia == 'ok') {
                    var data = {proid: proid};
                    $.ajax({
                        type: 'post',
                        url: formUrl + '/bdcdjSjdxx/saveSjclFs',
                        data: data,
                        success: function (data) {
                            window.close();
                            location.reload();//刷新父页面，并关闭子页面。
                        },
                        error: function (_ex) {

                        }
                    });

                }
            },
            error: function (_ex) {

            }
        });
    }
</script>
<div class="space-6"></div>
<div class="main-container">
    <div class="page-content" id="accessContent">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search" data-watermark="请输入编号/统编号">
                    </td>
                    <td class="Search">
                        <a href="#" id="searchBtn">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="display: none">
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="getResult">
                        <i class="ace-icon fa fa-file"></i>
                        <span>归档</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="printDafp">
                        <i class="ace-icon fa fa-file"></i>
                        <span>打印档案封皮</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="access-grid-table"></table>

        <div id="access-grid-pager"></div>

    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
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
                <#--<div class="row">-->
                <#--<div class="rowLabel col-xs-2">-->
                <#--<label>项目名称：</label>-->
                <#--</div>-->
                <#--<div class="rowContent col-xs-4">-->
                <#--<input type="text" name="xmmc" class="form-control">-->
                <#--</div>-->
                <#--<div class="rowLabel col-xs-2">-->
                <#--<label>坐落：</label>-->
                <#--</div>-->
                <#--<div class="rowContent col-xs-4">-->
                <#--<input type="text" name="zl" class="form-control">-->
                <#--</div>-->
                <#--</div>-->
                    <#list gdxxGjssOrderList as gdxxGjss>
                        <#if gdxxGjss == 'bdcdyh'>
                            <#if (gdxxGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="rowLabel col-xs-2">
                                <label>不动产单元号：</label>
                            </div>
                            <div class="rowContent col-xs-4">
                                <input type="text" name="bdcdyh" class="form-control">
                            </div>
                            <#if (gdxxGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   gdxxGjss=='bdcqzh'>
                            <#if (gdxxGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="rowLabel col-xs-2">
                                <label>不动产权证号：</label>
                            </div>
                            <div class="rowContent col-xs-4">
                                <input type="text" name="bdcqzh" class="form-control">
                            </div>
                            <#if (gdxxGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   gdxxGjss=='gdjg'>
                            <#if (gdxxGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="rowLabel col-xs-2">
                                <label>归档结果：</label>
                            </div>
                            <div class="rowContent col-xs-4">
                                <select name="isgd" class="form-control">
                                    <option value="" selected>请选择</option>
                                    <option value="a">未归档</option>
                                    <option value="b">已归档</option>
                                </select>
                            </div>
                            <#if (gdxxGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   gdxxGjss=='zl'>
                            <#if (gdxxGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="rowLabel col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="rowContent col-xs-4">
                                <input type="text" name="zl" class="form-control">
                            </div>
                            <#if (gdxxGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>
                        </#if>
                    </#list>


                <#--
                <div class="row">
                    <div class="rowLabel col-xs-2">
                        <label>起始日期：</label>
                    </div>
                    <div class="rowContent col-xs-4">
                    <span class="input-icon">
                         <input type="text" class="date-picker form-control" name="qsrq"
                                data-date-format="yyyy-mm-dd">
                        <i class="ace-icon fa fa-calendar"></i>
                    </span>
                    </div>
                    <div class="rowLabel col-xs-2">
                        <label>结束日期：</label>
                    </div>
                    <div class="rowContent col-xs-4">
                    <span class="input-icon">
                         <input type="text" class="date-picker form-control" name="jsrq"
                                data-date-format="yyyy-mm-dd">
                        <i class="ace-icon fa fa-calendar"></i>
                    </span>
                    </div>
                </div>-->
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none;" id="modal-backdrop-pop"></div>
</@com.html>