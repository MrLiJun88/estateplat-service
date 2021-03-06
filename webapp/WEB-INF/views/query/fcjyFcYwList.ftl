<@com.html title="房产交易房产业务查询" import="ace,public">
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
		var gzdh = $.trim($("#gzdh").val());
        if (gzdh == null || gzdh =='') {
			tipInfo("告知单号不能为空！");
			return false;
		}
		var Url = "${bdcdjUrl}/fcjyFcYw/queryFcjyFcYwList";
       	tableReload("grid-table", Url, {gzdh:gzdh});
    });
	    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

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
        datatype:"json",
        height:'auto',
        jsonReader:{id:'UUID'},
        colNames:["受理号",'行政编号','交易大类','交易小类','交易事项','告知单号','案件状态','查看'],
        colModel:[
			{name:'SLH', index:'SLH', width:'15%', sortable:false},
			{name:'XZBH', index:'XZBH', width:'23%', sortable:false},
			{name:'JYDL', index:'JYDL', width:'20%', sortable:false},
			{name:'JYXL', index:'JYXL', width:'13%', sortable:false},
			{name:'JYSX', index:'JYSX', width:'5%', sortable:false},
			{name:'GZDH',inde:'GZDH',width:'5%',sortable:false},
			{name:'AJZT',inde:'AJZT',width:'5%',sortable:false},
			{name:'ck', index:'', width:'5%', sortable:true, formatter:function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:5px;"> <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="openDetails(\'' + rowObject.SLH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-80 blue"></span></div>' +
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
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            }, 0);
            },
        editurl:"", //nothing is saved
      	caption:"",
        autowidth:true
    });
     });
function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
function openDetails(slh){
    	var url='${reportUrl!}/ReportServer?reportlet=edit%2Ffcjy_gzd.cpt&op=write&slh='+slh;
        openWin(encodeURI(url));
}

</script>
<div class="space-10"></div>
<div class="main-container">
	<div class="page-content">
		<div class="simpleSearch">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td style="border:0px">
						<label class="ace-icon " style="width:100px">告知单号：</label>
					</td>
					<td>
					<input type="text" class="input-icon" id="gzdh" style="height: 35px;border:0px solid;line-height:35px;width:250px"
						   data-watermark="请输入告知单号">
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td class="Search" style="border:0px">
						<a href="#" class="search" id="search">
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
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
