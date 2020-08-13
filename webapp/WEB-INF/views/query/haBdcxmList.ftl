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
<script src="${bdcdjUrl}/static/js/readCard.js"></script>
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
     	$("#gjSearchForm")[0].reset();
        var Url = "${bdcdjUrl}/haBdcxm/getBdcxmByPagesJson?" + $("#gjSearchForm").serialize();
        var dcxc=$("#search_xmmc").val()
        tableReload("grid-table", Url, {dcxc:dcxc});
    });

    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
    	var	date1=$("#qsrq").val();
    	var	date2=$("#jsrq").val();
    	var d1 = new Date(date1.replace(/\-/g, "\/"));
		var d2 = new Date(date2.replace(/\-/g, "\/"));
		if (date1 != "" && date2 != "" && d1 >= d2) {
			tipInfo("开始时间不能大于结束时间！");
			return false;
		}
        var Url = "${bdcdjUrl}/haBdcxm/getBdcxmByPagesJson?" + $("#gjSearchForm").serialize();
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
        url:"${bdcdjUrl}/haBdcxm/getBdcxmByPagesJson",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'PROID'},
        colNames:[ '受理编号','权利人',"权利人证件号", '不动产权证','坐落' ,'房屋建筑面积','流程名称','权属状态','查看','PROID'],
        colModel:[
            {name:'SLBH', index:'SLBH', width:'15%', sortable:false},
            {name:'QLR', index:'QLR', width:'10%', sortable:false},
            {name:'QLRZJH', index:'QLRZJH', width:'20%', sortable:false},
            {name:'BDCQZH', index:'BDCQZH', width:'20%', sortable:false},
            {name:'ZL', index:'ZL', width:'15%', sortable:false},
            {name:'FWJZMJ', index:'FWJZMJ', width:'10%', sortable:false},
            {name:'LCMC', index:'LCMC', width:'10%', sortable:false },
            {name:'QSZT', index:'QSZT', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue && cellvalue != "0") {
                    return"";
                }
                var xmzt = rowObject.QSZT;
                if (xmzt == '现势')
                    return '<span class="label label-success">' + xmzt + '</span>&nbsp;';
                else if (xmzt == '历史')
                    return '<span class="label label-gray">' + xmzt + '</span>&nbsp;';
                else if (xmzt == '临时')
                    return '<span class="label label-warning">' + xmzt + '</span>&nbsp;';
               else if (xmzt == '终止')
                   return '<span class="label label-danger">' + xmzt + '</span>&nbsp;';
            }},
            {name:'CK', index:'CK', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<div style="margin-left:8px;">' +
                        '<div title="查看" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="dianjick(\'' + rowObject.PROID + '\',\'' + rowObject.PROID + '\',\'' + rowObject.QSZT + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-file-text fa-lg red"></span></div>' +
                        '<div title="上下手关系" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="sxsgx(\'' + rowObject.PROID + '\',\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="glyphicon glyphicon-resize-vertical"></span></div>' +
                        '</div>'
            }
            },
            {name:'PROID', index:'PROID', width:'10%', sortable:false,hidden:true}
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
                getYtdqlxx(data.PROID, $(grid_selector), data.PROID);
            })
        },
		 /* ondblClickRow:function (rowid, index) {
            Exim(rowid);
        },*/
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
//二次查询
		function getYtdqlxx(proid, table, rowid) {
			$.ajax({
				type : "GET",
				url : "${bdcdjUrl}/haBdcxm/getHaBdcxmQlxx?proid=" + proid + "&time="
						+ new Date(),
				dataType : "json",
				success : function(result) {
					table.setCell(rowid, "QLR", result.QLRMC);
					table.setCell(rowid, "BDCQZH", result.BDCQZH);
					table.setCell(rowid, "ZL", result.ZL);
					table.setCell(rowid, "QLRZJH", result.QLRZJH);
					table.setCell(rowid, "LCMC", result.LCMC);
					table.setCell(rowid, "FWJZMJ", result.JZMJ);
				}
			});
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

//高拍仪
function readCard(){
    ReadIDCardNew($('#cqrxm'),$('#cqrzjh'));
}
function Exim(id) {
    if (id != "") {
        var tdid = encodeURI(encodeURI(id));
        var url = Url="${reportUrl}/ReportServer?reportlet=com.fr.function.AnthorCpt"+"&cpturl=/edit/bdc_cxjgzmy&op=write&cptName=bdc_cxjgzmy&tdid="+tdid;
        openWin(url);
    }
}
//上下手关系
function sxsgx(rowid,proid) {
    if (proid != "") {
        var proid = encodeURI(encodeURI(proid));
        var url = Url="${reportUrl}/ReportServer?reportlet=com.fr.function.AnthorCpt"+"&cpturl=/print/bdc_zslsgx&op=write&cptName=bdc_zslsgx&proid="+proid;
        openWin(url);
    }
}
//查看
function dianjick(rowid,proid,qszt) {
    if (proid != "") {
        var proid = encodeURI(encodeURI(proid));
        if(qszt=='现势'){
        var url = Url="${reportUrl}/ReportServer?reportlet=com.fr.function.AnthorCpt"+"&cpturl=/print/bdc_qszm&cptName=bdc_qszm&proid="+proid;
        openWin(url);
        }else{
         var url = Url="${reportUrl}/ReportServer?reportlet=com.fr.function.AnthorCpt"+"&cpturl=/print/bdc_qszm_read&cptName=bdc_qszm_read&proid="+proid;
        	openWin(url);
        }
    }
}
//点击证书关系
function hisRel( bdcdyh) {
    if (bdcdyh != "") {
//        var url = "/server/bdcZsRel?proid=" + id + "&bdcdyh=" + bdcdyh;
        var url = "/workflowPlugin/index.html?url=" + "${bdcdjUrl!}" + "/bdcZsRel/getDyAllXmRelXml?bdcdyh=" + bdcdyh;
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
    <input type="hidden" id="proid" value="${proid!}">

    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入不动产单元号/坐落/权利人名称/证件号" onkeypress='if(event.keyCode==13) $("#search").click()'>
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
                            <label>不动产单元号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="bdcdyh" id="bdcdyh" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" id="zl" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>产权人姓名：</label>
                        </div>
                        <div class="col-xs-4">
                             <input type="text" name="cqrxm" id="cqrxm" class="form-control" onclick="javascript:readCard()" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
                        </div>
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cqrzjh" id="cqrzjh" class="form-control" onclick="javascript:readCard()" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
                        </div>
                    </div>
                  <div class="row">
                        <div class="col-xs-2">
                            <label>发证起始日期：</label>
                        </div>
                        <div class="col-xs-4">
                        	 <span class="input-icon">
                           <input type="text" class="date-picker form-control" name="qsrq" id="qsrq"
                                data-date-format="yyyy-mm-dd" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
                        	<i class="ace-icon fa fa-calendar"></i></span>
                        </div>
                        <div class="col-xs-2">
                          <label>发证结束日期：</label>
                        </div>
                        <div class="col-xs-4">
                         <span class="input-icon">
                        <input type="text" class="date-picker form-control" name="jsrq" id="jsrq"
                                data-date-format="yyyy-mm-dd">
                        	<i class="ace-icon fa fa-calendar" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'></i>
                        	</span>
                        </div>
                    </div>
                              <div class="row">
                        <div class="col-xs-2">
                            <label>受理编号：</label>
                        </div>
                        <div class="col-xs-4">
                               <input type="text" name="slbh" id="slbh" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
                        </div>
                       <#--   <div class="col-xs-2">
                            <label>发证日期：</label>
                        </div>
                        <div class="col-xs-4">
                        	 <span class="input-icon">
                           <input type="text" class="date-picker form-control" name="fzrq" id="fzrq"
                                data-date-format="yyyy-mm-dd">
                        	<i class="ace-icon fa fa-calendar"></i></span>
                        </div>-->
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn" >搜索</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
