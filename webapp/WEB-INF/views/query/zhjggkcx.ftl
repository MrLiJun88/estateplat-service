<@com.html title="不动产登记业务管理系统" import="ace,public">
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

        /*高级搜索样式添加 end*/
    /*.ui-jqgrid .ui-jqgrid-bdiv {*/

        /*overflow-x: hidden;*/
    /*}*/

        /*重写下拉列表高度*/
    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
$(function () {
    //下拉框
    $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据", width:"100%"});
    $(window).on('resize.chosen',function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
    }).trigger('resize.chosen');

    try {
        Capture = document.getElementById("Capture");//根据js的脚本内容，必须先获取object对象
        content = $("#search_xmmc");
    } catch (err) {
        alert("请安装Active X控件CaptureVideo.cab");
    }
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


    $('#search_xmmc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#search").click();
        }
    });


	$('.date-picker').datepicker({
				autoclose : true,
				todayHighlight : true,
				language : 'zh-CN'
			}).next().on(ace.click_event, function() {
				$(this).prev().focus();
	});
    //项目表搜索事件
    $("#search").click(function () {
    	check(this.id);
        <#--var xmmc = $("#search_xmmc").val();
        var fanwei=$("#fanweiform").serialize();
        if(fanwei==''){
        	tipInfo("请选择查询范围：土地信息、房屋信息、登记信息");
        }else if(xmmc==''){
        	tipInfo("请输入搜索条件");
        }else if(fanwei=='fanwei=td&fanwei=fw&fanwei=dj'){
        	bootbox.dialog({
	            message:"查询范围：土地信息、房屋信息、登记信息全选后查询速度较慢，是否继续查询？",
	            title:"",
	            closeButton:false,
	            buttons:{
	                success:{
	                    label:"确定",
	                    className:"btn-success",
	                    callback:function () {
					        $("#gjSearchForm")[0].reset();
					        var Url = "${bdcdjUrl}//zhjggkcx/getZhjgCxxList?time="+new Date()+"&" + $("#gjSearchForm").serialize()+"&" + $("#fanweiform").serialize();
					        tableReload("grid-table", Url, {dcxc:xmmc});
	                    }
	                },
	                main:{
	                    label:"取消",
	                    className:"btn-primary",
	                    callback:function () {
	                        return;
	                    }
	                }
	            }
	        });
        }else{
	        $("#gjSearchForm")[0].reset();
	        var Url = "${bdcdjUrl}//zhjggkcx/getZhjgCxxList?time="+new Date()+"&" + $("#gjSearchForm").serialize()+"&" + $("#fanweiform").serialize();
	        //tableReload("grid-table", Url, {dcxc:xmmc});
	    }-->
    });

	function Search(){
		var xmmc = $("#search_xmmc").val();
		$("#gjSearchForm")[0].reset();
		var Url = "${bdcdjUrl}//zhjggkcx/getZhjgCxxList?time="+new Date()+"&" + $("#gjSearchForm").serialize()+"&" + $("#fanweiform").serialize();
		tableReload("grid-table", Url, {dcxc:xmmc});
		
	}
	function gjSearch(){
		var	date1=$("#fzqssj").val();
	    var	date2=$("#fzjssj").val();
	    var d1 = new Date(date1.replace(/\-/g, "\/"));
		var d2 = new Date(date2.replace(/\-/g, "\/"));
		if (date1 != "" && date2 != "" && d1 >= d2) {
			tipInfo("开始时间不能大于结束时间！");
			return false;
		}
	    var Url = "${bdcdjUrl}/zhjggkcx/getZhjgCxxList?time="+new Date()+"&" + $("#gjSearchForm").serialize()+"&" + $("#fanweiform").serialize();
	    tableReload("grid-table", Url, {dcxc:""});
	}
    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function () {
    	check(this.id);
    });
    
    var op="false";
    function check(id){
        if($("#fanweiform").serialize()==''){
        	tipInfo("请选择查询范围：土地信息、房屋信息、登记信息");
        	return op;
        }
        if(id=='search' && $("#search_xmmc").val()==''){
        	tipInfo("请输入搜索条件");
        	return op;
        }else if(id=='gjSearchBtn' && $("#gjSearchForm").serialize().length==46){
        	tipInfo("请输入搜索条件");
        	return op;
        }
        if($("#fanweiform").serialize().length==29){
        	bootbox.dialog({
	            message:"查询范围：土地信息、房屋信息、登记信息全选后查询速度较慢，是否继续查询？",
	            title:"",
	            closeButton:false,
	            buttons:{
	                success:{
	                    label:"确定",
	                    className:"btn-success",
	                    callback:function () {
		                    op="true";
		                    if(id=='search'){
		                    	Search();
		                    }else{
		                    	gjSearch();
		                    }
		                    return op;
	                    }
	                },
	                main:{
	                    label:"取消",
	                    className:"btn-primary",
	                    callback:function () {
	                        return op;
	                    }
	                }
	            }
	        });
        }
        else{
        	op="true";
		    if(id=='search'){
		    	Search();
		    }else{
		    	gjSearch();
		    }
		    return op;
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
//        var grid_selector = "#grid-table";
//        var pager_selector = "#grid-pager";
//
//        //resize to fit page size
//        $(window).on('resize.jqGrid', function () {
//            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
//        });
//        //resize on sidebar collapse/expand
//        var parent_column = $(grid_selector).closest('[class*="col-"]');
//        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
//            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
//                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
//            }
//        });
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
        url:"${bdcdjUrl}/zhjggkcx/getZhjgCxxList",
        datatype:"local",
        height:'auto',
        jsonReader:{id:'ZSID'},
        colNames:['不动产单元号','权利人', '坐落', '不动产权证号/不动产证明号','发证日期','证书状态','权利状态','备注', '查看', 'ID','BDCDYID','ZSLX','ZSTYPE'],
        colModel:[
            {name:'BDCDYH', index:'BDCDYH', width:'15%', sortable:false,formatter:function (cellvalue, options, rowObject) {
            	if(cellvalue !=null && cellvalue != ''){
                var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);}
                else 
                value='';
                return value;
            }},
            {name:'QLR', index:'QLR', width:'10%', sortable:false},
            {name:'ZL', index:'ZL', width:'10%', sortable:false},
            {name:'BDCQZH', index:'BDCQZH', width:'15%', sortable:false},
            {name:'FZRQ', index:'FZRQ', width:'10%', sortable:false},
            {name:'QSZT', index:'QSZT', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue && cellvalue != "0") {
                    return"";
                }
                var value = cellvalue;
                $.each(${qsztListJson!}, function (i, item) {
                    if (item.dm == cellvalue) {
                        value = item.mc;
                    }
                })
                if(value=='现势')
                    return '<span class="label label-success">' + value + '</span>&nbsp;';
                else if(value=='历史')
                    return '<span class="label label-gray">' + value + '</span>&nbsp;';
                else if(value=='临时')
                    return '<span class="label label-warning">' + value + '</span>&nbsp;';
            }},
            {name:'QLZT', index:'QLZT', width:'5%', sortable:false},
            {name:'LY', index:'LY', width:'5%', sortable:false},
            {name:'mydy', index:'', width:'3%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                return '<div style="margin-left:8px;">' +
                        '<div title="收件材料" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="LookFile(\'' + rowObject.ZSID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' +
                        '</div>'
            }
            },
            {name:'ZSID', index:'ZSID', width:'10%', sortable:false, hidden:true},
            {name:'BDCDYID', index:'BDCDYID', width:'10%', sortable:false, hidden:true},
            {name:'ZSLX', index:'ZSLX', width:'3%', sortable:false, hidden:true},
            {name:'ZSTYPE', index:'ZSTYPE', width:'3%', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
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
                getBdcqzQlrxx(data.ZSID,$(grid_selector), data.ZSID);
	            getBdcqzDyCfStatus(data.QSZT,data.ZSLX,data.ZSTYPE,data.ZSID, $(grid_selector), data.ZSID);
            })
        },
        ondblClickRow:function (rowid, index) {
           // dianji(rowid);
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

function LookFile(zsid){
	var url="${reportUrl!}/ReportServer?reportlet=print/bdc_djxxcx_zhjg.cpt&op=write&zsid=" + zsid;
	openWin(encodeURI(url))
}

function Trim(str, is_global) {
    var result;
    result = str.replace(/(^\s+)|(\s+$)/g, "");
    if (is_global.toLowerCase() == "g") {
        result = result.replace(/\s/g, "");
    }
    return result;
}
function getBdcqzDyCfStatus(qszt,zslx,zstype,zsid,table, rowid){
	var cellVal='';
	if (qszt != null && qszt.indexOf("历史") > 0 ){
		cellVal += '<span class="label label-success">正常</span>';
		table.setCell(rowid, "QLZT", cellVal);
	}else if (zslx !=null && (zslx=='17' || zslx=='18' ||zslx=='20' ||zslx=='21')){
		cellVal += qszt;
		table.setCell(rowid, "QLZT", cellVal);
	}else{
		$.ajax({
	        type:"GET",
	        url:"${bdcdjUrl}/zhjggkcx/getDycfXXByZsidZhjg?time="+new Date(),
	        data:{zsid:zsid},
	        dataType:"json",
	        success:function (result) {
	        	cellVal='';
	        	if ('yes'==result.cf) {
	                cellVal += '<span class="label label-danger">查封</span>';
	            }
	            if ('yes'==result.dy) {
	                cellVal += '<span class="label label-warning">抵押</span>';
	            }
	            if ('yes'==result.zx) {
	                cellVal += '<span class="label label-gray">注销</span>';
	            }
	            if ('yes'==result.zc) {
	                cellVal += '<span class="label label-success">正常</span>';
	            }
	            table.setCell(rowid, "QLZT", cellVal);
	        }
	    });
    }
}

function getBdcqzQlrxx(zsid,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/zhjggkcx/getDetailsBYzsidZhjg?time="+new Date(),
        data:{zsid:zsid},
        dataType:"json",
        success:function (result) {
            table.setCell(rowid, "QLR", result.QLR);
            table.setCell(rowid, "BDCDYH", result.BDCDYH);
            table.setCell(rowid, "ZL", result.ZL);
        }
    });
}

function openWin(url, name) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
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
                               data-watermark="请输入权利人/不动产单元号/不动产证明号/不动产权证号/坐落" onkeypress='if(event.keyCode==13) $("#search").click()'>
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" onclick="openDevices_onclick(0)">扫描</button>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>

			<div class="tableHeader">
	            <ul>
	                 <li>
	                 	<form id="fanweiform" >
							<input type="checkbox" name="fanwei" value="td"><i class="glyphicon glyphicon-export"></i>
	                        <span>土地信息</span>
							<input type="checkbox" name="fanwei" value="fw"><i class="glyphicon glyphicon-export"></i>
	                        <span>房产信息</span>
							<input type="checkbox" name="fanwei" value="dj"><i class="glyphicon glyphicon-export"></i>
	                        <span>不动产登记信息</span>
						</form>
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
	                                <input type="text" name="qlr" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
	                            </div>
	                            <div class="col-xs-2">
	                                <label>坐落：</label>
	                            </div>
	                            <div class="col-xs-4">
	                                <input type="text" name="zl" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
	                            </div>
                            </div>
                            <div class="row">
	                            <div class="col-xs-2">
	                                <label>不动产单元号：</label>
	                            </div>
	                            <div class="col-xs-4">
	                                <input type="text" name="bdcdyh" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
	                            </div>
	                            <div class="col-xs-2">
	                                <label>不动产权证号：</label>
	                            </div>
	                            <div class="col-xs-4">
	                                <input type="text" name="bdcqzh" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
	                            </div>
                            </div>
                            <div class="row">
	                            <div class="col-xs-2">
	                            	<label>发证起始日期：</label>
		                        </div>
		                        <div class="col-xs-4">
		                        	 <span class="input-icon">
		                           <input type="text" class="date-picker form-control" name="fzqssj" id="fzqssj"
		                                data-date-format="yyyy-mm-dd" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
		                        	<i class="ace-icon fa fa-calendar"></i></span>
		                        </div>
		                        <div class="col-xs-2">
		                          <label>发证结束日期：</label>
		                        </div>
		                        <div class="col-xs-4">
		                         <span class="input-icon">
		                        <input type="text" class="date-picker form-control" name="fzjssj" id="fzjssj"
		                                data-date-format="yyyy-mm-dd">
		                        	<i class="ace-icon fa fa-calendar" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'></i>
		                        	</span>
		                        </div>
                            </div>
                            <div class="row">
	                            <div class="col-xs-2">
	                                <label>权属状态：</label>
	                            </div>
	                            <div class="col-xs-4">
	                                <select name="qszt" class="form-control" onkeypress='if(event.keyCode==13) $("#gjSearchBtn").click()'>
	                                    <option></option>
	                                    <#list qsztList as qszt>
	                                        <option value="${qszt.dm}">${qszt.mc}</option>
	                                    </#list>
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
<div class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
