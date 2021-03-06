<@com.html title="不动产登记业务管理系统" import="ace">
<style type="text/css">
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
        width;100% !important;
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
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
    $(function () {
        try{
            Capture = document.getElementById("Capture");//根据js的脚本内容，必须先获取object对象
            content=$("#search_xmmc");
        }catch(err){
            alert("请安装Active X控件CaptureVideo.cab");
        }
        /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject){
            if(ua.match(/msie ([\d.]+)/)[1]=='8.0'){
                $(window).resize(function(){
                    $.each($(".moveModel > .modal-dialog"),function(){
                        $(this).css("left",($(window).width()-$(this).width())/2);
                        $(this).css("top","40px");
                    })
                })
            }
        }
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcDjb/getbdcDjbPagesJsonace?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:""});

        })

        $('#search_xmmc').keydown(function (event) {
            if (event.keyCode == 13) {
               serch();
            }
        });

        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        });
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

        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcDjb/getbdcDjbPagesJsonace",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'djbid'},
            colNames:["宗地/宗海号", '登簿人', '坐落', '查看'],
            colModel:[
                {name:'zdzhh', index:'zdzhh', width:'35%', sortable:false},
                {name:'dbr', index:'dbr', width:'20%', sortable:false},
                {name:'zl', index:'zl', width:'35%', sortable:false},
                {name:'ck', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;"> <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="doneCf(\'' + rowObject.djbid + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>' +
                            '</div>'
                }
                }
            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:true,
            rownumWidth:50,
            loadComplete:function () {
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
    });
    function openWin(url,name){
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, name, "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    function doneCf(a){
        openWin('${bdcdjUrl!}/bdcDjb/djb?djbid='+ a);
    }

    function EditXm(djbid){
        window.open('${bdcdjUrl}/bdcDjb/djb?djbid='+djbid);
    }

    /* 调用子页面方法  */
    function showModal(){

        var frame = window.parent;
        while(frame != frame.parent) {
            frameframe=frame.parent;
        }
        frame.postMessage("childCall", "*");
    }

    function serch(){
        var dcxc=$("#search_xmmc").val();
        var Url = "${bdcdjUrl}/bdcDjb/getbdcDjbPagesJsonace";
        tableReload("grid-table", Url, {dcxc:dcxc});
    }

    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
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
<div class="space-10"></div>
<div class="main-container">
    <div class="page-content">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入宗地/宗海号/登簿人/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="serch()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" onclick="openDevices_onclick(0)" >扫描</button>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <!--end  高级搜索 -->
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

                    <#list bdcdjbGjssOrderList as bdcdjbGjss>
                        <#if bdcdjbGjss == 'bdclx'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产类型：</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="bdclx" class="form-control" id="bdclxSel">
                                    <option></option>
                                    <#list bdcList as bdclx>
                                        <option value="${bdclx.DM}">${bdclx.MC}</option>
                                    </#list>
                                </select>
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjbGjss=='bdcdyh'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>不动产单元号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bdcdyh" class="form-control">
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjbGjss=='zl'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>坐落：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zl" class="form-control">
                            </div>
                            <div class="col-xs-2">
                            </div>
                            <div class="col-xs-4">
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjbGjss=='zdzdh'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>宗地宗海号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zdzhh" class="form-control">
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjbGjss=='dbr'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登簿人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="dbr" class="form-control">
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcdjbGjss=='zdh'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>宗地号：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="zdh" class="form-control">
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif bdcdjbGjss='xqmc'>
                            <#if (bdcdjbGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>小区名称：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="fwmc" class="form-control">
                            </div>
                            <#if (bdcdjbGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>
                        </#if>
                    </#list>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<object id="Capture" style ="width: 100%;height: 100%;border: 5 gray solid;display: none" classid="clsid:9A73DB73-2CA3-478D-9A3F-7E9D6A8D327C" codebase="CaptureVideo.cab#version=1,1,1,9">
    <embed></embed>
</object>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
