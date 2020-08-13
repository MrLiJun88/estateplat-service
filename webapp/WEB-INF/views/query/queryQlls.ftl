<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    .modal-dialog {
        width: 1000px;
    }

        /*高级搜索样式添加 begin*/
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
    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;100% !important;
        margin: 0px 5px 0px 0px;
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
        /*高级搜索样式添加 end*/
    /*.ui-jqgrid .ui-jqgrid-bdiv {*/

        /*overflow-x: hidden;*/
    /*}*/
</style>
<script type="text/javascript">
    $(function () {
        //项目表搜索事件
        $("#search").click(function () {
            var xmmc = $("#search_xmmc").val();
            $("#gjSearchForm")[0].reset();
            var Url = "${bdcdjUrl}/queryQlls/queryQllxByPageJsonace?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:xmmc});
        });

        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/queryQlls/queryQllxByPageJsonace?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:""});
        });
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
        });
    });
    /* 调用子页面方法  */
    function showModal() {
        $('#myModal').show();
        $('#modal-backdrop').show();
    }
    function hideModal() {
        $('#myModal').hide();
        $('#modal-backdrop').hide();
        $("#myModalFrame").attr("src", "${bdcdjUrl!}/bdcSjgl/toAddBdcxm");
    }
    var onmessage = function (e) {
        showModal();
    };

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

    $(function () {
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
            url:"${bdcdjUrl}/queryQlls/queryQllxByPageJsonace",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'PROID'},
            colNames:["权利人", '不动产单元号', '权利类型', '申请类型','不动产权证号', '坐落', '查看'],
            colModel:[
                {name:'QLR', index:'QLR', width:'6%', sortable:false},
                {name:'BDCDYH', index:'BDCDYH', width:'12%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                    var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                    return value;
                }},
                {name:'QLLX', index:'QLLX', width:'9%', sortable:false},
                {name:'SQLX', index:'SQLX', width:'12%', sortable:false},
                {name:'BDCQZH', index:'BDCQZH', width:'12%', sortable:false},
                {name:'ZL', index:'ZL', width:'10%', sortable:false},
                {name:'mydy', index:'', width:'3%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    return '<a href="#" title="查看"><div style="margin-left:8px;"> <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></div></div></a>'
                }
                }
            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:true,
            multiselect:false,
            /*rownumbers:true,*/
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                }, 0);
            },
            ondblClickRow:function (id) {
                EditXm(id);
            },
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
        $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
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
    });

    //修改项目信息的函数
    function EditXm(proid, bdcdyh) {
        if (proid != "") {
            var proid = encodeURI(encodeURI(proid));
            if (bdcdyh != null && bdcdyh != '') {
                var bdcdyh = encodeURI(encodeURI(bdcdyh));
            } else{
                bdcdyh='';
            }
            var url = "${bdcdjUrl}/qllxResource/djbQllxByProid?proid=" + proid + "&bdcdyh=" + bdcdyh;
        <#--var url="${bdcdjUrl}/qllxResource?proid="+proid;-->
            openWin(url);
        }
    }
    function openWin(url,name){
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, name, "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入权利人/申请类型/权利类型/不动产单元号/不动产权证号/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>

        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>申请类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="sqlx" class="form-control">
                                <option></option>
                                <#list sqlxList as sqlx>
                                    <option value="${sqlx.dm}">${sqlx.mc}</option>
                                </#list>
                            </select>

                        </div>
                        <div class="col-xs-2">
                            <label>不动产权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcqzh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>权利类型：</label>
                        </div>
                        <div class="col-xs-4">
                            <select name="qllx" class="form-control">
                                <option></option>
                                <#list qllxList as qllx>
                                    <option value="${qllx.dm}">${qllx.mc}</option>
                                </#list>
                            </select>
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
