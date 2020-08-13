<@com.html title="证明单信息列表" import="ace">
<style>
    /*移动modal样式*/
    #logSearchPop .modal-dialog {
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

    /*
     zwq 解决帆软的样式和页面上的jqgrid的下标的冲突
    */
    .ui-state-disabled {
        width: 10%;
        opacity:1;
        color: #BBB;
    }

    .distance {
        width: 10px;
        border: 0px !important;
    }

    .print {
        border: 0px !important;
        padding-left: 20px;
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

    pre, input[type="text"] {
        padding: 0px !important;
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

    .form .row .col-xs-4, .col-xs-10 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

    /*日期表单样式*/
    .dropdown-menu {
        z-index: 10000 !important;
    }

    .input-icon {
        width: 100%;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }
    .modal-content{
        width:400px;
    }
    .modal-dialog .modal-content .form .row {
        margin: 10px 0px 0px 50px;
    }

    .tab-content {
        overflow-x: hidden;
    }

</style>

<script type="text/javascript">

    $(function () {

        //初始化证明权利信息列表
        zmsjInitTable();
        /*   文字水印  */
        $(".watermarkText").watermark();
        //查询按钮点击事件
        $("#zmSearch").click(function () {
            var searchInfo = $("#zmsj_search").val();
            var workflowProid = "${workflowProid!}";
            var zmsjUrl = "${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=2" + "&bdcdyh=" + searchInfo;
            tableReload("zmsj-grid-table", zmsjUrl, {username:searchInfo});
        })

        $("#zsSearch").click(function () {
            var searchInfo = $("#zssj_search").val();
            var workflowProid = "${workflowProid!}";
            var zssjUrl = "${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=1" + "&bdcdyh=" + searchInfo;
            tableReload("zssj-grid-table", zssjUrl, {username:searchInfo});
        })


        var width = $(window).width() / 2;
        if (width < 1000) {
            width = 1000;
        }
        var height = $(window).height() - 20;
        $("#ace-settings-box").css("width", width).css("height", height);
        $(window).resize(function () {
            var width = $(window).width() / 2;
            if (width < 1000) {
                width = 1000;
            }
            var height = $(window).height() - 20;
            $("#ace-settings-box").css("width", width).css("height", height);
        })

        //定位
        $("#ace-settings-btn").click(function () {
            if ($("#ace-settings-box").hasClass("open")) {
                setTimeout(function () {
                    $("#iframe").attr("src", $("#iframeSrcUrl").val())
                }, 500);
            }
        })
        //日志表高级查询的搜索按钮事件
    <#--$("#logSearchBtn").click(function () {-->
    <#--var Url = "${bdcdjUrl}/bdcSjgl/getBdcXtLogListByPage?" + $("#logSearchForm").serialize();-->
    <#--tableReload("log-grid-table", Url, {username:""});-->
    <#--})-->
        //日志高级搜索关闭事件
//    $("#proHide").click(function () {
//        $("#logSearchPop").hide();
//        $("#logSearchForm")[0].reset();
//    });
        //日志高级查询按钮点击事件
//    $("#logShow").click(function () {
//        $("#logSearchPop").show();
//    });
        //时间控件
//    $('.date-picker').datepicker({
//        autoclose:true,
//        todayHighlight:true,
//        language:'zh-CN'
//    }).next().on(ace.click_event, function () {
//        $(this).prev().focus();
//    });

        /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
//    var ua = navigator.userAgent.toLowerCase();
//    if (window.ActiveXObject){
//        if(ua.match(/msie ([\d.]+)/)[1]=='8.0'){
//            $(window).resize(function(){
//                $.each($(".moveModel > .modal-dialog"),function(){
//                    $(this).css("left",($(window).width()-$(this).width())/2);
//                    $(this).css("top","40px");
//                })
//            })
//        }
//    }

        //拖拽功能
//    $(".modal-header").mouseover(function () {
//        $(this).css("cursor", "move");//改变鼠标指针的形状
//    })
//    $(".modal-header").mouseout(function () {
//        $(".show").css("cursor", "default");
//    })
//    $(".logSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth;
            if ($("#mainContent").width() > 0) {
                contentWidth = $("#mainContent").width();
            }
            $("#zmsj-grid-table").jqGrid('setGridWidth', contentWidth);
            $("#zssj-grid-table").jqGrid('setGridWidth', contentWidth);
        });


        $("#zmDataTab,#zsDataTab").click(function () {
            var workflowProid = "${workflowProid!}";
            if (this.id == "zmDataTab") {
                $("#zmData").addClass("active");
                var zmsjUrl = "${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=2" + "&bdcdyh=" + $("#zmsj_search").val();
                zmsjInitTable();
                tableReload("zmsj-grid-table", zmsjUrl, {username: $("#zmsj_search").val()});
            } else if (this.id == "zsDataTab") {
                $("#zsData").addClass("active");
                var zssjUrl = "${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=1" + "&bdcdyh=" + $("#zssj_search").val();
                zssjInitTable();
                tableReload("zssj-grid-table", zssjUrl, {username: $("#zssj_search").val()});
            }
        })


        $("#tzprint").click(function () {
            var wiid = "${wiid!}";
            var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_spfsc_list.cpt&op=write&from=task&wiid=" + wiid;
            window.open(url, "表单台账");
        })

        $("#zmplprint").click(function () {

            /*var grid_selector = "#log-grid-table";
            var proid = "${workflowProid!}";
        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/fraJax/getProidList?proid="+proid,
            success:function (result) {
                var p = [];
                var printurl = "${reportUrl}/ReportServer";
                $.each(result,function(index,item){
                    //打印所有页
                    p.push("{reportlet: '/print/mul/bdc_fwcq_zmd.cpt', proid : '" + item + "',bdcdyid=''}");

                })
                //将参数值组成的数组转化为字符串
                var rp = p.join(",");
                //使用FineReport自带的方法cjkEncode进行转码
                var reportlets = FR.cjkEncode("[" + rp + "]");

                var config = {
                    url:printurl,
                    isPopUp:true,
                    data:{
                        reportlets:reportlets
                    }
                };
                FR.doURLFlashPrint(config);
            }
        });*/
            var proids=getZmProid();
            var wiid =  "${wiid!}";
            //var printUrl = "${bdcdjUrl!}/bdcPrint/spfMulPrint?wiid=" + wiid +"&zslx=zmd&hiddeMode=true";
            var printUrl = "${bdcdjUrl!}/bdcPrint/spfMulPrint?wiid=" + wiid +"&zslx=zmd&hiddeMode=false&proids=" + proids;
            window.location.href = "eprt://" + printUrl;
        })

        $("#zsplprint").click(function () {
            var proids=getZsProid();
            var wiid =  "${wiid!}";
            //var printUrl = "${bdcdjUrl!}/bdcPrint/spfMulPrint?wiid=" + wiid +"&zslx=zmd&hiddeMode=true";
            var printUrl = "${bdcdjUrl!}/bdcPrint/spfMulPrint?wiid=" + wiid +"&zslx=zs&hiddeMode=false&proids=" + proids;
            window.location.href = "eprt://" + printUrl;
        })

        //打印
        $('#print').click(function(){
            var wiid =  "${wiid!}";
            var zs=$('#zsDataTab').parent().attr('class');
            var scdjxx=$('#zmDataTab').parent().attr('class');
            var type=zs=='active'?'zs':'scdjxx';
            var startNum=$('#startNum').val();
            var endNum=$('#endNum').val();
            var printUrl = "${bdcdjUrl!}/bdcPrint/spfMulPrint?wiid=" + wiid +"&zslx="+type+"&hiddeMode=false&startNum="+startNum+"&endNum="+endNum;
            window.location.href = "eprt://" + printUrl;
        })
        $("#addZsbh").click(function () {
            addZsbh();
        })

    })

    function addZsbh(){
        var wiid = "${wiid!}";
        var remoteUrl="${bdcdjUrl}/zsBhGl/getZsbhByPl";
        $.ajax({
            type: "POST",
            async:true,
            url: remoteUrl,
            data: {wiid:wiid},
            success: function (data) {
                //alert(data);
                refresh();
            },
            error: function (jqXHR, exception) {

            }
        });
    }
    function refresh(){
        var workflowProid = "${workflowProid!}";
        $("#zsData").addClass("active");
        var zssjUrl = "${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=1" + "&bdcdyh=" + $("#zssj_search").val();
        zssjInitTable();
        tableReload("zssj-grid-table", zssjUrl, {username: $("#zssj_search").val()});
    }
    //获取批量打印的proid
    function getZmProid(){
        var checkedVals = new Array();
        $(":checkbox[name='checkboxZmPl']").each(function(){
            if($(this).prop('checked')){
                checkedVals.push($(this).val());
            }
        });
        return checkedVals;
    }
    function getZsProid(){
        var checkedVals = new Array();
        $(":checkbox[name='checkboxZsPl']").each(function(){
            if($(this).prop('checked')){
                checkedVals.push($(this).val());
            }
        });
        return checkedVals;
    }
    //选择数据的事件
    function sel(proid, bdcdyid,fzlx) {
        if (proid != '' && proid != undefined && bdcdyid != '' && bdcdyid != undefined) {
            var rid = "${rid!}";
            var taskid = "${taskid!}";
            var from = "${from!}";
            var wiid = "${wiid!}";
            var url = "";
            if(fzlx == '2'){
                url = "${bdcdjUrl}/bdcZsResource?from=" + from + "&taskid=" + taskid + "&proid=" + proid + "&wiid=" + wiid + "&rid=" + rid + "&getSimpleCpt=zmd&bdcdyid=" + bdcdyid;
            }else{
                url = "${bdcdjUrl}/bdcZsResource?from=" + from + "&taskid=" + taskid + "&proid=" + proid + "&wiid=" + wiid + "&rid=" + rid + "&getSimpleCpt=zs&bdcdyid=" + bdcdyid;
            }

            $("#iframeSrcUrl").val(url);
            if ($("#ace-settings-box").hasClass("open")) {
                $("#iframe").attr("src", url);
            } else {
                $("#ace-settings-box").click();
            }
        }
    }

    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }


    function zmsjInitTable() {
        var grid_selector = "#zmsj-grid-table";
        var pager_selector = "#zmsj-grid-pager";
        var workflowProid = "${workflowProid!}";
        //resize to fit page size
        /* $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
        });*/
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        $(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=2",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'BDCDYID'},
            colNames:['序列', '坐落', '不动产单元号', "权利人", "定着物面积", '规划用途','PROID', 'ID'],
            colModel:[
                {name:'XL', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="xl" value="" onclick="sel(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYID + '\',\'' + rowObject.FZLX + '\')"/>'
                }
                },
                {name:'ZL', index:'ZL', width:'25%', sortable:false},
                {name:'BDCDYH', index:'BDCDYH', width:'30%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                    var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                    return value;
                }},
                {name:'QLR', index:'QLR', width:'15%', sortable:false},
                {name:'DJZWMJ', index:'DJZWMJ', width:'10%', sortable:false},
                {name:'GHYT', index:'GHYT', width:'10%', sortable:false},
                {name:'PROID', index:'PROID', width:'0%', sortable:false, hidden:true},
                {name:'BDCDYID', index:'BDCDYID', width:'0%', sortable:false, hidden:true}
            ],
            viewrecords:true,
            rowNum:8,
            rowList:[8, 16, 32],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:false,
            rownumWidth:40,
            multiboxonly:false,
            multiselect:false,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    // 异步获取权利人
                    asycAccessQlrByProid($(grid_selector),data.PROID,data.BDCDYID);
                });
//            var rowIds = $(grid_selector).jqGrid('getDataIDs');
//            for(var k=0; k<rowIds.length; k++) {
//                var curRowData = $(grid_selector).jqGrid('getRowData', rowIds[k]);
//                var curChk = $("#" + rowIds[k] + "").find(":checkbox");
//                curChk.attr('name', 'checkboxZmPl');   //给每一个checkbox赋名字
//                curChk.attr('value', curRowData['PROID']);   //给checkbox赋值
//            }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }

    function asycAccessQlrByProid(table,proid,rowid){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl!}/bdcZsResource/getBdcQlr?proid=" +proid,
            success: function (result) {
                table.setCell(rowid, "QLR", result);
            }
        });
    }

    function zssjInitTable() {
        var grid_selector = "#zssj-grid-table";
        var pager_selector = "#zssj-grid-pager";
        var workflowProid = "${workflowProid!}";
        //resize to fit page size
        /* $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
        });*/
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        $(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcZsResource/getZmdData?workflowProid=" + workflowProid + "&fzlx=1",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'BDCDYID'},
            colNames:['序列', '坐落', '不动产单元号', "权利人", "定着物面积", '规划用途','证书编号','PROID', 'ID'],
            colModel:[
                {name:'XL', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="xl" value="" onclick="sel(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYID + '\',\'' + rowObject.FZLX + '\')"/>'
                }
                },
                {name:'ZL', index:'ZL', width:'25%', sortable:false},
                {name:'BDCDYH', index:'BDCDYH', width:'30%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                    var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                    return value;
                }},
                {name:'QLR', index:'QLR', width:'15%', sortable:false},
                {name:'DJZWMJ', index:'DJZWMJ', width:'10%', sortable:false},
                {name:'GHYT', index:'GHYT', width:'10%', sortable:false},
                {name:'BH', index:'BH', width:'10%', sortable:false},
                {name:'PROID', index:'PROID', width:'0%', sortable:false, hidden:true},
                {name:'BDCDYID', index:'BDCDYID', width:'0%', sortable:false, hidden:true}
            ],
            viewrecords:true,
            rowNum:8,
            rowList:[8, 16, 32],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:false,
            rownumWidth:40,
            multiboxonly:false,
            multiselect:false,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
//            var rowIds = $(grid_selector).jqGrid('getDataIDs');
//            for(var k=0; k<rowIds.length; k++) {
//                var curRowData = $(grid_selector).jqGrid('getRowData', rowIds[k]);
//                var curChk = $("#" + rowIds[k] + "").find(":checkbox");
//                curChk.attr('name', 'checkboxZsPl');   //给每一个checkbox赋名字
//                curChk.attr('value', curRowData['PROID']);   //给checkbox赋值
//            }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
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
    function showSearchDialog(){
        $('#sfSearchPop').show();
    }

    function closeSearchDialog(){
        $('#sfSearchPop').hide();
        $('#startNum').val('');
        $('#endNum').val('');
    }

</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
    <#--图形-->
        <div class="ace-settings-container" id="ace-settings-container">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
                <i class="ace-icon fa fa-globe blue bigger-200"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
            </div>
        </div>

        <div class="row">
            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li class="active">
                        <a data-toggle="tab" id="zmDataTab" href="#zmData">
                            不动产首次信息表
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" id="zsDataTab" href="#zsData">
                            不动产权证书
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="zmData" class="tab-pane in active">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="zmsj_search" data-watermark="请输入不动产单元号">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="zmSearch">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>

                                    <td class="print">
                                        <button id="" onclick="showSearchDialog()" class="btn01 AdvancedButton" value="批量打印">批量打印</button>
                                    </td>

                                    <td class="print">
                                        <button id="tzprint" class="btn01 AdvancedButton" value="台账打印">台账打印</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <table id="zmsj-grid-table"></table>
                        <div id="zmsj-grid-pager"></div>
                    </div>
                    <div id="zsData" class="tab-pane">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="zssj_search" data-watermark="请输入不动产单元号">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="zsSearch">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>

                                    <td class="print">
                                        <button id="" onclick="showSearchDialog()" class="btn01 AdvancedButton" value="批量打印">批量打印</button>
                                    </td>
                                    <td class="distance"></td>
                                    <td style="border: 0px">
                                        <button id="addZsbh" class="btn01 AdvancedButton" value="获取证书编号">获取证书编号</button>
                                    </td>
                                </tr>

                            </table>
                        </div>
                        <table id="zssj-grid-table"></table>
                        <div id="zssj-grid-pager"></div>
                    </div>

                </div>
            </div>

        </div>

    </div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="sfSearchPop">
    <div class="modal-dialog ywsjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>批量打印</h4>
                <button type="button" id="searchHide"  onclick="closeSearchDialog()"class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="sfSearchForm">
                    <div class="row">
                        <div class="col-xs-4">
                            <input type="text" name="start" id ="startNum"class="form-control">
                        </div>
                        <div class="col-xs-1">
                            <label>:</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="end" id ="endNum" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="print">打印</button>
            </div>
        </div>
    </div>
</div>

<input type="hidden" id="iframeSrcUrl">
<div style="display: none">
    <ul>
        <#list proidList as proids>
            <li>
            ${proids}
            </li>
        </#list>
    </ul>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
