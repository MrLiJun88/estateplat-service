<@com.html title="不动产单元查询" import="ace,public">
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
        width;
        100% !important;
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
        /*重写下拉列表高度*/
    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
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
    //项目表搜索事件
    $("#search").click(function () {
        var xmmc = $("#search_xmmc").val();
     	$("#qlrs").val('');
     	$("#bdcdyhs").val('');
     	$("#bdcqzhs").val('');
     	$("#sqlxs").val('');
     	$("#ghyts").val('');
    	$("#qszts").val('');
     	$("#djlxs").val('');
     	$("#fzrqs").val('');
        $("#dcxcs").val($.trim(xmmc));
        $("#gjSearchForm")[0].reset();
        var Url = "${bdcdjUrl}/queryYFcxx/initList?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:xmmc});
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
    	var qlr =$("#qlr").val();
        $("#qlrs").val($.trim(qlr));
        var fczh=$("#fczh").val();
        $("#fczhs").val($.trim(fczh));
        var ghyt=$("#ghyt").val();
        $("#ghyts").val($.trim(ghyt));
        var fwjg=$("#fwjg").val();
        $("#fwjgs").val($.trim(fwjg));
        var fwzl=$("#fwzl").val();
        $("#fwzls").val($.trim(fwzl));
        var jzmj=$("#jzmj").val();
        $("#jzmjs").val($.trim(jzmj));
        $("#dcxcs").val('');
        var Url = "${bdcdjUrl}/queryYFcxx/initList?" + $("#gjSearchForm").serialize();
        tableReload("grid-table", Url, {dcxc:""});
    });
    
    //时间控件
			$('.date-picker').datepicker({
				autoclose : true,
				todayHighlight : true,
				language : 'zh-CN'
			}).next().on(ace.click_event, function() {
				$(this).prev().focus();
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
        $(".chosen-select").trigger('chosen:updated');
    });
    //项目高级查询按钮点击事件
    $("#show").click(function () {
        $("#gjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });
});
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
        url:"${bdcdjUrl}/queryYFcxx/initList",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'FCZH'},
        colNames:[ '权利人','房产证号', '房屋坐落','规划用途' ,'房屋结构', '建筑面积','套内建筑面积','分摊建筑面积','原权证号','是否注销','明细'],
        colModel:[
            {name:'QLR', index:'QLR', width:'13%', sortable:false},
            {name:'FCZH', index:'FCZH', width:'13%', sortable:false},
            {name:'FWZL', index:'FWZL', width:'13%', sortable:false},
            {name:'GHYT', index:'GHYT', width:'10%', sortable:false},
            {name:'FWJG', index:'FWJG', width:'10%', sortable:false },
            {name:'JZMJ', index:'JZMJ', width:'10%', sortable:false},
            {name:'TNJZMJ', index:'TNJZMJ', width:'5%', sortable:false},
            {name:'FTJZMJ', index:'FTJZMJ', width:'8%', sortable:false },
            {name:'YQZH', index:'YQZH', width:'8%', sortable:false},
            {name:'ISZX', index:'ISZX', width:'7%', sortable:false,
            		formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue && cellvalue != "" && cellvalue == 1) {
                            return '<span class="label label-warning">已注销</span>';
                        } else {
                            return '<span class="label label-success">未注销</span>';
                        }
                    }
            },
            {name:'ck', index:'', width:'0%', sortable:false,
            		formatter:function (cellvalue, options, rowObject) {
                        return '<a href="#" title="明细"><div style="margin-left:8px;"> <div title="明细" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="details(\'' + rowObject.FCZH  +'\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div></div></a>'
                    },hidden:true
            }
        ],
        viewrecords : true,
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pagerpos : "left",
		pager : pager_selector,
		altRows : false,
		multiboxonly : true,
		multiselect : true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            }, 0);
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                //getBdcdyhQlxx(data.BDCDYH, $(grid_selector), data.BDCDYH);
                getGdfwxx(data.FCZH, $(grid_selector), data.FCZH);
            })
        },
        ondblClickRow: function (rowid) {
        	//双击查看详情事件
        	//detail(rowid);
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
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
});

function detail(rowid){
	openWin("${reportUrl!}/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/zhjg/gdfwsyqxx&op=write&cptName=gdfwsyqxx&fczh="+rowid );
}
function Trim(str, is_global) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (is_global.toLowerCase() == "g") {
        result = result.replace(/\s/g, "");
    }
    return result;
}
//点击定位按钮，展示定位图
function locate(id, bdcdyh) {
    if (bdcdyh != "") {
        var url = "${bdcdjUrl}/bdzDyMap?proid=&bdcdyh=" + bdcdyh;
        openWin(url)
//        window.open(url);
    }
}


//xiejianan 获取不动产单元的不动产类型，权利人，坐落，权利状态信息
function getGdfwxx(fczh,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/queryYFcxx/getgdfwxxbyfczh?time="+new Date(),
        data:{fczh:fczh},
        dataType:"json",
        success:function (result) {
            table.setCell(rowid, "QLR", result.QLR);
            table.setCell(rowid, "FWZL", result.FWZL);
            table.setCell(rowid, "GHYT", result.GHYT);
            table.setCell(rowid, "FWJG", result.FWJG);
            table.setCell(rowid, "JZMJ", result.JZMJ);
            table.setCell(rowid, "TNJZMJ", result.TNJZMJ);
            table.setCell(rowid, "FTJZMJ", result.FTJZMJ);
            table.setCell(rowid, "YQZH", result.YQZH);
            table.setCell(rowid, "ISZX", result.ISZX);
        }
    });
}

//点击证书关系
function hisRel( bdcdyh) {
    if (bdcdyh != "") {
//        var url = "/server/bdcZsRel?proid=" + id + "&bdcdyh=" + bdcdyh;
        var url = "/workflowPlugin/index.html?url=" + "${bdcdjUrl!}" + "/bdcZsRel/getDyAllXmRelXml?bdcdyh=" + bdcdyh;
        openWin(url);
    }
}
function details(fczh){
	var url=encodeURI("${bdcdjUrl!}"+"/queryYFcxx/getDetailsByFczh?fczh="+fczh);
	openWin(url);
}
function exports(){
    <#-- var dxcm =$("#dcxcs").val();
     var qlr=$("#qlrs").val();
     var bdcdyh=$("#bdcdyhs").val();
     var bdcqzh=$("#bdcqzhs").val();
     var sqlx=$("#sqlxs").val();
     var ghyt=$("#ghyts").val();
     var qszt=$("#qszts").val();
     var djlx=$("#djlxs").val();
     var fzrq=$("#fzrqs").val();-->
     var records=$("#grid-table").jqGrid('getGridParam','records');
     if(records==0){
     	tipInfo("没有可供导出的记录");
     }else if(records > 6000){
     	tipInfo("导出的记录数过大！");
     }else{
 	 	var url="${reportUrl!}/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/print/fccxxxExport&op=write&cptName=fccxxxExport&"+$("#form").serialize();
 	 	//tipInfo(url);
 	 	openWin(url);
 	 }
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入房产证号/权利人/房屋坐落">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
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
		<div class="tableHeader">
            <ul><#--
                <li>
                    <button type="button" id="add">
                        <i class="glyphicon glyphicon-plus"></i>
                        <span>增加</span>
                    </button>
                </li>
                 <li>
                    <button type="button" id="edit">
                        <i class="glyphicon glyphicon-edit"></i>
                        <span>修改</span>
                    </button>
                </li>
                 <li>
                    <button type="button" id="delete">
                        <i class="glyphicon glyphicon-remove "></i>
                        <span>删除</span>
                    </button>
                </li>-->
                 <li>
                    <button type="button" id="export" onclick="exports()">
                        <i class="glyphicon glyphicon-export"></i>
                        <span>导出</span>
                    </button>
                </li>
            </ul>
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
                            <input type="text" id="qlr" name="qlr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>房产证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id="fczh" name="fczh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>规划用途：</label>
                        </div>
                        <div class="col-xs-4">
                             <input type="text" id="ghyt" name="ghyt" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>房屋结构：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id="fwjg" name="fwjg" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>房屋坐落：</label>
                        </div>
                        <div class="col-xs-4">
                               <input type="text" id="fwzl" name="fwzl" class="form-control">
                        </div>
                        <div class="col-xs-2">
                          <label>建筑面积：</label>
                        </div>
                        <div class="col-xs-4">
                          <input type="text" id="jzmj" name="jzmj" class="form-control">
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
<form id="form" hidden="hidden">
    <input type="hidden" id="dcxcs" name="dcxcs">
    <input type="hidden" id="qlrs" name="qlrs">
    <input type="hidden" id="fczhs" name="fczhs">
    <input type="hidden" id="ghyts" name="ghyts">
    <input type="hidden" id="fwjgs" name="fwjgs">
    <input type="hidden" id="fwzls" name="fwzls">
    <input type="hidden" id="jzmjs" name="jzmjs">
</form>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
