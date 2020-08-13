<@com.html title="查封登记查询" import="ace,public">
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
        width: 750px;
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
        width:95% !important;
        margin: 0px 5px 0px 5px;
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

    /*日期表单样式*/
    .dropdown-menu {
        z-index: 10000 !important;
    }
    .input-icon {
        width: 100%;
    }
</style>
<script type="text/javascript">
    $(function () {
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
        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcCf/queryCfdjList?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:""});
        })
        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
        });

        //自动解封
        $("#autoJf").click(function(){
            $("input[name='cfjsjsqx']").datepicker('setDate',new Date());
            $("select[name='qszt']").val(0);
            $("#gjSearchBtn").click();
        })
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
            url:"${bdcdjUrl}/bdcCf/queryCfdjList",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'QLID'},
            colNames:["业务号", '查封机构', '被执行人','查封开始期限','查封结束期限','登记时间','登簿人','解封登记时间','解封登簿人','权属状态', '解封'],
            colModel:[
                {name:'YWH', index:'YWH', width:'15%', sortable:false},
                {name:'CFJG', index:'CFJG', width:'13%', sortable:false},
                {name:'BZXR', index:'BZXR', width:'8%', sortable:false},
                {name:'CFKSQX', index:'CFKSQX', width:'8%', sortable:false ,formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data
                }},
                {name:'CFJSQX', index:'CFJSQX', width:'8%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
                {name:'DJSJ', index:'DJSJ', width:'8%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
                {name:'DBR', index:'DBR', width:'8%', sortable:false},
                <#--{name:'JFSJ', index:'JFSJ', width:'8%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},-->
                {name:'JFDJSJ', index:'JFDJSJ', width:'8%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
                {name:'JFDBR', index:'JFDBR', width:'8%', sortable:false},
                {name:'QSZT', index:'QSZT', width:'8%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    var value = cellvalue;
                     if(value==0){
                         return '<span class="label label-warning">办理中</span>';
                     }else if(value==1){
                         return '<span class="label label-success">正常</span>';
                     }else if(value==2){
                         return '<span class="label label-danger">注销</span>';
                     }
//                    return value;
                }},
                {name:'ck', index:'', width:'8%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    var value="";
                    if(rowObject.QSZT==1){
                        if(new Date(rowObject.CFJSQX).getTime()<=new Date().getTime()){
                            value ='<div style="margin-left:8px;"> <div title="解封" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="doneCf(\'' + rowObject.QLID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-unlock  bigger-120 blue"></span></div></div>';
                        }
                    }
                    return value;
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
                }, 0);
            },
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    });
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+":this.getMonth() + 1, //月份
            "d+":this.getDate(), //日
            "h+":this.getHours(), //小时
            "m+":this.getMinutes(), //分
            "s+":this.getSeconds(), //秒
            "q+":Math.floor((this.getMonth() + 3) / 3), //季度
            "S":this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    function openWin(url,name){
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, name, "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    function doneCf(qlid) {
        showConfirmDialog("提示信息", "是否解封?", "cfjf", "'" + qlid + "'", "", "");
    }
    var cfjf = function (qlid) {
        $.ajax({
            url:"${bdcdjUrl!}/bdcCf/jf?qlid=" + qlid,
            type: "GET",
            async: false,
            success: function (data) {
                alert(data);
                $("#grid-table").trigger("reloadGrid");//重新加载JqGrid
            }
        });
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
        var Url = "${bdcdjUrl}/bdcCf/queryCfdjList";
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
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入业务号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="serch()">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
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
                    <#list bdcCfGjssOrderList as bdcCfGjss>
                        <#if bdcCfGjss == 'djsjBegin'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登记时间(起)：</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="djqssj"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='djsjEnd'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登记时间(至)：</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="djjssj"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='jfdjsjBegin'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>解封登记时间(起):</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="jfqssj"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='jfdjsjEnd'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>解封登记时间(至):</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="jfjssj"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='cfjssjBegin'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>查封结束期限(起):</label>
                            </div>
                            <div class="col-xs-4">
                             <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="cfqsjsqx"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='cjjsqxEnd'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>查封结束期限(至):</label>
                            </div>
                            <div class="col-xs-4">
                            <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="cfjsjsqx"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                            </span>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='dbr'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>登簿人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="dbr" class="form-control">
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='jfdbr'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>解封登簿人：</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="jfdbr" class="form-control">
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='qszt'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>权属状态:</label>
                            </div>
                            <div class="col-xs-4">
                                <select name="qszt" class="form-control">
                                    <option value="">请选择</option>
                                    <option value="1">正常</option>
                                    <option value="2">注销</option>
                                </select>
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='ywh'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>业务号:</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="ywh" class="form-control">
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        <#elseif   bdcCfGjss=='bzxr'>
                            <#if (bdcCfGjss_index + 1) % 2 != 0>
                            <div class="row">
                            </#if>
                            <div class="col-xs-2">
                                <label>被执行人:</label>
                            </div>
                            <div class="col-xs-4">
                                <input type="text" name="bzxr" class="form-control">
                            </div>
                            <#if (bdcCfGjss_index + 1) % 2 == 0>
                            </div>
                            </#if>

                        </#if>
                    </#list>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
                <button type="button" class="btn btn-sm btn-success" id="autoJf">自动解封</button>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
