<@com.html title="不动产登记业务管理系统" import="ace,public">
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
		width: 100% !important;
		margin: 0px 5px 0px 0px
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
		var Url = "${bdcdjUrl}/bdcqsdj/getbdcCfPagesJsonace?" + $("#gjSearchForm").serialize();
		tableReload("grid-table", Url, {dcxc:""});
	})
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

	//zwq 回车键搜索
	$('input').focus(function(){
		$('#search_xm').keydown(function (event) {
			if (event.keyCode == 13) {
				serch();
			}
		});
		$('#search_sfzh').keydown(function (event) {
			if (event.keyCode == 13) {
				serch();
			}
		});
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
		url:"${bdcdjUrl}/bdcqsdj/getQlrJsonace",
		datatype:"json",
		height:'auto',
		jsonReader:{id:'BDCDYID'},
		colNames:['不动产单元号', '权利人', '坐落', '证书编号','查看','BDCDYID'],
		colModel:[
			{name:'BDCDYH', index:'BDCDYH', width:'25%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                var value = cellvalue.substr(0,6)+" "+cellvalue.substr(6,6)+" "+cellvalue.substr(12,7)+" "+cellvalue.substr(19);
                return value;
            }},
			{name:'QLR', index:'QLR', width:'15%', sortable:false},
			{name:'ZL', index:'ZL', width:'20%', sortable:false},
			{name:'ZS', index:'ZS', width:'20%', sortable:false},
			{name:'CK', index:'', width:'3%', sortable:false, formatter:function (cellvalue, options, rowObject) {
				return '<div style="margin-left:8px;">' +
						' <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="donebdcqsdj(\'' + rowObject.BDCDYID + '\',\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>'+
						'</div>'
				}
			},
			{name:'BDCDYID', index:'BDCDYID', width:'0', sortable:false, hidden:true}
		],
		viewrecords:true,
		rowNum:10,
		rowList:[10, 20, 30],
		pager:pager_selector,
		pagerpos:"left",
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
			var jqData = $(grid_selector).jqGrid("getRowData");
			$.each(jqData, function (index, data) {
                getBdcdyhxx(data.BDCDYID, $(grid_selector), data.BDCDYID);
            });
		},
		//ondblClickRow:function (rowid) {
		//	EditXm(rowid);
		//},
		onCellSelect:function (rowid) {

		},
		editurl:"", //nothing is saved
		caption:"",
		autowidth:true
	});
});


function openWin(url, name) {
	var w_width = screen.availWidth - 10;
	var w_height = screen.availHeight - 32;
	window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

function donebdcqsdj(bdcdyid,bdcdyh) {
	openWin("${reportUrl!}/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/bdc_qscxjgzm&op=write&cptName=bdc_qscxjgzm&bdcdyh="+bdcdyh+"&bdcdyid="+bdcdyid);
}
//xiejianan 获取不动产单元的不动产类型，权利人，坐落，权利状态信息
function getBdcdyhxx(bdcdyid,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcqsdj/getBdcdyhxx?bdcdyid=" + bdcdyid,
        dataType:"json",
        success:function (result) {
            table.setCell(rowid, "ZS", result.ZS);
            table.setCell(rowid, "QLR", result.QLR);
            table.setCell(rowid, "ZL", result.ZL);
        }
    });
}

function EditXm(rowid) {
	var grid_selector = "#grid-table";
	var rowdate=jQuery(grid_selector).jqGrid('getRowData',rowid);
	var proid=rowdate.PROID;
	var qlrid=rowdate.QLRID;
	window.open("${reportUrl!}/ReportServer?reportlet=com.fr.function.AnthorCpt&cpturl=/edit/bdc_qscxjgzm&op=write&cptName=bdc_qscxjgzm&proid=" + proid +"&qlrid="+qlrid);
}

/* 调用子页面方法  */
function showModal() {

	var frame = window.parent;
	while (frame != frame.parent) {
		frameframe = frame.parent;
	}
	frame.postMessage("childCall", "*");
}

function serch() {
	var xm = $("#search_xm").val();
	var zs = $("#search_zs").val();
	var zl = $("#search_zl").val();
	var Url = "${bdcdjUrl}/bdcqsdj/getQlrJsonace";
	tableReload("grid-table", Url, {xm:xm,zs:zs,zl:zl});
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
	document.getElementById()
}


</script>

<div class="space-10"></div>
<div class="main-container">
	<div class="page-content">
		<div class="simpleSearch">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td style="border: 0px">
						<label class="ace-icon " style="width:70px">权利人：</label>
					</td>
					<td>
					<input type="text" class="input-icon" id="search_xm" style="height: 35px;border:0px solid;line-height:35px"
						   data-watermark="请输入权利人姓名">
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td style="border: 0px">
						<label class="ace-icon " style="width:70px">坐落：</label>
					</td>
					<td>
						<input type="text" class="input-icon" id="search_zl" style="height: 35px;border:0px solid;line-height:35px"
							   data-watermark="请输入坐落">
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td style="border: 0px">
						<label class="ace-icon " style="width:70px">所有权证：</label>
					</td>
					<td>
						<input type="text" class="input-icon" id="search_zs" style="height: 35px;border:0px solid;line-height:35px"
							   data-watermark="请输所有权证">
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td class="Search">
						<a href="#" id="search" onclick="serch()">
							搜索
							<i class="ace-icon fa fa-search bigger-130"></i>
						</a>
					</td>
				</tr>
			</table>
		</div>
		<!-- 搜索结果 -->
		<table id="grid-table"></table>
		<div id="grid-pager"></div>
	</div>
</div>


<object id="Capture" style="width: 100%;height: 100%;border: 5 gray solid;display: none"
		classid="clsid:9A73DB73-2CA3-478D-9A3F-7E9D6A8D327C" codebase="CaptureVideo.cab#version=1,1,1,9">
	<embed></embed>
</object>


<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>