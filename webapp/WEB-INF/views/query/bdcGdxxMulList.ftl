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
            $("input[name='proid']").each(function(i,n){
                var id = $(n).val();
                var ajh = $("#gdxxajh_"+id).val();
                var mlh = $("#gdxxmlh_"+id).val();
                var ajjs = $("#gdxxajjs_"+id).val();
                var ajys = $("#gdxxajys_"+id).val();
                var o = new Object();
                o.xmid = id;
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
                    url: "${bdcdjUrl}/BdcGdxxMul/saveGdxxInfoForPl?postData=" + postData,
                    dataType: "json",
                    success: function (data) {
                        if (data.msg == "success") {
                            alert('保存成功');
                            $('#access-grid-table').trigger("reloadGrid");
                        } else {
                            alert(data.msg);
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

            //打印单个封皮
            if(ids.length==1){
                printDafp(ids);
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
            } else {
                printDafp(ids);
            }
        });
        //批量打印封皮
        function printAllDafp(proids){
            var printUrl = "${serverUrl}/bdcPrint/printAllDafpByProids?proids=" + proids +"&hiddeMode=false";
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
            if(search == "" || search == null ||search == undefined) {
                tipInfo("请输入受理编号/统编号");
            }else{
                $("#gjSearchForm")[0].reset();
                var Url = "${bdcdjUrl}/BdcGdxxMul/getGdxxPagesJson";
                tableReload("access-grid-table", Url, {searchText: search});
            }
        });
        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/BdcGdxxMul/getGdxxPagesJson?" + $("#gjSearchForm").serialize();
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
        //动态获取目该录号下最大的案卷号
        $("#mulMlh").change(function(){
            var mulMlh = $("#mulMlh").val();
            $.ajax({
                url:'${bdcdjUrl}/BdcGdxxMul/getCurrentMaxAjhByMlh?mlh='+mulMlh,
                type:'GET',
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
            jsonReader: {id: 'PROID'},
            colNames: ['编号', '不动产权证号', '不动产单元号', '坐落','案卷号','目录号','案卷件数','案卷页数', '归档人', '归档日期', '归档状态','打印档案封皮',<#if "${showUpFileButton!}"=="true">'上传附件',</#if> '档案ID','统编号','WIID','PROID','APROID'],
            colModel: [
                {name: 'BH', index: 'BH', width: '11%', sortable: false},
                {name: 'BDCQZH', index: 'BDCQZH', width: '20%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '18%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '18%', sortable: false},
                {name: 'AJH', index: 'AJH', width: '5%', sortable: false},
                {name: 'MLH', index: 'MLH', width: '5%', sortable: false},
                {name: 'AJJS', index: 'AJJS', width: '5%', sortable: false},
                {name: 'AJYS', index: 'AJYS', width: '5%', sortable: false},
                {name: 'GDR', index: 'GDR', width: '7%', sortable: false,hidden:true},
                {
                    name: 'GDRQ',
                    index: 'GDRQ',
                    width: '8%',
                    sortable: false,
                    hidden:true,
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
                    name: 'GDZT',
                    index: 'GDZT',
                    width: '8%',
                    sortable: false
                },
                {
                    name: 'EDIT',
                    index: 'EDIT',
                    width: '10%',
                    sortable: false ,
                    formatter:function(cellvalue, options, rowObject){
                        return '<div style="margin-left:20px;"><div title="编辑信息"  style="float:left;cursor:pointer; margin-left: 10px;" class="ui-pg-div ui-inline-edit" id="" onclick="showSearchDialog(\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg black"></span></div></div>'
                    }
                },
                <#if "${showUpFileButton!}"="true">
                                {
                                    name: 'upFile',
                                    index: '',
                                    width: '7%',
                                    sortable: false,
                                    formatter: function (cellvalue, options, rowObject) {
                                        return '<div style="margin-left:20px;"><div title="上传附件"  style="float:left;cursor:pointer; margin-left: 10px;" class="ui-pg-div ui-inline-edit" id="" onclick="upFileForDA(\'' + rowObject.PROID + '\',\'' + rowObject.WIID  + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg black"></span></div></div>'
                                    }
                                },
                </#if>
                {name: 'DAID', index: 'DAID', width: '0%', sortable: false},
                {name: 'DBH', index: 'DBH', width: '0%', sortable: false},
                {name: 'WIID', index: 'WIID', width: '0%', sortable: false},
                {name: 'PROID', index: 'PROID', width: '0%', sortable: false},
                {name: 'APROID',index:'APROID',width:'0%',sortable:false,hidden:true,
                    formatter:function(cellvalue, options, rowObject){
                        return '<input type="hidden" name="proid" value="'+cellvalue+'"style="height: 30px;width: 150px"/>';
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
                    loadDataForTable(data.PROID,$(grid_selector),data.PROID);
                    loadGdxxForTable(data.PROID,$(grid_selector),data.PROID);
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
    function loadDataForTable(proid,table,rowid){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/BdcGdxxMul/getBdcqxxByProid?proid=" + proid,
            success: function (result) {
                table.setCell(rowid, "ZL", result.zl);
                table.setCell(rowid, "BDCQZH", result.bdcqzh);
                table.setCell(rowid, "BDCDYH", result.bdcdyh);
            }
        });
    }

    function loadGdxxForTable(proid,table,rowid){
        var cellValue = '<span class="label label-warning">未归档</span>';
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/BdcGdxxMul/getBdcGdxxByProid?proid=" + proid,
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
                    table.setCell(rowid,"GDZT",cellValue);
                    table.setCell(rowid, "GDR", result.gdr);
                    table.setCell(rowid, "GDRQ", result.gdrq);
                }
            }
        });
    }

    function upFileForDA(proid, wiid){
        upFile("",proid,wiid);
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

    function showSearchDialog(proid){
        $.ajax({
            type:"GET",
            async:false,
            url:"${serverUrl}/BdcGdxxMul/getCurrentGdxxInfo?proid="+proid,
            dataType:"json",
            success: function (data) {
                if(data && data!="undefined"){
                    $('#ajjs').val(data.ajjs);
                    $('#ajys').val(data.ajys);
                    $('#ajh').val(data.ajh);
                    $('#mlh').val(data.mlh);
                }
            }
        });
        if(proid == "undefined"){
            proid = "";
        }
        $('#xmid').val(proid);
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

    //批量修改页面关闭
    function closeMulSearchDialog(){
        $('#MulsfSearchPop').hide();
        $('#mulAjh').val('');
        $('#mulMlh').val('');
    }


    function saveAjjsAndAjysDetail(){
        $.ajax({
            type:"POST",
            url:"${serverUrl}/BdcGdxxMul/saveGdxxInfo",
            data:$("#sfSearchForm").serialize(),
            dataType:"json",
            success: function (data) {
                if(data && data!="undefined") {
                    alert(data.msg);
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
                url:'${bdcdjUrl}/BdcGdxxMul/getCurrentMaxAjhByMlh?mlh='+pageMlh,
                type:'GET',
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
                                            saveGdxxForPl(ids);
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
                            saveGdxxForPl(ids);
                        }
                    }
                }
            })
        }
    }

    function saveGdxxForPl(ids){
        $.ajax({
            type:"POST",
            url:"${serverUrl}/BdcGdxxMul/saveMulGdxxInfo?ids=" + ids,
            data:$("#mulsfSearchForm").serialize(),
            dataType:"json",
            success: function (data) {
                if(data && data!="undefined") {
                    alert(data.msg);
                    $('#MulsfSearchPop').hide();
                    $('#mulAjh').val('');
                    $('#mulMlh').val('');
                    $('#access-grid-table').trigger("reloadGrid");
                }
            }
        })
    }
</script>
<div class="space-6"></div>
<!--jgrid表头编辑栏-->
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
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
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
<!--高级搜索弹出框-->
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
                    <div class="row" >
                        <div class="rowLabel col-xs-2">
                            <label>归档人：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                            <input type="text" name="gdr" class="form-control">
                        </div>
                        <div class="rowLabel col-xs-2">
                            <label>归档日期：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                            <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="gdrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="rowLabel col-xs-2">
                            <label>目录号：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                            <input type="text" name="mlh" class="form-control">
                        </div>
                        <div class="rowLabel col-xs-2">
                            <label>归档结果：</label>
                        </div>
                        <div class="rowContent col-xs-3">
                            <select name="isgd" class="form-control">
                                <option value="" selected>请选择</option>
                                <option value="a">未归档</option>
                                <option value="b">已归档</option>
                            </select>
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
                        <div class="col-xs-2" style="display: none">
                            <input type="text" name="xmid" id ="xmid" class="form-control">
                        </div>
                        <div class="col-xs-2" style="display: none">
                            <input type="text" name="daid" id ="daid" class="form-control">
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