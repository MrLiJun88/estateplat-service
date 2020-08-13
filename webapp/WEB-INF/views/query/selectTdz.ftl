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

    $("#djsjMulXx").click(function () {
        $("#modal-backdrop-mul").show();
        var table="";
        if (this.id == "djsjMulXx") {
            $("#djsjMulTable").show();
            djsjInitTable("djsj-mul");
            table="djsj-mul-grid-table";
        }
        $("#"+table).jqGrid("clearGridData");
        for(var i=0;i<=$mulData.length;i++){
            $("#"+table).jqGrid('addRowData',i+1,$mulData[i]);
        }
    });



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

    $("#djsjMulCloseBtn").click(function () {
            $("#grid-table").trigger("reloadGrid");
            $("#modal-backdrop-mul").hide();
            $("#djsjMulTable").hide();
    });

    //搜索事件
    $("#search").click(function () {
        var zh =  $("#zh").val();
        var zl =  $("#zl").val();
        var djh =  $("#djh").val();
        var qlr = $("#qlr").val();
        var exactQuery = $("#exactQueryywsj").get(0).checked ? "true" : "false";
        if((zh == "" || zh == null || zh == undefined) && (zl == "" || zl == null || zl == undefined) && (djh == "" || djh == null || djh == undefined) && (qlr == "" || qlr == null || qlr == undefined)){
            tipInfo("请输入土地证号/坐落/地籍号/权利人!");
        }else {
            var Url = "${bdcdjUrl}/glZs/selectTdz";
            tableReload("grid-table", Url, {zh: zh,djh:djh,zl:zl,qlr:qlr,exactQuery:exactQuery}, '');
        }
    })

	jQuery(grid_selector).jqGrid({
		datatype:"local",
		height:'auto',
		jsonReader:{id:'QLID'},
		colNames: [ '地籍号', '坐落', "土地证号", '关联', 'QLID',  'PROID', 'XMLY'],
		colModel:[
			{name: 'DJH', index: '', width: '15%', sortable: false},
			{name: 'ZL', index: '', width: '25%', sortable: false},
			{name: 'ZH', index: '', width: '25%', sortable: false},
			{name:'dr', index:'', width:'5%', sortable:false, formatter:function (cellvalue, options, rowObject) {
				return '<div style="margin-left:8px;">' +
						' <div title="关联" style="float:left;cursor:pointer; margin-left: 8px;" class="ui-pg-div ui-inline-edit" id="" onclick="daoru(\'' + rowObject.QLID + '\',\'' + rowObject.PROID + '\',\'' + rowObject.XMLY + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue "></span></div>' +
						'</div>'
			}
			},
			{name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
			{name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
            {name: 'XMLY', index: 'XMLY', width: '0%', sortable: false, hidden: true}
        ],
		viewrecords:true,
		rowNum:$rownum,
		rowList:[10, 20, 30],
		pager:pager_selector,
		pagerpos:"left",
		pagerpos:"left",
		altRows:false,
		rownumbers:true,
		rownumWidth:50,
        multiboxonly:false,
        multiselect:true,
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
            $("#djsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
			/*$.each(jqData, function (index, data) {
              getjyQlrByQlid(data.YWH, $(grid_selector), rowIds[index]);
			})*/
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getBdcTdXxByProidAndXmly(data.PROID,data.XMLY,data.QLID,$(grid_selector));
            });
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
            $("#djsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
        },
        onSelectRow:function(rowid,status){
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData',rowid);
            //判断是已选择界面还是原界面
            if(cm.QLID==rowid){
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

    $("#tdsjSure").click(function () {
        if($mulData.length==0){
            alert("请至少选择一条数据！");
            return;
        }

        var qlids="";
        var proids="";
        var xmlys="";
        for(var i=0;i<$mulData.length;i++){
            if(qlids!=null && qlids!="")
                qlids=qlids+","+$mulData[i].QLID;
            else
                qlids=$mulData[i].QLID;
            if(proids!=null && proids!="")
                proids=proids+","+$mulData[i].PROID;
            else
                proids=$mulData[i].PROID;
            if(xmlys!=null && xmlys!="")
                xmlys=xmlys+","+$mulData[i].XMLY;
            else
                xmlys=$mulData[i].XMLY;
        }
        gltd(qlids,proids,xmlys);
    });

    $("#djsjDel").click(function () {
        var ids ;
        var table;
        if (this.id == "djsjDel") {
            table="#djsj-mul-grid-table";
        }
        ids = $(table).jqGrid('getGridParam','selarrrow');
        for(var i = ids.length-1;i>-1 ;i--) {
            var cm = $(table).jqGrid('getRowData',ids[i]);
            var index;
            if(this.id == "djsjDel"){
                index=$.inArray(cm.QLID,$mulRowid);
            }
            $mulData.remove(index);
            $mulRowid.remove(index);
            $(table).jqGrid("delRowData", ids[i]);
        }
    });
});

// 异步获取地籍号、坐落、证号
function getBdcTdXxByProidAndXmly(proid,xmly,qlid,table) {
    $.ajax({
        type: "GET",
        url: "${bdcdjUrl!}/glZs/getBdcTdXxByProidAndXmly?proid=" + proid +"&xmly="+xmly,
        success: function (result) {
            if(result!=null && result!=""){
                if(result.djh!="" && result.djh!=null && result.djh!='undefined'){
                    table.setCell(qlid, "DJH", result.djh);
                }
                if(result.zl!="" && result.zl!=null && result.zl!='undefined'){
                    table.setCell(qlid, "ZL", result.zl);
                }
                if(result.zh!="" && result.zh!=null && result.zh!='undefined'){
                    table.setCell(qlid, "ZH", result.zh);
                }
            }
        }
    })
}
//业务数据
function djsjInitTable(tableKey) {
    var grid_selector = "#"+tableKey+"-grid-table";
    var pager_selector = "#"+tableKey+"-grid-pager";
    $('#search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#"+tableKey+"_search_btn").click();
        }
    });
    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', '275px');
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
        jsonReader:{id:'QLID'},
        colNames:["QLID","地籍号", '坐落','土地证号'],
        colModel:[
            {name:'QLID', index:'QLID', width:'0%', hidden:true},
            {name:'DJH', index:'DJH', width:'12%', sortable:false},
            {name:'ZL', index:'ZL', width:'10%', sortable:false},
            {name:'ZH', index:'TDZH', width:'6%', sortable:false}
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
                //$(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
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
            $("#djsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
        },
        onSelectAll: function(aRowids,status){
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids,function(i,e){
                var cm = $myGrid.jqGrid('getRowData',e);
                if(cm.PROID==e) {
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
            if(cm.PROID==rowid) {
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



function daoru(qlid,proid,xmly) {
    gltd(qlid,proid,xmly);
	/*var wiid =$("#wiid").val();
	$.blockUI({ message:"请稍等……" });
	$.ajax({
		type: "GET",
		url: "${bdcdjUrl}/glZs/glYtdz?qlids="+qlid+"&wiid="+wiid+"&proids="+proid+"&xmlys="+xmly,
		dataType: "json",
		success: function (result) {
			//去掉遮罩
			setTimeout($.unblockUI, 10);
			if (result.msg == "success") {
				alert("关联成功");
				window.parent.hideModel();
				window.parent.resourceRefresh();
			}else{
                showConfirmDialog("提示信息", "该产权存在抵押，是否关联？", "glSure","'" + wiid + "'", "", "");
			}

		},
		error: function (data) {
			setTimeout($.unblockUI, 10);
			alert("关联失败，请联系管理员！");
		}
	});*/

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

function serch() {
	var hhSearch = $("#search_xmmc").val();
	var Url = "${bdcdjUrl}/selectJyHth/getJyHthPage";
	tableReload("grid-table", Url, {hhSearch:hhSearch},'');
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
function gltd(qlids,proids,xmlys) {
    var wiid =$("#wiid").val();
    var proid =$("#proid").val();
    $.blockUI({ message:"请稍等……" });
    $.ajax({
        type: "POST",
        url: "${bdcdjUrl}/glZs/validateYtdz",
        data:{qlids:qlids,wiid:wiid,proids:proids,xmlys:xmlys,proid:proid},
        dataType: "json",
        success: function (result) {

            if (result.msg == "") {
                glSure(qlids,proids,xmlys);
            }else{
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if(result.type == "comfirm"){
                    showConfirmDialog("提示信息", result.msg + "，是否关联？", "glSure","'" + qlids +  "','" + proids + "','" + xmlys + "'", "", "");
                }else if(result.type == "alert") {
                    tipInfo(result.msg);
                }
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            tipInfo("关联失败，请联系管理员！");
        }
    });
}
function glSure(qlids,proids,xmlys) {
    $.blockUI({ message:"请稍等……" });
    debugger;
    var wiid =$("#wiid").val();
    var proid =$("#proid").val();

    $.ajax({
        type: "get",
        url: "${bdcdjUrl}/glZs/glYtdz",
        data: {qlids: qlids, wiid: wiid, proids: proids, xmlys: xmlys,proid:proid},
        dataType: "json",
        success: function (result) {
            $.ajax({
                type: 'get',
                async: true,
                url: '${bdcdjUrl}/wfProject/updateWorkFlow?proid='+proid+'&wiid=' + wiid,
                success: function (data) {
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    tipInfo("关联成功!");
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                }
            });
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            tipInfo("关联失败，请联系管理员！");
        }
    });

}
</script>
<input type="hidden" id="wiid" value="${wiid!}">
<input type="hidden" id="proid" value="${proid!}">

<div class="space-10"></div>
<div class="main-container">
	<div class="page-content">
        <form class="form advancedSearchTable" id="ywsjSearchForm">
            <div class="row">
                <div class="col-xs-1" style="width: 80px;text-align: right;">
                    <label>地籍号：</label>
                </div>
                <div class="col-xs-1" style="width: 200px;height: 30px;">
                    <input type="text" id="djh" style="width: 200px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 130px;text-align: right;">
                    <label>土地证号：</label>
                </div>
                <div class="col-xs-1" style="width: 200px;height: 30px;">
                    <input type="text" id="zh" style="width: 200px;height: 30px;">
                </div>
                <div class="col-xs-1" style="width: 90px;text-align: right;">
                    <label>坐落：</label>
                </div>
                <div class="col-xs-1" style="width: 200px;height: 30px;">
                    <input type="text" id="zl" style="width: 200px;height: 30px;">
                </div>
            </div>
            <div class="row">
                <div class="col-xs-1" style="width: 80px;text-align: right;">
                    <label>权利人：</label>
                </div>
                <div class="col-xs-1" style="width: 200px;height: 30px;">
                    <input type="text" id="qlr" style="width: 200px;height: 30px;">
                </div>
            </div>
            <div class="row">
                <div class="col-xs-1" style="width: 270px;">
                    <button type="button" class="btn btn-sm btn-primary"
                            id="search">搜&nbsp;&nbsp;索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </button>
                    <label><input type="checkbox" id="exactQueryywsj"/>精确查询</label>
                </div>
            </div>
        </form>
		<#--<div class="simpleSearch">-->
			<#--<table cellpadding="0" cellspacing="0" border="0">-->
				<#--<tr>-->
					<#--<td>-->
						<#--<input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入地籍号/土地证号/坐落">-->
					<#--</td>-->


					<#--<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>-->
					<#--<td class="Search">-->
						<#--<a href="#" id="search"">-->
							<#--搜索-->
							<#--<i class="ace-icon fa fa-search bigger-130"></i>-->
						<#--</a>-->
					<#--</td>-->
				<#--</tr>-->
			<#--</table>-->
		<#--</div>-->
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="tdsjSure">
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
<!--土地证选择内容查看-->
<div class="Pop-upBox moveModel" style="display: none;" id="djsjMulTable">
    <div class="modal-dialog tipPop-modal mulSelectPop">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">已选择的土地证</h4>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div class="tableHeader">
                    <ul>
                        <li>
                            <button type="button" id="djsjDel">
                                <i class="ace-icon fa fa-file-o"></i>
                                <span>删除</span>
                            </button>
                        </li>
                    </ul>
                </div>
                <table id="djsj-mul-grid-table"></table>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="djsjMulCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop-mul"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>