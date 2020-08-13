<@com.html title="不动产登记业务管理系统" import="ace,public">
<style type="text/css">
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
    //table每页行数
    $rownum = 8;
    //table 每页高度
    $pageHight = '300px';
    //多选数据
    $mulData=new Array();
    $mulRowid=new Array();
    $(function () {
        Array.prototype.remove = function(index) {
            if (index > -1) {
                this.splice(index, 1);
            }
        };
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
        //下拉框
        $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据", width:"100%"});
        $(window).on('resize.chosen',function () {
            $.each($('.chosen-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var w = $(obj).parent().width();
                $(obj).next().css("width", w);
            })
        }).trigger('resize.chosen');
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        // 回车键搜索
        $('input').focus(function(){
            $('#search_xmmc').keydown(function (event) {
                if (event.keyCode == 13) {
                }
            });
        });

        /*   文字水印  */
        $(".watermarkText").watermark();
        //搜索事件
        $("#search").click(function () {
            var hhSearch = $("#search_xmmc").val();
            var Url = "${bdcdjUrl}/bdcFwfsss/getGdFwForSyqJsonByPage";
            tableReload("grid-table", Url, {hhSearch:hhSearch},'');
        })

        //搜索事件
        $("#djsj_search_btn").click(function () {
            var djsjUrl = "${bdcdjUrl}/bdcFwfsss/getDjsjBdcdyPagesJson";
            tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
        })
        $("#ywsj_search_btn").click(function () {
            var ywsjUrl ="${bdcdjUrl}/bdcFwfsss/getGdFwForSyqJsonByPage";
            tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
        })

        $("#djsjTab,#ywsjTab").click(function () {
            if (this.id == "djsjTab") {
                $("#djsj").addClass("active");
                //var djsjUrl= "${bdcdjUrl}/bdcFwfsss/getDjsjBdcdyPagesJson";
                djsjInitTable();
                //tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
            } else if (this.id == "ywsjTab") {
                $("#ywsj").addClass("active");
                //var ywsjUrl ="${bdcdjUrl}/bdcFwfsss/getGdFwForSyqJsonByPage";
                ywsjInitTable();
                //tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
            }
        });

        //两种类型选择的确定按钮
        $("#djsjSure,#ywsjSure").click(function () {
            var wiid = $("#wiid").val();
            var bdcdyid = $("#bdcdyid").val();
            if (this.id == "djsjSure") {
                if ($mulData.length == 0) {
                    tipInfo("请至少选择一条数据！");
                    return;
                }
                var djids = "";
                var bdcdyhs = "";
                for (var i = 0; i < $mulData.length; i++) {
                    if (djids != null && djids != "")
                        djids = djids + "," + $mulData[i].ID;
                    else
                        djids = $mulData[i].ID;
                    if (bdcdyhs != null && bdcdyhs != "")
                        bdcdyhs = bdcdyhs + "," + $mulData[i].BDCDYH;
                    else
                        bdcdyhs = $mulData[i].BDCDYH;
                }
                var check=checkZfqlzt(wiid,bdcdyid);
                if(check=="true"){
                    var msg = "附属设施是否随同主房抵押或者查封!";
                    showConfirmDialog("提示信息", msg, "djsjEditXm", "'" + djids + "','" + bdcdyhs + "','" + "true" + "'", "djsjEditXm", "'" + djids + "','" + bdcdyhs + "','" + "false" + "'");
                }else{
                    djsjEditXm(djids, bdcdyhs,"false");
                }
            } else if (this.id == "ywsjSure") {
                if ($mulData.length == 0) {
                    tipInfo("请至少选择一条数据！");
                    return;
                }
                var fwids = "";
                var qlids = "";
                for (var i = 0; i < $mulData.length; i++) {
                    if (fwids != null && fwids != "")
                        fwids = fwids + "," + $mulData[i].FWID;
                    else
                        fwids = $mulData[i].FWID;
                    if (qlids != null && qlids != "")
                        qlids = qlids + "," + $mulData[i].QLID;
                    else
                        qlids = $mulData[i].QLID;
                }
                var check=checkZfqlzt(wiid,bdcdyid);
                if(check=="true"){
                    var msg = "附属设施是否随同主房抵押或者查封!";
                    showConfirmDialog("提示信息", msg, "ywsjEditXm", "'" + fwids + "','" + qlids + "','" + "true" + "'", "ywsjEditXm", "'" + fwids + "','" + qlids + "','" + "false" + "'");
                }else{
                    ywsjEditXm(fwids, qlids,"false");
                }
            }
        });
        //默认初始化表格
        ywsjInitTable();
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
            jsonReader: {id: 'ID'},
            colNames: ['地籍号', '不动产单元号', '坐落', '权利人', '不动产类型', 'ID'],
            colModel: [
                {name: 'DJH', index: 'DJH', width: '18%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '15%', sortable: false},
                {name: 'TDZL', index: 'TDZL', width: '20%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                {
                    name: 'BDCLX',
                    index: 'BDCLX',
                    width: '15%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        var value = "";
                        if (cellvalue != null && cellvalue != "") {
                                if (cellvalue.indexOf('FW') > -1) {
                                    value = "土地、房屋等建筑物";
                                }
                        }
                        return value;
                    }
                },
                {name: 'ID', index: 'ID', width: '10%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: true,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    var replacement =
                    {
                        'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
                    };
                    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                        var icon = $(this);
                        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                    })
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);

                //addDjhForTable(grid_selector);

                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                for(var i=0;i<=$mulRowid.length;i++){
                    $(grid_selector).jqGrid('setSelection',$mulRowid[i]);
                }
                //赋值数量
                $("#djsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            },
            onSelectAll: function(aRowids,status){
                var $myGrid = $(this);
                //aRowids.forEach(function(e){
                $.each(aRowids,function(i,e){
                    var cm = $myGrid.jqGrid('getRowData', e);
                    //判断是已选择界面还是原界面
                    if(cm.ID==e) {
                        var index = $.inArray(e, $mulRowid);
                        if (status && index < 0) {
                            $mulData.push(cm);
                            $mulRowid.push(e);
                        } else if (!status && index >= 0) {
                            $mulData.remove(index);
                            $mulRowid.remove(index);
                        }
                    }
                })
                //赋值数量
                $("#djsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            },
            onSelectRow:function(rowid,status){
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData',rowid);
                //判断是已选择界面还是原界面
                if(cm.ID==rowid){
                    var index=$.inArray(rowid,$mulRowid);
                    if(status && index<0){
                        $mulData.push(cm);
                        $mulRowid.push(rowid);
                    }else if(!status && index>=0){
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                    //赋值数量
                    $("#djsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
                }
            },
            editurl:"",
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
            jsonReader:{id:'FWID'},
            colNames: ['序列', '房屋坐落', '所在层', "总层数", '建筑面积', /*'QLID', 'GDPROID',*/ 'FWID'],
            colModel:[
                {name: 'XL', index: 'XL', width: '3%', sortable: false, formatter:function (cellvalue, options, rowObject) {
                    return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '</span>'
                }},
                {name: 'FWZL', index: 'FWZL', width: '20%', sortable: false},
                {name: 'SZC', index: 'SZC', width: '10%', sortable: false},
                {name: 'ZCS', index: 'ZCS', width: '10%', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '15%', sortable: false},
/*                {name: 'QLID', index: 'ZSID', width: '0%', sortable: false, hidden: true},
                {name: 'GDPROID', index: 'ZSID', width: '0%', sortable: false, hidden: true},*/
                {name: 'FWID', index: 'ZSID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: true,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    var replacement =
                    {
                        'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
                    };
                    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                        var icon = $(this);
                        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                    })
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);

                //addDjhForTable(grid_selector);

                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                var rowIds = $(grid_selector).jqGrid('getDataIDs');

                for(var i=0;i<=$mulRowid.length;i++){
                    $(grid_selector).jqGrid('setSelection',$mulRowid[i]);
                }
                //赋值数量
                $("#ywsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            },
            onSelectAll: function(aRowids,status){
                var $myGrid = $(this);
                //aRowids.forEach(function(e){
                $.each(aRowids,function(i,e){
                    var cm = $myGrid.jqGrid('getRowData', e);
                    //判断是已选择界面还是原界面
                    if(cm.QLID==e) {
                        var index = $.inArray(e, $mulRowid);
                        if (status && index < 0) {
                            $mulData.push(cm);
                            $mulRowid.push(e);
                        } else if (!status && index >= 0) {
                            $mulData.remove(index);
                            $mulRowid.remove(index);
                        }
                    }
                })
                //赋值数量
                $("#ywsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            },
            onSelectRow:function(rowid,status){
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData',rowid);
                //判断是已选择界面还是原界面
                if(cm.FWID==rowid){
                    var index=$.inArray(rowid,$mulRowid);
                    if(status && index<0){
                        $mulData.push(cm);
                        $mulRowid.push(rowid);
                    }else if(!status && index>=0){
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                    //赋值数量
                    $("#ywsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
                }
            },
            editurl:"",
            caption:"",
            autowidth:true
        });
    }

    function openWin(url, name) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }

    //open新窗口
    function addOrUpdate(url) {
        openWin(url);
    }



    /* 调用子页面方法  */
    function showModal() {

        var frame = window.parent;
        while (frame != frame.parent) {
            frameframe = frame.parent;
        }
        frame.postMessage("childCall", "*");
    }


    function tableReload(table, Url, data,loadComplete) {
        var jqgrid = $("#" + table);
        if (loadComplete != '') {
            jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data,loadComplete: loadComplete});
        }else{
            jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        }
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
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140',
            'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    //修改项目信息的函数
    function djsjEditXm(djids,bdcdyhs,qlFlag){
        var msg = "附属设施面积是否累加发证面积？";
        showConfirmDialog("提示信息", msg, "addDjsjBdcFsss", "'" + djids + "','" + bdcdyhs + "','" + qlFlag + "','" + '1' + "'", "addDjsjBdcFsss", "'" + djids + "','" + bdcdyhs + "','" + qlFlag + "','" + '0' + "'");
    }

    //关联增加附属设施
    function addDjsjBdcFsss(djids,bdcdyhs,qlFlag,jrfzmj) {
        var wiid = $("#wiid").val();
        var bdcdyh = $("#bdcdyh").val();
        var bdcdyid = $("#bdcdyid").val();
        $.blockUI({message: "请稍等……"});
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcFwfsss/addBdcFsss",
            data: {
                djids: djids,
                bdcdyhs: bdcdyhs,
                qlFlag: qlFlag,
                wiid: wiid,
                bdcdyh: bdcdyh,
                bdcdyid: bdcdyid,
                jrfzmj: jrfzmj
            },
            dataType: "json",
            success: function (result) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (result.msg == "success") {
                    tipInfo("关联成功");
                    window.returnValue = "1";
                    window.close();
                } else {
                    tipInfo(result.msg);
                }
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                tipInfo("关联失败，请联系管理员！");
            }
        });
    }


    //修改项目信息的函数
    function ywsjEditXm(fwids,qlids,qlFlag){
        var msg = "附属设施面积是否累加发证面积？";
        showConfirmDialog("提示信息", msg, "addGdsjBdcFsss", "'" + fwids + "','" + qlids + "','" + qlFlag + "','" + '1' + "'", "addGdsjBdcFsss", "'" + fwids + "','" + qlids + "','" + qlFlag + "','" + '0' + "'");
    }

    //关联增加附属设施
    function addGdsjBdcFsss(fwids,qlids,qlFlag,jrfzmj) {
        var wiid =$("#wiid").val();
        var bdcdyh =$("#bdcdyh").val();
        var bdcdyid =$("#bdcdyid").val();
        $.blockUI({ message:"请稍等……" });
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcFwfsss/addGdFsss",
            data:{fwids:fwids,qlids:qlids,qlFlag:qlFlag,wiid:wiid,bdcdyh:bdcdyh,bdcdyid:bdcdyid,jrfzmj:jrfzmj},
            dataType: "json",
            success: function (result) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (result.msg == "success") {
                    tipInfo("关联成功");
                    window.returnValue="1";
                    window.close();
                }else{
                    tipInfo(result.msg);
                }

            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                tipInfo("关联失败，请联系管理员！");
            }
        });
    }

    //检查主房不动产状态
    function checkZfqlzt(wiid,bdcdyid){
        var check='';
        $.ajax({
            url: "${bdcdjUrl}/bdcFwfsss/checkZfqlzt?wiid="+wiid+"&bdcdyid=" + bdcdyid,
            type: 'GET',
            async:false,
            success: function (result) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(result)) {
                    if(result.qlzt!='no'){
                        check='true';
                    }
                }
            },
            error: function (data) {
                tipInfo("检查失败");
            }
        });
        return check;
    }
</script>
<div class="main-container">
    <input type="hidden" id="wiid" value="${wiid!}">
    <input type="hidden" id="bdcdyh" value="${bdcdyh!}">
    <input type="hidden" id="bdcdyid" value="${bdcdyid!}">
    <div class="page-content">
        <div class="row">
            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li  class="active">
                        <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                            过渡房屋
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" id="djsjTab" href="#djsj">
                            不动产单元
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="ywsj" class="tab-pane in active  ">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="ywsj_search"
                                               data-watermark="请输入房产证号/原证号/权利人/坐落">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="ywsj_search_btn"">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="tableHeader">
                            <ul>
                                <li>
                                    <button type="button" id="ywsjSure">
                                        <i class="ace-icon fa fa-file-o"></i>
                                        <span>关联</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" id="ywsjMulXx">
                                        <span>已选择</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                        <!-- 搜索结果 -->
                        <table id="ywsj-grid-table"></table>
                        <div id="ywsj-grid-pager"></div>
                    </div>
                    <div id="djsj" class="tab-pane">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="djsj_search"
                                               data-watermark="请输入不动产单元号/权利人/坐落">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="djsj_search_btn"">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="tableHeader">
                            <ul>
                                <li>
                                    <button type="button" id="djsjSure">
                                        <i class="ace-icon fa fa-file-o"></i>
                                        <span>关联</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" id="djsjMulXx">
                                        <span>已选择</span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                        <!-- 搜索结果 -->
                        <table id="djsj-grid-table"></table>
                        <div id="djsj-grid-pager"></div>
                    </div>
                </div>
            </div>
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