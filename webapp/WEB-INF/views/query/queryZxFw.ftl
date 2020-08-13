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

    .mulSelectPop {
        width: 975px;
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


        /*	// 回车键搜索
            $('input').focus(function(){
                $('#search_xmmc').keydown(function (event) {
                    if (event.keyCode == 13) {
                        serch();
                    }
                });
            });*/


        //搜索事件
        $("#search").click(function () {
            var hhSearch = $("#search_xmmc").val();
            var Url = "${bdcdjUrl}/queryZxFw/getDyFwPagesJson?dyid=${dyid!}";
            tableReload("grid-table", Url, {hhSearch:hhSearch},'');
        })


        //查看界面关闭按钮
        $("#ywsjMulCloseBtn").click(function () {
            $("#grid-table").trigger("reloadGrid");
            $("#modal-backdrop-mul").hide();
            $("#ywsjMulTable").hide();
        });

        $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

        //确定按钮
        $("#ywsjSure,#ywsjMulSureBtn").click(function () {
            if($mulData.length==0){
                alert("请至少选择一条数据！");
                return;
            }

            var fwids="";
            for(var i=0;i<$mulData.length;i++){
                fwids+=$mulData[i].FWID + "${splitStr!}";
            }
            ywsjEditXm(fwids);

        });

        //已选按钮
        $("#ywsjMulXx").click(function () {
            if($mulData.length==0){
                alert("请至少选择一条数据！");
                return;
            }
            $("#modal-backdrop-mul").show();
            var table="";
            $("#ywsjMulTable").show();
            ywsjInitTable("ywsj-mul");
            table="ywsj-mul-grid-table";
            $("#"+table).jqGrid("clearGridData");
            for(var i=0;i<=$mulData.length;i++){
                $("#"+table).jqGrid('addRowData',i+1,$mulData[i]);
            }
        });

        //删除按钮
        $("#ywsjDel").click(function () {
            var ids ;
            var table;
            table="#ywsj-mul-grid-table";
            ids = $(table).jqGrid('getGridParam','selarrrow');
            for(var i = ids.length-1;i>-1 ;i--) {
                var cm = $(table).jqGrid('getRowData',ids[i]);
                var index;
                index=$.inArray(cm.FWID,$mulRowid);
                $mulData.remove(index);
                $mulRowid.remove(index);
                $(table).jqGrid("delRowData", ids[i]);
            }
        });

    });


    $(function(){

        Array.prototype.remove = function (index) {
            if (index > -1) {
                this.splice(index, 1);
            }
        };


        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

        /*   文字水印  */
        $(".watermarkText").watermark();
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
        ywsjInitTable("ywsj-mul");

        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/queryZxFw/getDyFwPagesJson?dyid=${dyid!}",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'FWID'},
            colNames:["fwid",'房屋坐落', '他项证号','所在层','总层数', '建筑面积', 'DYID'],
            colModel:[
                {name:'FWID', index:'FWID', width:'0%', hidden:true},
                {name:'FWZL', index:'FWZL', width:'30%', sortable:false},
                {name:'DYDJZMH', index:'DYDJZMH', width:'15%', sortable:false},
                {name:'SZC', index:'SZC', width:'10%', sortable:false},
                {name:'ZCS', index:'ZCS', width:'6%', sortable:false},
                {name:'JZMJ', index:'JZMJ', width:'6%', sortable:false},
                {name:'DYID', index:'DYID', width:'0%', hidden:true}
            ],
            viewrecords:true,
            rowNum:7,
            rowList:[7, 15, 20],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:false,
            multiselect:true,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "275px");
                }
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
                    var cm = $myGrid.jqGrid('getRowData',e);
                    if(cm.FWID==e) {
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
                if(cm.FWID==rowid) {
                    var index = $.inArray(rowid, $mulRowid);
                    if (status && index < 0) {
                        $mulData.push(cm);
                        $mulRowid.push(rowid);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                    //赋值数量
                    $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
                }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    });



    //业务数据
    function ywsjInitTable(tableKey) {
        var grid_selector = "#"+tableKey+"-grid-table";
        var pager_selector = "#"+tableKey+"-grid-pager";

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
            url:"${bdcdjUrl}/queryZxFw/getDyFwPagesJson?dyid=${dyid!}",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'FWID'},
            colNames:["fwid",'房屋坐落', '他项证号','所在层','总层数', '建筑面积', 'DYID'],
            colModel:[
                {name:'FWID', index:'FWID', width:'0%', hidden:true},
                {name:'FWZL', index:'FWZL', width:'30%', sortable:false},
                {name:'DYDJZMH', index:'DYDJZMH', width:'15%', sortable:false},
                {name:'SZC', index:'SZC', width:'10%', sortable:false},
                {name:'ZCS', index:'ZCS', width:'6%', sortable:false},
                {name:'JZMJ', index:'JZMJ', width:'6%', sortable:false},
                {name:'DYID', index:'DYID', width:'0%', hidden:true}
            ],
            viewrecords:true,
            rowNum:7,
            rowList:[7, 15, 20],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:false,
            multiselect:true,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "275px");
                }
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
                    var cm = $myGrid.jqGrid('getRowData',e);
                    if(cm.FWID==e) {
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
                if(cm.FWID==rowid) {
                    var index = $.inArray(rowid, $mulRowid);
                    if (status && index < 0) {
                        $mulData.push(cm);
                        $mulRowid.push(rowid);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                    //赋值数量
                    $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
                }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }



    //修改项目信息的函数
    function ywsjEditXm(ids) {
        var proid="";
        var wiid="";
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        if ($("#wiid").val() != '') {
            wiid = $("#wiid").val();
        }
        $.blockUI({ message:"请稍等……" });
        var options = {
            url:'${bdcdjUrl}/queryZxFw/changeQszt?proid='+proid+"&wiid="+wiid+"&ids="+ids+"&xmly=3",
            type:'post',
            dataType:'json',
            success:function (data) {
                setTimeout($.unblockUI, 10);
                if(data.msg == "success"){
                    alert("注销成功");
                }else{
                    alert("注销失败");
                }
                $(".mulSelectPop").parent().hide();
                window.parent.hideModel();
                window.parent.resourceRefresh();
            },
            error:function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                alert("注销失败");
                $("#modal-backdrop-mul").hide();
            }
        };
        $.ajax(options);
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
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140',
            'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }


</script>
<input type="hidden" id="proid" value="${proid!}">
<input type="hidden" id="dyid" value="${dyid!}">
<input type="hidden" id="wiid" value="${wiid!}">
<div class="space-10"></div>
<div class="main-container">
    <div class="page-content">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入他项证证号/坐落">
                    </td>


                    <td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
                    <td class="Search">
                        <a href="#" id="search"">
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
                        <span>确定</span>
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
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
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
<!--选择内容查看-->
<div class="Pop-upBox moveModel gjSearchPop-modal" style="display: none;" id="ywsjMulTable">
    <div class="modal-dialog tipPop-modal mulSelectPop">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">已选择的房屋</h4>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div class="tableHeader">
                    <ul>
                        <li>
                            <button type="button" id="ywsjDel">
                                <i class="ace-icon fa fa-file-o"></i>
                                <span>删除</span>
                            </button>
                        </li>
                    </ul>
                </div>
                <table id="ywsj-mul-grid-table"></table>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="ywsjMulSureBtn">确定</button>
                <button type="button" class="btn btn-sm btn-primary" id="ywsjMulCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>