<@com.html title="过渡归档信息" import="ace">
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
        padding-right: 0px;
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
        //时间控件
        $('.date-picker').datepicker({
            autoclose: true,
            todayHighlight: true,
            language: 'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });
        //生成表格
        accessTableGrid();
        //绑定回车键
        $('#search').keydown(function (event) {
            if (event.keyCode == 13) {
                $("#searchBtn").click();
            }
        });
        //保存该页所有的案卷号、目录号、案卷页数、案卷份数
        $('#saveInfo').click(function(){
            var array=[];
            //遍历该页的所有归档信息
            $("input[name='gdxxid']").each(function(i,n){
                var id = $(n).val();
                var ajh = $("#gdxxajh_"+id).val();
                var mlh = $("#gdxxmlh_"+id).val();
                var ajjs = $("#gdxxajjs_"+id).val();
                var ajys = $("#gdxxajys_"+id).val();
                var o = new Object();

                o.gdxxid = id;
                o.ajh = ajh;
                o.mlh = mlh;
                o.ajjs = ajjs;
                o.ajys = ajys;
                if(o.ajh != ""&&o.ajjs != ""&&o.ajys != ""&&o.mlh != ""){
                    array.push(o);
                }
            });
            if(array != "") {
                var postData = JSON.stringify(array);
                $.ajax({
                    type: "POST",
                    url: "${bdcdjUrl}/BdcGdxx/saveGdGdxxInfoForPl?postData=" + postData,
                    dataType: "json",
                    success: function (data) {
                        if (data.msg == "success") {
                            $.Prompt('保存成功',1500);
                            $('#access-grid-table').trigger("reloadGrid");
                        } else {
                            $.Prompt(data.msg,1500);
                        }
                    }
                });
            } else {
                bootbox.dialog({
                    message: "<h3><b>数据填写不完整!</b></h3>",
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
        });
        //批量修改
        $("#EditMulAll").click(function(){
            $('#MulsfSearchPop').show();
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

        //编辑弹出框打印档案封皮
        $("#print").click(function () {

            //避免未保存，打印之前先保存
            saveAjjsAndAjysDetail();
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
            printDafp(ids[0]);
        });
        //批量打印封皮
        function printAllDafp(gdxxids){
            var printUrl = "${serverUrl}/bdcPrint/printAllDafpByGdxxids?gdxxids=" + gdxxids+"&hiddeMode=false";
            window.location.href = "eprt:"+printUrl;
        }

        function isInArray(arr,value){
            for(var i = 0; i < arr.length; i++){
                if(value === arr[i]){
                    return true;
                }
            }
            return false;
        }

        function printDafp(gdxxid){
            var printUrl = "${serverUrl}/bdcPrint/printGdDafp?gdxxid=" + gdxxid+"&hiddeMode=false";
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
            var mlh = $("#mlh_ss").val();
            var ajhKs = $("#ajh_ss_ks").val();
            var ajhJs = $("#ajh_ss_js").val();
            var Url = "${bdcdjUrl}/BdcGdxx/getGdxxGdPagesJson";
            tableReload("access-grid-table", Url, {mlh:mlh,ajhKs: ajhKs, ajhJs: ajhJs});
        });


        //批量修改时动态获取目该录号下最大的案卷号
        $("#mulMlh").change(function(){
            var mulMlh = $("#mulMlh").val();
            $.ajax({
                url:'${bdcdjUrl}/BdcGdxx/getCurrentMaxAjhByMlh',
                type:'POST',
                data: {mlh: mulMlh, sfGd: "true"},
                dataType:'json',
                success: function (result) {
                    if(result.ajh && result.ajh!=null){
                        $("#mulAjh").val(result.ajh);
                    }
                }
            })
        })
    });

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
            datatype: "json",
            height: 'auto',
            jsonReader: {id: 'GDXXID'},
            colNames: ['档案号', '土地证号',  '坐落', '案卷号', '目录号', '案卷件数', '案卷页数', '归档状态', '打印档案封皮', '档案ID', 'GDXXID','XMID','AGDXXID'],
            colModel: [
                {name: 'DAH', index: 'DAH', width: '11%', sortable: false},
                {name: 'TDZH', index: 'TDZH', width: '20%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '18%', sortable: false},
                {name: 'AJH', index: 'AJH', width: '5%', sortable: false},
                {name: 'MLH', index: 'MLH', width: '5%', sortable: false},
                {name: 'AJJS', index: 'AJJS', width: '5%', sortable: false},
                {name: 'AJYS', index: 'AJYS', width: '5%', sortable: false},
                {name: 'GXZT', index: 'GXZT', width: '8%', sortable: false,
                   formatter: function (cellvalue, options, rowObject) {
                       if (rowObject.DAID && rowObject.DAID != "") {
                           return '<span class="label label-success">已归档</span>';
                       } else {
                           return '<span class="label label-warning">未归档</span>';
                       }
                   }
                },
                {
                    name: 'EDIT',
                    index: 'EDIT',
                    width: '10%',
                    sortable: false ,
                    formatter:function(cellvalue, options, rowObject){
                        return '<div style="margin-left:20px;"><div title="编辑信息"  style="float:left;cursor:pointer; margin-left: 10px;" class="ui-pg-div ui-inline-edit" id="" onclick="showSearchDialog(\'' + rowObject.GDXXID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg black"></span></div></div>'
                    }
                },
                {name: 'DAID', index: 'DAID', width: '0%', sortable: false},
                {name: 'GDXXID', index: 'GDXXID', width: '0%', sortable: false},
                {name: 'XMID', index: 'XMID', width: '0%', sortable: false},
                {name: 'AGDXXID',index:'AGDXXID',width:'0%',sortable:false,hidden:true,
                    formatter:function(cellvalue, options, rowObject){
                        return '<input type="hidden" name="gdxxid" value="'+cellvalue+'"style="height: 30px;width: 150px"/>';
                    }
                }
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
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 10) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "375px");
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                //异步加载表单所需的其他内容
                $.each(jqData, function (index, data) {
                    loadGdxxForTable($(grid_selector),data.GDXXID);
                });

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

    function loadGdxxForTable(table,rowid){
        var cellValue = '<span class="label label-warning">未归档</span>';
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/BdcGdxx/getBdcGdxxByGdxxid?gdxxid=" + rowid,
            success: function (result) {
                if(result&&result!=null) {
                    //组装可编辑案卷号、目录号、案卷件数和案卷页数
                    var ajhCell = '<input type="text" id="gdxxajh_'+ rowid +'" name="ajh" value="'+result.ajh+'"style="border: none;height: 25px;width: 150px"/>';
                    table.setCell(rowid, "AJH", ajhCell);
                    var mlhCell = '<input type="text" id="gdxxmlh_'+ rowid +'" name="mlh" value="'+result.mlh+'"style="border: none;height: 25px;width: 150px"/>';
                    table.setCell(rowid, "MLH", mlhCell);
                    table.setCell(rowid, "DAID", result.daid);
                    var ajjsCell = '<input type="text" id="gdxxajjs_'+ rowid +'" name="ajjs" value="'+result.ajjs+'"style="border: none;height: 25px;width: 150px"/>';
                    table.setCell(rowid, "AJJS", ajjsCell);
                    var ajysCell = '<input type="text" id="gdxxajys_'+ rowid +'" name="ajys" value="'+result.ajys+'"style="border: none;height: 25px;width: 150px"/>';
                    table.setCell(rowid, "AJYS", ajysCell);
                    if(result.daid != ""&&result.daid!="undefined"){
                        cellValue = '<span class="label label-success">已归档</span>';
                    }
                }
            }
        });
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

    function showSearchDialog(gdxxid){
        $.ajax({
            type:"GET",
            async:false,
            url:"${serverUrl}/BdcGdxx/getCurrentGdxxInfoGd?gdxxid="+gdxxid,
            success: function (data) {
                if(data && data!="undefined"){
                    $('#ajjs').val(data.ajjs);
                    $('#ajys').val(data.ajys);
                    $('#ajh').val(data.ajh);
                    $('#mlh').val(data.mlh);
                    $('#gdxxid_edit').val(data.gdxxid);
                }
            }
        });
        $('#sfSearchPop').show();
    }

    //单个页面修改
    function closeSearchDialog(){
        $('#sfSearchPop').hide();
        $('#ajjs').val('');
        $('#ajys').val('');
        $('#ajh').val('');
        $('#mlh').val('');
        $('#gdr').val('');
        $('#gdrq').val('');
    }



    function saveAjjsAndAjysDetail(){
        var ajjs = $('#ajjs').val();
        var ajys = $('#ajys').val();
        var ajh = $('#ajh').val();
        var mlh = $('#mlh').val();
        var gdxxid = $('#gdxxid_edit').val();
        $.ajax({
            type:"POST",
            url:"${serverUrl}/BdcGdxx/saveGdxxInfoGd",
            data:{gdxxid: gdxxid, ajjs: ajjs, ajys: ajys, ajh:ajh, mlh:mlh},
            success: function (data) {
                if(data && data!="undefined") {
                    $.Prompt(data.msg,1500);
                    //$('#sfSearchForm').reset();
                    $('#sfSearchPop').hide();
                    $('#access-grid-table').trigger("reloadGrid");
                }
            }
        })
    }


    //批量修改
    function saveMulDetail(){
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
        //防止编辑之后出现小于当前库里的ajh的值导致覆盖
        validateMaxAjh(ids);

    }

    function validateMaxAjh(ids){
        var pageAjh = $("#mulAjh").val();
        var pageMlh = $("#mulMlh").val();
        if(pageMlh!=null&&pageMlh!=""){
            $.ajax({
                url:'${bdcdjUrl}/BdcGdxx/getCurrentMaxAjhByMlh',
                type:'POST',
                data: {mlh: pageMlh, sfGd: "true"},
                dataType:'json',
                success: function (result) {
                    if(result.ajh && result.ajh!=null){
                        if(pageAjh < result.ajh - 1){
                            $.blockUI({ message:"请稍等……" });
                            bootbox.dialog({
                                message: "<h3><b>所填案卷号小于已存在最大案卷号，可能存在覆盖，是否继续？</b></h3>",
                                title:"",
                                closeButton:false,
                                buttons:{
                                    success:{
                                        label:"确定",
                                        className:"btn-success",
                                        callback:function () {
                                            setTimeout($.unblockUI, 10);
                                            //批量修改
                                            saveGdxxForPl(ids, pageMlh, pageAjh);
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
                            saveGdxxForPl(ids, pageMlh, pageAjh);
                        }
                    }
                }
            })
        }
    }

    function saveGdxxForPl(ids, mlh, ajh){
        var gdids = ids.join(",");
        $.ajax({
            type:"POST",
            async:false,
            url:"${serverUrl}/BdcGdxx/saveMulGdxxInfoGd",
            data:{gdids:gdids, mlh:mlh, ajh:ajh},
            success: function (data) {
                if(data && data!="undefined") {
                    $.Prompt(data.msg,1500);
                    $('#MulsfSearchPop').hide();
                    $('#mulAjh').val('');
                    $('#mulMlh').val('');
                    $('#access-grid-table').trigger("reloadGrid");
                }
            }
        })
    }

    //批量修改页面关闭
    function closeMulSearchDialog(){
        $('#MulsfSearchPop').hide();
        $('#mulAjh').val('');
        $('#mulMlh').val('');
    }

</script>
<div class="space-6"></div>
<!--jgrid表头编辑栏-->
<div class="main-container">
    <div class="page-content" id="accessContent">
        <div class="simpleSearch">
            <form class="form advancedSearchTable" id="gjSearchForm">
                <div class="row">
                    <div class="col-xs-2" style="width: 60px">
                        <label>目录号：</label>
                    </div>
                    <div class="col-xs-3">
                        <input type="text" name="mlh_ss" id="mlh_ss" class="form-control">
                    </div>
                    <div class="col-xs-1"style="width: 100px;padding-left: 30px">
                        <label>案卷号：</label>
                    </div>
                    <div class="col-xs-2">
                        <input type="text" name="ajh_ss_ks" id="ajh_ss_ks" class="form-control">
                    </div>
                    <div class="col-xs-1" style="width: 30px;">
                        <label>至</label>
                    </div>
                    <div class="col-xs-2">
                        <input type="text" name="ajh_ss_js" id="ajh_ss_js" class="form-control">
                    </div>
                    <div class="col-xs-1">
                        <button type="button" class="btn btn-sm btn-primary" id="searchBtn">搜索</button>
                    </div>
                </div>
            </form>
        </div>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="printDafp">
                        <i class="ace-icon fa fa-file"></i>
                        <span>打印档案封皮</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="saveInfo">
                        <i class="ace-icon fa fa-file"></i>
                        <span>保存</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="EditMulAll">
                        <i class="ace-icon fa fa-edit"></i>
                        <span>批量修改</span>
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
<div class="modal-backdrop fade in Pop" style="display:none;" id="modal-backdrop-pop"></div>
<!--编辑打印档案封皮的弹出框-->
<div class="Pop-upBox moveModel" style="display: none;" id="sfSearchPop">
    <div class="modal-dialog ywsjSearchPop-modal">
        <div class="modal-content" style="width: 80%">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>打印档案封皮</h4>
                <button type="button" id="searchHide"  onclick="closeSearchDialog()" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="sfSearchForm">
                    <div class="row" >
                        <div class="col-xs-2">
                            <label>案件卷数: </label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="ajjs" id ="ajjs" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>案件页数: </label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="ajys" id ="ajys" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>案卷号: </label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="ajh" id ="ajh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>目录号: </label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="mlh" id ="mlh" class="form-control">
                        </div>
                    </div>
                    <input type="text" name="gdxxid_edit" id ="gdxxid_edit" class="form-control" style="display: none">
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="save" onclick="saveAjjsAndAjysDetail()">保存</button>
                <button type="button" class="btn btn-sm btn-primary" id="print">打印</button>
            </div>
        </div>
    </div>
</div>
<!--批量修改弹出框-->
<div class="Pop-upBox moveModel" style="display: none;" id="MulsfSearchPop">
    <div class="modal-dialog ywsjSearchPop-modal">
        <div class="modal-content" style="width: 80%">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-edit bigger-110"></i>批量修改</h4>
                <button type="button" id="mulSearchHide"  onclick="closeMulSearchDialog()" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="mulsfSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>案卷号: </label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="ajh" id ="mulAjh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>目录号: </label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="mlh" id ="mulMlh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="save" onclick="saveMulDetail()">保存</button>
            </div>
        </div>
    </div>
</div>
</@com.html>